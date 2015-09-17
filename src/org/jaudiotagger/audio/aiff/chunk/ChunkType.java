package org.jaudiotagger.audio.aiff.chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Chunk types mark each {@link org.jaudiotagger.audio.iff.ChunkHeader}. They are <em>always</em> 4 ASCII chars long.
 *
 * @see org.jaudiotagger.audio.iff.Chunk
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

    private static final Map<String, ChunkType> CODE_TYPE_MAP = new HashMap<String, ChunkType>();
    private String code;

    /**
     * @param code 4 char string
     */
    ChunkType(final String code)
    {
        this.code=code;
    }

    /**
     * Get {@link ChunkType} for code (e.g. "SSND").
     *
     * @param code chunk id
     * @return chunk type or {@code null} if not registered
     */
    public synchronized static ChunkType get(final String code) {
        if (CODE_TYPE_MAP.isEmpty()) {
            for (final ChunkType type : values()) {
                CODE_TYPE_MAP.put(type.getCode(), type);
            }
        }
        return CODE_TYPE_MAP.get(code);
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
