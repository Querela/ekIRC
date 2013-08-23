/**
 * IRCDCCManager.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

import de.ekdev.ekirc.core.event.DCCFileTransferEvent;

/**
 * @author ekDev
 */
public class IRCDCCManager
{
    public final static int TIMEOUT = 30 * 1000; // 30 sec
    public final static int BUFFER_SIZE = 8 * 1024; // 8 KB

    private final IRCNetwork ircNetwork;

    public IRCDCCManager(IRCNetwork ircNetwork)
    {
        Objects.requireNonNull(ircNetwork, "ircNetwork must not be null!");

        this.ircNetwork = ircNetwork;
    }

    // ------------------------------------------------------------------------

    public boolean processRequest(IRCUser sourceIRCUser, IRCDCCMessage ircDCCMessage)
    {
        if (sourceIRCUser == null) return false;
        if (ircDCCMessage == null) return false;

        IRCDCCType dccType = IRCDCCType.valueOf(ircDCCMessage.getType()); // .toUpperCase()

        switch (dccType)
        {
            case SEND:
            {
                IRCDCCFileTransfer ircDCCFileTransfer = null;
                try
                {
                    ircDCCFileTransfer = IRCDCCFileTransfer.fromIRCDCCMessage(this, sourceIRCUser, ircDCCMessage);
                }
                catch (Exception e)
                {
                    // NullPointerException
                    // IllegalArgumentException
                    this.ircNetwork.getIRCConnectionLog().exception(e);
                    return false;
                }
                this.ircNetwork.raiseEvent(new DCCFileTransferEvent(this.ircNetwork, ircDCCFileTransfer));
                return true;
            }
            // case ACCEPT:
            // {
            //
            // }
            // case CHAT:
            // {
            //
            // }
            default:
            {
                return false;
            }
        }
    }

    // ------------------------------------------------------------------------

    // http://www.silisoftware.com/tools/ipconverter.php

    // using long to avoid the sign: ... int i = 4294967295

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
