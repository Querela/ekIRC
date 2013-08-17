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
import de.ekdev.ekirc.core.IRCChannelList;
import de.ekdev.ekirc.core.IRCIdentity;
import de.ekdev.ekirc.core.IRCManager;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCNicknameFormatException;
import de.ekdev.ekirc.core.IRCUsernameFormatException;
import de.ekdev.ekirc.core.commands.channel.IRCListCommand;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.event.NickChangeEvent;
import de.ekdev.ekirc.core.event.ChannelListUpdateEvent;
import de.ekdev.ekirc.core.event.listener.AutoReconnector;
import de.ekdev.ekirc.core.event.listener.UserConnectionRegistrator;

/**
 * @author ekDev
 */
public class SimpleIRCServerContextTest
{
    public SimpleIRCServerContextTest()
    {
    }

    public static void main(String[] args) throws NullPointerException, IRCNicknameFormatException,
            IRCUsernameFormatException
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
                public void newChannelList(ChannelListUpdateEvent ucle)
                {
                    if (ucle.hasOldIRCChannelList())
                    {
                        ucle.getIRCNetwork().getIRCConnectionLog()
                                .message("old channel list: size = " + ucle.getOldIRCChannelList().size());
                    }
                    ucle.getIRCNetwork().getIRCConnectionLog()
                            .message("new channel list: size = " + ucle.getNewIRCChannelList().size());
                    // ucle.getIRCNetwork().getIRCConnectionLog()
                    // .object("ucle.getNewIRCChannelList()", ucle.getNewIRCChannelList());
                    int totalUsers = 0;
                    for (IRCChannelList.Entry le : ucle.getNewIRCChannelList())
                    {
                        totalUsers += le.getNumberOfUsers();
                    }
                    ucle.getIRCNetwork()
                            .getIRCConnectionLog()
                            .message(
                                    "new channel list: delta users/channel = " + totalUsers
                                            / (float) ucle.getNewIRCChannelList().size());
                }
            });

            // only on inet for 3 successful times
            ircManager.getEventManager().register(new AutoReconnector(inet, 15 * 1000, 3));

            // on all nets forever trying
            // ircManager.getEventManager().register(new AutoReconnector(null, 15 * 1000, 0));
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

            // inet.send(new IRCNickCommand("nickles"));
            inet.send(new IRCListCommand());

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

        if (inet.isConnected()) inet.disconnect(false);
        inet.closeConnectionLog();
    }
}
