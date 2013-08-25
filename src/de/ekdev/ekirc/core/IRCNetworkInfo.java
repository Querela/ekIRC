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

    private String prefixes;
    private String channelTypes;
    private String channelModes2;
    private int maxModes;
    private int maxChannels;
    private String chanLimit;
    private int nickLength;
    private int maxBans;
    private String maxList;
    private String network;
    private String exceptBans;
    private String exceptInvites;
    private boolean wallOps;
    private boolean wallVoices;
    private String statusMessage;
    private String caseMapping;
    private String charset;
    private String eList;
    private int topicLength;
    private int kickLength;
    private int channelLength;
    private String channelIDLength;
    private String standard;
    private int silence;
    private boolean rfc2812;
    private boolean penalty;
    private boolean cPrivMsgExists;
    private boolean cNoticeExists;
    private boolean safeList;
    private boolean knockExists;
    private boolean whoX;
    private boolean callerID;
    private boolean userIPExists;
    private int maxWatch;

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

        this.prefixes = ircNetworkInfo.prefixes;
        this.channelTypes = ircNetworkInfo.channelTypes;
        this.channelModes2 = ircNetworkInfo.channelModes2;
        this.maxModes = ircNetworkInfo.maxModes;
        this.maxChannels = ircNetworkInfo.maxChannels;
        this.chanLimit = ircNetworkInfo.chanLimit;
        this.nickLength = ircNetworkInfo.nickLength;
        this.maxBans = ircNetworkInfo.maxBans;
        this.maxList = ircNetworkInfo.maxList;
        this.network = ircNetworkInfo.network;
        this.exceptBans = ircNetworkInfo.exceptBans;
        this.exceptInvites = ircNetworkInfo.exceptInvites;
        this.wallOps = ircNetworkInfo.wallOps;
        this.wallVoices = ircNetworkInfo.wallVoices;
        this.statusMessage = ircNetworkInfo.statusMessage;
        this.caseMapping = ircNetworkInfo.caseMapping;
        this.charset = ircNetworkInfo.charset;
        this.eList = ircNetworkInfo.eList;
        this.topicLength = ircNetworkInfo.topicLength;
        this.kickLength = ircNetworkInfo.kickLength;
        this.channelLength = ircNetworkInfo.channelLength;
        this.channelIDLength = ircNetworkInfo.channelIDLength;
        this.standard = ircNetworkInfo.standard;
        this.silence = ircNetworkInfo.silence;
        this.rfc2812 = ircNetworkInfo.rfc2812;
        this.penalty = ircNetworkInfo.penalty;
        this.cPrivMsgExists = ircNetworkInfo.cPrivMsgExists;
        this.cNoticeExists = ircNetworkInfo.cNoticeExists;
        this.safeList = ircNetworkInfo.safeList;
        this.knockExists = ircNetworkInfo.knockExists;
        this.whoX = ircNetworkInfo.whoX;
        this.callerID = ircNetworkInfo.callerID;
        this.userIPExists = ircNetworkInfo.userIPExists;
        this.maxWatch = ircNetworkInfo.maxWatch;

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
                String key = (i == -1) ? token : token.substring(0, index);
                key = key.toUpperCase();
                String value = (i == -1) ? null : token.substring(index + 1);

                this.supported.put(key, value);

                System.err.println("NETWORK MODE = " + token);

                if (key.equals("CASEMAPPING"))
                {
                    this.caseMapping = value;
                }
                else if (key.equals("CHANTYPES"))
                {
                    this.channelTypes = value;
                }
                else if (key.equals("CHANNELLEN"))
                {
                    try
                    {
                        this.channelLength = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("CHANMODES"))
                {
                    this.channelModes2 = value;
                    // TODO: debug ...
                    System.err.println("chanmodes1 = " + this.channelModes);
                    System.err.println("chanmodes2 = " + this.channelModes2);
                }
                else if (key.equals("CHANLIMIT"))
                {
                    this.chanLimit = value;
                }
                else if (key.equals("ELIST"))
                {
                    this.eList = value;
                }
                else if (key.equals("EXCEPTS"))
                {
                    this.exceptBans = value;
                }
                // else if (key.equals("FNC"))
                // {
                // // TODO:
                // }
                else if (key.equals("INVEX"))
                {
                    this.exceptInvites = value;
                }
                else if (key.equals("KICKLEN"))
                {
                    try
                    {
                        this.kickLength = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("KNOCK"))
                {
                    this.knockExists = true;
                }
                else if (key.equals("MAXCHANNELS"))
                {
                    try
                    {
                        this.maxChannels = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("MAXLIST"))
                {
                    this.maxList = value;
                }
                else if (key.equals("MODES"))
                {
                    try
                    {
                        this.maxModes = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("NETWORK"))
                {
                    this.network = value;
                }
                else if (key.equals("NICKLEN"))
                {
                    try
                    {
                        this.nickLength = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("PREFIX"))
                {
                    this.prefixes = value;
                }
                else if (key.equals("RFC2812"))
                {
                    this.rfc2812 = true;
                }
                else if (key.equals("SAFELIST"))
                {
                    this.safeList = true;
                }
                // else if (key.equals("SILENCE"))
                // {
                // // TODO:
                // }
                else if (key.equals("STATUSMSG"))
                {
                    this.statusMessage = value;
                }
                else if (key.equals("STD"))
                {
                    this.standard = value;
                }
                else if (key.equals("TOPICLEN"))
                {
                    try
                    {
                        this.topicLength = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else if (key.equals("WALLCHOPS"))
                {
                    this.wallOps = true;
                }
                else if (key.equals("WALLVOICES"))
                {
                    this.wallVoices = true;
                }
                else if (key.equals("WATCH"))
                {
                    try
                    {
                        this.topicLength = Integer.valueOf(value);
                    }
                    catch (NumberFormatException e)
                    {
                    }
                }
                else
                {
                    // unknown - ignore
                }
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

    protected IRCNetworkInfo setPrefixes(String prefixes)
    {
        this.prefixes = prefixes;
        return this;
    }

    protected IRCNetworkInfo setChannelTypes(String channelTypes)
    {
        this.channelTypes = channelTypes;
        return this;
    }

    protected IRCNetworkInfo setChannelModes2(String channelModes2)
    {
        // TODO: ?
        this.channelModes2 = channelModes2;
        return this;
    }

    protected IRCNetworkInfo setMaxModes(int maxModes)
    {
        this.maxModes = maxModes;
        return this;
    }

    protected IRCNetworkInfo setMaxChannels(int maxChannels)
    {
        this.maxChannels = maxChannels;
        return this;
    }

    protected IRCNetworkInfo setChanLimit(String chanLimit)
    {
        this.chanLimit = chanLimit;
        return this;
    }

    protected IRCNetworkInfo setMaxNickLength(int maxNickLength)
    {
        this.nickLength = maxNickLength;
        return this;
    }

    protected IRCNetworkInfo setMaxBans(int maxBans)
    {
        this.maxBans = maxBans;
        return this;
    }

    protected IRCNetworkInfo setMaxList(String maxList)
    {
        this.maxList = maxList;
        return this;
    }

    protected IRCNetworkInfo setNetwork(String network)
    {
        this.network = network;
        return this;
    }

    protected IRCNetworkInfo setExceptBans(String exceptBans)
    {
        this.exceptBans = exceptBans;
        return this;
    }

    protected IRCNetworkInfo setExceptInvites(String exceptInvites)
    {
        this.exceptInvites = exceptInvites;
        return this;
    }

    protected IRCNetworkInfo setWallOps(boolean wallOps)
    {
        this.wallOps = wallOps;
        return this;
    }

    protected IRCNetworkInfo setWallVoices(boolean wallVoices)
    {
        this.wallVoices = wallVoices;
        return this;
    }

    protected IRCNetworkInfo setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
        return this;
    }

    protected IRCNetworkInfo setCaseMapping(String caseMapping)
    {
        this.caseMapping = caseMapping;
        return this;
    }

    protected IRCNetworkInfo setCharset(String charset)
    {
        this.charset = charset;
        return this;
    }

    protected IRCNetworkInfo seteList(String eList)
    {
        this.eList = eList;
        return this;
    }

    protected IRCNetworkInfo setTopicLength(int topicLength)
    {
        this.topicLength = topicLength;
        return this;
    }

    protected IRCNetworkInfo setKickLength(int kickLength)
    {
        this.kickLength = kickLength;
        return this;
    }

    protected IRCNetworkInfo setChannelLength(int channelLength)
    {
        this.channelLength = channelLength;
        return this;
    }

    protected IRCNetworkInfo setChannelIDLength(String channelIDLength)
    {
        this.channelIDLength = channelIDLength;
        return this;
    }

    protected IRCNetworkInfo setStandard(String standard)
    {
        this.standard = standard;
        return this;
    }

    protected IRCNetworkInfo setSilence(int silence)
    {
        this.silence = silence;
        return this;
    }

    protected IRCNetworkInfo setRfc2812(boolean rfc2812)
    {
        this.rfc2812 = rfc2812;
        return this;
    }

    protected IRCNetworkInfo setPenalty(boolean penalty)
    {
        this.penalty = penalty;
        return this;
    }

    protected IRCNetworkInfo setcPrivMsgExists(boolean cPrivMsgExists)
    {
        this.cPrivMsgExists = cPrivMsgExists;
        return this;
    }

    protected IRCNetworkInfo setcNoticeExists(boolean cNoticeExists)
    {
        this.cNoticeExists = cNoticeExists;
        return this;
    }

    protected IRCNetworkInfo setSafeList(boolean safeList)
    {
        this.safeList = safeList;
        return this;
    }

    protected IRCNetworkInfo setKnockExists(boolean knockExists)
    {
        this.knockExists = knockExists;
        return this;
    }

    protected IRCNetworkInfo setWhoX(boolean whoX)
    {
        this.whoX = whoX;
        return this;
    }

    protected IRCNetworkInfo setCallerID(boolean callerID)
    {
        this.callerID = callerID;
        return this;
    }

    protected IRCNetworkInfo setUserIPExists(boolean userIPExists)
    {
        this.userIPExists = userIPExists;
        return this;
    }

    protected IRCNetworkInfo setMaxWatch(int maxWatch)
    {
        this.maxWatch = maxWatch;
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

    public String getPrefixes()
    {
        return prefixes;
    }

    public String getChannelTypes()
    {
        return channelTypes;
    }

    public String getChannelModes2()
    {
        return channelModes2;
    }

    public int getMaxModes()
    {
        return maxModes;
    }

    public int getMaxChannels()
    {
        return maxChannels;
    }

    public String getChanLimit()
    {
        return chanLimit;
    }

    public int getMaxNickLength()
    {
        return nickLength;
    }

    public int getMaxBans()
    {
        return maxBans;
    }

    public String getMaxList()
    {
        return maxList;
    }

    public String getNetwork()
    {
        return network;
    }

    public String getExceptBans()
    {
        return exceptBans;
    }

    public String getExceptInvites()
    {
        return exceptInvites;
    }

    public boolean isWallOps()
    {
        return wallOps;
    }

    public boolean isWallVoices()
    {
        return wallVoices;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public String getCaseMapping()
    {
        return caseMapping;
    }

    public String getCharset()
    {
        return charset;
    }

    public String geteList()
    {
        return eList;
    }

    public int getTopicLength()
    {
        return topicLength;
    }

    public int getKickLength()
    {
        return kickLength;
    }

    public int getChannelLength()
    {
        return channelLength;
    }

    public String getChannelIDLength()
    {
        return channelIDLength;
    }

    public String getStandard()
    {
        return standard;
    }

    public int getSilence()
    {
        return silence;
    }

    public boolean isRFC2812()
    {
        return rfc2812;
    }

    public boolean isPenalty()
    {
        return penalty;
    }

    public boolean iscPrivMsgExists()
    {
        return cPrivMsgExists;
    }

    public boolean iscNoticeExists()
    {
        return cNoticeExists;
    }

    public boolean isSafeList()
    {
        return safeList;
    }

    public boolean isKnockExists()
    {
        return knockExists;
    }

    public boolean isWhoX()
    {
        return whoX;
    }

    public boolean isCallerID()
    {
        return callerID;
    }

    public boolean isUserIPExists()
    {
        return userIPExists;
    }

    public int getMaxWatch()
    {
        return this.maxWatch;
    }

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
