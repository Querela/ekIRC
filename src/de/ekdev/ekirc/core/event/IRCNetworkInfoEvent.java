/**
 * IRCNetworkInfoEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCNetworkInfoEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final IRCMessage ircMessage;

    public IRCNetworkInfoEvent(IRCNetwork source, IRCMessage ircMessage)
    {
        super(source);
        this.ircMessage = ircMessage;
    }

    public IRCMessage getIRCMessage()
    {
        return this.ircMessage;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCNetworkInfoEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCNetworkInfoEvent.listeners;
    }
}
