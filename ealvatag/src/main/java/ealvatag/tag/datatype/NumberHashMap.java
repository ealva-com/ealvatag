/*
 * @author : Paul Taylor
 * @author : Eric Farng
 * <p>
 * Version @version:$Id$
 * <p>
 * MusicTag Copyright (C)2003,2004
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public  License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, you can get a copy from
 * http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301 USA
 * <p>
 * Description:
 */
package ealvatag.tag.datatype;

import ealvatag.logging.ErrorMessage;
import ealvatag.tag.InvalidDataTypeException;
import ealvatag.tag.id3.AbstractTagFrameBody;
import ealvatag.tag.id3.valuepair.ChannelTypes;
import ealvatag.tag.id3.valuepair.EventTimingTimestampTypes;
import ealvatag.tag.id3.valuepair.EventTimingTypes;
import ealvatag.tag.id3.valuepair.InterpolationTypes;
import ealvatag.tag.id3.valuepair.ReceivedAsTypes;
import ealvatag.tag.id3.valuepair.SynchronisedLyricsContentType;
import ealvatag.tag.id3.valuepair.TextEncoding;
import ealvatag.tag.reference.PictureTypes;
import ealvatag.utils.EqualsUtil;

import java.util.Map;

/**
 * Represents a number that acts as a key into an enumeration of values
 */
public class NumberHashMap extends NumberFixedLength {

    // TODO: 1/18/17 Change this to an interface. This depends on an implementation a lookup
    private Map<Integer, String> keyToValue = null;


    /**
     *
     */
    private boolean hasEmptyValue = false;


    /**
     * Creates a new ObjectNumberHashMap datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size
     *
     * @throws IllegalArgumentException
     */
    public NumberHashMap(String identifier, AbstractTagFrameBody frameBody, int size) {
        super(identifier, frameBody, size);

        switch (identifier) {
            // OBJ_GENRE never used!
//            case DataTypes.OBJ_GENRE:
//                valueToKey = GenreTypes.getInstanceOf().getValueToIdMap();
//                keyToValue = GenreTypes.getInstanceOf().getIdToValueMap();
//
//                //genres can be an id or literal value
//                hasEmptyValue = true;
//                break;
            case DataTypes.OBJ_TEXT_ENCODING:
                keyToValue = TextEncoding.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_INTERPOLATION_METHOD:
                keyToValue = InterpolationTypes.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_PICTURE_TYPE:
                keyToValue = PictureTypes.getInstanceOf().getIdToValueMap();

                //Issue #224 Values should map, but have examples where they dont, this is a workaround
                hasEmptyValue = true;
                break;
            case DataTypes.OBJ_TYPE_OF_EVENT:
                keyToValue = EventTimingTypes.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_TIME_STAMP_FORMAT:
                keyToValue = EventTimingTimestampTypes.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_TYPE_OF_CHANNEL:
                keyToValue = ChannelTypes.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_RECIEVED_AS:
                keyToValue = ReceivedAsTypes.getInstanceOf().getIdToValueMap();
                break;
            case DataTypes.OBJ_CONTENT_TYPE:
                keyToValue = SynchronisedLyricsContentType.getInstanceOf().getIdToValueMap();
                break;
            default:
                throw new IllegalArgumentException("Hashmap identifier not defined in this class: " + identifier);
        }
    }

    public NumberHashMap(NumberHashMap copyObject) {
        super(copyObject);

        this.hasEmptyValue = copyObject.hasEmptyValue;

        // we don't need to clone/copy the maps here because they are static
        this.keyToValue = copyObject.keyToValue;
    }

    public void setValue(Object value) {
        if (value instanceof Byte) {
            this.value = (long)(Byte)value;
        } else if (value instanceof Short) {
            this.value = (long)(Short)value;
        } else if (value instanceof Integer) {
            this.value = (long)(Integer)value;
        } else {
            this.value = value;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof NumberHashMap)) {
            return false;
        }

        NumberHashMap that = (NumberHashMap)obj;

        return
                EqualsUtil.areEqual(hasEmptyValue, that.hasEmptyValue) &&
                        EqualsUtil.areEqual(keyToValue, that.keyToValue) &&
                        super.equals(that);
    }

    /**
     * Read the key from the buffer.
     *
     * @param array
     * @param offset
     *
     * @throws InvalidDataTypeException if emptyValues are not allowed and the eky was invalid.
     */
    public void readByteArray(byte[] array, int offset) throws InvalidDataTypeException {
        super.readByteArray(array, offset);

        //Mismatch:Superclass uses Long, but maps expect Integer
        Integer intValue = ((Long)value).intValue();
        if (!keyToValue.containsKey(intValue)) {
            if (!hasEmptyValue) {
                throw new InvalidDataTypeException(ErrorMessage.MP3_REFERENCE_KEY_INVALID.getMsg(identifier, intValue));
            } else if (identifier.equals(DataTypes.OBJ_PICTURE_TYPE)) {
                LOG.warn(ErrorMessage.MP3_PICTURE_TYPE_INVALID.getMsg(value));
            }
        }
    }

    public String toString() {
        if (value == null) {
            return "";
        } else if (keyToValue.get(value) == null) {
            return "";
        } else {
            return keyToValue.get(value);
        }
    }
}
