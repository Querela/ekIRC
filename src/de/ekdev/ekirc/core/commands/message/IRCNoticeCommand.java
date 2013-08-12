/**
 * IRCNoticeCommand.java
 */
package de.ekdev.ekirc.core.commands.message;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCNoticeCommand implements AsIRCMessage
{
    public final static String COMMAND = "NOTICE";

    public IRCNoticeCommand()
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
