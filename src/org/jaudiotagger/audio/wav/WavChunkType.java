package org.jaudiotagger.audio.wav;

/**
 * Chunk types mark each {@link org.jaudiotagger.audio.iff.ChunkHeader}. They are <em>always</em> 4 ASCII chars long.
 *
 * @see org.jaudiotagger.audio.iff.Chunk
 */
public enum WavChunkType
{
    FORMAT("fmt "),
    ;

    private String code;

    /**
     * @param code 4 char string
     */
    WavChunkType(final String code)
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
