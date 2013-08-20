/**
 * PartEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class PartEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;
    private final IRCUser ircUser;
    private final String reason;

    public PartEvent(IRCNetwork ircNetwork, IRCChannel ircChannel, IRCUser ircUser, String reason)
    {
        super(ircNetwork);
        // Objects.requireNonNull(ircChannel, "ircChannel must not be null!");
        // Objects.requireNonNull(ircUser, "ircUser must not be null!");
        if (reason != null && reason.trim().length() == 0) reason = null;

        this.ircChannel = ircChannel;
        this.ircUser = ircUser;
        this.reason = reason;
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    public IRCUser getIRCUser()
    {
        return this.ircUser;
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
        return PartEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return PartEvent.listeners;
    }
}
