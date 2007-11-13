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
package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.tag.mp4.field.Mp4TagBinaryField;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents Cover Art
 *
 * <p>Note:Within this library we have a seperate TagCoverField for every image stored, however this does not map
 * very directly to how they are physically stored within a file, because all are stored under a single covr atom, so
 * a more complex conversion has to be done then for other fields when writing multiple images back to file.
 */
public class Mp4TagCoverField extends Mp4TagBinaryField
{
    //Type
    private Mp4FieldType imageType;

    /**
     * Empty CoverArt Field
     */
    public Mp4TagCoverField()
    {
        super(Mp4FieldKey.ARTWORK.getFieldName());
    }

    /**
     * Construct CoverField by reading data from audio file
     *
     * @param raw
     * @param type
     * @throws UnsupportedEncodingException
     */
    public Mp4TagCoverField(ByteBuffer raw,int type) throws UnsupportedEncodingException
    {
        super(Mp4FieldKey.ARTWORK.getFieldName(), raw);
        if(type==Mp4FieldType.COVERART_JPEG.getFileClassId())
        {
            imageType=Mp4FieldType.COVERART_JPEG;
        }
        else
        {
            imageType=Mp4FieldType.COVERART_PNG;
        }
    }

    /**
     * Construct new cover art with binarydata provided
     *
     * <p>
     * Identifies the imagetype by looking at the data, if doesnt match PNG assumes it is JPEG
     * TODO:Check how accurate is my method will it work for any PNG
     * TODO:What about if they try to add data that is corrupt or not PNG or JPG
     * @param data
     * @throws UnsupportedEncodingException
     */
    public Mp4TagCoverField(byte[] data) 
    {
        super(Mp4FieldKey.ARTWORK.getFieldName(),data);

        //Read signature
        if(
                (0x89==(data[0] & 0xff))||
                (0x50==(data[0] & 0xff))||
                (0x4E==(data[0] & 0xff))||
                (0x47==(data[0] & 0xff))
        )
        {
            imageType =Mp4FieldType.COVERART_PNG;
        }
        else
        {
            imageType =Mp4FieldType.COVERART_JPEG;
        }
    }

    /**
     * Return field type, for artwork this also identifies the imagetype
     * @return field type
     */
    public Mp4FieldType getFieldType()
    {
        return imageType;
    }

    public boolean isBinary()
    {
        return true;
    }

    
    public String toString()
    {
        switch(imageType)
        {
            case COVERART_JPEG:
                return "jpeg:"+dataBytes.length +"bytes";
            case COVERART_PNG:
                return "png"+dataBytes.length +"bytes";
            default:
                return "";
       }
    }

}
