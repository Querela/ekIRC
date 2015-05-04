/**
 * IRCNickMessage.java
 */
package de.ekdev.ekirc.core.commands.connection;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNicknameFormatException;
import de.ekdev.ekirc.core.IRCUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ekDev
 */
public final class IRCNickCommand implements AsIRCMessage
{
    private final String nick;
    public final static String COMMAND = "NICK";

    public IRCNickCommand(String nick)
            throws NullPointerException, IRCNicknameFormatException
    {
        this.nick = IRCUser.validateNickname(nick);
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
        return IRCNickCommand.COMMAND + ' ' + this.nick;
    }
}
