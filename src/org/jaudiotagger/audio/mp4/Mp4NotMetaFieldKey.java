package org.jaudiotagger.audio.mp4;

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
    FREE("free"),
    TRAK("trak"),
    SMHD("smhd"),
    STBL("stbl"),
    STSD("stsd"),
    MP4A("mp4a"),
    ESDS("esds"),
    MINF("minf");
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
