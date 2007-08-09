package org.jaudiotagger.tag.mp4;

/**
 * Starting list
 * From:
 * http://www.hydrogenaudio.org/forums/index.php?showtopic=29120&st=0&p=251686&#entry251686
 * http://wiki.musicbrainz.org/PicardQt/TagMapping
 * <p/>
 * <p/>
 * TODO:Are these field names correct or should some have an @ sign at front
 * TODO:musicbrainz fields (marked as ---- in PicardtMapping
 */
public enum Mp4FieldKey
{
    ARTIST("ART"),
    ALBUM("alb"),
    GENRE("gen"),
    TITLE("nam"),
    TRACKNUMBER("trkn"),
    DAY("day"),
    TRACK("trkn"),
    COMMENT("cmt"),
    COMPOSER("wrt"),
    GROUPING("grp"),
    DISCNUMBER("disk"),
    LYRICS("lyr"),
    RATING("rtng"),
    ENCODER("too"),
    COMPILATION("cpil");


    private String fieldName;

    Mp4FieldKey(String fieldName)
    {
        this.fieldName = fieldName;
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
}
