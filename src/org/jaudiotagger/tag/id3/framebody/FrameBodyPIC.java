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
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.audio.mp3.*;

import java.io.IOException;

public class FrameBodyPIC extends AbstractID3v2FrameBody implements ID3v22FrameBody
{


    /**
     * Creates a new FrameBodyPIC datatype.
     */
    public FrameBodyPIC()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte((byte) 0));
    }

    public FrameBodyPIC(FrameBodyPIC body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyPIC datatype.
     *
     * @param textEncoding DOCUMENT ME!
     * @param imageFormat  DOCUMENT ME!
     * @param pictureType  DOCUMENT ME!
     * @param description  DOCUMENT ME!
     * @param data         DOCUMENT ME!
     */
    public FrameBodyPIC(byte textEncoding, String imageFormat, byte pictureType, String description, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, imageFormat);
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, new Byte(pictureType));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data);
    }

    /**
     * Conversion from v2 PIC to v3/v4 APIC
     */
    public FrameBodyPIC(FrameBodyAPIC body)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(body.getTextEncoding()));
        this.setObjectValue(DataTypes.OBJ_IMAGE_FORMAT, ImageFormats.getFormatForMimeType((String) body.getObjectValue(DataTypes.OBJ_MIME_TYPE)));
        this.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, body.getObjectValue(DataTypes.OBJ_PICTURE_TYPE));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, body.getDescription());
        this.setObjectValue(DataTypes.OBJ_PICTURE_DATA, body.getObjectValue(DataTypes.OBJ_PICTURE_DATA));

    }

    /**
     * Creates a new FrameBodyPIC datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyPIC(java.io.RandomAccessFile file, int frameSize)
        throws IOException, InvalidTagException
    {
        super(file, frameSize);
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
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "PIC" + ((char) 0) + getDescription();
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, 1));
        objectList.add(new StringFixedLength(DataTypes.OBJ_IMAGE_FORMAT, this, 3));
        objectList.add(new NumberHashMap(DataTypes.OBJ_PICTURE_TYPE, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_PICTURE_DATA, this));
    }

}
