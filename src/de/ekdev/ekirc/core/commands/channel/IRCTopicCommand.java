/**
 * IRCTopicCommand.java
 */
package de.ekdev.ekirc.core.commands.channel;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCTopicCommand implements AsIRCMessage
{
    public final static String COMMAND = "TOPIC";

    public IRCTopicCommand()
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
