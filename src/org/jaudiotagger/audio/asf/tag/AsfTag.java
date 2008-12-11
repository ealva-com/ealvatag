package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.reference.PictureTypes;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Tag implementation for ASF.<br>
 *
 * @author Christian Laireiter
 */
public final class AsfTag extends AbstractTag
{
    /**
     * Stores a list of field keys, which identify common fields.<br>
     */
    public final static Set<AsfFieldKey> COMMON_FIELDS;

    /**
     * List of {@link AsfFieldKey} items, identifying contents that are stored in the
     * content description chunk (or unit) of ASF files.
     */
    public final static Set<AsfFieldKey> DESCRIPTION_FIELDS;

    static EnumMap<TagFieldKey, AsfFieldKey> tagFieldToAsfField = new EnumMap<TagFieldKey, AsfFieldKey>(TagFieldKey.class);

    //Mapping from generic key to mp4 key
    static
    {
        tagFieldToAsfField.put(TagFieldKey.ARTIST, AsfFieldKey.AUTHOR);
        tagFieldToAsfField.put(TagFieldKey.ALBUM, AsfFieldKey.ALBUM);
        tagFieldToAsfField.put(TagFieldKey.TITLE, AsfFieldKey.TITLE);
        tagFieldToAsfField.put(TagFieldKey.TRACK, AsfFieldKey.TRACK);
        tagFieldToAsfField.put(TagFieldKey.YEAR, AsfFieldKey.YEAR);
        tagFieldToAsfField.put(TagFieldKey.GENRE, AsfFieldKey.GENRE);
        tagFieldToAsfField.put(TagFieldKey.COMMENT, AsfFieldKey.DESCRIPTION);
        tagFieldToAsfField.put(TagFieldKey.ALBUM_ARTIST, AsfFieldKey.ALBUM_ARTIST);
        tagFieldToAsfField.put(TagFieldKey.COMPOSER, AsfFieldKey.COMPOSER);
        tagFieldToAsfField.put(TagFieldKey.GROUPING, AsfFieldKey.GROUPING);
        tagFieldToAsfField.put(TagFieldKey.DISC_NO, AsfFieldKey.DISC_NO);
        tagFieldToAsfField.put(TagFieldKey.BPM, AsfFieldKey.BPM);
        tagFieldToAsfField.put(TagFieldKey.ENCODER, AsfFieldKey.ENCODER);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_ARTISTID, AsfFieldKey.MUSICBRAINZ_ARTISTID);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_RELEASEID, AsfFieldKey.MUSICBRAINZ_RELEASEID);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_TRACK_ID, AsfFieldKey.MUSICBRAINZ_TRACK_ID);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_DISC_ID, AsfFieldKey.MUSICBRAINZ_DISC_ID);
        tagFieldToAsfField.put(TagFieldKey.MUSICIP_ID, AsfFieldKey.MUSICIP_ID);
        tagFieldToAsfField.put(TagFieldKey.AMAZON_ID, AsfFieldKey.AMAZON_ID);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS, AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE, AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE);
        tagFieldToAsfField.put(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY, AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY);
        tagFieldToAsfField.put(TagFieldKey.LYRICS, AsfFieldKey.LYRICS);
        tagFieldToAsfField.put(TagFieldKey.IS_COMPILATION, AsfFieldKey.IS_COMPILATION);
        tagFieldToAsfField.put(TagFieldKey.ARTIST_SORT, AsfFieldKey.ARTIST_SORT);
        tagFieldToAsfField.put(TagFieldKey.ALBUM_ARTIST_SORT, AsfFieldKey.ALBUM_ARTIST_SORT);
        tagFieldToAsfField.put(TagFieldKey.ALBUM_SORT, AsfFieldKey.ALBUM_SORT);
        tagFieldToAsfField.put(TagFieldKey.TITLE_SORT, AsfFieldKey.TITLE_SORT);
        tagFieldToAsfField.put(TagFieldKey.COMPOSER_SORT, AsfFieldKey.COMPOSER_SORT);
        tagFieldToAsfField.put(TagFieldKey.COVER_ART, AsfFieldKey.COVER_ART);
        tagFieldToAsfField.put(TagFieldKey.ISRC, AsfFieldKey.ISRC);
        tagFieldToAsfField.put(TagFieldKey.CATALOG_NO, AsfFieldKey.CATALOG_NO);
        tagFieldToAsfField.put(TagFieldKey.BARCODE, AsfFieldKey.BARCODE);
        tagFieldToAsfField.put(TagFieldKey.RECORD_LABEL, AsfFieldKey.RECORD_LABEL);
        tagFieldToAsfField.put(TagFieldKey.LYRICIST, AsfFieldKey.LYRICIST);
        tagFieldToAsfField.put(TagFieldKey.CONDUCTOR, AsfFieldKey.CONDUCTOR);
        tagFieldToAsfField.put(TagFieldKey.REMIXER, AsfFieldKey.REMIXER);
        tagFieldToAsfField.put(TagFieldKey.MOOD, AsfFieldKey.MOOD);
        tagFieldToAsfField.put(TagFieldKey.MEDIA, AsfFieldKey.MEDIA);
        tagFieldToAsfField.put(TagFieldKey.URL_OFFICIAL_RELEASE_SITE, AsfFieldKey.URL_OFFICIAL_RELEASE_SITE);
        tagFieldToAsfField.put(TagFieldKey.URL_DISCOGS_RELEASE_SITE, AsfFieldKey.URL_DISCOGS_RELEASE_SITE);
        tagFieldToAsfField.put(TagFieldKey.URL_WIKIPEDIA_RELEASE_SITE, AsfFieldKey.URL_WIKIPEDIA_RELEASE_SITE);
        tagFieldToAsfField.put(TagFieldKey.URL_OFFICIAL_ARTIST_SITE, AsfFieldKey.URL_OFFICIAL_ARTIST_SITE);
        tagFieldToAsfField.put(TagFieldKey.URL_DISCOGS_ARTIST_SITE, AsfFieldKey.URL_DISCOGS_ARTIST_SITE);
        tagFieldToAsfField.put(TagFieldKey.URL_WIKIPEDIA_ARTIST_SITE, AsfFieldKey.URL_WIKIPEDIA_ARTIST_SITE);
        tagFieldToAsfField.put(TagFieldKey.LANGUAGE, AsfFieldKey.LANGUAGE);
        tagFieldToAsfField.put(TagFieldKey.KEY, AsfFieldKey.INITIAL_KEY);
    }

    static
    {
        COMMON_FIELDS = new HashSet<AsfFieldKey>();
        COMMON_FIELDS.add(AsfFieldKey.ALBUM);
        COMMON_FIELDS.add(AsfFieldKey.AUTHOR);
        COMMON_FIELDS.add(AsfFieldKey.DESCRIPTION);
        COMMON_FIELDS.add(AsfFieldKey.GENRE);
        COMMON_FIELDS.add(AsfFieldKey.TITLE);
        COMMON_FIELDS.add(AsfFieldKey.TRACK);
        COMMON_FIELDS.add(AsfFieldKey.YEAR);
        DESCRIPTION_FIELDS = new HashSet<AsfFieldKey>();
        DESCRIPTION_FIELDS.add(AsfFieldKey.AUTHOR);
        DESCRIPTION_FIELDS.add(AsfFieldKey.COPYRIGHT);
        DESCRIPTION_FIELDS.add(AsfFieldKey.DESCRIPTION);
        DESCRIPTION_FIELDS.add(AsfFieldKey.RATING);
        DESCRIPTION_FIELDS.add(AsfFieldKey.TITLE);
    }

    /**
     * Determines if the {@linkplain ContentDescriptor#getName() name} equals an {@link AsfFieldKey} which
     * is {@linkplain #DESCRIPTION_FIELDS listed} to be stored in the content description chunk.
     *
     * @param contentDesc Descriptor to test.
     * @return see description.
     */
    public static boolean storesDescriptor(ContentDescriptor contentDesc)
    {
        AsfFieldKey asfFieldKey = AsfFieldKey.getAsfFieldKey(contentDesc.getName());
        return DESCRIPTION_FIELDS.contains(asfFieldKey);
    }


    /**
     * @see #isCopyingFields()
     */
    private final boolean copyFields;

    /**
     * Creates an empty instance.
     */
    public AsfTag()
    {
        this(false);
    }

    /**
     * Creates an instance and sets the field conversion property.<br>
     *
     * @param copyFields look at {@link #isCopyingFields()}.
     */
    public AsfTag(boolean copyFields)
    {
        this.copyFields = copyFields;
    }


    /**
     * Creates an instance and copies the fields of the source into the own
     * structure.<br>
     *
     * @param source     source to read tag fields from.
     * @param copyFields look at {@link #isCopyingFields()}.
     * @throws UnsupportedEncodingException {@link TagField#getRawContent()} which may be called
     */
    public AsfTag(Tag source, boolean copyFields) throws UnsupportedEncodingException
    {
        this(copyFields);
        copyFrom(source);
    }


    /**
     * Creates a field for copyright and adds it.<br>
     *
     * @param copyRight copyright content
     */
    public void addCopyright(String copyRight)
    {
        add(createCopyrightField(copyRight));
    }

    /**
     * Creates a field for rating and adds it.<br>
     *
     * @param rating rating.
     */
    public void addRating(String rating)
    {
        add(createRatingField(rating));
    }

    /**
     * This method copies tag fields from the source.<br>
     *
     * @param source source to read tag fields from.
     * @throws UnsupportedEncodingException {@link TagField#getRawContent()} which may be called
     */
    private void copyFrom(Tag source) throws UnsupportedEncodingException
    {
        if (source == null)
        {
            throw new NullPointerException();
        }
        final Iterator<TagField> fieldIterator = source.getFields();
        // iterate over all fields 
        while (fieldIterator.hasNext())
        {
            TagField copy = copyFrom(fieldIterator.next());
            if (copy != null)
            {
                super.add(copy);
            }
        }
    }

    /**
     * If {@link #isCopyingFields()} is <code>true</code>,
     * Creates a copy of <code>source</code>, if its not empty-<br>
     * However, plain {@link TagField} objects can only be transformed into binary fields using their
     * {@link TagField#getRawContent()} method.<br>
     *
     * @param source source field to copy.
     * @return A copy, which is as close to the source as possible, or <code>null</code> if the field is empty
     *         (empty byte[] or blank string}.
     * @throws UnsupportedEncodingException upon {@link TagField#getRawContent()}.
     */
    //TODO Add support for cover art field
    private TagField copyFrom(TagField source)
    {
        TagField result = null;
        if (isCopyingFields())
        {
            // Get the ASF internal key, where it applies
            String internalId = source.getId();
            if (source instanceof TagTextField)
            {
                String content = ((TagTextField) source).getContent();
                result = new AsfTagTextField(internalId, content);
            }
            else if (source instanceof AsfTagField)
            {
                result = new AsfTagCoverField(((AsfTagCoverField) source).getDescriptor());
            }
            else if (source instanceof AsfTagField)
            {
                result = new AsfTagField(((AsfTagField) source).getDescriptor());
            }
            else
            {
                throw new RuntimeException("Unknown Asf Tag Field class:"+source.getClass());
            }
        }
        else
        {
            result = source;
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createAlbumField(String content)
    {
        return new AsfTagTextField(getAlbumId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createArtistField(String content)
    {
        return new AsfTagTextField(getArtistId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createCommentField(String content)
    {
        return new AsfTagTextField(getCommentId(), content);
    }

    /**
     * Creates a field for storing the copyright.<br>
     *
     * @param content Copyright value.
     * @return {@link AsfTagTextField}
     */
    public TagField createCopyrightField(String content)
    {
        return new AsfTagTextField(AsfFieldKey.COPYRIGHT.getFieldName(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createGenreField(String content)
    {
        return new AsfTagTextField(getGenreId(), content);
    }

    /**
     * Creates a field for storing the copyright.<br>
     *
     * @param content Rating value.
     * @return {@link AsfTagTextField}
     */
    public TagField createRatingField(String content)
    {
        return new AsfTagTextField(AsfFieldKey.RATING.getFieldName(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTagField(TagFieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return createTagField(tagFieldToAsfField.get(genericKey), value);
    }

    /**
     * Create Tag Field using asf key
     * <p/>
     * Uses the correct subclass for the key
     *
     * @param asfFieldKey
     * @param value
     * @return
     * @throws KeyNotFoundException
     * @throws FieldDataInvalidException
     */
    public TagField createTagField(AsfFieldKey asfFieldKey, String value) throws KeyNotFoundException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (asfFieldKey == null)
        {
            throw new KeyNotFoundException("key not found for value:" + value);
        }

        switch (asfFieldKey)
        {
            case COVER_ART:
                throw new UnsupportedOperationException("Cover Art cannot be created using this method");

            default:
                System.out.println("Creating with value:"+value);
                return new AsfTagTextField(asfFieldKey.getFieldName(), value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTitleField(String content)
    {
        return new AsfTagTextField(getTitleId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createTrackField(String content) throws FieldDataInvalidException
    {
        return new AsfTagTextField(getTrackId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagField createYearField(String content)
    {
        return new AsfTagTextField(getYearId(), content);
    }

    /**
     * Create artwork field
     *
     * @param data raw image data
     * @return
     */
    public TagField createArtworkField(byte[] data) throws FieldDataInvalidException
    {
        return new AsfTagCoverField(data, PictureTypes.DEFAULT_ID, null);
    }

    /**
     * Create artwork field
     *
     * @return
     */
    public TagField createArtworkField(Artwork artwork) throws FieldDataInvalidException
    {
        return new AsfTagCoverField(artwork.getBinaryData(), artwork.getPictureType(), artwork.getDescription());
    }

    /**
     * Removes all fields which are stored to the provided field key.
     *
     * @param fieldKey fields to remove.
     */
    public void deleteTagField(AsfFieldKey fieldKey)
    {
        super.deleteField(fieldKey.getFieldName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTagField(TagFieldKey tagFieldKey) throws KeyNotFoundException
    {
        if (tagFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        super.deleteField(tagFieldToAsfField.get(tagFieldKey).getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagField> get(TagFieldKey tagFieldKey) throws KeyNotFoundException
    {
        if (tagFieldKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.get(tagFieldToAsfField.get(tagFieldKey).getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAlbumId()
    {
        return AsfFieldKey.ALBUM.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getArtistId()
    {
        return AsfFieldKey.AUTHOR.getFieldName();
    }

    /**
     * This method iterates through all stored fields.<br>
     * This method can only be used if this class has been created with field conversion turned on.
     *
     * @param <F>
     * @return Iterator for iterating through ASF fields.
     */
    public <F extends AsfTagField> Iterator<F> getAsfFields()
    {
        if (!isCopyingFields())
        {
            throw new IllegalStateException("Since the field conversion is not enabled, this method cannot be executed");
        }
        final Iterator<TagField> it = getFields();
        return new Iterator<F>()
        {

            public boolean hasNext()
            {
                return it.hasNext();
            }

            @SuppressWarnings("unchecked")
            public F next()
            {
                return (F) it.next();
            }

            public void remove()
            {
                it.remove();
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommentId()
    {
        return AsfFieldKey.DESCRIPTION.getFieldName();
    }

    /**
     * Returns a list of stored copyrights.
     *
     * @return list of stored copyrights.
     */
    public List<TagField> getCopyright()
    {
        return get(AsfFieldKey.COPYRIGHT.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirst(TagFieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return super.getFirst(tagFieldToAsfField.get(genericKey).getFieldName());
    }

    public AsfTagField getFirstField(TagFieldKey genericKey) throws KeyNotFoundException
    {
        if (genericKey == null)
        {
            throw new KeyNotFoundException();
        }
        return (AsfTagField) super.getFirstField(tagFieldToAsfField.get(genericKey).getFieldName());
    }

    /**
     * Returns the Copyright.
     *
     * @return the Copyright.
     */
    public String getFirstCopyright()
    {
        return getFirst(AsfFieldKey.COPYRIGHT.getFieldName());
    }

    /**
     * Returns the Rating.
     *
     * @return the Rating.
     */
    public String getFirstRating()
    {
        return getFirst(AsfFieldKey.RATING.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getGenreId()
    {
        return AsfFieldKey.GENRE.getFieldName();
    }

    /**
     * Returns a list of stored ratings.
     *
     * @return list of stored ratings.
     */
    public List<TagField> getRating()
    {
        return get(AsfFieldKey.RATING.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTitleId()
    {
        return AsfFieldKey.TITLE.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTrackId()
    {
        return AsfFieldKey.TRACK.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getYearId()
    {
        return AsfFieldKey.YEAR.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAllowedEncoding(String enc)
    {
        return AsfHeader.ASF_CHARSET.name().equals(enc);
    }

    /**
     * If <code>true</code>, the {@link #copyFrom(TagField)} method creates a new {@link AsfTagField} instance and
     * copies the content from the source.<br>
     * This method is utilized by {@link #add(TagField)} and {@link #set(TagField)}.<br>
     * So if <code>true</code> it is ensured that the {@link AsfTag} instance has its own copies of fields, which cannot
     * be modified after assignment (which could pass some checks), and it just stores {@link AsfTagField} objects.<br>
     * Only then {@link #getAsfFields()} can work. otherwise {@link IllegalStateException} is thrown.
     *
     * @return state of field conversion.
     */
    public boolean isCopyingFields()
    {
        return this.copyFields;
    }

    /**
     * Check field is valid and can be added to this tag
     * @param field
     * @return
     */
    //TODO introduce this concept to all formats
    private boolean isValidField(TagField field)
    {
        if (field == null)
        {
            return false;
        }

        if (!(field instanceof AsfTagField))
        {
            return false;
        }

        if (field.isEmpty())
        {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO introduce copy idea to all formats
    public void add(TagField field)
    {
        if (isValidField(field))
        {
            //Copy only occUrs if flag set
            field = copyFrom(field);
            if (AsfFieldKey.isMultiValued(field.getId()))
            {
                super.add(field);
            }
            else
            {
                super.set(field);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO introduce copy idea to all formats
    public void set(TagField field)
    {
        if (isValidField(field))
        {
            //Copy only occors if flag set
            field = copyFrom(field);
            super.set(field);
        }
    }

    /**
     * Sets the copyright.<br>
     *
     * @param Copyright the copyright to set.
     */
    public void setCopyright(String Copyright)
    {
        set(createCopyrightField(Copyright));
    }

    /**
     * Sets the Rating.<br>
     *
     * @param rating the rating to set.
     */
    public void setRating(String rating)
    {
        set(createRatingField(rating));
    }

    /**
     * @return
     */
    public List<Artwork> getArtworkList()
    {
        List<TagField> coverartList = get(TagFieldKey.COVER_ART);
        List<Artwork> artworkList = new ArrayList<Artwork>(coverartList.size());

        for (TagField next : coverartList)
        {
            AsfTagCoverField coverArt = (AsfTagCoverField) next;
            Artwork artwork = new Artwork();
            artwork.setBinaryData(coverArt.getRawImageData());
            artwork.setMimeType(coverArt.getMimeType());
            artwork.setDescription(coverArt.getDescription());
            artwork.setPictureType(coverArt.getPictureType());
            artworkList.add(artwork);
        }
        return artworkList;
    }
}
