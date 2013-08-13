/**
 * IRCChannelManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCChannelManager
{
    private final IRCNetwork ircNetwork;

    public IRCChannelManager(IRCNetwork ircNetwork) throws NullPointerException
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }
}
