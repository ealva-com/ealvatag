package org.jaudiotagger.tag.mp4;

import static org.jaudiotagger.tag.mp4.Mp4FieldType.*;
/**
 * Starting list
 * From:
 * http://www.hydrogenaudio.org/forums/index.php?showtopic=29120&st=0&p=251686&#entry251686
 * http://wiki.musicbrainz.org/PicardQt/TagMapping
 * http://atomicparsley.sourceforge.net/mpeg-4files.html
 * <p/>
 * <p/>
 * TODO:musicbrainz fields (marked as ---- in PicardtMapping)
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
    BPM("tmpo",NUMERIC),
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
    ARTISTSORT("soar",TEXT),
    ALBUMARTISTSORT("soaa",TEXT),
    ALBUMSORT("soal",TEXT),
    TITLESORT("sonm",TEXT),
    COMPOSERSORT("soco",TEXT),
    ARTWORK("covr",COVERART);

    private String fieldName;
    private Mp4FieldType fieldType;

    Mp4FieldKey(String fieldName,Mp4FieldType fieldType)
    {
        this.fieldName = fieldName;
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
}
