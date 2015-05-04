/**
 * IRCServerCommand.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public enum IRCServerCommand
{
    PING,
    //
    ERROR,
    //
    PRIVMSG,
    // User normal, USER CTCP, Channel
    JOIN,
    //
    PART,
    //
    NICK,
    //
    NOTICE,
    // CTCP responses
    QUIT,
    //
    KICK,
    //
    MODE,
    //
    TOPIC,
    //
    INVITE; //
}
