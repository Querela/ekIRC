/**
 * IRCPrivateMessageCommand.java
 */
package de.ekdev.ekirc.core.commands.message;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCPrivateMessageCommand implements AsIRCMessage
{
    public final static String COMMAND = "PRIVMSG";
    
    public IRCPrivateMessageCommand()
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
