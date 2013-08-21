/**
 * IRCDCCManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCDCCManager
{
    private final IRCNetwork ircNetwork;

    public IRCDCCManager(IRCNetwork ircNetwork)
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;
    }

    // ------------------------------------------------------------------------

    public boolean processRequest(IRCUser sourceIRCUser, IRCDCCMessage ircDCCMessage)
    {
        return false;
    }

    // ------------------------------------------------------------------------

    // http://www.silisoftware.com/tools/ipconverter.php

    public static byte[] longToIP(long address)
    {
        byte[] ip = new byte[4];

        for (int i = 3; i >= 0; i--)
        {
            ip[i] = (byte) (address % 256);
            address = address / 256;
        }

        return ip;
    }

    public static long ipToLong(byte[] addressBytes)
    {
        if (Objects.requireNonNull(addressBytes, "addressBytes must not be null!").length != 4)
        {
            throw new IllegalArgumentException("Argument addressBytes must be an byte array with length 4!");
        }

        long address = 0;

        for (int i = 0; i < 4; i++)
        {
            address <<= 8; // *= 256
            address += addressBytes[i] & 0xFF; // signed byte to unsigned int
        }

        return address;
    }

    public static String ipToString(byte[] addressBytes)
    {
        if (Objects.requireNonNull(addressBytes, "addressBytes must not be null!").length != 4)
        {
            throw new IllegalArgumentException("Argument addressBytes must be an byte array with length 4!");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++)
        {
            if (i != 0) sb.append('.');
            sb.append(addressBytes[i] & 0xFF);
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------------

    public final IRCNetwork getIRCNetwork()
    {
        return this.ircNetwork;
    }
}
