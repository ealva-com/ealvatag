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

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyTXXX
    extends AbstractFrameBodyTextInfo  implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyTXXX datatype.
     */
    public FrameBodyTXXX()
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(TextEncoding.ISO_8859_1));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_TEXT, "");

    }

    public FrameBodyTXXX(FrameBodyTXXX body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyTXXX datatype.
     *
     * @param textEncoding 
     * @param description  
     * @param text         
     */
    public FrameBodyTXXX(byte textEncoding, String description, String text)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    /**
     * Creates a new FrameBodyTXXX datatype.
     *
     * @throws InvalidTagException 
     */
    public FrameBodyTXXX(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     *  Set the desciption field
     *
     * @param description 
     */
    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * 
     *
     * @return the description field
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
        return ID3v24Frames.FRAME_ID_USER_DEFINED_INFO;
    }

    /**
     * This is different to other text Frames
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }

}
