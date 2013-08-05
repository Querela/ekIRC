/**
 * GenericEventManager.java
 */
package de.ekdev.ekevent;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author ekDev
 */
public class EventManager
{
    public void register(EventListener listener, Class<? extends Event> event) throws EventException
    {
        this.getEventListeners(event).registerAll(EventUtils.createRegisteredEventListenerMapping(listener).get(event));
    }

    public void register(EventListener listener) throws EventException
    {
        for (Map.Entry<Class<? extends Event>, Set<RegisteredEventListener>> entry : EventUtils
                .createRegisteredEventListenerMapping(listener).entrySet())
        {
            this.getEventListeners(entry.getKey()).registerAll(entry.getValue());
        }
    }

    public void unregister(EventListener listener, Class<? extends Event> event) throws EventException
    {
        this.getEventListeners(event).unregister(listener);

    }

    public void unregister(EventListener listener)
    {
        EventListenerList.unregisterAll(listener);
    }

    // ------------------------------------------------------------------------

    public void dispatch(Event event)
    {
        if (event == null) return;

        // TODO: async?
        synchronized (this)
        {
            this.fireEvent(event);
        }

    }

    private void fireEvent(Event event)
    {
        EventListenerList ell = event.getListeners();
        for (RegisteredEventListener rel : ell.getListeners())
        {
            try
            {
                rel.callEvent(event);
            }
            catch (EventException e)
            {
                e.printStackTrace();
            }
        }
    }

    // ------------------------------------------------------------------------

    private EventListenerList getEventListeners(Class<? extends Event> type) throws EventException
    {
        try
        {
            Method method = getRegistrationClass(type).getDeclaredMethod(Event.METHOD_LISTENERLIST);
            method.setAccessible(true);
            return (EventListenerList) method.invoke(null);
        }
        catch (Exception e)
        {
            throw new EventException(e);
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) throws EventException
    {
        try
        {
            clazz.getDeclaredMethod(Event.METHOD_LISTENERLIST);
            return clazz;
        }
        catch (NoSuchMethodException e)
        {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass()))
            {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            }
            else
            {
                throw new EventException("Unable to find listener list for event " + clazz.getName());
            }
        }
    }
}
