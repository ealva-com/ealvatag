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
package org.jaudiotagger.tag.mp4;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.util.Mp4MetaBox;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;
import org.jaudiotagger.audio.generic.Utils;

/**
 * Reads metadata from mp4, the metadata tags are held under the ilst atom as shown below
 * 
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
 * |.................................|---- name
 * |................................. ecetera
 * |.........................|---- ---- (Optional for reverse dns field)
 * |.................................|-- mean
 * |.................................|-- name
 * |.................................|-- data
 * |.................................... ecetere
 * |
 * |--- mdat
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
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();
        
        //Level 2-Searching for "udta" within "moov"
        Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.UDTA.getFieldName());

        //Level 3-Searching for "meta" within udta
        Mp4BoxHeader boxHeader  = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.META.getFieldName());
        Mp4MetaBox meta = new Mp4MetaBox(boxHeader,moovBuffer);
        meta.processData();

        //Level 4- Search for "ilst" within meta
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.ILST.getFieldName());

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
            tag.add(createMp4Field(boxHeader, metadataBuffer.slice()));

            //Move position in buffer to the start of the next header
            metadataBuffer.position(metadataBuffer.position() + boxHeader.getDataLength());
            read += boxHeader.getLength();
        }
        return tag;
    }

    private Mp4TagField createMp4Field(Mp4BoxHeader header, ByteBuffer raw) throws UnsupportedEncodingException
    {
        if(header.getId().equals(Mp4TagReverseDnsField.IDENTIFIER))
        {
            return new Mp4TagReverseDnsField(header.getId(),raw);
        }
        else
        {
            //Need this to decide what type of Field to create
            int type = Utils.getNumberBigEndian(raw,
                                                Mp4DataBox.TYPE_POS_INCLUDING_HEADER,
                                                Mp4DataBox.TYPE_POS_INCLUDING_HEADER + Mp4DataBox.TYPE_LENGTH - 1);

            logger.info("Box Type id:"+header.getId()+":type:"+type);

            //Special handling for dsome specific identifiers otherwise just base on class id
            if(header.getId().equals(Mp4FieldKey.TRACK.getFieldName()))
            {
                 return new Mp4TrackField(header.getId(), raw);
            }
            else if(header.getId().equals(Mp4FieldKey.DISCNUMBER.getFieldName()))
            {
                 return new Mp4DiscNoField(header.getId(), raw);
            }
            else if(type==Mp4FieldType.TEXT.getFileClassId())
            {
                return new Mp4TagTextField(header.getId(), raw);
            }
            else if(type==Mp4FieldType.NUMERIC.getFileClassId())
            {
                return new Mp4TagTextNumberField(header.getId(), raw);
            }
            else if(type==Mp4FieldType.BYTE.getFileClassId())
            {
                return new Mp4TagByteField(header.getId(), raw); 
            }
            else if(type==Mp4FieldType.COVERART.getFileClassId())
            {
                return new  Mp4TagCoverField(raw);
            }
            else
            {
                //TODO Try binary is this right?
                return new Mp4TagBinaryField(header.getId(), raw);
            }
        }
    }
}
