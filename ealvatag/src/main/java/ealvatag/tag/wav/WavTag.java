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
package ealvatag.tag.wav;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.audio.wav.WavOptions;
import ealvatag.logging.Hex;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Key;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.Id3SupportingTag;
import ealvatag.tag.images.Artwork;

import static com.ealva.ealvalog.LogLevel.INFO;
import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.checkArgNotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represent wav metadata found in a Wav file
 * <p>
 * This can come from LIST INFO chunk or ID3 tag, LIST INFO can only contain a subset of what can be held in an ID3v2
 * tag,
 * <p>
 * The default is that ID3 takes precedence if it exists
 */
public class WavTag implements TagFieldContainer, Id3SupportingTag {
  private static final JLogger LOG = JLoggers.get(WavTag.class, EalvaTagLog.MARKER);

  private static final String NULL = "\0";

  private List<ChunkSummary> chunkSummaryList = new ArrayList<>();
  private boolean isIncorrectlyAlignedTag = false;
  private boolean isExistingId3Tag = false;
  private boolean isExistingInfoTag = false;
  private WavInfoTag infoTag;
  private AbstractID3v2Tag id3Tag;
  private WavOptions wavOptions;

  public static AbstractID3v2Tag createDefaultID3Tag() {
    return TagOptionSingleton.createDefaultID3Tag();
  }

  @Override public boolean isReadOnly() {
    return getActiveTag().isReadOnly();
  }

  public WavTag(WavOptions wavOptions) {
    this.wavOptions = wavOptions;
  }

  public void addChunkSummary(ChunkSummary cs) {
    chunkSummaryList.add(cs);
  }

  public List<ChunkSummary> getChunkSummaryList() {
    return chunkSummaryList;
  }

  public WavInfoTag getInfoTag() {
    return infoTag;
  }

  public boolean isInfoTag() {
    return infoTag != null;
  }

  public void setInfoTag(WavInfoTag infoTag) {
    this.infoTag = infoTag;
  }

  public AbstractID3v2Tag getID3Tag() {
    return id3Tag;
  }

  public boolean isID3Tag() {
    return id3Tag != null;
  }

  /**
   * Sets the ID3 tag
   */
  public void setID3Tag(AbstractID3v2Tag t) {
    id3Tag = t;
  }

  public boolean equals(Object obj) {
    return getActiveTag().equals(obj); // TODO: 1/20/17 Wrong!
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (ChunkSummary cs : chunkSummaryList) {
      sb.append(cs.toString()).append("\n");
    }

    if (id3Tag != null) {
      sb.append("Wav ID3 Tag:\n");
      if (isExistingId3Tag()) {
        sb.append("\tstartLocation:").append(Hex.asDecAndHex(getStartLocationInFileOfId3Chunk())).append("\n");
        sb.append("\tendLocation:").append(Hex.asDecAndHex(getEndLocationInFileOfId3Chunk())).append("\n");
      }
      sb.append(id3Tag.toString()).append("\n");
    }
    if (infoTag != null) {
      sb.append(infoTag.toString()).append("\n");
    }
    return sb.toString();
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

  public TagFieldContainer getActiveTag() {
    switch (wavOptions) {
      case READ_ID3_ONLY:
      case READ_ID3_ONLY_AND_SYNC:
        return id3Tag;

      case READ_INFO_ONLY:
      case READ_INFO_ONLY_AND_SYNC:
        return infoTag;

      case READ_ID3_UNLESS_ONLY_INFO:
      case READ_ID3_UNLESS_ONLY_INFO_AND_SYNC:
        if (isExistingId3Tag() || !isExistingInfoTag()) {
          return id3Tag;
        } else {
          return infoTag;
        }

      case READ_INFO_UNLESS_ONLY_ID3:
      case READ_INFO_UNLESS_ONLY_ID3_AND_SYNC:
        if (isExistingInfoTag() || !isExistingId3Tag()) {
          return infoTag;
        } else {
          return id3Tag;
        }

      default:
        return id3Tag;

    }
  }

  /**
   * @return true if the file that this tag was written from already contains an ID3 chunk
   */
  public boolean isExistingId3Tag() {
    return isExistingId3Tag;
  }

  public void setExistingId3Tag(boolean isExistingId3Tag) {
    this.isExistingId3Tag = isExistingId3Tag;
  }

  /**
   * @return true if the file that this tag read from already contains a LISTINFO chunk
   */
  public boolean isExistingInfoTag() {
    return isExistingInfoTag;
  }

  public void setExistingInfoTag(boolean isExistingInfoTag) {
    this.isExistingInfoTag = isExistingInfoTag;
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return getActiveTag().getSupportedFields();
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
    return (getActiveTag() == null || getActiveTag().isEmpty());
  }

  /**
   * @param genericKey
   *
   * @return
   */
  public boolean hasField(FieldKey genericKey) {
    return getActiveTag().hasField(genericKey);
  }

  public boolean hasField(String id) {
    return getActiveTag().hasField(id);
  }

  @Override public int getFieldCount(final Key genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getFields(genericKey.name()).size();
  }

  public int getFieldCount() {
    return getActiveTag().getFieldCount();
  }

  public Tag setField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    TagField tagfield = createField(genericKey, values);
    setField(tagfield);
    return this;
  }

  public Tag addField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    TagField tagfield = createField(genericKey, values);
    addField(tagfield);
    return this;
  }

  public String getFirst(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, 0).or("");
  }

  public String getFirst(String id) throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getFirst(id);
  }

  @Override public Optional<String> getValue(final FieldKey genericKey) throws IllegalArgumentException {
    return getValue(genericKey, 0);
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    return getActiveTag().getValue(genericKey, index);
  }

  public String getFieldAt(FieldKey genericKey, int index) throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getValue(genericKey, index).or("");
  }

  public List<String> getAll(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getAll(genericKey);
  }

  public Tag deleteField(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    getActiveTag().deleteField(genericKey);
    return this;
  }

  public Tag deleteField(String id) throws IllegalArgumentException, UnsupportedFieldException {
    getActiveTag().deleteField(id);
    return this;
  }

  public Tag setArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    setField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));
    return this;
  }

  public Tag addArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    addField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));
    return this;
  }

  public Optional<Artwork> getFirstArtwork() throws UnsupportedFieldException {
    return getActiveTag().getFirstArtwork();
  }

  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    return getActiveTag().getArtworkList();
  }

  public Tag deleteArtwork() throws UnsupportedFieldException {
    getActiveTag().deleteArtwork();
    return this;
  }

  public boolean hasCommonFields() {
    return getActiveTag().hasCommonFields();
  }

  public int getFieldCountIncludingSubValues() {
    return getFieldCount();
  }

  public boolean setEncoding(final Charset enc) throws FieldDataInvalidException {
    return getActiveTag().setEncoding(enc);
  }

  public TagField createField(FieldKey genericKey, String... value) throws IllegalArgumentException,
                                                                           UnsupportedFieldException,
                                                                           FieldDataInvalidException {
    return getActiveTag().createField(genericKey, value);
  }

  /**
   * Create artwork field. Not currently supported.
   */
  public TagField createArtwork(Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    return getActiveTag().createArtwork(artwork);
  }

  public ImmutableList<TagField> getFields(FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getFields(genericKey);
  }

  public Iterator<TagField> getFields() {
    return getActiveTag().getFields();
  }

  public ImmutableList<TagField> getFields(String id) {
    return getActiveTag().getFields(id);
  }

  public Optional<TagField> getFirstField(String id) throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getFirstField(id);
  }

  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return getActiveTag().getFirstField(genericKey);
  }

  public TagField createCompilationField(boolean value) throws UnsupportedFieldException {
    try {
      return createField(FieldKey.IS_COMPILATION, String.valueOf(value));
    } catch (FieldDataInvalidException e) {
      throw new RuntimeException(e);  // if this ever happens it's library misconfiguration
    }
  }

  public void setField(TagField field) throws FieldDataInvalidException {
    getActiveTag().setField(field);
  }

  public void addField(TagField field) throws FieldDataInvalidException {
    getActiveTag().addField(field);
  }

  /**
   * @return size of the ID3 Chunk including header
   */
  public long getSizeOfID3TagIncludingChunkHeader() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return getSizeOfID3TagOnly() + ChunkHeader.CHUNK_HEADER_SIZE;
  }

  /**
   * @return size of the vanilla ID3Tag exclusing surrounding chunk
   */
  public long getSizeOfID3TagOnly() {
    if (!isExistingId3Tag()) {
      return 0;
    }
    return (id3Tag.getEndLocationInFile() - id3Tag.getStartLocationInFile());
  }

  /**
   * Call after read to ensure your preferred tag can make use of any additional metadata
   * held in the other tag, only used if the activetag field is empty for the fieldkey
   */
  public void syncTagsAfterRead() {
    if (getActiveTag() instanceof WavInfoTag) {
      syncToInfoFromId3IfEmpty();
    } else {
      syncToId3FromInfoIfEmpty();
    }

  }

  /**
   * If we have field in INFO tag but not ID3 tag (perhaps coz doesn't exist add them to ID3 tag)
   */
  public void syncToId3FromInfoIfEmpty() {

    try {
      for (FieldKey fieldKey : WavInfoTag.getSupportedTagKeys()) {
        if (id3Tag.getFirst(fieldKey).isEmpty()) {
          String first = infoTag.getFirst(fieldKey);
          if (!first.isEmpty()) {
            id3Tag.setField(fieldKey, stripNullTerminator(first));
          }
        }
      }
    } catch (FieldDataInvalidException deie) {
      LOG.log(INFO, "Couldn't sync to ID3 because the data to sync was invalid", deie);
    }
  }

  private String stripNullTerminator(String value) {
    return value.endsWith(NULL) ? value.substring(0, value.length() - 1) : value;
  }

  /**
   * If we have field in INFO tag but not ID3 tag (perhaps coz doesn't exist add them to ID3 tag)
   */
  public void syncToInfoFromId3IfEmpty() {

    try {
      for (FieldKey fieldKey : WavInfoTag.getSupportedTagKeys()) {
        if (infoTag.getFirst(fieldKey).isEmpty()) {
          if (!id3Tag.getFirst(fieldKey).isEmpty()) {
            infoTag.setField(fieldKey, addNullTerminatorIfNone(id3Tag.getFirst(fieldKey)));
          }
        }
      }
    } catch (FieldDataInvalidException deie) {
      LOG.log(INFO, "Couldn't sync to INFO because the data to sync was invalid", deie);
    }
  }

  private String addNullTerminatorIfNone(String value) {
    return value.endsWith(NULL) ? value : value + NULL;
  }

  /**
   * Call before save if saving both tags ensure any new information is the active tag is added to the other tag
   * overwriting any existing fields
   */
  public void syncTagBeforeWrite() {
    if (getActiveTag() instanceof WavInfoTag) {
      syncToId3FromInfoOverwrite();
    } else {
      syncToInfoFromId3Overwrite();
    }

  }

  /**
   * If we have field in INFO tag write to ID3 tag, if not we delete form ID3
   * (but only for tag that we can actually have in INFO tag)
   */
  public void syncToId3FromInfoOverwrite() {
    try {
      for (FieldKey fieldKey : WavInfoTag.getSupportedTagKeys()) {
        final String first = infoTag.getFirst(fieldKey);
        if (!first.isEmpty()) {
          id3Tag.setField(fieldKey, stripNullTerminator(first));
        } else if (hasField(fieldKey)) {
          id3Tag.deleteField(fieldKey);
        }
      }
    } catch (FieldDataInvalidException deie) {
      LOG.log(INFO, "Couldn't sync to ID3 because the data to sync was invalid", deie);
    }
  }

  /**
   * If we have field in ID3 tag write to INFO tag
   */
  public void syncToInfoFromId3Overwrite() {

    try {
      for (FieldKey fieldKey : WavInfoTag.getSupportedTagKeys()) {
        if (!id3Tag.getFirst(fieldKey).isEmpty()) {
          infoTag.setField(fieldKey, addNullTerminatorIfNone(id3Tag.getFirst(fieldKey)));
        } else if (hasField(fieldKey)) {
          infoTag.deleteField(fieldKey);
        }
      }
    } catch (FieldDataInvalidException deie) {
      LOG.log(INFO, "Couldn't sync to INFO because the data to sync was invalid", deie);
    }
  }

  public boolean isIncorrectlyAlignedTag() {
    return isIncorrectlyAlignedTag;
  }

  public void setIncorrectlyAlignedTag(boolean isIncorrectlyAlignedTag) {
    this.isIncorrectlyAlignedTag = isIncorrectlyAlignedTag;
  }
}
