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
    ALBUM_ARTIST("WM/AlbumArtist", TagFieldKey.ALBUM_ARTIST, false),
    ALBUM_ARTIST_SORT("WM/AlbumArtistSortOrder", TagFieldKey.ALBUM_ARTIST_SORT, false),
    ALBUM_SORT("WM/AlbumSortOrder", TagFieldKey.ALBUM_SORT, false),
    AMAZON_ID(null, TagFieldKey.AMAZON_ID, false),
    ARTIST(null, TagFieldKey.ARTIST, false),
    ARTIST_SORT("WM/ArtistSortOrder", TagFieldKey.ARTIST_SORT, false),
    BARCODE(null, TagFieldKey.BARCODE, false), // Description
    BPM("WM/BeatsPerMinute", TagFieldKey.BPM, false),
    CATALOG_NO(null, TagFieldKey.CATALOG_NO, false),
    COMMENT(null, TagFieldKey.COMMENT, false),
    COMPOSER("WM/Composer", TagFieldKey.COMPOSER, false),
    COMPOSER_SORT(null, TagFieldKey.COMPOSER_SORT, false),
    CONDUCTOR("WM/Conductor", TagFieldKey.CONDUCTOR, false),
    COPYRIGHT(null, null, false),
    DISC_NO("WM/PartOfSet", TagFieldKey.DISC_NO, false),
    ENCODER(null, TagFieldKey.ENCODER, false),
    GENRE("WM/Genre", TagFieldKey.GENRE, false),
    GENRE_ID("WM/GenreID", null, false),
    GROUPING("WM/ContentGroupDescription", TagFieldKey.GROUPING, false),
    IS_COMPILATION(null, TagFieldKey.IS_COMPILATION, false),
    ISRC("WM/ISRC", TagFieldKey.ISRC, false),
    LYRICIST("WM/Writer", TagFieldKey.LYRICIST, false),
    LYRICS("WM/Lyrics", TagFieldKey.LYRICS, false),
    MEDIA(null, TagFieldKey.MEDIA, false),
    MOOD("WM/Mood", TagFieldKey.MOOD, false),
    MUSICBRAINZ_ARTISTID("MusicBrainz/Artist Id", TagFieldKey.MUSICBRAINZ_ARTISTID, false),
    MUSICBRAINZ_DISC_ID("MusicBrainz/Disc Id", TagFieldKey.MUSICBRAINZ_DISC_ID, false),
    MUSICBRAINZ_RELEASE_COUNTRY(null, TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, false),
    MUSICBRAINZ_RELEASE_STATUS(null, TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, false),
    MUSICBRAINZ_RELEASE_TYPE(null, TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, false),
    MUSICBRAINZ_RELEASEARTISTID("MusicBrainz/Album Artist Id", TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, false),
    MUSICBRAINZ_RELEASEID("MusicBrainz/Album Id", TagFieldKey.MUSICBRAINZ_RELEASEID, false),
    MUSICBRAINZ_TRACK_ID("MusicBrainz/Track Id", TagFieldKey.MUSICBRAINZ_TRACK_ID, false),
    MUSICIP_ID(null, TagFieldKey.MUSICIP_ID, false),
    RATING(null, null, false),
    RECORD_LABEL(null, TagFieldKey.RECORD_LABEL, false),
    REMIXER("WM/ModifiedBy", TagFieldKey.REMIXER, false),
    TITLE(null, TagFieldKey.TITLE, false),
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
            else if (fields[i].name().equals(id))
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
    protected String getFieldId()
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
