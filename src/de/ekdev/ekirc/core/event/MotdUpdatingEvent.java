/**
 * MotdUpdatingEvent.java
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
public class MotdUpdatingEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();
    private final List<String> oldMotd;

    public MotdUpdatingEvent(IRCNetwork source)
    {
        super(source);
        this.oldMotd = new ArrayList<>(this.getIRCNetworkInfo().getMotd());
    }

    public List<String> getOldMotd()
    {
        return Collections.unmodifiableList(this.oldMotd);
    }

    public String getOldMotdString()
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.oldMotd.size(); i++)
        {
            if (i != 0) sb.append(System.lineSeparator());
            sb.append(this.oldMotd.get(i));
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
        return MotdUpdatingEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return MotdUpdatingEvent.listeners;
    }
}
