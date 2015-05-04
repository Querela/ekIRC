/**
 * Utils.java
 */
package de.ekdev.ekevent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ekDev
 */
public class EventUtils
{
    public static Map<Class<? extends Event>, Set<RegisteredEventListener>> createRegisteredEventListenerMapping(
            EventListener listener)
    {
        // check arguments
        Objects.requireNonNull(listener, "listener must not be null!");

        Map<Class<? extends Event>, Set<RegisteredEventListener>> ret = new HashMap<Class<? extends Event>, Set<RegisteredEventListener>>();
        Set<Method> methods = new HashSet<Method>();

        // get all methods
        try
        {
            for (Method method : listener.getClass().getMethods())
            {
                methods.add(method);
            }
            for (Method method : listener.getClass().getDeclaredMethods())
            {
                methods.add(method);
            }
        }
        catch (NoClassDefFoundError e)
        {
            e.printStackTrace();
            return ret;
        }

        // check all methods if EventHandlers
        for (final Method method : methods)
        {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;

            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class
                    .isAssignableFrom(checkClass = method.getParameterTypes()[0]))
            {
                // " Attempt to register an invalid EventHandler method signature \""
                // + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }

            // -> http://stackoverflow.com/questions/5939575/generics-and-class-assubclass/5939824#5939824
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);

            Set<RegisteredEventListener> eventSet = ret.get(eventClass);
            if (eventSet == null)
            {
                eventSet = new HashSet<RegisteredEventListener>();
                ret.put(eventClass, eventSet);
            }

            EventExecutor executor = new EventExecutor()
            {
                public void execute(Event event, EventListener listener)
                        throws EventException
                {
                    try
                    {
                        if (!eventClass.isAssignableFrom(event.getClass()))
                        {
                            return;
                        }
                        method.invoke(listener, event);
                    }
                    catch (InvocationTargetException ex)
                    {
                        throw new EventException(ex.getCause());
                    }
                    catch (Throwable t)
                    {
                        throw new EventException(t);
                    }
                }
            };

            eventSet.add(new RegisteredEventListener(listener, executor));
        }

        return ret;
    }

    public static Class<? extends Event> getAsSubclass(Class<?> clazz)
    {
        if (Event.class.isAssignableFrom(clazz))
        {
            return clazz.asSubclass(Event.class);
        }
        return null;
    }
}
