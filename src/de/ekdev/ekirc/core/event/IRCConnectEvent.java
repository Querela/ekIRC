/**
 * IRCConnectEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCConnectEvent extends IRCEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public IRCConnectEvent(IRCNetwork source)
    {
        super(source);
    }

    public IRCNetwork getIRCNetwork()
    {
        return (IRCNetwork) super.getSource();
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
