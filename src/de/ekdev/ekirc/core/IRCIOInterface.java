/**
 * IRCIOInterface.java
 */
package de.ekdev.ekirc.core;

import de.ekdev.ekirc.core.event.IRCEvent;

/**
 * @author ekDev
 */
public interface IRCIOInterface
{
    // To hide to much functionality

    public IRCConnection getIRCConnection();

    public IRCMessageProcessor getIRCMessageProcessor();

    public IRCConnectionLog getIRCConnectionLog();

    public void shutdown(boolean allowReconnect);

    public void raiseEvent(IRCEvent ircEvent);
}
