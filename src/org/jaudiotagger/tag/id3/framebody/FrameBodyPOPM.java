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
import java.nio.ByteBuffer;


public class FrameBodyPOPM extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyPOPM datatype.
     */
    public FrameBodyPOPM()
    {
        //        this.setObject("Email to User", "");
        //        this.setObject("Rating", new Byte((byte) 0));
        //        this.setObject("Counter", new Long(0));
    }

    public FrameBodyPOPM(FrameBodyPOPM body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyPOPM datatype.
     *
     * @param emailToUser DOCUMENT ME!
     * @param rating      DOCUMENT ME!
     * @param counter     DOCUMENT ME!
     */
    public FrameBodyPOPM(String emailToUser, byte rating, long counter)
    {
        this.setObjectValue(DataTypes.OBJ_EMAIL, emailToUser);
        this.setObjectValue(DataTypes.OBJ_RATING, new Byte(rating));
        this.setObjectValue(DataTypes.OBJ_COUNTER, new Long(counter));
    }

    /**
     * Creates a new FrameBodyPOPM datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyPOPM(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     */
    public void setEmailToUser(String description)
    {
        setObjectValue(DataTypes.OBJ_EMAIL, description);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEmailToUser()
    {
        return (String) getObjectValue(DataTypes.OBJ_EMAIL);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "POPM" + ((char) 0) + getEmailToUser();
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new StringNullTerminated(DataTypes.OBJ_EMAIL, this));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_RATING, this, 1));
        objectList.add(new NumberVariableLength(DataTypes.OBJ_COUNTER, this, 1));
    }
}
