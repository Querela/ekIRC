/**
 * UnknownServerCommandEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class UnknownServerCommandEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final IRCMessage ircMessage;

    public UnknownServerCommandEvent(IRCNetwork source, IRCMessage ircMessage)
    {
        super(source);
        this.ircMessage = ircMessage;
    }

    // ------------------------------------------------------------------------

    public boolean hasIRCMessage()
    {
        return this.ircMessage != null;
    }

    public IRCMessage getIRCMessage()
    {
        return this.ircMessage;
    }

    public String getUnknownCommand()
    {
        if (this.ircMessage == null) return null;

        return this.ircMessage.getCommand();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UnknownServerCommandEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UnknownServerCommandEvent.listeners;
    }
}
