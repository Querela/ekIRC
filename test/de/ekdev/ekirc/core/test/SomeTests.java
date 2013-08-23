/**
 * SomeTests.java
 */
package de.ekdev.ekirc.core.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.ekdev.ekirc.core.IRCDCCManager;

/**
 * @author ekDev
 */
public class SomeTests
{
    public SomeTests()
    {
    }

    public static void main(String[] args) throws UnknownHostException
    {

        long address = 1657180742;

        System.out.println("// Input long (ip address)");
        System.out.println("address      = " + address);

        byte[] addressBytes = IRCDCCManager.longToIP(address);

        System.out.println("// Converted long to ip address bytes (neg. values += 256)");
        System.out.print("address      = ");
        for (int i = 0; i < 4; i++)
        {
            if (i != 0) System.out.print('.');
            // System.out.print((addressBytes[i] < 0) ? addressBytes[i] + 256 : addressBytes[i]);
            System.out.print(addressBytes[i] & 0xFF);
        }
        System.out.println();

        System.out.println("// Output address bytes with StringBuilder and magic");
        System.out.println("address (sb) = " + IRCDCCManager.ipToString(addressBytes));

        long address2 = IRCDCCManager.ipToLong(addressBytes);

        System.out.println("// Convert ip address bytes to long");
        System.out.println("address      = " + address2);

        // --------------------------------------------------------------------

        // http://ip-lookup.net/

        System.out.println("// Get InetAddress by address bytes");
        InetAddress ia = InetAddress.getByAddress(addressBytes);
        System.out.println("address (ia) = " + ia.getHostAddress());
        System.out.println("name    (ia) = " + ia.getHostName());

        System.out.println("// Get InetAddress by long");
        ia = InetAddress.getByName("" + address);
        System.out.println("address (ia) = " + ia.getHostAddress());
        System.out.println("name    (ia) = " + ia.getHostName());

        System.out.println("// Get ip from address bytes from InetAddress");
        byte[] addressBytes2 = ia.getAddress();
        System.out.println("address (sb) = " + IRCDCCManager.ipToString(addressBytes2));

        System.out.println("------------------------------------------------");

        // String host = "irc.webchat.org";
        String host = "irc.irchighway.net";
        // String host = "irc.chatzona.org";

        ia = InetAddress.getByName(host);
        System.out.println(" - " + ia.getHostAddress());
        System.out.println(" - " + ia.getHostName());
        System.out.println(" - " + ia.getCanonicalHostName());

        System.out.println("- - --------------------------------------------");

        for (InetAddress ial : InetAddress.getAllByName(host))
        {
            System.out.print(" - " + ial.getHostAddress());
            System.out.print(" - " + ial.getHostName());
            System.out.println(" - " + ial.getCanonicalHostName());
        }

        System.out.println("- - --------------------------------------------");

    }
}
