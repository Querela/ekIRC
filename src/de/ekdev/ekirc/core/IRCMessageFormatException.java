/**
 * IRCMessageFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCMessageFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCMessageFormatException()
    {
    }

    public IRCMessageFormatException(String message)
    {
        super(message);
    }

    public IRCMessageFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
