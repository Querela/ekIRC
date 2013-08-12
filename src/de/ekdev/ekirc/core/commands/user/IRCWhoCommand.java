/**
 * IRCWhoCommand.java
 */
package de.ekdev.ekirc.core.commands.user;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCWhoCommand implements AsIRCMessage
{
    public final static String COMMAND = "WHO";

    public IRCWhoCommand()
    {
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        return null;
    }

    @Override
    public String asIRCMessageString()
    {
        return null;
    }
}
