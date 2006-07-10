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
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodySIGN extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    /**
     * Creates a new FrameBodySIGN datatype.
     */
    public FrameBodySIGN()
    {
        //        this.setObject("Group Symbol", new Byte((byte) 0));
        //        this.setObject("Signature", new byte[0]);
    }

    public FrameBodySIGN(FrameBodySIGN body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodySIGN datatype.
     *
     * @param groupSymbol DOCUMENT ME!
     * @param signature   DOCUMENT ME!
     */
    public FrameBodySIGN(byte groupSymbol, byte[] signature)
    {
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, new Byte(groupSymbol));
        this.setObjectValue(DataTypes.OBJ_SIGNATURE, signature);
    }

    /**
     * Creates a new FrameBodySIGN datatype.
     *
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodySIGN(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupSymbol DOCUMENT ME!
     */
    public void setGroupSymbol(byte groupSymbol)
    {
        setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, new Byte(groupSymbol));
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
            return ((Byte) getObjectValue(DataTypes.OBJ_GROUP_SYMBOL)).byteValue();
        }
        else
        {
            return 0;
        }
    }


    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SIGNATURE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param signature DOCUMENT ME!
     */
    public void setSignature(byte[] signature)
    {
        setObjectValue(DataTypes.OBJ_SIGNATURE, signature);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getSignature()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_SIGNATURE);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_SIGNATURE, this));
    }
}
