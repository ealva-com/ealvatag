package ealvatag.tag.aiff;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.logging.Hex;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Key;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.Id3SupportingTag;
import ealvatag.tag.images.Artwork;

import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.checkArgNotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Wraps ID3Tag for most of its metadata.
 */
public class AiffTag implements TagFieldContainer, Id3SupportingTag {
  private List<ChunkSummary> chunkSummaryList = new ArrayList<>();
  private boolean isIncorrectlyAlignedTag = false;
  private boolean isExistingId3Tag = false;
  private AbstractID3v2Tag id3Tag;

  public AiffTag() {
  }

  @Override public boolean isReadOnly() {
    return id3Tag.isReadOnly();
  }

  public AiffTag(AbstractID3v2Tag t) {
    id3Tag = t;
  }

  public void addChunkSummary(ChunkSummary cs) {
    chunkSummaryList.add(cs);
  }

  public List<ChunkSummary> getChunkSummaryList() {
    return chunkSummaryList;
  }

  /**
   * Returns the ID3 tag
   */
  public AbstractID3v2Tag getID3Tag() {
    return id3Tag;
  }

  /**
   * Sets the ID3 tag
   */
  public void setID3Tag(AbstractID3v2Tag t) {
    id3Tag = t;
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return id3Tag.getSupportedFields();
  }

  /**
   * Determines whether the tag has no fields specified.<br>
   * <p>
   * <p>If there are no images we return empty if either there is no VorbisTag or if there is a
   * VorbisTag but it is empty
   *
   * @return <code>true</code> if tag contains no field.
   */
  @Override
  public boolean isEmpty() {
    return (id3Tag == null || id3Tag.isEmpty());
  }

  @Override
  public boolean hasField(FieldKey genericKey) {
    return id3Tag.hasField(genericKey);
  }

  @Override
  public boolean hasField(String id) {
    return id3Tag.hasField(id);
  }

  @Override public int getFieldCount(final Key genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getFieldCount(genericKey);
  }

  @Override
  public int getFieldCount() {
    return id3Tag.getFieldCount();
  }

  @Override
  public Tag setField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    TagField tagfield = createField(genericKey, values);
    setField(tagfield);
    return this;
  }

  @Override
  public Tag addField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    TagField tagfield = createField(genericKey, values);
    addField(tagfield);
    return this;
  }

  @Override
  public String getFirst(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, 0).or("");
  }

  @Override
  public String getFirst(String id) throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getFirst(id);
  }

  @Override public Optional<String> getValue(final FieldKey genericKey) throws IllegalArgumentException {
    return id3Tag.getValue(genericKey);
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    return id3Tag.getValue(genericKey, index);
  }

  @Override
  public String getFieldAt(FieldKey genericKey, int index) throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getValue(genericKey, 0).or("");
  }

  @Override
  public List<String> getAll(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getAll(genericKey);
  }

  @Override
  public Tag deleteField(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    id3Tag.deleteField(checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey"));
    return this;
  }

  @Override
  public Tag deleteField(final String id) throws IllegalArgumentException, UnsupportedFieldException {
    id3Tag.deleteField(id);  // this call will check parameters
    return this;
  }

  @Override
  public Tag setArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    id3Tag.setArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork"));
    return this;
  }

  @Override
  public Tag addArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    id3Tag.addArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork"));
    return this;
  }

  @Override
  public Optional<Artwork> getFirstArtwork() throws UnsupportedFieldException {
    return id3Tag.getFirstArtwork();
  }

  @Override
  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    return id3Tag.getArtworkList();
  }

  @Override
  public Tag deleteArtwork() throws UnsupportedFieldException {
    id3Tag.deleteArtwork();
    return this;
  }

  @Override
  public boolean hasCommonFields() {
    return id3Tag.hasCommonFields();
  }

  @Override
  public int getFieldCountIncludingSubValues() {
    return getFieldCount();
  }

  @Override
  public boolean setEncoding(Charset enc) throws FieldDataInvalidException {
    return id3Tag.setEncoding(enc);
  }

  @Override
  public TagField createField(FieldKey genericKey, String... value) throws IllegalArgumentException,
                                                                           UnsupportedFieldException,
                                                                           FieldDataInvalidException {
    return id3Tag.createField(genericKey, value);
  }

  @Override
  public TagField createArtwork(Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    return id3Tag.createArtwork(artwork);
  }

  @Override
  public ImmutableList<TagField> getFields(FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getFields(genericKey);
  }

  @Override
  public Iterator<TagField> getFields() {
    return id3Tag.getFields();
  }

  @Override
  public ImmutableList<TagField> getFields(String id) {
    return id3Tag.getFields(id);
  }

  @Override
  public Optional<TagField> getFirstField(String id) throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getFirstField(id);
  }

  @Override
  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return id3Tag.getFirstField(genericKey);
  }

  @Override
  public TagField createCompilationField(boolean value) throws UnsupportedFieldException {
    try {
      return createField(FieldKey.IS_COMPILATION, String.valueOf(value));
    } catch (FieldDataInvalidException e) {
      throw new RuntimeException(e);  // should never happen. If so, library misconfiguration
    }
  }

  @Override
  public void setField(TagField field) throws FieldDataInvalidException {
    id3Tag.setField(field);
  }

  @Override
  public void addField(TagField field) throws FieldDataInvalidException {
    id3Tag.addField(field);
  }

  public long getSizeOfID3TagIncludingChunkHeader() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return getSizeOfID3TagOnly() + ChunkHeader.CHUNK_HEADER_SIZE;
  }

  public long getSizeOfID3TagOnly() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return (id3Tag.getEndLocationInFile() - id3Tag.getStartLocationInFile());
  }

  /**
   * @return true if the file that this tag was written from already contains an ID3 chunk
   */
  public boolean isExistingId3Tag() {
    return isExistingId3Tag;
  }

  @SuppressWarnings("SameParameterValue")
  public void setExistingId3Tag(boolean isExistingId3Tag) {
    this.isExistingId3Tag = isExistingId3Tag;
  }

  public boolean equals(Object obj) {
    return id3Tag.equals(obj);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (ChunkSummary cs : chunkSummaryList) {
      sb.append(cs.toString()).append("\n");
    }
    if (id3Tag != null) {
      sb.append("Aiff ID3 Tag:\n");
      if (isExistingId3Tag()) {
        if (isIncorrectlyAlignedTag) {
          sb.append("\tincorrectly starts as odd byte\n");
        }
        sb.append("\tstartLocation:").append(Hex.asDecAndHex(getStartLocationInFileOfId3Chunk())).append("\n");
        sb.append("\tendLocation:").append(Hex.asDecAndHex(getEndLocationInFileOfId3Chunk())).append("\n");
      }
      sb.append(id3Tag.toString()).append("\n");
      return sb.toString();
    } else {
      return "tag:empty";
    }
  }

  public long getStartLocationInFileOfId3Chunk() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return id3Tag.getStartLocationInFile() - ChunkHeader.CHUNK_HEADER_SIZE;
  }

  public long getEndLocationInFileOfId3Chunk() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return id3Tag.getEndLocationInFile();
  }

  public boolean isIncorrectlyAlignedTag() {
    return isIncorrectlyAlignedTag;
  }

  @SuppressWarnings("SameParameterValue")
  public void setIncorrectlyAlignedTag(boolean isIncorrectlyAlignedTag) {
    this.isIncorrectlyAlignedTag = isIncorrectlyAlignedTag;
  }

}
