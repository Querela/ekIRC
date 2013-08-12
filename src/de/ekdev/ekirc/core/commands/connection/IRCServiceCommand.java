/**
 * IRCServiceCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCServiceCommand implements AsIRCMessage
{
    public final static String COMMAND = "SERVICE";

    public IRCServiceCommand()
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
