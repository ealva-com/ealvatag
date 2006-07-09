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

import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;

import java.nio.charset.*;
import java.nio.*;


/** Represents a fixed length String, whereby the length of the String is known
 *
 */
public class StringFixedLength
    extends AbstractString
{
    /**
     * Creates a new ObjectStringFixedsize datatype.
     *
     * @param identifier DOCUMENT ME!
     * @param size       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public StringFixedLength(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody);
        if (size < 0)
        {
            throw new IllegalArgumentException("size is less than zero: " + size);
        }
        setSize(size);
    }

    public StringFixedLength(StringFixedLength copyObject)
    {
        super(copyObject);
        this.size = copyObject.size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof StringFixedLength) == false)
        {
            return false;
        }
        StringFixedLength object = (StringFixedLength) obj;
        if (this.size != object.size)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Read a string from buffer of fixed size(size has already been set in constructor)
     *
     * @param offset DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        try
        {
            //Get the Specified Decoder
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
            CharsetDecoder decoder = Charset.forName(charSetName).newDecoder();
            //Decode buffer if runs into problems should through exception which we
            //catch and then set value to empty string.
            logger.finest("Array length is:" + arr.length + "offset is:" + offset + "Size is:" + size);
            String str = decoder.decode(ByteBuffer.wrap(arr, offset, size)).toString();
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
        ByteBuffer dataBuffer = null;
        byte[] data = null;
        //Try and write to buffer using the CharSet defined by the textEncoding field.
        try
        {
            byte textEncoding = this.getFrameBody().getTextEncoding();
            String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
            CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
            dataBuffer = encoder.encode(CharBuffer.wrap((String) value));

        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            value = "";
        }
        /* We must return the defined size.
         * To check now because size is in bytes not chars
         */
        if (dataBuffer != null)
        {
            if (dataBuffer.limit() == size)
            {
                data = new byte[dataBuffer.limit()];
                dataBuffer.get(data, 0, dataBuffer.limit());
                return data;
            }
            else if (dataBuffer.limit() > size)
            {
                data = new byte[size];
                dataBuffer.get(data, 0, size);
                return data;
            }
            else
            {
                data = new byte[size];
                dataBuffer.get(data, 0, dataBuffer.limit());
                return data;
            }
        }
        data = new byte[size];
        setSize(data.length);
        return data;
    }
}
