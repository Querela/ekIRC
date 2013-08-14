/**
 * IRCUsernameFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCUsernameFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCUsernameFormatException()
    {
    }

    public IRCUsernameFormatException(String message)
    {
        super(message);
    }

    public IRCUsernameFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCUsernameFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
