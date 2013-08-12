/**
 * IRCOperCommand.java
 */
package de.ekdev.ekirc.core.commands.connection;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCOperCommand implements AsIRCMessage
{
    public final static String COMMAND = "OPER";

    public IRCOperCommand()
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
