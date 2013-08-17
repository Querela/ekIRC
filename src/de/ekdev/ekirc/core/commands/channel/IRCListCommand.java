/**
 * IRCListCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;

/**
 * @author ekDev
 */
public class IRCListCommand implements AsIRCMessage
{
    public final static String COMMAND = "LIST";

    private final List<String> channels;
    private final String targetServer;

    public IRCListCommand()
    {
        this.channels = Collections.emptyList();
        this.targetServer = null;
    }

    public IRCListCommand(Collection<String> channels, String targetServer)
    {
        Objects.requireNonNull(channels, "channels must not be null!");
        // TODO: check null channels

        this.channels = new ArrayList<>(channels);
        this.targetServer = targetServer;
    }

    // ------------------------------------------------------------------------

    protected String concatenateChannels(List<String> channels)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.channels.size(); i++)
        {
            sb.append(this.channels.get(i)).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);

        if (this.channels.size() > 0)
        {
            params.add(this.concatenateChannels(this.channels));

            if (this.targetServer != null) params.add(this.targetServer);
        }

        return new IRCMessage(null, IRCListCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        StringBuilder sb = new StringBuilder(510);
        sb.append(IRCListCommand.COMMAND);

        if (this.channels.size() > 0)
        {
            // add channels
            sb.append(' ').append(this.concatenateChannels(this.channels));

            // add target server which should respond to this command
            if (this.targetServer != null) sb.append(' ').append(this.targetServer);
        }

        return sb.toString();
    }
}
