package org.jaudiotagger.tag.mp4.field;

/**
 * Describes the possible types of data held within a Databox
 */
public enum Mp4FieldType
{
    NUMERIC(0x0),
    TEXT(0x1),
    TEXT_UTF16BE(0x02),
    TEXT_JAPANESE(0x03),
    HTML(0x06),
    XML(0x06),
    GUID(0x08),
    ISRC(0x09),
    MI3P(0x10),
    COVERART_GIF(0x0c),
    COVERART_JPEG(0x0d),
    COVERART_PNG(0x0e),
    URL(0x0f),
    DURATION(0x10),
    DATETIME(0x11),
    GENRES(0x12),
    BYTE(0x15),
    RIAAPA(0x18),
    UPC(0x19),
    BMP(0x1B),
    ;


    private int fileClassId;

    Mp4FieldType(int fileClassId)
    {
        this.fileClassId = fileClassId;
    }

    public int getFileClassId()
    {
        return fileClassId;
    }
}
