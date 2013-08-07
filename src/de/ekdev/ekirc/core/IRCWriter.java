/**
 * IRCWriter.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ekDev
 */
public class IRCWriter implements Runnable
{
    private final IRCConnection con;
    private final IRCConnectionLog log;

    private BufferedWriter writer;
    private boolean isRunning;
    private Thread thread;
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(); // TODO: add limit?
    private final static AtomicInteger threadCount = new AtomicInteger();

    public IRCWriter(IRCConnection con, IRCConnectionLog log) throws IllegalArgumentException
    {
        if (con == null)
        {
            throw new IllegalArgumentException("Argument con is null!");
        }
        if (log == null)
        {
            throw new IllegalArgumentException("Argument log is null!");
        }

        this.con = con;
        this.log = log;
    }

    // ------------------------------------------------------------------------

    public boolean isRunning()
    {
        this.isRunning = (this.thread != null) && !this.thread.isInterrupted();
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

        // init writer
        this.writer = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(this.con.getOutputStream()),
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
        this.log.message("WRITER THREAD STARTED ---");

        try
        {
            while (!this.thread.isInterrupted())
            {
                String line2send = this.queue.take();
                if (line2send != null && this.con.isConnected())
                {
                    sendLineNow(line2send);
                }
                Thread.sleep(this.con.getSendDelay());
            }
        }
        catch (InterruptedException e)
        {
        }

        this.log.message("WRITER THREAD STOPPED ---");
    }

    // ------------------------------------------------------------------------

    public int getQueueSize()
    {
        return this.queue.size();
    }

    // public void flushQueue()
    // {
    // try
    // {
    // while (! this.queue.isEmpty())
    // {
    // String line2send = this.queue.take();
    // if (line2send != null && this.con.isConnected())
    // {
    // sendLineNow(line2send);
    // }
    // }
    // }
    // catch (InterruptedException e)
    // {
    // }
    // }

    public void send(AsIRCMessage ircMessage)
    {
        // TODO: check length?
        this.sendLine(ircMessage.asIRCMessageString());
    }

    public void sendImmediate(AsIRCMessage ircMessage)
    {
        this.sendLineNow(ircMessage.asIRCMessageString());
    }

    public void sendLine(String line)
    {
        try
        {
            this.queue.put(line);
        }
        catch (InterruptedException e)
        {
            // TODO: abort?
            this.log.exception(e);
        }
    }

    public void sendLineNow(String line)
    {
        if (line.length() - 2 > IRCMessage.MAX_IRC_LINE_LENGTH)
        {
            line = line.substring(0, IRCMessage.MAX_IRC_LINE_LENGTH - 2);
        }
        synchronized (this.writer)
        {
            try
            {
                this.writer.write(line);
                this.writer.write(IRCMessage.IRC_LINE_ENDING);
                this.writer.flush();

                this.log.out(line);
            }
            catch (Exception e)
            {
                // IOException
                // TODO: abort here?
                this.log.exception(e);
            }
        }
    }

    // public void sendRawLineNow(String line)
    // {
    // // TODO: better to use above
    // synchronized (this.writer)
    // {
    // try
    // {
    // this.writer.write(line);
    // this.writer.flush();
    // }
    // catch (Exception e)
    // {
    // // IOException
    // e.printStackTrace();
    // }
    // }
    // }
}
