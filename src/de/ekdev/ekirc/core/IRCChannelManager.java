/**
 * IRCChannelManager.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCChannelManager
{
    private final IRCNetwork ircNetwork;
    
    public IRCChannelManager(IRCNetwork ircNetwork)
    {
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork is null!");
        }
        
        this.ircNetwork = ircNetwork;
    }
    
    // ------------------------------------------------------------------------
    
    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }
}
