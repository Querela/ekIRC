/**
 * IRCUser.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCUser implements Comparable<IRCUser>
{
    public final static int MAX_USER_NAME_LENGTH = 9;
    public final static char USER_EXCLAMATION_MARK = '!';
    public final static char USER_AT = '@';

    private final IRCUserManager ircUserManager;

    private String nickname;
    private String username;
    private String host;

    // ------------------------------------------------------------------------

    public IRCUser(IRCUserManager ircUserManager, String nickname) throws NullPointerException
    {
        Objects.requireNonNull(ircUserManager, "ircUserManager must not be null!");
        Objects.requireNonNull(nickname, "nickname must not be null!");
        // IRCUser.validateNickname(nickname); // throws exceptions

        this.ircUserManager = ircUserManager;
        this.nickname = nickname;

        // automatically add to manager
        this.ircUserManager.addIRCUser(this);
    }

    // ------------------------------------------------------------------------

    public final IRCUserManager getIRCUserManager()
    {
        return this.ircUserManager;
    }

    public final boolean isMe()
    {
        IRCUser me = this.ircUserManager.getIRCNetwork().getMyIRCIdentity().getIRCUser();
        if (me == null) return false;

        return me.equals(this);
    }

    // ------------------------------------------------------------------------

    public String getNickname()
    {
        return this.nickname;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getHost()
    {
        return this.host;
    }

    public String getPrefix()
    {
        return this.nickname
                + ((this.host != null) ? ((this.username != null) ? IRCUser.USER_EXCLAMATION_MARK + this.username : "")
                        + IRCUser.USER_AT + this.host : "");
    }

    public void setNickname(String newNickname)
    {
        this.ircUserManager.removeIRCUser(this);

        // TODO: validate?
        this.nickname = newNickname;

        this.ircUserManager.addIRCUser(this);
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setHostmask(String hostmask)
    {
        this.host = hostmask;
    }

    // ------------------------------------------------------------------------

    public static boolean validateNickname(String nickname) throws NullPointerException, IRCNicknameFormatException
    {
        Objects.requireNonNull(nickname, "Invalid nickname format: nickname must not be null!");
        if (nickname.length() == 0)
        {
            throw new IRCNicknameFormatException("nickname is empty!");
        }
        if (nickname.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IRCNicknameFormatException("nickname can't contain a space character!");
        }
        // TODO: validateNickname
        return true;
    }

    public static boolean validateUsername(String username) throws NullPointerException, IRCUsernameFormatException
    {
        Objects.requireNonNull(username, "Invalid username format: username must not be null!");
        if (username.length() == 0)
        {
            throw new IRCUsernameFormatException("username can't be empty!");
        }
        if (username.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IRCUsernameFormatException("username can't contain a space character!");
        }
        // TODO: validateUsername
        return true;
    }

    public static boolean validateHost(String hostmask) throws NullPointerException, IRCHostFormatException
    {
        Objects.requireNonNull(hostmask, "Invalid host format: host must not be null!");
        // TODO: validateHostmask
        return true;
    }

    // TODO: add nickname compare/equality especially for special chars

    public static String getNickByPrefix(String prefix)
    {
        if (prefix == null) return null;

        int excla = prefix.indexOf(IRCUser.USER_EXCLAMATION_MARK);
        if (excla == -1) return prefix;

        return prefix.substring(0, excla);
    }

    public static String getUsernameByPrefix(String prefix)
    {
        if (prefix == null) return null;

        int excla = prefix.indexOf(IRCUser.USER_EXCLAMATION_MARK);
        if (excla == -1) return null;

        int at = prefix.indexOf(IRCUser.USER_AT, excla);
        if (at == -1) return null;

        return prefix.substring(excla + 1, at);
    }

    public static String getHostByPrefix(String prefix)
    {
        if (prefix == null) return null;

        int at = prefix.indexOf(IRCUser.USER_AT);
        if (at == -1) return null;

        return prefix.substring(at + 1);
    }

    // ------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IRCUser other = (IRCUser) obj;
        if (nickname == null)
        {
            if (other.nickname != null) return false;
        }
        else if (!nickname.equals(other.nickname)) return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(IRCUser other)
    {
        // TODO: scandinavian compare ? []\~ == {}|^
        return this.nickname.compareToIgnoreCase(other.getNickname());
    }
}
