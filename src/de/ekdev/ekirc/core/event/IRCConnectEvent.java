/**
 * IRCConnectEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCConnectEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public IRCConnectEvent(IRCNetwork ircNetwork)
    {
        super(ircNetwork);
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCConnectEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCConnectEvent.listeners;
    }
}
