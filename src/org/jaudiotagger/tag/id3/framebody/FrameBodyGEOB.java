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

import java.io.IOException;


public class FrameBodyGEOB extends AbstractID3v2FrameBody implements ID3v24FrameBody
{

    /**
     * Creates a new FrameBodyGEOB datatype.
     */
    public FrameBodyGEOB()
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte((byte) 0));
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, "");
        this.setObjectValue(DataTypes.OBJ_FILENAME, "");
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        this.setObjectValue(DataTypes.OBJ_DATA, new byte[0]);
    }

    public FrameBodyGEOB(FrameBodyGEOB body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyGEOB datatype.
     *
     * @param textEncoding DOCUMENT ME!
     * @param mimeType     DOCUMENT ME!
     * @param filename     DOCUMENT ME!
     * @param description  DOCUMENT ME!
     * @param object       DOCUMENT ME!
     */
    public FrameBodyGEOB(byte textEncoding, String mimeType, String filename, String description, byte[] object)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);
        this.setObjectValue(DataTypes.OBJ_FILENAME, filename);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_DATA, object);
    }

    /**
     * Creates a new FrameBodyGEOB datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyGEOB(java.io.RandomAccessFile file, int frameSize)
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
        return "GEOB";
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_FILENAME, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }
}
