/**
 * IRCUserModeCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCUserModeCommand implements AsIRCMessage
{
    public final static String COMMAND = "MODE";

    public IRCUserModeCommand()
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
