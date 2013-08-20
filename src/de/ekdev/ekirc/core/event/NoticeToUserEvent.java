/**
 * NoticeToUserEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class NoticeToUserEvent extends IRCMessageEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCUser sourceIRCUser;
    private final IRCUser targetIRCUser;
    private final String message;

    public NoticeToUserEvent(IRCNetwork ircNetwork, IRCUser sourceIRCUser, IRCUser targetIRCUser, String message)
    {
        super(ircNetwork);

        this.sourceIRCUser = sourceIRCUser;
        this.targetIRCUser = targetIRCUser;
        this.message = message;
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

    @Override
    public String getMessage()
    {
        return this.message;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return NoticeToUserEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return NoticeToUserEvent.listeners;
    }
}
