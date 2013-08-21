/**
 * IRCChannelModeCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCChannelModeCommand implements AsIRCMessage
{
    public final static String COMMAND = "MODE";

    private final IRCChannel ircChannel;
    private final String modes;
    private final List<String> modeparams;

    public IRCChannelModeCommand(IRCChannel ircChannel, String modes, List<String> modeparams)
    {
        Objects.requireNonNull(ircChannel, "ircChannel must not be null!");

        // TODO: mode validation
        if (modes != null && modes.trim().length() == 0)
        {
            throw new IllegalArgumentException("Invalid channel mode argument!");
        }

        this.ircChannel = ircChannel;
        this.modes = modes; // if null then requesting channel mode info

        this.modeparams = new ArrayList<>();
        if (modeparams != null) this.modeparams.addAll(modeparams);
    }

    public IRCChannelModeCommand(IRCChannel ircChannel, String modes, String... modeparams)
    {
        this(ircChannel, modes, new ArrayList<String>());

        if (modeparams != null)
        {
            for (String modeparam : modeparams)
            {
                this.modeparams.add(modeparam);
            }
        }
    }

    public IRCChannelModeCommand(IRCChannel ircChannel)
    {
        this(ircChannel, null, new ArrayList<String>());
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2 + this.modeparams.size());
        params.add(this.ircChannel.getName());
        if (this.modes != null)
        {
            params.add(this.modes);
            params.addAll(this.modeparams);
        }

        return new IRCMessage(null, IRCChannelModeCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        StringBuilder sb = new StringBuilder().append(IRCChannelModeCommand.COMMAND);
        sb.append(' ').append(this.ircChannel.getName());

        if (this.modes != null)
        {
            sb.append(' ').append(this.modes);

            for (String modeparam : this.modeparams)
            {
                sb.append(' ').append(modeparam);
            }
        }

        return sb.toString();
    }
}
