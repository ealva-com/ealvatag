package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.audio.generic.AbstractTag;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.field.*;
import org.jaudiotagger.logging.ErrorMessage;

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
        tagFieldToAsfField.put(TagFieldKey.ARTIST, AsfFieldKey.ARTIST);
        tagFieldToAsfField.put(TagFieldKey.ALBUM, AsfFieldKey.ALBUM);
        tagFieldToAsfField.put(TagFieldKey.TITLE, AsfFieldKey.TITLE);
        tagFieldToAsfField.put(TagFieldKey.TRACK, AsfFieldKey.TRACK);
        tagFieldToAsfField.put(TagFieldKey.YEAR, AsfFieldKey.YEAR);
        tagFieldToAsfField.put(TagFieldKey.GENRE, AsfFieldKey.GENRE);
        tagFieldToAsfField.put(TagFieldKey.COMMENT, AsfFieldKey.COMMENT);
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
    }

    static
    {
        COMMON_FIELDS = new HashSet<AsfFieldKey>();
        COMMON_FIELDS.add(AsfFieldKey.ALBUM);
        COMMON_FIELDS.add(AsfFieldKey.ARTIST);
        COMMON_FIELDS.add(AsfFieldKey.COMMENT);
        COMMON_FIELDS.add(AsfFieldKey.GENRE);
        COMMON_FIELDS.add(AsfFieldKey.TITLE);
        COMMON_FIELDS.add(AsfFieldKey.TRACK);
        COMMON_FIELDS.add(AsfFieldKey.YEAR);
        DESCRIPTION_FIELDS = new HashSet<AsfFieldKey>();
        DESCRIPTION_FIELDS.add(AsfFieldKey.ARTIST);
        DESCRIPTION_FIELDS.add(AsfFieldKey.COPYRIGHT);
        DESCRIPTION_FIELDS.add(AsfFieldKey.COMMENT);
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
     * @see #isConvertingFields()    
     */
    private final boolean convertFields;

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
     * @param fieldConversion look at {@link #isConvertingFields()}.
     */
    public AsfTag(boolean fieldConversion)
    {
        this.convertFields = fieldConversion;
    }

    /**
     * Creates an instance an copies the fields of the source into the own
     * structure.<br>
     * 
     * @param source source to read tag fields from.
     */
    public AsfTag(Tag source)
    {
        this(false);
        try
        {
            copyFrom(source);
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("This should have been impossible, by class design. Check the Code!!!", e);
        }
    }

    /**
     * Creates an instance an copies the fields of the source into the own
     * structure.<br>
     * 
     * @param source source to read tag fields from.
     * @param fieldConversion look at {@link #isConvertingFields()}.
     * @throws UnsupportedEncodingException {@link TagField#getRawContent()} which may be called 
     */
    public AsfTag(Tag source, boolean fieldConversion) throws UnsupportedEncodingException
    {
        this(fieldConversion);
        copyFrom(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(TagField field)
    {
        try
        {
            TagField copy = copyFrom(field);
            if (copy != null)
            {
                if (AsfFieldKey.isMultiValued(copy.getId()))
                {
                    super.add(copy);
                }
                else
                {
                    super.set(copy);
                }
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Cannot get raw content.", e);
        }
    }

    /**
     * Creates a field for copyright and adds it.<br>
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
     * If {@link #isConvertingFields()} is <code>true</code>,
     * yCreates a copy of <code>source</code>, if its not empty-<br>
     * However, plain {@link TagField} objects can only be transformed into binary fields using their 
     * {@link TagField#getRawContent()} method.<br>
     * 
     * @param source source field to copy.
     * @return A copy, which is as close to the source as possible, or <code>null</code> if the field is empty 
     *         (empty byte[] or blank string}.
     * @throws UnsupportedEncodingException upon {@link TagField#getRawContent()}.
     */
    private TagField copyFrom(TagField source) throws UnsupportedEncodingException
    {
        TagField result = null;
        if (isConvertingFields())
        {
            // Get the ASF internal key, where it applies
            String internalId = source.getId();
            if (source instanceof TagTextField)
            {
                String content = ((TagTextField) source).getContent();
                if (!Utils.isBlank(content))
                {
                    // If its a non blank text field, value copy is simple
                    result = new AsfTagTextField(internalId, content);
                }
            }
            else if (source instanceof AsfTagField)
            {
                if (!((AsfTagField) source).isEmpty())
                {
                    // If its a AsfTagField, the value copy is simple too, since all possible data types and their content
                    // are managed by ContentDescriptor objects. 
                    result = new AsfTagField(((AsfTagField) source).getDescriptor());
                }
            }
            else
            {
                /*
                 * Well, now what to do with Tagfields. The interface only allows access to raw content (byte[]).
                 * It depends on the audio formats implementation whether there is for example the raw picture data,
                 * or additionally the field identifier contained within.
                 */
                /*
                 * Well simply convert take it as plain binary data for now, maybe there will be some changes in future. 
                 */
                byte[] content = source.getRawContent();
                if (content != null && content.length > 0)
                {
                    ContentDescriptor desc = new ContentDescriptor(internalId, ContentDescriptor.TYPE_BINARY);
                    desc.setBinaryValue(content);
                    result = new AsfTagField(desc);
                }
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
    public TagField createTagField(AsfFieldKey asfFieldKey, String value) throws KeyNotFoundException, FieldDataInvalidException
    {
        if (value == null)
        {
            throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (asfFieldKey == null)
        {
            throw new KeyNotFoundException("key not found for value:"+value);
        }
        
        switch (asfFieldKey)
        {
            default:
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
     * Removes all fields which are stored to the provided field key. 
     * @param fieldKey fields to remove.
     */
    public void deleteTagField(AsfFieldKey fieldKey)
    {
        super.deleteField(fieldKey.getFieldName());
    }


    /**
     * Removes all fields with the specified field key.<br>
     * 
     * @param fieldKey the key of the fields to remove.<br> 
     */
    public void deleteTagField(String fieldKey)
    {
        super.deleteField(fieldKey);
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
    public List<TagField> get(TagFieldKey id) throws KeyNotFoundException
    {
        if (id == null)
        {
            throw new KeyNotFoundException();
        }
        return super.get(tagFieldToAsfField.get(id).getFieldName());
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
        return AsfFieldKey.ARTIST.getFieldName();
    }

    /**
     * This method iterates through all stored fields.<br>
     * This method can only be used if this class has been created with field conversion turned on.
     * @param <F> 
     * @return Iterator for iterating through ASF fields.
     */
    public <F extends AsfTagField> Iterator<F> getAsfFields()
    {
        if (!isConvertingFields())
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
        return AsfFieldKey.COMMENT.getFieldName();
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
    public boolean isConvertingFields()
    {
        return this.convertFields;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(TagField field)
    {
        try
        {
            TagField copy = copyFrom(field);
            if (copy != null)
            {
                super.set(copy);
            }
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Cannot get raw content.", e);
        }
    }

    /**
     * Sets the copyright.<br>
     * @param Copyright the copyright to set.
     */
    public void setCopyright(String Copyright)
    {
        set(createCopyrightField(Copyright));
    }

    /**
     * Sets the Rating.<br>
     * @param rating the rating to set.
     */
    public void setRating(String rating)
    {
        set(createRatingField(rating));
    }

}
