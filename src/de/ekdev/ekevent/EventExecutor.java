/**
 * EventExecutor.java
 */
package de.ekdev.ekevent;

/**
 * @author ekDev
 */
public interface EventExecutor
{
    public void execute(Event event, EventListener listener) throws EventException;
}
