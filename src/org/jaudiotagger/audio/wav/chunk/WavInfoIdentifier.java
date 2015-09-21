package org.jaudiotagger.audio.wav.chunk;

import org.jaudiotagger.tag.FieldKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Known Identifiers used in an INFO Chunk together with their mapping to a generic FieldKey (if known)
 */
public enum WavInfoIdentifier
{
    ARTIST("IART", FieldKey.ARTIST),
    ALBUM_ARTIST("iaar", FieldKey.ALBUM_ARTIST), //Dont know why lowercase, maybe MediaMonkey custom field
    TITLE("INAM", FieldKey.TITLE),
    ALBUM("IPRD", FieldKey.ALBUM),
    TRACKNO("ITRK", FieldKey.TRACK),
    YEAR("ICRD", FieldKey.YEAR),
    GENRE("IGNR", FieldKey.GENRE),
    COMMENTS("ICMT", FieldKey.COMMENT),
    COPYRIGHT("ICOP", null),
    ENCODER("ISFT", FieldKey.ENCODER),
    RATING("IRTD", FieldKey.RATING),
    COMPOSER("IMUS", FieldKey.COMPOSER),
    CONDUCTOR("ITCH", FieldKey.CONDUCTOR),
    LYRICIST("IWRI", FieldKey.LYRICIST),
    ISRC("ISRC", FieldKey.ISRC),
    LABEL("ICMS", FieldKey.RECORD_LABEL),
    TRACK_GAIN("ITGL",null),
    ALBUM_GAIN("IAGL", null)
    ;

    private static final Map<String, WavInfoIdentifier> CODE_TYPE_MAP = new HashMap<String, WavInfoIdentifier>();
    private String code;
    private FieldKey fieldKey;

    WavInfoIdentifier(String code, FieldKey fieldKey)
    {
        this.code=code;
        this.fieldKey=fieldKey;
    }

    public String getCode()
    {
        return code;
    }

    public FieldKey getFieldKey()
    {
        return fieldKey;
    }


    /**
     * Get {@link WavInfoIdentifier} for code (e.g. "SSND").
     *
     * @param code chunk id
     * @return chunk type or {@code null} if not registered
     */
    public synchronized static WavInfoIdentifier get(final String code) {
        if (CODE_TYPE_MAP.isEmpty()) {
            for (final WavInfoIdentifier type : values()) {
                CODE_TYPE_MAP.put(type.getCode(), type);
            }
        }
        return CODE_TYPE_MAP.get(code);
    }

}
