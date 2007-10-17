package org.jaudiotagger.tag.mp4.field;

/**
 * Describes the possible types of data held within a Databox
 */
public enum Mp4FieldType
{
    NUMERIC(0x0),
    TEXT(0x1),
    BYTE(0x15),      
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
