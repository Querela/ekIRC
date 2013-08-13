/**
 * ListAutoNickRenamer.java
 */
package de.ekdev.ekirc.core.event.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");
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

        // if invalid nickname skip to next one ...
        for (; this.index < this.nicknames.size();)
        {
            try
            {
                this.ircNetwork.send(new IRCNickCommand(this.nicknames.get(this.index++)));
                break; // leave if no error
            }
            catch (Exception e)
            {
                this.ircNetwork.getIRCConnectionLog().exception(e);
            }
        }
    }
}
