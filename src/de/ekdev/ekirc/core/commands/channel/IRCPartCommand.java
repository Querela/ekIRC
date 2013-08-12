/**
 * IRCPartCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCPartCommand implements AsIRCMessage
{
    public final static String COMMAND = "PART";

    public IRCPartCommand()
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
