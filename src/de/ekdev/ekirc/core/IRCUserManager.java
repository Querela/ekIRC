/**
 * IRCUserManager.java
 */
package de.ekdev.ekirc.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ekDev
 */
public class IRCUserManager
{
    private final IRCNetwork ircNetwork;

    private final ConcurrentHashMap<String, IRCUser> users;

    public IRCUserManager(IRCNetwork ircNetwork)
    {
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork is null!");
        }

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

    protected IRCUser getOrCreateIRCUser(String nick)
    {
        IRCUser ircUser = this.users.get(nick);
        if (ircUser == null)
        {
            ircUser = new IRCUser(this, nick);
        }
        return ircUser;
    }

    public IRCUser getIRCUser(String nick)
    {
        if (nick == null) return null;

        return this.getOrCreateIRCUser(nick);
    }

    public IRCUser getIRCUserByPrefix(String prefix)
    {
        if (prefix == null) return null;

        return this.getIRCUser(IRCUser.getNickByPrefix(prefix));
    }

    public void addIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        this.users.put(ircUser.getNickname(), ircUser);
    }

    public void removeIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        this.removeIRCUser(ircUser.getNickname());
    }

    public void removeIRCUser(String nick)
    {
        if (nick == null) return;

        this.users.remove(nick);
    }
}
