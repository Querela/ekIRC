/**
 * KickEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class KickEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;
    private final IRCUser source;
    private final IRCUser recipient;
    private final String reason;

    public KickEvent(IRCNetwork ircNetwork, IRCChannel ircChannel, IRCUser source, IRCUser recipient, String reason)
    {
        super(ircNetwork);
        this.ircChannel = ircChannel;
        this.source = source;
        this.recipient = recipient;
        this.reason = reason;
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    public IRCUser getSourceIRCUser()
    {
        return this.source;
    }

    public IRCUser getRecipientIRCUser()
    {
        return this.recipient;
    }

    public boolean hasReason()
    {
        return this.reason != null;
    }

    public String getReason()
    {
        return this.reason;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return KickEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return KickEvent.listeners;
    }
}
