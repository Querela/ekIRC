/**
 * ChannelListUpdateEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannelList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class ChannelListUpdateEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannelList oldIRCChannelList;
    private final IRCChannelList newIRCChannelList;

    public ChannelListUpdateEvent(IRCNetwork ircNetwork, IRCChannelList oldIRCChannelList)
    {
        super(ircNetwork);

        this.oldIRCChannelList = oldIRCChannelList;
        this.newIRCChannelList = ircNetwork.getIRCChannelManager().getIRCChannelList();
    }

    // ------------------------------------------------------------------------

    public boolean hasOldIRCChannelList()
    {
        return this.oldIRCChannelList != null;
    }

    public IRCChannelList getOldIRCChannelList()
    {
        return this.oldIRCChannelList;
    }

    public IRCChannelList getNewIRCChannelList()
    {
        return this.newIRCChannelList;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return ChannelListUpdateEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return ChannelListUpdateEvent.listeners;
    }
}
