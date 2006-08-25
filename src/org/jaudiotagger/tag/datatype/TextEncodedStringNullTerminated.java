package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Represents a String whose size is determined by finding of a null character at the end of the String. 
 * 
 * The String will be encoded based upon the text encoding of the frame that it belongs to.
 */
public class TextEncodedStringNullTerminated
    extends AbstractString
{
    /**
     * Creates a new TextEncodedStringNullTerminated datatype.
     *
     * @param identifier identifies the frame type
     */
    public TextEncodedStringNullTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    /**
     * Creates a new TextEncodedStringNullTerminated datatype, with value
     *
     * @param identifier
     * @param frameBody
     * @param value
     */
    public TextEncodedStringNullTerminated(String identifier, AbstractTagFrameBody frameBody,String value)
    {
        super(identifier, frameBody,value);
    }

    public TextEncodedStringNullTerminated(TextEncodedStringNullTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof TextEncodedStringNullTerminated == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Read a string from buffer upto null character (if exists)
     *
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings seperated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        try
        {
            logger.finer("Reading from array from offset:" + offset);
            int size = 0;
            //Get the Specified Decoder
            String charSetName = getTextEncodingCharSet();
            CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();

            /* We only want to load up to null terminator, data after this is part of different
             * fields and it may not be possible to decode it so do the check before we do
             * do the decoding,encoding dependent. */
            ByteBuffer buffer = ByteBuffer.wrap(arr, offset, arr.length - offset);
            int endPosition = 0;

            /* Latin-1 and UTF-8 strings are terminated by a single-byte null,
             * while UTF-16 and its variants need two bytes for the null terminator.
             */
            final boolean nullIsOneByte
            = (charSetName.equals(TextEncoding.CHARSET_ISO_8859_1)|| charSetName.equals(TextEncoding.CHARSET_UTF_8));

            boolean isNullTerminatorFound=false;
            while (buffer.hasRemaining())
            {

                byte nextByte = buffer.get();

                if (nextByte == 0x00)
                {
                    if (nullIsOneByte)
                    {
                        logger.finest("Null terminator found at:"+buffer.position());
                        buffer.mark();
                        buffer.reset();
                        endPosition = buffer.position() - 1;
                        isNullTerminatorFound=true;
                        break;
                    }
                    else
                    {
                        // Looking for two-byte null
                        nextByte = buffer.get();
                        if (nextByte == 0x00)
                        {
                            logger.finest("UTf16:Null terminator found at:"+buffer.position());
                            buffer.mark();
                            buffer.reset();
                            endPosition = buffer.position() - 2;
                            isNullTerminatorFound=true;
                            break;
                        }
                    }
                }
            }

            if(isNullTerminatorFound==false)
            {
                 throw new InvalidDataTypeException("Unable to find null terminated string");
            }

            //Set Size so offset is ready for next field (includes the null terminator)
            logger.finest("End Position is:" + endPosition + "Offset:" + offset);
            size = endPosition - offset;
            size++;

            if (!nullIsOneByte)
            {
                size++;
            }
            setSize(size);
            /* Decode buffer if runs into problems should throw exception which we
             * catch and then set value to empty string. (We dont read the null terminator
             * because we dont want to display this */
            int bufferSize = endPosition - offset;
            logger.finest("Buffer size is:" + bufferSize);
            if (bufferSize == 0)
            {
                value = "";
            }
            else
            {
                String str = decoder.decode(ByteBuffer.wrap(arr, offset, bufferSize)).toString();
                if (str == null)
                {
                    throw new NullPointerException("String is null");
                }
                value = str;
            }
        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            value = "";
        }
        //Set Size so offset is ready for next field (includes the null terminator)
        logger.info("Read NullTerminatedString:" + value+" size:"+size);
    }

    /**
     * Write String into byte array, adding a null character to the end of the String
     *
     * @return the data as a byte array in format to write to file
     */
    public byte[] writeByteArray()
    {
        logger.info("Writing NullTerminatedString." + value);
        byte[] data = null;
        //Write to buffer using the CharSet defined by getTextEncodingCharSet()
        //Add a null terminator which will be encoded based on encoding.
        try
        {
            String charSetName = getTextEncodingCharSet();
            CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
            ByteBuffer bb = encoder.encode(CharBuffer.wrap((String) value + '\0'));
            data = new byte[bb.limit()];
            bb.get(data, 0, bb.limit());
        }
        //Should never happen so if does throw a RuntimeException
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            throw new RuntimeException(ce);
        }                          
        setSize(data.length);
        return data;
    }

    protected String  getTextEncodingCharSet()
    {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        logger.finest("text encoding:"+textEncoding + " charset:"+charSetName);
        return charSetName;
    }
}
