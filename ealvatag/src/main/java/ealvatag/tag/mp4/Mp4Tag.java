/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphael Slinckx <raphael@slinckx.net>
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
package ealvatag.tag.mp4;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import ealvatag.utils.StandardCharsets;
import ealvatag.audio.AbstractTag;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.mp4.field.Mp4DiscNoField;
import ealvatag.tag.mp4.field.Mp4GenreField;
import ealvatag.tag.mp4.field.Mp4TagByteField;
import ealvatag.tag.mp4.field.Mp4TagCoverField;
import ealvatag.tag.mp4.field.Mp4TagReverseDnsField;
import ealvatag.tag.mp4.field.Mp4TagTextField;
import ealvatag.tag.mp4.field.Mp4TagTextNumberField;
import ealvatag.tag.mp4.field.Mp4TrackField;

import static ealvatag.tag.mp4.Mp4FieldKey.DISCNUMBER;
import static ealvatag.tag.mp4.Mp4FieldKey.GENRE;
import static ealvatag.tag.mp4.Mp4FieldKey.GENRE_CUSTOM;
import static ealvatag.tag.mp4.Mp4FieldKey.KEY_OLD;
import static ealvatag.tag.mp4.Mp4FieldKey.TRACK;
import static ealvatag.utils.Check.AT_LEAST_ONE_REQUIRED;
import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.checkArgNotNull;
import static ealvatag.utils.Check.checkArgNotNullOrEmpty;
import static ealvatag.utils.Check.checkVarArg0NotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A Logical representation of Mp4Tag, i.e the meta information stored in an Mp4 file underneath the
 * moov.udt.meta.ilst atom.
 */
public class Mp4Tag extends AbstractTag {

  private static final ImmutableMap<FieldKey, Mp4FieldKey> tagFieldToMp4Field;
  private static final TagFieldSupplier TRACK_FIELD_SUPPLIER = new TagFieldSupplier();
  private static final DiscTagFieldSupplier DISC_TAG_FIELD_SUPPLIER = new DiscTagFieldSupplier();

  //Mapping from generic key to mp4 key
  static {
    final ImmutableMap.Builder<FieldKey, Mp4FieldKey> builder = ImmutableMap.builder();
    builder.put(FieldKey.ACOUSTID_FINGERPRINT, Mp4FieldKey.ACOUSTID_FINGERPRINT)
           .put(FieldKey.ACOUSTID_ID, Mp4FieldKey.ACOUSTID_ID)
           .put(FieldKey.ALBUM, Mp4FieldKey.ALBUM)
           .put(FieldKey.ALBUM_ARTIST, Mp4FieldKey.ALBUM_ARTIST)
           .put(FieldKey.ALBUM_ARTIST_SORT, Mp4FieldKey.ALBUM_ARTIST_SORT)
           .put(FieldKey.ALBUM_ARTISTS, Mp4FieldKey.ALBUM_ARTISTS)
           .put(FieldKey.ALBUM_ARTISTS_SORT, Mp4FieldKey.ALBUM_ARTISTS_SORT)
           .put(FieldKey.ALBUM_SORT, Mp4FieldKey.ALBUM_SORT)
           .put(FieldKey.AMAZON_ID, Mp4FieldKey.ASIN)
           .put(FieldKey.ARRANGER, Mp4FieldKey.ARRANGER)
           .put(FieldKey.ARTIST, Mp4FieldKey.ARTIST)
           .put(FieldKey.ARTISTS, Mp4FieldKey.ARTISTS)
           .put(FieldKey.ARTIST_SORT, Mp4FieldKey.ARTIST_SORT)
           .put(FieldKey.BARCODE, Mp4FieldKey.BARCODE)
           .put(FieldKey.BPM, Mp4FieldKey.BPM)
           .put(FieldKey.CATALOG_NO, Mp4FieldKey.CATALOGNO)
           .put(FieldKey.CHOIR, Mp4FieldKey.CHOIR)
           .put(FieldKey.CHOIR_SORT, Mp4FieldKey.CHOIR_SORT)
           .put(FieldKey.CLASSICAL_CATALOG, Mp4FieldKey.CLASSICAL_CATALOG)
           .put(FieldKey.CLASSICAL_NICKNAME, Mp4FieldKey.CLASSICAL_NICKNAME)
           .put(FieldKey.COMMENT, Mp4FieldKey.COMMENT)
           .put(FieldKey.COMPOSER, Mp4FieldKey.COMPOSER)
           .put(FieldKey.COMPOSER_SORT, Mp4FieldKey.COMPOSER_SORT)
           .put(FieldKey.CONDUCTOR, Mp4FieldKey.CONDUCTOR)
           .put(FieldKey.COUNTRY, Mp4FieldKey.COUNTRY)
           .put(FieldKey.COVER_ART, Mp4FieldKey.ARTWORK)
           .put(FieldKey.CUSTOM1, Mp4FieldKey.MM_CUSTOM_1)
           .put(FieldKey.CUSTOM2, Mp4FieldKey.MM_CUSTOM_2)
           .put(FieldKey.CUSTOM3, Mp4FieldKey.MM_CUSTOM_3)
           .put(FieldKey.CUSTOM4, Mp4FieldKey.MM_CUSTOM_4)
           .put(FieldKey.CUSTOM5, Mp4FieldKey.MM_CUSTOM_5)
           .put(FieldKey.DISC_NO, Mp4FieldKey.DISCNUMBER)
           .put(FieldKey.DISC_SUBTITLE, Mp4FieldKey.DISC_SUBTITLE)
           .put(FieldKey.DISC_TOTAL, Mp4FieldKey.DISCNUMBER)
           .put(FieldKey.DJMIXER, Mp4FieldKey.DJMIXER)
           .put(FieldKey.MOOD_ELECTRONIC, Mp4FieldKey.MOOD_ELECTRONIC)
           .put(FieldKey.ENCODER, Mp4FieldKey.ENCODER)
           .put(FieldKey.ENGINEER, Mp4FieldKey.ENGINEER)
           .put(FieldKey.ENSEMBLE, Mp4FieldKey.ENSEMBLE)
           .put(FieldKey.ENSEMBLE_SORT, Mp4FieldKey.ENSEMBLE_SORT)
           .put(FieldKey.FBPM, Mp4FieldKey.FBPM)
           .put(FieldKey.GENRE, Mp4FieldKey.GENRE)
           .put(FieldKey.GROUPING, Mp4FieldKey.GROUPING)
           .put(FieldKey.INVOLVED_PERSON, Mp4FieldKey.INVOLVED_PEOPLE)
           .put(FieldKey.ISRC, Mp4FieldKey.ISRC)
           .put(FieldKey.IS_COMPILATION, Mp4FieldKey.COMPILATION)
           .put(FieldKey.IS_CLASSICAL, Mp4FieldKey.IS_CLASSICAL)
           .put(FieldKey.IS_SOUNDTRACK, Mp4FieldKey.IS_SOUNDTRACK)
           .put(FieldKey.KEY, Mp4FieldKey.KEY)
           .put(FieldKey.LANGUAGE, Mp4FieldKey.LANGUAGE)
           .put(FieldKey.LYRICIST, Mp4FieldKey.LYRICIST)
           .put(FieldKey.LYRICS, Mp4FieldKey.LYRICS)
           .put(FieldKey.MEDIA, Mp4FieldKey.MEDIA)
           .put(FieldKey.MIXER, Mp4FieldKey.MIXER)
           .put(FieldKey.MOOD, Mp4FieldKey.MOOD)
           .put(FieldKey.MOOD_ACOUSTIC, Mp4FieldKey.MOOD_ACOUSTIC)
           .put(FieldKey.MOOD_AGGRESSIVE, Mp4FieldKey.MOOD_AGGRESSIVE)
           .put(FieldKey.MOOD_AROUSAL, Mp4FieldKey.MOOD_AROUSAL)
           .put(FieldKey.MOOD_DANCEABILITY, Mp4FieldKey.MOOD_DANCEABILITY)
           .put(FieldKey.MOOD_HAPPY, Mp4FieldKey.MOOD_HAPPY)
           .put(FieldKey.MOOD_INSTRUMENTAL, Mp4FieldKey.MOOD_INSTRUMENTAL)
           .put(FieldKey.MOOD_PARTY, Mp4FieldKey.MOOD_PARTY)
           .put(FieldKey.MOOD_RELAXED, Mp4FieldKey.MOOD_RELAXED)
           .put(FieldKey.MOOD_SAD, Mp4FieldKey.MOOD_SAD)
           .put(FieldKey.MOOD_VALENCE, Mp4FieldKey.MOOD_VALENCE)
           .put(FieldKey.MOVEMENT, Mp4FieldKey.MOVEMENT)
           .put(FieldKey.MOVEMENT_NO, Mp4FieldKey.MOVEMENT_NO)
           .put(FieldKey.MOVEMENT_TOTAL, Mp4FieldKey.MOVEMENT_TOTAL)
           .put(FieldKey.MUSICBRAINZ_WORK, Mp4FieldKey.MUSICBRAINZ_WORK)
           .put(FieldKey.MUSICBRAINZ_ARTISTID, Mp4FieldKey.MUSICBRAINZ_ARTISTID)
           .put(FieldKey.MUSICBRAINZ_DISC_ID, Mp4FieldKey.MUSICBRAINZ_DISCID)
           .put(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, Mp4FieldKey.MUSICBRAINZ_ORIGINALALBUMID)
           .put(FieldKey.MUSICBRAINZ_RELEASEARTISTID, Mp4FieldKey.MUSICBRAINZ_ALBUMARTISTID)
           .put(FieldKey.MUSICBRAINZ_RELEASEID, Mp4FieldKey.MUSICBRAINZ_ALBUMID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, Mp4FieldKey.RELEASECOUNTRY)
           .put(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, Mp4FieldKey.MUSICBRAINZ_RELEASE_GROUPID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_STATUS, Mp4FieldKey.MUSICBRAINZ_ALBUM_STATUS)
           .put(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, Mp4FieldKey.MUSICBRAINZ_RELEASE_TRACKID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_TYPE, Mp4FieldKey.MUSICBRAINZ_ALBUM_TYPE)
           .put(FieldKey.MUSICBRAINZ_TRACK_ID, Mp4FieldKey.MUSICBRAINZ_TRACKID)
           .put(FieldKey.MUSICBRAINZ_WORK_ID, Mp4FieldKey.MUSICBRAINZ_WORKID)
           .put(FieldKey.MUSICIP_ID, Mp4FieldKey.MUSICIP_PUID)
           .put(FieldKey.OCCASION, Mp4FieldKey.MM_OCCASION)
           .put(FieldKey.ORCHESTRA, Mp4FieldKey.ORCHESTRA)
           .put(FieldKey.ORIGINAL_ALBUM, Mp4FieldKey.MM_ORIGINAL_ALBUM_TITLE)
           .put(FieldKey.ORIGINAL_ARTIST, Mp4FieldKey.MM_ORIGINAL_ARTIST)
           .put(FieldKey.ORIGINAL_LYRICIST, Mp4FieldKey.MM_ORIGINAL_LYRICIST)
           .put(FieldKey.ORIGINAL_YEAR, Mp4FieldKey.MM_ORIGINAL_YEAR)
           .put(FieldKey.PART, Mp4FieldKey.PART)
           .put(FieldKey.PERFORMER, Mp4FieldKey.PERFORMER)
           .put(FieldKey.PERFORMER_NAME, Mp4FieldKey.PERFORMER_NAME)
           .put(FieldKey.PERFORMER_NAME_SORT, Mp4FieldKey.PERFORMER_NAME_SORT)
           .put(FieldKey.PRODUCER, Mp4FieldKey.PRODUCER)
           .put(FieldKey.QUALITY, Mp4FieldKey.MM_QUALITY)
           .put(FieldKey.RANKING, Mp4FieldKey.RANKING)
           .put(FieldKey.RATING, Mp4FieldKey.SCORE)
           .put(FieldKey.RECORD_LABEL, Mp4FieldKey.LABEL)
           .put(FieldKey.REMIXER, Mp4FieldKey.REMIXER)
           .put(FieldKey.SCRIPT, Mp4FieldKey.SCRIPT)
           .put(FieldKey.SUBTITLE, Mp4FieldKey.SUBTITLE)
           .put(FieldKey.TAGS, Mp4FieldKey.TAGS)
           .put(FieldKey.TEMPO, Mp4FieldKey.TEMPO)
           .put(FieldKey.TIMBRE, Mp4FieldKey.TIMBRE)
           .put(FieldKey.TITLE, Mp4FieldKey.TITLE)
           .put(FieldKey.TITLE_MOVEMENT, Mp4FieldKey.TITLE_MOVEMENT)
           .put(FieldKey.TITLE_SORT, Mp4FieldKey.TITLE_SORT)
           .put(FieldKey.TONALITY, Mp4FieldKey.TONALITY)
           .put(FieldKey.TRACK, Mp4FieldKey.TRACK)
           .put(FieldKey.TRACK_TOTAL, Mp4FieldKey.TRACK)
           .put(FieldKey.URL_DISCOGS_ARTIST_SITE, Mp4FieldKey.URL_DISCOGS_ARTIST_SITE)
           .put(FieldKey.URL_DISCOGS_RELEASE_SITE, Mp4FieldKey.URL_DISCOGS_RELEASE_SITE)
           .put(FieldKey.URL_LYRICS_SITE, Mp4FieldKey.URL_LYRICS_SITE)
           .put(FieldKey.URL_OFFICIAL_ARTIST_SITE, Mp4FieldKey.URL_OFFICIAL_ARTIST_SITE)
           .put(FieldKey.URL_OFFICIAL_RELEASE_SITE, Mp4FieldKey.URL_OFFICIAL_RELEASE_SITE)
           .put(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, Mp4FieldKey.URL_WIKIPEDIA_ARTIST_SITE)
           .put(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, Mp4FieldKey.URL_WIKIPEDIA_RELEASE_SITE)
           .put(FieldKey.WORK, Mp4FieldKey.WORK)
           .put(FieldKey.YEAR, Mp4FieldKey.DAY)
           .put(FieldKey.WORK_TYPE, Mp4FieldKey.WORK_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, Mp4FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID)
           .put(FieldKey.PART_TYPE, Mp4FieldKey.PART_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, Mp4FieldKey.MUSICBRAINZ_WORK_COMPOSITION)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, Mp4FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE)
           .put(FieldKey.PART_NUMBER, Mp4FieldKey.PART_NUMBER)
           .put(FieldKey.ARTISTS_SORT, Mp4FieldKey.ARTISTS_SORT)
           .put(FieldKey.CONDUCTOR_SORT, Mp4FieldKey.CONDUCTOR_SORT)
           .put(FieldKey.ORCHESTRA_SORT, Mp4FieldKey.ORCHESTRA_SORT)

           .put(FieldKey.ARRANGER_SORT, Mp4FieldKey.ARRANGER_SORT)
           .put(FieldKey.OPUS, Mp4FieldKey.OPUS)
           .put(FieldKey.SINGLE_DISC_TRACK_NO, Mp4FieldKey.SINGLE_DISC_TRACK_NO)
           .put(FieldKey.PERIOD, Mp4FieldKey.PERIOD);

    tagFieldToMp4Field = builder.build();
  }

  protected Mp4Tag() {
    super(false);
  }

  /**
   * Create genre field
   * <p>
   * <p>If the content can be parsed to one of the known values use the genre field otherwise
   * use the custom field.
   *
   * @param genre the genre string
   *
   * @return TagField for the genre
   */
  @SuppressWarnings("unused")
  private TagField createGenreField(String genre) {
    String content = checkArgNotNullOrEmpty(genre);

    //Always write as text
    if (TagOptionSingleton.getInstance().isWriteMp4GenresAsText()) {
      return new Mp4TagTextField(GENRE_CUSTOM.getFieldName(), content);
    }

    if (Mp4GenreField.isValidGenre(content)) {
      return new Mp4GenreField(content);
    } else {
      return new Mp4TagTextField(GENRE_CUSTOM.getFieldName(), content);
    }
  }

  protected boolean isAllowedEncoding(Charset enc) {
    return StandardCharsets.UTF_8.equals(enc);
  }

  public String toString() {
    return "Mpeg4 " + super.toString();
  }

  public boolean hasField(FieldKey genericKey) {
    return getFields(genericKey).size() != 0;
  }

  public boolean hasField(Mp4FieldKey mp4FieldKey) {
    return getFieldList(mp4FieldKey.getFieldName()).size() != 0;
  }

  public ImmutableList<TagField> getFields(FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    Mp4FieldKey mp4FieldKey = getMp4FieldKey(genericKey);
    ImmutableList<TagField> list = getFields(mp4FieldKey.getFieldName());
    ImmutableList.Builder<TagField> filteredList = ImmutableList.builder();

    if (genericKey == FieldKey.KEY) {
      if (list.size() == 0) {
        list = getFields(KEY_OLD.getFieldName());
      }
      return list;
    } else if (genericKey == FieldKey.GENRE) {
      if (list.size() == 0) {
        list = getFields(GENRE_CUSTOM.getFieldName());
      }
      return list;
    } else if (genericKey == FieldKey.TRACK) {
      for (TagField next : list) {
        Mp4TrackField trackField = (Mp4TrackField)next;
        if (trackField.getTrackNo() > 0) {
          filteredList.add(next);
        }
      }
      return filteredList.build();
    } else if (genericKey == FieldKey.TRACK_TOTAL) {
      for (TagField next : list) {
        Mp4TrackField trackField = (Mp4TrackField)next;
        if (trackField.getTrackTotal() > 0) {
          filteredList.add(next);
        }
      }
      return filteredList.build();
    } else if (genericKey == FieldKey.DISC_NO) {
      for (TagField next : list) {
        Mp4DiscNoField discNoField = (Mp4DiscNoField)next;
        if (discNoField.getDiscNo() > 0) {
          filteredList.add(next);
        }
      }
      return filteredList.build();
    } else if (genericKey == FieldKey.DISC_TOTAL) {
      for (TagField next : list) {
        Mp4DiscNoField discNoField = (Mp4DiscNoField)next;
        if (discNoField.getDiscTotal() > 0) {
          filteredList.add(next);
        }
      }
      return filteredList.build();
    } else {
      return list;
    }
  }

  private Mp4FieldKey getMp4FieldKey(final FieldKey genericKey) throws UnsupportedFieldException {
    final Mp4FieldKey mp4FieldKey = tagFieldToMp4Field.get(genericKey);
    if (mp4FieldKey == null) {
      throw new UnsupportedFieldException(genericKey.name());
    }
    return mp4FieldKey;
  }

  public List<String> getAll(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    List<String> values = new ArrayList<>();
    List<TagField> fields = getFields(genericKey);
    for (TagField tagfield : fields) {
      if (genericKey == FieldKey.TRACK) {
        values.add(((Mp4TrackField)tagfield).getTrackNo().toString());
      } else if (genericKey == FieldKey.TRACK_TOTAL) {
        values.add(((Mp4TrackField)tagfield).getTrackTotal().toString());
      } else if (genericKey == FieldKey.DISC_NO) {
        values.add(((Mp4DiscNoField)tagfield).getDiscNo().toString());
      } else if (genericKey == FieldKey.DISC_TOTAL) {
        values.add(((Mp4DiscNoField)tagfield).getDiscTotal().toString());
      } else {
        values.add(tagfield.toString());
      }
    }
    return values;
  }

  public List<TagField> get(Mp4FieldKey mp4FieldKey) throws IllegalArgumentException {
    checkArgNotNull(mp4FieldKey, CANNOT_BE_NULL, "mp4FieldKey");
    return getFieldList(mp4FieldKey.getFieldName());
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    List<TagField> fields = getFields(genericKey);
    if (fields.size() > index) {
      TagField field = fields.get(index);
      switch (genericKey) {
        case TRACK:
          return Optional.of(((Mp4TrackField)field).getTrackNoString());
        case TRACK_TOTAL:
          return Optional.of(((Mp4TrackField)field).getTrackTotalString());
        case DISC_NO:
          return Optional.of(((Mp4DiscNoField)field).getDiscNoString());
        case DISC_TOTAL:
          return Optional.of(((Mp4DiscNoField)field).getDiscTotalString());
        default:
          return Optional.of(field.toString());
      }
    }
    return Optional.absent();
  }

  public String getFieldAt(FieldKey genericKey, int index) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, 0).or("");
  }

  public String getFirst(Mp4FieldKey mp4Key) throws IllegalArgumentException {
    checkArgNotNull(mp4Key);
    return super.getFirst(mp4Key.getFieldName());
  }

  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    List<TagField> fields = getFields(checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey"));
    if (fields.size() == 0) {
      return Optional.absent();
    }
    return Optional.fromNullable(fields.get(0));
  }

  public Mp4TagField getFirstField(Mp4FieldKey mp4Key) throws IllegalArgumentException {
    checkArgNotNull(mp4Key);
    return (Mp4TagField)super.getFirstField(mp4Key.getFieldName()).orNull();
  }

  public Tag deleteField(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    String mp4FieldName = getMp4FieldKey(genericKey).getFieldName();
    if (genericKey == FieldKey.KEY) {
      deleteField(Mp4FieldKey.KEY_OLD);
      deleteField(mp4FieldName);
    } else if (genericKey == FieldKey.TRACK) {
      String trackTotal = this.getFirst(FieldKey.TRACK_TOTAL);
      if (trackTotal.length() == 0) {
        super.deleteField(mp4FieldName);
        return this;
      } else {
        Mp4TrackField field = (Mp4TrackField)getFirstField(FieldKey.TRACK_TOTAL).or(TRACK_FIELD_SUPPLIER);
        field.setTrackNo(0);
        return this;
      }
    } else if (genericKey == FieldKey.TRACK_TOTAL) {
      String track = this.getFirst(FieldKey.TRACK);
      if (track.length() == 0) {
        super.deleteField(mp4FieldName);
        return this;
      } else {
        Mp4TrackField field = (Mp4TrackField)getFirstField(FieldKey.TRACK).or(TRACK_FIELD_SUPPLIER);
        field.setTrackTotal(0);
        return this;
      }
    } else if (genericKey == FieldKey.DISC_NO) {
      String discTotal = this.getFirst(FieldKey.DISC_TOTAL);
      if (discTotal.length() == 0) {
        super.deleteField(mp4FieldName);
        return this;
      } else {
        Mp4DiscNoField field = (Mp4DiscNoField)getFirstField(FieldKey.DISC_TOTAL).or(DISC_TAG_FIELD_SUPPLIER);
        field.setDiscNo(0);
        return this;
      }
    } else if (genericKey == FieldKey.DISC_TOTAL) {
      String discno = this.getFirst(FieldKey.DISC_NO);
      if (discno.length() == 0) {
        super.deleteField(mp4FieldName);
        return this;
      } else {
        Mp4DiscNoField field = (Mp4DiscNoField)getFirstField(FieldKey.DISC_NO).or(DISC_TAG_FIELD_SUPPLIER);
        field.setDiscTotal(0);
        return this;
      }
    } else if (genericKey == FieldKey.GENRE) {
      super.deleteField(Mp4FieldKey.GENRE.getFieldName());
      super.deleteField(Mp4FieldKey.GENRE_CUSTOM.getFieldName());
    } else {
      super.deleteField(mp4FieldName);
    }
    return this;
  }

  public void deleteField(Mp4FieldKey mp4Key) throws IllegalArgumentException {
    checkArgNotNull(mp4Key);
    super.deleteField(mp4Key.getFieldName());
  }

  TagField createArtworkField(byte[] data) {
    return new Mp4TagCoverField(data);
  }

  public TagField createArtwork(Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    return new Mp4TagCoverField(artwork.getBinaryData());
  }

  @Override
  public Tag addField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if ((genericKey == FieldKey.TRACK) ||
        (genericKey == FieldKey.TRACK_TOTAL) ||
        (genericKey == FieldKey.DISC_NO) ||
        (genericKey == FieldKey.DISC_TOTAL)) {
      setField(genericKey, values);
    } else {
      TagField tagfield = createField(genericKey, values);
      addField(tagfield);
    }
    return this;
  }

  @Override
  public TagField createField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                            UnsupportedFieldException,
                                                                            FieldDataInvalidException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    String value = checkVarArg0NotNull(values, AT_LEAST_ONE_REQUIRED, "values");

    // TODO: 1/20/17 Make this a big switch statement without the nasty if else logic
    //Special handling for these number fields because multiple generic keys map to a single mp4 field
    if ((genericKey == FieldKey.TRACK) ||
        (genericKey == FieldKey.TRACK_TOTAL) ||
        (genericKey == FieldKey.DISC_NO) ||
        (genericKey == FieldKey.DISC_TOTAL)) {
      try {
        int number = Integer.parseInt(value);
        switch (genericKey) {
          case TRACK:
            return new Mp4TrackField(number);
          case TRACK_TOTAL:
            return new Mp4TrackField(0, number);
          case DISC_NO:
            return new Mp4DiscNoField(number);
          case DISC_TOTAL:
            return new Mp4DiscNoField(0, number);
        }
      } catch (NumberFormatException nfe) {
        //If not number we want to convert to an expected exception (which is not a RuntimeException)
        //so can be handled properly by calling program
        throw new FieldDataInvalidException("Value " + value + " is not a number as required");
      }
    } else if (genericKey == FieldKey.GENRE) {
      //Always write as text
      if (TagOptionSingleton.getInstance().isWriteMp4GenresAsText()) {
        return new Mp4TagTextField(GENRE_CUSTOM.getFieldName(), value);
      }

      if (Mp4GenreField.isValidGenre(value)) {
        return new Mp4GenreField(value);
      } else {
        return new Mp4TagTextField(GENRE_CUSTOM.getFieldName(), value);
      }
    }
    //Default for all other fields
    return createField(getMp4FieldKey(genericKey), value);
  }

  /**
   * {@inheritDoc}
   * Cannot have both a genre field and a custom genre field
   */
  @Override
  public Tag setField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                    UnsupportedFieldException,
                                                                    FieldDataInvalidException {
    TagField tagfield = createField(genericKey, values);

    if (genericKey == FieldKey.GENRE) {
      if (tagfield.getId().equals(GENRE.getFieldName())) {
        this.deleteField(Mp4FieldKey.GENRE_CUSTOM);
      } else if (tagfield.getId().equals(GENRE_CUSTOM.getFieldName())) {
        this.deleteField(Mp4FieldKey.GENRE);
      }
    }
    setField(tagfield);
    return this;
  }

  public void setField(Mp4FieldKey fieldKey, String value) throws IllegalArgumentException, FieldDataInvalidException {
    TagField tagfield = createField(fieldKey, value);
    setField(tagfield);
  }

  @Override
  public void setField(TagField field) {
    if (field == null) {
      return;
    }

    if (field.getId().equals(TRACK.getFieldName())) {
      List<TagField> list = getFieldList(field.getId());
      if (list.size() == 0) {
        super.setField(field);
      } else {
        Mp4TrackField existingTrackField = (Mp4TrackField)list.get(0);
        Mp4TrackField newTrackField = (Mp4TrackField)field;
        Short trackNo = existingTrackField.getTrackNo();
        Short trackTotal = existingTrackField.getTrackTotal();
        if (newTrackField.getTrackNo() > 0) {
          trackNo = newTrackField.getTrackNo();
        }
        if (newTrackField.getTrackTotal() > 0) {
          trackTotal = newTrackField.getTrackTotal();
        }

        Mp4TrackField mergedTrackField = new Mp4TrackField(trackNo, trackTotal);
        super.setField(mergedTrackField);
      }
    } else if (field.getId().equals(DISCNUMBER.getFieldName())) {
      List<TagField> list = getFieldList(field.getId());
      if (list.size() == 0) {
        super.setField(field);
      } else {
        Mp4DiscNoField existingDiscNoField = (Mp4DiscNoField)list.get(0);
        Mp4DiscNoField newDiscNoField = (Mp4DiscNoField)field;
        Short discNo = existingDiscNoField.getDiscNo();
        Short discTotal = existingDiscNoField.getDiscTotal();
        if (newDiscNoField.getDiscNo() > 0) {
          discNo = newDiscNoField.getDiscNo();
        }
        if (newDiscNoField.getDiscTotal() > 0) {
          discTotal = newDiscNoField.getDiscTotal();
        }

        Mp4DiscNoField mergedDiscNoField = new Mp4DiscNoField(discNo, discTotal);
        super.setField(mergedDiscNoField);
      }
    } else {
      super.setField(field);
    }
  }

  /**
   * Create Tag Field using mp4 key
   * <p>
   * Uses the correct subclass for the key
   *
   * @param mp4FieldKey mp4 field key
   * @param value       value to set into tag field
   *
   * @return tag field for the given key
   *
   * @throws IllegalArgumentException  if {@code mp4FieldKey} or {@code value} are null
   * @throws FieldDataInvalidException if the data is not valid for the given {@link Mp4FieldKey}
   * @throws UnsupportedFieldException if the key is unsupported
   */
  public TagField createField(Mp4FieldKey mp4FieldKey, String value) throws IllegalArgumentException,
                                                                            UnsupportedFieldException,
                                                                            FieldDataInvalidException {
    checkArgNotNull(mp4FieldKey);
    checkArgNotNull(value);

    //This is boolean stored as 1, but calling program might setField as 'true' so we handle this
    //case internally , any other values it is set to we treat as false
    if (mp4FieldKey == Mp4FieldKey.COMPILATION) {
      if (value.equalsIgnoreCase("true") || value.equals("1")) {
        return createCompilationField(true);
      } else {
        return createCompilationField(false);
      }
    } else if (mp4FieldKey == Mp4FieldKey.GENRE) {
      if (Mp4GenreField.isValidGenre(value)) {
        return new Mp4GenreField(value);
      } else {
        throw new IllegalArgumentException(ErrorMessage.NOT_STANDARD_MP$_GENRE);
      }
    } else if (mp4FieldKey == Mp4FieldKey.GENRE_CUSTOM) {
      return new Mp4TagTextField(GENRE_CUSTOM.getFieldName(), value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.DISC_NO) {
      return new Mp4DiscNoField(value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.TRACK_NO) {
      return new Mp4TrackField(value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.BYTE) {
      return new Mp4TagByteField(mp4FieldKey, value, mp4FieldKey.getFieldLength());
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.NUMBER) {
      return new Mp4TagTextNumberField(mp4FieldKey.getFieldName(), value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.REVERSE_DNS) {
      return new Mp4TagReverseDnsField(mp4FieldKey, value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.ARTWORK) {
      throw new UnsupportedFieldException(ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.TEXT) {
      return new Mp4TagTextField(mp4FieldKey.getFieldName(), value);
    } else if (mp4FieldKey.getSubClassFieldType() == Mp4TagFieldSubType.UNKNOWN) {
      throw new UnsupportedFieldException(ErrorMessage.DO_NOT_KNOW_HOW_TO_CREATE_THIS_ATOM_TYPE, mp4FieldKey.getFieldName());
    } else {
      throw new UnsupportedFieldException(ErrorMessage.DO_NOT_KNOW_HOW_TO_CREATE_THIS_ATOM_TYPE, mp4FieldKey.getFieldName());
    }
  }

  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    List<TagField> coverartList = get(Mp4FieldKey.ARTWORK);
    List<Artwork> artworkList = new ArrayList<>(coverartList.size());

    for (TagField next : coverartList) {
      Mp4TagCoverField mp4CoverArt = (Mp4TagCoverField)next;
      Artwork artwork = ArtworkFactory.getNew();
      artwork.setBinaryData(mp4CoverArt.getData());
      artwork.setMimeType(Mp4TagCoverField.getMimeTypeForImageType(mp4CoverArt.getFieldType()));
      artworkList.add(artwork);
    }
    return artworkList;
  }

  public TagField createCompilationField(boolean origValue) throws UnsupportedFieldException {
    try {
      return new Mp4TagByteField(Mp4FieldKey.COMPILATION,
                                 origValue ? Mp4TagByteField.TRUE_VALUE : Mp4TagByteField.FALSE_VALUE,
                                 Mp4FieldKey.COMPILATION.getFieldLength());
    } catch (FieldDataInvalidException e) {
      throw new RuntimeException(e); // should never happen, else library misconfiguration
    }
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return tagFieldToMp4Field.keySet();
  }

  public void markReadOnly() {
    setReadOnly();
  }

  public static Mp4Tag makeEmpty() {
    return new Mp4Tag();
  }

  private static class TagFieldSupplier implements Supplier<TagField> {
    private Mp4TrackField mp4TrackField = new Mp4TrackField(0);

    @Override public TagField get() {return mp4TrackField;}
  }

  private static class DiscTagFieldSupplier implements Supplier<TagField> {
    private Mp4DiscNoField mp4DiscNoField = new Mp4DiscNoField(0);

    @Override public TagField get() {return mp4DiscNoField;}
  }
}
