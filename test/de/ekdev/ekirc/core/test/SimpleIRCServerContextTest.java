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
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.commands.connection.IRCUserCommand;
import de.ekdev.ekirc.core.event.IRCConnectEvent;
import de.ekdev.ekirc.core.event.listener.IncrementalAutoNickRenamer;

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
        IRCNetwork inet = new IRCNetwork(ircManager);

        final String nick = "coor";

        try
        {
            ircManager.getEventManager().register(new EventListener() {
                @EventHandler
                public void onConnect(IRCConnectEvent ice)
                {
                    System.out.println("Connect - EventHandler");

                    try
                    {
                        ice.getIRCNetwork().getIRCManager().getEventManager()
                                .register(new IncrementalAutoNickRenamer(ice.getIRCNetwork(), nick, 0));
                    }
                    catch (EventException e)
                    {
                        ice.getIRCNetwork().getIRCConnectionLog().exception(e);
                    }

                    ice.getIRCNetwork().send(new IRCNickCommand(nick), new IRCUserCommand("userles", true, "realles"));

                }
            });
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }

        // inet.connect("irc.irchighway.net", 6667, "pass");
        // inet.connect("irc.chatzona.org", 6667, "pass");
        inet.connect("irc.webchat.org", 6667, "pass");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
            }

            inet.send(new IRCNickCommand("nickles"));

            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
            }

            // inet.getIRCConnectionLog().tryMoveLogFile("src/newlog.txt");

            inet.send(new IRCNickCommand("nick"));

            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
            }

            inet.send(new IRCNickCommand("coor"));

            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
            }
        }
        catch (IOException e)
        {
        }

        inet.quit("-)");

        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
        }

        if (inet.isConnected()) inet.disconnect();
    }
}
