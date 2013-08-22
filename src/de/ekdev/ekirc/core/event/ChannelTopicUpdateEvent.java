/**
 * ChannelTopicUpdateEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class ChannelTopicUpdateEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;
    private final String topic;

    public ChannelTopicUpdateEvent(IRCNetwork ircNetwork, IRCChannel ircChannel)
    {
        super(ircNetwork);

        this.ircChannel = ircChannel;
        this.topic = (ircChannel != null) ? ircChannel.getTopic() : null;
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    public String getTopic()
    {
        return this.topic;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return ChannelTopicUpdateEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return ChannelTopicUpdateEvent.listeners;
    }
}
