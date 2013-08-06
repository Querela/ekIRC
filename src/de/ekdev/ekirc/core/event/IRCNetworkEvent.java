/**
 * IRCNetworkEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public abstract class IRCNetworkEvent extends IRCEvent
{
    public IRCNetworkEvent(IRCNetwork source)
    {
        super(source);
    }

    // ------------------------------------------------------------------------

    public IRCNetwork getIRCNetwork()
    {
        return (IRCNetwork) super.getSource();
    }
}
