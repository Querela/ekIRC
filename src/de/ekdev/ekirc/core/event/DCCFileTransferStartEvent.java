/**
 * DCCFileTransferStartEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCDCCFileTransfer;
import de.ekdev.ekirc.core.IRCNetwork;

import java.io.File;

/**
 * @author ekDev
 */
public class DCCFileTransferStartEvent extends DCCFileTransferEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public DCCFileTransferStartEvent(IRCNetwork ircNetwork, IRCDCCFileTransfer ircDCCFileTransfer)
    {
        super(ircNetwork, ircDCCFileTransfer);
    }

    // ------------------------------------------------------------------------

    public File getLocalFile()
    {
        return this.getIRCDCCFileTransfer().getLocalFile();
    }

    public boolean isResumeAllowed()
    {
        return this.getIRCDCCFileTransfer().isResumeAllowed();
    }

    // -- ?

    public IRCDCCFileTransfer.Status getTransferStatus()
    {
        return this.getIRCDCCFileTransfer().getStatus();
    }

    public IRCDCCFileTransfer.Direction getTransferDirection()
    {
        return this.getIRCDCCFileTransfer().getDirection();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return DCCFileTransferStartEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return DCCFileTransferStartEvent.listeners;
    }
}
