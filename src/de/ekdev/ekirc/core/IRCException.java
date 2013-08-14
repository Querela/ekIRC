/**
 * IRCException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCException extends Exception
{
    private static final long serialVersionUID = 1L;

    public IRCException()
    {
    }

    public IRCException(String message)
    {
        super(message);
    }

    public IRCException(Throwable cause)
    {
        super(cause);
    }

    public IRCException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public IRCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
