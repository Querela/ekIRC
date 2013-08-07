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
    private final IRCManager ircManager;
    private final IRCNetwork ircNetwork;

    protected final IRCMessageParser parser;

    public IRCMessageProcessor(IRCManager ircManager, IRCNetwork ircNetwork)
    {
        if (ircManager == null)
        {
            throw new IllegalArgumentException("Argument ircManager is null!");
        }
        if (ircNetwork == null)
        {
            throw new IllegalArgumentException("Argument ircNetwork is null!");
        }

        this.ircManager = ircManager;
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
            this.ircManager.getEventManager().dispatch(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
            return;
        }

        switch (code)
        {
            case ERR_NICKNAMEINUSE:
            {
                this.ircManager.getEventManager().dispatch(new IRCNickAlreadyInUseEvent(this.ircNetwork, im));
                break;
            }
            case RPL_BOUNCE:
            {

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
            this.ircManager.getEventManager().dispatch(new IRCUnknownServerCommandEvent(this.ircNetwork, im));
            return;
        }

        switch (isc)
        {
            case PING:
            {
                this.ircManager.getEventManager().dispatch(new IRCPingEvent(this.ircNetwork, im.getParams().get(0)));
                break;
            }
            case ERROR:
            {
            }
            default:
            {
                this.ircNetwork.getIRCConnectionLog().message(im.getCommand() + "-Handler not yet implemented.");
                break;
            }
        }
    }

    // ------------------------------------------------------------------------

    public IRCMessageParser createDefaultIRCMessageParser()
    {
        return new IRCMessageParser();
    }

    // ------------------------------------------------------------------------

    protected final IRCManager getIRCManager()
    {
        return this.ircManager;
    }

    public final IRCMessageParser getIRCMessageParser()
    {
        return this.parser;
    }
}
