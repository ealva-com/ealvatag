/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
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
 * Description:
 *
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.*;

/**
 * Represents a String whose size is determined by finding of a null character
 */
public class StringNullTerminated
    extends AbstractString
{
    /**
     * Creates a new ObjectStringNullTerminated datatype.
     *
     * @param identifier DOCUMENT ME!
     */
    public StringNullTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public StringNullTerminated(StringNullTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof StringNullTerminated == false)
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
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
            logger.finest("text encoding:"+textEncoding + " charset:"+charSetName);
            CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();

            /* We only want to load up to null terminator, data after this is part of different
             * fields and it may not be possible to decode it so do the check before we do
             * do the decoding,encoding dependent. */
            ByteBuffer buffer = ByteBuffer.wrap(arr, offset, arr.length - offset);
            int endPosition = 0;
            boolean isNullTerminatorFound=false;
            while (buffer.hasRemaining())
            {

                byte nextByte = buffer.get();

                if (nextByte == 0x00)
                {
                    if (textEncoding == 0)
                    {
                        logger.finest("null terminator found at:"+buffer.position());
                        buffer.mark();
                        buffer.reset();
                        endPosition = buffer.position() - 1;
                        isNullTerminatorFound=true;
                        break;
                    }
                    //UTF16
                    else
                    {
                        nextByte = buffer.get();
                        if (nextByte == 0x00)
                        {
                            logger.finest("UTf16:null terminator found at:"+buffer.position());
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
                 throw new InvalidDataTypeException("Unable to find null termiated string");
            }

            //Set Size so offset is ready for next field (includes the null terminator)
            logger.finest("End Position is:" + endPosition + "Offset:" + offset);
            size = endPosition - offset;
            size++;
            //UTF-16 needs two bytes for null terminator
            if (textEncoding != 0)
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
        logger.info("Null Terminator size is:" + size);
        logger.info("Read NullTerminatedString:" + value);
    }

    /**
     * Write String into Buffer.
     * We need to take into account whether there are characters that require
     * the encoding to be done using one of the UTF-16 variants. And null terminate
     * the String.
     *
     * @return the dat as a byte array in format to write to file
     */
    public byte[] writeByteArray()
    {
        logger.info("Writing NullTerminatedString." + value);
        byte[] data = null;
        //Write to buffer using the CharSet defined by the textEncoding field.
        //Add a null terminator which will be encoded based on encoding.
        try
        {
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
            CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
            ByteBuffer bb = encoder.encode(CharBuffer.wrap((String) value + '\0'));
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
}
