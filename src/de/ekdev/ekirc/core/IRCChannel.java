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
    final static int MAX_CHANNEL_NAME_LENGTH = 50;

    private final IRCChannelManager ircChannelManager;

    private String name;
    private String topic;

    // http://webtoman.com/opera/panel/ircdmodes.html
    private String modes; // -> EnumSet?

    // ------------------------------------------------------------------------

    public IRCChannel(IRCChannelManager ircChannelManager) throws NullPointerException
    {
        Objects.requireNonNull(ircChannelManager, "ircChannelManager must not be null!");

        this.ircChannelManager = ircChannelManager;
    }

    // ------------------------------------------------------------------------

    // TODO: get fields

    // ------------------------------------------------------------------------

    // TODO: actions
}
