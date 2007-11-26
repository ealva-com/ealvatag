package org.jaudiotagger.tag.vorbiscomment;

/**
 * Common Vorbis Comment Field Names
 * 
 * <p>Note:The enumname is also the the actual name of the field in the VorbisCommentTag, but you could introduce a level
 * of indirection here if required.
 *
 * <p>
 * This partial list is derived fom the following sources:
 * <ul>
 * <li>http://xiph.org/vorbis/doc/v-comment.html</li>
 * <li>http://wiki.musicbrainz.org/PicardQt/TagMapping</li>
 * <li>http://reactor-core.org/ogg-tagging.html</li>
 * </ul>
 */
public enum VorbisCommentFieldKey
{
    ARTIST,
    ALBUM,
    DESCRIPTION,
    GENRE,
    TITLE,
    TRACKNUMBER,
    DATE,    
    COMMENT,
    ALBUMARTIST,
    COMPOSER,
    GROUPING,
    DISCNUMBER,
    BPM,
    MUSICBRAINZ_ARTISTID,
    MUSICBRAINZ_ALBUMID,
    MUSICBRAINZ_ALBUMARTISTID,
    MUSICBRAINZ_TRACKID,
    MUSICBRAINZ_DISCID,
    MUSICIP_PUID,
    ASIN,
    MUSICBRAINZ_ALBUMSTATUS,
    MUSICBRAINZ_ALBUMTYPE,
    RELEASECOUNTRY,
    LYRICS,
    COMPILATION,
    ARTISTSORT,
    ALBUMARTISTSORT,
    ALBUMSORT,
    TITLESORT,
    COMPOSERSORT,
    COVERARTMIME,
    COVERART,
    VENDOR
}
