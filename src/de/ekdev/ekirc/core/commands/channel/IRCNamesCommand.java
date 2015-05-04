/**
 * IRCNamesCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCNamesCommand implements AsIRCMessage
{
    public final static String COMMAND = "NAMES";

    private final String channels;
    private final String targetServer;

    public IRCNamesCommand()
    {
        // list all channels and their occupants
        this.channels = null;
        this.targetServer = null;
    }

    public IRCNamesCommand(IRCChannel ircChannel, String targetServer)
    {
        this.channels = Objects.requireNonNull(ircChannel, "ircChannels must not be null!").getName();
        this.targetServer = IRCUtils.emptyToNull(targetServer);
    }

    public IRCNamesCommand(IRCChannel ircChannel)
    {
        this(ircChannel, null);
    }

    public IRCNamesCommand(Collection<IRCChannel> ircChannels, String targetServer)
    {
        this.channels = Objects.requireNonNull(
                IRCUtils.concatenateChannelNames(Objects.requireNonNull(ircChannels, "ircChannels must not be null!")),
                "ircChannels must not be empty");
        this.targetServer = IRCUtils.emptyToNull(targetServer);
    }

    public IRCNamesCommand(Collection<IRCChannel> ircChannels)
    {
        this(ircChannels, null);
    }

    // --------------------------------

    // constructors for IRCChannelList(.Entry)?

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);

        if (this.channels != null)
        {
            params.add(this.channels);

            if (this.targetServer != null) params.add(this.targetServer);
        }

        return new IRCMessage(null, IRCNamesCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(IRCNamesCommand.COMMAND);

        if (this.channels != null)
        {
            // add channels
            sb.append(' ').append(this.channels);

            // add target server which should respond to this command
            if (this.targetServer != null) sb.append(' ').append(this.targetServer);
        }

        return sb.toString();
    }
}
