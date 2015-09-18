package org.jaudiotagger.audio.aiff;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AiffUtil {

    private final static SimpleDateFormat dateFmt =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    private final static Charset LATIN1 = StandardCharsets.ISO_8859_1;

    public static double read80BitDouble (ByteBuffer chunkData)
                throws IOException
    {
        byte[] buf = new byte[10];
        chunkData.get(buf);
        ExtDouble xd = new ExtDouble (buf);
        return xd.toDouble();
    }

    /** Converts a Macintosh-style timestamp (seconds since
     *  January 1, 1904) into a Java date.  The timestamp is
     *  treated as a time in the default localization.
     *  Depending on that localization,
     *  there may be some variation in the exact hour of the date 
     *  returned, e.g., due to daylight savings time.
     * 
     */
    public static Date timestampToDate (long timestamp)
    {
        Calendar cal = Calendar.getInstance ();
        cal.set (1904, 0, 1, 0, 0, 0);
        
        // If we add the seconds directly, we'll truncate the long
        // value when converting to int.  So convert to hours plus
        // residual seconds.
        int hours = (int) (timestamp / 3600);
        int seconds = (int) (timestamp - (long) hours * 3600L);
        cal.add (Calendar.HOUR_OF_DAY, hours);
        cal.add (Calendar.SECOND, seconds);
        Date dat = cal.getTime ();
        return dat;
    }
    
    /** Format a date as text */
    public static String formatDate (Date dat) {
        return dateFmt.format(dat);
    }
    
    /**
     *  Convert a byte array to a Pascal string. The first byte is the byte count,
     *  followed by that many active characters.
     */
    public static String bytesToPascalString (byte[] data) {
        int len = (int) data[0];
        return new String(data, 1, len, LATIN1);
    }

    public static String readPascalString(ByteBuffer bb) throws IOException {
        int len = bb.get();
        byte[] buf = new byte[len + 1];
        bb.get(buf, 1, len);
        buf[0] = (byte) len;
        return bytesToPascalString(buf);
    }
}
