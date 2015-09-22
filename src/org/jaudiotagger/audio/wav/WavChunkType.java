package org.jaudiotagger.audio.wav;

import java.util.HashMap;
import java.util.Map;

/**
 * Chunk types mark each {@link org.jaudiotagger.audio.iff.ChunkHeader}. They are <em>always</em> 4 ASCII chars long.
 *
 * @see org.jaudiotagger.audio.iff.Chunk
 */
public enum WavChunkType
{
    FORMAT("fmt "),
    FACT("fact"),
    DATA("data"),
    LIST("LIST"),
    INFO("INFO"),
    ID3("id3 "),

    ;

    private static final Map<String, WavChunkType> CODE_TYPE_MAP = new HashMap<String, WavChunkType>();
    private String code;

    /**
     * Get {@link WavChunkType} for code (e.g. "SSND").
     *
     * @param code chunk id
     * @return chunk type or {@code null} if not registered
     */
    public synchronized static WavChunkType get(final String code) {
        if (CODE_TYPE_MAP.isEmpty()) {
            for (final WavChunkType type : values()) {
                CODE_TYPE_MAP.put(type.getCode(), type);
            }
        }
        return CODE_TYPE_MAP.get(code);
    }

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
