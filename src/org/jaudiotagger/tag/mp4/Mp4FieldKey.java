package org.jaudiotagger.tag.mp4;

import static org.jaudiotagger.tag.mp4.field.Mp4FieldType.*;
import org.jaudiotagger.tag.mp4.field.Mp4FieldType;
import org.jaudiotagger.tag.mp4.field.Mp4TagReverseDnsField;
import org.jaudiotagger.tag.Tagger;

/**
 * Starting list of known mp4 metadata fields that follow the Parent,Data or ---,issuer,name,data
 * convention. Atoms that contain metadata in other formats are not listed here because they need to be processed
 * specially.
 *
 * <p>Simple metaitems use the parent atom id as their identifier whereas reverse dns (----) atoms use
 * the reversedns,issuer and name fields as their identifier. When the atom is non-0standard but follws the rules
 * we list it here with an additional Tagger field to indicate where the field was originally designed.
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
    BPM("tmpo",BYTE,2),
    DAY("©day",TEXT),
    COMMENT("©cmt",TEXT),
    COMPOSER("©wrt",TEXT),
    GROUPING("©grp",TEXT),
    DISCNUMBER("disk",NUMERIC),
    LYRICS("©lyr",TEXT),
    RATING("rtng",BYTE),   //AFAIK Cant be set in itunes, but if set to explicit itunes will show as explicit
    ENCODER("©too",TEXT),
    COMPILATION("cpil",BYTE,1),
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
    SHOW("tvsh",TEXT),      //tv show but also used just as show
    ARTWORK("covr", COVERART_JPEG),
    PURCHASE_DATE("purd",TEXT),    
    MUSICBRAINZ_ARTISTID("com.apple.iTunes","MusicBrainz Artist Id",TEXT,Tagger.PICARD),
    MUSICBRAINZ_ALBUMID("com.apple.iTunes","MusicBrainz Album Id",TEXT,Tagger.PICARD),
    MUSICBRAINZ_ALBUMARTISTID("com.apple.iTunes","MusicBrainz Album Artist Id",TEXT,Tagger.PICARD),
    MUSICBRAINZ_TRACKID("com.apple.iTunes","MusicBrainz Track Id",TEXT,Tagger.PICARD),
    MUSICBRAINZ_DISCID("com.apple.iTunes","MusicBrainz Disc Id",TEXT,Tagger.PICARD),
    MUSICIP_PUID("com.apple.iTunes","MusicIP PUID",TEXT,Tagger.PICARD),
    ASIN("com.apple.iTunes","ASIN",TEXT,Tagger.PICARD),
    MUSICBRAINZ_ALBUM_STATUS("com.apple.iTunes","MusicBrainz Album Status",TEXT,Tagger.PICARD),
    MUSICBRAINZ_ALBUM_TYPE("com.apple.iTunes","MusicBrainz Album Type",TEXT,Tagger.PICARD),
    RELEASECOUNTRY("com.apple.iTunes","MusicBrainz Album Release Country",TEXT,Tagger.PICARD),
    PART_OF_GAPLESS_ALBUM("pgap",BYTE),
    ITUNES_SMPB("com.apple.iTunes","iTunSMPB",TEXT),
    ITUNES_NORM("com.apple.iTunes","iTunNORM",TEXT),

    //AFAIK These arent actually used by Audio Only files, but there is nothing to prevent them being used
    CONTENT_TYPE("stik",BYTE,1),
    PODCAST_KEYWORD("keyw",TEXT),
    PODCAST_URL("purl",NUMERIC),   //TODO Actually seems to store text but is marked as numeric!
    EPISODE_GLOBAL_ID("egid",NUMERIC),   //TODO Actually seems to store text but is marked as numeric!
    TV_NETWORK("tvnn",TEXT),
    TV_EPISODE_NUMBER("tven",TEXT),
    TV_SEASON("tvsn",BYTE,1),
    TV_EPISODE("tves",BYTE,1),

    //These seem to be used in DRM Files, of type byte , we need to know the byte length to allow them to be written
    //back correctly on saving them, we don't provides options to modify them as may break drm
    AP_ID("apID",TEXT),
    AT_ID("atID",BYTE,4),
    CN_ID("cnID",BYTE,4),
    PL_ID("plID",BYTE,8),
    GE_ID("geID",BYTE,4),
    SF_ID("sfID",BYTE,4),
    AK_ID("akID",BYTE,1),

    //Media Monkey3
    SCORE("rate",TEXT,Tagger.MEDIA_MONKEY),    //As in mark out of 100 
    ISRC("isrc",TEXT,Tagger.MEDIA_MONKEY),
    LYRICIST("lyrc",TEXT,Tagger.MEDIA_MONKEY),
    CONDUCTOR("cond",TEXT,Tagger.MEDIA_MONKEY),
    ORIGINAL_ARTIST("oart",TEXT,Tagger.MEDIA_MONKEY),
    ORIGINAL_ALBUM_TITLE("otit",TEXT,Tagger.MEDIA_MONKEY),
    ORIGINAL_LYRICIST("olyr",TEXT,Tagger.MEDIA_MONKEY),
    INVOLVED_PEOPLE("peop",TEXT,Tagger.MEDIA_MONKEY),
    TEMPO("empo",TEXT,Tagger.MEDIA_MONKEY),
    MOOD("mood",TEXT,Tagger.MEDIA_MONKEY),
    OCCASION("occa",TEXT,Tagger.MEDIA_MONKEY),
    QUALITY("qual",TEXT,Tagger.MEDIA_MONKEY),
    CUSTOM_1("cus1",TEXT,Tagger.MEDIA_MONKEY),
    CUSTOM_2("cus2",TEXT,Tagger.MEDIA_MONKEY),
    CUSTOM_3("cus3",TEXT,Tagger.MEDIA_MONKEY),
    CUSTOM_4("cus4",TEXT,Tagger.MEDIA_MONKEY),
    CUSTOM_5("cus5",TEXT,Tagger.MEDIA_MONKEY),
    ;

    private Tagger tagger;
    private String fieldName;
    private String issuer;
    private String identifier;
    private Mp4FieldType fieldType;
    private int fieldLength;

    /**
     * For usual metadata fields that use a data field
     *
     * @param fieldName
     * @param fieldType of data atom
     */
    Mp4FieldKey(String fieldName,Mp4FieldType fieldType)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    /**
     * For usual metadata fields that use a data field, but not recognised as standard field
     *
     * @param fieldName
     * @param fieldType of data atom
     */
    Mp4FieldKey(String fieldName,Mp4FieldType fieldType,Tagger tagger)
    {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.tagger    = tagger;
    }

    /**
     * For usual metadata fields that use a data field where the field length is fixed
     * such as Byte fields
     *
     * @param fieldName
     * @param fieldType
     * @param fieldLength
     */
    Mp4FieldKey(String fieldName,Mp4FieldType fieldType,int fieldLength)
    {
        this.fieldName   = fieldName;
        this.fieldType   = fieldType;
        this.fieldLength = fieldLength;
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
     * For reverse dns fields that use an internal fieldname of '----' and have  additional issuer
     * and identifier fields, we use all three seperated by a ':' ) to give us a unique key
     * For non-standard fields
     * @param identifier
     * @param fieldType of data atom
     */
    Mp4FieldKey(String issuer,String identifier,Mp4FieldType fieldType,Tagger tagger)
    {

        this.issuer=issuer;
        this.identifier= identifier;
        this.fieldName = Mp4TagReverseDnsField.IDENTIFIER+":"+issuer+":"+identifier;
        this.fieldType = fieldType;
        this.tagger    = tagger;
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

    /**
     *
     * @return true if this is a reverse dns key
     */
    public boolean isReverseDnsType()
    {
        return identifier.startsWith(Mp4TagReverseDnsField.IDENTIFIER);
    }

    /**
     *
     * @return issuer (Reverse Dns Fields Only)
     */
    public String getIssuer()
    {
        return issuer;
    }

    /**
     *
     * @return identifier (Reverse Dns Fields Only)
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     *
     * @return field length (currently only used by byte fields)
     */
    public int getFieldLength()
    {
        return fieldLength;
    }

    public Tagger getTagger()
    {
        if(tagger!=null)
        {
            return tagger;
        }
        return Tagger.ITUNES;
    }
}
