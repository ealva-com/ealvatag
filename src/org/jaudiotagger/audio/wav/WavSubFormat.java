package org.jaudiotagger.audio.wav;

import java.util.HashMap;
import java.util.Map;

/**
 * Wav sub format stored as two byte le integer
 */
public enum WavSubFormat
{
    FORMAT_PCM(1),
    FORMAT_FLOAT(3),
    FORMAT_ALAW(6),
    FORMAT_MULAW(7),
    FORMAT_EXTENSIBLE(0xFFFE),
    ;

    private int code;

    WavSubFormat(int code)
    {
        this.code=code;
    }

    public int getCode()
    {
        return code;
    }

    // Reverse-lookup map for getting a compression type from code
    private static final Map<Integer, WavSubFormat> lookup = new HashMap<Integer, WavSubFormat>();

    static
    {
        for (WavSubFormat next : WavSubFormat.values())
        {
            lookup.put(next.getCode(), next);
        }
    }

    public static WavSubFormat getByCode(Integer code)
    {
        return lookup.get(code);
    }
}
