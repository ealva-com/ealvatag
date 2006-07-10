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
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.StringHashMap;
import org.jaudiotagger.tag.datatype.StringNullTerminated;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.io.IOException;
import java.io.*;
import java.nio.*;

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3Frames;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.valuepair.Languages;

public class FrameBodyCOMM  extends AbstractID3v2FrameBody  implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyCOMM datatype.
     */
    public FrameBodyCOMM()
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(TextEncoding.ISO_8859_1));             
        setObjectValue(DataTypes.OBJ_LANGUAGE, Languages.DEFAULT_ID);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, "");
        setObjectValue(DataTypes.OBJ_TEXT, "");
    }

    public FrameBodyCOMM(FrameBodyCOMM body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyCOMM datatype.
     *
     * @param textEncoding DOCUMENT ME!
     * @param language     DOCUMENT ME!
     * @param description  DOCUMENT ME!
     * @param text         DOCUMENT ME!
     */
    public FrameBodyCOMM(byte textEncoding, String language, String description, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }

     /**
     * Construct a Comment frame body from the buffer
      *
     * @param byteBuffer
     * @param frameSize
     * @throws IOException
     * @throws InvalidTagException
     */
    public FrameBodyCOMM(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBriefDescription()
    {
        return this.getText();
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
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_COMMENT;
    }

    /**
     * DOCUMENT ME!
     *
     * @param language DOCUMENT ME!
     */
    public void setLanguage(String language)
    {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText()
    {
        return (String) getObjectValue(DataTypes.OBJ_TEXT);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }

    /**
     * Because COMM have a text encoding we need to check the text String does
     * not contain characters that cannot be encoded in current encoding before
     * we write data. If there are we change the encoding.
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        if (((AbstractString) getObject(DataTypes.OBJ_TEXT)).canBeEncoded() == false)
        {
            this.setTextEncoding((byte) TextEncoding.UTF_16BE);
        }
        if (((AbstractString) getObject(DataTypes.OBJ_DESCRIPTION)).canBeEncoded() == false)
        {
            this.setTextEncoding((byte) TextEncoding.UTF_16BE);
        }
        super.write(tagBuffer);
    }

}
