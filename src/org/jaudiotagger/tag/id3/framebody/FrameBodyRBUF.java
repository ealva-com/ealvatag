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

import org.jaudiotagger.tag.datatype.BooleanByte;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyRBUF extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyRBUF datatype.
     */
    public FrameBodyRBUF()
    {
        //        this.setObject("Buffer Size", new Byte((byte) 0));
        //        this.setObject("Embedded Info Flag", new Boolean(false));
        //        this.setObject("Offset to Next Flag", new Byte((byte) 0));
    }

    public FrameBodyRBUF(FrameBodyRBUF body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyRBUF datatype.
     *
     * @param bufferSize       DOCUMENT ME!
     * @param embeddedInfoFlag DOCUMENT ME!
     * @param offsetToNextTag  DOCUMENT ME!
     */
    public FrameBodyRBUF(byte bufferSize, boolean embeddedInfoFlag, byte offsetToNextTag)
    {
        this.setObjectValue(DataTypes.OBJ_BUFFER_SIZE, new Byte(bufferSize));
        this.setObjectValue(DataTypes.OBJ_EMBED_FLAG, new Boolean(embeddedInfoFlag));
        this.setObjectValue(DataTypes.OBJ_OFFSET, new Byte(offsetToNextTag));
    }

    /**
     * Creates a new FrameBodyRBUF datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyRBUF(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "RBUF";
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_BUFFER_SIZE, this, 3));
        objectList.add(new BooleanByte(DataTypes.OBJ_EMBED_FLAG, this, (byte) 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_OFFSET, this, 4));
    }
}
