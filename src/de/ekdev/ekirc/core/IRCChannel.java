/**
 * IRCChannel.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCChannel
{
    public final static int MAX_CHANNEL_NAME_LENGTH = 50;
    public final static String CHANNEL_PREFIXES = "&#+!";

    private final IRCChannelManager ircChannelManager;

    private final String name;
    private String topic;

    // http://webtoman.com/opera/panel/ircdmodes.html
    private String modes; // -> EnumSet?

    // ------------------------------------------------------------------------

    public IRCChannel(IRCChannelManager ircChannelManager, String name, String topic) throws NullPointerException
    {
        Objects.requireNonNull(ircChannelManager, "ircChannelManager must not be null!");
        Objects.requireNonNull(name, "name must not be null!");

        this.ircChannelManager = ircChannelManager;
        this.name = name;
        this.topic = topic;

        // automatically add to manager
        // this.ircChannelManager.addIRCChannel(this);
    }

    public IRCChannel(IRCChannelManager ircChannelManager, String name) throws NullPointerException
    {
        this(ircChannelManager, name, null);
    }

    // ------------------------------------------------------------------------

    public final IRCChannelManager getIRCChannelManager()
    {
        return this.ircChannelManager;
    }

    // ------------------------------------------------------------------------

    // TODO: get fields
    public String getName()
    {
        return this.name;
    }

    public String getTopic()
    {
        return this.topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    // ------------------------------------------------------------------------

    // TODO: actions

    // ------------------------------------------------------------------------

    public static boolean validateChannelname(String channelname)
    {
        // TODO: validateChannelname
        return true;
    }

    public static boolean validateChannelkey(String channelkey)
    {
        // TODO: validateChannelkey
        return true;
    }

    // ------------------------------------------------------------------------
}
