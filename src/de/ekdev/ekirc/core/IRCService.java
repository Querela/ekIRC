/**
 * IRCService.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCService
{
    private final IRCServiceManager ircServiceManager;

    private final String nickname;
    private final String servername;

    // ------------------------------------------------------------------------

    public IRCService(IRCServiceManager ircServiceManager, String nickname, String servername)
            throws NullPointerException
    {
        Objects.requireNonNull(ircServiceManager, "ircServiceManager must not be null!");
        Objects.requireNonNull(nickname, "nickname must not be null!");
        Objects.requireNonNull(servername, "servername must not be null!");

        this.ircServiceManager = ircServiceManager;
        this.nickname = nickname;
        this.servername = servername;

        // automatically add to manager
        this.ircServiceManager.addIRCService(this);
    }

    // ------------------------------------------------------------------------

    public String getNickname()
    {
        return this.nickname;
    }

    public String getServername()
    {
        return this.servername;
    }
}
