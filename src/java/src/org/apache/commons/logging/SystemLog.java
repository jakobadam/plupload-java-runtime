package org.apache.commons.logging;

import org.apache.commons.logging.Log;


public class SystemLog implements Log {


    /** "Trace" level logging. */
    public static final int LOG_LEVEL_TRACE  = 1;
    /** "Debug" level logging. */
    public static final int LOG_LEVEL_DEBUG  = 2;
    /** "Info" level logging. */
    public static final int LOG_LEVEL_INFO   = 3;
    /** "Warn" level logging. */
    public static final int LOG_LEVEL_WARN   = 4;
    /** "Error" level logging. */
    public static final int LOG_LEVEL_ERROR  = 5;
    /** "Fatal" level logging. */
    public static final int LOG_LEVEL_FATAL  = 6;

    /** Enable all logging levels */
    public static final int LOG_LEVEL_ALL    = (LOG_LEVEL_TRACE - 1);

    /** Enable no logging levels */
    public static final int LOG_LEVEL_OFF    = (LOG_LEVEL_FATAL + 1);

    // ------------------------------------------------------------- Attributes

    
    /** The current log level */
    public static int currentLogLevel;


    // ------------------------------------------------------------ Constructor

    /**
     * Construct a simple log with given name.
     *
     * @param name log name
     */
    public SystemLog() {

    }


    // -------------------------------------------------------- Properties


    /**
     * <p> Get logging level. </p>
     */
    public int getLevel() {

        return currentLogLevel;
    }


    // -------------------------------------------------------- Logging Methods


    /**
     * <p> Do the actual logging.
     * This method assembles the message
     * and then calls <code>write()</code> to cause it to be written.</p>
     *
     * @param type One of the LOG_LEVEL_XXX constants defining the log level
     * @param message The message itself (typically a String)
     * @param t The exception whose stack trace should be logged
     */
    protected void log(int type, Object message, Throwable t) {
        // Use a string buffer for better performance
        StringBuffer buf = new StringBuffer();

        // Append a readable representation of the log level
        switch(type) {
            case SystemLog.LOG_LEVEL_TRACE: buf.append("[TRACE] "); break;
            case SystemLog.LOG_LEVEL_DEBUG: buf.append("[DEBUG] "); break;
            case SystemLog.LOG_LEVEL_INFO:  buf.append("[INFO] ");  break;
            case SystemLog.LOG_LEVEL_WARN:  buf.append("[WARN] ");  break;
            case SystemLog.LOG_LEVEL_ERROR: buf.append("[ERROR] "); break;
            case SystemLog.LOG_LEVEL_FATAL: buf.append("[FATAL] "); break;
        }

        
        buf.append(" - ");

        // Append the message
        buf.append(String.valueOf(message));

        // Append stack trace if not null
        if(t != null) {
            buf.append(" <");
            buf.append(t.toString());
            buf.append(">");

            java.io.StringWriter sw= new java.io.StringWriter(1024);
            java.io.PrintWriter pw= new java.io.PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            buf.append(sw.toString());
        }

        // Print to the appropriate destination
        write(buf);

    }


    /**
     * <p>Write the content of the message accumulated in the specified
     * <code>StringBuffer</code> to the appropriate output destination.  The
     * default implementation writes to <code>System.err</code>.</p>
     *
     * @param buffer A <code>StringBuffer</code> containing the accumulated
     *  text to be logged
     */
    protected void write(StringBuffer buffer) {

        System.err.println(buffer.toString());

    }


    /**
     * Is the given log level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    protected boolean isLevelEnabled(int logLevel) {
        // log level are numerically ordered so can use simple numeric
        // comparison
        return (logLevel >= currentLogLevel);
    }


    // -------------------------------------------------------- Log Implementation


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_DEBUG</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public final void debug(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_DEBUG)) {
            log(SystemLog.LOG_LEVEL_DEBUG, message, null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_DEBUG</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#debug(Object, Throwable)
     */
    public final void debug(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_DEBUG)) {
            log(SystemLog.LOG_LEVEL_DEBUG, message, t);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_TRACE</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#trace(Object)
     */
    public final void trace(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_TRACE)) {
            log(SystemLog.LOG_LEVEL_TRACE, message, null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_TRACE</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#trace(Object, Throwable)
     */
    public final void trace(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_TRACE)) {
            log(SystemLog.LOG_LEVEL_TRACE, message, t);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_INFO</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public final void info(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_INFO)) {
            log(SystemLog.LOG_LEVEL_INFO,message,null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_INFO</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#info(Object, Throwable)
     */
    public final void info(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_INFO)) {
            log(SystemLog.LOG_LEVEL_INFO, message, t);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_WARN</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public final void warn(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_WARN)) {
            log(SystemLog.LOG_LEVEL_WARN, message, null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_WARN</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#warn(Object, Throwable)
     */
    public final void warn(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_WARN)) {
            log(SystemLog.LOG_LEVEL_WARN, message, t);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_ERROR</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#error(Object)
     */
    public final void error(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_ERROR)) {
            log(SystemLog.LOG_LEVEL_ERROR, message, null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_ERROR</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public final void error(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_ERROR)) {
            log(SystemLog.LOG_LEVEL_ERROR, message, t);
        }
    }


    /**
     * Log a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_FATAL</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#fatal(Object)
     */
    public final void fatal(Object message) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_FATAL)) {
            log(SystemLog.LOG_LEVEL_FATAL, message, null);
        }
    }


    /**
     * Logs a message with 
     * <code>org.apache.commons.logging.impl.SystemLog.LOG_LEVEL_FATAL</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#fatal(Object, Throwable)
     */
    public final void fatal(Object message, Throwable t) {

        if (isLevelEnabled(SystemLog.LOG_LEVEL_FATAL)) {
            log(SystemLog.LOG_LEVEL_FATAL, message, t);
        }
    }


    /**
     * <p> Are debug messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isDebugEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_DEBUG);
    }


    /**
     * <p> Are error messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isErrorEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_ERROR);
    }


    /**
     * <p> Are fatal messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isFatalEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_FATAL);
    }


    /**
     * <p> Are info messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isInfoEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_INFO);
    }


    /**
     * <p> Are trace messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isTraceEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_TRACE);
    }


    /**
     * <p> Are warn messages currently enabled? </p>
     *
     * <p> This allows expensive operations such as <code>String</code>
     * concatenation to be avoided when the message will be ignored by the
     * logger. </p>
     */
    public final boolean isWarnEnabled() {

        return isLevelEnabled(SystemLog.LOG_LEVEL_WARN);
    }
}
