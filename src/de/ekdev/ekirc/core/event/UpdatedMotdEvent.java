/**
 * UpdatedMotdEvent.java
 */
package de.ekdev.ekirc.core.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCNetworkInfo;

/**
 * @author ekDev
 */
public class UpdatedMotdEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final List<String> motd;

    public UpdatedMotdEvent(IRCNetwork source)
    {
        super(source);
        this.motd = new ArrayList<>(this.getIRCNetworkInfo().getMotd());
    }

    public List<String> getMotd()
    {
        return Collections.unmodifiableList(this.motd);
    }

    public String getMotdString()
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.motd.size(); i++)
        {
            if (i != 0) sb.append(System.lineSeparator());
            sb.append(this.motd.get(i));
        }

        return sb.toString();
    }

    // ------------------------------------------------------------------------

    public IRCNetworkInfo getIRCNetworkInfo()
    {
        return this.getIRCNetwork().getIRCNetworkInfo();
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return UpdatedMotdEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return UpdatedMotdEvent.listeners;
    }
}
