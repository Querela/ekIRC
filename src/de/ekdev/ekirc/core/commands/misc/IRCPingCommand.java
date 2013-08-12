/**
 * IRCPingCommand.java
 */
package de.ekdev.ekirc.core.commands.misc;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCPingCommand implements AsIRCMessage
{
    public final static String COMMAND = "PING";

    public IRCPingCommand()
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
