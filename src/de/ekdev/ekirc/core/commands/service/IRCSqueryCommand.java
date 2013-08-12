/**
 * IRCSqueryCommand.java
 */
package de.ekdev.ekirc.core.commands.service;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCSqueryCommand implements AsIRCMessage
{
    public final static String COMMAND = "SQUERY";

    public IRCSqueryCommand()
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
