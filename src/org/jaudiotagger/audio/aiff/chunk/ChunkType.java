package org.jaudiotagger.audio.aiff.chunk;

/**
 * Chunk types mark each {@link ChunkHeader}. They are <em>always</em> 4 ASCII chars long.
 *
 * @see Chunk
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
    COPYRIGHT("(c) "),
    ANNOTATION("ANNO"),
    TAG("ID3 ");

    private String code;

    /**
     * @param code 4 char string
     */
    ChunkType(final String code)
    {
        this.code=code;
    }

    /**
     * 4 char type code.
     *
     * @return 4 char type code, e.g. "SSND" for the sound chunk.
     */
    public String getCode()
    {
        return code;
    }
}
