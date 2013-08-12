/**
 * SimpleIRCServerContextTest.java
 */
package de.ekdev.ekirc.core.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.ekdev.ekevent.EventException;
import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCIdentity;
import de.ekdev.ekirc.core.IRCManager;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.event.IRCDisconnectEvent;
import de.ekdev.ekirc.core.event.NickChangeEvent;
import de.ekdev.ekirc.core.event.listener.UserConnectionRegistrator;

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
        // IRCNetwork inet = new IRCNetwork(ircManager, new IRCIdentity("rea ree", "pass"), "irc.irchighway.net", 6667);
        // IRCNetwork inet = new IRCNetwork(ircManager, new IRCIdentity("rea ree", "pass"), "irc.chatzona.org", 6667);
        IRCNetwork inet = new IRCNetwork(ircManager, new IRCIdentity("rea ree", "pass"), "irc.webchat.org", 6667);

        final String nick = "coor";
        // final String nick = "mike";

        try
        {
            ircManager.getEventManager().register(new UserConnectionRegistrator(inet, nick, "uerles", true));

            ircManager.getEventManager().register(new EventListener() {
                @EventHandler
                public void onNickChange(NickChangeEvent nce)
                {
                    nce.getIRCNetwork()
                            .getIRCConnectionLog()
                            .message(
                                    (nce.isMe() ? "MY " : "") + "NICK CHANGE FROM '" + nce.getOldNick() + "' TO '"
                                            + nce.getNewNick() + "'!");
                }
            });

            ircManager.getEventManager().register(new EventListener() {
                @EventHandler
                public void onDisconnect(IRCDisconnectEvent ide)
                {
                    final IRCNetwork inet = ide.getIRCNetwork();
                    final EventListener el = this;
                    final ScheduledExecutorService stp = Executors.newScheduledThreadPool(1);
                    final Runnable tr = new Runnable() {
                        private int counter = 0;

                        @Override
                        public void run()
                        {
                            if (inet.isConnected())
                            {
                                inet.getIRCConnectionLog().message("Shutting down reconnector ...");
                                stp.shutdown();
                                IRCDisconnectEvent.getListenerList().unregister(el);
                                return;
                            }

                            inet.getIRCConnectionLog().message("Trying to reconnect ... [" + ++counter + "]");
                            try
                            {
                                inet.reconnect();
                            }
                            catch (Exception e)
                            {
                                // display only the first time ...
                                if (counter == 1) inet.getIRCConnectionLog().exception(e);
                            }
                        }
                    };
                    stp.scheduleAtFixedRate(tr, 0, 10, TimeUnit.SECONDS);
                }
            });
        }
        catch (EventException e)
        {
            e.printStackTrace();
        }

        inet.connect();

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

            // inet.disconnect();
            //
            // inet.reconnect();

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
