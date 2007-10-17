package org.jaudiotagger.tag.mp4.atom;

import org.jaudiotagger.tag.mp4.field.Mp4FieldType;
import org.jaudiotagger.tag.mp4.field.Mp4TagReverseDnsField;

/**
 * List of valid values for the Content Type (Stik) atom
 *
 * These are held as a byte field
 *
 * TODO:Is this only used in video 
 */
public enum Mp4ContentTypeValue
{
    MOVIE("Movie",0),
    NORMAL("Normal",1),
    AUDIO_BOOK("AudioBook",2),
    BOOKMARK("Whacked Bookmark",5),
    MUSIC_VIDEO("Music Video",6),
    SHORT_FILM("Short Film",9),
    TV_SHOW("TV Show",10),
    BOOKLET("Booklet",11)
    ;

    private String description;
    private int    id;


    /**
     *
     * @param description of value
     * @param id used internally
     */
    Mp4ContentTypeValue(String description, int id)
    {
        this.description = description;
        this.id          = id;
    }

     /**
     * Return id used in the file
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * This is the value of the fieldname that is actually used to write mp4
     *
     * @return
     */
    public String getDescription()
    {
        return description;
    }


}
