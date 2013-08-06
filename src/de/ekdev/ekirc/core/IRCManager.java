/**
 * IRCManager.java
 */
package de.ekdev.ekirc.core;

import de.ekdev.ekevent.EventException;
import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekevent.EventManager;
import de.ekdev.ekirc.core.event.IRCPingEvent;

/**
 * @author ekDev
 */
public class IRCManager
{
    protected EventManager eventManager;

    public IRCManager()
    {
        this.eventManager = this.createDefaultEventManager();

        this.initializeEventSystem();
    }

    // ------------------------------------------------------------------------

    protected void initializeEventSystem()
    {
        // create a default ping event listener for all networks
        // TODO: check for differences in network's pings
        try
        {
            this.eventManager.register(this.createDefaultPingEventListener());
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------

    protected EventManager createDefaultEventManager()
    {
        return new EventManager();
    }

    protected EventListener createDefaultPingEventListener()
    {
        return new EventListener() {
            @EventHandler
            public void onPingEvent(IRCPingEvent ipe)
            {
                ipe.getIRCNetwork().sendPong(ipe.getPingValue());
            }
        };
    }

    // ------------------------------------------------------------------------

    public EventManager getEventManager()
    {
        return this.eventManager;
    }
}
