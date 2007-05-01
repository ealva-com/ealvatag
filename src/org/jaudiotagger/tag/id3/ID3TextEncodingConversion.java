package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.AbstractTagFrame;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.logging.LogFormatter;

import java.util.logging.Logger;

/**
 */
public class ID3TextEncodingConversion
{
     protected static Logger logger = LogFormatter.getLogger();
    /**
     * Check the text encoding is valid for this header type and is appropriate for
     * user text encoding options.                                             *
     *
     * This is called before writing any frames that use text encoding
     *
     * @param header used to identify the ID3tagtype
     * @param textEncoding currently set
     * @return valid encoding according to version type and user options
     */
    public static byte getTextEncoding(AbstractTagFrame header,byte textEncoding)
    {
        //Should not happen, assume v23 and provide a warning
        if(header==null)
        {
            logger.warning("Header has not yet been set for this framebody");
            return convertV24textEncodingToV23textEncoding(textEncoding);
        }
        else if(header instanceof ID3v24Frame)
        {
            //All text encodings supported nothing to do
            return textEncoding;
        }
        else
        {
             //If text encoding is an unsupported v24 one we use unicode v23 equivalent
             return convertV24textEncodingToV23textEncoding(textEncoding);

        }
    }

    /** Sets the text encoding to best Unicode type for the version
     *
     * @param header
     * @return
     */
    public static byte getUnicodeTextEncoding(AbstractTagFrame header)
    {
        if(header==null)
        {
            logger.warning("Header has not yet been set for this framebody");
            return TextEncoding.UTF_16;
        }
        else if(header instanceof ID3v24Frame)
        {
            return TextEncoding.UTF_16;
        }
        else
        {
             return TextEncoding.UTF_16;
        }
    }
    /**
     * Convert v24 text encoding to a valid v23 encoding
     *
     * @param textEncoding
     * @return valid encoding
     */
    private static byte convertV24textEncodingToV23textEncoding(byte textEncoding)
    {
         if(
                 (textEncoding==TextEncoding.UTF_16BE)||
                 (textEncoding==TextEncoding.UTF_8)
                )
             {
                 return TextEncoding.UTF_16;
             }
        else
         {
             return textEncoding;
         }
    }


}
