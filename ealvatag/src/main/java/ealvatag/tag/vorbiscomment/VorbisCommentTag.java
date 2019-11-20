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
package ealvatag.tag.vorbiscomment;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import ealvatag.utils.StandardCharsets;
import ealvatag.audio.AbstractTag;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.audio.ogg.util.VorbisHeader;
import ealvatag.tag.FieldDataInvalidException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.InvalidFrameException;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.TagTextField;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.vorbiscomment.util.Base64Coder;

import static ealvatag.tag.vorbiscomment.VorbisCommentFieldKey.VENDOR;
import static ealvatag.utils.Check.AT_LEAST_ONE_REQUIRED;
import static ealvatag.utils.Check.CANNOT_BE_NULL;
import static ealvatag.utils.Check.checkArgNotNull;
import static ealvatag.utils.Check.checkVarArg0NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the logical representation of  Vorbis Comment Data
 */
public class VorbisCommentTag extends AbstractTag implements ContainsVorbisCommentField {
  //This is the vendor string that will be written if no other is supplied. Should be the name of the software
  //that actually encoded the file in the first place.
  private static final String DEFAULT_VENDOR = "ealvatag";
  private static final ImmutableMap<FieldKey, VorbisCommentFieldKey> tagFieldToOggField = makeFieldMap();

  /**
   * Use to construct a new tag properly initialized
   */
  public static VorbisCommentTag createNewTag() {
    VorbisCommentTag tag = new VorbisCommentTag();
    tag.setVendor(DEFAULT_VENDOR);
    return tag;
  }

  /**
   * Only used within Package, hidden because it doesnt set Vendor
   * which should be done when created by end user
   */
  public VorbisCommentTag() {
    super(false);
  }

  private static ImmutableMap<FieldKey, VorbisCommentFieldKey> makeFieldMap() {
    final ImmutableMap.Builder<FieldKey, VorbisCommentFieldKey> builder = ImmutableMap.builder();
    builder.put(FieldKey.ACOUSTID_FINGERPRINT, VorbisCommentFieldKey.ACOUSTID_FINGERPRINT)
           .put(FieldKey.ACOUSTID_ID, VorbisCommentFieldKey.ACOUSTID_ID)
           .put(FieldKey.ALBUM, VorbisCommentFieldKey.ALBUM)
           .put(FieldKey.ALBUM_ARTIST, VorbisCommentFieldKey.ALBUMARTIST)
           .put(FieldKey.ALBUM_ARTISTS, VorbisCommentFieldKey.ALBUMARTISTS)
           .put(FieldKey.ALBUM_ARTISTS_SORT, VorbisCommentFieldKey.ALBUMARTISTSSORT)
           .put(FieldKey.ALBUM_ARTIST_SORT, VorbisCommentFieldKey.ALBUMARTISTSORT)
           .put(FieldKey.ALBUM_SORT, VorbisCommentFieldKey.ALBUMSORT)
           .put(FieldKey.AMAZON_ID, VorbisCommentFieldKey.ASIN)
           .put(FieldKey.ARRANGER, VorbisCommentFieldKey.ARRANGER)
           .put(FieldKey.ARRANGER_SORT, VorbisCommentFieldKey.ARRANGER_SORT)
           .put(FieldKey.ARTIST, VorbisCommentFieldKey.ARTIST)
           .put(FieldKey.ARTISTS, VorbisCommentFieldKey.ARTISTS)
           .put(FieldKey.ARTISTS_SORT, VorbisCommentFieldKey.ARTISTS_SORT)
           .put(FieldKey.ARTIST_SORT, VorbisCommentFieldKey.ARTISTSORT)
           .put(FieldKey.BARCODE, VorbisCommentFieldKey.BARCODE)
           .put(FieldKey.BPM, VorbisCommentFieldKey.BPM)
           .put(FieldKey.CATALOG_NO, VorbisCommentFieldKey.CATALOGNUMBER)
           .put(FieldKey.CHOIR, VorbisCommentFieldKey.CHOIR)
           .put(FieldKey.CHOIR_SORT, VorbisCommentFieldKey.CHOIR_SORT)
           .put(FieldKey.CLASSICAL_CATALOG, VorbisCommentFieldKey.CLASSICAL_CATALOG)
           .put(FieldKey.CLASSICAL_NICKNAME, VorbisCommentFieldKey.CLASSICAL_NICKNAME)
           .put(FieldKey.COMMENT, VorbisCommentFieldKey.COMMENT)
           .put(FieldKey.COMPOSER, VorbisCommentFieldKey.COMPOSER)
           .put(FieldKey.COMPOSER_SORT, VorbisCommentFieldKey.COMPOSERSORT)
           .put(FieldKey.CONDUCTOR, VorbisCommentFieldKey.CONDUCTOR)
           .put(FieldKey.CONDUCTOR_SORT, VorbisCommentFieldKey.CONDUCTOR_SORT)
           .put(FieldKey.COUNTRY, VorbisCommentFieldKey.COUNTRY)
           .put(FieldKey.COVER_ART, VorbisCommentFieldKey.METADATA_BLOCK_PICTURE)
           .put(FieldKey.CUSTOM1, VorbisCommentFieldKey.CUSTOM1)
           .put(FieldKey.CUSTOM2, VorbisCommentFieldKey.CUSTOM2)
           .put(FieldKey.CUSTOM3, VorbisCommentFieldKey.CUSTOM3)
           .put(FieldKey.CUSTOM4, VorbisCommentFieldKey.CUSTOM4)
           .put(FieldKey.CUSTOM5, VorbisCommentFieldKey.CUSTOM5)
           .put(FieldKey.DISC_NO, VorbisCommentFieldKey.DISCNUMBER)
           .put(FieldKey.DISC_SUBTITLE, VorbisCommentFieldKey.DISCSUBTITLE)
           .put(FieldKey.DISC_TOTAL, VorbisCommentFieldKey.DISCTOTAL)
           .put(FieldKey.DJMIXER, VorbisCommentFieldKey.DJMIXER)
           .put(FieldKey.ENCODER, VorbisCommentFieldKey.VENDOR)     //Known as vendor in VorbisComment
           .put(FieldKey.ENGINEER, VorbisCommentFieldKey.ENGINEER)
           .put(FieldKey.ENSEMBLE, VorbisCommentFieldKey.ENSEMBLE)
           .put(FieldKey.ENSEMBLE_SORT, VorbisCommentFieldKey.ENSEMBLE_SORT)
           .put(FieldKey.FBPM, VorbisCommentFieldKey.FBPM)
           .put(FieldKey.GENRE, VorbisCommentFieldKey.GENRE)
           .put(FieldKey.GROUPING, VorbisCommentFieldKey.GROUPING)
           .put(FieldKey.INVOLVED_PERSON, VorbisCommentFieldKey.INVOLVED_PERSON)
           .put(FieldKey.ISRC, VorbisCommentFieldKey.ISRC)
           .put(FieldKey.IS_CLASSICAL, VorbisCommentFieldKey.IS_CLASSICAL)
           .put(FieldKey.IS_COMPILATION, VorbisCommentFieldKey.COMPILATION)
           .put(FieldKey.IS_SOUNDTRACK, VorbisCommentFieldKey.IS_SOUNDTRACK)
           .put(FieldKey.KEY, VorbisCommentFieldKey.KEY)
           .put(FieldKey.LANGUAGE, VorbisCommentFieldKey.LANGUAGE)
           .put(FieldKey.LYRICIST, VorbisCommentFieldKey.LYRICIST)
           .put(FieldKey.LYRICS, VorbisCommentFieldKey.LYRICS)
           .put(FieldKey.MEDIA, VorbisCommentFieldKey.MEDIA)
           .put(FieldKey.MIXER, VorbisCommentFieldKey.MIXER)
           .put(FieldKey.MOOD, VorbisCommentFieldKey.MOOD)
           .put(FieldKey.MOOD_ACOUSTIC, VorbisCommentFieldKey.MOOD_ACOUSTIC)
           .put(FieldKey.MOOD_AGGRESSIVE, VorbisCommentFieldKey.MOOD_AGGRESSIVE)
           .put(FieldKey.MOOD_AROUSAL, VorbisCommentFieldKey.MOOD_AROUSAL)
           .put(FieldKey.MOOD_DANCEABILITY, VorbisCommentFieldKey.MOOD_DANCEABILITY)
           .put(FieldKey.MOOD_ELECTRONIC, VorbisCommentFieldKey.MOOD_ELECTRONIC)
           .put(FieldKey.MOOD_HAPPY, VorbisCommentFieldKey.MOOD_HAPPY)
           .put(FieldKey.MOOD_INSTRUMENTAL, VorbisCommentFieldKey.MOOD_INSTRUMENTAL)
           .put(FieldKey.MOOD_PARTY, VorbisCommentFieldKey.MOOD_PARTY)
           .put(FieldKey.MOOD_RELAXED, VorbisCommentFieldKey.MOOD_RELAXED)
           .put(FieldKey.MOOD_SAD, VorbisCommentFieldKey.MOOD_SAD)
           .put(FieldKey.MOOD_VALENCE, VorbisCommentFieldKey.MOOD_VALENCE)
           .put(FieldKey.MOVEMENT, VorbisCommentFieldKey.MOVEMENT)
           .put(FieldKey.MOVEMENT_NO, VorbisCommentFieldKey.MOVEMENT_NO)
           .put(FieldKey.MOVEMENT_TOTAL, VorbisCommentFieldKey.MOVEMENT_TOTAL)
           .put(FieldKey.MUSICBRAINZ_ARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID)
           .put(FieldKey.MUSICBRAINZ_DISC_ID, VorbisCommentFieldKey.MUSICBRAINZ_DISCID)
           .put(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID,
                VorbisCommentFieldKey.MUSICBRAINZ_ORIGINAL_ALBUMID)
           .put(FieldKey.MUSICBRAINZ_RELEASEARTISTID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID)
           .put(FieldKey.MUSICBRAINZ_RELEASEID, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, VorbisCommentFieldKey.RELEASECOUNTRY)
           .put(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, VorbisCommentFieldKey.MUSICBRAINZ_RELEASEGROUPID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_STATUS, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMSTATUS)
           .put(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_RELEASETRACKID)
           .put(FieldKey.MUSICBRAINZ_RELEASE_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_ALBUMTYPE)
           .put(FieldKey.MUSICBRAINZ_TRACK_ID, VorbisCommentFieldKey.MUSICBRAINZ_TRACKID)
           .put(FieldKey.MUSICBRAINZ_WORK, VorbisCommentFieldKey.MUSICBRAINZ_WORK)
           .put(FieldKey.MUSICBRAINZ_WORK_COMPOSITION, VorbisCommentFieldKey.MUSICBRAINZ_WORK_COMPOSITION)
           .put(FieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_COMPOSITION_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORKID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL1)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL1_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL2)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL2_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL3)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL3_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL4)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL4_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL5)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL5_TYPE)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL6)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_ID)
           .put(FieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE, VorbisCommentFieldKey.MUSICBRAINZ_WORK_PART_LEVEL6_TYPE)
           .put(FieldKey.MUSICIP_ID, VorbisCommentFieldKey.MUSICIP_PUID)
           .put(FieldKey.OCCASION, VorbisCommentFieldKey.OCCASION)
           .put(FieldKey.OPUS, VorbisCommentFieldKey.OPUS)
           .put(FieldKey.ORCHESTRA, VorbisCommentFieldKey.ORCHESTRA)
           .put(FieldKey.ORCHESTRA_SORT, VorbisCommentFieldKey.ORCHESTRA_SORT)
           .put(FieldKey.ORIGINAL_ALBUM, VorbisCommentFieldKey.ORIGINAL_ALBUM)
           .put(FieldKey.ORIGINAL_ARTIST, VorbisCommentFieldKey.ORIGINAL_ARTIST)
           .put(FieldKey.ORIGINAL_LYRICIST, VorbisCommentFieldKey.ORIGINAL_LYRICIST)
           .put(FieldKey.ORIGINAL_YEAR, VorbisCommentFieldKey.ORIGINAL_YEAR)
           .put(FieldKey.PART, VorbisCommentFieldKey.PART)
           .put(FieldKey.PART_NUMBER, VorbisCommentFieldKey.PART_NUMBER)
           .put(FieldKey.PART_TYPE, VorbisCommentFieldKey.PART_TYPE)
           .put(FieldKey.PERFORMER, VorbisCommentFieldKey.PERFORMER)
           .put(FieldKey.PERFORMER_NAME, VorbisCommentFieldKey.PERFORMER_NAME)
           .put(FieldKey.PERFORMER_NAME_SORT, VorbisCommentFieldKey.PERFORMER_NAME_SORT)
           .put(FieldKey.PERIOD, VorbisCommentFieldKey.PERIOD)
           .put(FieldKey.PRODUCER, VorbisCommentFieldKey.PRODUCER)
           .put(FieldKey.QUALITY, VorbisCommentFieldKey.QUALITY)
           .put(FieldKey.RANKING, VorbisCommentFieldKey.RANKING)
           .put(FieldKey.RATING, VorbisCommentFieldKey.RATING)
           .put(FieldKey.RECORD_LABEL, VorbisCommentFieldKey.LABEL)
           .put(FieldKey.REMIXER, VorbisCommentFieldKey.REMIXER)
           .put(FieldKey.SCRIPT, VorbisCommentFieldKey.SCRIPT)
           .put(FieldKey.SINGLE_DISC_TRACK_NO, VorbisCommentFieldKey.SINGLE_DISC_TRACK_NO)
           .put(FieldKey.SUBTITLE, VorbisCommentFieldKey.SUBTITLE)
           .put(FieldKey.TAGS, VorbisCommentFieldKey.TAGS)
           .put(FieldKey.TEMPO, VorbisCommentFieldKey.TEMPO)
           .put(FieldKey.TIMBRE, VorbisCommentFieldKey.TIMBRE)
           .put(FieldKey.TITLE, VorbisCommentFieldKey.TITLE)
           .put(FieldKey.TITLE_MOVEMENT, VorbisCommentFieldKey.TITLE_MOVEMENT)
           .put(FieldKey.TITLE_SORT, VorbisCommentFieldKey.TITLESORT)
           .put(FieldKey.TONALITY, VorbisCommentFieldKey.TONALITY)
           .put(FieldKey.TRACK, VorbisCommentFieldKey.TRACKNUMBER)
           .put(FieldKey.TRACK_TOTAL, VorbisCommentFieldKey.TRACKTOTAL)
           .put(FieldKey.URL_DISCOGS_ARTIST_SITE, VorbisCommentFieldKey.URL_DISCOGS_ARTIST_SITE)
           .put(FieldKey.URL_DISCOGS_RELEASE_SITE, VorbisCommentFieldKey.URL_DISCOGS_RELEASE_SITE)
           .put(FieldKey.URL_LYRICS_SITE, VorbisCommentFieldKey.URL_LYRICS_SITE)
           .put(FieldKey.URL_OFFICIAL_ARTIST_SITE, VorbisCommentFieldKey.URL_OFFICIAL_ARTIST_SITE)
           .put(FieldKey.URL_OFFICIAL_RELEASE_SITE, VorbisCommentFieldKey.URL_OFFICIAL_RELEASE_SITE)
           .put(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_ARTIST_SITE)
           .put(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, VorbisCommentFieldKey.URL_WIKIPEDIA_RELEASE_SITE)
           .put(FieldKey.WORK, VorbisCommentFieldKey.WORK)
           .put(FieldKey.WORK_TYPE, VorbisCommentFieldKey.WORK_TYPE)
           .put(FieldKey.YEAR, VorbisCommentFieldKey.DATE);
    return builder.build();
  }

  /**
   * @return the vendor, generically known as the encoder
   */
  public String getVendor() {
    return getFirst(VENDOR.getFieldName());
  }

  /**
   * Set the vendor, known as the encoder  generally
   * <p>
   * We dont want this to be blank, when written to file this field is written to a different location
   * to all other fields but user of library can just reat it as another field
   */
  void setVendor(String vendor) {
    if (vendor == null) {
      vendor = DEFAULT_VENDOR;
    }
    super.setField(new VorbisCommentTagField(VENDOR.getFieldName(), vendor));
  }

  @Override public ImmutableSet<FieldKey> getSupportedFields() {
    return tagFieldToOggField.keySet();
  }

  @Override public Optional<String> getValue(final FieldKey genericKey, final int index) throws IllegalArgumentException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (genericKey == FieldKey.ALBUM_ARTIST) {
      switch (TagOptionSingleton.getInstance().getVorbisAlbumArtisReadOptions()) {
        case READ_ALBUMARTIST:
          return getValue(VorbisCommentFieldKey.ALBUMARTIST.getFieldName(), index);
        case READ_JRIVER_ALBUMARTIST:
          return getValue(VorbisCommentFieldKey.ALBUMARTIST_JRIVER.getFieldName(), index);
        case READ_ALBUMARTIST_THEN_JRIVER: {
          Optional<String> value = getValue(VorbisCommentFieldKey.ALBUMARTIST.getFieldName(), index);
          if (!value.isPresent()) {
            return getValue(VorbisCommentFieldKey.ALBUMARTIST_JRIVER.getFieldName(), index);
          } else {
            return value;
          }
        }
        case READ_JRIVER_THEN_ALBUMARTIST: {
          Optional<String> value = getValue(VorbisCommentFieldKey.ALBUMARTIST_JRIVER.getFieldName(), index);
          if (!value.isPresent()) {
            return getValue(VorbisCommentFieldKey.ALBUMARTIST.getFieldName(), index);
          } else {
            return value;
          }
        }
      }
    }
    return getValue(getVorbisCommentFieldKey(genericKey).getFieldName(), index);
  }

  public String getFieldAt(FieldKey genericKey, int index) throws IllegalArgumentException, UnsupportedFieldException {
    return getValue(genericKey, index).or("");
  }

  public List<String> getAll(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return super.getAll(getVorbisCommentFieldKey(genericKey).getFieldName());
  }

  public Tag deleteField(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    if (genericKey == FieldKey.ALBUM_ARTIST) {
      TagOptionSingleton.getInstance()
                        .getVorbisAlbumArtistSaveOptions()
                        .deleteField(this, getVorbisCommentFieldKey(genericKey));
    } else {
      VorbisCommentFieldKey vorbisCommentFieldKey = getVorbisCommentFieldKey(genericKey);
      deleteField(vorbisCommentFieldKey);
    }
    return this;
  }

  /**
   * @return list of artwork images
   */
  public List<Artwork> getArtworkList() throws UnsupportedFieldException {
    List<Artwork> artworkList = new ArrayList<>(1);

    //Read Old Format
    if (getArtworkBinaryData() != null & getArtworkBinaryData().length > 0) {
      Artwork artwork = ArtworkFactory.getNew();
      artwork.setMimeType(getArtworkMimeType());
      artwork.setBinaryData(getArtworkBinaryData());
      artworkList.add(artwork);
    }

    //New Format (Supports Multiple Images)
    List<TagField> metadataBlockPics = this.get(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);
    for (TagField tagField : metadataBlockPics) {

      try {
        byte[] imageBinaryData = Base64Coder.decode(((TagTextField)tagField).getContent());
        MetadataBlockDataPicture coverArt = new MetadataBlockDataPicture(ByteBuffer.wrap(imageBinaryData));
        Artwork artwork = ArtworkFactory.createArtworkFromMetadataBlockDataPicture(coverArt);
        artworkList.add(artwork);
      } catch (IOException | InvalidFrameException ioe) {
        throw new RuntimeException(ioe);
      }
    }
    return artworkList;
  }

  public List<TagField> get(VorbisCommentFieldKey vorbisCommentKey) throws IllegalArgumentException {
    return getFieldList(checkArgNotNull(vorbisCommentKey).getFieldName());
  }

  /**
   * Retrieve artwork raw data when using the deprecated COVERART format
   */
  byte[] getArtworkBinaryData() {
    String base64data = this.getFirst(VorbisCommentFieldKey.COVERART);
    return Base64Coder.decode(base64data.toCharArray());
  }

  public String getFirst(VorbisCommentFieldKey vorbisCommentKey) throws IllegalArgumentException {
    return super.getFirst(checkArgNotNull(vorbisCommentKey).getFieldName());
  }

  /**
   * Retrieve artwork mimeType when using deprecated COVERART format
   *
   * @return mimetype
   */
  String getArtworkMimeType() {
    return this.getFirst(VorbisCommentFieldKey.COVERARTMIME);
  }

  /**
   * Create Tag Field using generic key
   */
  @Override
  public TagField createField(FieldKey genericKey, String... values) throws IllegalArgumentException,
                                                                            UnsupportedFieldException,
                                                                            FieldDataInvalidException {
    return createField(getVorbisCommentFieldKey(checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey")),
                       checkVarArg0NotNull(values, AT_LEAST_ONE_REQUIRED, "values"));
  }

  @Override
  public TagField createField(VorbisCommentFieldKey vorbisCommentFieldKey, String value) throws UnsupportedFieldException,
                                                                                                FieldDataInvalidException {
    return new VorbisCommentTagField(vorbisCommentFieldKey.getFieldName(), value);
  }

  public TagField createArtwork(Artwork artwork) throws UnsupportedFieldException, FieldDataInvalidException {
    try {
      return createField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE,
                         new String(Base64Coder.encode(createMetadataBlockDataPicture(artwork).getRawContent())));
    } catch (UnsupportedEncodingException uee) {
      throw new FieldDataInvalidException(uee);
    }
  }

  public ImmutableList<TagField> getFields(FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return super.getFields(getVorbisCommentFieldKey(genericKey).getFieldName());
  }

  private VorbisCommentFieldKey getVorbisCommentFieldKey(final FieldKey genericKey) throws IllegalArgumentException,
                                                                                           UnsupportedFieldException {
    checkArgNotNull(genericKey, CANNOT_BE_NULL, "genericKey");
    final VorbisCommentFieldKey vorbisCommentFieldKey = tagFieldToOggField.get(genericKey);
    if (vorbisCommentFieldKey == null) {
      throw new UnsupportedFieldException(genericKey.name());
    }
    return vorbisCommentFieldKey;
  }

  public Optional<TagField> getFirstField(final FieldKey genericKey)
      throws IllegalArgumentException, UnsupportedFieldException {
    return getFirstField(getVorbisCommentFieldKey(genericKey).getFieldName());
  }

  public TagField createCompilationField(boolean value) throws UnsupportedFieldException {
    try {
      return createField(FieldKey.IS_COMPILATION, String.valueOf(value));
    } catch (FieldDataInvalidException e) {
      throw new RuntimeException(e);  // should never happen unless library misconfiguration
    }
  }

  /**
   * Create MetadataBlockPicture field, this is the preferred way of storing artwork in VorbisComment tag now but
   * has to be base encoded to be stored in VorbisComment
   *
   * @return MetadataBlockDataPicture
   */
  private MetadataBlockDataPicture createMetadataBlockDataPicture(Artwork artwork) throws FieldDataInvalidException {
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
        throw new FieldDataInvalidException("Unable to create MetadataBlockDataPicture from buffered");
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

  public void deleteField(VorbisCommentFieldKey vorbisCommentFieldKey) throws IllegalArgumentException {
    super.deleteField(checkArgNotNull(vorbisCommentFieldKey).getFieldName());
  }

  public boolean hasField(VorbisCommentFieldKey vorbisFieldKey) {
    return getFieldList(vorbisFieldKey.getFieldName()).size() != 0;
  }

  /**
   * Is this tag empty
   * <p>
   * <p>Overridden because check for size of one because there is always a vendor tag unless just
   * created an empty vorbis tag as part of flac tag in which case size could be zero
   *
   * @see ealvatag.tag.Tag#isEmpty()
   */
  public boolean isEmpty() {
    return getFieldsMapSize() <= 1;
  }

  public boolean hasField(FieldKey genericKey) throws IllegalArgumentException, UnsupportedFieldException {
    return getFieldList(getVorbisCommentFieldKey(genericKey).getFieldName()).size() != 0;
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
    String value = checkVarArg0NotNull(values, AT_LEAST_ONE_REQUIRED, "values");
    if (genericKey == FieldKey.ALBUM_ARTIST) {
      TagOptionSingleton.getInstance().getVorbisAlbumArtistSaveOptions().addField(this, genericKey, value);
    } else {
      TagField tagfield = createField(genericKey, value);
      addField(tagfield);
    }
    return this;
  }

  @Override
  public Tag setArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    setField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));

    //If worked okay above then that should be first artwork and if we still had old coverart format
    //that should be removed
    if (getFirst(VorbisCommentFieldKey.COVERART).length() > 0) {
      deleteField(VorbisCommentFieldKey.COVERART);
      deleteField(VorbisCommentFieldKey.COVERARTMIME);
    }
    return this;
  }

  public Tag addArtwork(Artwork artwork) throws IllegalArgumentException, UnsupportedFieldException, FieldDataInvalidException {
    addField(createArtwork(checkArgNotNull(artwork, CANNOT_BE_NULL, "artwork")));
    return this;
  }

  public Tag deleteArtwork() throws UnsupportedFieldException {
    //New Method
    deleteField(VorbisCommentFieldKey.METADATA_BLOCK_PICTURE);

    //Old Method
    deleteField(VorbisCommentFieldKey.COVERART);
    deleteField(VorbisCommentFieldKey.COVERARTMIME);
    return this;
  }

  protected boolean isAllowedEncoding(Charset enc) {
    return enc.equals(VorbisHeader.CHARSET_UTF_8);
  }

  /**
   * {@inheritDoc}
   * <p>
   * There can only be one vendor set
   */
  public void addField(TagField field) {
    if (field.getId().equals(VorbisCommentFieldKey.VENDOR.getFieldName())) {
      super.setField(field);
    } else {
      super.addField(field);
    }
  }

  public String toString() {
    return "OGG " + super.toString();
  }

  /**
   * Create artwork field using the non-standard COVERART tag
   * <p>
   * <p>
   * Actually create two fields , the data field and the mimetype. Its is not recommended that you use this
   * method anymore.
   *
   * @param data     raw image data
   * @param mimeType mimeType of data
   *
   * @return
   */
  @Deprecated
  public void setArtworkField(byte[] data, String mimeType) {
    char[] testdata = Base64Coder.encode(data);
    String base64image = new String(testdata);
    VorbisCommentTagField dataField =
        new VorbisCommentTagField(VorbisCommentFieldKey.COVERART.getFieldName(), base64image);
    VorbisCommentTagField mimeField =
        new VorbisCommentTagField(VorbisCommentFieldKey.COVERARTMIME.getFieldName(), mimeType);

    setField(dataField);
    setField(mimeField);

  }

  public void setField(String vorbisCommentKey, String value) throws IllegalArgumentException, FieldDataInvalidException {
    TagField tagfield = createField(vorbisCommentKey, value);
    setField(tagfield);
  }

  /**
   * Create Tag Field using ogg key
   * <p>
   * This method is provided to allow you to create key of any value because VorbisComment allows
   * arbitary keys.
   *
   * @param vorbisCommentFieldKey
   * @param value
   *
   * @return
   */
  public TagField createField(String vorbisCommentFieldKey, String value) {
    checkArgNotNull(value);
    return new VorbisCommentTagField(vorbisCommentFieldKey, value);
  }

  public void addField(String vorbisCommentKey, String value) throws IllegalArgumentException, FieldDataInvalidException {
    TagField tagfield = createField(vorbisCommentKey, value);
    addField(tagfield);
  }
}

