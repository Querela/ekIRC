/**
 * IRCQuitCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ekDev
 */
public final class IRCQuitCommand implements AsIRCMessage
{
    private final String quitReason;
    public final static String COMMAND = "QUIT";

    public IRCQuitCommand(String reason)
    {
        this.quitReason = IRCUtils.emptyToNull(reason);
    }

    public IRCQuitCommand()
    {
        this(null);
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(1);
        if (this.quitReason != null) params.add(this.quitReason);

        return new IRCMessage(null, IRCQuitCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCQuitCommand.COMMAND + ((this.quitReason != null) ? " :" + this.quitReason : "");
    }
}
