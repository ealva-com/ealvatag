package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Represents a String which is not delimted by null pointer, this type of String will usually
 * only be used when it is the last field within a frame, when reading the remainder of the byte array will
 * be read, when writing the frame will be accomodate the required size for the String. The String
 * will be encoded based upon the text encoding of the frame that it belongs to.
 */
public class TextEncodedStringSizeTerminated
    extends AbstractString
{

    /**
     * Creates a new ObjectStringSizeTerminated datatype.
     *
     * @param identifier  identifies the frame type
     */
    public TextEncodedStringSizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public TextEncodedStringSizeTerminated(TextEncodedStringSizeTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof TextEncodedStringSizeTerminated == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Read a 'n' bytes from buffer into a string where n is the framesize - offset
     * so thefore cannot use this if there are other objects after it because it has no
     * delimiter.
     *
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings seperated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        try
        {
            logger.finest("Reading from array from offset:" + offset);
            //Get the Specified Decoder
            String charSetName = getTextEncodingCharSet();
            CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();
            //Decode buffer if runs into problems should throw exception which we
            //catch and then set value to empty string.
            String str = decoder.decode(ByteBuffer.wrap(arr, offset, arr.length - offset)).toString();
            if (str == null)
            {
                throw new NullPointerException("String is null");
            }
            value = str;
        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            value = "";
        }
        setSize(arr.length - offset);
        logger.finest("read value:" + value);
    }

    /**
     * Write String into byte array
     *
     * @return the dat as a byte array in format to write to file
    */
    public byte[] writeByteArray()
    {
        byte[] data = null;
        //Try and write to buffer using the CharSet defined by getTextEncodingCharSet()
        try
        {
            String charSetName = getTextEncodingCharSet();
            CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
            ByteBuffer bb = encoder.encode(CharBuffer.wrap((String) value));
            data = new byte[bb.limit()];
            bb.get(data, 0, bb.limit());
        }
        //Should never happen
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
        }
        setSize(data.length);
        return data;
    }

    protected String  getTextEncodingCharSet()
    {
         byte textEncoding = this.getFrameBody().getTextEncoding();
         String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
         logger.finest("text encoding:"+textEncoding + " charset:"+charSetName);
        return charSetName;
    }
}
