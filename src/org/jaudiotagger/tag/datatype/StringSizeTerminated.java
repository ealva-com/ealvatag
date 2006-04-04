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
 * Contains a string which is NOT null terminated.
 * Warning this datatype type can only be used as the last datatype in a frame because
 * it reads the remainder of the frame as there is no null terminated or provision
 * for setting a defined size.
 *
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.*;

/**
 * Represensts a String of a defined size
 */
public class StringSizeTerminated
    extends AbstractString
{

    /**
     * Creates a new ObjectStringSizeTerminated datatype.
     *
     * @param identifier DOCUMENT ME!
     */
    public StringSizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public StringSizeTerminated(StringSizeTerminated object)
    {
        super(object);
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof StringSizeTerminated == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Read a 'n' bytes from buffer into a string where n is the framesize - offset
     * so thefore cannot use this if there are other objects after it because it has no
     * delimiter.
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
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
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
     * Write String into Buffer.
     * We need to take into account whether there are characters that require
     * the encoding to be done using one of the UTF-16 variants.
     *
     * @return DOCUMENT ME!
     */
    public byte[] writeByteArray()
    {
        byte[] data = null;
        //Try and write to buffer using the CharSet defined by the textEncoding field.
        try
        {
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
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
}
