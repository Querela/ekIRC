/**
 * PingEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class PingEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final String pingValue;

    public PingEvent(IRCNetwork ircNetwork, String pingValue)
    {
        super(ircNetwork);
        this.pingValue = pingValue;
    }

    public String getPingValue()
    {
        return this.pingValue;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return PingEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return PingEvent.listeners;
    }
}
