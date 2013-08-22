/**
 * IRCChannelManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ekDev
 */
public class IRCChannelManager
{
    private final IRCNetwork ircNetwork;

    private final ConcurrentHashMap<String, IRCChannel> channels;

    private IRCChannelList ircChannelList;

    public IRCChannelManager(IRCNetwork ircNetwork) throws NullPointerException
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;

        this.channels = new ConcurrentHashMap<String, IRCChannel>();
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }

    // --------------------------------

    public IRCChannelList getIRCChannelList()
    {
        return this.ircChannelList;
    }

    protected void updateIRCChannelList(IRCChannelList ircChannelList)
    {
        Objects.requireNonNull(ircChannelList, "ircChannelList must not be null!");

        this.ircChannelList = ircChannelList;
    }

    protected void createIRCChannelsFromIRCChannelList(IRCChannelList ircChannelList)
    {
        if (ircChannelList == null) return;

        for (IRCChannelList.Entry entry : ircChannelList)
        {
            IRCChannel ircChannel = this.getOrCreateIRCChannel(entry.getName());
            ircChannel.setTopic(entry.getTopic());
        }
    }

    // ------------------------------------------------------------------------

    protected IRCChannel getOrCreateIRCChannel(String name)
    {
        IRCChannel ircChannel = this.channels.get(name);
        if (ircChannel == null)
        {
            ircChannel = new IRCChannel(this, name);
        }
        return ircChannel;
    }

    public IRCChannel getIRCChannel(String name)
    {
        if (name == null) return null;

        return this.getOrCreateIRCChannel(name);
    }

    public boolean hasIRCChannel(String name)
    {
        if (name == null) return false;

        return this.channels.containsKey(name);
    }

    protected void addIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.channels.put(ircChannel.getName(), ircChannel);
    }

    protected void removeIRCCChannel(String name)
    {
        if (name == null) return;

        IRCChannel ircChannel = this.channels.remove(name);

        if (ircChannel != null) ircChannel.removeAllIRCUsers();
    }

    protected void removeIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.removeIRCCChannel(ircChannel.getName());
    }

    // ------------------------------------------------------------------------

    protected void removeIRCUserFromAllIRCChannels(IRCUser ircUser)
    {
        for (IRCChannel ircChannel : this.channels.values())
        {
            ircChannel.removeIRCUser(ircUser);
        }
    }
}
