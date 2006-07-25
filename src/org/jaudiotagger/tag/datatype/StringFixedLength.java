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


/**
 * Represents a fixed length String, whereby the length of the String is known. The String
 * will be encoded based upon the text encoding of the frame that it belongs to.
 */
public class StringFixedLength
    extends AbstractString
{
    /**
     * Creates a new ObjectStringFixedsize datatype.
     *
     * @param identifier 
     * @param size       
     * @throws IllegalArgumentException 
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
     * 
     *
     * @param obj 
     * @return 
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
     * @param arr    this is the buffer for the frame
     * @param offset this is where to start reading in the buffer for this field
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        try
        {
            String charSetName = getTextEncodingCharSet();
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
     * Write String into byte array
     *
     * @return the byte array to be written to the file
     */
    public byte[] writeByteArray()
    {
        ByteBuffer dataBuffer = null;
        byte[] data = null;

        try
        {
            String charSetName = getTextEncodingCharSet();
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

    /**
     *
     * @return the encoding of the frame body this datatype belongs to
     */
    protected String  getTextEncodingCharSet()
    {
         byte textEncoding = this.getFrameBody().getTextEncoding();
         String charSetName = TextEncoding.getInstanceOf().getValueForId(textEncoding);
         logger.finest("text encoding:"+textEncoding + " charset:"+charSetName);
        return charSetName;
    }
}
