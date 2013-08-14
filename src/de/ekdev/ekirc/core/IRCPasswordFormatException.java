/**
 * IRCPasswordFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCPasswordFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCPasswordFormatException()
    {
    }

    public IRCPasswordFormatException(String message)
    {
        super(message);
    }

    public IRCPasswordFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCPasswordFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
