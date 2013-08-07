/**
 * IRCReader.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ekDev
 */
public class IRCReader implements Runnable
{
    private final IRCConnection con;
    private final IRCConnectionLog log;
    private final IRCMessageProcessor msgProc;
    private final IRCNetwork net;

    private BufferedReader reader;
    private boolean isRunning;
    private Thread thread;
    private final static AtomicInteger threadCount = new AtomicInteger();

    public IRCReader(IRCConnection con, IRCNetwork net, IRCConnectionLog log, IRCMessageProcessor msgProc)
            throws IllegalArgumentException
    {
        if (con == null)
        {
            throw new IllegalArgumentException("Argument con is null!");
        }
        if (net == null)
        {
            throw new IllegalArgumentException("Argument net is null!");
        }
        if (log == null)
        {
            throw new IllegalArgumentException("Argument log is null!");
        }
        if (msgProc == null)
        {
            throw new IllegalArgumentException("Argument msgProc is null!");
        }

        this.msgProc = msgProc;
        this.con = con;
        this.log = log;
        this.net = net;
    }

    // ------------------------------------------------------------------------

    public boolean isRunning()
    {
        // TODO: test interrupt status
        this.isRunning = (this.thread != null) && this.thread.isAlive();
        return this.isRunning;
    }

    public boolean start()
    {
        if (!this.con.isConnected())
        {
            return false;
        }
        if (this.isRunning())
        {
            return false;
        }

        // init reader
        this.reader = new BufferedReader(new InputStreamReader(new DataInputStream(this.con.getInputStream()),
                this.con.getCharset()));

        // run asynchronously
        this.thread = new Thread(this);
        this.thread.setName(this.getClass().getSimpleName() + "-Thread-" + threadCount.getAndIncrement());
        this.thread.start();
        this.isRunning = true;

        return true;
    }

    public void stop()
    {
        if (this.isRunning())
        {
            this.thread.interrupt();
            this.isRunning = false;
        }
    }

    @Override
    public void run()
    {
        this.log.message("READER THREAD STARTED ---");

        while (!this.thread.isInterrupted())
        {
            String line = null;
            try
            {
                line = this.reader.readLine();
            }
            // catch (SocketTimeoutException e)
            // {
            // e.printStackTrace();
            // if (this.thread.isInterrupted())
            // {
            // line = null;
            // }
            // else
            // {
            // continue;
            // }
            // }
            catch (InterruptedIOException e)
            {
                this.log.message("READ INTERRUPT ---");
                // send a ping
                continue;
            }
            catch (Exception e)
            {
                // SocketException: socket closed
                this.log.exception(e);
                line = null;
            }

            // check if end
            if (line == null)
            {
                break;
            }

            this.log.in(line);
            msgProc.handleLine(line);
        }

        this.log.message("READER THREAD STOPPED ---");
        this.isRunning = false;
        this.con.disconnect();
        this.net.disconnect();
    }
}
