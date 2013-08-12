/**
 * UserConnectionRegistrator.java
 */
package de.ekdev.ekirc.core.event.listener;

import de.ekdev.ekevent.EventException;
import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.commands.connection.IRCUserCommand;
import de.ekdev.ekirc.core.event.IRCConnectEvent;

/**
 * @author ekDev
 */
public class UserConnectionRegistrator implements EventListener
{
    private final IRCNetwork ircNetwork;
    private final String nickname;
    private final String username;
    private final String realname;
    private final boolean invisible;

    public UserConnectionRegistrator(IRCNetwork ircNetwork, String nickname, String username, boolean invisible)
    {
        // TODO: check
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork can't be null!");
        }

        this.ircNetwork = ircNetwork;
        this.nickname = nickname;
        this.username = username;
        this.realname = this.ircNetwork.getMyIRCIdentity().getRealname();
        this.invisible = invisible;
    }

    @EventHandler
    public void onIRCConnectEvent(IRCConnectEvent ice)
    {
        if (!this.ircNetwork.equals(ice.getIRCNetwork())) return;

        try
        {
            this.ircNetwork.getIRCManager().getEventManager()
                    .register(new IncrementalAutoNickRenamer(ice.getIRCNetwork(), this.nickname, 5));
        }
        catch (EventException e)
        {
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }

        try
        {
            this.ircNetwork.send(new IRCNickCommand(this.nickname), new IRCUserCommand(this.username, this.invisible,
                    this.realname));
        }
        catch (Exception e)
        {
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }

        // don't remove if reconnect ... !?!
        // IRCConnectEvent.getListenerList().unregister(this);
        // this.ircNetwork.getIRCManager().getEventManager().unregister(this);
    }
}
