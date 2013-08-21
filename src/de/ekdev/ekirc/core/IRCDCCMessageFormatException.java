/**
 * IRCDCCMessageFormatException.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCDCCMessageFormatException extends IRCMessageFormatException
{
    private static final long serialVersionUID = 1L;

    public IRCDCCMessageFormatException()
    {
    }

    public IRCDCCMessageFormatException(String message)
    {
        super(message);
    }

    public IRCDCCMessageFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
