/**
 * IRCUserManager.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCUserManager
{
    private final IRCNetwork ircNetwork;
    
    public IRCUserManager(IRCNetwork ircNetwork)
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
