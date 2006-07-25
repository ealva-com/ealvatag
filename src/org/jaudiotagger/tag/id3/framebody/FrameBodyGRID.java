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

import org.jaudiotagger.tag.datatype.ByteArraySizeTerminated;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FrameBodyGRID extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyGRID datatype.
     */
    public FrameBodyGRID()
    {
        //        this.setObject(ObjectTypes.OBJ_OWNER, "");
        //        this.setObject("Group Symbol", new Byte((byte) 0));
        //        this.setObject("Group Dependent Data", new byte[0]);
    }

    public FrameBodyGRID(FrameBodyGRID body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyGRID datatype.
     *
     * @param owner       DOCUMENT ME!
     * @param groupSymbol DOCUMENT ME!
     * @param data        DOCUMENT ME!
     */
    public FrameBodyGRID(String owner, byte groupSymbol, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, new Byte(groupSymbol));
        this.setObjectValue(DataTypes.OBJ_GROUP_DATA, data);
    }

    /**
     * Creates a new FrameBodyGRID datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer 
     */
    public FrameBodyGRID(ByteBuffer byteBuffer, int frameSize)
        throws  InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param textEncoding DOCUMENT ME!
     */
    public void setGroupSymbol(byte textEncoding)
    {
        setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, new Byte(textEncoding));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte getGroupSymbol()
    {
        if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null)
        {
            return ((Long) getObjectValue(DataTypes.OBJ_GROUP_SYMBOL)).byteValue();
        }
        else
        {
            return (byte)0;
        }
    }

     /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_GROUP_ID_REG ;
    }

    

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     */
    public void setOwner(String owner)
    {
        setObjectValue(DataTypes.OBJ_OWNER, owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_OWNER, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_GROUP_DATA, this));
    }
}
