/**
 * IncrementalAutoNickRenamer.java
 */
package de.ekdev.ekirc.core.event.listener;

import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.event.IRCNickAlreadyInUseEvent;

/**
 * @author ekDev
 */
public class IncrementalAutoNickRenamer implements EventListener
{
    private final IRCNetwork ircNetwork;
    private final String nickname;
    private final int maxTries;
    private int counter = 0;

    public IncrementalAutoNickRenamer(IRCNetwork ircNetwork, String nickname, int tries)
    {
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork can't be null!");
        }
        if (nickname == null || nickname.length() == 0)
        {
            throw new IllegalArgumentException("Argument nickname can't be null or empty!");
        }

        this.ircNetwork = ircNetwork;
        this.nickname = nickname;
        this.maxTries = tries;
    }

    @EventHandler
    public void onIRCNickAlreadyInUseEvent(IRCNickAlreadyInUseEvent inaiue)
    {
        if (!this.ircNetwork.equals(inaiue.getIRCNetwork())) return;

        this.ircNetwork.getIRCConnectionLog().object("inaiue", inaiue);
        this.ircNetwork.getIRCConnectionLog().object("inaiue.getIRCMessage()", inaiue.getIRCMessage());
        // this.ircNetwork.getIRCConnectionLog().object("inaiue.getIRCMessage().asIRCMessage()",
        // inaiue.getIRCMessage().asIRCMessage());
        this.ircNetwork.getIRCConnectionLog().message(inaiue.getIRCMessage().asIRCMessageString());

        this.counter++;

        if (this.counter > this.maxTries)
        {
            // DOES NOT WORK
            // -> java.util.ConcurrentModificationException
            // --> at de.ekdev.ekevent.EventManager.fireEvent(EventManager.java:57)
            // this.ircNetwork.getIRCConnectionLog().message(
            // "Unregister " + this.getClass().getSimpleName() + " after " + (counter - 1) + " tries ...");
            // this.ircNetwork.getIRCManager().getEventManager().unregister(this);
            return;
        }

        String newNick = this.nickname + counter;
        try
        {
            this.ircNetwork.send(new IRCNickCommand(newNick));
        }
        catch (Exception e)
        {
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }
    }
}
