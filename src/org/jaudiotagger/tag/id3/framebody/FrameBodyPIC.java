/**
 *  @author : Paul Taylor
 *  @author : Eric Farng
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v22Frames;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * ID3v22 Attached Picture
 * <p/>
 * <p> This frame contains a picture directly related to the audio file.
 * Image format is preferably "PNG" [PNG] or "JPG" [JFIF]. Description
 * is a short description of the picture, represented as a terminated
 * textstring. The description has a maximum length of 64 characters,
 * but may be empty. There may be several pictures attached to one file,
 * each in their individual "PIC" frame, but only one with the same
 * ontent descriptor. There may only be one picture with the picture
 * type declared as picture type $01 and $02 respectively. There is a
 * possibility to put only a link to the image file by using the 'image
 * format' "-->" and having a complete URL [URL] instead of picture data.
 * The use of linked files should however be used restrictively since
 * there is the risk of separation of files.
 */
public class FrameBodyPIC extends AbstractID3v2FrameBody implements ID3v22FrameBody
{
    public static final String IMAGE_IS_URL = "-->";

    /**
     * Creates a new FrameBodyPIC datatype.
     */
    public FrameBodyPIC()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
    }

    public FrameBodyPIC(FrameBodyPIC body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyPIC datatype.
     *
     * @param textEncoding
     * @param imageFormat
     * @param pictureType
     * @param description
     * @param data
     */
    public FrameBodyPIC(byte textEncoding, String imageFormat, byte pictureType, String description, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, imageFormat);
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, pictureType);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data);
    }

    /**
     * Conversion from v2 PIC to v3/v4 APIC
     */
    public FrameBodyPIC(FrameBodyAPIC body)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding());
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, ImageFormats.getFormatForMimeType((String) body.getObjectValue(DataTypes.OBJ_MIME_TYPE)));
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, body.getObjectValue(DataTypes.OBJ_PICTURE_TYPE));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, body.getDescription());
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, body.getObjectValue(DataTypes.OBJ_PICTURE_DATA));

    }

    /**
     * Creates a new FrameBodyPIC datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    public FrameBodyPIC(ByteBuffer byteBuffer, int frameSize)
            throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * Set a description of the image
     *
     * @param description of the image
     */
    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * Get a description of the image
     *
     * @return a description of the image
     */
    public String getDescription()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE;
    }


    /**
     * If the description cannot be encoded using current encoder, change the encoder
     */
    public void write(ByteArrayOutputStream tagBuffer)
    {
        if (((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded() == false)
        {
            this.setTextEncoding(TextEncoding.UTF_16);
        }
        super.write(tagBuffer);
    }

    /**
     * Get a description of the image
     *
     * @return a description of the image
     */
    public String getFormatType()
    {
        return (String) getObjectValue(DataTypes.OBJ_IMAGE_FORMAT);
    }

    public boolean isImageUrl()
    {
        if (getFormatType() == null)
        {
            return false;
        }
        return getFormatType().equals(IMAGE_IS_URL);
    }

    /**
     *
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringFixedLength(DataTypes.OBJ_IMAGE_FORMAT, this, 3));
        objectList.add(new NumberHashMap(DataTypes.OBJ_PICTURE_TYPE, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this));
    }

}
