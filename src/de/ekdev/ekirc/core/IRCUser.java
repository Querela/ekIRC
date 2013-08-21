/**
 * IRCUser.java
 */
package de.ekdev.ekirc.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import de.ekdev.ekirc.core.commands.message.IRCNoticeCommand;
import de.ekdev.ekirc.core.commands.message.IRCPrivateMessageCommand;

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

    private String realname;
    private String server;
    private int hops;
    private boolean isAway;
    private boolean isIRCOperator;

    private final Set<IRCChannel> channels;

    // ------------------------------------------------------------------------

    public IRCUser(IRCUserManager ircUserManager, String nickname) throws NullPointerException
    {
        Objects.requireNonNull(ircUserManager, "ircUserManager must not be null!");
        Objects.requireNonNull(nickname, "nickname must not be null!");
        // IRCUser.validateNickname(nickname); // throws exceptions

        this.ircUserManager = ircUserManager;
        this.nickname = nickname;

        this.channels = Collections.synchronizedSet(new HashSet<IRCChannel>());

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

    protected void setNickname(String newNickname)
    {
        this.ircUserManager.removeIRCUser(this);

        // TODO: validate?
        this.nickname = newNickname;

        this.ircUserManager.addIRCUser(this);
    }

    protected void setUsername(String username)
    {
        this.username = username;
    }

    protected void setHost(String host)
    {
        this.host = host;
    }

    // --------------------------------

    public String getRealname()
    {
        return this.realname;
    }

    public String getServer()
    {
        return this.server;
    }

    public int getHopsAway()
    {
        return this.hops;
    }

    public boolean isAway()
    {
        return this.isAway;
    }

    public boolean isIRCOp()
    {
        return this.isIRCOperator;
    }

    protected void setRealname(String realname)
    {
        this.realname = realname;
    }

    protected void setServer(String server)
    {
        this.server = server;
    }

    protected void setHops(int hops)
    {
        this.hops = hops;
    }

    protected void setAway(boolean isAway)
    {
        this.isAway = isAway;
    }

    protected void setIRCOp(boolean isIRCOperator)
    {
        this.isIRCOperator = isIRCOperator;
    }

    // --------------------------------
    // Update

    public Set<IRCChannel> getIRCChannels()
    {
        return Collections.unmodifiableSet(this.channels);
    }

    protected void addIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.channels.add(ircChannel);
    }

    protected void removeIRCChannel(IRCChannel ircChannel)
    {
        if (ircChannel == null) return;

        this.channels.remove(ircChannel);
    }

    // ------------------------------------------------------------------------
    // Actions

    public void sendPrivateMessage(String message) throws NullPointerException
    {
        AsIRCMessage ircMessage = new IRCPrivateMessageCommand(this, message);

        this.ircUserManager.getIRCNetwork().send(ircMessage);
    }

    public boolean sendPrivateMessageSilent(String message)
    {
        boolean ret = true;

        try
        {
            this.sendPrivateMessage(message);
        }
        catch (Exception e)
        {
            this.ircUserManager.getIRCNetwork().getIRCConnectionLog().exception(e); // TODO: log?
            ret = false;
        }

        return ret;
    }

    public void sendNotice(String message) throws NullPointerException
    {
        AsIRCMessage ircMessage = new IRCNoticeCommand(this, message);

        this.ircUserManager.getIRCNetwork().send(ircMessage);
    }

    public boolean sendNoticeSilent(String message)
    {
        boolean ret = true;

        try
        {
            this.sendNotice(message);
        }
        catch (Exception e)
        {
            this.ircUserManager.getIRCNetwork().getIRCConnectionLog().exception(e); // TODO: log?
            ret = false;
        }

        return ret;
    }

    public void updateInfos()
    {

    }

    // ------------------------------------------------------------------------

    public static boolean validateNickname(String nickname) throws NullPointerException, IRCNicknameFormatException
    {
        Objects.requireNonNull(nickname, "Invalid nickname format: nickname must not be null!");
        if (nickname.length() == 0)
        {
            throw new IRCNicknameFormatException("nickname must not be empty!");
        }
        if (nickname.indexOf(0x20) != -1)
        {
            throw new IRCNicknameFormatException("nickname must not contain a 0x20 (space) character!");
        }

        char[] chars = nickname.toCharArray();

        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            if ((c >= 0x41 && c <= 0x5A) // letter: A-Z
                    || (c >= 0x61 && c <= 0x7A) // letter: a-z
                    || (c >= 0x5B && c <= 0x60) // special: [\\]^_`
                    || (c >= 0x7B && c <= 0x7D) // special: {|}
            )
            {
                continue;
            }
            else if ((i > 0) && // not the first
                    ((c >= 0x30 && c <= 0x39) // digit: 0-9
                    || (c == 0x2d)) // hypen/minus: -
            )
            {
                continue;
            }
            else
            {
                throw new IRCNicknameFormatException("nickname contains an illegal character: "
                        + ((c >= 0x20) ? "\"" + c + "\" " : "") + "[0x" + String.format("%02X", c) + "] at index " + i);
            }
        }

        return true;
    }

    public static boolean validateUsername(String username) throws NullPointerException, IRCUsernameFormatException
    {
        Objects.requireNonNull(username, "Invalid username format: username must not be null!");
        if (username.length() == 0)
        {
            throw new IRCUsernameFormatException("username must not be empty!");
        }

        if (username.indexOf(0x00) != -1)
        {
            throw new IRCUsernameFormatException("username must not contain a 0x00 (null) character!");
        }
        if (username.indexOf(0x0A) != -1)
        {
            throw new IRCUsernameFormatException("username must not contain a 0x0A (carriage return) character!");
        }
        if (username.indexOf(0x0D) != -1)
        {
            throw new IRCUsernameFormatException("username must not contain a 0x0D (line feed) character!");
        }
        if (username.indexOf(0x20) != -1)
        {
            throw new IRCUsernameFormatException("username must not contain a 0x20 (space) character!");
        }
        if (username.indexOf(0x40) != -1)
        {
            throw new IRCUsernameFormatException("username must not contain a 0x40 (at / @) character!");
        }

        return true;
    }

    public static boolean validateHost(String hostmask) throws NullPointerException, IRCHostFormatException
    {
        Objects.requireNonNull(hostmask, "Invalid host format: host must not be null!");

        // TODO: validateHostmask

        // host: "(\\w[\\w-]*\\w*)(\\.\\w[\\w-]*\\w*)*"
        // ipv4: "\\b([12][0-9]{2}|[1-9]?[0-9])(\\.([12][0-9]{2}|[1-9]?[0-9])){3}\\b"
        // ipv6: ?
        // "([0-9A-Fa-f]{0,4}(:[0-9A-Fa-f]{0,4}){1,7})\\b([12][0-9]{2}|[1-9]?[0-9])(\\.([12][0-9]{2}|[1-9]?[0-9])){3}"

        return true;
    }

    // TODO: add nickname compare/equality especially for special chars

    public static String nicknameToLowerCase(String nickname)
    {
        return nickname.toLowerCase(Locale.ENGLISH).replace('[', '{').replace(']', '}').replace('\\', '|')
                .replace('~', '^');
    }

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

    // TODO: add other field too?

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
        // scandinavian compare? []\~ == {}|^
        return IRCUser.nicknameToLowerCase(this.nickname).compareTo(IRCUser.nicknameToLowerCase(other.getNickname()));
    }
}
