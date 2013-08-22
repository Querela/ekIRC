/**
 * IRCTopicCommand.java
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
public class IRCTopicCommand implements AsIRCMessage
{
    public final static String COMMAND = "TOPIC";

    private final String channel;
    private final String topic;

    public IRCTopicCommand(IRCChannel ircChannel)
    {
        this.channel = Objects.requireNonNull(ircChannel, "ircChannel must not be null!").getName();
        this.topic = null;
    }

    public IRCTopicCommand(IRCChannel ircChannel, String newTopic)
    {
        this.channel = Objects.requireNonNull(ircChannel, "ircChannel must not be null!").getName();
        this.topic = Objects.requireNonNull(newTopic, "newTopic must not be null!");
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        params.add(this.channel);
        if (this.topic != null) params.add(this.topic);

        return new IRCMessage(null, IRCTopicCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCTopicCommand.COMMAND + ' ' + this.channel + ((this.topic != null) ? " :" + this.topic : "");
    }
}
