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
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;

import java.io.IOException;


public class FrameBodyPRIV extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    /**
     * Creates a new FrameBodyPRIV datatype.
     */
    public FrameBodyPRIV()
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, "");
        this.setObjectValue(DataTypes.OBJ_DATA, new byte[0]);
    }

    public FrameBodyPRIV(FrameBodyPRIV body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyPRIV datatype.
     *
     * @param owner DOCUMENT ME!
     * @param data  DOCUMENT ME!
     */
    public FrameBodyPRIV(String owner, byte[] data)
    {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.setObjectValue(DataTypes.OBJ_DATA, data);
    }

    /**
     * Creates a new FrameBodyPRIV datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyPRIV(java.io.RandomAccessFile file, int frameSize)
        throws IOException, InvalidTagException
    {
        super(file, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBriefDescription()
    {
        return this.getOwner();
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     */
    public void setData(byte[] data)
    {
        setObjectValue(DataTypes.OBJ_DATA, data);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getData()
    {
        return (byte[]) getObjectValue(DataTypes.OBJ_DATA);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "PRIV";
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
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
