/**
 * IRCNetwork.java
 */
package de.ekdev.ekirc.core;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.commands.connection.IRCPassCommand;
import de.ekdev.ekirc.core.commands.connection.IRCQuitCommand;
import de.ekdev.ekirc.core.commands.misc.IRCPongCommand;
import de.ekdev.ekirc.core.event.IRCConnectEvent;
import de.ekdev.ekirc.core.event.IRCDisconnectEvent;
import de.ekdev.ekirc.core.event.IRCEvent;

/**
 * @author ekDev
 */
public class IRCNetwork implements IRCIOInterface
{
    public final static int MAX_SERVER_NAME_LENGTH = 63;

    private String name;
    private IRCNetworkInfo ircNetworkInfo;

    protected IRCIdentity myIRCIdentity;

    private final IRCManager ircManager;

    protected final IRCChannelManager ircChannelManager;
    protected final IRCUserManager ircUserManager;

    protected IRCConnection ircConnection;
    protected IRCReader ircReader;
    protected IRCWriter ircWriter;
    protected IRCConnectionLog ircConnectionLog;
    protected IRCMessageProcessor ircMessageProcessor;

    public IRCNetwork(IRCManager ircManager, IRCIdentity myIRCIdentity, String host, int port)
            throws NullPointerException
    {
        Objects.requireNonNull(ircManager, "ircManager must not be null!");
        Objects.requireNonNull(myIRCIdentity, "myIRCIdentity must not be null!");

        this.ircManager = ircManager;

        this.myIRCIdentity = myIRCIdentity;
        this.name = host;

        // default network info
        this.ircNetworkInfo = new IRCNetworkInfo();

        // default user/channel management
        this.ircChannelManager = this.createDefaultIRCChannelManager();
        this.ircUserManager = this.createDefaultIRCUserManager();

        // open log!
        this.openConnectionLog();

        // set up connection without actually connecting
        this.ircConnection = new IRCConnection(host, port);

        // default message processor - can be replaced/redirected later
        this.ircMessageProcessor = this.createDefaultIRCMessageProcessor();
    }

    // ------------------------------------------------------------------------

    public void connect()
    {
        Objects.requireNonNull(this.ircConnection,
                "this.ircConnection is null! Can't connect to network! Create new IRCNetwork object!");

        if (this.ircConnection.isConnected()) return;

        // create reader, writer threads
        // TODO: To separate further -> proxy object
        this.ircReader = new IRCReader((IRCIOInterface) this);
        this.ircWriter = new IRCWriter((IRCIOInterface) this);

        // start connection
        this.ircConnectionLog.message("Connecting to network ...");
        try
        {
            this.ircConnection.connect();
        }
        catch (UnknownHostException e)
        {
            this.ircConnectionLog.exception(e);
        }

        if (this.ircConnection.isConnected())
        {
            this.ircReader.start();
            this.ircWriter.start();

            String password = this.myIRCIdentity.getConnectionPassword();
            if (password != null && password.length() > 0)
            {
                this.ircWriter.sendImmediate(new IRCPassCommand(password));
            }

            // raise an identification / socket opened / connection established event
            this.ircManager.getEventManager().dispatch(new IRCConnectEvent(this));
        }
        else
        {
            this.ircConnectionLog.message("Couldn't connect to network!");

            this.disconnect();
        }
    }

    public boolean isConnected()
    {
        return (this.ircConnection != null && this.ircConnection.isConnected());
    }

    public void quit(String reason)
    {
        if (this.ircWriter != null && this.ircWriter.isRunning())
            this.ircWriter.sendImmediate(new IRCQuitCommand(reason));
    }

    public void disconnect()
    {
        // TODO: check only null? So we can reconnect on first time if never connected?
        if (this.ircConnection == null || !this.ircConnection.isConnected()) return;

        this.ircConnectionLog.message("Disconnecting from network ...");

        this.ircWriter.stop();
        this.ircWriter = null;
        this.ircReader.stop();
        this.ircReader = null;

        this.ircConnection.disconnect();

        this.ircManager.getEventManager().dispatch(new IRCDisconnectEvent(this));
    }

    public void reconnect()
    {
        this.connect();
    }

    // --------------------------------

    protected void openConnectionLog()
    {
        try
        {
            this.ircConnectionLog = new IRCConnectionLog("IRCLog_" + this.name.replaceAll("\\.", "(dot)") + ".log",
                    true);
            this.ircConnectionLog.header(this.name, null);
        }
        catch (Exception e)
        {
            // FileNotFoundException
            // IllegalArgumentException
            e.printStackTrace();
            this.ircConnectionLog = new IRCConnectionLog(System.out);
            this.ircConnectionLog.header(this.name, "IRCConnectionLog file creation failed. Switch to std::out.");
            this.ircConnectionLog.exception(e);
        }
    }

    public void closeConnectionLog()
    {
        if (this.ircConnectionLog != null) this.ircConnectionLog.close();
    }

    // protected void reopenConnectionLog()
    // {
    // if (this.ircConnectionLog != null) this.closeConnectionLog();
    // this.openConnectionLog();
    // }

    // ------------------------------------------------------------------------

    public String getName()
    {
        return this.name;
    }

    public String getHostname()
    {
        if (this.isConnected())
        {
            return this.ircConnection.getHost();
        }
        else
        {
            return null;
        }
    }

    public int getPort()
    {
        if (this.isConnected())
        {
            return this.ircConnection.getPort();
        }
        else
        {
            return -1;
        }
    }

    // ------------------------------------------------------------------------

    public IRCNetworkInfo getIRCNetworkInfo()
    {
        return this.ircNetworkInfo;
    }

    public IRCIdentity getMyIRCIdentity()
    {
        return this.myIRCIdentity;
    }

    // ------------------------------------------------------------------------

    public void send(AsIRCMessage ircMessage)
    {
        if (this.ircConnection != null && this.ircConnection.isConnected())
        {
            this.ircWriter.send(ircMessage);
        }
    }

    public void sendImmediate(AsIRCMessage ircMessage)
    {
        // TODO: only IRCPongCommands?
        if (this.ircConnection != null && this.ircConnection.isConnected())
        {
            this.ircWriter.sendImmediate(ircMessage);
        }
    }

    public void send(List<AsIRCMessage> ircMessages)
    {
        if (ircMessages == null) return;

        for (AsIRCMessage ircMessage : ircMessages)
        {
            this.send(ircMessage);
        }
    }

    public void send(AsIRCMessage... ircMessages)
    {
        if (ircMessages == null) return;

        for (AsIRCMessage ircMessage : ircMessages)
        {
            this.send(ircMessage);
        }
    }

    public void sendPong(String pingValue)
    {
        try
        {
            this.sendImmediate(new IRCPongCommand(pingValue));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------

    protected IRCChannelManager createDefaultIRCChannelManager()
    {
        return new IRCChannelManager(this);
    }

    protected IRCUserManager createDefaultIRCUserManager()
    {
        return new IRCUserManager(this);
    }

    protected IRCMessageProcessor createDefaultIRCMessageProcessor()
    {
        // TODO: create new or use from manager?
        // TODO: use proxy too?
        return new IRCMessageProcessor(this);
    }

    // ------------------------------------------------------------------------

    protected void setIRCMessageProcessor(IRCMessageProcessor ircMessageProcessor)
    {
        // update message processor if specific irc server implementation is known
        this.ircMessageProcessor = ircMessageProcessor;
    }

    // ------------------------------------------------------------------------

    public final IRCManager getIRCManager()
    {
        return this.ircManager;
    }

    public final IRCChannelManager getIRCChannelManager()
    {
        return this.ircChannelManager;
    }

    public final IRCUserManager getIRCUserManager()
    {
        return this.ircUserManager;
    }

    // --------------------------------

    @Override
    public final IRCConnection getIRCConnection()
    {
        return this.ircConnection;
    }

    @Override
    public final IRCMessageProcessor getIRCMessageProcessor()
    {
        return this.ircMessageProcessor;
    }

    @Override
    public final IRCConnectionLog getIRCConnectionLog()
    {
        return this.ircConnectionLog;
    }

    @Override
    public final void shutdown()
    {
        this.disconnect();
    }

    @Override
    public final void raiseEvent(IRCEvent ircEvent)
    {
        this.ircManager.getEventManager().dispatch(ircEvent);
    }

    // --------------------------------

    @Override
    protected void finalize() throws Throwable
    {
        if (this.isConnected()) this.disconnect();

        if (this.ircConnectionLog != null) this.closeConnectionLog();

        super.finalize();
    }
}
