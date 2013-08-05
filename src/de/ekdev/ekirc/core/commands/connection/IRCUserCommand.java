/**
 * IRCUserCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public final class IRCUserCommand implements AsIRCMessage
{
    private final String username;
    private final String realname;
    private final boolean invisible;
    public final static String COMMAND = "USER";

    public IRCUserCommand(String username, boolean invisible, String realname)
    {
        if (username == null || username.length() == 0)
        {
            throw new IllegalArgumentException("Argument username can't be null or empty in a USER command!");
        }
        if (username.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IllegalArgumentException("Argument username can't contain a space character!");
        }

        if (realname == null || realname.length() == 0)
        {
            throw new IllegalArgumentException("Argument realname can't be null or empty in a USER command!");
        }
        if (realname.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IllegalArgumentException("Argument realname can't contain a space character!");
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
