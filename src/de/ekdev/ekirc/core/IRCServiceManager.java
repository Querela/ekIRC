/**
 * IRCServiceManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ekDev
 */
public class IRCServiceManager
{
    private final IRCNetwork ircNetwork;

    private final ConcurrentHashMap<String, IRCService> services;
    private long lastUpdate; // Date.getTime(); - more efficient

    public IRCServiceManager(IRCNetwork ircNetwork) throws NullPointerException
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;

        this.services = new ConcurrentHashMap<String, IRCService>();
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }

    // ------------------------------------------------------------------------

    public void addIRCService(IRCService ircService)
    {
        if (ircService == null) return;

        // TODO: or composed name ...
        this.services.put(ircService.getNickname(), ircService);
    }
}
