/**
 * NickAlreadyInUseEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class NickAlreadyInUseEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCMessage ircMessage;

    public NickAlreadyInUseEvent(IRCNetwork source, IRCMessage ircMessage)
    {
        super(source);
        this.ircMessage = ircMessage;
    }

    public IRCMessage getIRCMessage()
    {
        return this.ircMessage;
    }

    public String getNick()
    {
        return this.ircMessage.getParams().get(1);
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return NickAlreadyInUseEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return NickAlreadyInUseEvent.listeners;
    }
}
