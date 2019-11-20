/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */
package ealvatag.audio;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagTextField;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.images.Artwork;

import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.checkArgNotNull;
import static ealvatag.utils.Check.checkVarArg0NotNull;

import java.nio.charset.Charset;
import ealvatag.utils.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * This is a complete example implementation of  {@link AbstractTag}
 *
 * @author Raphaël Slinckx
 */
public abstract class GenericTag extends AbstractTag {
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[]{};
  final private static ImmutableSet<FieldKey> supportedKeys = ImmutableSet.of(FieldKey.ALBUM,
                                                                              FieldKey.ARTIST,
                                                                              FieldKey.TITLE,
                                                                              FieldKey.TRACK,
                                                                              FieldKey.GENRE,
                                                                              FieldKey.COMMENT,
                                                                              FieldKey.YEAR);


  public static ImmutableSet<FieldKey> getSupportedTagKeys() {
    return supportedKeys;
  }

  protected GenericTag() {
    super(false);
  }

  /**
   * Implementations of {@link TagTextField} for use with
   * &quot;ISO-8859-1&quot; strings.
   *
   * @author Raphaël Slinckx
   */
  protected class GenericTagTextField implements TagTextField {

    /**
     * Stores the string.
     */
    private String content;

    /**
     * Stores the identifier.
     */
    private final String id;

    /**
     * Creates an instance.
     *
     * @param fieldId        The identifier.
     * @param initialContent The string.
     */
    public GenericTagTextField(final String fieldId, final String initialContent) {
      this.id = fieldId;
      this.content = initialContent;
    }

    @Override
    public void copyContent(final TagField field) {
      if (field instanceof TagTextField) {
        this.content = ((TagTextField)field).getContent();
      }
    }

    @Override
    public String getContent() {
      return this.content;
    }

    @Override
    public Charset getEncoding() {
      return StandardCharsets.ISO_8859_1;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public byte[] getRawContent() {
      return this.content == null ? EMPTY_BYTE_ARRAY : this.content.getBytes(getEncoding());
    }

    @Override
    public boolean isBinary() {
      return false;
    }

    @Override
    public void isBinary(boolean b) {
            /* not supported */
    }

    @Override
    public boolean isCommon() {
      return true;
    }

    @Override
    public boolean isEmpty() {
      return "".equals(this.content);
    }

    @Override
    public void setContent(final String s) {
      this.content = s;
    }

    @Override
    public void setEncoding(final Charset s) {
            /* Not allowed */
    }

    @Override
    public String toString() {
      return getContent();
    }
  }

  @Override
  protected boolean isAllowedEncoding(final Charset enc) {
    return true;
  }

  @Override
  public TagField createField(final FieldKey genericKey, final String... values) throws IllegalArgumentException,
                                                                                        UnsupportedFieldException,
                                                                                        FieldDataInvalidException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (getSupportedFields().contains(genericKey)) {
      return new GenericTagTextField(genericKey.name(), checkVarArg0NotNull(values));
    } else {
      throw new UnsupportedFieldException(genericKey.name());
    }
  }

  @Override
  public String getFirst(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, 0).or("");
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    return getValue(genericKey.name(), index);
  }

  @Override
  public String getFieldAt(final FieldKey genericKey, final int index) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, index).or("");
  }

  @Override
  public List<String> getAll(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (!getSupportedFields().contains(genericKey)) {
      throw new UnsupportedFieldException(genericKey.name());
    }
    return super.getAll(genericKey.name());
  }

  @Override
  public Tag deleteField(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (getSupportedFields().contains(genericKey)) {
      deleteField(genericKey.name());
    } else {
      throw new UnsupportedFieldException(genericKey.name());
    }
    return this;
  }

  @Override
  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, genericKey);
    if (getSupportedFields().contains(genericKey)) {
      return getFirstField(genericKey.name());
    } else {
      throw new UnsupportedFieldException(genericKey.name());
    }
  }

  @Override
  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    return Collections.emptyList();
  }

  @Override
  public TagField createArtwork(final Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    throw new UnsupportedFieldException(FieldKey.COVER_ART.name());
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return supportedKeys;
  }
}
