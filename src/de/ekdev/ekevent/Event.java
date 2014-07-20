/**
 * Event.java
 */
package de.ekdev.ekevent;

/**
 * @author ekDev
 */
public abstract class Event
{
    private Object source;
    // private final static EventListenerList listeners = new EventListenerList();
    public final static String METHOD_LISTENERLIST = "getListenerList";

    public Event(Object source)
    {
        this.source = source;
    }

    public Object getSource()
    {
        return this.source;
    }

    public abstract EventListenerList getListeners();

    // public static EventListenerList getListenerList()
    // {
    // return Event.listeners;
    // }
}
