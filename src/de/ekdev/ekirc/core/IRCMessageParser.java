/**
 * IRCMessageParser.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ekDev
 */
public class IRCMessageParser
{

    public IRCMessage parseRawLine(String line) throws IRCMessageFormatException
    {
        // throw more exceptions?
        if (line == null || line.length() == 0) return null;

        int i = line.indexOf(IRCMessage.IRC_SPACE);

        // optional prefix
        String prefix = null;
        if (line.charAt(0) == IRCMessage.IRC_COLON)
        {
            // only prefix? -> not allowed
            if (i == -1)
            {
                throw new IRCMessageFormatException("IRC message with a prefix only!");
            }
            // colon and prefix have to stand together
            if (line.charAt(1) == IRCMessage.IRC_SPACE)
            {
                throw new IRCMessageFormatException(
                        "Wrong IRC message format! after prefix colon can't follow a space.");
            }

            prefix = line.substring(1, i);
            line = line.substring(i + 1);
            i = line.indexOf(IRCMessage.IRC_SPACE);
        }

        String command;
        if (i == -1)
        {
            command = line;
            line = "";
        }
        else
        {
            command = line.substring(0, i);
            line = line.substring(i + 1);
        }

        List<String> params = new ArrayList<String>(15);
        while (line.length() > 0)
        {
            if (line.charAt(0) == IRCMessage.IRC_COLON)
            {
                // remove colon of trailing parameter
                params.add(line.substring(1));
                break;
            }
            i = line.indexOf(IRCMessage.IRC_SPACE);
            if (i == -1)
            {
                params.add(line);
                break;
            }
            else
            {
                params.add(line.substring(0, i));
                line = line.substring(i + 1);
            }
        }
        if (params.size() > IRCMessage.MAX_PARAM_COUNT)
        {
            throw new IRCMessageFormatException("IRC message with over 15 parameters!");
        }

        return new IRCMessage(prefix, command, params);
    }
}
