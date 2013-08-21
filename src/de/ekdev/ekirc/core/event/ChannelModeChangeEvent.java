/**
 * ChannelModeChangeEvent.java
 */
package de.ekdev.ekirc.core.event;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCChannel;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCUser;

/**
 * @author ekDev
 */
public class ChannelModeChangeEvent extends IRCNetworkEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    private final IRCUser sourceIRCUser;
    private final IRCChannel targetIRCChannel;
    private final String oldMode;
    private final String modeChange;
    private final String newMode;

    public ChannelModeChangeEvent(IRCNetwork ircNetwork, IRCUser sourceIRCUser, IRCChannel targetIRCChannel,
            String oldMode, String modeChange)
    {
        super(ircNetwork);

        this.sourceIRCUser = sourceIRCUser;
        this.targetIRCChannel = targetIRCChannel;
        this.oldMode = oldMode;
        this.modeChange = modeChange;
        this.newMode = targetIRCChannel.getMode();
    }

    // ------------------------------------------------------------------------

    public IRCUser getSourceIRCUser()
    {
        return this.sourceIRCUser;
    }

    public IRCChannel getTargetIRCChannel()
    {
        return this.targetIRCChannel;
    }

    public String getOldMode()
    {
        return this.oldMode;
    }

    public String getModeChange()
    {
        return this.modeChange;
    }

    public String getNewMode()
    {
        return this.newMode;
    }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return ChannelModeChangeEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return ChannelModeChangeEvent.listeners;
    }
}
