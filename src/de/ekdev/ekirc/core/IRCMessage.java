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
public class IRCMessage
{
    public final static int MAX_IRC_LINE_LENGTH = 512;
    public final static int MAX_PARAM_COUNT = 15;
    public final static String IRC_LINE_ENDING = "\r\n"; // CRLF,

    public final static char IRC_SPACE = ' '; // = 0x20; // SPACE,
    public final static char IRC_COLON = ':'; // = 0x3b; // COLON

    private final String prefix;
    private final String command;
    private final List<String> params;

    public IRCMessage(final String prefix, final String command, final List<String> params)
    {

        this.prefix = prefix;
        this.command = command;
        this.params = new ArrayList<String>(params);
    }

    // ------------------------------------------------------------------------

    public String getPrefix()
    {
        return this.prefix;
    }

    public boolean hasPrefix()
    {
        return (this.prefix != null);
    }

    public String getCommand()
    {
        return this.command;
    }

    public List<String> getParams()
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
        return "IRCMessage [prefix=" + prefix + ", command=" + command + ", params=" + params + "]";
    }
}
