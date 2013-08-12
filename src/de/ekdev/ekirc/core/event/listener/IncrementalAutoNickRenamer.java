/**
 * IncrementalAutoNickRenamer.java
 */
package de.ekdev.ekirc.core.event.listener;

import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.event.NickAlreadyInUseEvent;

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
    public void onNickAlreadyInUseEvent(NickAlreadyInUseEvent naiue)
    {
        if (!this.ircNetwork.equals(naiue.getIRCNetwork())) return;

        this.ircNetwork.getIRCConnectionLog().object("inaiue", naiue);
        this.ircNetwork.getIRCConnectionLog().object("inaiue.getIRCMessage()", naiue.getIRCMessage());
        // this.ircNetwork.getIRCConnectionLog().object("inaiue.getIRCMessage().asIRCMessage()",
        // inaiue.getIRCMessage().asIRCMessage());
        this.ircNetwork.getIRCConnectionLog().message(naiue.getIRCMessage().asIRCMessageString());

        this.counter++;

        if (this.counter > this.maxTries)
        {
            // DOES WORK
            // -> java.util.ConcurrentModificationException
            // --> at de.ekdev.ekevent.EventManager.fireEvent(EventManager.java:57)
            // because of CopyOnWriteArrayList!
            this.ircNetwork.getIRCConnectionLog().message(
                    "Unregister " + this.getClass().getSimpleName() + " after " + (counter - 1) + " tries ...");
            // this.ircNetwork.getIRCManager().getEventManager().unregister(this);
            NickAlreadyInUseEvent.getListenerList().unregister(this);
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
