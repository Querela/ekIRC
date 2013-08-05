/**
 * IRCEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.Event;

/**
 * @author ekDev
 */
public abstract class IRCEvent extends Event
{
    public IRCEvent(Object source)
    {
        super(source);
    }
}
