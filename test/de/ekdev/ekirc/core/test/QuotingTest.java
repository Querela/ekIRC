/**
 * QuotingTest.java
 */
package de.ekdev.ekirc.core.test;

import de.ekdev.ekirc.core.IRCIdentity;
import de.ekdev.ekirc.core.IRCManager;
import de.ekdev.ekirc.core.IRCMessageProcessor;
import de.ekdev.ekirc.core.IRCNetwork;

/**
 * @author ekDev
 */
public class QuotingTest
{

    public QuotingTest()
    {
    }

    public static void main(String[] args)
    {
        IRCManager ircManager = new IRCManager();
        IRCIdentity ircIdentity = new IRCIdentity("a", "a");
        IRCNetwork ircNetwork = new IRCNetwork(ircManager, ircIdentity, "", 0);

        new IRCMessageProcessor(ircNetwork) {
            public void doSomething()
            {
                String message1 = "\u0001ACTION :barfs on the floor\u0001";
                System.out.println(message1);
                System.out.println(IRCMessageProcessor.quoteCTCP(message1));
                System.out.println(IRCMessageProcessor.dequoteCTCP(IRCMessageProcessor.quoteCTCP(message1)));
            }
        }.doSomething();

        new IRCMessageProcessor(ircNetwork) {
            public void doSomething()
            {
                // String message1 = "\\n\\t\\big\\020\\001\\000\\\\:";
                String message1 = "\n\t\big\020\001\000\\:";
                System.out.println(message1);
                System.out.println(IRCMessageProcessor.quoteCTCP(message1));
                System.out.println(IRCMessageProcessor.dequoteCTCP(IRCMessageProcessor.quoteCTCP(message1)));
            }
        }.doSomething();

        new IRCMessageProcessor(ircNetwork) {
            public void doSomething()
            {
                String message1 = "PRIVMSG victim :\001SED \n\t\big\020\\a\000\\\\:\001";
                System.out.println(message1);
                System.out.println(IRCMessageProcessor.dequoteCTCP(message1));
            }
        }.doSomething();
    }
}
