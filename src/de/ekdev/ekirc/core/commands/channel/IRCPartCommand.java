/**
 * IRCPartCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCUtils;

/**
 * @author ekDev
 */
public class IRCPartCommand implements AsIRCMessage
{
    public final static String COMMAND = "PART";

    private final String ircChannel;
    private final String reason;

    public IRCPartCommand(IRCChannel ircChannel, String reason)
    {
        this.ircChannel = Objects.requireNonNull(ircChannel, "ircChannel must not be null!").getName();
        this.reason = IRCUtils.emptyToNull(reason);
    }

    public IRCPartCommand(IRCChannel ircChannel)
    {
        this(ircChannel, null);
    }

    public IRCPartCommand(Collection<IRCChannel> ircChannels, String reason)
    {
        this.ircChannel = IRCUtils.concatenateChannelNames(Objects.requireNonNull(ircChannels,
                "ircChannels must not be null!"));
        this.reason = IRCUtils.emptyToNull(reason);
    }

    public IRCPartCommand(Collection<IRCChannel> ircChannels)
    {
        this(ircChannels, null);
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        params.add(this.ircChannel);
        if (this.reason != null) params.add(this.reason);

        return new IRCMessage(null, IRCPartCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCPartCommand.COMMAND + ' ' + ircChannel + ((this.reason != null) ? " :" + this.reason : "");
    }
}
