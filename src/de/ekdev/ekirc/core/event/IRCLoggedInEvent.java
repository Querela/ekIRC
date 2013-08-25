/**
 * IRCLoggedInEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCIdentity;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class IRCLoggedInEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public IRCLoggedInEvent(IRCNetwork ircNetwork)
    {
        super(ircNetwork);
    }

    public IRCIdentity getMyIRCIdentity()
    {
        return this.getIRCNetwork().getMyIRCIdentity();
    }

    public IRCUser getMyIRCUser()
    {
        return this.getMyIRCIdentity().getIRCUser();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCLoggedInEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCLoggedInEvent.listeners;
    }
}
