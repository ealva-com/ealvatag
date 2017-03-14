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

package ealvatag.tag;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ealvatag.tag.images.Artwork;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * No-op {@link Tag} implementation. Especially useful for testing
 * <p>
 * Created by Eric A. Snell on 1/22/17.
 */
public final class NullTag implements Tag {
  public static final Tag INSTANCE = new NullTag();

  private NullTag() {
  }

  @Override public boolean isReadOnly() {
    return true;
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return ImmutableSet.of();
  }

  @Override public boolean isEmpty() {
    return true;
  }

  @Override public boolean hasField(final FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return false;
  }

  @Override public boolean hasField(final String id) {
    return false;
  }

  @Override public int getFieldCount(final Key genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return 0;
  }

  @Override public int getFieldCount() {
    return 0;
  }

  @Override public Tag setField(final FieldKey genericKey, final String... values) throws IllegalArgumentException,
                                                                                          UnsupportedFieldException,
                                                                                          FieldDataInvalidException {
    return this;
  }

  @Override public Tag addField(final FieldKey genericKey, final String... values) throws IllegalArgumentException,
                                                                                          UnsupportedFieldException,
                                                                                          FieldDataInvalidException {
    return this;
  }

  @Override public String getFirst(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return "";
  }

  @Override public String getFirst(final String id) throws IllegalArgumentException, UnsupportedFieldException {
    return null;
  }

  @Override public Optional<String> getValue(final FieldKey genericKey) throws IllegalArgumentException {
    return Optional.absent();
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    return Optional.absent();
  }

  @Override public String getFieldAt(final FieldKey genericKey, final int index)
      throws IllegalArgumentException, UnsupportedFieldException {
    return "";
  }

  @Override public List<String> getAll(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return ImmutableList.of();
  }

  @Override public Tag deleteField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return this;
  }

  @Override public Tag deleteField(final String id) throws IllegalArgumentException, UnsupportedFieldException {
    return this;
  }

  @Override public Tag setArtwork(final Artwork artwork)
      throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    return this;
  }

  @Override public Tag addArtwork(final Artwork artwork)
      throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    return this;
  }

  @Override public Optional<Artwork> getFirstArtwork() throws UnsupportedFieldException {
    return Optional.absent();
  }

  @Override public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    return ImmutableList.of();
  }

  @Override public Tag deleteArtwork() throws UnsupportedFieldException {
    return this;
  }

  @Override public boolean hasCommonFields() {
    return false;
  }

  @Override public int getFieldCountIncludingSubValues() {
    return 0;
  }

  @Override public boolean setEncoding(final Charset enc) throws FieldDataInvalidException {
    return false;
  }

  @Override public TagField createField(final FieldKey genericKey, final String... value) throws IllegalArgumentException,
                                                                                                 UnsupportedFieldException,
                                                                                                 FieldDataInvalidException {
    return NullTagField.INSTANCE;
  }

  @Override public TagField createArtwork(final Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    return NullTagField.INSTANCE;
  }

  @Override public ImmutableList<TagField> getFields(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return ImmutableList.of();
  }

  @Override public Iterator<TagField> getFields() {
    return ImmutableList.<TagField>of().iterator();
  }

  @Override public ImmutableList<TagField> getFields(final String id) {
    return ImmutableList.of();
  }

  @Override public Optional<TagField> getFirstField(final String id) throws IllegalArgumentException, UnsupportedFieldException {
    return Optional.absent();
  }

  @Override public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return Optional.absent();
  }

  @Override public TagField createCompilationField(final boolean value) throws UnsupportedFieldException {
    return NullTagField.INSTANCE;
  }
}
