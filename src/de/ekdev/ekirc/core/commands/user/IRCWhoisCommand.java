/**
 * IRCWhoisCommand.java
 */
package de.ekdev.ekirc.core.commands.user;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCWhoisCommand implements AsIRCMessage
{
    public final static String COMMAND = "WHOIS";

    public IRCWhoisCommand()
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
