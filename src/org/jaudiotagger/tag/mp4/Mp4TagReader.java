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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.util.Mp4Box;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;
import org.jaudiotagger.audio.generic.Utils;

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

        Mp4Box box = new Mp4Box();


        //Get to the facts
        //1-Searching for "moov"
        seek(raf, box, Mp4NotMetaFieldKey.MOOV.getFieldName());

        //2-Searching for "udta"
        seek(raf, box, Mp4NotMetaFieldKey.UDTA.getFieldName());

        //3-Searching for "meta"
        seek(raf, box, Mp4NotMetaFieldKey.META.getFieldName());

        //4-skip the meta flags
        byte[] b = new byte[4];
        raf.read(b);
        if (b[0] != 0)
        {
            throw new CannotReadException();
        }

        //5-Seek the "ilst"
        seek(raf, box, Mp4NotMetaFieldKey.ILST.getFieldName());

        //Size of metadata (exclude the size of the ilst header)
        int length = box.getLength() - Mp4Box.HEADER_LENGTH;

        int read = 0;
        while (read < length)
        {
            //Read the box
            b = new byte[Mp4Box.HEADER_LENGTH];
            raf.read(b);
            box.update(b);

            //Now read the complete child databox into a byte array , we remove the length of the parents identifier,
            //and the length of the length field of the child
            int fieldLength = box.getLength() - Mp4Box.HEADER_LENGTH;
            b = new byte[fieldLength];
            raf.read(b);

            //Create the corresponding datafield from the id
            tag.add(createMp4Field(box.getId(), b));
            read += box.getLength();
        }

        return tag;
    }

    private Mp4TagField createMp4Field(String id, byte[] raw) throws UnsupportedEncodingException
    {
        //Need this to decide what type of Field to create
        int type     = Utils.getNumberBigEndian(raw,
                                                Mp4TagTextField.TYPE_POS ,
                                                Mp4TagTextField.TYPE_POS + Mp4TagTextField.TYPE_LENGTH - 1);

        logger.fine("Box Type is:"+type);

        if(type==Mp4FieldType.TEXT.getFileClassId())
        {
            return new Mp4TagTextField(id, raw);
        }
        else if(type==Mp4FieldType.NUMERIC.getFileClassId())
        {
            return new Mp4TagTextNumberField(id, raw);
        }
        else if(type==Mp4FieldType.BYTE.getFileClassId())
        {
            //TODO Byte subclass
            return new Mp4TagTextNumberField(id, raw);
        }
        else if(type==Mp4FieldType.COVERART.getFileClassId())
        {
            return new  Mp4TagCoverField(raw);
        }
        else
        {
            //Try binary
            return new Mp4TagBinaryField(id, raw);
        }
    }

    private void seek(RandomAccessFile raf, Mp4Box box, String id) throws IOException
    {
        byte[] b = new byte[Mp4Box.HEADER_LENGTH];
        raf.read(b);
        box.update(b);

        //Unable to find id immediately after offset so goes round loop why is this
        while (!box.getId().equals(id))
        {
            raf.skipBytes(box.getLength() -Mp4Box.HEADER_LENGTH);
            raf.read(b);
            box.update(b);
        }
    }
}
