package ealvatag.tag.flac;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ealvatag.utils.StandardCharsets;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Key;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.reference.PictureTypes;
import ealvatag.tag.vorbiscomment.ContainsVorbisCommentField;
import ealvatag.tag.vorbiscomment.VorbisCommentFieldKey;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;

import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.CANNOT_BE_NULL_OR_EMPTY;
import static ealvatag.utils.Check.checkArgNotNull;
import static ealvatag.utils.Check.checkArgNotNullOrEmpty;
import static ealvatag.utils.Check.checkVarArg0NotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Flac uses Vorbis Comment for most of its metadata and a Flac Picture Block for images
 * <p>
 * <p>
 * This class enscapulates the items into a single tag
 */
public class FlacTag implements Tag, ContainsVorbisCommentField {
  private VorbisCommentTag tag = null;
  private List<MetadataBlockDataPicture> images = new ArrayList<>();
  private final boolean readOnly;

  public FlacTag() {
    this(VorbisCommentTag.createNewTag(), new ArrayList<MetadataBlockDataPicture>(), false);
  }

  public FlacTag(VorbisCommentTag tag, List<MetadataBlockDataPicture> images, final boolean readOnly) {
    this.tag = tag;
    this.images = images;
    this.readOnly = readOnly;
  }

  @Override public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * @return images
   */
  public List<MetadataBlockDataPicture> getImages() {
    return images;
  }

  /**
   * @return the vorbis tag (this is what handles text metadata)
   */
  public VorbisCommentTag getVorbisCommentTag() {
    return tag;
  }

  @Override
  public Tag setField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    if (genericKey == FieldKey.ALBUM_ARTIST) {
      TagOptionSingleton.getInstance().getVorbisAlbumArtistSaveOptions().setField(this, genericKey, checkVarArg0NotNull(values));
    } else {
      TagField tagfield = createField(genericKey, values);
      setField(tagfield);
    }
    return this;
  }

  @Override
  public Tag addField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    String value = checkVarArg0NotNull(values);
    if (genericKey == FieldKey.ALBUM_ARTIST) {
      TagOptionSingleton.getInstance().getVorbisAlbumArtistSaveOptions().addField(this, genericKey, value);
    } else {
      TagField tagfield = createField(genericKey, value);
      addField(tagfield);
    }
    return this;
  }

  public Tag deleteField(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (genericKey.equals(FieldKey.COVER_ART)) {
      images.clear();
    } else {
      tag.deleteField(genericKey);
    }
    return this;
  }

  public Tag deleteField(final String id) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNullOrEmpty(id, CANNOT_BE_NULL_OR_EMPTY, "id");
    if (id.equals(FieldKey.COVER_ART.name())) {
      images.clear();
    } else {
      tag.deleteField(id);
    }
    return this;
  }

  public ImmutableList<TagField> getFields(String id) {
    if (id.equals(FieldKey.COVER_ART.name())) {
      ImmutableList.Builder<TagField> builder = ImmutableList.builder();
      builder.addAll(images);
      return builder.build();
    } else {
      return tag.getFields(id);
    }
  }

  public ImmutableList<TagField> getFields(FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    if (genericKey.equals(FieldKey.COVER_ART)) {
      ImmutableList.Builder<TagField> builder = ImmutableList.builder();
      builder.addAll(images);
      return builder.build();
    } else {
      return tag.getFields(genericKey);
    }
  }

  public List<String> getAll(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    if (genericKey == FieldKey.COVER_ART) {
      throw new UnsupportedFieldException(genericKey.name());
    } else {
      return tag.getAll(genericKey);
    }
  }

  //TODO addField images to iterator
  public Iterator<TagField> getFields() {
    return tag.getFields();
  }

  public String getFirst(String id) throws IllegalArgumentException, UnsupportedFieldException {
    if (id.equals(FieldKey.COVER_ART.name())) {
      throw new UnsupportedFieldException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD);
    } else {
      return tag.getFirst(id);
    }
  }

  public String getFirst(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, 0).or("");
  }

  @Override public Optional<String> getValue(final FieldKey genericKey) throws IllegalArgumentException {
    return getValue(genericKey, 0);
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    if (FieldKey.COVER_ART.equals(genericKey)) {
      throw new UnsupportedFieldException(genericKey.name());
    } else {
      return tag.getValue(genericKey, index);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @throws UnsupportedFieldException if the {@link FieldKey} is {@link FieldKey#COVER_ART}
   */
  public String getFieldAt(FieldKey genericKey, int index) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, index).or("");
  }

  public Optional<TagField> getFirstField(String id) throws IllegalArgumentException, UnsupportedFieldException {
    if (FieldKey.COVER_ART.name().equals(id)) {
      if (images.size() > 0) {
        return Optional.<TagField>fromNullable(images.get(0));
      } else {
        return Optional.absent();
      }
    } else {
      return tag.getFirstField(id);
    }
  }

  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (genericKey == FieldKey.COVER_ART) {
      return getFirstField(FieldKey.COVER_ART.name());
    } else {
      return tag.getFirstField(genericKey);
    }
  }

  public boolean hasCommonFields() {
    return tag.hasCommonFields();
  }

  /**
   * @param genericKey
   *
   * @return
   */
  public boolean hasField(FieldKey genericKey) {
    if (genericKey == FieldKey.COVER_ART) {
      return images.size() > 0;
    } else {
      return tag.hasField(genericKey);
    }
  }

  public boolean hasField(String id) {
    if (id.equals(FieldKey.COVER_ART.name())) {
      return images.size() > 0;
    } else {
      return tag.hasField(id);
    }
  }

  /**
   * Determines whether the tag has no fields specified.<br>
   * <p>
   * <p>If there are no images we return empty if either there is no VorbisTag or if there is a
   * VorbisTag but it is empty
   *
   * @return <code>true</code> if tag contains no field.
   */
  public boolean isEmpty() {
    return (tag == null || tag.isEmpty()) && images.size() == 0;
  }

  public int getFieldCount() {
    return tag.getFieldCount() + images.size();
  }

  public int getFieldCountIncludingSubValues() {
    return getFieldCount();
  }

  @Override
  public boolean setEncoding(Charset enc) throws FieldDataInvalidException {
    return tag.setEncoding(enc);
  }

  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    List<Artwork> artworkList = new ArrayList<Artwork>(images.size());

    for (MetadataBlockDataPicture coverArt : images) {
      Artwork artwork = ArtworkFactory.createArtworkFromMetadataBlockDataPicture(coverArt);
      artworkList.add(artwork);
    }
    return artworkList;
  }

  public Optional<Artwork> getFirstArtwork() throws UnsupportedFieldException {
    List<Artwork> artwork = getArtworkList();
    if (artwork.size() > 0) {
      return Optional.of(artwork.get(0));
    }
    return Optional.absent();
  }

  public Tag deleteArtwork() throws UnsupportedFieldException {
    return deleteField(FieldKey.COVER_ART);
  }

  public TagField createArtwork(Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    if (artwork.isLinked()) {
      return new MetadataBlockDataPicture(
          artwork.getImageUrl().getBytes(StandardCharsets.ISO_8859_1),
          artwork.getPictureType(),
          MetadataBlockDataPicture.IMAGE_IS_URL,
          "",
          0,
          0,
          0,
          0);
    } else {
      if (!artwork.setImageFromData()) {
        throw new FieldDataInvalidException("Unable to createField buffered image from the image");
      }

      return new MetadataBlockDataPicture(artwork.getBinaryData(),
                                          artwork.getPictureType(),
                                          artwork.getMimeType(),
                                          artwork.getDescription(),
                                          artwork.getWidth(),
                                          artwork.getHeight(),
                                          0,
                                          0);
    }
  }

  public Tag setArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    setField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));
    return this;
  }

  public Tag addArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    addField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));
    return this;
  }

  /**
   * @param field
   *
   * @throws FieldDataInvalidException
   */
  public void setField(TagField field) throws FieldDataInvalidException {
    if (field instanceof MetadataBlockDataPicture) {
      if (images.size() == 0) {
        images.add(0, (MetadataBlockDataPicture)field);
      } else {
        images.set(0, (MetadataBlockDataPicture)field);
      }
    } else {
      tag.setField(field);
    }
  }

  public void addField(TagField field) throws FieldDataInvalidException {
    if (field instanceof MetadataBlockDataPicture) {
      images.add((MetadataBlockDataPicture)field);
    } else {
      tag.addField(field);
    }
  }

  public TagField createField(FieldKey genericKey, String... value) throws IllegalArgumentException,
                                                                           UnsupportedFieldException,
                                                                           FieldDataInvalidException {
    if (genericKey.equals(FieldKey.COVER_ART)) {
      throw new UnsupportedFieldException(genericKey.name());
    } else {
      return tag.createField(genericKey, value);
    }
  }

  public TagField createCompilationField(boolean value) throws UnsupportedFieldException {
    return tag.createCompilationField(value);
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return tag.getSupportedFields();
  }

  public void setField(String vorbisCommentKey, String value) throws IllegalArgumentException,
                                                                     UnsupportedFieldException,
                                                                     FieldDataInvalidException {
    TagField tagfield = createField(vorbisCommentKey, value);
    setField(tagfield);
  }

  public void addField(String vorbisCommentKey, String value) throws IllegalArgumentException,
                                                                     UnsupportedFieldException,
                                                                     FieldDataInvalidException {
    addField(createField(vorbisCommentKey, value));
  }

  public TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws UnsupportedFieldException,
                                                                                                FieldDataInvalidException {
    if (vorbisCommentFieldKey.equals(VorbisCommentFieldKey.COVERART)) {
      throw new UnsupportedFieldException(vorbisCommentFieldKey.name());
    }
    return tag.createField(vorbisCommentFieldKey, value);
  }

  public TagField createField(String vorbisCommentFieldKey, String value) {
    if (vorbisCommentFieldKey.equals(VorbisCommentFieldKey.COVERART.getFieldName())) {
      throw new UnsupportedFieldException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD);
    }
    return tag.createField(vorbisCommentFieldKey, value);
  }

  public TagField createArtworkField(byte[] imageData,
                                     int pictureType,
                                     String mimeType,
                                     String description,
                                     int width,
                                     int height,
                                     int colourDepth,
                                     int indexedColouredCount) throws FieldDataInvalidException {
    if (imageData == null) {
      throw new FieldDataInvalidException("ImageData cannot be null");
    }
    return new MetadataBlockDataPicture(imageData,
                                        pictureType,
                                        mimeType,
                                        description,
                                        width,
                                        height,
                                        colourDepth,
                                        indexedColouredCount);
  }

  /**
   * Create Link to Image File, not recommended because if either flac or image file is moved link
   * will be broken.
   *
   * @param url
   *
   * @return
   */
  public TagField createLinkedArtworkField(String url) {
    //Add to image list
    return new MetadataBlockDataPicture(url.getBytes(StandardCharsets.ISO_8859_1),
                                        PictureTypes.DEFAULT_ID,
                                        MetadataBlockDataPicture.IMAGE_IS_URL,
                                        "",
                                        0,
                                        0,
                                        0,
                                        0);
  }

  /**
   * @param vorbisFieldKey
   *
   * @return
   */
  public boolean hasField(VorbisCommentFieldKey vorbisFieldKey) {
    return tag.hasField(vorbisFieldKey);
  }

  public String toString() {
    return "FLAC " + getVorbisCommentTag();
  }

  @Override public int getFieldCount(final Key genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    if (FieldKey.COVER_ART.name().equals(genericKey.name())) {
      return images.size();
    }
    return tag.getFieldCount(genericKey);
  }

}
