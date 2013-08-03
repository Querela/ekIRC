/**
 * TestEvent.java
 */
package de.ekdev.ekevent.test;

import de.ekdev.ekevent.Event;
import de.ekdev.ekevent.EventListenerList;

/**
 * @author ekDev
 * 
 */
public class TestEvent extends Event
{
    public TestEvent(Object source)
    {
        super(source);
    }

    // required !

    private final static EventListenerList listeners = new EventListenerList();

    @Override
    public EventListenerList getListeners()
    {
        return TestEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return TestEvent.listeners;
    }
}
