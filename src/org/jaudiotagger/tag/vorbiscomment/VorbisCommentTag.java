/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
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
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.datatype.Artwork;
import static org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey.ALBUM;
import static org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey.VENDOR;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * This is the logical representation of  Vorbis Comment Data
 */
public class VorbisCommentTag extends AbstractTag
{
    private static EnumMap<FieldKey, VorbisCommentFieldKey> tagFieldToOggField = new EnumMap<FieldKey, VorbisCommentFieldKey>(FieldKey.class);

    static
    {
        tagFieldToOggField.put(FieldKey.ARTIST, VorbisCommentFieldKey.ARTIST);
        tagFieldToOggField.put(FieldKey.ALBUM, VorbisCommentFieldKey.ALBUM);
        tagFieldToOggField.put(FieldKey.TITLE, VorbisCommentFieldKey.TITLE);
        tagFieldToOggField.put(FieldKey.TRACK, VorbisCommentFieldKey.TRACKNUMBER);
        tagFieldToOggField.put(FieldKey.YEAR, VorbisCommentFieldKey.DATE);
        tagFieldToOggField.put(FieldKey.GENRE, VorbisCommentFieldKey.GENRE);
        tagFieldToOggField.put(FieldKey.COMMENT, VorbisCommentFieldKey.COMMENT);
        tagFieldToOggField.put(FieldKey.ALBUM_ARTIST, VorbisCommentFieldKey.ALBUMARTIST);
        tagFieldToOggField.put(FieldKey.COMPOSER, VorbisCommentFieldKey.COMPOSER);
        tagFieldToOggField.put(FieldKey.GROUPING, VorbisCommentFieldKey.GROUPING);
        tagFieldToOggField.put(FieldKey.DISC_NO, VorbisCommentFieldKey.DISCNUMBER);
        tagFieldToOggField.put(FieldKey.BPM, VorbisCommentFieldKey.BPM);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_ARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASEID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASEARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_TRACKID);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_DISC_ID, VorbisCommentFieldKey.MUSICBRAINZ_DISCID);
        tagFieldToOggField.put(FieldKey.MUSICIP_ID, VorbisCommentFieldKey.MUSICIP_PUID);
        tagFieldToOggField.put(FieldKey.AMAZON_ID, VorbisCommentFieldKey.ASIN);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_STATUS, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMSTATUS);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMTYPE);
        tagFieldToOggField.put(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, VorbisCommentFieldKey.RELEASECOUNTRY);
        tagFieldToOggField.put(FieldKey.LYRICS, VorbisCommentFieldKey.LYRICS);
        tagFieldToOggField.put(FieldKey.IS_COMPILATION, VorbisCommentFieldKey.COMPILATION);
        tagFieldToOggField.put(FieldKey.ARTIST_SORT, VorbisCommentFieldKey.ARTISTSORT);
        tagFieldToOggField.put(FieldKey.ALBUM_ARTIST_SORT, VorbisCommentFieldKey.ALBUMARTISTSORT);
        tagFieldToOggField.put(FieldKey.ALBUM_SORT, VorbisCommentFieldKey.ALBUMSORT);
        tagFieldToOggField.put(FieldKey.TITLE_SORT, VorbisCommentFieldKey.TITLESORT);
        tagFieldToOggField.put(FieldKey.COMPOSER_SORT, VorbisCommentFieldKey.COMPOSERSORT);
        tagFieldToOggField.put(FieldKey.ENCODER, VorbisCommentFieldKey.VENDOR);     //Known as vendor in VorbisComment
        tagFieldToOggField.put(FieldKey.ISRC, VorbisCommentFieldKey.ISRC);
        tagFieldToOggField.put(FieldKey.BARCODE, VorbisCommentFieldKey.BARCODE);
        tagFieldToOggField.put(FieldKey.CATALOG_NO, VorbisCommentFieldKey.CATALOGNUMBER);
        tagFieldToOggField.put(FieldKey.RECORD_LABEL, VorbisCommentFieldKey.LABEL);
        tagFieldToOggField.put(FieldKey.LYRICIST, VorbisCommentFieldKey.LYRICIST);
        tagFieldToOggField.put(FieldKey.CONDUCTOR, VorbisCommentFieldKey.CONDUCTOR);
        tagFieldToOggField.put(FieldKey.REMIXER, VorbisCommentFieldKey.REMIXER);
        tagFieldToOggField.put(FieldKey.MOOD, VorbisCommentFieldKey.MOOD);
        tagFieldToOggField.put(FieldKey.MEDIA, VorbisCommentFieldKey.MEDIA);
        tagFieldToOggField.put(FieldKey.URL_DISCOGS_ARTIST_SITE, VorbisCommentFieldKey.URL_DISCOGS_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_DISCOGS_RELEASE_SITE, VorbisCommentFieldKey.URL_DISCOGS_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.URL_OFFICIAL_ARTIST_SITE, VorbisCommentFieldKey.URL_OFFICIAL_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_OFFICIAL_RELEASE_SITE, VorbisCommentFieldKey.URL_OFFICIAL_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_ARTIST_SITE);
        tagFieldToOggField.put(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_RELEASE_SITE);
        tagFieldToOggField.put(FieldKey.LANGUAGE, VorbisCommentFieldKey.LANGUAGE);
        tagFieldToOggField.put(FieldKey.KEY, VorbisCommentFieldKey.KEY);
        tagFieldToOggField.put(FieldKey.URL_LYRICS_SITE, VorbisCommentFieldKey.URL_LYRICS_SITE);
        tagFieldToOggField.put(FieldKey.TRACK_TOTAL, VorbisCommentFieldKey.TRACKTOTAL);
        tagFieldToOggField.put(FieldKey.DISC_TOTAL, VorbisCommentFieldKey.DISCTOTAL);

    }

    //This is the vendor string that will be written if no other is supplied. Should be the name of the software
    //that actually encoded the file in the first place.
    public static final String DEFAULT_VENDOR = "jaudiotagger";

    /**
     * Only used within Package, hidden because it doesnt set Vendor
     * which should be done when created by end user
     */
    VorbisCommentTag()
    {

    }

    /**
     * Use to construct a new tag properly initialized
     *
     * @return
     */
    public static VorbisCommentTag createNewTag()
    {
        VorbisCommentTag tag = new VorbisCommentTag();
        tag.setVendor(DEFAULT_VENDOR);
        return tag;
    }

    private String getAlbumId()
    {
        return ALBUM.name();
    }

    /**
     * @return the vendor, generically known as the encoder
     */
    public String getVendor()
    {
        return getFirst(VENDOR.name());
    }

    /**
     * Set the vendor, known as the encoder  generally
     * <p/>
     * We dont want this to be blank, when written to file this field is written to a different location
     * to all other fields but user of library can just reat it as another field
     *
     * @param vendor
     */
    public void setVendor(String vendor)
    {
        if (vendor == null)
        {
            vendor = DEFAULT_VENDOR;
        }
        super.setField(new VorbisCommentTagField(VENDOR.name(), vendor));
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
    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException,FieldDataInvalidException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return createTagField(tagFieldToOggField.get(genericKey), value);
    }

    /**
     * Create Tag Field using ogg key
     *
     * @param vorbisCommentFieldKey
     * @param value
     * @return
     */
    public TagField createTagField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws KeyNotFoundException,FieldDataInvalidException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }

        return new VorbisCommentTagField(vorbisCommentFieldKey.name(), value);
    }

    /**
     * Create Tag Field using ogg key
     * <p/>
     * This method is provided to allow you to create key of any value because VorbisComment allows
     * arbitary keys.
     *
     * @param vorbisCommentFieldKey
     * @param value
     * @return
     */
    public TagField createTagField(String vorbisCommentFieldKey, String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        return new VorbisCommentTagField(vorbisCommentFieldKey, value);
    }

    /**
     * Maps the generic key to the ogg key and return the list of values for this field
     *
     * @param genericKey
     */
    @Override
    public List<TagField> getFields(FieldKey genericKey) throws KeyNotFoundException
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.get(vorbisCommentFieldKey.name());
    }

    /**
     * Retrieve the first value that exists for this vorbis comment key
     *
     * @param vorbisCommentKey
     * @return
     */
    public List<TagField> get(VorbisCommentFieldKey vorbisCommentKey) throws KeyNotFoundException
    {
        if (vorbisCommentKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.get(vorbisCommentKey.name());
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    public String getFirst(FieldKey genericKey) throws KeyNotFoundException
    {
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFirst(vorbisCommentFieldKey.name());
    }

    /**
     * Retrieve the first value that exists for this vorbis comment key
     *
     * @param vorbisCommentKey
     * @return
     */
    public String getFirst(VorbisCommentFieldKey vorbisCommentKey) throws KeyNotFoundException
    {
        if (vorbisCommentKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFirst(vorbisCommentKey.name());
    }

    /**
     * Delete fields with this generic key
     *
     * @param genericKey
     */
    public void deleteField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
        deleteTagField(vorbisCommentFieldKey);
    }

    /**
     * Delete fields with this vorbisCommentFieldKey
     *
     * @param vorbisCommentFieldKey
     */
    public void deleteTagField(VorbisCommentFieldKey vorbisCommentFieldKey) throws KeyNotFoundException
    {
        if (vorbisCommentFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        super.deleteField(vorbisCommentFieldKey.name());
    }

    /**
     * Create artwork field
     * <p/>
     * Actually create two fields , the data field and the mimetype
     *
     * @param data     raw image data
     * @param mimeType mimeType of data
     *                 <p/>
     *                 TODO could possibly work out mimetype from data, but unlike mp4 there is nothing to restrict to only png
     *                 or jpeg images
     * @return
     */
    public void setArtworkField(byte[] data, String mimeType)
    {
        char[] testdata = Base64Coder.encode(data);
        String base64image = new String(testdata);
        VorbisCommentTagField dataField = new VorbisCommentTagField(VorbisCommentFieldKey.COVERART.name(), base64image);
        VorbisCommentTagField mimeField = new VorbisCommentTagField(VorbisCommentFieldKey.COVERARTMIME.name(), mimeType);

        setField(dataField);
        setField(mimeField);

    }

    /**
     * Retrieve artwork raw data
     *
     * @return
     */
    public byte[] getArtworkBinaryData()
    {
        String base64data = this.getFirst(VorbisCommentFieldKey.COVERART);
        byte[] rawdata = Base64Coder.decode(base64data.toCharArray());
        return rawdata;
    }

    /**
     * @return mimetype
     */
    public String getArtworkMimeType()
    {
        return this.getFirst(VorbisCommentFieldKey.COVERARTMIME);
    }

    /**
     * Is this tag empty
     * <p/>
     * <p>Overridden because check for size of one because there is always a vendor tag unless just
     * created an empty vorbis tag as part of flac tag in which case size could be zero
     *
     * @see org.jaudiotagger.tag.Tag#isEmpty()
     */
    public boolean isEmpty()
    {
        return fields.size() <= 1;
    }

    /**
     * Add Field
     * <p/>
     * <p>Overidden because there can only be one vendor set
     *
     * @param field
     */
    public void addField(TagField field)
    {
        if (field.getId().equals(VorbisCommentFieldKey.VENDOR.name()))
        {
            super.setField(field);
        }
        else
        {
            super.addField(field);
        }
    }

     public TagField getFirstField(FieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return getFirstField(tagFieldToOggField.get(genericKey).name());
    }

    public List<Artwork> getArtworkList()
    {
        List<Artwork>  artworkList  = new ArrayList<Artwork>(1);

        if(getArtworkBinaryData()!=null & getArtworkBinaryData().length>0)
        {
            Artwork artwork=new Artwork();
            artwork.setMimeType(getArtworkMimeType());
            artwork.setBinaryData(getArtworkBinaryData());
            artworkList.add(artwork);
        }
        return artworkList;
    }

    /**
     * Create artwork field
     *
     * Not supported because reuire two fields to be created use
     * @return
     */
    public TagField createField(Artwork artwork) throws FieldDataInvalidException
    {
        throw new UnsupportedOperationException("Please use setField instead");
    }

    /**
     * Create artwork field
     *
     * Actually sets two fields
     *
     * @return
     */
    @Override
    public void setField(Artwork artwork) throws FieldDataInvalidException
    {
        char[] testdata = Base64Coder.encode(artwork.getBinaryData());
  		String base64image = new String(testdata);
   	    TagField imageTagField  = createTagField(VorbisCommentFieldKey.COVERART, base64image);
   		TagField imageTypeField = createTagField(VorbisCommentFieldKey.COVERARTMIME, artwork.getMimeType());

        this.setField(imageTagField);
        this.setField(imageTypeField);
    }

     /**
     * Delete all instance of artwork Field
     *
     * @throws KeyNotFoundException
     */
    public void deleteArtworkField() throws KeyNotFoundException
    {
        this.deleteTagField(VorbisCommentFieldKey.COVERART);
        this.deleteTagField(VorbisCommentFieldKey.COVERARTMIME);
    }
}

