/**
 * IRCMessageEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public abstract class IRCMessageEvent extends IRCNetworkEvent
{

    public IRCMessageEvent(IRCNetwork ircNetwork)
    {
        super(ircNetwork);
    }

    // ------------------------------------------------------------------------

    // public abstract IRCUser getSourceIRCUser();

    public abstract String getMessage();
}
