/**
 * IRCMessageProcessor.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCMessageProcessor
{
    private final IRCManager ircManager;

    protected final IRCMessageParser parser;

    public IRCMessageProcessor(IRCManager ircManager)
    {
        if (ircManager == null)
        {
            throw new IllegalArgumentException("Argument ircManager is null!");
        }

        this.ircManager = ircManager;
        this.parser = this.createDefaultIRCMessageParser();
    }

    // ------------------------------------------------------------------------

    public void handleLine(String line)
    {
        try
        {
            IRCMessage im = this.parser.parseRawLine(line);
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
