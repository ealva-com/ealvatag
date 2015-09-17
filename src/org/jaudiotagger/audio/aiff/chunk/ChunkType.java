package org.jaudiotagger.audio.aiff.chunk;

/**
 * Created by Paul on 16/09/2015.
 */
public enum ChunkType
{
    FORMAT_VERSION("FVER"),
    APPLICATION("APPL"),
    SOUND("SSND"),
    COMMON("COMM"),
    COMMENTS("COMT"),
    NAME("NAME"),
    AUTHOR("AUTH"),
    COPYRIGHT("(c)"),
    ANNOTATION("ANNO"),
    TAG("ID3 "),
    ;

    private String code;

    ChunkType(String code)
    {
        this.code=code;
    }

    public String getCode()
    {
        return code;
    }
}
