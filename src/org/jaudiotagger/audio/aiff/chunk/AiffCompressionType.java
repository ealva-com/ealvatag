package org.jaudiotagger.audio.aiff.chunk;

import java.util.HashMap;
import java.util.Map;

/**
 * Known compression types that can be used with AIFF
 */
public enum AiffCompressionType
{
    NONE("NONE", "not compressed"),
    RAW("raw ", "PCM 8-bit offset-binary"),
    TWOS("twos", "PCM 16-bit twos-complement big-endian"),
    SOWT("sowt", "PCM 16-bit twos-complement little-endian"),
    FL32("fl32", "PCM 32-bit floating point"),
    FL64("fl64", "PCM 64-bit floating point"),
    IN24("in24", "PCM 24-bit integer"),
    IN32("in32", "PCM 32-bit integer"),;

    // Reverse-lookup map for getting a compression type from code
    private static final Map<String, AiffCompressionType> lookup = new HashMap<String, AiffCompressionType>();

    static {
        for (AiffCompressionType d : AiffCompressionType.values()) {
            lookup.put(d.getCode(), d);
        }
    }


    private String code;
    private String description;

    AiffCompressionType(String code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    public static AiffCompressionType getByCode(String code) {
        return lookup.get(code);
    }

}
