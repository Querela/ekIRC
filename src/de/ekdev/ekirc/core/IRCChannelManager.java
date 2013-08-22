/**
 * IRCChannelManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.ekdev.ekirc.core.commands.channel.IRCListCommand;

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

    public Set<IRCChannel> getIRCChannels()
    {
        return Collections.unmodifiableSet(new HashSet<IRCChannel>(this.channels.values()));
    }

    public IRCChannelList getIRCChannelList()
    {
        return this.ircChannelList;
    }

    protected void updateIRCChannelList(IRCChannelList ircChannelList)
    {
        Objects.requireNonNull(ircChannelList, "ircChannelList must not be null!");

        this.ircChannelList = ircChannelList;
    }

    protected void createIRCChannelFromIRCChannelListEntry(IRCChannelList.Entry ircChannelListEntry)
    {
        if (ircChannelListEntry == null) return;

        IRCChannel ircChannel = this.getOrCreateIRCChannel(ircChannelListEntry.getName(), true);
        ircChannel.setTopic(ircChannelListEntry.getTopic());
    }

    protected void createIRCChannelsFromIRCChannelList(IRCChannelList ircChannelList)
    {
        if (ircChannelList == null) return;

        for (IRCChannelList.Entry entry : ircChannelList)
        {
            this.createIRCChannelFromIRCChannelListEntry(entry);
        }
    }

    // ------------------------------------------------------------------------

    protected IRCChannel getOrCreateIRCChannel(String name, boolean autoCreateIfNull)
    {
        IRCChannel ircChannel = this.channels.get(name);
        if (ircChannel == null && autoCreateIfNull)
        {
            ircChannel = new IRCChannel(this, name);
        }
        return ircChannel;
    }

    public IRCChannel getIRCChannel(String name)
    {
        if (name == null) return null;

        return this.getOrCreateIRCChannel(name, true);
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

    // ------------------------------------------------------------------------

    public void refreshChannelList()
    {
        this.ircNetwork.send(new IRCListCommand());
    }
}
