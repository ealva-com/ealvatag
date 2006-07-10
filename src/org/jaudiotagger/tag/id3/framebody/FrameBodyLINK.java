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
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyLINK extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyLINK datatype.
     */
    public FrameBodyLINK()
    {
        //        this.setObject("Frame Identifier", "");
        //        this.setObject("URL", "");
        //        this.setObject("ID and Additional Data", "");
    }

    public FrameBodyLINK(FrameBodyLINK body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyLINK datatype.
     *
     * @param frameIdentifier DOCUMENT ME!
     * @param url             DOCUMENT ME!
     * @param additionalData  DOCUMENT ME!
     */
    public FrameBodyLINK(String frameIdentifier, String url, String additionalData)
    {
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier);
        this.setObjectValue(DataTypes.OBJ_URL, url);
        this.setObjectValue(DataTypes.OBJ_ID, additionalData);
    }

    /**
     * Creates a new FrameBodyLINK datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyLINK(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAdditionalData()
    {
        return (String) getObjectValue(DataTypes.OBJ_ID);
    }

    /**
     * DOCUMENT ME!
     *
     * @param additionalData DOCUMENT ME!
     */
    public void getAdditionalData(String additionalData)
    {
        setObjectValue(DataTypes.OBJ_ID, additionalData);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFrameIdentifier()
    {
        return (String) getObjectValue(DataTypes.OBJ_DESCRIPTION);
    }

    /**
     * DOCUMENT ME!
     *
     * @param frameIdentifier DOCUMENT ME!
     */
    public void getFrameIdentifier(String frameIdentifier)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, frameIdentifier);
    }

    /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_LINKED_INFO;
    }
    

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new StringFixedLength(DataTypes.OBJ_DESCRIPTION, this, 4));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_URL, this));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_ID, this));
    }
}
