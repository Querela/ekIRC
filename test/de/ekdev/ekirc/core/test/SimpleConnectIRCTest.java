/**
 * SimpleConnectIRCTest.java
 */
package de.ekdev.ekirc.core.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.ekdev.ekirc.core.IRCConnection;
import de.ekdev.ekirc.core.IRCReader;
import de.ekdev.ekirc.core.IRCWriter;

/**
 * @author ekDev
 */
public class SimpleConnectIRCTest
{
    public static void main(String[] args)
    {
        IRCConnection c = new IRCConnection("irc.irchighway.net", 6667);
        c.connect();

        IRCReader r = new IRCReader(c);
        IRCWriter w = new IRCWriter(c);

        r.start();
        w.start();

        // w.sendLine("PASS abc");
        // w.sendLine("NICK absabd");
        // w.sendLine("USER abcabda \"hier\" \"irc.irchighway.net\" :absabd");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.equals("")) break;
                w.sendLine(line);
            }

            c.disconnect();
            w.stop();
            r.stop();
            // c.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
