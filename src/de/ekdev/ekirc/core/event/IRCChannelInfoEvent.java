/**
 * IRCChannelInfoEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCChannelInfoEvent extends IRCNetworkInfoEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;

    public IRCChannelInfoEvent(IRCNetwork ircNetwork, IRCMessage ircMessage, IRCChannel ircChannel)
    {
        super(ircNetwork, ircMessage);

        this.ircChannel = ircChannel;
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCChannelInfoEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCChannelInfoEvent.listeners;
    }
}
