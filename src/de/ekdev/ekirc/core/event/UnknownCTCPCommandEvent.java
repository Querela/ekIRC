/**
 * UnknownCTCPCommandEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCExtendedDataMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class UnknownCTCPCommandEvent extends UnknownServerCommandEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCExtendedDataMessage ircExtendedDataMessage;

    public UnknownCTCPCommandEvent(IRCNetwork ircNetwork, IRCMessage ircMessage,
            IRCExtendedDataMessage ircExtendedDataMessage)
    {
        super(ircNetwork, ircMessage);

        this.ircExtendedDataMessage = ircExtendedDataMessage;
    }

    public IRCExtendedDataMessage getIRCExtendedDataMessage()
    {
        return this.ircExtendedDataMessage;
    }

    @Override
    public String getUnknownCommand()
    {
        if (this.ircExtendedDataMessage == null) return null;

        return this.ircExtendedDataMessage.getTag();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UnknownCTCPCommandEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UnknownCTCPCommandEvent.listeners;
    }
}
