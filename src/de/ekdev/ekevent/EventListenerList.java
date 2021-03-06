/**
 * EventListenerList.java
 */
package de.ekdev.ekevent;

import java.util.*;

/**
 * @author ekDev
 */
public class EventListenerList
{
    // TODO: check out: CopyOnWriteArrayList to avoid ConcurrentModificationException if removing listener while raising
    // events
    private final List<RegisteredEventListener> listeners;
    private static List<EventListenerList> allLists = new ArrayList<EventListenerList>();

    public EventListenerList()
    {
        this.listeners = new ArrayList<RegisteredEventListener>();

        synchronized (allLists)
        {
            allLists.add(this);
        }
    }

    public List<RegisteredEventListener> getListeners()
    {
        // now iterating over the list won't throw a ConcurrentModificationException
        // because it is operation on a static copy
        return Collections.unmodifiableList(new ArrayList<RegisteredEventListener>(this.listeners));
    }

    public static List<EventListenerList> getEventListenerLists()
    {
        return Collections.unmodifiableList(allLists);
    }

    // ------------------------------------------------------------------------

    public synchronized void register(RegisteredEventListener listener)
    {
        if (listener == null) return;
        if (!this.listeners.contains(listener)) this.listeners.add(listener);
    }

    public void registerAll(Collection<RegisteredEventListener> listeners)
    {
        for (RegisteredEventListener listener : listeners)
        {
            register(listener);
        }
    }

    // ------------------------------------------------------------------------

    public synchronized void unregister(RegisteredEventListener listener)
    {
        this.listeners.remove(listener);
    }

    public synchronized void unregister(EventListener listener)
    {
        for (ListIterator<RegisteredEventListener> li = this.listeners.listIterator(); li.hasNext(); )
        {
            if (li.next().getListener().equals(listener)) li.remove();
        }
    }

    public synchronized void unregister()
    {
        this.listeners.clear();
    }

    // ------------------------------------------------------------------------

    public static void unregisterAll()
    {
        synchronized (allLists)
        {
            for (EventListenerList list : allLists)
            {
                list.unregister();
            }
        }
    }

    public static void unregisterAll(EventListener listener)
    {
        synchronized (allLists)
        {
            for (EventListenerList list : allLists)
            {
                list.unregister(listener);
            }
        }
    }
}
