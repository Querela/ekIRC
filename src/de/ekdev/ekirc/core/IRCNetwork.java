/**
 * IRCNetwork.java
 */
package de.ekdev.ekirc.core;

import java.util.List;

import de.ekdev.ekirc.core.commands.connection.IRCPassCommand;
import de.ekdev.ekirc.core.commands.connection.IRCQuitCommand;
import de.ekdev.ekirc.core.event.IRCConnectEvent;

/**
 * @author ekDev
 */
public class IRCNetwork
{
    public final static int MAX_SERVER_NAME_LENGTH = 63;

    private String name;

    private final IRCManager ircManager;

    protected final IRCChannelManager ircChannelManager;
    protected final IRCUserManager ircUserManager;

    protected IRCConnection ircConnection;
    protected IRCReader ircReader;
    protected IRCWriter ircWriter;

    public IRCNetwork(IRCManager ircManager)
    {
        if (ircManager == null)
        {
            throw new IllegalArgumentException("Argument ircManager is null!");
        }

        this.ircManager = ircManager;

        this.ircChannelManager = this.createDefaultIRCChannelManager();
        this.ircUserManager = this.createDefaultIRCUserManager();
    }

    // ------------------------------------------------------------------------

    public void connect(String host, int port, String password)
    {
        if (this.ircConnection != null && this.ircConnection.isConnected()) return;

        this.ircConnection = new IRCConnection(host, port);

        // create reader, writer threads
        this.ircReader = new IRCReader(this.ircConnection, this.createDefaultIRCMessageProcessor());
        this.ircWriter = new IRCWriter(this.ircConnection);

        // start connection
        this.ircConnection.connect();
        if (this.ircConnection.isConnected())
        {
            this.ircReader.start();
            this.ircWriter.start();

            if (password != null && password.length() > 0)
            {
                this.ircWriter.sendImmediate(new IRCPassCommand(password));
            }

            // raise an identification / socket opened / connection established event
            this.ircManager.getEventManager().dispatch(new IRCConnectEvent(this));
        }
    }

    public void quit(String reason)
    {
        if (this.ircWriter.isRunning()) this.ircWriter.sendImmediate(new IRCQuitCommand(reason));
    }

    public void disconnect()
    {
        this.ircWriter.stop();
        this.ircReader.stop();

        this.ircConnection.disconnect();
    }

    // ------------------------------------------------------------------------

    public void send(AsIRCMessage ircMessage)
    {
        if (this.ircConnection.isConnected())
        {
            this.ircWriter.send(ircMessage);
        }
    }

    public void sendImmediate(AsIRCMessage ircMessage)
    {
        // TODO: only IRCPongCommands?
        if (this.ircConnection.isConnected())
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
        return new IRCMessageProcessor(this.ircManager, this);
    }

    protected void registerEvents()
    {

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
}
