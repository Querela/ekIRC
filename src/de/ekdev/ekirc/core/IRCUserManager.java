/**
 * IRCUserManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ekDev
 */
public class IRCUserManager
{
    private final IRCNetwork ircNetwork;

    private final ConcurrentHashMap<String, IRCUser> users;

    public IRCUserManager(IRCNetwork ircNetwork)
            throws NullPointerException
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;

        // this.users = Collections.synchronizedMap(new HashMap<String, IRCUser>());
        this.users = new ConcurrentHashMap<String, IRCUser>();
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }

    // ------------------------------------------------------------------------

    protected IRCUser getOrCreateIRCUser(String nick, boolean autoCreateIfNull)
    {
        IRCUser ircUser = this.users.get(nick);
        if (ircUser == null && autoCreateIfNull)
        {
            ircUser = new IRCUser(this, nick);
        }
        return ircUser;
    }

    public IRCUser getIRCUser(String nick)
    {
        if (nick == null) return null;

        return this.getOrCreateIRCUser(nick, true);
    }

    public IRCUser getIRCUserByPrefix(String prefix)
    {
        if (prefix == null) return null;

        return this.getIRCUser(IRCUser.getNickByPrefix(prefix));
    }

    protected void addIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        this.users.put(ircUser.getNickname(), ircUser);
    }

    protected void removeIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        this.removeIRCUser(ircUser.getNickname());
    }

    protected void removeIRCUser(String nick)
    {
        if (nick == null) return;

        IRCUser ircUser = this.users.remove(nick);

        // remove user from all channels
        if (ircUser != null) this.ircNetwork.getIRCChannelManager().removeIRCUserFromAllIRCChannels(ircUser);
    }
}
