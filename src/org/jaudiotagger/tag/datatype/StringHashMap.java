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

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;
import org.jaudiotagger.tag.id3.valuepair.Languages;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.charset.*;
import java.nio.*;


/**
 * Represents a String thats acts as a key into an enumeration of values. The String will be encoded
 * using the default encoding regardless of what encoding may be specified in the framebody
 */
public class StringHashMap extends StringFixedLength implements HashMapInterface
{

    /**
     * DOCUMENT ME!
     */
    HashMap keyToValue = null;

    /**
     * DOCUMENT ME!
     */
    HashMap valueToKey = null;

    /**
     * DOCUMENT ME!
     */
    boolean hasEmptyValue = false;

    /**
     * Creates a new ObjectStringHashMap datatype.
     *
     * @param identifier DOCUMENT ME!
     * @param size       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public StringHashMap(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody, size);

        if (identifier.equals(DataTypes.OBJ_LANGUAGE))
        {
            valueToKey = Languages.getInstanceOf().getValueToIdMap();
            keyToValue = Languages.getInstanceOf().getIdToValueMap();
        }
        else
        {
            throw new IllegalArgumentException("Hashmap identifier not defined in this class: " + identifier);
        }
    }

    public StringHashMap(StringHashMap copyObject)
    {
        super(copyObject);

        this.hasEmptyValue = copyObject.hasEmptyValue;
        this.keyToValue = copyObject.keyToValue;
        this.valueToKey = copyObject.valueToKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HashMap getKeyToValue()
    {
        return keyToValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HashMap getValueToKey()
    {
        return valueToKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(Object value)
    {
        if (value instanceof String)
        {
            this.value = ((String) value).toLowerCase();
        }
        else
        {
            this.value = value;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof StringHashMap) == false)
        {
            return false;
        }

        StringHashMap object = (StringHashMap) obj;

        if (this.hasEmptyValue != object.hasEmptyValue)
        {
            return false;
        }

        if (this.keyToValue == null)
        {
            if (object.keyToValue != null)
            {
                return false;
            }
        }
        else
        {
            if (this.keyToValue.equals(object.keyToValue) == false)
            {
                return false;
            }
        }

        if (this.keyToValue == null)
        {
            if (object.keyToValue != null)
            {
                return false;
            }
        }
        else
        {
            if (this.valueToKey.equals(object.valueToKey) == false)
            {
                return false;
            }
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator()
    {
        if (keyToValue == null)
        {
            return null;
        }
        else
        {
            // put them in a treeset first to sort them
            TreeSet treeSet = new TreeSet(keyToValue.values());

            if (hasEmptyValue)
            {
                treeSet.add("");
            }

            return treeSet.iterator();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        if (value == null)
        {
            return "";
        }
        else if (keyToValue.get(value) == null)
        {
            return "";
        }
        else
        {
            return keyToValue.get(value).toString();
        }
    }

    /**
     * Read a string from buffer of fixed size, ignoring the frames charset encoding
     * The hashMap types alwys use the default encoding. For example COMM encodes it
     * texts and description but not the language.
     *
     * @param offset DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        try
        {
            String charSetName = TextEncoding.getInstanceOf().getValueForId(TextEncoding.ISO_8859_1);
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
        catch (IndexOutOfBoundsException e)
        {
            logger.warning(e.getMessage());
            value = "";
        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
            value = "";
        }
    }

    /**
     * Write string from buffer of fixed size, ignoring the frames charset encoding
     * The hashmap types always use the default encoding. For example COMM encodes it
     * texts and description but not the language.
     *
     * @return DOCUMENT ME!
     */
    public byte[] writeByteArray()
    {
        ByteBuffer dataBuffer = null;
        //Write to buffer using the standard encoding
        try
        {
            String charSetName = TextEncoding.getInstanceOf().getValueForId(TextEncoding.ISO_8859_1);
            CharsetEncoder encoder = Charset.forName(charSetName).newEncoder();
            dataBuffer = encoder.encode(CharBuffer.wrap((String) value));
        }
        catch (CharacterCodingException ce)
        {
            logger.severe(ce.getMessage());
        }


        /* We must return the defined size.
         * To check now because size is in bytes not chars
         */
        if (dataBuffer != null)
        {
            if (dataBuffer.capacity() == size)
            {
                return dataBuffer.array();
            }
            else if (dataBuffer.capacity() > size)
            {
                byte[] data = new byte[size];
                dataBuffer.get(data, 0, size);
                return data;
            }
            else
            {
                byte[] data = new byte[size];
                dataBuffer.get(data, 0, dataBuffer.capacity());
                return data;
            }
        }
        else
        {
            byte[] data = new byte[size];
            setSize(data.length);
            return data;
        }
    }
}
