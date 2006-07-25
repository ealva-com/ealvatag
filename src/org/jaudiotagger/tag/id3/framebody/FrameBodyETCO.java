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

import org.jaudiotagger.tag.datatype.AbstractDataType;
import org.jaudiotagger.tag.datatype.GroupRepeated;
import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.nio.ByteBuffer;


public class FrameBodyETCO extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyETCO datatype.
     */
    public FrameBodyETCO()
    {
        //        this.setObject("Time Stamp Format", new Long(0));
        //        this.addGroup((byte) 0, (int) 0);
    }

    public FrameBodyETCO(FrameBodyETCO body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyETCO datatype.
     *
     * @param timeStampFormat DOCUMENT ME!
     * @param event           DOCUMENT ME!
     * @param timeStamp       DOCUMENT ME!
     */
    public FrameBodyETCO(byte timeStampFormat, byte event, int timeStamp)
    {
        this.setObjectValue(DataTypes.OBJ_TIME_STAMP_FORMAT, new Long((long) timeStampFormat));
        this.addGroup(event, timeStamp);
    }

    /**
     * Creates a new FrameBodyETCO datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    public FrameBodyETCO(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
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
        return ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES  ;
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
     *
     * @param description DOCUMENT ME!
     */
    public void getOwner(String description)
    {
        setObjectValue(DataTypes.OBJ_OWNER, description);
    }

    /**
     * DOCUMENT ME!
     *
     * @param event     DOCUMENT ME!
     * @param timeStamp DOCUMENT ME!
     */
    public void addGroup(byte event, int timeStamp)
    {
        GroupRepeated group = (GroupRepeated) this.getObjectValue(DataTypes.OBJ_DATA);
        AbstractDataType ev = new NumberHashMap(DataTypes.OBJ_TYPE_OF_EVENT, this, 1);
        AbstractDataType ts = new NumberFixedLength(DataTypes.OBJ_TIME_STAMP_FORMAT, this, 4);
        group.addObject(ev);
        group.addObject(ts);
        this.setObjectValue(DataTypes.OBJ_DATA, group);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TIME_STAMP_FORMAT, this, 1));

        GroupRepeated group = new GroupRepeated(DataTypes.OBJ_DATA, this);
        group.addProperty(new NumberHashMap(DataTypes.OBJ_TYPE_OF_EVENT, this, 1));
        group.addProperty(new NumberFixedLength(DataTypes.OBJ_TIME_STAMP_FORMAT, this, 4));
        objectList.add(group);
    }
}
