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
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.valuepair.Languages;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyUSLT extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    /**
     * Creates a new FrameBodyUSLT datatype.
     */
    public FrameBodyUSLT()
    {
        //        setObject("Text Encoding", new Byte((byte) 0));
        //        setObject("Language", "");
        //        setObject(ObjectTypes.OBJ_DESCRIPTION, "");
        //        setObject("Lyrics/Text", "");
    }

    public FrameBodyUSLT(FrameBodyUSLT body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyUSLT datatype.
     *
     * @param textEncoding 
     * @param language     
     * @param description  
     * @param text         
     */
    public FrameBodyUSLT(byte textEncoding, String language, String description, String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        setObjectValue(DataTypes.OBJ_LYRICS, text);
    }

    /**
     * Creates a new FrameBodyUSLT datatype.
     *
     * @param file 
     * @throws IOException         
     * @throws InvalidTagException 
     */
    public FrameBodyUSLT(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * 
     *
     * @param description 
     */
    public void setDescription(String description)
    {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
    }

    /**
     * 
     *
     * @return 
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
        return ID3v24Frames.FRAME_ID_UNSYNC_LYRICS;
    }

    /**
     * 
     *
     * @param language 
     */
    public void setLanguage(String language)
    {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language);
    }

    /**
     * 
     *
     * @return 
     */
    public String getLanguage()
    {
        return (String) getObjectValue(DataTypes.OBJ_LANGUAGE);
    }

    /**
     * 
     *
     * @param lyric 
     */
    public void setLyric(String lyric)
    {
        setObjectValue(DataTypes.OBJ_LYRICS, lyric);
    }

    /**
     * 
     *
     * @return 
     */
    public String getLyric()
    {
        return (String) getObjectValue(DataTypes.OBJ_LYRICS);
    }

    /**
     * 
     *
     * @param text 
     */
    public void addLyric(String text)
    {
        setLyric(getLyric() + text);
        ;
    }

    /**
     * 
     *
     * @param line 
     */
    public void addLyric(Lyrics3Line line)
    {
        setLyric(getLyric() + line.writeString());
    }


    /**
     * 
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_LYRICS, this));
    }
}
