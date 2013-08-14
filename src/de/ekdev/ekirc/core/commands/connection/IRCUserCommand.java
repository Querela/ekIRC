/**
 * IRCUserCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCUser;
import de.ekdev.ekirc.core.IRCUsernameFormatException;

/**
 * @author ekDev
 */
public final class IRCUserCommand implements AsIRCMessage
{
    private final String username;
    private final String realname;
    private final boolean invisible;
    public final static String COMMAND = "USER";

    public IRCUserCommand(String username, boolean invisible, String realname) throws NullPointerException, IRCUsernameFormatException
    {
        IRCUser.validateUsername(username);

        if (realname == null || realname.length() == 0)
        {
            throw new IllegalArgumentException("Argument realname can't be null or empty in a USER command!");
        }

        this.username = username;
        this.invisible = invisible;
        this.realname = realname;
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(4);
        params.add(this.username);
        params.add((this.invisible) ? "8" : "0"); // only invisible, wallops ?
        params.add("*"); // reserved - ignored
        params.add(this.realname);

        return new IRCMessage(null, IRCUserCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCUserCommand.COMMAND + " " + this.username + ((this.invisible) ? " 8 * :" : " 0 * :") + this.realname;
    }
}
