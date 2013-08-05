/**
 * IRCMessageParsingTest.java
 */
package de.ekdev.ekirc.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCMessageFormatException;
import de.ekdev.ekirc.core.IRCMessageParser;

/**
 * @author ekDev
 *
 */
public class IRCMessageParsingTest
{

    /**
     * Test method for {@link de.ekdev.ekirc.core.IRCMessageParser#parseRawLine(java.lang.String)}.
     */
    @Test
    public void testParseRawLine()
    {
        String[] lines = new String[] {"", ""};
        
        IRCMessageParser imp = new IRCMessageParser();
        for (String line : lines)
        {
            IRCMessage im = null;
            try
            {
                im = imp.parseRawLine(line);
            }
            catch (IRCMessageFormatException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(line + "\n>>> " + im + "\n" /*+ im.asIRCString()*/);
            //assertArrayEquals(line.toCharArray(), im.asIRCString().toCharArray());
        }
    }

}
