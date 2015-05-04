/**
 * IRCPongCommand.java
 */
package de.ekdev.ekirc.core.commands.misc;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ekDev
 */
public class IRCPongCommand implements AsIRCMessage
{
    private final String pingValue;
    public final static String COMMAND = "PONG";

    public IRCPongCommand(String pingValue)
    {
        if (pingValue == null || pingValue.length() == 0)
        {
            throw new IllegalArgumentException("Argument pingValue can't be null or empty in a PONG command!");
        }

        this.pingValue = pingValue;
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(1);
        params.add(this.pingValue);

        return new IRCMessage(null, IRCPongCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCPongCommand.COMMAND + " :" + this.pingValue;
    }
}
