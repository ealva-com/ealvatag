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
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.logging.ErrorMessage;
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
import okio.BufferedSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Represents an mp4 trak box
 * <p>
 * Created by Eric A. Snell on 2/3/17.
 */
public class Mp4IlstBox {
    private static final Logger LOG = LoggerFactory.getLogger(Mp4IlstBox.class);

    public Mp4IlstBox(final Mp4BoxHeader ilstBoxHeader,
                      final BufferedSource bufferedSource,
                      final Mp4Tag mp4Tag) throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.ILST.matches(ilstBoxHeader.getId()));

        int dataSize = ilstBoxHeader.getDataLength();
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH) {
            Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
            if (childHeader.getDataLength() > 0) {        //Header with no data #JAUDIOTAGGER-463
                // throwing it in a byte buffer and refactor createMp4Field when I'm lucid
                createMp4Field(mp4Tag, childHeader, ByteBuffer.wrap(bufferedSource.readByteArray(childHeader.getDataLength())));
            }
            dataSize -= childHeader.getLength();
        }
        bufferedSource.skip(dataSize);
    }

    private void createMp4Field(Mp4Tag tag, Mp4BoxHeader header, ByteBuffer raw) throws UnsupportedEncodingException {
        //Header with no data #JAUDIOTAGGER-463. Just ignore boxes with no data
        if (header.getDataLength() != 0) {
            if (header.getId().equals(Mp4TagReverseDnsField.IDENTIFIER)) {  //Reverse Dns Atom
                try {
                    TagField field = new Mp4TagReverseDnsField(header, raw);
                    tag.addField(field);
                } catch (Exception e) {
                    LOG.warn(ErrorMessage.MP4_UNABLE_READ_REVERSE_DNS_FIELD.getMsg(e.getMessage()));
                    TagField field = new Mp4TagRawBinaryField(header, raw);
                    tag.addField(field);
                }
            } else {  //Normal Parent with Data atom
                int currentPos = raw.position();
                boolean isDataIdentifier = Utils.getString(raw,
                                                           Mp4BoxHeader.IDENTIFIER_POS,
                                                           Mp4BoxHeader.IDENTIFIER_LENGTH,
                                                           StandardCharsets.ISO_8859_1).equals(Mp4DataBox.IDENTIFIER);
                raw.position(currentPos);
                if (isDataIdentifier) {
                    //Need this to decide what type of Field to create
                    int type = Utils.getIntBE(raw,
                                              Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                              Mp4DataBox.TYPE_POS_INCLUDING_HEADER + Mp4DataBox.TYPE_LENGTH - 1);
                    Mp4FieldType fieldType = Mp4FieldType.getFieldType(type);
                    LOG.debug("Box Type id:" + header.getId() + ":type:" + fieldType);

                    //Special handling for some specific identifiers otherwise just base on class id
                    if (header.getId().equals(Mp4FieldKey.TRACK.getFieldName())) {
                        TagField field = new Mp4TrackField(header.getId(), raw);
                        tag.addField(field);
                    } else if (header.getId().equals(Mp4FieldKey.DISCNUMBER.getFieldName())) {
                        TagField field = new Mp4DiscNoField(header.getId(), raw);
                        tag.addField(field);
                    } else if (header.getId().equals(Mp4FieldKey.GENRE.getFieldName())) {
                        TagField field = new Mp4GenreField(header.getId(), raw);
                        tag.addField(field);
                    } else if (header.getId().equals(Mp4FieldKey.ARTWORK.getFieldName()) ||
                            Mp4FieldType.isCoverArtType(fieldType)) {
                        int processedDataSize = 0;
                        int imageCount = 0;
                        //The loop should run for each image (each data atom)
                        while (processedDataSize < header.getDataLength()) {
                            //There maybe a mixture of PNG and JPEG images so have to check type
                            //for each subimage (if there are more than one image)
                            if (imageCount > 0) {
                                type = Utils.getIntBE(raw, processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                                      processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER +
                                                              Mp4DataBox.TYPE_LENGTH - 1);
                                fieldType = Mp4FieldType.getFieldType(type);
                            }
                            Mp4TagCoverField field = new Mp4TagCoverField(raw, fieldType);
                            tag.addField(field);
                            processedDataSize += field.getDataAndHeaderSize();
                            imageCount++;
                        }
                    } else if (fieldType == Mp4FieldType.TEXT) {
                        TagField field = new Mp4TagTextField(header.getId(), raw);
                        tag.addField(field);
                    } else if (fieldType == Mp4FieldType.IMPLICIT) {
                        TagField field = new Mp4TagTextNumberField(header.getId(), raw);
                        tag.addField(field);
                    } else if (fieldType == Mp4FieldType.INTEGER) {
                        TagField field = new Mp4TagByteField(header.getId(), raw);
                        tag.addField(field);
                    } else {
                        boolean existingId = false;
                        for (Mp4FieldKey key : Mp4FieldKey.values()) {
                            if (key.getFieldName().equals(header.getId())) {
                                //The parentHeader is a known id but its field type is not one of the expected types so
                                //this field is invalid. i.e I received a file with the TMPO set to 15 (Oxf) when it should
                                //be 21 (ox15) so looks like somebody got their decimal and hex numbering confused
                                //So in this case best to ignore this field and just write a warning
                                existingId = true;
                                LOG.warn("Known Field:" + header.getId() + " with invalid field type of:" + type +
                                                 " is ignored");
                                break;
                            }
                        }

                        //Unknown field id with unknown type so just create as binary
                        if (!existingId) {
                            LOG.warn("UnKnown Field:" + header.getId() + " with invalid field type of:" + type +
                                             " created as binary");
                            TagField field = new Mp4TagBinaryField(header.getId(), raw);
                            tag.addField(field);
                        }
                    }
                } else {                 //Special Cases
                    //MediaMonkey 3 CoverArt Attributes field, does not have data items so just
                    //copy parent and child as is without modification
                    if (header.getId().equals(Mp4NonStandardFieldKey.AAPR.getFieldName())) {
                        TagField field = new Mp4TagRawBinaryField(header, raw);
                        tag.addField(field);
                    } else {
                        //Default case
                        TagField field = new Mp4TagRawBinaryField(header, raw);
                        tag.addField(field);
                    }
                }
            }
        }
    }

// Original, keep for readily available documentation for now
//    /**
//     * Process the field and add to the tag
//     * <p>
//     * Note:In the case of coverart MP4 holds all the coverart within individual dataitems all within
//     * a single covr atom, we will add separate mp4field for each image.
//     *
//     * @param tag
//     * @param header
//     * @param raw
//     *
//     * @return
//     *
//     * @throws UnsupportedEncodingException
//     */
//    private void createMp4Field(Mp4Tag tag, Mp4BoxHeader header, Buffer raw) throws UnsupportedEncodingException {
//        if (header.getId().equals(Mp4TagReverseDnsField.IDENTIFIER)) {
//            Reverse Dns Atom
//            try {
//                TagField field = new Mp4TagReverseDnsField(header, raw);
//                tag.addField(field);
//            } catch (Exception e) {
//                LOG.warn(ErrorMessage.MP4_UNABLE_READ_REVERSE_DNS_FIELD.getMsg(e.getMessage()));
//                TagField field = new Mp4TagRawBinaryField(header, raw);
//                tag.addField(field);
//            }
//        }
//        else {
//            //Normal Parent with Data atom
//            boolean isDataIdentifier = Utils.getString(raw,
//                                                       Mp4BoxHeader.IDENTIFIER_POS,
//                                                       Mp4BoxHeader.IDENTIFIER_LENGTH,
//                                                       StandardCharsets.ISO_8859_1).equals(Mp4DataBox.IDENTIFIER);
//            if (isDataIdentifier) {
//                //Need this to decide what type of Field to create
//                int type = Utils.getIntBE(raw,
//                                          Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
//                                          Mp4DataBox.TYPE_POS_INCLUDING_HEADER + Mp4DataBox.TYPE_LENGTH - 1);
//                Mp4FieldType fieldType = Mp4FieldType.getFieldType(type);
//                LOG.debug("Box Type id:" + header.getId() + ":type:" + fieldType);
//
//                //Special handling for some specific identifiers otherwise just base on class id
//                if (header.getId().equals(Mp4FieldKey.TRACK.getFieldName())) {
//                    TagField field = new Mp4TrackField(header.getId(), raw);
//                    tag.addField(field);
//                } else if (header.getId().equals(Mp4FieldKey.DISCNUMBER.getFieldName())) {
//                    TagField field = new Mp4DiscNoField(header.getId(), raw);
//                    tag.addField(field);
//                } else if (header.getId().equals(Mp4FieldKey.GENRE.getFieldName())) {
//                    TagField field = new Mp4GenreField(header.getId(), raw);
//                    tag.addField(field);
//                } else if (header.getId().equals(Mp4FieldKey.ARTWORK.getFieldName()) ||
//                        Mp4FieldType.isCoverArtType(fieldType)) {
//                    int processedDataSize = 0;
//                    int imageCount = 0;
//                    //The loop should run for each image (each data atom)
//                    while (processedDataSize < header.getDataLength()) {
//                        //There maybe a mixture of PNG and JPEG images so have to check type
//                        //for each subimage (if there are more than one image)
//                        if (imageCount > 0) {
//                            type = Utils.getIntBE(raw, processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
//                                                  processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER +
//                                                          Mp4DataBox.TYPE_LENGTH - 1);
//                            fieldType = Mp4FieldType.getFieldType(type);
//                        }
//                        Mp4TagCoverField field = new Mp4TagCoverField(raw, fieldType);
//                        tag.addField(field);
//                        processedDataSize += field.getDataAndHeaderSize();
//                        imageCount++;
//                    }
//                } else if (fieldType == Mp4FieldType.TEXT) {
//                    TagField field = new Mp4TagTextField(header.getId(), raw);
//                    tag.addField(field);
//                } else if (fieldType == Mp4FieldType.IMPLICIT) {
//                    TagField field = new Mp4TagTextNumberField(header.getId(), raw);
//                    tag.addField(field);
//                } else if (fieldType == Mp4FieldType.INTEGER) {
//                    TagField field = new Mp4TagByteField(header.getId(), raw);
//                    tag.addField(field);
//                } else {
//                    boolean existingId = false;
//                    for (Mp4FieldKey key : Mp4FieldKey.values()) {
//                        if (key.getFieldName().equals(header.getId())) {
//                            //The parentHeader is a known id but its field type is not one of the expected types so
//                            //this field is invalid. i.e I received a file with the TMPO set to 15 (Oxf) when it should
//                            //be 21 (ox15) so looks like somebody got their decimal and hex numbering confused
//                            //So in this case best to ignore this field and just write a warning
//                            existingId = true;
//                            LOG.warn("Known Field:" + header.getId() + " with invalid field type of:" + type +
//                                             " is ignored");
//                            break;
//                        }
//                    }
//
//                    //Unknown field id with unknown type so just create as binary
//                    if (!existingId) {
//                        LOG.warn("UnKnown Field:" + header.getId() + " with invalid field type of:" + type +
//                                         " created as binary");
//                        TagField field = new Mp4TagBinaryField(header.getId(), raw);
//                        tag.addField(field);
//                    }
//                }
//            }
//            //Special Cases
//            else {
//                //MediaMonkey 3 CoverArt Attributes field, does not have data items so just
//                //copy parent and child as is without modification
//                if (header.getId().equals(Mp4NonStandardFieldKey.AAPR.getFieldName())) {
//                    TagField field = new Mp4TagRawBinaryField(header, raw);
//                    tag.addField(field);
//                }
//                //Default case
//                else {
//                    TagField field = new Mp4TagRawBinaryField(header, raw);
//                    tag.addField(field);
//                }
//            }
//        }

//    }
}
