/**
 * IRCConnectCommand.java
 */
package de.ekdev.ekirc.core.commands.server;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCConnectCommand implements AsIRCMessage
{
    public final static String COMMAND = "CONNECT";

    public IRCConnectCommand()
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
