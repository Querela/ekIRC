/**
 * AsIRCMessage.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public interface AsIRCMessage
{
    public IRCMessage asIRCMessage();

    public String asIRCMessageString();
}
