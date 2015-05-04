/**
 * IRCDCCMessage.java
 */
package de.ekdev.ekirc.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCDCCMessage
{
    public final static long INVALID_SIZE_ARGUMENT = -1;

    // TODO: safe data in list? (passive dcc, varying argument count)
    private final String type;
    private final String argument;
    private final String address;
    private final int port;
    private final long size;

    public IRCDCCMessage(String type, String argument, String address, int port, long size)
            throws NullPointerException
    {
        Objects.requireNonNull(type, "type must not be null!");
        Objects.requireNonNull(argument, "argument must not be null!");
        Objects.requireNonNull("address", "address must not be null!");

        this.type = type;
        this.argument = argument;
        this.address = address;
        this.port = port;
        this.size = size;
    }

    public IRCDCCMessage(String type, String argument, String address, String port, String size)
    {
        this(type, argument, address, Integer.valueOf(Objects.requireNonNull(port, "port must not be null!")),
                ((size != null) ? Long.valueOf(size) : IRCDCCMessage.INVALID_SIZE_ARGUMENT));
    }

    public IRCDCCMessage(String type, String argument, String address, String port)
    {
        this(type, argument, address, port, null);
    }

    public IRCDCCMessage(String type, String argument, String address, int port)
    {
        this(type, argument, address, port, IRCDCCMessage.INVALID_SIZE_ARGUMENT);
    }

    public static IRCDCCMessage createIRCDCCMessageFromTokens(String[] tokens)
            throws NullPointerException, IRCDCCMessageFormatException
    {
        Objects.requireNonNull(tokens, "tokens must not be null!");

        if (tokens.length < 4)
        {
            throw new IRCDCCMessageFormatException(
                    "DCC message must contain at least 4 tokens separated with space characters!");
        }
        if (tokens.length > 5)
        {
            throw new IRCDCCMessageFormatException("DCC message must not contain more than 5 tokens!");
        }

        return new IRCDCCMessage(tokens[0], tokens[1], tokens[2], tokens[3], ((tokens.length == 5) ? tokens[4] : null));
    }

    // ------------------------------------------------------------------------

    public String getType()
    {
        return this.type;
    }

    public String getArgument()
    {
        return this.argument;
    }

    public String getAddress()
    {
        return this.address;
    }

    public InetAddress getInetAddress()
            throws UnknownHostException
    {
        return InetAddress.getByName("" + this.address);
    }

    public int getPort()
    {
        return this.port;
    }

    public long getSize()
    {
        return this.size;
    }

    public boolean hasSize()
    {
        return this.size != IRCDCCMessage.INVALID_SIZE_ARGUMENT;
    }

    // ------------------------------------------------------------------------

    public IRCExtendedDataMessage getAsIRCExtendedDataMessage()
    {
        StringBuilder sb = new StringBuilder().append(this.type).append(' ').append(this.argument).append(' ')
                .append(this.address).append(' ').append(this.port);
        if (this.hasSize()) sb.append(' ').append(this.size);

        return new IRCExtendedDataMessage("DCC", sb.toString());
    }

    // ------------------------------------------------------------------------

    @Override
    public String toString()
    {
        return "IRCDCCMessage [type=" + type + ", argument=" + argument + ", address=" + address + ", port=" + port
                + ", size=" + size + "]";
    }
}
