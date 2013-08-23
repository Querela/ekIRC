/**
 * IRCDCCFileTransfer.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ekDev
 */
public class IRCDCCFileTransfer implements Runnable
{
    private final IRCDCCManager ircDCCManager;

    private final IRCUser ircUser;
    private final IRCDCCFileTransfer.Direction direction;

    private final String filename;
    private final String address;
    private final int port;
    private final long totalSize;

    // --------------------------------

    private IRCDCCFileTransfer.Status status;
    private File localFile;
    private long size;
    private boolean allowResume;
    private long startTime;
    private long endTime;
    private double transferRate;
    private Thread thread;
    private final static AtomicInteger threadCount = new AtomicInteger();

    // ------------------------------------------------------------------------
    // Incoming file transfers

    public IRCDCCFileTransfer(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser, String filename, String address,
            int port, long size) throws NullPointerException, IllegalArgumentException
    {
        this.ircDCCManager = Objects.requireNonNull(ircDCCManager, "ircDCCManager must not be null!");
        this.ircUser = Objects.requireNonNull(sourceIRCUser, "sourceIRCUser must not be null!");

        // get only the last part of the filepath, the filename and extension
        Objects.requireNonNull(IRCUtils.emptyToNull(filename), "filename must not be null or empty!");
        this.filename = filename;

        this.address = Objects.requireNonNull(IRCUtils.emptyToNull(address), "address must not be null or empty!");

        if (port <= 0 || port > 65535)
        {
            throw new IllegalArgumentException("a valid <port> must be between 1 and 65535");
        }
        this.port = port;

        this.totalSize = (size < 0) ? -1 : size;

        this.direction = IRCDCCFileTransfer.Direction.INCOMING;
        this.status = IRCDCCFileTransfer.Status.WAITING;
    }

    public IRCDCCFileTransfer(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser, String filename, String address,
            int port) throws NullPointerException, IllegalArgumentException
    {
        this(ircDCCManager, sourceIRCUser, filename, address, port, -1);
    }

    public static IRCDCCFileTransfer fromIRCDCCMessage(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser,
            IRCDCCMessage ircDCCMessage) throws NullPointerException, IllegalArgumentException
    {
        Objects.requireNonNull(ircDCCMessage, "ircDCCMessage must not be null!");

        return new IRCDCCFileTransfer(ircDCCManager, sourceIRCUser, ircDCCMessage.getArgument(),
                ircDCCMessage.getAddress(), ircDCCMessage.getPort(), ircDCCMessage.getSize());
    }

    // ------------------------------------------------------------------------
    // Outgoing file transfers

    // ------------------------------------------------------------------------

    public final IRCUser getIRCUser()
    {
        return this.ircUser;
    }

    // --------------------------------

    public String getFilename()
    {
        return new File(this.filename).getName();
    }

    public String getAddress()
    {
        return this.address;
    }

    public int getPort()
    {
        return this.port;
    }

    public long getSize()
    {
        return this.totalSize;
    }

    public IRCDCCFileTransfer.Direction getDirection()
    {
        return this.direction;
    }

    public IRCDCCFileTransfer.Status getStatus()
    {
        return this.status;
    }

    // ------------------------------------------------------------------------

    public void startTransfer(File localFile, boolean allowResume)
    {
        this.localFile = Objects.requireNonNull(localFile, "localFile must not be null!");
        this.allowResume = allowResume;

        // run asynchronously
        this.thread = new Thread(this);
        this.thread.setName(this.getClass().getSimpleName() + "-Thread-" + threadCount.getAndIncrement());
        this.thread.start();
    }

    public void abort()
    {
        if (this.isRunning())
        {
            this.status = IRCDCCFileTransfer.Status.ABORTED;
            this.thread.interrupt();
        }
    }

    // ------------------------------------------------------------------------

    public boolean isRunning()
    {
        return this.thread != null && !this.thread.isInterrupted();
    }

    @Override
    public void run()
    {
        // abort if already working or finished
        if (this.status != IRCDCCFileTransfer.Status.WAITING) return;

        if (this.direction == IRCDCCFileTransfer.Direction.INCOMING)
        {
            try
            {
                receive();
            }
            catch (FileAlreadyExistsException e)
            {
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            }
        }
        else
        // (this.direction == IRCDCCFileTransfer.Direction.OUTGOING)
        {
            send();
        }
    }

    protected void receive() throws FileAlreadyExistsException
    {
        this.size = this.localFile.length();

        // check if file is already there
        if (this.size > this.totalSize)
        {
            // another file
            throw new FileAlreadyExistsException(this.localFile.getAbsolutePath());
        }
        else if (this.size > 0)
        {
            // may be our file
            if (this.allowResume)
            {
                this.ircDCCManager.getIRCNetwork().send(new AsIRCMessage() {
                    @Override
                    public String asIRCMessageString()
                    {
                        return "PRIVMSG " + IRCDCCFileTransfer.this.ircUser.getNickname() + " :\u0001DCC RESUME \""
                                + IRCDCCFileTransfer.this.filename + "\" " + IRCDCCFileTransfer.this.port + " "
                                + IRCDCCFileTransfer.this.size + "\u0001";
                    }

                    @Override
                    public IRCMessage asIRCMessage()
                    {
                        return null;
                    }
                });
                this.status = IRCDCCFileTransfer.Status.RESUMING;

                return;
            }
            else
            {
                this.localFile.delete();

                return;
            }
        }

        // new file
        try
        {
            Socket sock = new Socket(InetAddress.getByName(this.address), this.port);
            sock.setSoTimeout(IRCDCCManager.TIMEOUT);

            this.startTime = System.currentTimeMillis();

            BufferedInputStream sock_bi = new BufferedInputStream(sock.getInputStream());
            BufferedOutputStream sock_bo = new BufferedOutputStream(sock.getOutputStream());

            BufferedOutputStream file_bo = new BufferedOutputStream(new FileOutputStream(this.localFile,
                    this.allowResume));

            byte[] buffer = new byte[IRCDCCManager.BUFFER_SIZE];
            byte[] ack = new byte[4];

            long t1 = System.currentTimeMillis();
            long t2;
            int bytesRead = -1;
            while ((bytesRead = sock_bi.read(buffer)) != -1)
            {
                t2 = System.currentTimeMillis();
                file_bo.write(buffer, 0, bytesRead);
                this.size += bytesRead;

                ack[0] = (byte) ((this.size >> 24) & 0xff);
                ack[1] = (byte) ((this.size >> 16) & 0xff);
                ack[2] = (byte) ((this.size >> 8) & 0xff);
                ack[3] = (byte) ((this.size >> 0) & 0xff);
                sock_bo.write(ack);
                sock_bo.flush();

                // do delay?

                // TODO: compute transferRate ...
                this.transferRate = 0.6 * this.transferRate + 0.4 * bytesRead / (t2 - t1 + 1.0);
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().object("transferRate", this.transferRate);

                t1 = System.currentTimeMillis();
            }

            file_bo.flush();
            this.endTime = System.currentTimeMillis();

            file_bo.close();
            sock_bo.close();
            sock_bi.close();
            sock.close();
        }
        catch (UnknownHostException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
        }
        catch (IOException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
        }
    }

    private void send()
    {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    // ------------------------------------------------------------------------

    public static enum Direction
    {
        INCOMING, OUTGOING
    }

    public static enum Status
    {
        WAITING, RESUMING, TRANSFERING, FINISHED, ABORTED // PAUSED/PAUSING
    }

    // ------------------------------------------------------------------------

    public final IRCDCCManager getIRCDCCManager()
    {
        return this.ircDCCManager;
    }
}
