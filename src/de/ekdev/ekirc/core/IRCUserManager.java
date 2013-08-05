/**
 * IRCUserManager.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCUserManager
{
    private final IRCServerContext serverContext;
    
    public IRCUserManager(IRCServerContext serverContext)
    {
        if (serverContext == null)
        {
            throw new IllegalArgumentException("Argument serverContext is null!");
        }
        
        this.serverContext = serverContext;
    }
    
    // ------------------------------------------------------------------------
    
    public final IRCServerContext getIRCServerContext()
    {
        return this.serverContext;
    }
}
