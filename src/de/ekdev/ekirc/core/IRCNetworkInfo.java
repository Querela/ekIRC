/**
 * IRCNetworkInfo.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCNetworkInfo
{
    // reply 004 RPL_MYINFO
    private String serverName;
    private String serverVersion;
    private String userModes;
    private String channelModes;

    // reply 005 RPL_???
    private HashMap<String, String> supported;

    public final static String KEY_AWAYLEN = "AWAYLEN";
    public final static String KEY_CALLERID = "CALLERID";
    public final static String KEY_CASEMAPPING = "CASEMAPPING";
    public final static String KEY_CHANLIMIT = "CHANLIMIT";
    public final static String KEY_CHANMODES = "CHANMODES";
    public final static String KEY_CHANNELLEN = "CHANNELLEN";
    public final static String KEY_CHANTYPES = "CHANTYPES";
    public final static String KEY_CHARSET = "CHARSET";
    public final static String KEY_CLIENTVER = "CLIENTVER";
    public final static String KEY_CNOTICE = "CNOTICE";
    public final static String KEY_CPRIVMSG = "CPRIVMSG";
    public final static String KEY_DEAF = "DEAF";
    public final static String KEY_ELIST = "ELIST";
    public final static String KEY_ETRACE = "ETRACE";
    public final static String KEY_EXCEPTS = "EXCEPTS";
    public final static String KEY_EXTBAN = "EXTBAN";
    public final static String KEY_FNC = "FNC";
    public final static String KEY_INVEX = "INVEX";
    public final static String KEY_KICKLEN = "KICKLEN";
    public final static String KEY_KNOCK = "KNOCK";
    public final static String KEY_MAXCHANNELS = "MAXCHANNELS";
    public final static String KEY_MAXLIST = "MAXLIST";
    public final static String KEY_MODES = "MODES";
    public final static String KEY_MONITOR = "MONITOR";
    public final static String KEY_NAMESX = "NAMESX";
    public final static String KEY_NETWORK = "NETWORK";
    public final static String KEY_NICKLEN = "NICKLEN";
    public final static String KEY_PREFIX = "PREFIX";
    public final static String KEY_RFC2812 = "RFC2812";
    public final static String KEY_SAFELIST = "SAFELIST";
    public final static String KEY_STATUSMSG = "STATUSMSG";
    public final static String KEY_STD = "STD";
    public final static String KEY_TARGMAX = "TARGMAX";
    public final static String KEY_TOPICLEN = "TOPICLEN";
    public final static String KEY_UHNAMES = "UHNAMES";
    public final static String KEY_USERIP = "USERIP";
    public final static String KEY_WALLCHOPS = "WALLCHOPS";
    public final static String KEY_WALLVOICES = "WALLVOICES";
    public final static String KEY_WATCH = "WATCH";
    public final static String KEY_WHOX = "WHOX";

    //
    private List<String> motd;
    private List<String> tempMotd;

    // --------------------------------------------------------------------
    // --------------------------------------------------------------------

    public IRCNetworkInfo()
    {
        this.supported = new HashMap<String, String>();

        this.motd = new ArrayList<>();
        this.tempMotd = new ArrayList<>();
    }

    public IRCNetworkInfo(IRCNetworkInfo ircNetworkInfo) throws NullPointerException
    {
        this();
        Objects.requireNonNull(ircNetworkInfo, "ircNetworkInfo must not be null!");

        this.serverName = ircNetworkInfo.serverName;
        this.serverVersion = ircNetworkInfo.serverVersion;
        this.userModes = ircNetworkInfo.userModes;
        this.channelModes = ircNetworkInfo.channelModes;

        // copy all entries
        this.supported.putAll(ircNetworkInfo.supported);

        this.motd = new ArrayList<>(ircNetworkInfo.motd);
        this.tempMotd = new ArrayList<>(ircNetworkInfo.tempMotd);
    }

    // --------------------------------------------------------------------

    protected IRCNetworkInfo update(IRCMessage ircMessage)
    {
        if (ircMessage == null) return this;
        if (!ircMessage.isNumericReply()) return this;

        if (ircMessage.getNumericReply() == IRCNumericServerReply.RPL_MYINFO)
        {
            this.serverName = ircMessage.getParams().get(1);
            this.serverVersion = ircMessage.getParams().get(2);
            this.userModes = ircMessage.getParams().get(3);
            this.channelModes = ircMessage.getParams().get(4);
        }
        else if (ircMessage.getNumericReply() == IRCNumericServerReply.RPL_ISUPPORT)
        {
            for (int i = 1; i < ircMessage.getParams().size() - 1; i++)
            {
                String token = ircMessage.getParams().get(i);
                int index = token.indexOf('=');
                String key = (index == -1) ? token : token.substring(0, index);
                key = key.toUpperCase();
                String value = (i == -1) ? null : token.substring(index + 1);

                // add the modes
                this.supported.put(key, value);
                // System.err.println("NETWORK MODE = " + token);

                // if (key.equals("CHANMODES"))
                // {
                // System.err.println("chanmodes1 = " + this.channelModes);
                // System.err.println("chanmodes2 = " + value);
                // }
            }
        }

        return this;
    }

    // --------------------------------------------------------------------

    // --------------------------------------------------------------------

    protected IRCNetworkInfo setServerName(String serverName)
    {
        this.serverName = serverName;
        return this;
    }

    protected IRCNetworkInfo setServerVersion(String serverVersion)
    {
        this.serverVersion = serverVersion;
        return this;
    }

    protected IRCNetworkInfo setUserModes(String userModes)
    {
        this.userModes = userModes;
        return this;
    }

    protected IRCNetworkInfo setChannelModes(String channelModes)
    {
        this.channelModes = channelModes;
        return this;
    }

    // --------------------------------------------------------------------

    protected IRCNetworkInfo newMotd()
    {
        this.tempMotd = new ArrayList<>();
        return this;
    }

    protected IRCNetworkInfo addModtLine(String line)
    {
        if (line == null) return this;

        this.tempMotd.add(line);
        return this;
    }

    protected IRCNetworkInfo finishNewMotd()
    {
        this.motd = new ArrayList<>(this.tempMotd);
        this.tempMotd.clear();
        return this;
    }

    // --------------------------------------------------------------------
    // --------------------------------------------------------------------

    public String getSupported(String key)
    {
        return this.supported.get(key);
    }

    public int getSupportedInt(String key, int defaultValue)
    {
        try
        {
            return Integer.valueOf(this.supported.get(key));
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public int getSupportedInt(String key)
    {
        return this.getSupportedInt(key, -1);
    }

    public boolean isSupported(String key)
    {
        return this.supported.containsKey(key);
    }

    // --------------------------------

    public String getServerName()
    {
        return serverName;
    }

    public String getServerVersion()
    {
        return serverVersion;
    }

    public String getUserModes()
    {
        return userModes;
    }

    public String getChannelModes()
    {
        return channelModes;
    }

    // --------------------------------

    public String getPrefixes()
    {
        return this.getSupported(KEY_PREFIX);
    }

    public String getChannelTypes()
    {
        return this.getSupported(KEY_CHANTYPES);
    }

    public String getChannelModes2()
    {
        return this.getSupported(KEY_CHANMODES);
    }

    public int getMaxModes()
    {
        return this.getSupportedInt(KEY_MODES, -1);
    }

    public int getMaxChannels()
    {
        return this.getSupportedInt(KEY_MAXCHANNELS, -1);
    }

    public String getChanLimit()
    {
        return this.getSupported(KEY_CHANLIMIT);
    }

    public int getMaxNickLength()
    {
        return this.getSupportedInt(KEY_NICKLEN, -1);
    }

    public int getMaxBans()
    {
        return this.getSupportedInt(KEY_MAXLIST, -1);
    }

    public String getMaxList()
    {
        return this.getSupported(KEY_MAXLIST);
    }

    public String getNetwork()
    {
        return this.getSupported(KEY_NETWORK);
    }

    public String getExceptBans()
    {
        return this.getSupported(KEY_EXCEPTS);
    }

    public String getExceptInvites()
    {
        return this.getSupported(KEY_INVEX);
    }

    public boolean isWallOps()
    {
        return this.isSupported(KEY_WALLCHOPS);
    }

    public boolean isWallVoices()
    {
        return this.isSupported(KEY_WALLVOICES);
    }

    public String getStatusMessage()
    {
        return this.getSupported(KEY_STATUSMSG);
    }

    public String getCaseMapping()
    {
        return this.getSupported(KEY_CASEMAPPING);
    }

    public String getCharset()
    {
        return this.getSupported(KEY_CHARSET);
    }

    public String geteList()
    {
        return this.getSupported(KEY_ELIST);
    }

    public int getTopicLength()
    {
        return this.getSupportedInt(KEY_TOPICLEN, -1);
    }

    public int getKickLength()
    {
        return this.getSupportedInt(KEY_KICKLEN, -1);
    }

    public int getChannelLength()
    {
        return this.getSupportedInt(KEY_CHANNELLEN, -1);
    }

    public String getStandard()
    {
        return this.getSupported(KEY_STD);
    }

    public boolean isRFC2812()
    {
        return this.isSupported(KEY_RFC2812);
    }

    public boolean iscPrivMsgExists()
    {
        return this.isSupported(KEY_CPRIVMSG);
    }

    public boolean iscNoticeExists()
    {
        return this.isSupported(KEY_CNOTICE);
    }

    public boolean isSafeList()
    {
        return this.isSupported(KEY_SAFELIST);
    }

    public boolean isKnockExists()
    {
        return this.isSupported(KEY_KNOCK);
    }

    public boolean isWhoX()
    {
        return this.isSupported(KEY_WHOX);
    }

    public boolean isUserIPExists()
    {
        return this.isSupported(KEY_USERIP);
    }

    public int getMaxWatch()
    {
        return this.getSupportedInt(KEY_WATCH, -1);
    }

    // --------------------------------

    public List<String> getMotd()
    {
        return Collections.unmodifiableList(motd);
    }

    public String getMotdString()
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.motd.size(); i++)
        {
            if (i != 0) sb.append(System.lineSeparator());
            sb.append(this.motd.get(i));
        }

        return sb.toString();
    }
}
