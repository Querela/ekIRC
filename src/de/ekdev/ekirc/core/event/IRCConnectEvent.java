/**
 * IRCConnectEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCServerContext;

/**
 * @author ekDev
 */
public class IRCConnectEvent extends IRCEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public IRCConnectEvent(IRCServerContext source)
    {
        super(source);
    }

    public IRCServerContext getIRCServerContext()
    {
        return (IRCServerContext) super.getSource();
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
