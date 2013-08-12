/**
 * IRCAwayCommand.java
 */
package de.ekdev.ekirc.core.commands.optional;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCAwayCommand implements AsIRCMessage
{
    public final static String COMMAND = "AWAY";

    public IRCAwayCommand()
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
