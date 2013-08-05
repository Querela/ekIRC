/**
 * IRCManager.java
 */
package de.ekdev.ekirc.core;

import de.ekdev.ekevent.EventManager;

/**
 * @author ekDev
 */
public class IRCManager
{
    protected EventManager eventManager;

    public IRCManager()
    {
        this.eventManager = this.createDefaultEventManager();
    }

    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------

    protected EventManager createDefaultEventManager()
    {
        return new EventManager();
    }

    // ------------------------------------------------------------------------

    public EventManager getEventManager()
    {
        return this.eventManager;
    }
}
