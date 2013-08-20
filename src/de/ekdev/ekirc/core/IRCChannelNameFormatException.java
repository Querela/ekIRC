/**
 * IRCChannelNameFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCChannelNameFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCChannelNameFormatException()
    {
    }

    public IRCChannelNameFormatException(String message)
    {
        super(message);
    }

    public IRCChannelNameFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCChannelNameFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
