package org.jaudiotagger.audio.mp4;

/**
 * This a list of mp4boxes identifiers, that are not part of the metadata info.
 *
 * <p>These are of limited interest to JAudiotagger but are required when reading audio info
 * or when writing modifications to the file when the location of audio has shifted.
 */
public enum Mp4NotMetaFieldKey
{
    MOOV("moov","Top level Presentation"),
    MVHD("mvhd","Tracks Summary"),
    UDTA("udta","Copyright"),
    META("meta","MetaInformation"),
    ILST("ilst","MetaInformation Optional"),
    MDAT("mdat","Audio Data"),
    MDIA("mdia",""),
    MDHD("mdhd",""),
    TKHD("tkhd",""),
    FREE("free","Padding"),
    TRAK("trak","Track"),
    SMHD("smhd","Audio Balance"),
    STBL("stbl",""),
    STSD("stsd",""),
    MP4A("mp4a","AAC Audio "),
    ESDS("esds","Track codec specific information"),
    MINF("minf",""),
    STCO("stco","Offsets into Audio Data"),
    DRMS("drms","DRM protected File");
    private String fieldName;
    private String description;

    Mp4NotMetaFieldKey(String fieldName,String description)
    {
        this.fieldName = fieldName;
        this.description=description;

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

    /**
     *
     * @return description, human redable description of the atom
     */
    public String getDescription()
    {
        return description;
    }
}
