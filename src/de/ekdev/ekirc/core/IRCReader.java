/**
 * IRCReader.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;

/**
 * @author ekDev
 */
public class IRCReader implements Runnable
{
    private final IRCConnection con;
    private final IRCMessageProcessor msgProc;
    private BufferedReader reader;
    private boolean isRunning;
    private Thread thread;

    public IRCReader(IRCConnection con, IRCMessageProcessor msgProc) throws IllegalArgumentException
    {
        if (con == null)
        {
            throw new IllegalArgumentException("Argument con is null!");
        }
        if (msgProc == null)
        {
            throw new IllegalArgumentException("Argument msgProc is null!");
        }

        this.msgProc = msgProc;
        this.con = con;
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
        System.out.println(">>> STARTING READER ---");

        while (true)
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
                System.out.println(">>> READ INTERRUPT");
                // send a ping
                continue;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                line = null;
            }

            // check if end
            if (line == null)
            {
                break;
            }

            msgProc.handleLine(line);

            // TODO: handle line -> event
            System.out.println(">>>" + line);
        }

        System.out.println(">>> STOPPING READER ---");
        this.isRunning = false;
        this.con.disconnect();
    }
}
