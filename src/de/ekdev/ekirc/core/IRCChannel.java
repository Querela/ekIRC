/**
 * IRCChannel.java
 */
package de.ekdev.ekirc.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ekDev
 */
public class IRCChannel
{
    public final static int MAX_CHANNEL_NAME_LENGTH = 50;
    public final static String CHANNEL_PREFIXES = "&#+!";

    private final IRCChannelManager ircChannelManager;

    private final String name;
    private String topic;

    // http://webtoman.com/opera/panel/ircdmodes.html
    private String modes; // -> EnumSet?

    private final Set<IRCUser> users;

    // TODO: more Sets for different statuses of users? Map<User, Status?>
    // for set operations are different sets better

    // ------------------------------------------------------------------------

    public IRCChannel(IRCChannelManager ircChannelManager, String name, String topic) throws NullPointerException
    {
        Objects.requireNonNull(ircChannelManager, "ircChannelManager must not be null!");
        Objects.requireNonNull(name, "name must not be null!");

        this.ircChannelManager = ircChannelManager;
        this.name = name;
        this.topic = topic;

        this.users = Collections.synchronizedSet(new HashSet<IRCUser>());

        // automatically add to manager
        this.ircChannelManager.addIRCChannel(this);
    }

    public IRCChannel(IRCChannelManager ircChannelManager, String name) throws NullPointerException
    {
        this(ircChannelManager, name, null);
    }

    // ------------------------------------------------------------------------

    public final IRCChannelManager getIRCChannelManager()
    {
        return this.ircChannelManager;
    }

    // ------------------------------------------------------------------------
    // Fields

    public String getName()
    {
        return this.name;
    }

    public String getTopic()
    {
        return this.topic;
    }

    protected void setTopic(String topic)
    {
        this.topic = topic;
    }

    // --------------------------------

    public Set<IRCUser> getUsers()
    {
        return Collections.unmodifiableSet(this.users);
    }

    // ------------------------------------------------------------------------
    // Update

    protected void addIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        // TODO: add to different sets (MODE dependent)

        this.users.add(ircUser);

        // add channel reference in user
        ircUser.addIRCChannel(this);
    }

    protected void removeIRCUser(IRCUser ircUser)
    {
        if (ircUser == null) return;

        // TODO: remove from all other sets too

        this.users.remove(ircUser);

        // remove channel reference from user
        ircUser.removeIRCChannel(this);
    }

    protected void removeAllIRCUsers()
    {
        // remove the channel reference from all users of this channel
        for (IRCUser user : this.users)
        {
            user.removeIRCChannel(this);
        }

        this.users.clear();
    }

    // ------------------------------------------------------------------------
    // Actions

    // ------------------------------------------------------------------------

    public static boolean validateChannelname(String channelname)
    {
        // TODO: validateChannelname
        return true;
    }

    public static boolean validateChannelkey(String channelkey)
    {
        // TODO: validateChannelkey
        return true;
    }

    // ------------------------------------------------------------------------
}
