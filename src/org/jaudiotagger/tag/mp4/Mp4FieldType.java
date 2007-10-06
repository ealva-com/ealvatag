package org.jaudiotagger.tag.mp4;

/**
 * The classes that MP4Data boxes can be part of.
 */
public enum Mp4FieldType
{
    TEXT(0x1),
    BYTE(0x15),
    NUMERIC(0x0),
    COVERART_JPEG(0x0d),
    COVERART_PNG(0x0e);


    private int fileClassId;

    Mp4FieldType(int fileClassId)
    {
        this.fileClassId=fileClassId;
    }

    public int getFileClassId()
    {
        return  fileClassId;
    }
}
