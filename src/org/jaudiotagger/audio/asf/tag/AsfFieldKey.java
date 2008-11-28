package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.util.Utils;

import org.jaudiotagger.tag.TagFieldKey;

import java.util.HashMap;

/**
 * Field keys which need to be mapped for ASF files, or only specified for ASF.
 * 
 * @author Christian Laireiter
 */
public enum AsfFieldKey
{

    ALBUM("WM/AlbumTitle", TagFieldKey.ALBUM, false),
    ALBUM_ARTIST("WM/AlbumArtist", TagFieldKey.ALBUM_ARTIST, false),
    ALBUM_ARTIST_SORT("WM/AlbumArtistSortOrder", TagFieldKey.ALBUM_ARTIST_SORT, false),
    ALBUM_SORT("WM/AlbumSortOrder", TagFieldKey.ALBUM_SORT, false),
    AMAZON_ID("ASIN", TagFieldKey.AMAZON_ID, false),
    ARTIST("Artist", TagFieldKey.ARTIST, false),
    ARTIST_SORT("WM/ArtistSortOrder", TagFieldKey.ARTIST_SORT, false),
    BARCODE(null, TagFieldKey.BARCODE, false),
    BPM("WM/BeatsPerMinute", TagFieldKey.BPM, false),
    CATALOG_NO(null, TagFieldKey.CATALOG_NO, false),
    COMMENT("WM/Comments", TagFieldKey.COMMENT, false),
    COMPOSER("WM/Composer", TagFieldKey.COMPOSER, false),
    COMPOSER_SORT(null, TagFieldKey.COMPOSER_SORT, false),
    CONDUCTOR("WM/Conductor", TagFieldKey.CONDUCTOR, false),
    COPYRIGHT(null, null, false),
    COVER_ART(null, TagFieldKey.COVER_ART, false),
    DISC_NO("WM/PartOfSet", TagFieldKey.DISC_NO, false),
    ENCODER("WM/ToolName", TagFieldKey.ENCODER, false),
    GENRE("WM/Genre", TagFieldKey.GENRE, false),
    GENRE_ID("WM/GenreID", null, false),
    GROUPING("WM/ContentGroupDescription", TagFieldKey.GROUPING, false),
    IS_COMPILATION(null, TagFieldKey.IS_COMPILATION, false),
    ISRC("WM/ISRC", TagFieldKey.ISRC, false),
    ISVBR("IsVBR", null, false),
    LYRICIST("WM/Writer", TagFieldKey.LYRICIST, false),
    LYRICS("WM/Lyrics", TagFieldKey.LYRICS, false),
    MEDIA(null, TagFieldKey.MEDIA, false),
    MOOD("WM/Mood", TagFieldKey.MOOD, false),
    MUSICBRAINZ_ARTISTID("MusicBrainz/Artist Id", TagFieldKey.MUSICBRAINZ_ARTISTID, false),
    MUSICBRAINZ_DISC_ID("MusicBrainz/Disc Id", TagFieldKey.MUSICBRAINZ_DISC_ID, false),
    MUSICBRAINZ_RELEASE_COUNTRY("MusicBrainz/Album Release Country", TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, false),
    MUSICBRAINZ_RELEASE_STATUS("MusicBrainz/Album Status", TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, false),
    MUSICBRAINZ_RELEASE_TYPE("MusicBrainz/Album Type", TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, false),
    MUSICBRAINZ_RELEASEARTISTID("MusicBrainz/Album Artist Id", TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, false),
    MUSICBRAINZ_RELEASEID("MusicBrainz/Album Id", TagFieldKey.MUSICBRAINZ_RELEASEID, false),
    MUSICBRAINZ_TRACK_ID("MusicBrainz/Track Id", TagFieldKey.MUSICBRAINZ_TRACK_ID, false),
    MUSICIP_ID("MusicIP/PUID", TagFieldKey.MUSICIP_ID, false),
    RATING(null, null, false),    
    RECORD_LABEL("WM/Publisher", TagFieldKey.RECORD_LABEL, false),
    REMIXER("WM/ModifiedBy", TagFieldKey.REMIXER, false),
    TITLE("Title", TagFieldKey.TITLE, false),
    TITLE_SORT("WM/TitleSortOrder", TagFieldKey.TITLE_SORT, false),
    TRACK("WM/TrackNumber", TagFieldKey.TRACK, false),
    URL_DISCOGS_ARTIST_SITE(null, TagFieldKey.URL_DISCOGS_ARTIST_SITE, false),
    URL_DISCOGS_RELEASE_SITE(null, TagFieldKey.URL_DISCOGS_RELEASE_SITE, false),
    URL_OFFICIAL_ARTIST_SITE(null, TagFieldKey.URL_OFFICIAL_ARTIST_SITE, false),
    URL_OFFICIAL_RELEASE_SITE(null, TagFieldKey.URL_OFFICIAL_RELEASE_SITE, false),
    URL_WIKIPEDIA_ARTIST_SITE(null, TagFieldKey.URL_WIKIPEDIA_ARTIST_SITE, false),
    URL_WIKIPEDIA_RELEASE_SITE(null, TagFieldKey.URL_WIKIPEDIA_RELEASE_SITE, false),
    YEAR("WM/Year", TagFieldKey.YEAR, false);

    /**
      * Stores the {@link AsfFieldKey#getCorresponding()} name() to the field key.
      */
    private final static HashMap<String, AsfFieldKey> CORR_MAP;
    /**
     * Stores the {@link AsfFieldKey#fieldName} to the field key.
     */
    private final static HashMap<String, AsfFieldKey> FIELD_ID_MAP;
    /**
     * Stores the {@link AsfFieldKey#name()} to the field key.}
     */
    private final static HashMap<String, AsfFieldKey> NAME_MAP;

    static
    {
        FIELD_ID_MAP = new HashMap<String, AsfFieldKey>(AsfFieldKey.values().length);
        CORR_MAP = new HashMap<String, AsfFieldKey>(AsfFieldKey.values().length);
        NAME_MAP = new HashMap<String, AsfFieldKey>(AsfFieldKey.values().length);
        for (AsfFieldKey curr : AsfFieldKey.values())
        {
            NAME_MAP.put(curr.name(), curr);
            if (!Utils.isBlank(curr.getFieldName()))
            {
                FIELD_ID_MAP.put(curr.getFieldName(), curr);
            }
            if (curr.getCorresponding() != null)
            {
                CORR_MAP.put(curr.getCorresponding().name(), curr);
            }
        }
    }

    /**
     * Returns id itself if:<br>
     * <ol>
     * <li>Id is the {@linkplain #getFieldName() field identifier} of an AsfFieldKey</li>
     * <li>There is no {@link TagFieldKey} with this {@link Enum#name()}</li>
     * </ol>
     * <br>
     * Otherwise a AsfFieldKey is searched which has a TagFieldKey with this name assigned, and
     * returns its field identifier.<br>
     * @param id The field identifier (key) to look up.
     * @return converted identifier
     */
    public static String convertId(String id)
    {
        String result = null;
        AsfFieldKey key = getAsfFieldKey(id);
        if (key == null)
        {
            result = id;
        }
        else
        {
            result = key.getPublicFieldId();
        }
        return result;
    }

    /**
     * Convenience method for converting the id by the keys {@linkplain TagFieldKey#name() name}.<br>
     * 
     * @param key the key whose name to convert.
     * @return ASF internal key.
     */
    public static String convertId(TagFieldKey key)
    {
        return convertId(key.name());
    }

    /**
     * Searches for an ASF field key which represents the given id string.<br>
     * 
     * @param id the key configured for this id.
     * @return ASF field key for the id, or <code>null</code> if not available. 
     */
    public static AsfFieldKey getAsfFieldKey(String id)
    {
        AsfFieldKey result = FIELD_ID_MAP.get(id);
        if (result == null)
        {
            result = CORR_MAP.get(id);
        }
        if (result == null)
        {
            result = NAME_MAP.get(id);
        }
        return result;
    }

    /**
     * Tests whether the field is enabled for multiple values.<br>
     * @param id field id to test.
     * @return <code>true</code> if ASF implementation supports multiple values for the field.
     */
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
    private String fieldName;

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
     * @param multiValue <code>true</code> if the this ASF field can have multiple values.
     */
    private AsfFieldKey(String asfFieldId, TagFieldKey correspondingKey, boolean multiValue)
    {
        this.fieldName = asfFieldId;
        this.corresponding = correspondingKey;
        this.multiValued = multiValue;
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
    public String getFieldName()
    {
        return this.fieldName;
    }

    /**
     * Returns {@link #getFieldName()} if not <code>null</code>, otherwise the name of {@link #getCorresponding()}.
     * If this is <code>null</code> too, <code>null</code> will be returned.
     * 
     * @return see description
     */
    public String getPublicFieldId()
    {
        String result = getFieldName();
        if (result == null)
        {
            if (getCorresponding() != null)
            {
                result = getCorresponding().name();
            }
            else
            {
                result = this.name();
            }
        }
        return result;
    }

    /**
     * Returns <code>true</code> if this field can store multiple values.
     * 
     * @return <code>true</code> if multiple values are supported for this field.
     */
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
