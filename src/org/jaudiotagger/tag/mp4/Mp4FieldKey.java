package org.jaudiotagger.tag.mp4;

import static org.jaudiotagger.tag.mp4.Mp4FieldType.*;

/**
 * Starting list of known mp4 metadata fields
 *
 * From:
 * http://www.hydrogenaudio.org/forums/index.php?showtopic=29120&st=0&p=251686&#entry251686
 * http://wiki.musicbrainz.org/PicardQt/TagMapping
 * http://atomicparsley.sourceforge.net/mpeg-4files.html
 * <p/>
 * <p/>
 */
public enum Mp4FieldKey
{
    ARTIST("©ART",TEXT),
    ALBUM("©alb",TEXT),
    ALBUM_ARTIST("aART",TEXT),
    GENRE_CUSTOM("©gen",TEXT),
    GENRE("gnre",NUMERIC),
    TITLE("©nam",TEXT),
    TRACK("trkn",NUMERIC),
    BPM("tmpo",BYTE),
    DAY("©day",TEXT),
    COMMENT("©cmt",TEXT),
    COMPOSER("©wrt",TEXT),
    GROUPING("©grp",TEXT),
    DISCNUMBER("disk",NUMERIC),
    LYRICS("©lyr",TEXT),
    RATING("rtng",BYTE),
    ENCODER("©too",TEXT),
    COMPILATION("cpil",BYTE),
    COPYRIGHT("cprt",TEXT),
    CATEGORY("catg",TEXT),
    KEYWORD("keyw",TEXT),
    DESCRIPTION("desc",TEXT),
    ARTIST_SORT("soar",TEXT),
    ALBUM_ARTIST_SORT("soaa",TEXT),
    ALBUM_SORT("soal",TEXT),
    TITLE_SORT("sonm",TEXT),
    COMPOSER_SORT("soco",TEXT),
    SHOW_SORT("sosn",TEXT),
    SHOW("tvsh",TEXT),
    ARTWORK("covr",COVERART),
    MUSICBRAINZ_ARTISTID("com.apple.iTunes","MusicBrainz Artist Id",TEXT),
    MUSICBRAINZ_ALBUMID("com.apple.iTunes","MusicBrainz Album Id",TEXT),
    MUSICBRAINZ_ALBUMARTISTID("com.apple.iTunes","MusicBrainz Album Artist Id",TEXT),
    MUSICBRAINZ_TRACKID("com.apple.iTunes","MusicBrainz Track Id",TEXT),
    MUSICBRAINZ_DISCID("com.apple.iTunes","MusicBrainz Disc Id",TEXT),
    MUSICIP_PUID("com.apple.iTunes","MusicIP PUID",TEXT),
    ASIN("com.apple.iTunes","ASIN",TEXT),
    MUSICBRAINZ_ALBUM_STATUS("com.apple.iTunes","MusicBrainz Album Status",TEXT),
    MUSICBRAINZ_ALBUM_TYPE("com.apple.iTunes","MusicBrainz Album Type",TEXT),
    RELEASECOUNTRY("com.apple.iTunes","MusicBrainz Album Release Country",TEXT)
    ;

    private String fieldName;
    private String issuer;
    private String identifier;
    private Mp4FieldType fieldType;

    /**
     * For usual metadata fields that use a data field
     * @param fieldName
     * @param fieldType of data atom
     */
    Mp4FieldKey(String fieldName,Mp4FieldType fieldType)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    /**
     * For reverse dns fields that use an internal fieldname of '----' and have  additional issuer
     * and identifier fields, we use all three seperated by a ':' ) to give us a unique key
     *
     * @param identifier
     * @param fieldType of data atom
     */
    Mp4FieldKey(String issuer,String identifier,Mp4FieldType fieldType)
    {

        this.issuer=issuer;
        this.identifier= identifier;
        this.fieldName = Mp4TagReverseDnsField.IDENTIFIER+":"+issuer+":"+identifier;

        this.fieldType = fieldType;
    }

    /**
     * This is the value of the fieldname that is actually used to write mp4
     *
     * @return
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     *
     * @return fieldtype
     */
    public Mp4FieldType getFieldType()
    {
        return fieldType;
    }

    public boolean isReverseDnsType()
    {
        return identifier.startsWith(Mp4TagReverseDnsField.IDENTIFIER);
    }
}
