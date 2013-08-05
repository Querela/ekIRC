/**
 * IRCServerContext.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCServerContext
{
    public final static int MAX_SERVER_NAME_LENGTH = 63;

    private String name;

    private final IRCManager ircManager;

    protected final IRCChannelManager ircChannelManager;
    protected final IRCUserManager ircUserManager;

    protected IRCConnection ircConnection;
    protected IRCReader ircReader;
    protected IRCWriter ircWriter;

    public IRCServerContext(IRCManager ircManager)
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

    // ------------------------------------------------------------------------

    protected IRCChannelManager createDefaultIRCChannelManager()
    {
        return new IRCChannelManager(this);
    }

    protected IRCUserManager createDefaultIRCUserManager()
    {
        return new IRCUserManager(this);
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
