package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.tag.mp4.Mp4FieldType;
import org.jaudiotagger.audio.exceptions.CannotReadException;

/**
 * This a list mp4boxes identifier, that are not part of the metadata info.
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
    MDAT("mdat");

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
