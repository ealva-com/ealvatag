/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.audio.mp4;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import java.util.Iterator;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.atom.Mp4MetaBox;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.mp4.field.*;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4NonStandardFieldKey;

/**
 * Reads metadata from mp4,
 *
 * <p>The metadata tags are held under the ilst atom as shown below
 *
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |.........................|
 * |.........................|---- @nam (Optional for each metadatafield)
 * |.........................|.......|-- data
 * |.........................|....... ecetera
 * |.........................|---- ---- (Optional for reverse dns field)
 * |.................................|-- mean
 * |.................................|-- name
 * |.................................|-- data
 * |.................................... ecetere
 * |
 * |--- mdat
 * </pre
 */
public class Mp4TagReader
{
    
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.mp4");

    /*
     * The metadata is stored in the box under the hierachy moov.udta.meta.ilst
     *
     * There are gaps between these boxes

     */
    public Mp4Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        Mp4Tag tag = new Mp4Tag();

        //Get to the facts everything we are interested in is within the moov box, so just load data from file
        //once so no more file I/O needed
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf,Mp4NotMetaFieldKey.MOOV.getFieldName());
        if( moovHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();
        
        //Level 2-Searching for "udta" within "moov"
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.UDTA.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        
        //Level 3-Searching for "meta" within udta
        boxHeader  = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.META.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        Mp4MetaBox meta = new Mp4MetaBox(boxHeader,moovBuffer);
        meta.processData();

        //Level 4- Search for "ilst" within meta
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.ILST.getFieldName());

        //This file does not actually contain a tag
        if(boxHeader==null)
        {
            return tag;    
        }
        //Size of metadata (exclude the size of the ilst header), take a slice starting at
        //metadata children to make things safer
        int length = boxHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH;
        ByteBuffer metadataBuffer = moovBuffer.slice();
        //Datalength is longer are there boxes after ilst at this level?
        logger.info("headerlengthsays:"+length+"datalength:"+metadataBuffer.limit());
        int read = 0;
        logger.info("Started to read metadata fields at position is in metadata buffer:"+metadataBuffer.position());
        while (read < length)
        {
            //Read the boxHeader
            boxHeader.update(metadataBuffer);

            //Create the corresponding datafield from the id, and slice the buffer so position of main buffer
            //wont get affected
            logger.info("Next position is at:"+metadataBuffer.position());
            createMp4Field(tag,boxHeader, metadataBuffer.slice());

            //Move position in buffer to the start of the next header
            metadataBuffer.position(metadataBuffer.position() + boxHeader.getDataLength());
            read += boxHeader.getLength();
        }
        return tag;
    }

    /**
     * Process the field and add to the tag
     *
     * Note:In the case of coverart MP4 holds all the coverart within individual dataitems all within
     * a single covr atom, we will add seperate mp4field for eaxh image.
     *
     * @param tag
     * @param header
     * @param raw
     * @return
     * @throws UnsupportedEncodingException
     */
    private void createMp4Field(Mp4Tag tag,Mp4BoxHeader header, ByteBuffer raw) throws UnsupportedEncodingException
    {
        //Reverse Dns Atom
        if(header.getId().equals(Mp4TagReverseDnsField.IDENTIFIER))
        {
            TagField field =  new Mp4TagReverseDnsField(raw);
            tag.add(field);
        }
        //Normal Parent with Data atom
        else
        {
            int currentPos = raw.position();
            boolean isDataIdentifier = Utils.getString(raw, Mp4BoxHeader.IDENTIFIER_POS, Mp4BoxHeader.IDENTIFIER_LENGTH,"ISO-8859-1").equals(Mp4DataBox.IDENTIFIER);
            raw.position(currentPos);
            if(isDataIdentifier)
            {
                //Need this to decide what type of Field to create
                int type = Utils.getNumberBigEndian(raw,
                                                    Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                                    Mp4DataBox.TYPE_POS_INCLUDING_HEADER + Mp4DataBox.TYPE_LENGTH - 1);

                logger.info("Box Type id:"+header.getId()+":type:"+type);

                //Special handling for some specific identifiers otherwise just base on class id
                if(header.getId().equals(Mp4FieldKey.TRACK.getFieldName()))
                {
                     TagField field =  new Mp4TrackField(header.getId(), raw);
                     tag.add(field);
                }
                else if(header.getId().equals(Mp4FieldKey.DISCNUMBER.getFieldName()))
                {
                     TagField field =   new Mp4DiscNoField(header.getId(), raw);
                     tag.add(field);
                }
                else if(header.getId().equals(Mp4FieldKey.GENRE.getFieldName()))
                {
                     TagField field =   new Mp4GenreField(header.getId(), raw);
                     tag.add(field);
                }
                else if(type==Mp4FieldType.TEXT.getFileClassId())
                {
                    TagField field =   new Mp4TagTextField(header.getId(), raw);
                    tag.add(field);
                }
                else if(type==Mp4FieldType.NUMERIC.getFileClassId())
                {
                    TagField field =  new Mp4TagTextNumberField(header.getId(), raw);
                    tag.add(field);
                }
                else if(type==Mp4FieldType.BYTE.getFileClassId())
                {
                    TagField field =  new Mp4TagByteField(header.getId(), raw);
                    tag.add(field);
                }
                else if(type==Mp4FieldType.COVERART_JPEG.getFileClassId()||
                        type== Mp4FieldType.COVERART_PNG.getFileClassId())
                {
                    int processedDataSize = 0;
                    int imageCount =0;
                    while(processedDataSize  < header.getDataLength())
                    {
                        //There maybe a mixture of PNG and JPEG images so have to check type
                        //for each subimage (if there are more than one image)
                        if(imageCount>0)
                        {
                            type = Utils.getNumberBigEndian(raw,
                                                processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                                processedDataSize + Mp4DataBox.TYPE_POS_INCLUDING_HEADER + Mp4DataBox.TYPE_LENGTH - 1);
                        }
                        Mp4TagCoverField field = new Mp4TagCoverField(raw,type);
                        tag.add(field);
                        processedDataSize+=field.getDataSize() + Mp4BoxHeader.HEADER_LENGTH ;
                        imageCount++;
                    }
                }
                else
                {
                    boolean existingId = false;
                    for(Mp4FieldKey key:Mp4FieldKey.values())
                    {
                        if(key.getFieldName().equals(header.getId()))
                        {
                            //The header is a known id but its field type is not one of the expected types so
                            //this field is invalid. i.e I received a file with the TMPO set to 15 (Oxf) when it should
                            //be 21 (ox15) so looks like somebody got their decimal and hex numbering confused
                            //So in this case best to ignore this field and just write a warning
                            existingId=true;
                            logger.warning("Known Field:"+header.getId()+" with invalid field type of:"+type +" is ignored");
                            break;
                        }
                    }

                    //Unknown field id with unknown type so just create as binary
                    if(!existingId)
                    {
                        logger.warning("UnKnown Field:"+header.getId()+" with invalid field type of:"+type +" created as binary");
                        TagField field =  new Mp4TagBinaryField(header.getId(), raw);
                        tag.add(field);
                    }
                }
            }
            //Special Cases
            else
            {
                //MediaMonkey 3 CoverArt Attributes field, does not have data items so just
                //copy parent and child as is without modification
                if(header.getId().equals(Mp4NonStandardFieldKey.AAPR.getFieldName()))
                {
                    TagField field =  new Mp4TagRawBinaryField(header, raw);
                    tag.add(field);
                }
                //Default case
                else
                {
                    TagField field =  new Mp4TagRawBinaryField(header, raw);
                    tag.add(field);
                }
            }
        }

    }
}
