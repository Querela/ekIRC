/**
 * IRCMessageProcessor.java
 */
package de.ekdev.ekirc.core;

import de.ekdev.ekirc.core.event.IRCNickAlreadyInUseEvent;
import de.ekdev.ekirc.core.event.IRCPingEvent;
import de.ekdev.ekirc.core.event.IRCUnknownServerCommandEvent;

/**
 * @author ekDev
 */
public class IRCMessageProcessor
{
    private final IRCNetwork ircNetwork;

    protected final IRCMessageParser parser;

    public IRCMessageProcessor(IRCNetwork ircNetwork)
    {
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork is null!");
        }

        this.ircNetwork = ircNetwork;
        this.parser = this.createDefaultIRCMessageParser();
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
            processServerResponse(im);
        }
        else
        {
            processCommand(im);
        }
    }

    protected void processServerResponse(IRCMessage im)
    {
        IRCNumericServerReply code = IRCNumericServerReply.byCode(im.getNumericReply());

        if (code == null)
        {
            this.ircNetwork.getIRCManager().getEventManager()
                    .dispatch(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
            return;
        }

        switch (code)
        {
            case RPL_WELCOME: // 001
            case RPL_YOURHOST: // 002
            case RPL_CREATED: // 003
            case RPL_MYINFO: // 004
            case RPL_BOUNCE: // 005
                // 007
            {
                this.ircNetwork.getIRCConnectionLog().message(
                        im.getCommand() + "-Handler (Successful Registration) not yet implemented.");
                break;
            }
            case RPL_MOTDSTART: // 375
            case RPL_MOTD: // 372
            case RPL_ENDOFMOTD: // 376
                // 377
            {
                this.ircNetwork.getIRCConnectionLog().message(im.getCommand() + "-Handler (MOTD) not yet implemented.");
                break;
            }
            case RPL_LUSERCLIENT: // 251
            case RPL_LUSEROP: // 252
            case RPL_LUSERUNKNOWN: // 253
            case RPL_LUSERCHANNELS: // 254
            case RPL_LUSERME: // 255
                // 265
                // 266
            {
                this.ircNetwork.getIRCConnectionLog().message(
                        im.getCommand() + "-Handler (LUSER-Info) not yet implemented.");
                break;
            }
            case ERR_NICKNAMEINUSE:
            {
                this.ircNetwork.getIRCManager().getEventManager()
                        .dispatch(new IRCNickAlreadyInUseEvent(this.ircNetwork, im));
                break;
            }
            default:
            {
                this.ircNetwork.getIRCConnectionLog().message(im.getCommand() + "-Handler not yet implemented.");
                break;
            }
        }
    }

    protected void processCommand(IRCMessage im)
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
            this.ircNetwork.getIRCManager().getEventManager()
                    .dispatch(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
            return;
        }

        switch (isc)
        {
            case PING:
            {
                this.ircNetwork.getIRCManager().getEventManager()
                        .dispatch(new IRCPingEvent(this.ircNetwork, im.getParams().get(0)));
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
            // case QUIT:
            // {
            //
            // }
            // case NICK:
            // {
            //
            // }
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
                this.ircNetwork.disconnect();
                break;
            }
            default:
            {
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
