/**
 * IRCReplyConstantsTest.java
 */
package de.ekdev.ekirc.core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.ekdev.ekirc.core.IRCNumericServerReply;

/**
 * @author ekDev
 */
public class IRCReplyConstantsTest
{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * Test method for {@link de.ekdev.ekirc.core.IRCNumericServerReply#getCode()}.
     */
    @Test
    public void testGetCode()
    {
        // fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link de.ekdev.ekirc.core.IRCNumericServerReply#byCode(int)}.
     */
    @Test
    public void testByCode()
    {
        // fail("Not yet implemented"); // TODO
    }

    @Test
    public void test()
    {
        IRCNumericServerReply insr1 = IRCNumericServerReply.ERR_NICKNAMEINUSE;
        System.out.println(insr1);
        System.out.println("ordinal=" + insr1.ordinal());
        System.out.println("code=" + insr1.getCode());

        assertEquals(IRCNumericServerReply.ERR_NICKNAMEINUSE, IRCNumericServerReply.valueOf("ERR_NICKNAMEINUSE"));

        insr1 = IRCNumericServerReply.byCode(insr1.getCode());

        switch (insr1)
        {
            case ERR_NICKNAMEINUSE:
            {
                System.out.println("SWITCH CASE ERR_NICKNAMEINUSE");
                break;
            }
            default:
            {
                System.out.println("SWITCH CASE DEFAULT");
                break;
            }
        }

        System.out.println(IRCNumericServerReply.byCode("324"));
        System.out.println(IRCNumericServerReply.byCode(null));
        System.out.println(IRCNumericServerReply.byCode("abc"));
        System.out.println(IRCNumericServerReply.byCode("800"));

        for (int i = 250; i < 270; i += 2)
            System.out.println(IRCNumericServerReply.byCode(i));

        assertNull(IRCNumericServerReply.byCode(-1));
        assertNull(IRCNumericServerReply.byCode(800));
    }

}
