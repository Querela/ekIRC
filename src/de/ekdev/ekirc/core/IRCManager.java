/**
 * IRCManager.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ekdev.ekevent.EventException;
import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekevent.EventManager;
import de.ekdev.ekirc.core.event.PingEvent;
import de.ekdev.ekirc.core.event.IRCUnknownServerCommandEvent;

/**
 * @author ekDev
 */
public class IRCManager
{
    private final EventManager eventManager;

    protected final List<IRCNetwork> networks;

    public IRCManager()
    {
        this.eventManager = this.createDefaultEventManager();

        this.networks = Collections.synchronizedList(new ArrayList<IRCNetwork>());

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

        try
        {
            this.eventManager.register(this.createDefaultUnknownCommandEventListener());
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

    // ------------------------------------------------------------------------

    protected EventListener createDefaultPingEventListener()
    {
        return new EventListener() {
            @EventHandler
            public void onPingEvent(PingEvent ipe)
            {
                ipe.getIRCNetwork().sendPong(ipe.getPingValue());
            }
        };
    }

    protected EventListener createDefaultUnknownCommandEventListener()
    {
        return new EventListener() {
            @EventHandler
            public void onUnknownCommandEvent(IRCUnknownServerCommandEvent iusce)
            {
                try
                {
                    iusce.getIRCNetwork()
                            .getIRCConnectionLog()
                            .message(
                                    iusce.getUnknownCommand() + " - Unknown Server Reply code! [Network: "
                                            + iusce.getIRCNetwork().getName() + "]");
                }
                catch (Exception e)
                {
                }
            }
        };
    }

    // protected EventListener createDefaultIRCdServerVersionEventListener()

    // ------------------------------------------------------------------------

    // TODO: add methods
    // - create/add new network + connect to it with identity
    // - remove network?
    // -> map<network, identity>? -> no
    // cross network operations? or extra class/user implementation?

    protected void addIRCNetwork(IRCNetwork ircNetwork)
    {
        if (ircNetwork == null) return;

        this.networks.add(ircNetwork);
    }

    // --------------------------------

    public List<IRCNetwork> getNetworks()
    {
        return Collections.unmodifiableList(this.networks);
    }

    // ------------------------------------------------------------------------

    public EventManager getEventManager()
    {
        return this.eventManager;
    }
}
