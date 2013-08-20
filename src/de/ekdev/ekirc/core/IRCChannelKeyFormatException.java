/**
 * IRCChannelKeyFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCChannelKeyFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCChannelKeyFormatException()
    {
    }

    public IRCChannelKeyFormatException(String message)
    {
        super(message);
    }

    public IRCChannelKeyFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCChannelKeyFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
