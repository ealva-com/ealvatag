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
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyEQU2 extends AbstractID3v2FrameBody  implements ID3v24FrameBody
{
    /**
     * Creates a new FrameBodyEQU2 datatype.
     */
    public FrameBodyEQU2()
    {
        //        this.setObject("Interpolation Method", new Byte((byte) 0));
        //        this.setObject(ObjectTypes.OBJ_OWNER, "");
        //        this.addGroup((short) 0, (short) 0);
    }

    public FrameBodyEQU2(FrameBodyEQU2 body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyEQU2 datatype.
     *
     * @param interpolationMethod DOCUMENT ME!
     * @param owner               DOCUMENT ME!
     * @param frequency           DOCUMENT ME!
     * @param volumeAdjustment    DOCUMENT ME!
     */
    public FrameBodyEQU2(byte interpolationMethod, String owner, short frequency, short volumeAdjustment)
    {
        this.setObjectValue(DataTypes.OBJ_INTERPOLATION_METHOD, new Byte(interpolationMethod));
        this.setObjectValue(DataTypes.OBJ_OWNER, owner);
        this.addGroup(frequency, volumeAdjustment);
    }

    /**
     * Creates a new FrameBodyEQU2 datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyEQU2(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

      /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_EQUALISATION2;
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
     * @param frequency        DOCUMENT ME!
     * @param volumeAdjustment DOCUMENT ME!
     */
    public void addGroup(short frequency, short volumeAdjustment)
    {
        GroupRepeated group = (GroupRepeated) this.getObjectValue(DataTypes.OBJ_DATA);
        AbstractDataType freq = new NumberFixedLength(DataTypes.OBJ_FREQUENCY, this, 2);
        AbstractDataType volume = new NumberFixedLength(DataTypes.OBJ_VOLUME_ADJUSTMENT, this, 2);
        group.addObject(freq);
        group.addObject(volume);
        this.setObjectValue(DataTypes.OBJ_DATA, group);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_INTERPOLATION_METHOD, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_OWNER, this));

        GroupRepeated group = new GroupRepeated(DataTypes.OBJ_DATA, this);
        group.addProperty(new NumberFixedLength(DataTypes.OBJ_FREQUENCY, this, 2));
        group.addProperty(new NumberFixedLength(DataTypes.OBJ_VOLUME_ADJUSTMENT, this, 2));
        objectList.add(group);
    }
}
