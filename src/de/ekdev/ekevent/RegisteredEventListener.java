/**
 * RegisteredEventListener.java
 */
package de.ekdev.ekevent;

/**
 * @author ekDev
 */
public class RegisteredEventListener
{
    private final EventListener listener;
    private final EventExecutor executor;

    public RegisteredEventListener(final EventListener listener, final EventExecutor executor)
    {
        this.listener = listener;
        this.executor = executor;
    }

    public EventListener getListener()
    {
        return this.listener;
    }

    public void callEvent(final Event event)
            throws EventException
    {
        executor.execute(event, listener);
    }

}
