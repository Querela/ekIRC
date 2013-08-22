/**
 * IRCWhoCommand.java
 */
package de.ekdev.ekirc.core.commands.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCWhoCommand implements AsIRCMessage
{
    public final static String COMMAND = "WHO";

    private final String mask;
    private final boolean onlyOperators;

    public IRCWhoCommand(String mask, boolean onlyOperators)
    {
        if (mask != null && mask.trim().length() == 0) mask = null;
        if (mask == null && onlyOperators) mask = "0";

        // mask of null will most probably trigger help for command ... or not

        this.mask = mask;
        this.onlyOperators = onlyOperators;
    }

    public IRCWhoCommand()
    {
        // list all
        this("0", false);
    }

    public IRCWhoCommand(IRCChannel ircChannel, boolean onlyOperators)
    {
        this(Objects.requireNonNull(ircChannel, "ircChannel must not be null!").getName(), onlyOperators);
    }

    public IRCWhoCommand(IRCChannel ircChannel)
    {
        this(ircChannel, false);
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        if (this.mask != null)
        {
            params.add(this.mask);
            if (this.onlyOperators) params.add("o");
        }

        return new IRCMessage(null, IRCWhoCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCWhoCommand.COMMAND
                + ((this.mask != null) ? ' ' + this.mask + ((this.onlyOperators) ? " o" : "") : "");
    }
}
