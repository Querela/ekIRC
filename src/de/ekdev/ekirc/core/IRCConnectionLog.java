/**
 * IRCConnectionLog.java
 */
package de.ekdev.ekirc.core;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public IRCConnectionLog(String filename, boolean append)
            throws FileNotFoundException, IllegalArgumentException, NullPointerException
    {
        // create a file output log
        Objects.requireNonNull(filename, "filename must not be null!");

        this.file = new File(filename);

        boolean fileexists = this.file.exists();

        if (fileexists && !this.file.isFile())
        {
            throw new IllegalArgumentException("Argument filename specifies no file!");
        }

        this.writer = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(this.file, append), Charset.defaultCharset())));
        this.canWrite = true;
        this.createWriterCloseShutdownHandler();

        // append a empty line if file existed and probably had Content
        if (fileexists && append) this.line("");
    }

    public IRCConnectionLog()
            throws IOException
    {
        // create log with temp file
        this.file = File.createTempFile("IRCConnectionLog", ".temp.log");
        // this.file.deleteOnExit();

        this.writer = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(this.file, false), Charset.defaultCharset())));
        this.canWrite = true;
        this.createWriterCloseShutdownHandler();
    }

    public IRCConnectionLog(OutputStream os)
            throws NullPointerException
    {
        // Create a log with given output stream
        Objects.requireNonNull(os, "os must not be null!");

        this.file = null;
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
        this.canWrite = true;
    }

    // ------------------------------------------------------------------------

    protected void createWriterCloseShutdownHandler()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                writer.close();
            }
        }, this.getClass().getCanonicalName() + "-ShutdownHook"));
    }

    public void close()
    {
        if (this.writer != null)
        {
            this.writer.flush();
            this.writer.close();
        }

        this.canWrite = false;
    }

    @Override
    protected void finalize()
            throws Throwable
    {
        this.close();

        super.finalize();
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

    public synchronized boolean moveLogFile(String newFilename)
            throws FileAlreadyExistsException, UnsupportedOperationException
    {
        if (!this.canMoveLogFile())
        {
            this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
            this.line(IRCConnectionLog.PREFIX_MESSAGE + "! Attempt to move log file to '" + newFilename + "' !");
            this.line(IRCConnectionLog.PREFIX_MESSAGE + "! Failed due to having no valid File object !");
            this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);

            throw new UnsupportedOperationException(
                    "moveLogFile(String) can only be called if a valid File object exists - not for streams!");
        }

        final File newfile = new File(newFilename);
        if (newfile.isFile())
        {
            this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);
            this.line(IRCConnectionLog.PREFIX_MESSAGE + "! Attempt to move log file to '" + newFilename + "' !");
            this.line(IRCConnectionLog.PREFIX_MESSAGE + "! Failed because destination file already exists !");
            this.line(IRCConnectionLog.PREFIX_MESSAGE + IRCConnectionLog.SEP);

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
            this.writer = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(this.file, true), Charset.defaultCharset())));
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

    public boolean tryMoveLogFile(String newFilename)
    {
        if (!this.canMoveLogFile()) return false;

        boolean ret = false;
        try
        {
            ret = this.moveLogFile(newFilename);
        }
        catch (Exception e)
        {
            return false;
        }

        return ret;
    }

    public synchronized boolean moveAndAppendLogFile(String newFilename, boolean append)
    {
        // TODO: append old file onto destination file, if no append then remove existing destination or abort?
        return false;
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
        this.message(msg, null);
    }

    public void message(String msg, String optPrefix)
    {
        if (msg == null) return;
        if (optPrefix == null) optPrefix = "";

        StringReader sr = new StringReader(msg);
        BufferedReader br = new BufferedReader(sr);

        String line;
        List<String> lines = new ArrayList<>();
        try
        {
            while ((line = br.readLine()) != null) lines.add(IRCConnectionLog.PREFIX_MESSAGE + optPrefix + line);
        }
        catch (IOException ioe)
        {
        }

        this.lines(lines);

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
        List<String> lines = new ArrayList<>();
        try
        {
            while ((line = br.readLine()) != null) lines.add(IRCConnectionLog.PREFIX_EXCEPTION + line);
        }
        catch (IOException ioe)
        {
        }

        this.lines(lines);

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
