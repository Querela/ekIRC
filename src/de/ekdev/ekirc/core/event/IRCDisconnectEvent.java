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
    private final boolean reconnectPossible;

    public IRCDisconnectEvent(IRCNetwork source, boolean reconnectPossible)
    {
        super(source);
        this.reconnectPossible = reconnectPossible;
    }

    public boolean isReconnectPossible()
    {
        return this.reconnectPossible;
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
