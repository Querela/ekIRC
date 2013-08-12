/**
 * IRCWhowasCommand.java
 */
package de.ekdev.ekirc.core.commands.user;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCWhowasCommand implements AsIRCMessage
{
    public final static String COMMAND = "WHOWAS";

    public IRCWhowasCommand()
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
