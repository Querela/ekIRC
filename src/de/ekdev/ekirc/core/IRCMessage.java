/**
 * IRCMessage.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ekDev
 */
public class IRCMessage implements AsIRCMessage
{
    public final static int MAX_IRC_LINE_LENGTH = 512;
    public final static int MAX_PARAM_COUNT = 15;
    public final static String IRC_LINE_ENDING = "\r\n"; // CRLF,

    public final static char IRC_SPACE = ' '; // = 0x20; // SPACE,
    public final static char IRC_COLON = ':'; // = 0x3a; // COLON

    private final String prefix;
    private final String command;
    private final int numericReply;
    private final boolean isNumericReply;
    private final List<String> params;

    public IRCMessage(final String prefix, final String command, List<String> params)
    {
        this.prefix = prefix;
        this.command = command;

        if (params == null) params = new ArrayList<String>();

        this.params = new ArrayList<String>(params);

        // or runtime computation
        boolean inr = false;
        int code = -1;
        try
        {
            code = Integer.parseInt(command);
            inr = true;
        }
        catch (NumberFormatException e)
        {
            inr = false;
        }
        this.numericReply = code;
        this.isNumericReply = inr;
    }

    public IRCMessage(final String command, List<String> params)
    {
        this(null, command, params);
    }

    // public IRCMessage(final String prefix, final String command, final String... params)
    // {
    //
    // }

    // ------------------------------------------------------------------------

    public final String getPrefix()
    {
        return this.prefix;
    }

    public final boolean hasPrefix()
    {
        return (this.prefix != null);
    }

    public final boolean isServerPrefix()
    {
        if (!this.hasPrefix()) return true;

        return this.prefix.indexOf("@") == -1 && (this.prefix.indexOf('.') != -1 || this.prefix.indexOf(':') != -1);
    }

    public final String getCommand()
    {
        return this.command;
    }

    public final int getNumericReply()
    {
        return this.numericReply;
    }

    public final boolean isNumericReply()
    {
        return this.isNumericReply;
    }

    public final List<String> getParams()
    {
        return Collections.unmodifiableList(this.params);
    }

    public String asIRCString()
    {
        StringBuilder sb = new StringBuilder(IRCMessage.MAX_IRC_LINE_LENGTH - 2);
        if (this.hasPrefix())
        {
            sb.append(IRCMessage.IRC_COLON).append(this.prefix).append(IRCMessage.IRC_SPACE);
        }
        sb.append(this.command);
        for (int i = 0; i < this.params.size(); i++)
        {
            sb.append(IRCMessage.IRC_SPACE);
            if ((i + 1) == this.params.size() /* && this.params.get(i).indexOf(IRCMessage.IRC_SPACE) != -1 */)
            {
                sb.append(IRCMessage.IRC_COLON);
            }
            sb.append(this.params.get(i));
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return "IRCMessage [prefix=" + prefix + ", command=" + command + ", isNumericReply=" + isNumericReply
                + ", params=" + params + "]";
    }

    @Override
    public IRCMessage asIRCMessage()
    {
        return this;
    }

    @Override
    public String asIRCMessageString()
    {
        return this.asIRCString();
    }
}
