/**
 * IRCDisconnectEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCDisconnectEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public IRCDisconnectEvent(IRCNetwork source)
    {
        super(source);
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCDisconnectEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCDisconnectEvent.listeners;
    }
}
