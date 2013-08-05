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
    private final IRCServerContext serverContext;

    protected final IRCMessageParser parser;

    public IRCMessageProcessor(IRCManager ircManager, IRCServerContext serverContext)
    {
        if (ircManager == null)
        {
            throw new IllegalArgumentException("Argument ircManager is null!");
        }
        if (serverContext == null)
        {
            throw new IllegalArgumentException("Argument serverContext is null!");
        }

        this.ircManager = ircManager;
        this.serverContext = serverContext;
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
            this.ircManager.getEventManager().dispatch(new IRCPingEvent(this.serverContext, im.getParams().get(0)));
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
