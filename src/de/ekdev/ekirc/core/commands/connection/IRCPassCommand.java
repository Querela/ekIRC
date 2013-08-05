/**
 * PassCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public final class IRCPassCommand implements AsIRCMessage
{
    private final String password;
    public final static String COMMAND = "PASS";

    public IRCPassCommand(String password)
    {
        if (password == null || password.length() == 0)
        {
            throw new IllegalArgumentException("Argument password can't be null or empty in a PASS command!");
        }
        if (password.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IllegalArgumentException("Argument password can't contain a space character!");
        }

        this.password = password;
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(1);
        params.add(this.password);

        return new IRCMessage(null, IRCPassCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCPassCommand.COMMAND + " " + this.password;
    }
}
