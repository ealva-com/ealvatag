package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.tag.mp4.Mp4FieldType;
import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * This a list of mp4boxes identifiers, that are not part of the metadata info.
 *
 * These are of limited interest to JAudiotagger
 */
public enum Mp4NotMetaFieldKey
{
    MOOV("moov"),
    MVHD("mvhd"),
    UDTA("udta"),
    META("meta"),
    ILST("ilst"),
    MDAT("mdat"),
    MDIA("mdia"),
    MDHD("mdhd"),
    TKHD("tkhd"),
    TRAK("trak");

    private String fieldName;


    Mp4NotMetaFieldKey(String fieldName)
    {
        this.fieldName = fieldName;

    }

    /**
     * This is the value of the fieldname that is actually used to write mp4
     *
     * @return
     */
    public String getFieldName()
    {
        return fieldName;
    }
}
