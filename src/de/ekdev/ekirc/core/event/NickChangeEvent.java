/**
 * NickChangeEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class NickChangeEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final IRCUser ircUser;
    private final String oldNick;
    private final String newNick;

    public NickChangeEvent(IRCNetwork source, IRCUser ircUser, String oldNick, String newNick)
    {
        super(source);
        this.ircUser = ircUser;
        this.oldNick = oldNick;
        this.newNick = newNick;
    }

    public IRCUser getIRCUser()
    {
        return this.ircUser;
    }

    public String getOldNick()
    {
        return this.oldNick;
    }

    public String getNewNick()
    {
        return this.newNick;
    }

    public boolean isMe()
    {
        return this.ircUser.isMe();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return NickChangeEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return NickChangeEvent.listeners;
    }
}
