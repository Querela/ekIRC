/**
 * UserInfoUpdateEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class UserInfoUpdateEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCUser ircUser;

    public UserInfoUpdateEvent(IRCNetwork ircNetwork, IRCUser ircUser)
    {
        super(ircNetwork);

        this.ircUser = ircUser;
    }

    public IRCUser getIRCUser()
    {
        return this.ircUser;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UserInfoUpdateEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UserInfoUpdateEvent.listeners;
    }
}
