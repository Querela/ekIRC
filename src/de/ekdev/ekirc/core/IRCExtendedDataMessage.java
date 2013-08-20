/**
 * IRCExtendedDataMessage.java
 */
package de.ekdev.ekirc.core;

/**
 * @author ekDev
 */
public class IRCExtendedDataMessage
{
    private final String tag;
    private final String extendedData;

    public IRCExtendedDataMessage(String tag, String extendedData)
    {
        // if tag == null or empty then empty message
        // if extendedData == null then empty extendedData
        // if extendedData is empty then tag only message

        this.tag = tag;
        this.extendedData = extendedData;
    }

    // ------------------------------------------------------------------------

    public String getTag()
    {
        return this.tag;
    }

    public String getExtendedData()
    {
        return this.extendedData;
    }

    public boolean isEmpty()
    {
        return (this.tag == null || this.tag.length() == 0);
    }

    public boolean hasExtendedData()
    {
        return (this.extendedData != null && this.extendedData.length() > 0);
    }

    // ------------------------------------------------------------------------

    public static String getReadyForInsertEmpty()
    {
        return IRCMessageProcessor.CTCP_XDELIM + IRCMessageProcessor.CTCP_XDELIM;
    }

    public static String getReadyForInsertTagOnly(String tag, boolean withSpace)
    {
        // tag mustn't contain space or CTCP_XDELIM
        return IRCMessageProcessor.CTCP_XDELIM + tag + ((withSpace) ? " " : "") + IRCMessageProcessor.CTCP_XDELIM;
    }

    public static String getReadyForInsertAll(String tag, String extendedData)
    {
        // tag mustn't contain space or CTCP_XDELIM
        // extendedData mustn't contain CTCP_XDELIM
        return IRCMessageProcessor.CTCP_XDELIM + tag + " " + extendedData + IRCMessageProcessor.CTCP_XDELIM;
    }

    // ------------------------------------------------------------------------

    public String getReadyForInsert()
    {
        if (this.isEmpty()) return IRCExtendedDataMessage.getReadyForInsertEmpty();

        if (this.hasExtendedData()) return IRCExtendedDataMessage.getReadyForInsertAll(this.tag, this.extendedData);

        return IRCExtendedDataMessage.getReadyForInsertTagOnly(this.tag, (this.extendedData != null));
    }

    @Override
    public String toString()
    {
        return "IRCExtendedDataMessage [tag=" + tag + ", extendedData=" + extendedData + "]";
    }
}
