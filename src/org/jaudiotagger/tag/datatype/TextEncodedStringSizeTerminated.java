package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Represents a String which is not delimited by null character.
 * <p/>
 * This type of String will usually only be used when it is the last field within a frame, when reading the remainder of
 * the byte array will be read, when writing the frame will be accomodate the required size for the String. The String
 * will be encoded based upon the text encoding of the frame that it belongs to.
 */
public class TextEncodedStringSizeTerminated
    extends AbstractString
{

    /**
     * Creates a new ObjectStringSizeTerminated datatype.
     *
     * @param identifier identifies the frame type
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
     * <p/>
     * Must take into account the text encoding defined in the Encoding Object
     * ID3 Text Frames often allow multiple strings seperated by the null char
     * appropriate for the encoding.
     *
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        logger.finest("Reading from array from offset:" + offset);

        //Get the Specified Decoder
        String charSetName = getTextEncodingCharSet();
        CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();

        //Decode sliced inBuffer
        ByteBuffer inBuffer = ByteBuffer.wrap(arr, offset, arr.length - offset).slice();
        CharBuffer outBuffer = CharBuffer.allocate(arr.length - offset);
        decoder.reset();
        CoderResult coderResult = decoder.decode(inBuffer, outBuffer, true);
        if (coderResult.isError())
        {
            logger.warning("Decoding error:" + coderResult.toString());
        }
        decoder.flush(outBuffer);
        outBuffer.flip();
        value = outBuffer.toString();

        //IF SIZE NOT WHAT WAS EXPECTED , CHECK FOR NULL TERMINATORS ?
        //TODO Size
        setSize(arr.length - offset);
        logger.info("Read SizeTerminatedString:" + value + " size:" + size);
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
        //Should never happen so if does throw a RuntimeException
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            throw new RuntimeException(ce);
        }
        setSize(data.length);
        return data;
    }

    protected String getTextEncodingCharSet()
    {
        byte textEncoding = this.getBody().getTextEncoding();
        String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
        logger.finest("text encoding:" + textEncoding + " charset:" + charSetName);
        return charSetName;
    }
}
