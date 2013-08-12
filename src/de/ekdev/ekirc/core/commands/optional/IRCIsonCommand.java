/**
 * IRCIsonCommand.java
 */
package de.ekdev.ekirc.core.commands.optional;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCIsonCommand implements AsIRCMessage
{
    public final static String COMMAND = "ISON";

    public IRCIsonCommand()
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
