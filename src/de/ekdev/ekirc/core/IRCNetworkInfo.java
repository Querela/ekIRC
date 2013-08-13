/**
 * IRCNetworkInfo.java
 */
package de.ekdev.ekirc.core;

import java.util.ArrayList;
import java.util.Collections;
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
    private String prefixes;
    private String channelTypes;
    private String channelModes2;
    private int maxModes;
    private int maxChannels;
    private String chanLimit;
    private int maxNickLength;
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

    //
    private List<String> motd;
    private List<String> tempMotd;

    // --------------------------------------------------------------------
    // --------------------------------------------------------------------

    public IRCNetworkInfo()
    {
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

        this.prefixes = ircNetworkInfo.prefixes;
        this.channelTypes = ircNetworkInfo.channelTypes;
        this.channelModes2 = ircNetworkInfo.channelModes2;
        this.maxModes = ircNetworkInfo.maxModes;
        this.maxChannels = ircNetworkInfo.maxChannels;
        this.chanLimit = ircNetworkInfo.chanLimit;
        this.maxNickLength = ircNetworkInfo.maxNickLength;
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

        this.motd = new ArrayList<>(ircNetworkInfo.motd);
        this.tempMotd = new ArrayList<>(ircNetworkInfo.tempMotd);
    }

    // --------------------------------------------------------------------

    public IRCNetworkInfo update(IRCMessage ircMessage)
    {
        if (ircMessage == null) return this;
        if (!ircMessage.isNumericReply()) return this;

        if (ircMessage.getNumericReply() == IRCNumericServerReply.RPL_MYINFO)
        {

        }
        else if (ircMessage.getNumericReply() == IRCNumericServerReply.RPL_ISUPPORT)
        {

        }

        return this;
    }

    // --------------------------------------------------------------------

    public IRCNetworkInfo setServerName(String serverName)
    {
        this.serverName = serverName;
        return this;
    }

    public IRCNetworkInfo setServerVersion(String serverVersion)
    {
        this.serverVersion = serverVersion;
        return this;
    }

    public IRCNetworkInfo setUserModes(String userModes)
    {
        this.userModes = userModes;
        return this;
    }

    public IRCNetworkInfo setChannelModes(String channelModes)
    {
        this.channelModes = channelModes;
        return this;
    }

    public IRCNetworkInfo setPrefixes(String prefixes)
    {
        this.prefixes = prefixes;
        return this;
    }

    public IRCNetworkInfo setChannelTypes(String channelTypes)
    {
        this.channelTypes = channelTypes;
        return this;
    }

    public IRCNetworkInfo setChannelModes2(String channelModes2)
    {
        // TODO: ?
        this.channelModes2 = channelModes2;
        return this;
    }

    public IRCNetworkInfo setMaxModes(int maxModes)
    {
        this.maxModes = maxModes;
        return this;
    }

    public IRCNetworkInfo setMaxChannels(int maxChannels)
    {
        this.maxChannels = maxChannels;
        return this;
    }

    public IRCNetworkInfo setChanLimit(String chanLimit)
    {
        this.chanLimit = chanLimit;
        return this;
    }

    public IRCNetworkInfo setMaxNickLength(int maxNickLength)
    {
        this.maxNickLength = maxNickLength;
        return this;
    }

    public IRCNetworkInfo setMaxBans(int maxBans)
    {
        this.maxBans = maxBans;
        return this;
    }

    public IRCNetworkInfo setMaxList(String maxList)
    {
        this.maxList = maxList;
        return this;
    }

    public IRCNetworkInfo setNetwork(String network)
    {
        this.network = network;
        return this;
    }

    public IRCNetworkInfo setExceptBans(String exceptBans)
    {
        this.exceptBans = exceptBans;
        return this;
    }

    public IRCNetworkInfo setExceptInvites(String exceptInvites)
    {
        this.exceptInvites = exceptInvites;
        return this;
    }

    public IRCNetworkInfo setWallOps(boolean wallOps)
    {
        this.wallOps = wallOps;
        return this;
    }

    public IRCNetworkInfo setWallVoices(boolean wallVoices)
    {
        this.wallVoices = wallVoices;
        return this;
    }

    public IRCNetworkInfo setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
        return this;
    }

    public IRCNetworkInfo setCaseMapping(String caseMapping)
    {
        this.caseMapping = caseMapping;
        return this;
    }

    public IRCNetworkInfo setCharset(String charset)
    {
        this.charset = charset;
        return this;
    }

    public IRCNetworkInfo seteList(String eList)
    {
        this.eList = eList;
        return this;
    }

    public IRCNetworkInfo setTopicLength(int topicLength)
    {
        this.topicLength = topicLength;
        return this;
    }

    public IRCNetworkInfo setKickLength(int kickLength)
    {
        this.kickLength = kickLength;
        return this;
    }

    public IRCNetworkInfo setChannelLength(int channelLength)
    {
        this.channelLength = channelLength;
        return this;
    }

    public IRCNetworkInfo setChannelIDLength(String channelIDLength)
    {
        this.channelIDLength = channelIDLength;
        return this;
    }

    public IRCNetworkInfo setStandard(String standard)
    {
        this.standard = standard;
        return this;
    }

    public IRCNetworkInfo setSilence(int silence)
    {
        this.silence = silence;
        return this;
    }

    public IRCNetworkInfo setRfc2812(boolean rfc2812)
    {
        this.rfc2812 = rfc2812;
        return this;
    }

    public IRCNetworkInfo setPenalty(boolean penalty)
    {
        this.penalty = penalty;
        return this;
    }

    public IRCNetworkInfo setcPrivMsgExists(boolean cPrivMsgExists)
    {
        this.cPrivMsgExists = cPrivMsgExists;
        return this;
    }

    public IRCNetworkInfo setcNoticeExists(boolean cNoticeExists)
    {
        this.cNoticeExists = cNoticeExists;
        return this;
    }

    public IRCNetworkInfo setSafeList(boolean safeList)
    {
        this.safeList = safeList;
        return this;
    }

    public IRCNetworkInfo setKnockExists(boolean knockExists)
    {
        this.knockExists = knockExists;
        return this;
    }

    public IRCNetworkInfo setWhoX(boolean whoX)
    {
        this.whoX = whoX;
        return this;
    }

    public IRCNetworkInfo setCallerID(boolean callerID)
    {
        this.callerID = callerID;
        return this;
    }

    public IRCNetworkInfo setUserIPExists(boolean userIPExists)
    {
        this.userIPExists = userIPExists;
        return this;
    }

    // --------------------------------------------------------------------

    public IRCNetworkInfo newMotd()
    {
        this.tempMotd = new ArrayList<>();
        return this;
    }

    public IRCNetworkInfo addModtLine(String line)
    {
        if (line == null) return this;

        this.tempMotd.add(line);
        return this;
    }

    public IRCNetworkInfo finishNewMotd()
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
        return maxNickLength;
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
