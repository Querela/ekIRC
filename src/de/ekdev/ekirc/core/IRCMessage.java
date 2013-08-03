/**
 * IRCMessage.java
 */
package de.ekdev.ekirc.core;

import java.util.List;

/**
 * @author ekDev
 */
public class IRCMessage
{
    final static int MAX_IRC_LINE_LENGTH = 512;
    final static int MAX_PARAM_COUNT = 15;
    final static String IRC_LINE_ENDING = "\r\n"; // CRLF,

    String prefix;
    String command;
    List<String> params;
}
