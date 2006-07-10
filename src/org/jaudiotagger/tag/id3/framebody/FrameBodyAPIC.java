/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
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
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.id3.ID3Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.*;
import org.jaudiotagger.audio.mp3.*;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class FrameBodyAPIC extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{

    /**
     * Creates a new FrameBodyAPIC datatype.
     */
    public FrameBodyAPIC()
    {
        //Initilise default text encoding
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(TextEncoding.ISO_8859_1));
    }

    public FrameBodyAPIC(FrameBodyAPIC body)
    {
        super(body);
    }

    /**
     * Conversion from v2 PIC to v3/v4 APIC
     */
    public FrameBodyAPIC(FrameBodyPIC body)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(body.getTextEncoding()));
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, ImageFormats.getMimeTypeForFormat((String) body.getObjectValue(DataTypes.OBJ_IMAGE_FORMAT)));
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, body.getObjectValue(DataTypes.OBJ_PICTURE_TYPE));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, body.getDescription());
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, body.getObjectValue(DataTypes.OBJ_PICTURE_DATA));

    }

    /**
     * Creates a new FrameBodyAPIC datatype.
     *
     * @param textEncoding DOCUMENT ME!
     * @param mimeType     DOCUMENT ME!
     * @param pictureType  DOCUMENT ME!
     * @param description  DOCUMENT ME!
     * @param data         DOCUMENT ME!
     */
    public FrameBodyAPIC(byte textEncoding,
                         String mimeType,
                         byte pictureType,
                         String description,
                         byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);

        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, new Byte(pictureType));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data);
    }

    /**
     * Creates a new FrameBodyAPIC datatype.
     *
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyAPIC(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     */
    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription()
    {
        return getDescription();
    }

      /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_ATTACHED_PICTURE;
    }


    /** If the description cannot be encoded using current encoder, change the encoder */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        if (((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded() == false)
        {
            this.setTextEncoding(TextEncoding.UTF_16BE);
        }
        super.write(tagBuffer);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new NumberHashMap(DataTypes.OBJ_PICTURE_TYPE, this, PictureTypes.PICTURE_TYPE_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this));
    }
}
