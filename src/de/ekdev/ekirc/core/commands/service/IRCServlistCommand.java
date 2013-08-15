/**
 * IRCServlistCommand.java
 */
package de.ekdev.ekirc.core.commands.service;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCServlistCommand implements AsIRCMessage
{
    public final static String COMMAND = "SERVLIST";

    private final String mask;
    private final String type;

    public IRCServlistCommand(String mask, String type)
    {
        // TODO: validateMask()
        // TODO: known command?

        this.mask = mask;
        this.type = type;
    }

    public IRCServlistCommand(String mask)
    {
        this(mask, null);
    }

    public IRCServlistCommand()
    {
        this(null, null);
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        if (this.mask != null)
        {
            params.add(this.mask);
            if (this.type != null)
            {
                params.add(this.type);
            }
        }

        return new IRCMessage(null, IRCServlistCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCServlistCommand.COMMAND
                + ((this.mask != null) ? " " + this.mask + ((this.type != null) ? " " + this.type : "") : "");
    }
}
