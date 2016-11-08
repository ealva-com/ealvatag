/*
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
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.PartOfSet;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;

/**
 * Apple defined Movement No/Total frame works the same way as the TRCK frame
 *
 * This is not an official standard frame, but Apple makes its own rules !
 *
 * @author : Paul Taylor
 */
public class FrameBodyMVNM extends AbstractID3v2FrameBody implements ID3v23FrameBody, ID3v24FrameBody
{

    /**
     * Creates a new FrameBodyMVNM datatype.
     */
    public FrameBodyMVNM()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue());
    }

    public FrameBodyMVNM(FrameBodyMVNM body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    public FrameBodyMVNM(byte textEncoding, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }

    public FrameBodyMVNM(byte textEncoding, Integer trackNo, Integer trackTotal)
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding);
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(trackNo,trackTotal));
    }

    public String getUserFriendlyValue()
    {
        return String.valueOf(getTrackNo());
    }

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws org.jaudiotagger.tag.InvalidTagException
     */
    public FrameBodyMVNM(ByteBuffer byteBuffer, int frameSize) throws InvalidTagException
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
        return ID3v24Frames.FRAME_ID_MOVEMENT_NO;
    }

    public Integer getTrackNo()
    {
        PartOfSet.PartOfSetValue value = (PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT);
        return value.getCount();
    }

    public String getTrackNoAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getCountAsText();
    }

    public String getText()
    {
        return getObjectValue(DataTypes.OBJ_TEXT).toString();
    }

    public void setMovementNo(Integer movementNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(movementNo);
    }

    public void setMovementNo(String movementNo)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setCount(movementNo);
    }

    public Integer getMovementTotal()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotal();
    }

    public String getMovementTotalAsText()
    {
        return ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).getTotalAsText();
    }


    public void setMovementTotal(Integer trackTotal)
    {
         ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
    }

    public void setMovementTotal(String trackTotal)
    {
        ((PartOfSet.PartOfSetValue)getObjectValue(DataTypes.OBJ_TEXT)).setTotal(trackTotal);
    }

    public void setText(String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT, new PartOfSet.PartOfSetValue(text));
    }
    
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new PartOfSet(DataTypes.OBJ_TEXT, this));
    }
}