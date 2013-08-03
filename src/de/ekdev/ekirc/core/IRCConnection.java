/**
 * IRCConnection.java
 */
package de.ekdev.ekirc.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * @author ekDev
 */
public class IRCConnection
{
    private Socket sock;
    private InetAddress ia;
    private boolean isConnected;
    private String host;
    private int port;
    private Charset charset;
    private int sendDelay;

    public IRCConnection(String host, int port, Charset charset, int sendDelay)
    {
        this.host = host;
        this.port = port;
        if (this.port < 0 || this.port > 65535)
        {
            this.port = 6667;
        }
        this.charset = charset;
        this.setSendDelay(sendDelay);
    }

    public IRCConnection(String host, int port)
    {
        this(host, port, Charset.defaultCharset(), 1000);
    }

    // ------------------------------------------------------------------------

    public static Socket getSocket(InetAddress host, int port) throws IOException
    {
        // TODO: secure socket / socketFactory ?
        return new Socket(host, port);
    }

    // ------------------------------------------------------------------------

    public boolean isConnected()
    {
        this.isConnected = this.sock != null && !this.sock.isClosed();
        return this.isConnected;
    }

    public InputStream getInputStream()
    {
        if (this.isConnected())
        {
            try
            {
                return this.sock.getInputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public OutputStream getOutputStream()
    {
        if (this.isConnected())
        {
            try
            {
                return this.sock.getOutputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Charset getCharset()
    {
        return this.charset;
    }

    public int getSendDelay()
    {
        return this.sendDelay;
    }

    public void setSendDelay(int sendDelay)
    {
        this.sendDelay = sendDelay;
        if (this.sendDelay < 0) this.sendDelay = 1000;
    }

    // ------------------------------------------------------------------------

    public String getIP()
    {
        if (this.isConnected())
        {
            return this.ia.getHostAddress();
        }

        return null;
    }

    public String getHost()
    {
        if (this.isConnected())
        {
            return this.ia.getHostName();
        }

        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }

    // ------------------------------------------------------------------------

    public boolean connect()
    {
        // if already connected
        if (this.isConnected())
        {
            // TODO: ...
            // reconnect/disconnect
            return false;
        }

        // if previously connected
        if (this.ia != null)
        {
            try
            {
                this.sock = IRCConnection.getSocket(ia, this.port);
                this.isConnected = true;

                // set socket timeout? for exiting

                return true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        // connect new
        try
        {
            for (InetAddress tia : InetAddress.getAllByName(this.host))
            {
                try
                {
                    this.sock = IRCConnection.getSocket(tia, this.port);
                    this.isConnected = true;
                    this.ia = tia;

                    return true;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void disconnect()
    {
        // TODO: some cleaning before? should be called last

        try
        {
            this.sock.close();
            this.isConnected = false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
