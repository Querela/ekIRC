/**
 * IRCDCCEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public abstract class IRCDCCEvent extends IRCNetworkEvent
{
    public IRCDCCEvent(IRCNetwork ircNetwork)
    {
        super(ircNetwork);
    }
}
