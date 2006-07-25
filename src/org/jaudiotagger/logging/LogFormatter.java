/**
 *  Initial @author : Paul Taylor
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.jaudiotagger.logging;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.*;

/*
 * For Formatting log output
*/
public class LogFormatter
    extends Formatter
{
    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.
        GetPropertyAction("line.separator"));

    private SimpleDateFormat sfDateOut = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss:");
    Date date = new Date();

    public LogFormatter()
    {
    }

    public String format(LogRecord record)
    {
        StringBuffer sb = new StringBuffer();
        date.setTime(record.getMillis());
        sb.append(sfDateOut.format(date));
        if (record.getSourceClassName() != null)
        {
            sb.append(record.getSourceClassName());
        }
        else
        {
            sb.append(record.getLoggerName());
        }
        if (record.getSourceMethodName() != null)
        {
            sb.append(":");
            sb.append(record.getSourceMethodName());
        }
        String message = formatMessage(record);
        sb.append(": ");
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null)
        {
            try
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
            catch (Exception ex)
            {
            }
        }
        return sb.toString();
    }

    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.mp3");

    public static Logger getLogger()
    {
        return logger;
    }

}
