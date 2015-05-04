/**
 * IRCJoinCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import de.ekdev.ekirc.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCJoinCommand implements AsIRCMessage
{
    public final static String COMMAND = "JOIN";

    public final static String PARAM_LEAVE_ALL = "0";

    private final String channel;
    private final String key;

    public IRCJoinCommand(String channel, String key)
            throws NullPointerException, IRCChannelNameFormatException
    {
        this.channel = IRCChannel.validateChannelname(channel);
        // IRCChannel.validateChannelkey(key);
        this.key = IRCUtils.emptyToNull(key);
    }

    public IRCJoinCommand(String channel)
            throws NullPointerException, IRCChannelNameFormatException
    {
        this(channel, null);
    }

    public IRCJoinCommand(List<String> channellist)
            throws NullPointerException, IRCChannelNameFormatException
    {
        Objects.requireNonNull(channellist, "channellist must not be null!");
        if (channellist.size() == 0)
        {
            throw new IllegalArgumentException("channellist must not be empty!");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < channellist.size(); i++)
        {
            if (i != 0) sb.append(',');
            sb.append(IRCChannel.validateChannelname(channellist.get(i)));
        }

        this.channel = sb.toString();
        this.key = null;
    }

    public IRCJoinCommand()
    {
        // leave all channels
        this.channel = IRCJoinCommand.PARAM_LEAVE_ALL;
        this.key = null;
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        params.add(this.channel);
        if (this.key != null) params.add(this.key);

        return new IRCMessage(null, IRCJoinCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCJoinCommand.COMMAND + ' ' + this.channel + ((this.key != null) ? ' ' + this.key : "");
    }
}
