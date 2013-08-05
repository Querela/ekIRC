/**
 * SimpleIRCServerContextTest.java
 */
package de.ekdev.ekirc.core.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.ekdev.ekevent.EventException;
import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCManager;
import de.ekdev.ekirc.core.IRCServerContext;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.commands.connection.IRCUserCommand;
import de.ekdev.ekirc.core.commands.misc.IRCPongCommand;
import de.ekdev.ekirc.core.event.IRCConnectEvent;
import de.ekdev.ekirc.core.event.IRCPingEvent;

/**
 * @author ekDev
 */
public class SimpleIRCServerContextTest
{
    public SimpleIRCServerContextTest()
    {
    }

    public static void main(String[] args)
    {

        IRCManager ircManager = new IRCManager();
        IRCServerContext isc = new IRCServerContext(ircManager);

        try
        {
            ircManager.getEventManager().register(new EventListener() {
                @EventHandler
                public void onConnect(IRCConnectEvent ice)
                {
                    System.out.println("Connect - EventHandler");
                    ice.getIRCServerContext().send(new IRCNickCommand("nickles"),
                            new IRCUserCommand("userles", true, "realles"));

                }
            });
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }

        try
        {
            ircManager.getEventManager().register(new EventListener() {
                private int counter = 0;

                @EventHandler
                public void doThePong(IRCPingEvent ipe)
                {
                    System.out.println("Ping - EventHandler (" + ++counter + ")");
                    ipe.getIRCServerContext().sendImmediate(new IRCPongCommand(ipe.getPingValue()));

                }
            });
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }

        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
        }

        // isc.connect("irc.irchighway.net", 6667, "pass");
        isc.connect("irc.chatzona.org", 6667, "pass");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
            }
        }
        catch (IOException e)
        {
        }

        isc.quit("-)");

        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
        }

        isc.disconnect();
    }

}
