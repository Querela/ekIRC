/**
 * ChannelModeUpdateEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class ChannelModeUpdateEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;
    private final String mode;

    public ChannelModeUpdateEvent(IRCNetwork ircNetwork, IRCChannel ircChannel, String mode)
    {
        super(ircNetwork);

        this.ircChannel = ircChannel;
        this.mode = mode; // set mode with '= ircChannel.getMode();' ?
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    public String getMode()
    {
        return this.mode;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return ChannelModeUpdateEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return ChannelModeUpdateEvent.listeners;
    }
}
