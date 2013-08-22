/**
 * IRCUtils.java
 */
package de.ekdev.ekirc.core;

import java.util.List;

/**
 * @author ekDev
 */
public class IRCUtils
{
    protected IRCUtils()
    {
    }

    // ------------------------------------------------------------------------

    public static String emptyToNull(String str)
    {
        if (str == null) return null;

        if (str.trim().length() == 0) return null;

        return str;
    }

    public static String concatenate(List<String> strings, String separator)
    {
        if (separator == null) separator = "";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strings.size(); i++)
        {
            if (i != 0) sb.append(separator);
            sb.append(strings.get(i));
        }

        return sb.toString();
    }
}
