/**
 * IRCDCCManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCDCCManager
{
    private final IRCNetwork ircNetwork;

    public IRCDCCManager(IRCNetwork ircNetwork)
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;
    }

    // ------------------------------------------------------------------------

    public boolean processRequest(IRCUser sourceIRCUser, String request)
    {
        return false;
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }
}
