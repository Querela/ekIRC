/**
 * NoticeToChannelEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class NoticeToChannelEvent extends IRCMessageEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCUser sourceIRCUser;
    private final IRCChannel targetIRCChannel;
    private final String message;

    public NoticeToChannelEvent(IRCNetwork ircNetwork, IRCUser sourceIRCUser, IRCChannel targetIRCChannel,
            String message)
    {
        super(ircNetwork);

        this.sourceIRCUser = sourceIRCUser;
        this.targetIRCChannel = targetIRCChannel;
        this.message = message;
    }

    // ------------------------------------------------------------------------

    public IRCUser getSourceIRCUser()
    {
        return this.sourceIRCUser;
    }

    public IRCChannel getTargetIRCChannel()
    {
        return this.targetIRCChannel;
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
        return NoticeToChannelEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return NoticeToChannelEvent.listeners;
    }
}
