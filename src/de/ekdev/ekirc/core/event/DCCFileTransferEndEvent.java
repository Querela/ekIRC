/**
 * DCCFileTransferEndEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCDCCFileTransfer;
import de.ekdev.ekirc.core.IRCNetwork;

import java.io.File;

/**
 * @author ekDev
 */
public class DCCFileTransferEndEvent extends DCCFileTransferEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public DCCFileTransferEndEvent(IRCNetwork ircNetwork, IRCDCCFileTransfer ircDCCFileTransfer)
    {
        super(ircNetwork, ircDCCFileTransfer);
    }

    // ------------------------------------------------------------------------

    public File getLocalFile()
    {
        return this.getIRCDCCFileTransfer().getLocalFile();
    }

    public boolean successful()
    {
        return this.getTransferStatus() == IRCDCCFileTransfer.Status.FINISHED;
    }

    public boolean complete()
    {
        return this.getTransferStatus() == IRCDCCFileTransfer.Status.FINISHED;
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
        return DCCFileTransferEndEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return DCCFileTransferEndEvent.listeners;
    }
}
