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
import org.jaudiotagger.tag.id3.valuepair.*;
import org.jaudiotagger.tag.id3.ID3Frames;

/**
 * Represents a number thats acts as a key into an enumeration of values
 */
public class NumberHashMap extends NumberFixedLength implements HashMapInterface
{

    /**
     * DOCUMENT ME!
     */
    private HashMap keyToValue = null;

    /**
     * DOCUMENT ME!
     */
    private HashMap valueToKey = null;

    /**
     * DOCUMENT ME!
     */
    private boolean hasEmptyValue = false;

    /**
     * Creates a new ObjectNumberHashMap datatype.
     *
     * @param identifier DOCUMENT ME!
     * @param size       DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public NumberHashMap(String identifier, AbstractTagFrameBody frameBody, int size)
    {
        super(identifier, frameBody, size);

        if (identifier.equals(DataTypes.OBJ_GENRE))
        {
            valueToKey = GenreTypes.getInstanceOf().getValueToIdMap();
            keyToValue = GenreTypes.getInstanceOf().getIdToValueMap();
            hasEmptyValue = true;
        }
        else if (identifier.equals(DataTypes.OBJ_TEXT_ENCODING))
        {
            valueToKey = TextEncoding.getInstanceOf().getValueToIdMap();
            keyToValue = TextEncoding.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_INTERPOLATION_METHOD))
        {
            valueToKey = InterpolationTypes.getInstanceOf().getValueToIdMap();
            keyToValue = InterpolationTypes.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_PICTURE_TYPE))
        {
            valueToKey = PictureTypes.getInstanceOf().getValueToIdMap();
            keyToValue = PictureTypes.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_TYPE_OF_EVENT))
        {
            valueToKey = EventTimingTypes.getInstanceOf().getValueToIdMap();
            keyToValue = EventTimingTypes.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_TIME_STAMP_FORMAT))
        {
            valueToKey = EventTimingTimestampTypes.getInstanceOf().getValueToIdMap();
            keyToValue = EventTimingTimestampTypes.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_TYPE_OF_CHANNEL))
        {
            valueToKey = ChannelTypes.getInstanceOf().getValueToIdMap();
            keyToValue = ChannelTypes.getInstanceOf().getIdToValueMap();
        }
        else if (identifier.equals(DataTypes.OBJ_RECIEVED_AS))
        {
            valueToKey = ReceivedAsTypes.getInstanceOf().getValueToIdMap();
            keyToValue = ReceivedAsTypes.getInstanceOf().getIdToValueMap();
        }
        else
        {
            throw new IllegalArgumentException("Hashmap identifier not defined in this class: " + identifier);
        }
    }

    public NumberHashMap(NumberHashMap copyObject)
    {
        super(copyObject);

        this.hasEmptyValue = copyObject.hasEmptyValue;

        // we dont' need to clone/copy the maps here because they are static
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
        if (value instanceof Byte)
        {
            this.value = new Long(((Byte) value).byteValue());
        }
        else if (value instanceof Short)
        {
            this.value = new Long(((Short) value).shortValue());
        }
        else if (value instanceof Integer)
        {
            this.value = new Long(((Integer) value).intValue());
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
        if ((obj instanceof NumberHashMap) == false)
        {
            return false;
        }

        NumberHashMap object = (NumberHashMap) obj;

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

        if (this.valueToKey == null)
        {
            if (object.valueToKey != null)
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
}
