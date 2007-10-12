/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import static org.jaudiotagger.tag.mp4.Mp4FieldKey.*;
import org.jaudiotagger.tag.mp4.field.*;

import java.util.EnumMap;
import java.util.List;
import java.util.ArrayList;

/**
 * A Logical representation of Mp4Tag, i.e the meta information stored in an Mp4 file underneath the
 * moov.udt.meta.ilst atom.
 */
public class Mp4Tag extends AbstractTag
{

    static EnumMap<TagFieldKey, Mp4FieldKey> tagFieldToMp4Field = new EnumMap<TagFieldKey, Mp4FieldKey>(TagFieldKey.class);

    //Mapping from gerneric key to mp4 key
    static
    {
        tagFieldToMp4Field.put(TagFieldKey.ARTIST, Mp4FieldKey.ARTIST);
        tagFieldToMp4Field.put(TagFieldKey.ALBUM, Mp4FieldKey.ALBUM);
        tagFieldToMp4Field.put(TagFieldKey.TITLE, Mp4FieldKey.TITLE);
        tagFieldToMp4Field.put(TagFieldKey.TRACK, Mp4FieldKey.TRACK);
        tagFieldToMp4Field.put(TagFieldKey.YEAR, Mp4FieldKey.DAY);
        tagFieldToMp4Field.put(TagFieldKey.GENRE, Mp4FieldKey.GENRE);
        tagFieldToMp4Field.put(TagFieldKey.COMMENT, Mp4FieldKey.COMMENT);
        tagFieldToMp4Field.put(TagFieldKey.ALBUM_ARTIST, Mp4FieldKey.ALBUM_ARTIST);
        tagFieldToMp4Field.put(TagFieldKey.COMPOSER, Mp4FieldKey.COMPOSER);
        tagFieldToMp4Field.put(TagFieldKey.GROUPING, Mp4FieldKey.GROUPING);
        tagFieldToMp4Field.put(TagFieldKey.DISC_NO, Mp4FieldKey.DISCNUMBER);
        tagFieldToMp4Field.put(TagFieldKey.BPM, Mp4FieldKey.BPM);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_ARTISTID, Mp4FieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_RELEASEID, Mp4FieldKey.MUSICBRAINZ_ALBUMID);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_TRACK_ID, Mp4FieldKey.MUSICBRAINZ_TRACKID);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_DISC_ID, Mp4FieldKey.MUSICBRAINZ_DISCID);
        tagFieldToMp4Field.put(TagFieldKey.MUSICIP_ID, Mp4FieldKey.MUSICIP_PUID);
        tagFieldToMp4Field.put(TagFieldKey.AMAZON_ID, Mp4FieldKey.ASIN);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, Mp4FieldKey.MUSICBRAINZ_ALBUM_STATUS);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, Mp4FieldKey.MUSICBRAINZ_ALBUM_TYPE);
        tagFieldToMp4Field.put(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, Mp4FieldKey.RELEASECOUNTRY);
        tagFieldToMp4Field.put(TagFieldKey.LYRICS, Mp4FieldKey.LYRICS);
        tagFieldToMp4Field.put(TagFieldKey.IS_COMPILATION, Mp4FieldKey.COMPILATION);
        tagFieldToMp4Field.put(TagFieldKey.ARTIST_SORT, Mp4FieldKey.ARTIST_SORT);
        tagFieldToMp4Field.put(TagFieldKey.ALBUM_ARTIST_SORT, Mp4FieldKey.ALBUM_ARTIST_SORT);
        tagFieldToMp4Field.put(TagFieldKey.ALBUM_SORT, Mp4FieldKey.ALBUM_SORT);
        tagFieldToMp4Field.put(TagFieldKey.TITLE_SORT, Mp4FieldKey.TITLE_SORT);
        tagFieldToMp4Field.put(TagFieldKey.COMPOSER_SORT, Mp4FieldKey.COMPOSER_SORT);
        tagFieldToMp4Field.put(TagFieldKey.COVER_ART,Mp4FieldKey.ARTWORK);
    }

    protected String getArtistId()
    {
        return ARTIST.getFieldName();
    }

    protected String getAlbumId()
    {
        return ALBUM.getFieldName();
    }

    protected String getTitleId()
    {
        return TITLE.getFieldName();
    }

    protected String getTrackId()
    {
        return TRACK.getFieldName();
    }

    protected String getYearId()
    {
        return DAY.getFieldName();
    }

    protected String getCommentId()
    {
        return COMMENT.getFieldName();
    }

    protected String getGenreId()
    {
        return GENRE.getFieldName();
    }

    /**
     * There are two genres fields in mp4 files, but only one can be used at a time, so this method tries to make
     * things easier by checking both and returning the populated one (if any)
     */
    @Override
    public List getGenre()
    {
        List genres = get(GENRE.getFieldName());
        if (genres.size() == 0)
        {
            genres = get(GENRE_CUSTOM.getFieldName());
        }
        return genres;
    }

    public TagField createArtistField(String content)
    {
        return new Mp4TagTextField(getArtistId(), content);
    }

    public TagField createAlbumField(String content)
    {
        return new Mp4TagTextField(getAlbumId(), content);
    }

    public TagField createTitleField(String content)
    {
        return new Mp4TagTextField(getTitleId(), content);
    }

    public TagField createTrackField(String content)
    {
        return new Mp4TrackField(content);
    }

    public TagField createYearField(String content)
    {
        return new Mp4TagTextField(getYearId(), content);
    }

    public TagField createCommentField(String content)
    {
        return new Mp4TagTextField(getCommentId(), content);
    }

    public TagField createGenreField(String content)
    {
        return new Mp4TagTextField(getGenreId(), content);
    }

    protected boolean isAllowedEncoding(String enc)
    {
        return enc.equals(Mp4BoxHeader.CHARSET_UTF_8);
    }

    public String toString()
    {
        return "Mpeg4 " + super.toString();
    }


    /**
     * Maps the generic key to the mp4 key and return the list of values for this field
     *
     * @param genericKey
     */
    @Override
    public List get(TagFieldKey genericKey)
    {
        return super.get(tagFieldToMp4Field.get(genericKey).getFieldName());
    }

    /**
     * Retrieve the  values that exists for this mp4keyId (this is the internalid actually used)
     *
     * @param mp4KeyId TODO:this is inefficient we need to change calling code to use the enums directly
     */
    @Override
    public List get(String mp4KeyId)
    {
        for (Mp4FieldKey mp4FieldKey : Mp4FieldKey.values())
        {
            if (mp4FieldKey.getFieldName().equals(mp4KeyId))
            {
                return super.get(mp4FieldKey.getFieldName());
            }
        }
        return new ArrayList();
    }

    /**
     * Retrieve the  values that exists for this mp4keyId (this is the internalid actually used)
     * <p/>
     * TODO we want AbstractTag to use this method rather than the String equivalent
     *
     * @param mp4FieldKey
     */
    public List get(Mp4FieldKey mp4FieldKey)
    {
        return super.get(mp4FieldKey.getFieldName());
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    public String getFirst(TagFieldKey genericKey)
    {
        return super.getFirst(tagFieldToMp4Field.get(genericKey).getFieldName());
    }

    /**
     * Retrieve the first value that exists for this mp4key
     *
     * @param mp4Key
     * @return
     */
    public String getFirst(Mp4FieldKey mp4Key)
    {
        return super.getFirst(mp4Key.getFieldName());
    }

    public Mp4TagField getFirstField(Mp4FieldKey mp4Key)
    {
        return (Mp4TagField)super.getFirstField(mp4Key.getFieldName());
    }

    /**
     * Delete fields with this generic key
     *
     * @param genericKey
     */
    public void deleteTagField(TagFieldKey genericKey)
    {
        super.deleteField(tagFieldToMp4Field.get(genericKey).getFieldName());
    }

    /**
     * Create discno field
     *
     * @param content can be any of the following
     *                1
     *                1/10
     * @return
     */
    public TagField createDiscNoField(String content)
    {
        return new Mp4DiscNoField(content);
    }

    /**
     * Create artwork field
     *
     * @param data raw image data
     * @return
     */
    public TagField createArtworkField(byte[] data)
    {
        return new Mp4TagCoverField(data);
    }

    /**
     * Create Tag Field using generic key
     * <p/>
     * This should use the correct subclass for the key
     */
    @Override
    public TagField createTagField(TagFieldKey genericKey, String value)
    {
        return createTagField(tagFieldToMp4Field.get(genericKey), value);
    }

    /**
     * Create Tag Field using mp4 key
     * <p/>
     * Uses the correct subclass for the key
     *
     * @param mp4FieldKey
     * @param value
     * @return
     */
    public TagField createTagField(Mp4FieldKey mp4FieldKey, String value)
    {
        switch (mp4FieldKey)
        {
            case BPM:
                COMPILATION:
                RATING:
                return new Mp4TagByteField(mp4FieldKey, value, mp4FieldKey.getFieldLength());

            case GENRE:
                //TODO this doesnt work
                return new Mp4TagTextNumberField(mp4FieldKey.getFieldName(),value);

            case DISCNUMBER:
                return new Mp4DiscNoField(value);

            case TRACK:
                return new Mp4TrackField(value);

            case MUSICBRAINZ_TRACKID:
                MUSICBRAINZ_ARTISTID:
                MUSICBRAINZ_ALBUMID:
                MUSICBRAINZ_ALBUMARTISTID:
                MUSICBRAINZ_DISCID:
                MUSICIP_PUID:
                ASIN:
                MUSICBRAINZ_ALBUM_STATUS:
                MUSICBRAINZ_ALBUM_TYPE:
                RELEASECOUNTRY:
                PART_OF_GAPLESS_ALBUM:
                ITUNES_SMPB:
                ITUNES_NORM:
                return new Mp4TagReverseDnsField(mp4FieldKey, value);

            default:
                return new Mp4TagTextField(mp4FieldKey.getFieldName(), value);
        }
    }
}
