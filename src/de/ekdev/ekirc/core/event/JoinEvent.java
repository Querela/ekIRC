/**
 * JoinEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class JoinEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCChannel ircChannel;
    private final IRCUser ircUser;

    public JoinEvent(IRCNetwork source, IRCChannel ircChannel, IRCUser ircUser)
    {
        super(source);
        // Objects.requireNonNull(ircChannel, "ircChannel must not be null!");
        // Objects.requireNonNull(ircUser, "ircUser must not be null!");

        this.ircChannel = ircChannel;
        this.ircUser = ircUser;
    }

    public IRCChannel getIRCChannel()
    {
        return this.ircChannel;
    }

    public IRCUser getIRCUser()
    {
        return this.ircUser;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return JoinEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return JoinEvent.listeners;
    }
}
