/**
 * IRCServerReply.java
 */
package de.ekdev.ekirc.core;

/**
 * Numeric reply constants for IRC server.
 *
 * @author ekDev
 * @see <a href="http://datatracker.ietf.org/doc/rfc1459/?include_text=1">RFC 1459</a>
 * @see <a href="http://datatracker.ietf.org/doc/rfc2812/?include_text=1">RFC 2812</a>
 * @see <a
 * href="http://www.networksorcery.com/enp/protocol/irc.htm">http://www.networksorcery.com/enp/protocol/irc.htm</a>
 * @see <a href="https://www.alien.net.au/irc/irc2numerics.html">https://www.alien.net.au/irc/irc2numerics.html</a>
 */
public class IRCNumericServerReply
{
    // --------------------------------
    // Replies in the range from 001 to 099 are used for client-server connections only and should never travel between
    // servers:
    /**
     * "Welcome to the Internet Relay Network &lt;nick&gt;!&lt;user&gt;@&lt;host&gt;"
     */
    public final static int RPL_WELCOME = 001;
    /**
     * "Your host is &lt;servername&gt;, running version &lt;ver&gt;"
     */
    public final static int RPL_YOURHOST = 002;
    /**
     * "This server was created &lt;date&gt;"
     */
    public final static int RPL_CREATED = 003;
    /**
     * "&lt;servername&gt; &lt;version&gt; &lt;available user modes&gt; &lt;available channel modes&gt;"
     */
    public final static int RPL_MYINFO = 004;
    /**
     * http://www.irc.org/tech_docs/005.html<br />
     *
     * @see RPL_PROTOCTL
     */
    public final static int RPL_ISUPPORT = 005;
    /**
     * "Try server &lt;server name&gt;, port &lt;port number&gt;"<br />
     * never used like this?
     *
     * @see RPL_SLINE
     */
    public final static int RPL_BOUNCE = 005;

    // --------------------------------
    // Error replies are found in the range from 400 to 599:
    /**
     * "&lt;nickname&gt; :No such nick/channel"
     */
    public final static int ERR_NOSUCHNICK = 401;
    /**
     * "&lt;server name&gt; :No such server"
     */
    public final static int ERR_NOSUCHSERVER = 402;
    /**
     * "&lt;channel name&gt; :No such channel"
     */
    public final static int ERR_NOSUCHCHANNEL = 403;
    /**
     * "&lt;channel name&gt; :Cannot send to channel"
     */
    public final static int ERR_CANNOTSENDTOCHAN = 404;
    /**
     * "&lt;channel name&gt; :You have joined too many channels"
     */
    public final static int ERR_TOOMANYCHANNELS = 405;
    /**
     * "&lt;nickname&gt; :There was no such nickname"
     */
    public final static int ERR_WASNOSUCHNICK = 406;
    /**
     * "&lt;target&gt; :&lt;error code&gt; recipients. &lt;abort message&gt;"
     */
    public final static int ERR_TOOMANYTARGETS = 407;
    /**
     * "&lt;service name&gt; :No such service"
     */
    public final static int ERR_NOSUCHSERVICE = 408;
    /**
     * ":No origin specified"
     */
    public final static int ERR_NOORIGIN = 409;
    /**
     * ":No recipient given (&lt;command&gt;)"
     */
    public final static int ERR_NORECIPIENT = 411;
    /**
     * ":No text to send"
     */
    public final static int ERR_NOTEXTTOSEND = 412;
    /**
     * "&lt;mask&gt; :No toplevel domain specified"
     */
    public final static int ERR_NOTOPLEVEL = 413;
    /**
     * "&lt;mask&gt; :Wildcard in toplevel domain"
     */
    public final static int ERR_WILDTOPLEVEL = 414;
    /**
     * "&lt;mask&gt; :Bad Server/host mask"
     */
    public final static int ERR_BADMASK = 415;
    /**
     * "&lt;command&gt; [&ltmask&gt;] :&ltinfo&gt;"<br />
     * TODO: check
     */
    public final static int ERR_TOOMANYMATCHES = 416;
    /**
     * "&lt;command&gt; :Unknown command"
     */
    public final static int ERR_UNKNOWNCOMMAND = 421;
    /**
     * ":MOTD File is missing"
     */
    public final static int ERR_NOMOTD = 422;
    /**
     * "&lt;server&gt; :No administrative info available"
     */
    public final static int ERR_NOADMININFO = 423;
    /**
     * ":File error doing &lt;file op&gt; on &lt;file&gt;"
     */
    public final static int ERR_FILEERROR = 424;
    /**
     * ":No nickname given"
     */
    public final static int ERR_NONICKNAMEGIVEN = 431;
    /**
     * "&lt;nick&gt; :Erroneous nickname"
     */
    public final static int ERR_ERRONEUSNICKNAME = 432;
    /**
     * "&lt;nick&gt; :Nickname is already in use"
     */
    public final static int ERR_NICKNAMEINUSE = 433;
    /**
     * "&lt;nick&gt; :Nickname collision KILL from &lt;user&gt;@&lt;host&gt;"
     */
    public final static int ERR_NICKCOLLISION = 436;
    /**
     * "&lt;nick/channel&gt; :Nick/channel is temporarily unavailable"
     */
    public final static int ERR_UNAVAILRESOURCE = 437;
    /**
     * "&lt;nick&gt; &lt;channel&gt; :They aren't on that channel"
     */
    public final static int ERR_USERNOTINCHANNEL = 441;
    /**
     * "&lt;channel&gt; :You're not on that channel"
     */
    public final static int ERR_NOTONCHANNEL = 442;
    /**
     * "&lt;user&gt; &lt;channel&gt; :is already on channel"
     */
    public final static int ERR_USERONCHANNEL = 443;
    /**
     * "&lt;user&gt; :User not logged in"
     */
    public final static int ERR_NOLOGIN = 444;
    /**
     * ":SUMMON has been disabled"
     */
    public final static int ERR_SUMMONDISABLED = 445;
    /**
     * ":USERS has been disabled"
     */
    public final static int ERR_USERSDISABLED = 446;
    /**
     * ":You have not registered"
     */
    public final static int ERR_NOTREGISTERED = 451;
    /**
     * "&lt;command&gt; :Not enough parameters"
     */
    public final static int ERR_NEEDMOREPARAMS = 461;
    /**
     * ":Unauthorized command (already registered)"
     */
    public final static int ERR_ALREADYREGISTRED = 462;
    /**
     * ":Your host isn't among the privileged"
     */
    public final static int ERR_NOPERMFORHOST = 463;
    /**
     * ":Password incorrect"
     */
    public final static int ERR_PASSWDMISMATCH = 464;
    /**
     * ":You are banned from this server"
     */
    public final static int ERR_YOUREBANNEDCREEP = 465;
    /**
     *
     */
    public final static int ERR_YOUWILLBEBANNED = 466;
    /**
     * "&lt;channel&gt; :Channel key already set"
     */
    public final static int ERR_KEYSET = 467;
    /**
     * "&lt;channel&gt; :Cannot join channel (+l)"
     */
    public final static int ERR_CHANNELISFULL = 471;
    /**
     * "&lt;char&gt; :is unknown mode char to me for &lt;channel&gt;"
     */
    public final static int ERR_UNKNOWNMODE = 472;
    /**
     * "&lt;channel&gt; :Cannot join channel (+i)"
     */
    public final static int ERR_INVITEONLYCHAN = 473;
    /**
     * "&lt;channel&gt; :Cannot join channel (+b)"
     */
    public final static int ERR_BANNEDFROMCHAN = 474;
    /**
     * "&lt;channel&gt; :Cannot join channel (+k)"
     */
    public final static int ERR_BADCHANNELKEY = 475;
    /**
     * "&lt;channel&gt; :Bad Channel Mask"
     */
    public final static int ERR_BADCHANMASK = 476;
    /**
     * "&lt;channel&gt; :Channel doesn't support modes"
     */
    public final static int ERR_NOCHANMODES = 477;
    /**
     * "&lt;channel&gt; &lt;char&gt; :Channel list is full"
     */
    public final static int ERR_BANLISTFULL = 478;
    /**
     * ":Permission Denied- You're not an IRC operator"
     */
    public final static int ERR_NOPRIVILEGES = 481;
    /**
     * "&lt;channel&gt; :You're not channel operator"
     */
    public final static int ERR_CHANOPRIVSNEEDED = 482;
    /**
     * ":You can't kill a server!"
     */
    public final static int ERR_CANTKILLSERVER = 483;
    /**
     * ":Your connection is restricted!"
     */
    public final static int ERR_RESTRICTED = 484;
    /**
     * ":You're not the original channel operator"
     */
    public final static int ERR_UNIQOPPRIVSNEEDED = 485;
    /**
     * ":No O-lines for your host"
     */
    public final static int ERR_NOOPERHOST = 491;
    /**
     * ":Unknown MODE flag"
     */
    public final static int ERR_UMODEUNKNOWNFLAG = 501;
    /**
     * ":Cannot change mode for other users"
     */
    public final static int ERR_USERSDONTMATCH = 502;

    // --------------------------------
    // Replies generated in the response to commands are found in the range from 200 to 399:
    /**
     * ":*1&lt;reply&gt; *( " " &lt;reply&gt; )"<br />
     * reply = nickname [ "*" ] "=" ( "+" / "-" ) hostname
     */
    public final static int RPL_USERHOST = 302;
    /**
     * ":*1&lt;nick&gt; *( " " &lt;nick&gt; )"
     */
    public final static int RPL_ISON = 303;
    /**
     * "&lt;nick&gt; :&lt;away message&gt;"
     */
    public final static int RPL_AWAY = 301;
    /**
     * ":You are no longer marked as being away"
     */
    public final static int RPL_UNAWAY = 305;
    /**
     * ":You have been marked as being away"
     */
    public final static int RPL_NOWAWAY = 306;
    /**
     * "&lt;nick&gt; &lt;user&gt; &lt;host&gt; * :&lt;real name&gt;"
     */
    public final static int RPL_WHOISUSER = 311;
    /**
     * "&lt;nick&gt; &lt;server&gt; :&lt;server info&gt;"
     */
    public final static int RPL_WHOISSERVER = 312;
    /**
     * "&lt;nick&gt; :is an IRC operator"
     */
    public final static int RPL_WHOISOPERATOR = 313;
    /**
     * "&lt;nick&gt; &lt;integer&gt; :seconds idle"
     */
    public final static int RPL_WHOISIDLE = 317;
    /**
     * "&lt;nick&gt; :End of WHOIS list"
     */
    public final static int RPL_ENDOFWHOIS = 318;
    /**
     * "&lt;nick&gt; :*( ( "@" / "+" ) &lt;channel&gt; " " )"
     */
    public final static int RPL_WHOISCHANNELS = 319;
    /**
     * "&lt;nick&gt; &lt;user&gt; &lt;host&gt; * :&lt;real name&gt;"
     */
    public final static int RPL_WHOWASUSER = 314;
    /**
     * "&lt;nick&gt; :End of WHOWAS"
     */
    public final static int RPL_ENDOFWHOWAS = 369;
    /**
     * Obsolete. Not used.
     */
    public final static int RPL_LISTSTART = 321;
    /**
     * "&lt;channel&gt; &lt;# visible&gt; :&lt;topic&gt;"
     */
    public final static int RPL_LIST = 322;
    /**
     * ":End of LIST"
     */
    public final static int RPL_LISTEND = 323;
    /**
     * "&lt;channel&gt; &lt;nickname&gt;"
     */
    public final static int RPL_UNIQOPIS = 325;
    /**
     * "&lt;channel&gt; &lt;mode&gt; &lt;mode params&gt;"
     */
    public final static int RPL_CHANNELMODEIS = 324;
    /**
     * TODO: check
     */
    public final static int RPL_CREATIONTIME = 329;
    /**
     * "&lt;channel&gt; :No topic is set"
     */
    public final static int RPL_NOTOPIC = 331;
    /**
     * "&lt;channel&gt; :&lt;topic&gt;"
     */
    public final static int RPL_TOPIC = 332;
    /**
     * "&lt;channel&gt; &lt;topic setter&gt; &lt;time&gt;"
     *
     * @see RPL_TOPICWHOTIME
     */
    public final static int RPL_TOPICINFO = 333;
    /**
     * TODO: check
     */
    public final static int RPL_COMMANDSYNTAX = 334;
    /**
     * "&lt;channel&gt; &lt;nick&gt;"
     */
    public final static int RPL_INVITING = 341;
    /**
     * "&lt;user&gt; :Summoning user to IRC"
     */
    public final static int RPL_SUMMONING = 342;
    /**
     * "&lt;channel&gt; &lt;invitemask&gt;"
     */
    public final static int RPL_INVITELIST = 346;
    /**
     * "&lt;channel&gt; :End of channel invite list"
     */
    public final static int RPL_ENDOFINVITELIST = 347;
    /**
     * "&lt;channel&gt; &lt;exceptionmask&gt;"
     */
    public final static int RPL_EXCEPTLIST = 348;
    /**
     * "&lt;channel&gt; :End of channel exception list"
     */
    public final static int RPL_ENDOFEXCEPTLIST = 349;
    /**
     * "&lt;version&gt;.&lt;debuglevel&gt; &lt;server&gt; :&lt;comments&gt;"
     */
    public final static int RPL_VERSION = 351;
    /**
     * "&lt;channel&gt; &lt;user&gt; &lt;host&gt; &lt;server&gt; &lt;nick&gt; ( "H" / "G" ) ["*"] [ ( "@" / "+
     * " ) ] :&lt;hopcount&gt; &lt;real name&gt;"
     */
    public final static int RPL_WHOREPLY = 352;
    /**
     * "&lt;name&gt; :End of WHO list"
     */
    public final static int RPL_ENDOFWHO = 315;
    /**
     * "( "=" / "*" / "@" ) &lt;channel&gt; :[ "@" / "+" ] &lt;nick&gt; *( [ "@" / "+" ] &lt;nick&gt; )"
     */
    public final static int RPL_NAMREPLY = 353;
    /**
     * "&lt;channel&gt; :End of NAMES list"
     */
    public final static int RPL_ENDOFNAMES = 366;
    /**
     * "&lt;mask&gt; &lt;server&gt; :&lt;hopcount&gt; &lt;server info&gt;"
     */
    public final static int RPL_LINKS = 364;
    /**
     * "&lt;mask&gt; :End of LINKS list"
     */
    public final static int RPL_ENDOFLINKS = 365;
    /**
     * "&lt;channel&gt; &lt;banmask&gt;"
     */
    public final static int RPL_BANLIST = 367;
    /**
     * "&lt;channel&gt; :End of channel ban list"
     */
    public final static int RPL_ENDOFBANLIST = 368;
    /**
     * ":&lt;string&gt;"
     */
    public final static int RPL_INFO = 371;
    /**
     * ":End of INFO list"
     */
    public final static int RPL_ENDOFINFO = 374;
    /**
     * ":- &lt;server&gt; Message of the day - "
     */
    public final static int RPL_MOTDSTART = 375;
    /**
     * ":- &lt;text&gt;"
     */
    public final static int RPL_MOTD = 372;
    /**
     * ":End of MOTD command"
     */
    public final static int RPL_ENDOFMOTD = 376;
    /**
     * ":You are now an IRC operator"
     */
    public final static int RPL_YOUREOPER = 381;
    /**
     * "&lt;config file&gt; :Rehashing"
     */
    public final static int RPL_REHASHING = 382;
    /**
     * "You are service &lt;servicename&gt;"
     */
    public final static int RPL_YOURESERVICE = 383;
    /**
     * "&lt;server&gt; :&lt;string showing server's local time&gt;"
     */
    public final static int RPL_TIME = 391;
    /**
     * ":UserID   Terminal  Host"
     */
    public final static int RPL_USERSSTART = 392;
    /**
     * ":&lt;username&gt; &lt;ttyline&gt; &lt;hostname&gt;"
     */
    public final static int RPL_USERS = 393;
    /**
     * ":End of users"
     */
    public final static int RPL_ENDOFUSERS = 394;
    /**
     * ":Nobody logged in"
     */
    public final static int RPL_NOUSERS = 395;
    /**
     * "Link &lt;version & debug level&gt; &lt;destination&gt; &lt;next server&gt; V&lt;protocol version&gt; &lt;link uptime in seconds&gt; &lt;backstream sendq&gt; &lt;upstream sendq&gt;"
     */
    public final static int RPL_TRACELINK = 200;
    /**
     * "Try. &lt;class&gt; &lt;server&gt;"
     */
    public final static int RPL_TRACECONNECTING = 201;
    /**
     * "H.S. &lt;class&gt; &lt;server&gt;"
     */
    public final static int RPL_TRACEHANDSHAKE = 202;
    /**
     * "???? &lt;class&gt; [&lt;client IP address in dot form&gt;]"
     */
    public final static int RPL_TRACEUNKNOWN = 203;
    /**
     * "Oper &lt;class&gt; &lt;nick&gt;"
     */
    public final static int RPL_TRACEOPERATOR = 204;
    /**
     * "User &lt;class&gt; &lt;nick&gt;"
     */
    public final static int RPL_TRACEUSER = 205;
    /**
     * "Serv &lt;class&gt; &lt;int&gt;S &lt;int&gt;C &lt;server&gt; &lt;nick!user|*!*&gt;@&lt;host|server&gt; V&lt;protocol version&gt;"
     */
    public final static int RPL_TRACESERVER = 206;
    /**
     * "Service &lt;class&gt; &lt;name&gt; &lt;type&gt; &lt;active type&gt;"
     */
    public final static int RPL_TRACESERVICE = 207;
    /**
     * "&lt;newtype&gt; 0 &lt;client name&gt;"
     */
    public final static int RPL_TRACENEWTYPE = 208;
    /**
     * "Class &lt;class&gt; &lt;count&gt;"
     */
    public final static int RPL_TRACECLASS = 209;
    /**
     * Unused
     */
    public final static int RPL_TRACERECONNECT = 210;
    /**
     * "File &lt;logfile&gt; &lt;debug level&gt;"
     */
    public final static int RPL_TRACELOG = 261;
    /**
     * "&lt;server name&gt; &lt;version & debug level&gt; :End of TRACE"
     */
    public final static int RPL_TRACEEND = 262;
    /**
     * "&lt;linkname&gt; &lt;sendq&gt; &lt;sent messages&gt; &lt;sent Kbytes&gt; &lt;received messages&gt; &lt;received Kbytes&gt; &lt;time open&gt;"
     */
    public final static int RPL_STATSLINKINFO = 211;
    /**
     * "&lt;command&gt; &lt;count&gt; &lt;byte count&gt; &lt;remote count&gt;"
     */
    public final static int RPL_STATSCOMMANDS = 212;
    /**
     * "&lt;stats letter&gt; :End of STATS report"
     */
    public final static int RPL_ENDOFSTATS = 219;
    /**
     * ":Server Up %d days %d:%02d:%02d"
     */
    public final static int RPL_STATSUPTIME = 242;
    /**
     * "O &lt;hostmask&gt; * &lt;name&gt;"
     */
    public final static int RPL_STATSOLINE = 243;
    /**
     * "&lt;user mode string&gt;"
     */
    public final static int RPL_UMODEIS = 221;
    /**
     * "&lt;name&gt; &lt;server&gt; &lt;mask&gt; &lt;type&gt; &lt;hopcount&gt; &lt;info&gt;"
     */
    public final static int RPL_SERVLIST = 234;
    /**
     * "&lt;mask&gt; &lt;type&gt; :End of service listing"
     */
    public final static int RPL_SERVLISTEND = 235;
    /**
     * ":There are &lt;integer&gt; users and &lt;integer&gt; services on &lt;integer&gt; servers"
     */
    public final static int RPL_LUSERCLIENT = 251;
    /**
     * "&lt;integer&gt; :operator(s) online"
     */
    public final static int RPL_LUSEROP = 252;
    /**
     * "&lt;integer&gt; :unknown connection(s)"
     */
    public final static int RPL_LUSERUNKNOWN = 253;
    /**
     * "&lt;integer&gt; :channels formed"
     */
    public final static int RPL_LUSERCHANNELS = 254;
    /**
     * ":I have &lt;integer&gt; clients and &lt;integer&gt; servers"
     */
    public final static int RPL_LUSERME = 255;
    /**
     * "&lt;server&gt; :Administrative info"
     */
    public final static int RPL_ADMINME = 256;
    /**
     * ":&lt;admin info&gt;"
     */
    public final static int RPL_ADMINLOC1 = 257;
    /**
     * ":&lt;admin info&gt;"
     */
    public final static int RPL_ADMINLOC2 = 258;
    /**
     * ":&lt;admin info&gt;"
     */
    public final static int RPL_ADMINEMAIL = 259;
    /**
     * "&lt;command&gt; :Please wait a while and try again."
     */
    public final static int RPL_TRYAGAIN = 263;

    // --------------------------------
    // Reserved numerics
    /**
     *
     */
    public final static int RPL_SERVICEINFO = 231;
    /**
     *
     */
    public final static int RPL_ENDOFSERVICES = 232;
    /**
     *
     */
    public final static int RPL_SERVICE = 233;
    /**
     *
     */
    public final static int RPL_NONE = 300;
    /**
     *
     */
    public final static int RPL_WHOISCHANOP = 316;
    /**
     *
     */
    public final static int RPL_KILLDONE = 361;
    /**
     *
     */
    public final static int RPL_CLOSING = 362;
    /**
     *
     */
    public final static int RPL_CLOSEEND = 363;
    /**
     *
     */
    public final static int RPL_INFOSTART = 373;
    /**
     *
     */
    public final static int RPL_MYPORTIS = 384;

    /**
     *
     */
    public final static int RPL_STATSCLINE = 213;
    /**
     *
     */
    public final static int RPL_STATSNLINE = 214;
    /**
     *
     */
    public final static int RPL_STATSILINE = 215;
    /**
     *
     */
    public final static int RPL_STATSKLINE = 216;
    /**
     *
     */
    public final static int RPL_STATSQLINE = 217;
    /**
     *
     */
    public final static int RPL_STATSYLINE = 218;
    /**
     *
     */
    public final static int RPL_STATSVLINE = 240;
    /**
     *
     */
    public final static int RPL_STATSLLINE = 241;
    /**
     *
     */
    public final static int RPL_STATSHLINE = 244;
    /**
     *
     */
    public final static int RPL_STATSSLINE = 244;
    /**
     *
     */
    public final static int RPL_STATSPING = 246;
    /**
     *
     */
    public final static int RPL_STATSBLINE = 247;
    /**
     *
     */
    public final static int RPL_STATSDLINE = 250;

    /**
     *
     */
    public final static int ERR_NOSERVICEHOST = 492;

    // ------------------------------------------------------------------------

    private IRCNumericServerReply()
    {
    }
}
