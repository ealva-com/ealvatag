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
package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;

import static org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey.*;
import org.jaudiotagger.tag.TagFieldKey;

import java.util.EnumMap;
import java.util.List;

/**
 * This is the logical representation of  Vorbis Comment Data 
 *
 * This partial list is derived fom the following sources:
 * <p>
 * http://xiph.org/vorbiscomment/doc/v-comment.html
 * http://wiki.musicbrainz.org/PicardQt/TagMapping
 * http://reactor-core.org/ogg-tagging.html
 * </p>
 */
public class VorbisCommentTag extends AbstractTag
{

    static EnumMap<TagFieldKey, VorbisCommentFieldKey> tagFieldToOggField = new EnumMap<TagFieldKey, VorbisCommentFieldKey>(TagFieldKey.class);

    static
    {
        tagFieldToOggField.put(TagFieldKey.ARTIST, VorbisCommentFieldKey.ARTIST);
        tagFieldToOggField.put(TagFieldKey.ALBUM, VorbisCommentFieldKey.ALBUM);
        tagFieldToOggField.put(TagFieldKey.TITLE, VorbisCommentFieldKey.TITLE);
        tagFieldToOggField.put(TagFieldKey.TRACK, VorbisCommentFieldKey.TRACKNUMBER);
        tagFieldToOggField.put(TagFieldKey.YEAR, VorbisCommentFieldKey.DATE);
        tagFieldToOggField.put(TagFieldKey.GENRE, VorbisCommentFieldKey.GENRE);
        tagFieldToOggField.put(TagFieldKey.COMMENT, VorbisCommentFieldKey.COMMENT);
        tagFieldToOggField.put(TagFieldKey.ALBUM_ARTIST, VorbisCommentFieldKey.ALBUMARTIST);
        tagFieldToOggField.put(TagFieldKey.COMPOSER, VorbisCommentFieldKey.COMPOSER);
        tagFieldToOggField.put(TagFieldKey.GROUPING, VorbisCommentFieldKey.GROUPING);
        tagFieldToOggField.put(TagFieldKey.DISC_NO, VorbisCommentFieldKey.DISCNUMBER);
        tagFieldToOggField.put(TagFieldKey.BPM, VorbisCommentFieldKey.BPM);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_ARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_RELEASEID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMEID);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_TRACKID);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_DISC_ID, VorbisCommentFieldKey.MUSICBRAINZ_DISCID);
        tagFieldToOggField.put(TagFieldKey.MUSICIP_ID, VorbisCommentFieldKey.MUSICIP_PUID);
        tagFieldToOggField.put(TagFieldKey.AMAZON_ID, VorbisCommentFieldKey.ASIN);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMSTATUS);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMTYPE);
        tagFieldToOggField.put(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, VorbisCommentFieldKey.RELEASECOUNTRY);
        tagFieldToOggField.put(TagFieldKey.LYRICS, VorbisCommentFieldKey.LYRICS);
        tagFieldToOggField.put(TagFieldKey.IS_COMPILATION, VorbisCommentFieldKey.COMPILATION);
        tagFieldToOggField.put(TagFieldKey.ARTIST_SORT, VorbisCommentFieldKey.ARTISTSORT);
        tagFieldToOggField.put(TagFieldKey.ALBUM_ARTIST_SORT, VorbisCommentFieldKey.ALBUMARTISTSORT);
        tagFieldToOggField.put(TagFieldKey.ALBUM_SORT, VorbisCommentFieldKey.ALBUMSORT);
        tagFieldToOggField.put(TagFieldKey.TITLE_SORT, VorbisCommentFieldKey.TITLESORT);
        tagFieldToOggField.put(TagFieldKey.COMPOSER_SORT, VorbisCommentFieldKey.COMPOSERSORT);
    }

    private String vendor = "";
    //This is the vendor string that will be written if no other is supplied
    public static final String DEFAULT_VENDOR = "jaudiotagger";

    protected TagField createAlbumField(String content)
    {
        return new VorbisCommentTagField(getAlbumId(), content);
    }

    protected TagField createArtistField(String content)
    {
        return new VorbisCommentTagField(getArtistId(), content);
    }

    protected TagField createCommentField(String content)
    {
        return new VorbisCommentTagField(getCommentId(), content);
    }

    protected TagField createGenreField(String content)
    {
        return new VorbisCommentTagField(getGenreId(), content);
    }

    protected TagField createTitleField(String content)
    {
        return new VorbisCommentTagField(getTitleId(), content);
    }

    protected TagField createTrackField(String content)
    {
        return new VorbisCommentTagField(getTrackId(), content);
    }

    protected TagField createYearField(String content)
    {
        return new VorbisCommentTagField(getYearId(), content);
    }

    protected String getAlbumId()
    {
        return ALBUM.name();
    }

    protected String getArtistId()
    {
        return ARTIST.name();
    }

    protected String getCommentId()
    {
        return DESCRIPTION.name();
    }

    protected String getGenreId()
    {
        return GENRE.name();
    }

    protected String getTitleId()
    {
        return TITLE.name();
    }

    protected String getTrackId()
    {
        return TRACKNUMBER.name();
    }

    public String getVendor()
    {
        if (!this.vendor.trim().equals(""))
        {
            return vendor;
        }

        return DEFAULT_VENDOR;
    }

    protected String getYearId()
    {
        return DATE.toString();
    }

    public void setVendor(String vendor)
    {
        if (vendor == null)
        {
            this.vendor = "";
            return;
        }
        this.vendor = vendor;
    }

    protected boolean isAllowedEncoding(String enc)
    {
        return enc.equals(VorbisHeader.CHARSET_UTF_8);
    }

    public String toString()
    {
        return "OGG " + super.toString();
    }

    /**
     * Create Tag Field using generic key
     */
    @Override
    public TagField createTagField(TagFieldKey genericKey, String value)
    {
        return new VorbisCommentTagField(tagFieldToOggField.get(genericKey).name(), value);
    }

    /**
     * Create Tag Field using ogg key
     *
     * @param vorbisCommentFieldKey
     * @param value
     * @return
     */
    public TagField createTagField(VorbisCommentFieldKey vorbisCommentFieldKey, String value)
    {
        return new VorbisCommentTagField(vorbisCommentFieldKey.name(), value);
    }

    /**
     * Maps the generic key to the ogg key and return the list of values for this field
     *
     * @param genericKey
     */
    @Override
    public List get(TagFieldKey genericKey)
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            return null;
        }
        return super.get(vorbisCommentFieldKey.name());
    }

    /**
     * Retrieve the first value that exists for this vorbis comment key
     *
     * @param vorbisCommentKey
     * @return
     */
    public List get(VorbisCommentFieldKey vorbisCommentKey)
    {
        if (vorbisCommentKey == null)
        {
            return null;
        }
        return super.get(vorbisCommentKey.name());
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    public String getFirst(TagFieldKey genericKey)
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            return null;
        }
        return super.getFirst(vorbisCommentFieldKey.name());
    }

    /**
     * Retrieve the first value that exists for this vorbis comment key
     *
     * @param vorbisCommentKey
     * @return
     */
    public String getFirst(VorbisCommentFieldKey vorbisCommentKey)
    {
        if (vorbisCommentKey == null)
        {
            return null;
        }
        return super.getFirst(vorbisCommentKey.name());
    }

    /**
     * Delete fields with this generic key
     *
     * @param genericKey
     */
    public void deleteTagField(TagFieldKey genericKey)
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            return;
        }
        super.deleteField(vorbisCommentFieldKey.name());
    }
}

