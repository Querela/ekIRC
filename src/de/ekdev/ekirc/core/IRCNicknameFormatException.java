/**
 * IRCNicknameFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCNicknameFormatException extends IRCException
{
    private static final long serialVersionUID = 1L;

    public IRCNicknameFormatException()
    {
    }

    public IRCNicknameFormatException(String message)
    {
        super(message);
    }

    public IRCNicknameFormatException(Throwable cause)
    {
        super(cause);
    }

    public IRCNicknameFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
