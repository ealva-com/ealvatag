package org.jaudiotagger.audio.ogg.util;

/**
 * Vorbis Packet Type
 */
public enum VorbisPacketType
{
    AUDIO(0),
    IDENTIFICATION_HEADER(1),
    COMMENT_HEADER(3),
    SETUP_HEADER(5);

    int type;

    VorbisPacketType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
}
