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
    private final IRCIOInterface ircInterface;

    private BufferedReader reader;
    private boolean isRunning;
    private Thread thread;
    private final static AtomicInteger threadCount = new AtomicInteger();

    public IRCReader(IRCIOInterface ircInterface) throws IllegalArgumentException
    {
        if (ircInterface == null)
        {
            throw new IllegalArgumentException("Argument ircInterface is null!");
        }

        this.ircInterface = ircInterface;
    }

    // ------------------------------------------------------------------------

    public boolean isRunning()
    {
        // TODO: test interrupt status
        this.isRunning = (this.thread != null) && !this.thread.isInterrupted();
        return this.isRunning;
    }

    public boolean start()
    {
        if (!this.ircInterface.getIRCConnection().isConnected())
        {
            return false;
        }
        if (this.isRunning())
        {
            return false;
        }

        // init reader
        this.reader = new BufferedReader(new InputStreamReader(new DataInputStream(this.ircInterface.getIRCConnection()
                .getInputStream()), this.ircInterface.getIRCConnection().getCharset()));

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
        this.ircInterface.getIRCConnectionLog().message("READER THREAD STARTED ---");

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
                this.ircInterface.getIRCConnectionLog().message("READ INTERRUPT ---");
                // send a ping
                continue;
            }
            catch (Exception e)
            {
                // SocketException: socket closed
                // IOException: Stream closed
                this.ircInterface.getIRCConnectionLog().exception(e);
                line = null;
            }

            // check if end
            if (line == null)
            {
                break;
            }

            this.ircInterface.getIRCConnectionLog().in(line);
            this.ircInterface.getIRCMessageProcessor().handleLine(line);
        }

        this.ircInterface.getIRCConnectionLog().message("READER THREAD STOPPED ---");
        this.isRunning = false;
        this.ircInterface.shutdown();
    }
}
