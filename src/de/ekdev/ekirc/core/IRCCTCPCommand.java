/**
 * IRCCTCPCommand.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public enum IRCCTCPCommand
{
    // -> http://www.irchelp.org/irchelp/rfc/ctcpspec.html

    /**
     * Used to simulate "role playing" on IRC.
     */
    ACTION,
    /**
     * Negotiates file transfers and direct TCP chat connections between clients.
     */
    DCC,
    /**
     * Used to send encrypted messages between clients.
     */
    SED,
    /**
     * Returns the user's full name, and idle time.
     */
    FINGER,
    /**
     * The version and type of the client.
     */
    VERSION,
    /**
     * Where to obtain a copy of a client.
     */
    SOURCE,
    /**
     * A string set by the user (never the client coder).
     */
    USERINFO,
    /**
     * Dynamic master index of what a client knows.
     */
    CLIENTINFO,
    /**
     * Used when an error needs to be replied with.
     */
    ERRMSG,
    /**
     * Used to measure the delay of the IRC network between clients.
     */
    PING,
    /**
     * Gets the local date and time from other clients.
     */
    TIME;
}
