/**
 * IRCChannelList.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ekDev
 */
public class IRCChannelList implements Iterable<IRCChannelList.Entry>
{
    private final Set<IRCChannelList.Entry> channels;

    // TODO: creation date? for comparing?
    // TODO: union, intersection, substraction ...?
    // TODO: set? list shouldn't contains doubles if from server

    public IRCChannelList()
    {
        this.channels = Collections.unmodifiableSet(new LinkedHashSet<IRCChannelList.Entry>());
    }

    public IRCChannelList(Set<IRCChannelList.Entry> channelEntries)
    {
        Objects.requireNonNull(channelEntries, "channelEntries must not be null!");

        this.channels = Collections.unmodifiableSet(new LinkedHashSet<IRCChannelList.Entry>(channelEntries));
    }

    // --------------------------------

    public boolean isEmpty()
    {
        return this.channels.isEmpty();
    }

    public int size()
    {
        return this.channels.size();
    }

    public boolean contains(Object o) throws ClassCastException, NullPointerException
    {
        return this.channels.contains(o);
    }

    // --------------------------------

    public Set<IRCChannelList.Entry> getSet()
    {
        return this.channels;
    }

    // --------------------------------

    @Override
    public Iterator<IRCChannelList.Entry> iterator()
    {
        return this.channels.iterator();
    }

    @Override
    public String toString()
    {
        return "IRCChannelList [channels=" + channels + "]";
    }

    // ------------------------------------------------------------------------

    public static class Entry implements Comparable<IRCChannelList.Entry>
    {
        private final String name;
        private final int users;
        private final String topic;

        public Entry(String name, int numberOfUsers, String topic) throws NullPointerException,
                IllegalArgumentException
        {
            Objects.requireNonNull(name, "name must not be null!");
            if (numberOfUsers < 1)
                throw new IllegalArgumentException("numberOfUsers must not be lower or equal zero!");
            if (topic != null && topic.trim().equals("")) topic = null;

            this.name = name;
            this.users = numberOfUsers;
            this.topic = topic;
        }

        public Entry(String name, int numberOfUsers)
        {
            this(name, numberOfUsers, null);
        }

        // ----------------------------

        public String getName()
        {
            return this.name;
        }

        public int getNumberOfUsers()
        {
            return this.users;
        }

        public String getTopic()
        {
            return this.topic;
        }

        public boolean hasTopic()
        {
            return this.topic != null;
        }

        // ----------------------------

        @Override
        public int compareTo(IRCChannelList.Entry o)
        {
            return this.name.compareToIgnoreCase(o.name);
        }

        // ----------------------------

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((topic == null) ? 0 : topic.hashCode());
            result = prime * result + users;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            IRCChannelList.Entry other = (IRCChannelList.Entry) obj;
            if (name == null)
            {
                if (other.name != null) return false;
            }
            else if (!name.equals(other.name)) return false;
            if (topic == null)
            {
                if (other.topic != null) return false;
            }
            else if (!topic.equals(other.topic)) return false;
            if (users != other.users) return false;
            return true;
        }

        @Override
        public String toString()
        {
            return "Entry [name=" + name + ", users=" + users + ", topic=" + topic + "]";
        }
    }

    // ------------------------------------------------------------------------

    public static class Builder
    {
        private final LinkedHashSet<IRCChannelList.Entry> channels;

        public Builder()
        {
            this.channels = new LinkedHashSet<>();
        }

        // ----------------------------

        public synchronized Builder add(IRCChannelList.Entry channelEntry)
        {
            if (channelEntry != null) this.channels.add(channelEntry);

            return this;
        }

        public Builder add(String channelName, int numberOfUsers, String topic)
        {
            return this.add(new IRCChannelList.Entry(channelName, numberOfUsers, topic));
        }
        
        public Builder add(String channelName, String numberOfUsersString, String topic) throws IRCMessageFormatException
        {
            int numberOfUsers = 0;
            try
            {
                numberOfUsers = Integer.parseInt(numberOfUsersString);
            }
            catch (NumberFormatException e)
            {
                throw new IRCMessageFormatException("Invalid number!", e);
            }
            return this.add(new IRCChannelList.Entry(channelName, numberOfUsers, topic));
        }

        public synchronized Builder clear()
        {
            this.channels.clear();

            return this;
        }

        public synchronized Builder sort()
        {
            // order maintained from insertion (-> insert a sorted list)
            // TODO: benchmark?
            ArrayList<IRCChannelList.Entry> list = new ArrayList<>(this.channels);
            Collections.sort(list);
            this.channels.clear();
            this.channels.addAll(list);

            // TreeSet<IRCChannelList.Entry> tree = new TreeSet<>(this.channels);
            // this.channels.clear();
            // this.channels.addAll(tree);

            return this;
        }

        public synchronized IRCChannelList build()
        {
            return new IRCChannelList(this.channels);
        }
    }
}
