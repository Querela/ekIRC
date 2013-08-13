/**
 * IRCIdentity.java
 */
package de.ekdev.ekirc.core;

import java.util.Objects;

/**
 * @author ekDev
 */
public class IRCIdentity
{
    private final String realname;
    private String connectionPassword;
    private IRCUser myIRCUser;

    // TODO: extends IRCUser?

    // ------------------------------------------------------------------------

    public IRCIdentity(String realname, String password) throws NullPointerException
    {
        Objects.requireNonNull(realname, "realname must not be null!");
        IRCIdentity.validatePassword(password);

        this.realname = realname;
        this.connectionPassword = password;
    }

    // ------------------------------------------------------------------------

    public String getRealname()
    {
        return this.realname;
    }

    public String getConnectionPassword()
    {
        return this.connectionPassword;
    }

    public IRCUser getIRCUser()
    {
        return this.myIRCUser;
    }

    public void setIRCUser(IRCUser ircUser) throws NullPointerException
    {
        Objects.requireNonNull(ircUser, "ircUser must not be null!");
        this.myIRCUser = ircUser;
    }

    // ------------------------------------------------------------------------

    public static boolean validatePassword(String password) throws NullPointerException, IllegalArgumentException
    {
        Objects.requireNonNull(password, "Invalid password format: password must not be null!");
        if (password.length() == 0)
        {
            throw new IllegalArgumentException("Invalid password format: password can't be empty!");
        }
        if (password.indexOf(IRCMessage.IRC_SPACE) != -1)
        {
            throw new IllegalArgumentException("Invalid password format: password can't contain a space character!");
        }
        // TODO: validatePassword
        return true;
    }
}
