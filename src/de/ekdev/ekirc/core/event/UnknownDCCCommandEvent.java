/**
 * UnknownDCCCommandEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCDCCMessage;
import de.ekdev.ekirc.core.IRCExtendedDataMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class UnknownDCCCommandEvent extends UnknownCTCPCommandEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCDCCMessage ircDCCMessage;

    public UnknownDCCCommandEvent(IRCNetwork ircNetwork, IRCMessage ircMessage,
            IRCExtendedDataMessage ircExtendedDataMessage, IRCDCCMessage ircDCCMessage)
    {
        super(ircNetwork, ircMessage, ircExtendedDataMessage);

        this.ircDCCMessage = ircDCCMessage;
    }

    // ------------------------------------------------------------------------

    public boolean hasIRCDCCMessage()
    {
        return this.ircDCCMessage != null;
    }

    public IRCDCCMessage getIRCDCCMessage()
    {
        return this.ircDCCMessage;
    }

    @Override
    public String getUnknownCommand()
    {
        if (this.ircDCCMessage == null) return super.getUnknownCommand();

        return this.ircDCCMessage.getType();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UnknownDCCCommandEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UnknownDCCCommandEvent.listeners;
    }
}
