/**
 * UserModeChangeEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class UserModeChangeEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCUser sourceIRCUser;
    private final IRCUser targetIRCUser;
    private final String mode;

    public UserModeChangeEvent(IRCNetwork ircNetwork, IRCUser sourceIRCUser, IRCUser targetIRCUser, String mode)
    {
        super(ircNetwork);

        this.sourceIRCUser = sourceIRCUser;
        this.targetIRCUser = targetIRCUser;
        this.mode = mode;
    }

    // ------------------------------------------------------------------------

    public IRCUser getSourceIRCUser()
    {
        return this.sourceIRCUser;
    }

    public IRCUser getTargetIRCUser()
    {
        return this.targetIRCUser;
    }

    public String getMode()
    {
        return this.mode;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UserModeChangeEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UserModeChangeEvent.listeners;
    }
}
