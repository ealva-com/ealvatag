package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.AsfHeader;
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
public final class AsfTag extends AbstractTag {
    /**
     * This iterator is used to iterator an {@link Iterator} with
     * {@link TagField} objects and returns them by casting to
     * {@link AsfTagField}.<br>
     * 
     * @author Christian Laireiter
     */
    private static class AsfFieldIterator implements Iterator<AsfTagField> {

        /**
         * source iterator.
         */
        private final Iterator<TagField> fieldIterator;

        /**
         * Creates an isntance.
         * 
         * @param iterator
         *            iterator to read from.
         */
        public AsfFieldIterator(final Iterator<TagField> iterator) {
            assert iterator != null;
            this.fieldIterator = iterator;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            return this.fieldIterator.hasNext();
        }

        /**
         * {@inheritDoc}
         */
        public AsfTagField next() {
            return (AsfTagField) this.fieldIterator.next();
        }

        /**
         * {@inheritDoc}
         */
        public void remove() {
            this.fieldIterator.remove();
        }
    }

    /**
     * Stores a list of field keys, which identify common fields.<br>
     */
    public final static Set<AsfFieldKey> COMMON_FIELDS;

    /**
     * This map contains the mapping from {@link TagFieldKey} to
     * {@link AsfFieldKey}.
     */
    private static final EnumMap<TagFieldKey, AsfFieldKey> TAGFIELD_TO_ASFFIELD;

    // Mapping from generic key to asf key
    static {
        TAGFIELD_TO_ASFFIELD = new EnumMap<TagFieldKey, AsfFieldKey>(
                TagFieldKey.class);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ARTIST, AsfFieldKey.AUTHOR);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ALBUM, AsfFieldKey.ALBUM);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.TITLE, AsfFieldKey.TITLE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.TRACK, AsfFieldKey.TRACK);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.YEAR, AsfFieldKey.YEAR);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.GENRE, AsfFieldKey.GENRE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.COMMENT, AsfFieldKey.DESCRIPTION);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ALBUM_ARTIST,
                AsfFieldKey.ALBUM_ARTIST);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.COMPOSER, AsfFieldKey.COMPOSER);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.GROUPING, AsfFieldKey.GROUPING);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.DISC_NO, AsfFieldKey.DISC_NO);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.BPM, AsfFieldKey.BPM);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ENCODER, AsfFieldKey.ENCODER);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_ARTISTID,
                AsfFieldKey.MUSICBRAINZ_ARTISTID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_RELEASEID,
                AsfFieldKey.MUSICBRAINZ_RELEASEID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID,
                AsfFieldKey.MUSICBRAINZ_RELEASEARTISTID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_TRACK_ID,
                AsfFieldKey.MUSICBRAINZ_TRACK_ID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_DISC_ID,
                AsfFieldKey.MUSICBRAINZ_DISC_ID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICIP_ID, AsfFieldKey.MUSICIP_ID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.AMAZON_ID, AsfFieldKey.AMAZON_ID);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_RELEASE_STATUS,
                AsfFieldKey.MUSICBRAINZ_RELEASE_STATUS);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_RELEASE_TYPE,
                AsfFieldKey.MUSICBRAINZ_RELEASE_TYPE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MUSICBRAINZ_RELEASE_COUNTRY,
                AsfFieldKey.MUSICBRAINZ_RELEASE_COUNTRY);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.LYRICS, AsfFieldKey.LYRICS);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.IS_COMPILATION,
                AsfFieldKey.IS_COMPILATION);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ARTIST_SORT, AsfFieldKey.ARTIST_SORT);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ALBUM_ARTIST_SORT,
                AsfFieldKey.ALBUM_ARTIST_SORT);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ALBUM_SORT, AsfFieldKey.ALBUM_SORT);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.TITLE_SORT, AsfFieldKey.TITLE_SORT);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.COMPOSER_SORT,
                AsfFieldKey.COMPOSER_SORT);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.COVER_ART, AsfFieldKey.COVER_ART);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.ISRC, AsfFieldKey.ISRC);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.CATALOG_NO, AsfFieldKey.CATALOG_NO);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.BARCODE, AsfFieldKey.BARCODE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.RECORD_LABEL,
                AsfFieldKey.RECORD_LABEL);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.LYRICIST, AsfFieldKey.LYRICIST);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.CONDUCTOR, AsfFieldKey.CONDUCTOR);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.REMIXER, AsfFieldKey.REMIXER);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MOOD, AsfFieldKey.MOOD);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.MEDIA, AsfFieldKey.MEDIA);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_OFFICIAL_RELEASE_SITE,
                AsfFieldKey.URL_OFFICIAL_RELEASE_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_DISCOGS_RELEASE_SITE,
                AsfFieldKey.URL_DISCOGS_RELEASE_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_WIKIPEDIA_RELEASE_SITE,
                AsfFieldKey.URL_WIKIPEDIA_RELEASE_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_OFFICIAL_ARTIST_SITE,
                AsfFieldKey.URL_OFFICIAL_ARTIST_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_DISCOGS_ARTIST_SITE,
                AsfFieldKey.URL_DISCOGS_ARTIST_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_WIKIPEDIA_ARTIST_SITE,
                AsfFieldKey.URL_WIKIPEDIA_ARTIST_SITE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.LANGUAGE, AsfFieldKey.LANGUAGE);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.KEY, AsfFieldKey.INITIAL_KEY);
        TAGFIELD_TO_ASFFIELD.put(TagFieldKey.URL_LYRICS_SITE, AsfFieldKey.URL_LYRICS_SITE);
    }

    static {
        COMMON_FIELDS = new HashSet<AsfFieldKey>();
        COMMON_FIELDS.add(AsfFieldKey.ALBUM);
        COMMON_FIELDS.add(AsfFieldKey.AUTHOR);
        COMMON_FIELDS.add(AsfFieldKey.DESCRIPTION);
        COMMON_FIELDS.add(AsfFieldKey.GENRE);
        COMMON_FIELDS.add(AsfFieldKey.TITLE);
        COMMON_FIELDS.add(AsfFieldKey.TRACK);
        COMMON_FIELDS.add(AsfFieldKey.YEAR);
    }

    /**
     * @see #isCopyingFields()
     */
    private final boolean copyFields;

    /**
     * Creates an empty instance.
     */
    public AsfTag() {
        this(false);
    }

    /**
     * Creates an instance and sets the field conversion property.<br>
     * 
     * @param copy
     *            look at {@link #isCopyingFields()}.
     */
    public AsfTag(final boolean copy) {
        super();
        this.copyFields = copy;
    }

    /**
     * Creates an instance and copies the fields of the source into the own
     * structure.<br>
     * 
     * @param source
     *            source to read tag fields from.
     * @param copy
     *            look at {@link #isCopyingFields()}.
     * @throws UnsupportedEncodingException
     *             {@link TagField#getRawContent()} which may be called
     */
    public AsfTag(final Tag source, final boolean copy)
            throws UnsupportedEncodingException {
        this(copy);
        copyFrom(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO introduce copy idea to all formats
    public void add(final TagField field) {
        if (isValidField(field)) {
            if (AsfFieldKey.isMultiValued(field.getId())) {
                super.add(copyFrom(field));
            } else {
                super.set(copyFrom(field));
            }
        }
    }

    /**
     * Creates a field for copyright and adds it.<br>
     * 
     * @param copyRight
     *            copyright content
     */
    public void addCopyright(final String copyRight) {
        add(createCopyrightField(copyRight));
    }

    /**
     * Creates a field for rating and adds it.<br>
     * 
     * @param rating
     *            rating.
     */
    public void addRating(final String rating) {
        add(createRatingField(rating));
    }

    /**
     * This method copies tag fields from the source.<br>
     * 
     * @param source
     *            source to read tag fields from.
     */
    private void copyFrom(final Tag source) {
        final Iterator<TagField> fieldIterator = source.getFields();
        // iterate over all fields
        while (fieldIterator.hasNext()) {
            final TagField copy = copyFrom(fieldIterator.next());
            if (copy != null) {
                super.add(copy);
            }
        }
    }

    /**
     * If {@link #isCopyingFields()} is <code>true</code>, Creates a copy of
     * <code>source</code>, if its not empty-<br>
     * However, plain {@link TagField} objects can only be transformed into
     * binary fields using their {@link TagField#getRawContent()} method.<br>
     * 
     * @param source
     *            source field to copy.
     * @return A copy, which is as close to the source as possible, or
     *         <code>null</code> if the field is empty (empty byte[] or blank
     *         string}.
     */
    private TagField copyFrom(final TagField source) {
        TagField result = null;
        if (isCopyingFields()) {
            if (source instanceof AsfTagField) {
                try {
                    result = (TagField) ((AsfTagField) source).clone();
                } catch (CloneNotSupportedException e) {
                    result = new AsfTagField(((AsfTagField) source)
                            .getDescriptor());
                }
            } else if (source instanceof TagTextField) {
                final String content = ((TagTextField) source).getContent();
                result = new AsfTagTextField(source.getId(), content);
            } else {
                throw new RuntimeException("Unknown Asf Tag Field class:" // NOPMD
                        // by
                        // Christian
                        // Laireiter
                        // on
                        // 5/9/09
                        // 5:44
                        // PM
                        + source.getClass());
            }
        } else {
            result = source;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createAlbumField(final String content) {
        return new AsfTagTextField(getAlbumId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createArtistField(final String content) {
        return new AsfTagTextField(getArtistId(), content);
    }

    /**
     * Creates an {@link AsfTagCoverField} from given artwork
     * 
     * @param artwork
     *            artwork to create a ASF field from.
     * 
     * @return ASF field capable of storing artwork.
     */
    public AsfTagCoverField createArtworkField(final Artwork artwork) {
        return new AsfTagCoverField(artwork.getBinaryData(), artwork
                .getPictureType(), artwork.getDescription(), artwork
                .getMimeType());
    }

    /**
     * Create artwork field
     * 
     * @param data
     *            raw image data
     * @return creates a default ASF picture field with default
     *         {@linkplain PictureTypes#DEFAULT_ID picture type}.
     */
    public AsfTagCoverField createArtworkField(final byte[] data) {
        return new AsfTagCoverField(data, PictureTypes.DEFAULT_ID, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createCommentField(final String content) {
        return new AsfTagTextField(getCommentId(), content);
    }

    /**
     * Creates a field for storing the copyright.<br>
     * 
     * @param content
     *            Copyright value.
     * @return {@link AsfTagTextField}
     */
    public AsfTagTextField createCopyrightField(final String content) {
        return new AsfTagTextField(AsfFieldKey.COPYRIGHT, content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createGenreField(final String content) {
        return new AsfTagTextField(getGenreId(), content);
    }

    /**
     * Creates a field for storing the copyright.<br>
     * 
     * @param content
     *            Rating value.
     * @return {@link AsfTagTextField}
     */
    public AsfTagTextField createRatingField(final String content) {
        return new AsfTagTextField(AsfFieldKey.RATING, content);
    }

    /**
     * Create tag text field using ASF key
     * <p/>
     * Uses the correct subclass for the key.<br>
     * 
     * @param asfFieldKey
     *            field key to create field for.
     * @param value
     *            string value for the created field.
     * @return text field with given content.
     */
    public AsfTagTextField createTagField(final AsfFieldKey asfFieldKey,
            final String value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (asfFieldKey == null) {
            throw new IllegalArgumentException(
                    ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        switch (asfFieldKey) {
        case COVER_ART:
            throw new UnsupportedOperationException(
                    "Cover Art cannot be created using this method");
        case BANNER_IMAGE:
            throw new UnsupportedOperationException(
                    "Banner Image cannot be created using this method");
        default:
            return new AsfTagTextField(asfFieldKey.getFieldName(), value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createTagField(final TagFieldKey genericKey,
            final String value) throws KeyNotFoundException,
            FieldDataInvalidException {
        if (value == null) {
            throw new IllegalArgumentException(
                    ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        if (genericKey == null) {
            throw new IllegalArgumentException(
                    ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        }
        final AsfFieldKey asfFieldKey = TAGFIELD_TO_ASFFIELD.get(genericKey);
        if (asfFieldKey == null) {
            throw new KeyNotFoundException("No ASF fieldkey for "
                    + genericKey.toString());
        }
        return createTagField(asfFieldKey, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createTitleField(final String content) {
        return new AsfTagTextField(getTitleId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createTrackField(final String content)
            throws FieldDataInvalidException {
        return new AsfTagTextField(getTrackId(), content);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagTextField createYearField(final String content) {
        return new AsfTagTextField(getYearId(), content);
    }

    /**
     * Removes all fields which are stored to the provided field key.
     * 
     * @param fieldKey
     *            fields to remove.
     */
    public void deleteTagField(final AsfFieldKey fieldKey) {
        super.deleteField(fieldKey.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTagField(final TagFieldKey tagFieldKey)
            throws KeyNotFoundException {
        if (tagFieldKey == null) {
            throw new KeyNotFoundException();
        }
        super.deleteField(TAGFIELD_TO_ASFFIELD.get(tagFieldKey).getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagField> get(final TagFieldKey tagFieldKey)
            throws KeyNotFoundException {
        if (tagFieldKey == null) {
            throw new KeyNotFoundException();
        }
        return super.get(TAGFIELD_TO_ASFFIELD.get(tagFieldKey).getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAlbumId() {
        return AsfFieldKey.ALBUM.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getArtistId() {
        return AsfFieldKey.AUTHOR.getFieldName();
    }

    /**
     * @return
     */
    public List<Artwork> getArtworkList() {
        final List<TagField> coverartList = get(TagFieldKey.COVER_ART);
        final List<Artwork> artworkList = new ArrayList<Artwork>(coverartList
                .size());

        for (final TagField next : coverartList) {
            final AsfTagCoverField coverArt = (AsfTagCoverField) next;
            final Artwork artwork = new Artwork();
            artwork.setBinaryData(coverArt.getRawImageData());
            artwork.setMimeType(coverArt.getMimeType());
            artwork.setDescription(coverArt.getDescription());
            artwork.setPictureType(coverArt.getPictureType());
            artworkList.add(artwork);
        }
        return artworkList;
    }

    /**
     * This method iterates through all stored fields.<br>
     * This method can only be used if this class has been created with field
     * conversion turned on.
     * 
     * @return Iterator for iterating through ASF fields.
     */
    public Iterator<AsfTagField> getAsfFields() {
        if (!isCopyingFields()) {
            throw new IllegalStateException(
                    "Since the field conversion is not enabled, this method cannot be executed");
        }
        return new AsfFieldIterator(getFields());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCommentId() {
        return AsfFieldKey.DESCRIPTION.getFieldName();
    }

    /**
     * Returns a list of stored copyrights.
     * 
     * @return list of stored copyrights.
     */
    public List<TagField> getCopyright() {
        return get(AsfFieldKey.COPYRIGHT.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirst(final TagFieldKey genericKey)
            throws KeyNotFoundException {
        if (genericKey == null) {
            throw new KeyNotFoundException();
        }
        return super.getFirst(TAGFIELD_TO_ASFFIELD.get(genericKey).getFieldName());
    }

    /**
     * Returns the Copyright.
     * 
     * @return the Copyright.
     */
    public String getFirstCopyright() {
        return getFirst(AsfFieldKey.COPYRIGHT.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AsfTagField getFirstField(final TagFieldKey genericKey)
            throws KeyNotFoundException {
        if (genericKey == null) {
            throw new KeyNotFoundException();
        }
        return (AsfTagField) super.getFirstField(TAGFIELD_TO_ASFFIELD.get(
                genericKey).getFieldName());
    }

    /**
     * Returns the Rating.
     * 
     * @return the Rating.
     */
    public String getFirstRating() {
        return getFirst(AsfFieldKey.RATING.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getGenreId() {
        return AsfFieldKey.GENRE.getFieldName();
    }

    /**
     * Returns a list of stored ratings.
     * 
     * @return list of stored ratings.
     */
    public List<TagField> getRating() {
        return get(AsfFieldKey.RATING.getFieldName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTitleId() {
        return AsfFieldKey.TITLE.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTrackId() {
        return AsfFieldKey.TRACK.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getYearId() {
        return AsfFieldKey.YEAR.getFieldName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAllowedEncoding(final String enc) {
        return AsfHeader.ASF_CHARSET.name().equals(enc);
    }

    /**
     * If <code>true</code>, the {@link #copyFrom(TagField)} method creates a
     * new {@link AsfTagField} instance and copies the content from the source.<br>
     * This method is utilized by {@link #add(TagField)} and
     * {@link #set(TagField)}.<br>
     * So if <code>true</code> it is ensured that the {@link AsfTag} instance
     * has its own copies of fields, which cannot be modified after assignment
     * (which could pass some checks), and it just stores {@link AsfTagField}
     * objects.<br>
     * Only then {@link #getAsfFields()} can work. otherwise
     * {@link IllegalStateException} is thrown.
     * 
     * @return state of field conversion.
     */
    public boolean isCopyingFields() {
        return this.copyFields;
    }

    /**
     * Check field is valid and can be added to this tag
     * 
     * @param field
     *            field to add
     * @return <code>true</code> if field may be added.
     */
    // TODO introduce this concept to all formats
    private boolean isValidField(final TagField field) {
        if (field == null) {
            return false;
        }

        if (!(field instanceof AsfTagField)) {
            return false;
        }

        if (field.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO introduce copy idea to all formats
    public void set(final TagField field) {
        if (isValidField(field)) {
            // Copy only occurs if flag set
            super.set(copyFrom(field));
        }
    }

    /**
     * Sets the copyright.<br>
     * 
     * @param Copyright
     *            the copyright to set.
     */
    public void setCopyright(final String Copyright) {
        set(createCopyrightField(Copyright));
    }

    /**
     * Sets the Rating.<br>
     * 
     * @param rating
     *            the rating to set.
     */
    public void setRating(final String rating) {
        set(createRatingField(rating));
    }
}
