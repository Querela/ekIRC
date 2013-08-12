/**
 * IRCErrorCommand.java
 */
package de.ekdev.ekirc.core.commands.misc;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCErrorCommand implements AsIRCMessage
{
    public final static String COMMAND = "ERROR";

    public IRCErrorCommand()
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
