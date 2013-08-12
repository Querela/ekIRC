/**
 * IRCIdentity.java
 */
package de.ekdev.ekirc.core;

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

    public IRCIdentity(String realname, String password)
    {
        // TODO: validate ...

        this.realname = realname;
        this.connectionPassword = password;
    }

    public IRCIdentity(IRCUser ircUser, String realname, String password)
    {
        // TODO: validate ...
        this.myIRCUser = ircUser;

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

    public void setIRCUser(IRCUser ircUser)
    {
        this.myIRCUser = ircUser;
    }

    // ------------------------------------------------------------------------

    public static boolean validatePassword(String password)
    {
        // TODO: validatePassword
        return true;
    }
}
