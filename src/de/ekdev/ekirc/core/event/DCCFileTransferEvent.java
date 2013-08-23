/**
 * DCCFileTransferEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCDCCFileTransfer;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class DCCFileTransferEvent extends IRCDCCEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCDCCFileTransfer ircDCCFileTransfer;

    public DCCFileTransferEvent(IRCNetwork ircNetwork, IRCDCCFileTransfer ircDCCFileTransfer)
    {
        super(ircNetwork);

        this.ircDCCFileTransfer = ircDCCFileTransfer;
    }

    public IRCDCCFileTransfer getIRCDCCFileTransfer()
    {
        return this.ircDCCFileTransfer;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return DCCFileTransferEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return DCCFileTransferEvent.listeners;
    }
}
