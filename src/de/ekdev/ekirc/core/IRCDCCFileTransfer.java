/**
 * IRCDCCFileTransfer.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import de.ekdev.ekirc.core.event.DCCFileTransferEndEvent;
import de.ekdev.ekirc.core.event.DCCFileTransferStartEvent;

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
    private float transferRate;
    private Thread thread;
    private final static AtomicInteger threadCount = new AtomicInteger();

    private long minDelay = 0L;
    private long maxDelay = 0L;

    protected final static float alphaOld = 0.5f;
    protected final static float alphaNew = 1.0f - alphaOld;

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

        // add to manager
        this.ircDCCManager.addIRCDCCFileTransfer(this);
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

    // TODO: add constructors

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

    public String getIPAddress()
    {
        try
        {
            return IRCDCCManager.ipToString(IRCDCCManager.longToIP(Long.valueOf(this.address)));
        }
        catch (NumberFormatException e)
        {
            return this.address;
        }
    }

    public int getPort()
    {
        return this.port;
    }

    public long getTotalSize()
    {
        return this.totalSize;
    }

    public IRCDCCFileTransfer.Direction getDirection()
    {
        return this.direction;
    }

    // TODO: toString()

    public String getLongDescription()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getSimpleName()) // class name
                .append(" (").append(this.direction.toString()).append(",") // direction
                .append(this.status.toString()).append(")") // status
                .append(": ").append(String.format("%,d", this.size)).append("/") // current size
                .append(String.format("%,d", this.totalSize)).append(" byte [") // total size
                .append(String.format("%03.2f", this.getProgress() * 100.0f)).append(" %] - ") // percentage
                .append(this.getSourceString()).append(" \"") // user (nickname), ip & port
                .append(this.getFilename()).append("\""); // filename
        if (this.filename != null) sb.append("->\"").append(this.localFile.getAbsolutePath()).append("\" (local)");
        if (this.thread.equals(Thread.currentThread())) sb.append(" - [").append(this.getThreadName()).append("]");

        return sb.toString();
    }

    public String getSourceString()
    {
        return new StringBuilder(this.ircUser.getNickname()).append(" [").append(this.getIPAddress()).append(":")
                .append(this.getPort()).append("]").toString();
    }

    public String getID()
    {
        return new StringBuilder(this.ircUser.getNickname()).append(" [").append(this.getPort()).append("]").toString();
    }

    public String getThreadName()
    {
        if (this.thread == null) return null;

        return this.thread.getName();
    }

    // --------------------------------

    public IRCDCCFileTransfer.Status getStatus()
    {
        return this.status;
    }

    public long getSize()
    {
        return this.size;
    }

    public float getTransferRate()
    {
        return this.transferRate;
    }

    public float getProgress()
    {
        return (float) this.size / this.totalSize;
    }

    public File getLocalFile()
    {
        return this.localFile;
    }

    public boolean isResumeAllowed()
    {
        return this.allowResume;
    }

    // --------------------------------

    public long getStartTime()
    {
        return this.startTime;
    }

    public long getEndTime()
    {
        return this.endTime;
    }

    public long getTotalTime()
    {
        return this.endTime - this.startTime;
    }

    // --------------------------------

    public long getMaxDelay()
    {
        return this.maxDelay;
    }

    public long getMinDelay()
    {
        return this.minDelay;
    }

    public void setMaxDelay(long maxDelay)
    {
        // TODO: check for maxDelay > minDelay?
        if (maxDelay < 0L) maxDelay = 0L;

        this.maxDelay = maxDelay;
    }

    public void setMinDelay(long minDelay)
    {
        // TODO: check for maxDelay > minDelay?
        if (minDelay < 0L) minDelay = 0L;

        this.minDelay = minDelay;
    }

    // ------------------------------------------------------------------------

    public void startTransfer(File localFile, boolean allowResume)
    {
        this.localFile = Objects.requireNonNull(localFile, "localFile must not be null!");
        this.allowResume = allowResume;

        if (!this.checkFile()) return;

        // run asynchronously
        this.thread = new Thread(this);
        this.thread.setName(this.getClass().getSimpleName() + "-Thread-" + threadCount.getAndIncrement());
        this.thread.start();
    }

    public void startTransfer(File localFile)
    {
        // no append/resume ...
        this.startTransfer(localFile, false);
    }

    public void resumeTransfer(long fileOffset)
    {
        if (this.status != IRCDCCFileTransfer.Status.RESUMING) return;

        this.size = fileOffset;
        this.size = (this.size < 0) ? 0L : this.size;

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

            try
            {
                this.thread.join();
            }
            catch (InterruptedException e)
            {
            }
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
        if (this.status != IRCDCCFileTransfer.Status.WAITING && this.status != IRCDCCFileTransfer.Status.RESUMING)
            return;

        // start event
        this.ircDCCManager.getIRCNetwork().raiseEvent(
                new DCCFileTransferStartEvent(this.ircDCCManager.getIRCNetwork(), this));

        if (this.direction == IRCDCCFileTransfer.Direction.INCOMING)
        {
            if (this.status == IRCDCCFileTransfer.Status.WAITING || this.status == IRCDCCFileTransfer.Status.RESUMING)
            {
                receive();
            }
            else
            {
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog()
                        .message("Wrong file transfer status :" + this.status.toString() + ". Aborting.");
            }
        }
        else
        // (this.direction == IRCDCCFileTransfer.Direction.OUTGOING)
        {
            send();
        }

        // end event
        this.ircDCCManager.getIRCNetwork().raiseEvent(
                new DCCFileTransferEndEvent(this.ircDCCManager.getIRCNetwork(), this));
    }

    protected boolean checkFile()
    {
        this.size = this.localFile.length();

        // check if a file is already there
        if (this.size > this.totalSize)
        {
            // TODO: check ?
            this.ircDCCManager
                    .getIRCNetwork()
                    .getIRCConnectionLog()
                    .message(
                            "Local file \"" + this.localFile.getAbsolutePath()
                                    + "\" is larger than the receiving file. Aborting.");
            return false;
        }
        else if (this.size > 0)
        {
            // may be our file
            if (this.allowResume)
            {
                this.ircDCCManager
                        .getIRCNetwork()
                        .getIRCConnectionLog()
                        .message(
                                "Try to resume transfer <\"" + this.getFilename() + "\"> from "
                                        + this.ircUser.getNickname());
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

                // TODO: store this object ... ?

                return false;
            }
            else
            {
                // this.localFile.delete();
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog()
                        .message("Overwriting file \"" + this.localFile.getAbsolutePath() + "\"");
                this.size = 0L;
            }
        }

        return true;
    }

    protected void delay(long maxDelay, long minDelay, long off)
    {
        long delay = maxDelay - off;
        delay = (delay > maxDelay) ? maxDelay : delay; // not too much
        delay = (delay < minDelay) ? minDelay : delay; // not too less
        delay = (delay < 0L) ? 0L : delay; // not less zero
        // long delay = Math.min(Math.min(0, minDelay), maxDelay - off);

        if (delay < 10L) return;

        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
        }
    }

    // TODO: receive with OutputStream os
    // TODO: how to open/close?
    // TODO: output-adapter-interface? start->os, finish->(close?) ?, for File, Zip, ... ?
    protected boolean receive()
    {
        SocketChannel sockChannel;
        Socket sock;

        try
        {
            sockChannel = SocketChannel.open();
            sockChannel.configureBlocking(true);
            sock = sockChannel.socket();
            sock.connect(new InetSocketAddress(InetAddress.getByName(this.address), this.port), IRCDCCManager.TIMEOUT);
            sock.setSoTimeout(IRCDCCManager.TIMEOUT);
        }
        catch (UnknownHostException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.FAILED;
            return false;
        }
        catch (SocketException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.FAILED;
            return false;
        }
        catch (IOException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.FAILED;
            return false;
        }

        this.startTime = System.currentTimeMillis();

        BufferedInputStream sock_bi = null;
        BufferedOutputStream sock_bo = null;
        BufferedOutputStream file_bo = null;

        try
        {
            sock_bi = new BufferedInputStream(sock.getInputStream());
            sock_bo = new BufferedOutputStream(sock.getOutputStream());

            file_bo = new BufferedOutputStream(new FileOutputStream(this.localFile, this.allowResume));

            // ----------------------------------------------------------------
            // do action ...

            this.status = IRCDCCFileTransfer.Status.TRANSFERING;
            this.ircDCCManager
                    .getIRCNetwork()
                    .getIRCConnectionLog()
                    .message(
                            "Start receiving file <\"" + this.getFilename() + "\"> from " + this.ircUser.getNickname()
                                    + "[" + IRCDCCManager.ipToString(sock.getInetAddress().getAddress()) + ":"
                                    + this.port + "]" + " (Storing in: \"" + this.localFile.getAbsolutePath()
                                    + "\") ... [" + Thread.currentThread().getName() + "]");

            byte[] buffer = new byte[IRCDCCManager.BUFFER_SIZE];
            byte[] ack = new byte[4];

            long t1;
            long t2;

            int bytesRead = -1;

            while (this.status == IRCDCCFileTransfer.Status.TRANSFERING)
            {
                // read from stream
                t1 = System.currentTimeMillis();
                bytesRead = sock_bi.read(buffer);
                t2 = System.currentTimeMillis();

                if (bytesRead == -1)
                {
                    if (this.thread.isInterrupted())
                    {
                        this.status = IRCDCCFileTransfer.Status.ABORTED;
                    }
                    else
                    {
                        this.status = IRCDCCFileTransfer.Status.FINISHED;
                    }

                    break;
                }

                // write to file
                file_bo.write(buffer, 0, bytesRead);
                this.size += bytesRead;

                // send ack
                ack[0] = (byte) ((this.size >> 24) & 0xff);
                ack[1] = (byte) ((this.size >> 16) & 0xff);
                ack[2] = (byte) ((this.size >> 8) & 0xff);
                ack[3] = (byte) ((this.size >> 0) & 0xff);
                sock_bo.write(ack);
                sock_bo.flush();

                long deltaTime = t2 - t1;
                deltaTime = (deltaTime == 0) ? 1L : deltaTime;

                // do delay?
                this.delay(this.maxDelay, this.minDelay, deltaTime);

                // compute transferRate ...
                this.transferRate = IRCDCCFileTransfer.alphaOld * this.transferRate + // old transferRate
                        IRCDCCFileTransfer.alphaNew * bytesRead / deltaTime * 1000.0f; // new transferRate

                this.ircDCCManager
                        .getIRCNetwork()
                        .getIRCConnectionLog()
                        .object("transferRate [" + Thread.currentThread().getName() + "] (Byte/sec)  ",
                                this.transferRate);
                this.ircDCCManager
                        .getIRCNetwork()
                        .getIRCConnectionLog()
                        .object("progress     [" + Thread.currentThread().getName() + "] (percentage)",
                                this.getProgress() * 100.0f);
            }

            file_bo.flush();

            // ----------------------------------------------------------------
        }
        catch (FileNotFoundException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.FAILED;
        }
        catch (SocketTimeoutException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.ABORTED;
        }
        catch (IOException e)
        {
            this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            this.status = IRCDCCFileTransfer.Status.FAILED;
        }
        finally
        {
            this.endTime = System.currentTimeMillis();

            // close the buffered streams
            try
            {
                if (sock_bi != null) sock_bi.close();
                if (sock_bo != null) sock_bo.close();
                if (file_bo != null)
                {
                    file_bo.flush();
                    file_bo.close();
                }
            }
            catch (IOException e)
            {
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            }

            // close the socket & socket channel
            try
            {
                sock.close();
                sockChannel.close();
            }
            catch (IOException e)
            {
                this.ircDCCManager.getIRCNetwork().getIRCConnectionLog().exception(e);
            }
        }

        this.ircDCCManager
                .getIRCNetwork()
                .getIRCConnectionLog()
                .message(
                        "Finished receiving file <\"" + this.getFilename() + "\"> from " + this.ircUser.getNickname()
                                + "[" + IRCDCCManager.ipToString(sock.getInetAddress().getAddress()) + ":" + this.port
                                + "]" + " with status: " + this.status.toString());

        return this.status == IRCDCCFileTransfer.Status.FINISHED;
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
        WAITING, RESUMING, TRANSFERING, FINISHED, ABORTED, FAILED // PAUSED/PAUSING
    }

    // ------------------------------------------------------------------------

    public final IRCDCCManager getIRCDCCManager()
    {
        return this.ircDCCManager;
    }
}
