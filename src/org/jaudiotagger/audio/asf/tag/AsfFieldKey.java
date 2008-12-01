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
    ALBUM("WM/AlbumTitle", false),
    ALBUM_ARTIST("WM/AlbumArtist", false),
    ALBUM_ARTIST_SORT("WM/AlbumArtistSortOrder", false),
    ALBUM_SORT("WM/AlbumSortOrder", false),
    AMAZON_ID("ASIN", false),
    ARTIST("Artist", false),
    ARTIST_SORT("WM/ArtistSortOrder", false),
    BARCODE("WM/Barcode",  false),
    BPM("WM/BeatsPerMinute", false),
    CATALOG_NO("WM/CatalogNo",  false),
    COMMENT("WM/Comments", false),
    COMPOSER("WM/Composer",  false),
    COMPOSER_SORT("WM/ComposerSort",  false),
    CONDUCTOR("WM/Conductor",  false),
    COPYRIGHT("COPYRIGHT", false),
    COVER_ART("CoverArt",  false),
    DISC_NO("WM/PartOfSet",  false),
    ENCODER("WM/ToolName",  false),
    GENRE("WM/Genre",  false),
    GENRE_ID("WM/GenreID",false),
    GROUPING("WM/ContentGroupDescription", false),
    IS_COMPILATION("WM/IsCompilation",  false),
    ISRC("WM/ISRC", false),
    ISVBR("IsVBR",  false),
    LYRICIST("WM/Writer", false),
    LYRICS("WM/Lyrics", false),
    MEDIA("WM/Media",  false),
    MOOD("WM/Mood",  false),
    MUSICBRAINZ_ARTISTID("MusicBrainz/Artist Id", false),
    MUSICBRAINZ_DISC_ID("MusicBrainz/Disc Id", false),
    MUSICBRAINZ_RELEASE_COUNTRY("MusicBrainz/Album Release Country", false),
    MUSICBRAINZ_RELEASE_STATUS("MusicBrainz/Album Status",  false),
    MUSICBRAINZ_RELEASE_TYPE("MusicBrainz/Album Type", false),
    MUSICBRAINZ_RELEASEARTISTID("MusicBrainz/Album Artist Id", false),
    MUSICBRAINZ_RELEASEID("MusicBrainz/Album Id",  false),
    MUSICBRAINZ_TRACK_ID("MusicBrainz/Track Id",  false),
    MUSICIP_ID("MusicIP/PUID", false),
    RATING("RATING",false),
    RECORD_LABEL("WM/Publisher", false),
    REMIXER("WM/ModifiedBy", false),
    TITLE("Title", false),
    TITLE_SORT("WM/TitleSortOrder", false),
    TRACK("WM/TrackNumber", false),
    URL_DISCOGS_ARTIST_SITE("WM/DiscogsArtistUrl", false),
    URL_DISCOGS_RELEASE_SITE("WM/DiscogsReleaseUrl", false),
    URL_OFFICIAL_ARTIST_SITE("WM/OfficialArtistUrl", false),
    URL_OFFICIAL_RELEASE_SITE("WM/OfficialReleaseUrl", false),
    URL_WIKIPEDIA_ARTIST_SITE("WM/WikipediaArtistUrl", false),
    URL_WIKIPEDIA_RELEASE_SITE("WM/WikipediaReleaseUrl", false),
    YEAR("WM/Year", false);

    /**
     * Stores the {@link AsfFieldKey#fieldName} to the field key.
     */
    private final static HashMap<String, AsfFieldKey> FIELD_ID_MAP;

    static
    {
        FIELD_ID_MAP = new HashMap<String, AsfFieldKey>(AsfFieldKey.values().length);
        for (AsfFieldKey curr : AsfFieldKey.values())
        {
            FIELD_ID_MAP.put(curr.getFieldName(), curr);
        }
    }


    /**
     * Searches for an ASF field key which represents the given id string.<br>
     * 
     * @param fieldName the fieldname used for this key
     * @return tjhe Enum that represents this field
     */
    public static AsfFieldKey getAsfFieldKey(String fieldName)
    {
       return FIELD_ID_MAP.get(fieldName);
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
     * If set, the field has a standard id assigned.
     */
    private String fieldName;

    /**
     * If <code>true</code>, the field will be stored repeatedly if occurs so in tags.
     */
    private boolean multiValued;

    /**
     * Creates an instance<br>
     * 
     * @param asfFieldId standard field identifier.
     * @param multiValue <code>true</code> if the this ASF field can have multiple values.
     */
    private AsfFieldKey(String asfFieldId, boolean multiValue)
    {
        this.fieldName = asfFieldId;
        this.multiValued = multiValue;
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
        return getFieldName();
    }
}
