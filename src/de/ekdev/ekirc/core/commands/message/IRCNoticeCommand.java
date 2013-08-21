/**
 * IRCNoticeCommand.java
 */
package de.ekdev.ekirc.core.commands.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCMessageProcessor;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class IRCNoticeCommand implements AsIRCMessage
{
    public final static String COMMAND = "NOTICE";
    private final String target;
    private final String message;

    protected IRCNoticeCommand(String target, String message, boolean quoteToLowLevel) throws NullPointerException
    {
        // TODO: validate target (servermask, hostmask, channel, user)
        Objects.requireNonNull(target, "target must not be null!");
        Objects.requireNonNull(message, "message must not be null!");

        // message = message.trim(); // TODO: or shall the whitespaces be left alone?
        if (message.trim().length() == 0)
        {
            throw new IllegalArgumentException("Argument message must not be empty!");
        }

        if (quoteToLowLevel) message = IRCMessageProcessor.quoteLowLevel(message);

        this.target = target;
        this.message = message;
    }

    public IRCNoticeCommand(IRCUser targetIRCUser, String message) throws NullPointerException
    {
        this(Objects.requireNonNull(targetIRCUser, "targetIRCUser must not be null!").getNickname(), message, true);
    }

    public IRCNoticeCommand(IRCChannel targetIRCChannel, String message) throws NullPointerException
    {
        this(Objects.requireNonNull(targetIRCChannel, "targetIRCChannel must not be null!").getName(), message, true);
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        params.add(this.target);
        params.add(this.message);

        return new IRCMessage(null, IRCNoticeCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCNoticeCommand.COMMAND + " " + this.target + " :" + this.message;
    }
}
