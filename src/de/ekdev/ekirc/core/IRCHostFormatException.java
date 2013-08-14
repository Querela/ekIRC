/**
 * IRCHostFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCHostFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCHostFormatException()
    {
    }

    public IRCHostFormatException(String message)
    {
        super(message);
    }

    public IRCHostFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCHostFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
