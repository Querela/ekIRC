/**
 * QuitEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class QuitEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final IRCUser ircUser;
    private final String reason;

    public QuitEvent(IRCNetwork ircNetwork, IRCUser ircUser, String reason)
    {
        super(ircNetwork);
        this.ircUser = ircUser;
        this.reason = reason;
    }

    public IRCUser getIRCUser()
    {
        return this.ircUser;
    }

    public String getReason()
    {
        return this.reason;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return QuitEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return QuitEvent.listeners;
    }
}
