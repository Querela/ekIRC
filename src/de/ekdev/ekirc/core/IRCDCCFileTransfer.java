/**
 * IRCDCCFileTransfer.java
 */
package de.ekdev.ekirc.core;

import java.io.File;
import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCDCCFileTransfer
{
    private final IRCDCCManager ircDCCManager;

    private final IRCUser sourceIRCUser;

    private final String filename;
    private final String address;
    private final int port;
    private final long size;

    public IRCDCCFileTransfer(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser, String filename, String address,
            int port, long size) throws NullPointerException, IllegalArgumentException
    {
        this.ircDCCManager = Objects.requireNonNull(ircDCCManager, "ircDCCManager must not be null!");
        this.sourceIRCUser = Objects.requireNonNull(sourceIRCUser, "sourceIRCUser must not be null!");

        // get only the last part of the filepath, the filename and extension
        Objects.requireNonNull(IRCUtils.emptyToNull(filename), "filename must not be null or empty!");
        File file = new File(filename);
        this.filename = file.getName();
        file = null;

        this.address = Objects.requireNonNull(IRCUtils.emptyToNull(address), "address must not be null or empty!");

        if (port <= 0 || port > 65535)
        {
            throw new IllegalArgumentException("a valid <port> must be between 1 and 65535");
        }
        this.port = port;

        this.size = (size < 0) ? -1 : size;
    }

    public IRCDCCFileTransfer(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser, String filename, String address,
            int port) throws NullPointerException, IllegalArgumentException
    {
        this(ircDCCManager, sourceIRCUser, filename, address, port, -1);
    }

    public static IRCDCCFileTransfer fromIRCDCCMessage(IRCDCCManager ircDCCManager, IRCUser sourceIRCUser,
            IRCDCCMessage ircDCCMessage) throws NullPointerException, IllegalArgumentException
    {
        Objects.requireNonNull(ircDCCMessage, "ircDCCMessage must not be null!");

        return new IRCDCCFileTransfer(ircDCCManager, sourceIRCUser, ircDCCMessage.getArgument(),
                ircDCCMessage.getAddress(), ircDCCMessage.getPort(), ircDCCMessage.getSize());
    }

    // ------------------------------------------------------------------------

    public final IRCUser getSourceIRCUser()
    {
        return this.sourceIRCUser;
    }

    // --------------------------------

    public String getFilename()
    {
        return this.filename;
    }

    public String getAddress()
    {
        return this.address;
    }

    public int getPort()
    {
        return this.port;
    }

    public long getSize()
    {
        return this.size;
    }

    // ------------------------------------------------------------------------

    

    // ------------------------------------------------------------------------

    public final IRCDCCManager getIRCDCCManager()
    {
        return this.ircDCCManager;
    }
}
