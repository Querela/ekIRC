/**
 * IRCListCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCUtils;

/**
 * @author ekDev
 */
public class IRCListCommand implements AsIRCMessage
{
    public final static String COMMAND = "LIST";

    private final String channels;
    private final String targetServer;

    public IRCListCommand()
    {
        this.channels = null;
        this.targetServer = null;
    }

    public IRCListCommand(Collection<String> channels, String targetServer)
    {
        Objects.requireNonNull(channels, "channels must not be null!");
        // TODO: check null channels

        this.channels = this.concatenateChannels(new ArrayList<String>(channels));
        this.targetServer = IRCUtils.emptyToNull(targetServer);
    }

    // ------------------------------------------------------------------------

    protected String concatenateChannels(List<String> channels)
    {
        return IRCUtils.emptyToNull(IRCUtils.concatenate(channels, ","));
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);

        if (this.channels != null)
        {
            params.add(this.channels);

            if (this.targetServer != null) params.add(this.targetServer);
        }

        return new IRCMessage(null, IRCListCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(IRCListCommand.COMMAND);

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
