package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.tag.TagFieldKey;

/**
 * Field keys which need to be mapped for ASF files, or only specified for ASF.
 * 
 * @author Christian Laireiter
 */
public enum AsfFieldKey
{

    ALBUM("WM/AlbumTitle", TagFieldKey.ALBUM, false),
    ARTIST("WM/AlbumArtist", TagFieldKey.ARTIST, false),
    COMMENT(null, TagFieldKey.COMMENT, false),
    COPYRIGHT("SPECIAL/WM/COPYRIGHT", null, false),
    GENRE("WM/Genre", TagFieldKey.GENRE, false),
    GENRE_ID("WM/GenreID", null, false),
    RATING(null, null, false),
    TITLE(null, TagFieldKey.TITLE, false), // Description
    TRACK("WM/TrackNumber", TagFieldKey.TRACK, false),
    YEAR("WM/Year", TagFieldKey.YEAR, false);

    /**
     * Returns id itself if:<br>
     * <ol>
     * <li>Id is the {@linkplain #getFieldId() field identifier} of an AsfFieldKey</li>
     * <li>There is no {@link TagFieldKey} with this {@link Enum#name()}</li>
     * </ol>
     * <br>
     * Otherwise a AsfFieldKey is searched which has a TagFieldKey with this name assigned, and
     * returns its field identifier.<br>
     * @param id The field identifier (key) to look up.
     */
    public static String convertId(String id)
    {
        String result = null;
        AsfFieldKey[] fields = AsfFieldKey.class.getEnumConstants();
        for (int i = 0; i < fields.length && result == null; i++)
        {
            if (fields[i].getFieldId() != null && id.equals(fields[i].getFieldId()))
            {
                result = id;
            }
            else if (fields[i].getCorresponding() != null && id.equals(fields[i].getCorresponding().name()))
            {
                result = fields[i].getFieldId();
            }
        }
        if (result == null)
        {
            result = id;
        }
        return result;
    }

    public static String convertId(TagFieldKey key)
    {
        return convertId(key.name());
    }

    public static AsfFieldKey getAsfFieldKey(String id)
    {
        AsfFieldKey result = null;
        AsfFieldKey[] fields = AsfFieldKey.class.getEnumConstants();
        for (int i = 0; i < fields.length && result == null; i++)
        {
            if (fields[i].getFieldId() != null && id.equals(fields[i].getFieldId()))
            {
                result = fields[i];
            }
            else if (fields[i].getCorresponding() != null && id.equals(fields[i].getCorresponding().name()))
            {
                result = fields[i];
            }
        }
        return result;
    }
    
    public static boolean isMultiValued(String id)
    {
        boolean result = false; // For now, there is no support for multi values.
        AsfFieldKey fieldKey = getAsfFieldKey(id);
        if (fieldKey != null)
        {
            result = fieldKey.isMultiValued();
        }
        return result;
    }

    /**
     * If set, there is a translation from this <code>TagFieldKey</code>.
     */
    private TagFieldKey corresponding;

    /**
     * If set, the field has a standard id assigned.
     */
    private String fieldId;

    /**
     * If <code>true</code>, the field will be stored repeatedly if occurs so in tags.
     */
    private boolean multiValued;

    /**
     * Creates an instance, which corresponds to <code>corresponding</code> and has a standard identifier
     * in the ASF file format.<br>
     * 
     * @param asfFieldId standard field identifier.
     * @param correspondingKey the corresponding tag field key.
     */
    private AsfFieldKey(String asfFieldId, TagFieldKey correspondingKey, boolean mutliValue)
    {
        this.fieldId = asfFieldId;
        this.corresponding = correspondingKey;
        this.multiValued = mutliValue;
    }

    /**
     * Returns the corresponding field key.<br>
     * 
     * @return the corresponding field key. (may be <code>null</code>)
     */
    public TagFieldKey getCorresponding()
    {
        return this.corresponding;
    }

    /**
     * Returns the standard field id.
     * 
     * @return the standard field id. (may be <code>null</code>)
     */
    public String getFieldId()
    {
        return this.fieldId;
    }

    /**
     * Returns {@link #getFieldId()} if not <code>null</code>, otherwise the name of {@link #getCorresponding()}.
     * If this is <code>null</code> too, <code>null</code> will be returned.
     * 
     * @return see description
     */
    public String getPublicFieldId()
    {
        String result = getFieldId();
        if (result == null && getCorresponding() != null)
        {
            result = getCorresponding().name();
        }
        return result;
    }

    public boolean isMultiValued()
    {
        return this.multiValued;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getPublicFieldId();
    }
}
