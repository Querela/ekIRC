/**
 * AutoReconnector.java
 */
package de.ekdev.ekirc.core.event.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.ekdev.ekevent.EventHandler;
import de.ekdev.ekevent.EventListener;
import de.ekdev.ekirc.core.IRCNetwork;
import de.ekdev.ekirc.core.event.IRCDisconnectEvent;

/**
 * @author ekDev
 */
public class AutoReconnector implements EventListener
{
    private final IRCNetwork ircNetwork;
    private final int timeout;
    private final int times;

    private int reconnectCounter = 0;

    public AutoReconnector(IRCNetwork ircNetwork, int millisecondsTimeout, int timesToSuccessfullyReconnect)
    {
        // no ircNetwork means for all networks
        if (millisecondsTimeout < 10) millisecondsTimeout = 15000;
        // timesToSuccessfullyReconnect <= 0 -> infinity ...

        this.ircNetwork = ircNetwork;
        this.timeout = millisecondsTimeout;
        this.times = timesToSuccessfullyReconnect;
    }

    @EventHandler
    public void onDisconnect(IRCDisconnectEvent ide)
    {
        // abort if no reconnect possible
        if (!ide.isReconnectPossible()) return;

        // if no ircNetwork given then continue
        if (this.ircNetwork != null && !this.ircNetwork.equals(ide.getIRCNetwork())) return;

        final IRCNetwork inet = ide.getIRCNetwork();
        final ScheduledExecutorService stp = Executors.newScheduledThreadPool(1);
        final Runnable tr = new Runnable() {
            private int tryCounter = 0;

            @Override
            public void run()
            {
                if (inet.isConnected())
                {
                    inet.getIRCConnectionLog().message("Successfully reconnected after " + tryCounter + " tries ...");
                    stp.shutdown();
                    return;
                }

                inet.getIRCConnectionLog().message("Trying to reconnect ... [" + ++tryCounter + "]");
                try
                {
                    inet.reconnect();
                }
                catch (Exception e)
                {
                    // display only the first time ...
                    if (tryCounter == 1) inet.getIRCConnectionLog().exception(e);
                }
            }
        };
        stp.scheduleAtFixedRate(tr, this.timeout, this.timeout, TimeUnit.MILLISECONDS);

        if (this.times > 0 && ++reconnectCounter >= this.times)
        {
            IRCDisconnectEvent.getListenerList().unregister(this);
            inet.getIRCConnectionLog().message(
                    "Unregistering " + this.getClass().getSimpleName() + " after " + reconnectCounter
                            + " reconnect(s) ...");
        }
    }
}
