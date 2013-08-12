/**
 * ListAutoNickRenamer.java
 */
package de.ekdev.ekirc.core.event.listener;

import java.util.ArrayList;
import java.util.List;

import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.commands.connection.IRCNickCommand;
import de.ekdev.ekirc.core.event.NickAlreadyInUseEvent;

/**
 * @author ekDev
 */
public class ListAutoNickRenamer implements EventListener
{
    private final IRCNetwork ircNetwork;
    private final List<String> nicknames;
    private int index = 0;

    public ListAutoNickRenamer(IRCNetwork ircNetwork, List<String> nicknames)
    {
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork can't be null!");
        }
        if (nicknames == null || nicknames.size() == 0)
        {
            throw new IllegalArgumentException("Argument nicknames can't be null or an empty list!");
        }

        this.ircNetwork = ircNetwork;
        this.nicknames = new ArrayList<String>(nicknames);
    }

    @EventHandler
    public void onNickAlreadyInUseEvent(NickAlreadyInUseEvent naiue)
    {
        if (!this.ircNetwork.equals(naiue.getIRCNetwork())) return;

        if (this.index >= this.nicknames.size())
        {
            // TODO: rewind to first element

            // TODO: remove listener
            this.ircNetwork.getIRCConnectionLog().message(
                    "Unregister " + this.getClass().getSimpleName() + " after trying " + this.index + " nicknames ...");
            this.ircNetwork.getIRCManager().getEventManager().unregister(this);

            return;
        }

        try
        {
            this.ircNetwork.send(new IRCNickCommand(this.nicknames.get(this.index++)));
        }
        catch (Exception e)
        {
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }
    }
}
