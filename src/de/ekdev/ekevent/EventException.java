/**
 * EventException.java
 */
package de.ekdev.ekevent;

/**
 * @author ekDev
 */
public class EventException extends Exception
{
    private static final long serialVersionUID = 1L;

    // reroute to super class

    public EventException()
    {
        super();
    }

    public EventException(String message)
    {
        super(message);
    }

    public EventException(Throwable cause)
    {
        super(cause);
    }

    public EventException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
