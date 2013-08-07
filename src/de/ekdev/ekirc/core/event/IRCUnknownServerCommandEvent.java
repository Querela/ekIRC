/**
 * IRCUnknownServerCommandEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class IRCUnknownServerCommandEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final IRCMessage ircMessage;

    public IRCUnknownServerCommandEvent(IRCNetwork source, IRCMessage ircMessage)
    {
        super(source);
        this.ircMessage = ircMessage;
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
        return IRCUnknownServerCommandEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCUnknownServerCommandEvent.listeners;
    }
}
