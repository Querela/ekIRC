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
    
    public void updateIRCChannelList(IRCChannelList ircChannelList)
    {
        Objects.requireNonNull(ircChannelList, "ircChannelList must not be null!");
        
        this.ircChannelList = ircChannelList;
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

    public void addIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.channels.put(ircChannel.getName(), ircChannel);
    }

    public void removeIRCCChannel(String name)
    {
        if (name == null) return;

        this.channels.remove(name);
    }

    public void removeIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.removeIRCCChannel(ircChannel.getName());
    }
}
