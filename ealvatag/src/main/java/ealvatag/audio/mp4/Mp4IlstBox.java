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

package ealvatag.audio.mp4;

import com.google.common.base.Preconditions;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.utils.StandardCharsets;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.logging.ErrorMessage;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagField;
import ealvatag.tag.mp4.Mp4FieldKey;
import ealvatag.tag.mp4.Mp4NonStandardFieldKey;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.mp4.atom.Mp4DataBox;
import ealvatag.tag.mp4.field.Mp4DiscNoField;
import ealvatag.tag.mp4.field.Mp4FieldType;
import ealvatag.tag.mp4.field.Mp4GenreField;
import ealvatag.tag.mp4.field.Mp4TagBinaryField;
import ealvatag.tag.mp4.field.Mp4TagByteField;
import ealvatag.tag.mp4.field.Mp4TagCoverField;
import ealvatag.tag.mp4.field.Mp4TagRawBinaryField;
import ealvatag.tag.mp4.field.Mp4TagReverseDnsField;
import ealvatag.tag.mp4.field.Mp4TagTextField;
import ealvatag.tag.mp4.field.Mp4TagTextNumberField;
import ealvatag.tag.mp4.field.Mp4TrackField;
import ealvatag.utils.Buffers;
import okio.BufferedSource;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents an mp4 trak box
 * <p>
 * Created by Eric A. Snell on 2/3/17.
 */
public class Mp4IlstBox {
  private static final JLogger LOG = JLoggers.get(Mp4IlstBox.class, EalvaTagLog.MARKER);

  public Mp4IlstBox(final Mp4BoxHeader ilstBoxHeader,
                    final BufferedSource bufferedSource,
                    final Mp4Tag mp4Tag,
                    final boolean ignoreArtwork) throws IOException, CannotReadException {
    Preconditions.checkArgument(Mp4AtomIdentifier.ILST.matches(ilstBoxHeader.getId()));

    int dataSize = ilstBoxHeader.getDataLength();
    while (dataSize >= Mp4BoxHeader.HEADER_LENGTH) {
      Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
      final int dataLength = childHeader.getDataLength();
      if (dataLength > 0) {        //Header with no data #JAUDIOTAGGER-463
        if (Mp4TagReverseDnsField.IDENTIFIER.equals(childHeader.getId())) {  //Reverse Dns Atom
          handleReverseDns(mp4Tag,
                           childHeader,
                           ByteBuffer.wrap(bufferedSource.readByteArray(dataLength)));
        } else {
          final boolean isDataIdentifier = Mp4DataBox.IDENTIFIER.equals(Buffers.peekString(bufferedSource,
                                                                                           Mp4BoxHeader.IDENTIFIER_POS,
                                                                                           Mp4BoxHeader.IDENTIFIER_LENGTH,
                                                                                           StandardCharsets.ISO_8859_1));
          Mp4FieldType fieldType = Mp4FieldType.getFieldType(Buffers.peek3ByteInt(bufferedSource,
                                                                                  Mp4DataBox.TYPE_POS_INCLUDING_HEADER));

          if (isArtworkField(childHeader, fieldType)) {
            if (ignoreArtwork) {
              mp4Tag.markReadOnly();
              bufferedSource.skip(dataLength);
            } else {
              handleArtwork(mp4Tag, childHeader, ByteBuffer.wrap(bufferedSource.readByteArray(dataLength)),
                            fieldType);
            }
          } else {
            // throwing it in a byte buffer and refactor createMp4Field when I'm lucid
            createMp4Field(mp4Tag,
                           childHeader,
                           ByteBuffer.wrap(bufferedSource.readByteArray(dataLength)),
                           isDataIdentifier,
                           fieldType);
          }
        }
      }
      dataSize -= childHeader.getLength();
    }
    if (dataSize > 0) {
      LOG.log(DEBUG, "%s did not fully read. Skipping %s", getClass(), dataSize);
      bufferedSource.skip(dataSize);
    }
  }

  private void handleReverseDns(final Mp4Tag tag,
                                final Mp4BoxHeader header,
                                final ByteBuffer byteBuffer) throws UnsupportedEncodingException {
    try {
      TagField field = new Mp4TagReverseDnsField(header, byteBuffer);
      tag.addField(field);
    } catch (Exception e) {
      LOG.log(WARN, ErrorMessage.MP4_UNABLE_READ_REVERSE_DNS_FIELD, e);
      TagField field = new Mp4TagRawBinaryField(header, byteBuffer);
      tag.addField(field);
    }
  }

  private void createMp4Field(Mp4Tag tag,
                              Mp4BoxHeader header,
                              ByteBuffer raw,
                              final boolean isDataIdentifier,
                              Mp4FieldType fieldType) throws UnsupportedEncodingException {
    final String id = header.getId();
    if (isDataIdentifier) {
      //Special handling for some specific identifiers otherwise just base on class id
      if (Mp4FieldKey.TRACK.fieldMatchesId(id)) {
        tag.addField(new Mp4TrackField(id, raw));
      } else if (Mp4FieldKey.DISCNUMBER.fieldMatchesId(id)) {
        tag.addField(new Mp4DiscNoField(id, raw));
      } else if (Mp4FieldKey.GENRE.fieldMatchesId(id)) {
        tag.addField(new Mp4GenreField(id, raw));
      } else if (fieldType == Mp4FieldType.TEXT) {
        tag.addField(new Mp4TagTextField(id, raw));
      } else if (fieldType == Mp4FieldType.IMPLICIT) {
        tag.addField(new Mp4TagTextNumberField(id, raw));
      } else if (fieldType == Mp4FieldType.INTEGER) {
        tag.addField(new Mp4TagByteField(id, raw));
      } else {
        boolean existingId = false;
        for (Mp4FieldKey key : Mp4FieldKey.values()) {
          if (key.fieldMatchesId(id)) {
            //The parentHeader is a known id but its field type is not one of the expected types so
            //this field is invalid. i.e I received a file with the TMPO set to 15 (Oxf) when it should
            //be 21 (ox15) so looks like somebody got their decimal and hex numbering confused
            //So in this case best to ignore this field and just write a warning
            existingId = true;
            LOG.log(WARN, "Known Field:%s with invalid field type of:%s is ignored", id, fieldType);
            break;
          }
        }

        //Unknown field id with unknown type so just create as binary
        if (!existingId) {
          LOG.log(WARN, "UnKnown Field:%s with invalid field type of:%s", id, fieldType);
          tag.addField(new Mp4TagBinaryField(id, raw));
        }
      }
    } else {                 //Special Cases
      //MediaMonkey 3 CoverArt Attributes field, does not have data items so just
      //copy parent and child as is without modification
      if (Mp4NonStandardFieldKey.AAPR.matchesIdentifier(id)) {
        tag.addField(new Mp4TagRawBinaryField(header, raw));
      } else {
        //Default case
        tag.addField(new Mp4TagRawBinaryField(header, raw));
      }
    }
  }

  private void handleArtwork(final Mp4Tag tag,
                             final Mp4BoxHeader header,
                             final ByteBuffer raw,
                             Mp4FieldType fieldType) throws UnsupportedEncodingException {
    int processedDataSize = 0;
    int imageCount = 0;
    //The loop should run for each image (each data atom)
    while (processedDataSize < header.getDataLength()) {
      //There maybe a mixture of PNG and JPEG images so have to check type
      //for each subimage (if there are more than one image)
      if (imageCount > 0) {
        int type = Utils.getIntBE(raw, processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                  processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER +
                                      Mp4DataBox.TYPE_LENGTH - 1);
        fieldType = Mp4FieldType.getFieldType(type);
      }
      Mp4TagCoverField field = new Mp4TagCoverField(raw, fieldType);
      tag.addField(field);
      processedDataSize += field.getDataAndHeaderSize();
      imageCount++;
    }
  }

  private boolean isArtworkField(final Mp4BoxHeader header, final Mp4FieldType fieldType) {
    return Mp4FieldKey.ARTWORK.fieldMatchesId(header.getId()) || Mp4FieldType.isCoverArtType(fieldType);
  }

}
