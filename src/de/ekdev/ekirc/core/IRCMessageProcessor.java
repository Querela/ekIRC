/**
 * IRCMessageProcessor.java
 */
package de.ekdev.ekirc.core;

import static de.ekdev.ekirc.core.IRCNumericServerReply.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.event.ChannelListUpdateEvent;
import de.ekdev.ekirc.core.event.IRCNetworkInfoEvent;
import de.ekdev.ekirc.core.event.IRCUnknownServerCommandEvent;
import de.ekdev.ekirc.core.event.JoinEvent;
import de.ekdev.ekirc.core.event.KickEvent;
import de.ekdev.ekirc.core.event.MotdUpdatedEvent;
import de.ekdev.ekirc.core.event.MotdUpdatingEvent;
import de.ekdev.ekirc.core.event.NickAlreadyInUseEvent;
import de.ekdev.ekirc.core.event.NickChangeEvent;
import de.ekdev.ekirc.core.event.PartEvent;
import de.ekdev.ekirc.core.event.PingEvent;
import de.ekdev.ekirc.core.event.QuitEvent;

/**
 * @author ekDev
 */
public class IRCMessageProcessor
{
    // TODO: work with chars?
    public final static String MQUOTE = "\u0010";
    public final static String CTCP_XDELIM = "\u0001";
    public final static String CTCP_XQUOTE = "\\"; // "\u005C\u005C";

    private final IRCNetwork ircNetwork;

    protected final IRCMessageParser parser;

    private IRCChannelList.Builder ircChannelListBuilder;

    // ------------------------------------------------------------------------

    public IRCMessageProcessor(IRCNetwork ircNetwork) throws NullPointerException
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;
        this.parser = this.createDefaultIRCMessageParser();

        this.ircChannelListBuilder = new IRCChannelList.Builder();
    }

    // ------------------------------------------------------------------------

    public void handleLine(String line)
    {
        IRCMessage im = null;
        try
        {
            im = this.parser.parseRawLine(line);
        }
        catch (IRCMessageFormatException e)
        {
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }
        catch (Exception e)
        {
            // index, null ?
            this.ircNetwork.getIRCConnectionLog().exception(e);
        }

        // silently ignore empty messages
        if (im == null) return;

        if (im.isNumericReply())
        {
            processServerResponse(im, false);
        }
        else
        {
            processCommand(im, false);
        }
    }

    protected void processServerResponse(IRCMessage im, boolean handledAlready)
    {
        // im.getParams().get(0); // --> always me ?
        // this.ircNetwork.ircConnectionLog.object("im", im);

        switch (im.getNumericReply())
        {
            case RPL_WELCOME:
            {
                int i = im.getParams().get(1).lastIndexOf(IRCMessage.IRC_SPACE);
                String nickuserhost = im.getParams().get(1).substring(i + 1);

                // set my user object !
                IRCUser ircUser = new IRCUser(this.ircNetwork.getIRCUserManager(),
                        IRCUser.getNickByPrefix(nickuserhost));
                ircUser.setUsername(IRCUser.getUsernameByPrefix(nickuserhost));
                ircUser.setHostmask(IRCUser.getHostByPrefix(nickuserhost));
                this.ircNetwork.getMyIRCIdentity().setIRCUser(ircUser);
                this.ircNetwork.getIRCConnectionLog().object("ircUser", ircUser);
                // go on
            }
            case RPL_YOURHOST:
            case RPL_CREATED:
            case RPL_MYINFO:
            case RPL_ISUPPORT:
            {
                this.ircNetwork.raiseEvent(new IRCNetworkInfoEvent(this.ircNetwork, im));
                this.ircNetwork.getIRCNetworkInfo().update(im);
                break; // -----------------------------------------------------
            }
            case RPL_MOTDSTART:
            {
                this.ircNetwork.raiseEvent(new IRCNetworkInfoEvent(this.ircNetwork, im));
                this.ircNetwork.raiseEvent(new MotdUpdatingEvent(this.ircNetwork));
                this.ircNetwork.getIRCNetworkInfo().newMotd().addModtLine(im.getParams().get(1).substring(2));
                break; // -----------------------------------------------------
            }
            case RPL_MOTD:
            {
                this.ircNetwork.raiseEvent(new IRCNetworkInfoEvent(this.ircNetwork, im));
                this.ircNetwork.getIRCNetworkInfo().addModtLine(im.getParams().get(1).substring(2));
                break; // -----------------------------------------------------
            }
            case RPL_ENDOFMOTD:
            {
                this.ircNetwork.raiseEvent(new IRCNetworkInfoEvent(this.ircNetwork, im));
                this.ircNetwork.getIRCNetworkInfo().addModtLine(im.getParams().get(1).substring(2)).finishNewMotd();
                this.ircNetwork.raiseEvent(new MotdUpdatedEvent(this.ircNetwork));
                break; // -----------------------------------------------------
            }
            case RPL_LUSERCLIENT:
            case RPL_LUSEROP:
            case RPL_LUSERUNKNOWN:
            case RPL_LUSERCHANNELS:
            case RPL_LUSERME:
            {
                this.ircNetwork.raiseEvent(new IRCNetworkInfoEvent(this.ircNetwork, im));
                this.ircNetwork.getIRCConnectionLog().message(
                        im.getCommand() + "-Handler (LUSER-Info) not yet implemented.");
                break; // -----------------------------------------------------
            }
            case ERR_NICKNAMEINUSE:
            {
                this.ircNetwork.raiseEvent(new NickAlreadyInUseEvent(this.ircNetwork, im));
                break; // -----------------------------------------------------
            }
            // ----------------------------------------------------------------
            // reply to NAMES command

            // ----------------------------------------------------------------
            // reply to LIST command
            case RPL_LISTSTART:
            {
                // we could also ignore this reply ...
                this.ircChannelListBuilder.clear();
                this.ircNetwork.getIRCConnectionLog().object("im", im);
                break;
            }
            case RPL_LIST:
            {
                try
                {
                    this.ircChannelListBuilder.add(im.getParams().get(1), im.getParams().get(2), im.getParams().get(3));
                }
                catch (Exception e)
                {
                    // shouldn't occur but better safe than sorry ... ;-)
                    // IRCMessageFormatException
                    // NullPointerException
                    // IllegalArgumentException
                    this.ircNetwork.getIRCConnectionLog().exception(e);
                }
                break; // -----------------------------------------------------
            }
            case RPL_LISTEND:
            {
                IRCChannelList old = this.ircNetwork.getIRCChannelManager().getIRCChannelList();

                this.ircChannelListBuilder.sort(); // ?
                this.ircNetwork.getIRCChannelManager().updateIRCChannelList(ircChannelListBuilder.build());
                this.ircChannelListBuilder.clear();

                this.ircNetwork.raiseEvent(new ChannelListUpdateEvent(this.ircNetwork, old));
                break; // -----------------------------------------------------
            }
            // ----------------------------------------------------------------
            default:
            {
                if (!handledAlready) this.ircNetwork.raiseEvent(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
                break;
            }
        }
    }

    protected void processCommand(IRCMessage im, boolean handledAlready)
    {
        // TODO: EnumMap ?
        IRCServerCommand isc = null;;
        try
        {
            isc = IRCServerCommand.valueOf(im.getCommand());
        }
        catch (Exception e)
        {
        }

        if (isc == null)
        {
            this.ircNetwork.raiseEvent(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
            return;
        }

        switch (isc)
        {
            case PING:
            {
                // TODO: this.ircNetwork.sendPong(im.getParams().get(0)); // send reply immediately?
                this.ircNetwork.raiseEvent(new PingEvent(this.ircNetwork, im.getParams().get(0)));
                break; // -----------------------------------------------------
            }
            case PRIVMSG:
            {
                this.processPrivateMessage(im);
                break; // -----------------------------------------------------
            }
            case NOTICE:
            {
                this.processNotice(im);
                break; // -----------------------------------------------------
            }
            case JOIN:
            {
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUserByPrefix(im.getPrefix());
                IRCChannel ircChannel = this.ircNetwork.getIRCChannelManager().getIRCChannel(im.getParams().get(0));

                if (ircUser.isMe())
                {
                    // TODO: request information ... or as reaction to event?
                    // TODO: WHO/NAMES, MODE
                }

                ircChannel.addIRCUser(ircUser);

                this.ircNetwork.raiseEvent(new JoinEvent(this.ircNetwork, ircChannel, ircUser));
                break; // -----------------------------------------------------
            }
            case PART:
            {
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUserByPrefix(im.getPrefix());
                IRCChannel ircChannel = this.ircNetwork.getIRCChannelManager().getIRCChannel(im.getParams().get(0));
                String reason = (im.getParams().size() > 1) ? im.getParams().get(1) : null;

                if (ircUser.isMe())
                {
                    // remove channel?
                    // this.ircNetwork.getIRCChannelManager().removeIRCChannel(ircChannel);
                }

                // TODO: to get a snapshot add code here
                ircChannel.removeIRCUser(ircUser);

                this.ircNetwork.raiseEvent(new PartEvent(this.ircNetwork, ircChannel, ircUser, reason));
                break; // -----------------------------------------------------
            }
            case QUIT:
            {
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUserByPrefix(im.getPrefix());
                // TODO: to get a snapshot add code here
                this.ircNetwork.getIRCUserManager().removeIRCUser(ircUser);
                this.ircNetwork.raiseEvent(new QuitEvent(this.ircNetwork, ircUser, im.getParams().get(0)));
                break; // -----------------------------------------------------
            }
            case NICK:
            {
                String sourceNick = IRCUser.getNickByPrefix(im.getPrefix());
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUser(sourceNick);
                if (ircUser == null) break;

                ircUser.setNickname(im.getParams().get(0));

                this.ircNetwork.raiseEvent(new NickChangeEvent(this.ircNetwork, ircUser, sourceNick, im.getParams()
                        .get(0)));
                break; // -----------------------------------------------------
            }
            case KICK:
            {
                IRCUser source = this.ircNetwork.getIRCUserManager().getIRCUserByPrefix(im.getPrefix());
                IRCChannel ircChannel = this.ircNetwork.getIRCChannelManager().getIRCChannel(im.getParams().get(0));
                IRCUser recipient = this.ircNetwork.getIRCUserManager().getIRCUser(im.getParams().get(1));
                String reason = (im.getParams().size() > 2) ? im.getParams().get(2) : null;

                if (recipient.isMe())
                {
                    // kill the source ... ;-)
                }

                ircChannel.removeIRCUser(recipient);

                this.ircNetwork.raiseEvent(new KickEvent(this.ircNetwork, ircChannel, source, recipient, reason));
                break; // -----------------------------------------------------
            }
            case MODE:
            {
                this.processMode(im);
                break; // -----------------------------------------------------
            }
            // case TOPIC:
            // {
            //
            // }
            // case INVITE:
            // {
            //
            // }
            case ERROR:
            {
                // TODO: something more to do?
                this.ircNetwork.getIRCConnectionLog().message(
                        im.getCommand() + " - reason: '" + im.getParams().get(0) + "'");
                this.ircNetwork.disconnect(false);
                break; // -----------------------------------------------------
            }
            default:
            {
                if (!handledAlready)
                    this.ircNetwork.getIRCConnectionLog().message(im.getCommand() + "-Handler not yet implemented.");
                break;
            }
        }
    }

    // --------------------------------

    protected void processPrivateMessage(IRCMessage im)
    {
        // TODO: raise event and react in event listener/handler and then here? static or dynamic binding?
        // CTCP, DCC, user, channel
        this.ircNetwork.getIRCConnectionLog().object("im", im);

        String message = im.getParams().get(1);
        if (message.startsWith(IRCMessageProcessor.CTCP_XDELIM) && message.endsWith(IRCMessageProcessor.CTCP_XDELIM))
        {
            message = message.substring(1, message.length() - 1);

            this.ircNetwork.getIRCConnectionLog().message("CTCP message: '" + message + "'");
        }
        // check if exists ...
        else if (message.indexOf(IRCMessageProcessor.CTCP_XDELIM) != -1)
        {
            this.ircNetwork.getIRCConnectionLog().message("check this CTCP message?: '" + message + "'");
        }

        if (this.containsCTCPMessage(im.getParams().get(1))) this.processCTCP(im, im.getParams().get(1));
    }

    protected void processNotice(IRCMessage im)
    {
        // TODO: can contain CTCP answers ...
        this.ircNetwork.getIRCConnectionLog().object("im", im);

        if (this.containsCTCPMessage(im.getParams().get(1))) this.processCTCP(im, im.getParams().get(1));
    }

    protected void processCTCP(IRCMessage im, String ctcpMessage)
    {
        this.ircNetwork.getIRCConnectionLog().message("Is CTCP in: " + im.getCommand());
        this.ircNetwork.getIRCConnectionLog().object("lowLevelMessage   ", ctcpMessage);
        String middleLevelMessage = this.dequoteLowLevel(ctcpMessage);
        this.ircNetwork.getIRCConnectionLog().object("middleLevelMessage", middleLevelMessage);
        String highLevelMessage = this.dequoteCTCP(this.removeCTCPMessages(middleLevelMessage));
        this.ircNetwork.getIRCConnectionLog().object("highLevelMessage  ", highLevelMessage);

        List<ExtendedDataMessage> le = this.extractCTCPData(middleLevelMessage);
        for (ExtendedDataMessage edm : le)
        {
            this.ircNetwork.getIRCConnectionLog().object("XXX extDataMsg", edm);
        }

        // TODO: something
    }

    // ------------

    protected String quoteCTCP(String highLevelMessage)
    {
        StringBuilder sb = new StringBuilder(highLevelMessage);

        // X-DELIM --> X-QUOTE 'a'
        // 0x01 -> \a
        int index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.CTCP_XDELIM, index)) != -1)
        {
            sb.replace(index, index + 1, IRCMessageProcessor.CTCP_XQUOTE + 'a');
            index += 2;
        }

        // X-QUOTE --> X-QUOTE X-QUOTE
        // \ -> \\
        index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.CTCP_XQUOTE, index)) != -1)
        {
            sb.insert(index, IRCMessageProcessor.CTCP_XQUOTE);
            index += 2;
        }

        return sb.toString();
    }

    protected String dequoteCTCP(String middleLevelMessage)
    {
        StringBuilder sb = new StringBuilder(middleLevelMessage);

        // X-QUOTE [NOT X-QUOTE | 'a'] -> ignore X-QUOTE
        // TODO: how?

        // X-QUOTE X-QUOTE --> X-QUOTE
        int index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.CTCP_XQUOTE + IRCMessageProcessor.CTCP_XQUOTE, index)) != -1)
        {
            sb.deleteCharAt(index);
            index += 1;
        }

        // X-QUOTE 'a' --> X-DELIM
        index = 0;
        String quotea = IRCMessageProcessor.CTCP_XQUOTE + 'a';
        while ((index = sb.indexOf(quotea, index)) != -1)
        {
            sb.replace(index, index + 2, IRCMessageProcessor.CTCP_XDELIM);
            index += 1;
        }

        return sb.toString();
    }

    protected boolean containsCTCPMessage(String message)
    {
        return (message != null && message.indexOf(IRCMessageProcessor.CTCP_XDELIM) != -1);
    }

    // ------------

    protected List<Integer> getCTCPMessageIndizes(String message)
    {
        // gets the indizes of the CTCP_XDELIMs
        // returns an even sized list with indizes
        List<Integer> ints = new ArrayList<Integer>();

        int index = 0;
        while ((index = message.indexOf(IRCMessageProcessor.CTCP_XDELIM, index)) != -1)
        {
            ints.add(index);
            index++;
        }

        // last checks
        if (ints.size() > 2 && ints.size() % 2 == 1) ints.remove(ints.size() - 2); // fix
        if (ints.size() == 1) return new ArrayList<Integer>(); // would result in an error ?

        return ints;
    }

    protected String removeCTCPMessages(String message)
    {
        StringBuilder sb = new StringBuilder(message);

        List<Integer> ints = this.getCTCPMessageIndizes(message);
        for (int i = 0; i < ints.size(); i += 2)
        {
            sb.replace(ints.get(i), ints.get(i + 1) + 1, "");
        }

        return sb.toString();
    }

    protected List<String> extractCTCPMessages(String message)
    {
        List<String> list = new ArrayList<>();

        List<Integer> ints = this.getCTCPMessageIndizes(message);
        for (int i = 0; i < ints.size(); i += 2)
        {
            // empty messages too
            list.add(message.substring(ints.get(i) + 1, ints.get(i + 1)));
        }

        return list;
    }

    protected List<ExtendedDataMessage> extractCTCPData(String message)
    {
        List<ExtendedDataMessage> list = new ArrayList<>();

        List<String> messages = this.extractCTCPMessages(message);
        for (String m : messages)
        {
            if (m.length() == 0)
            {
                // no tag -> empty message
                list.add(new ExtendedDataMessage(null, null));
                continue;
            }

            int i = m.indexOf(' ');
            if (i == -1)
            {
                // no space -> only tag
                list.add(new ExtendedDataMessage(m, null));
                continue;
            }
            else
            {
                // with space -> tag + opt. ext.data
                String tag = m.substring(0, i);
                String extData = ((m.length() > i + 1) ? m.substring(i + 1) : "");
                list.add(new ExtendedDataMessage(tag, extData));
                continue;
            }
        }

        return list;
    }

    // ------------

    protected String quoteLowLevel(String middleLevelMessage)
    {
        // middle level (can contain every character) -> low level
        StringBuilder sb = new StringBuilder(middleLevelMessage);

        // M-QUOTE --> M-QUOTE M-QUOTE
        int index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE, index)) != -1)
        {
            sb.insert(index, IRCMessageProcessor.MQUOTE);
            index += 2;
        }

        // NUL --> M-QUOTE '0'
        index = 0;
        while ((index = sb.indexOf("\u0000", index)) != -1)
        {
            sb.replace(index, index + 1, IRCMessageProcessor.MQUOTE + '0');
            index += 2;
        }

        // NL --> M-QUOTE 'n'
        index = 0;
        while ((index = sb.indexOf("\n", index)) != -1)
        {
            sb.replace(index, index + 1, IRCMessageProcessor.MQUOTE + 'n');
            index += 2;
        }

        // CR --> M-QUOTE 'r'
        index = 0;
        while ((index = sb.indexOf("\r", index)) != -1)
        {
            sb.replace(index, index + 1, IRCMessageProcessor.MQUOTE + 'r');
            index += 2;
        }

        return sb.toString();
    }

    protected String dequoteLowLevel(String lowLevelMessage)
    {
        // low level -> middle level
        StringBuilder sb = new StringBuilder(lowLevelMessage);

        // M-QUOTE 'r' --> CR
        int index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE + 'r', index)) != -1)
        {
            sb.replace(index, index + 2, "\r");
            index += 1;
        }

        // M-QUOTE 'n' --> NL
        index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE + 'n', index)) != -1)
        {
            sb.replace(index, index + 2, "\n");
            index += 1;
        }

        // M-QUOTE '0' --> NUL
        index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE + '0', index)) != -1)
        {
            sb.replace(index, index + 2, "\u0000");
            index += 1;
        }

        index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE + 'r', index)) != -1)
        {
            sb.replace(index, index + 2, "\r");
            index += 1;
        }

        // M-QUOTE M-QUOTE --> M-QUOTE
        index = 0;
        while ((index = sb.indexOf(IRCMessageProcessor.MQUOTE + IRCMessageProcessor.MQUOTE, index)) != -1)
        {
            sb.deleteCharAt(index);
            index += 1;
        }

        return sb.toString();
    }

    // ------------

    protected static class ExtendedDataMessage
    {
        private final String tag;
        private final String extendedData;

        public ExtendedDataMessage(String tag, String extendedData)
        {
            // if tag == null or empty then empty message
            // if extendedData == null then empty extendedData
            // if extendedData is empty then tag only message

            this.tag = tag;
            this.extendedData = extendedData;
        }

        public String getTag()
        {
            return this.tag;
        }

        public String getExtendedData()
        {
            return this.extendedData;
        }

        public boolean isEmpty()
        {
            return (this.tag == null || this.tag.length() == 0);
        }

        public boolean hasExtendedData()
        {
            return (this.extendedData != null && this.extendedData.length() > 0);
        }

        public static String getReadyForInsertEmpty()
        {
            return IRCMessageProcessor.CTCP_XDELIM + IRCMessageProcessor.CTCP_XDELIM;
        }

        public static String getReadyForInsertTagOnly(String tag, boolean withSpace)
        {
            // tag mustn't contain space or CTCP_XDELIM
            return IRCMessageProcessor.CTCP_XDELIM + tag + ((withSpace) ? " " : "") + IRCMessageProcessor.CTCP_XDELIM;
        }

        public static String getReadyForInsertAll(String tag, String extendedData)
        {
            // tag mustn't contain space or CTCP_XDELIM
            // extendedData mustn't contain CTCP_XDELIM
            return IRCMessageProcessor.CTCP_XDELIM + tag + " " + extendedData + IRCMessageProcessor.CTCP_XDELIM;
        }

        public String getReadyForInsert()
        {
            if (this.isEmpty()) return ExtendedDataMessage.getReadyForInsertEmpty();

            if (this.hasExtendedData()) return ExtendedDataMessage.getReadyForInsertAll(this.tag, this.extendedData);

            return ExtendedDataMessage.getReadyForInsertTagOnly(this.tag, (this.extendedData != null));
        }

        @Override
        public String toString()
        {
            return "ExtendedDataMessage [tag=" + tag + ", extendedData=" + extendedData + "]";
        }
    }

    // --------------------------------

    protected void processMode(IRCMessage im)
    {
        // TODO: ...
        this.ircNetwork.getIRCConnectionLog().object("im", im);
        // - MODE
        // - USERMODE
    }

    // ------------------------------------------------------------------------

    protected IRCMessageParser createDefaultIRCMessageParser()
    {
        return new IRCMessageParser();
    }

    // ------------------------------------------------------------------------

    protected final IRCManager getIRCManager()
    {
        return this.ircNetwork.getIRCManager();
    }

    public final IRCMessageParser getIRCMessageParser()
    {
        return this.parser;
    }
}
