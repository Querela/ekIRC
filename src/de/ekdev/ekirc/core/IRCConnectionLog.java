/**
 * IRCConnectionLog.java
 */
package de.ekdev.ekirc.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ekDev
 */
public class IRCConnectionLog
{
    public final static String PREFIX_IN = ">>> ";
    public final static String PREFIX_OUT = "<<< ";
    public final static String PREFIX_EXCEPTION = "### ";
    public final static String PREFIX_MESSAGE = "--- ";
    public final static String LS = System.lineSeparator();
    public final static String SEP = new String(new char[80]).replace("\0", "-");

    public final static String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss.S zzz";
    public final static String DATE_FORMAT_2 = "dow mon dd hh:mm:ss zzz yyyy";
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(IRCConnectionLog.DATE_FORMAT_1);

    private File file;
    private PrintWriter writer;
    private boolean canWrite;
    protected Date startDate;

    public IRCConnectionLog(String filename, boolean append) throws FileNotFoundException, IllegalArgumentException
    {
        // create a file output log
        if (filename == null)
        {
            throw new IllegalArgumentException("Argument filename is null!");
        }

        file = new File(filename);

        if (file.exists() && !file.isFile())
        {
            throw new IllegalArgumentException("Argument filename specifies no file!");
        }

        boolean hadContent = file.exists();

        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append),
                Charset.defaultCharset())));
        this.canWrite = true;

        // append a empty line if probably hadContent
        if (append && hadContent) this.line("");
    }

    public IRCConnectionLog() throws IOException
    {
        // create log with temp file
        this.file = File.createTempFile("IRCConnectionLog", ".temp.log");
        // this.file.deleteOnExit();

        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false),
                Charset.defaultCharset())));
        this.canWrite = true;
    }

    public IRCConnectionLog(OutputStream os)
    {
        // Create a log with given output stream
        if (os == null)
        {
            throw new IllegalArgumentException("Argument os is null!");
        }

        this.file = null;
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
        this.canWrite = true;
    }

    // ------------------------------------------------------------------------

    public void close()
    {
        if (this.writer != null)
        {
            this.writer.flush();
            this.writer.close();
        }

        this.canWrite = false;
    }

    public boolean canWrite()
    {
        this.canWrite = (this.writer != null && this.writer.checkError());

        return this.canWrite;
    }

    public File getFile()
    {
        return this.file;
    }

    public final boolean canMoveLogFile()
    {
        return (this.file != null);
    }

    public synchronized boolean moveLogFile(String newFilename) throws FileAlreadyExistsException,
            UnsupportedOperationException
    {
        if (this.canMoveLogFile())
        {
            throw new UnsupportedOperationException(
                    "moveLogFile(String) can only be called if a valid File object exists - not for streams!");
        }

        final File newfile = new File(newFilename);
        if (newfile.isFile())
        {
            throw new FileAlreadyExistsException(newFilename, null, "Can't rename file if already existing!");
        }

        long start = System.currentTimeMillis();

        this.close();

        String old = this.file.getAbsolutePath();
        String newf = newfile.getAbsolutePath();

        boolean ret = this.file.renameTo(newfile);
        if (ret) this.file = newfile; // replace only if successful

        try
        {
            this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file,
                    true), Charset.defaultCharset())));
            this.canWrite = true;

            if (ret)
            {
                long diff = System.currentTimeMillis() - start;

                this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
                this.line(IRCConnectionLog.PREFIX_MESSAGE + "Moved Log from: '" + old + "'");
                this.line(IRCConnectionLog.PREFIX_MESSAGE + "            to: '" + newf + "'");
                this.line(IRCConnectionLog.PREFIX_MESSAGE + "          time: " + (diff / 1000.0) + " sec");
                this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
            }
            else
            {
                this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
                this.line(IRCConnectionLog.PREFIX_MESSAGE + "! Moving log file to '" + newf + "' failed!");
                this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            this.canWrite = false;
            return false;
        }

        return ret;
    }

    // ------------------------------------------------------------------------

    public void header(String name, String note)
    {
        if (this.startDate != null) return;
        this.startDate = new Date();

        String msg1 = (name == null || name.length() == 0) ? "Start Log: " : "Start Log for " + name + ": ";

        this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
        this.line(IRCConnectionLog.PREFIX_MESSAGE + msg1 + IRCConnectionLog.simpleDateFormat.format(this.startDate));
        if (note != null)
        {
            this.line(IRCConnectionLog.PREFIX_MESSAGE);
            this.message(note);
        }
        this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
    }

    // ------------------------------------------------------------------------

    public void in(String line)
    {
        this.line(IRCConnectionLog.PREFIX_IN + line);
    }

    public void out(String line)
    {
        this.line(IRCConnectionLog.PREFIX_OUT + line);
    }

    public void message(String msg)
    {
        if (msg == null) return;

        StringReader sr = new StringReader(msg);
        BufferedReader br = new BufferedReader(sr);

        String line;
        try
        {
            while ((line = br.readLine()) != null)
                this.line(IRCConnectionLog.PREFIX_MESSAGE + line);
        }
        catch (IOException ioe)
        {
        }

        try
        {
            br.close();
        }
        catch (IOException e)
        {
        }
    }

    public void exception(Exception ex)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.flush();
        pw.close();

        StringReader sr = new StringReader(sw.toString());
        BufferedReader br = new BufferedReader(sr);

        String line;
        try
        {
            while ((line = br.readLine()) != null)
                this.line(IRCConnectionLog.PREFIX_EXCEPTION + line);
        }
        catch (IOException ioe)
        {
        }

        try
        {
            br.close();
        }
        catch (IOException e)
        {
        }
    }

    public void object(String varName, Object object)
    {
        if (varName == null) return; // ignore because we want a variable name!

        if (object == null)
        {
            this.message(varName + " = null");
        }
        else
        {
            this.message(varName + " = " + object.toString());
        }
    }

    // ------------------------------------------------------------------------

    protected void lines(List<String> lines)
    {
        if (lines == null || lines.isEmpty()) return;

        this.lines(lines.toArray(new String[lines.size()]));
    }

    protected synchronized void lines(String[] lines)
    {
        if (lines == null || lines.length == 0) return;

        for (String line : lines)
            this.line(line);
    }

    protected synchronized void line(String line)
    {
        if (this.canWrite) this.writer.println(line);
        System.out.println(line);
    }
}
