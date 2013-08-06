/**
 * IRCMessageProcessor.java
 */
package de.ekdev.ekirc.core;

import de.ekdev.ekirc.core.event.IRCPingEvent;

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
            System.out.println(im);
        }
        catch (IRCMessageFormatException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // index, null ?
            e.printStackTrace();
        }

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
        if (im.getNumericReply() == 433)
        {
            // autorename?
        }
    }

    protected void processCommand(IRCMessage im)
    {
        if ("PING".equals(im.getCommand()))
        {
            this.ircManager.getEventManager().dispatch(new IRCPingEvent(this.ircNetwork, im.getParams().get(0)));
        }
    }

    // ------------------------------------------------------------------------

    public IRCMessageParser createDefaultIRCMessageParser()
    {
        return new IRCMessageParser();
    }

    // ------------------------------------------------------------------------

    public final IRCManager getIRCManager()
    {
        return this.ircManager;
    }

    public final IRCMessageParser getIRCMessageParser()
    {
        return this.parser;
    }
}
