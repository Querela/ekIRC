/**
 * IRCNickMessage.java
 */
package de.ekdev.ekirc.core.commands.connection;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public final class IRCNickCommand implements AsIRCMessage
{
    private final String nick;
    public final static String COMMAND = "NICK";

    public IRCNickCommand(String nick)
    {
        if (nick == null || nick.length() == 0)
        {
            throw new IllegalArgumentException("Argument nick can't be null or empty in a NICK command!");
        }
        if (nick.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IllegalArgumentException("Argument nick can't contain a space character!");
        }

        this.nick = nick;
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(1);
        params.add(this.nick);

        return new IRCMessage(null, IRCNickCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCNickCommand.COMMAND + " " + this.nick;
    }
}
