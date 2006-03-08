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

import org.jaudiotagger.tag.datatype.NumberFixedLength;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.InvalidTagException;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyRVRB extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyRVRB datatype.
     */
    public FrameBodyRVRB()
    {
        //        this.setObject("Reverb Left", new Short((short) 0));
        //        this.setObject("Reverb Right", new Short((short) 0));
        //        this.setObject("Reverb Bounces Left", new Byte((byte) 0));
        //        this.setObject("Reverb Bounces Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Left To Left", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Left To Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Right To Right", new Byte((byte) 0));
        //        this.setObject("Reverb Feedback Right to Left", new Byte((byte) 0));
        //        this.setObject("Premix Left To Right", new Byte((byte) 0));
        //        this.setObject("Premix Right To Left", new Byte((byte) 0));
    }

    public FrameBodyRVRB(FrameBodyRVRB body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyRVRB datatype.
     *
     * @param reverbLeft                 DOCUMENT ME!
     * @param reverbRight                DOCUMENT ME!
     * @param reverbBouncesLeft          DOCUMENT ME!
     * @param reverbBouncesRight         DOCUMENT ME!
     * @param reverbFeedbackLeftToLeft   DOCUMENT ME!
     * @param reverbFeedbackLeftToRight  DOCUMENT ME!
     * @param reverbFeedbackRightToRight DOCUMENT ME!
     * @param reverbFeedbackRightToLeft  DOCUMENT ME!
     * @param premixLeftToRight          DOCUMENT ME!
     * @param premixRightToLeft          DOCUMENT ME!
     */
    public FrameBodyRVRB(short reverbLeft, short reverbRight, byte reverbBouncesLeft, byte reverbBouncesRight, byte reverbFeedbackLeftToLeft, byte reverbFeedbackLeftToRight, byte reverbFeedbackRightToRight, byte reverbFeedbackRightToLeft, byte premixLeftToRight, byte premixRightToLeft)
    {
        this.setObjectValue(DataTypes.OBJ_REVERB_LEFT, new Short(reverbLeft));
        this.setObjectValue(DataTypes.OBJ_REVERB_RIGHT, new Short(reverbRight));
        this.setObjectValue(DataTypes.OBJ_REVERB_BOUNCE_LEFT, new Byte(reverbBouncesLeft));
        this.setObjectValue(DataTypes.OBJ_REVERB_BOUNCE_RIGHT, new Byte(reverbBouncesRight));
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_LEFT, new Byte(reverbFeedbackLeftToLeft));
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_RIGHT, new Byte(reverbFeedbackLeftToRight));
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_RIGHT, new Byte(reverbFeedbackRightToRight));
        this.setObjectValue(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_LEFT, new Byte(reverbFeedbackRightToLeft));
        this.setObjectValue(DataTypes.OBJ_PREMIX_LEFT_TO_RIGHT, new Byte(premixLeftToRight));
        this.setObjectValue(DataTypes.OBJ_PREMIX_RIGHT_TO_LEFT, new Byte(premixRightToLeft));
    }

    /**
     * Creates a new FrameBodyRVRB datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyRVRB(ByteBuffer byteBuffer, int frameSize)
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
        return "RVRB";
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_LEFT, this, 2));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_RIGHT, this, 2));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_BOUNCE_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_BOUNCE_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_LEFT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_REVERB_FEEDBACK_RIGHT_TO_LEFT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREMIX_LEFT_TO_RIGHT, this, 1));
        objectList.add(new NumberFixedLength(DataTypes.OBJ_PREMIX_RIGHT_TO_LEFT, this, 1));
    }
}
