/**
 * IRCMessageProcessor.java
 */
package de.ekdev.ekirc.core;

import static de.ekdev.ekirc.core.IRCNumericServerReply.*;

import java.util.Objects;

import de.ekdev.ekirc.core.event.IRCNetworkInfoEvent;
import de.ekdev.ekirc.core.event.IRCPingEvent;
import de.ekdev.ekirc.core.event.IRCUnknownServerCommandEvent;
import de.ekdev.ekirc.core.event.NickAlreadyInUseEvent;
import de.ekdev.ekirc.core.event.NickChangeEvent;
import de.ekdev.ekirc.core.event.QuitEvent;
import de.ekdev.ekirc.core.event.UpdatedChannelListEvent;
import de.ekdev.ekirc.core.event.UpdatedMotdEvent;
import de.ekdev.ekirc.core.event.UpdatingMotdEvent;

/**
 * @author ekDev
 */
public class IRCMessageProcessor
{
    private final IRCNetwork ircNetwork;

    protected final IRCMessageParser parser;

    protected IRCChannelList.Builder ircChannelListBuilder;

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
                this.ircNetwork.raiseEvent(new UpdatingMotdEvent(this.ircNetwork));
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
                this.ircNetwork.raiseEvent(new UpdatedMotdEvent(this.ircNetwork));
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

                this.ircNetwork.raiseEvent(new UpdatedChannelListEvent(this.ircNetwork, old));
                break; // -----------------------------------------------------
            }
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
                this.ircNetwork.raiseEvent(new IRCPingEvent(this.ircNetwork, im.getParams().get(0)));
                break;
            }
            case PRIVMSG:
            {
                this.processPrivateMessage(im);
                break;
            }
            // case NOTICE:
            // {
            //
            // }
            // case JOIN:
            // {
            //
            // }
            // case PART:
            // {
            //
            // }
            case QUIT:
            {
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUserByPrefix(im.getPrefix());
                this.ircNetwork.getIRCUserManager().removeIRCUser(ircUser);
                this.ircNetwork.raiseEvent(new QuitEvent(this.ircNetwork, ircUser, im.getParams().get(0)));
                break;
            }
            case NICK:
            {
                String sourceNick = IRCUser.getNickByPrefix(im.getPrefix());
                IRCUser ircUser = this.ircNetwork.getIRCUserManager().getIRCUser(sourceNick);
                if (ircUser == null) break;

                ircUser.setNickname(im.getParams().get(0));

                this.ircNetwork.raiseEvent(new NickChangeEvent(this.ircNetwork, ircUser, sourceNick, im.getParams()
                        .get(0)));
                break;
            }
            // case KICK:
            // {
            //
            // }
            // case MODE:
            // {
            //
            // }
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
                break;
            }
            default:
            {
                if (!handledAlready)
                    this.ircNetwork.getIRCConnectionLog().message(im.getCommand() + "-Handler not yet implemented.");
                break;
            }
        }
    }

    protected void processPrivateMessage(IRCMessage im)
    {
        // TODO: raise event and react in event listener/handler and then here? static or dynamic binding?
        // CTCP, DCC, user, channel
    }

    // ------------------------------------------------------------------------

    public IRCMessageParser createDefaultIRCMessageParser()
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
