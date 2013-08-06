/**
 * IRCPingEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCPingEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final String pingValue;

    public IRCPingEvent(IRCNetwork source, String pingValue)
    {
        super(source);
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
        return IRCPingEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCPingEvent.listeners;
    }
}
