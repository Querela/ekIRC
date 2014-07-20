/**
 * IRCErrorReplyEvent.java
 */
package de.ekdev.ekirc.core.event;

import java.lang.reflect.Field;

import de.ekdev.ekevent.EventListenerList;
import de.ekdev.ekirc.core.IRCMessage;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.IRCNumericServerReply;
import de.ekdev.ekirc.core.IRCUtils;

/**
 * @author ekDev
 */
public class IRCErrorReplyEvent extends IRCNetworkInfoEvent
{
    private final static EventListenerList listeners = new EventListenerList();

    public final static int IS_NO_ERROR_REPLY = -1;
    public final static String NO_ERROR_NAME = "NO_ERROR";

    private String errorName;
    private final String errorMessage;
    private final IRCErrorReplyEvent.Usage usage;

    public IRCErrorReplyEvent(IRCNetwork ircNetwork, IRCMessage ircMessage, IRCErrorReplyEvent.Usage usage,
            String errorMessage)
    {
        super(ircNetwork, ircMessage);

        if (usage == null) usage = IRCErrorReplyEvent.Usage.ALL;
        this.usage = usage;

        // user defined String
        this.errorMessage = IRCUtils.emptyToNull(errorMessage);
    }

    public IRCErrorReplyEvent(IRCNetwork ircNetwork, IRCMessage ircMessage, String errorMessage)
    {
        this(ircNetwork, ircMessage, IRCErrorReplyEvent.Usage.ALL, errorMessage);
    }

    public IRCErrorReplyEvent(IRCNetwork ircNetwork, IRCMessage ircMessage, IRCErrorReplyEvent.Usage usage)
    {
        super(ircNetwork, ircMessage);

        if (usage == null) usage = IRCErrorReplyEvent.Usage.ALL;
        this.usage = usage;

        // create error message from server reply
        this.errorMessage = this.createErrorMessage();
    }

    public IRCErrorReplyEvent(IRCNetwork ircNetwork, IRCMessage ircMessage)
    {
        this(ircNetwork, ircMessage, IRCErrorReplyEvent.Usage.ALL);
    }

    // ------------------------------------------------------------------------

    public boolean isErrorReply()
    {
        return this.getErrorCode() != IRCErrorReplyEvent.IS_NO_ERROR_REPLY;
    }

    public int getErrorCode()
    {
        if (this.getIRCMessage() == null) return IRCErrorReplyEvent.IS_NO_ERROR_REPLY;
        if (!this.getIRCMessage().isNumericReply()) return IRCErrorReplyEvent.IS_NO_ERROR_REPLY;

        return this.getIRCMessage().getNumericReply();
    }

    public synchronized String getErrorName()
    {
        if (this.errorName != null) return this.errorName;

        int errorCode = this.getErrorCode();
        if (errorCode == IRCErrorReplyEvent.IS_NO_ERROR_REPLY) return null;

        String errorName = null;
        Field[] fields = IRCNumericServerReply.class.getDeclaredFields();
        for (Field field : fields)
        {
            field.setAccessible(true);
            String fieldname = field.getName();

            if (!fieldname.startsWith("ERR_")) continue;

            try
            {
                if (errorCode == field.getInt(null))
                {
                    if (errorName == null)
                    {
                        errorName = fieldname;
                    }
                    // to allow multiple names for a single code
                    else
                    {
                        errorName += ',' + fieldname;
                    }
                }
            }
            catch (IllegalArgumentException e)
            {
            }
            catch (IllegalAccessException e)
            {
            }
        }

        this.errorName = errorName;

        return this.errorName;
    }

    protected String createErrorMessage()
    {
        // create error message from ircMessage (String after prefix, command & nickname)
        StringBuilder sb = new StringBuilder();
        if (this.getErrorCode() != IRCErrorReplyEvent.IS_NO_ERROR_REPLY && this.getIRCMessage().getParams().size() > 1)
        {
            sb.append(this.getIRCMessage().asIRCMessageString());
            int index = sb.indexOf(this.getIRCMessage().getParams().get(1));
            if (index != -1)
            {
                sb.delete(0, index);
            }
            else
            {
                sb = new StringBuilder(); // clear it ... should not happen
            }
        }
        return IRCUtils.emptyToNull(sb.toString());
    }

    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    public IRCErrorReplyEvent.Usage getUsage()
    {
        return this.usage;
    }

    // ------------------------------------------------------------------------

    public static enum Usage
    {
        ALL, CHANNEL, USER, SERVER, COMMAND, REGISTRATION
    }

    // public static enum Scope
    // {
    // ALL, CHANNEL
    // }

    // ------------------------------------------------------------------------

    @Override
    public EventListenerList getListeners()
    {
        return IRCErrorReplyEvent.listeners;
    }

    public static EventListenerList getListenerList()
    {
        return IRCErrorReplyEvent.listeners;
    }
}
