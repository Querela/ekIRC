/**
 * IRCSqueryCommand.java
 */
package de.ekdev.ekirc.core.commands.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.ekdev.ekirc.core.AsIRCMessage;
import de.ekdev.ekirc.core.IRCMessage;

/**
 * @author ekDev
 */
public class IRCSqueryCommand implements AsIRCMessage
{
    public final static String COMMAND = "SQUERY";

    private final String servicename;
    private final String text;

    public IRCSqueryCommand(String servicename, String text)
    {
        // TODO: validate
        Objects.requireNonNull(servicename, "servicename must not be null!");
        Objects.requireNonNull(text, "text must not be null!");

        this.servicename = servicename;
        this.text = text;
    }

    // ------------------------------------------------------------------------

    @Override
    public IRCMessage asIRCMessage()
    {
        List<String> params = new ArrayList<String>(2);
        params.add(this.servicename);
        params.add(this.text);
        // List<String> params = new ArrayList<String>() {
        // private static final long serialVersionUID = 1L;
        // {
        // this.add(servicename);
        // this.add(text);
        // }
        // };

        return new IRCMessage(null, IRCSqueryCommand.COMMAND, params);
    }

    @Override
    public String asIRCMessageString()
    {
        return IRCSqueryCommand.COMMAND + " " + this.servicename + " :" + this.text;
    }
}
