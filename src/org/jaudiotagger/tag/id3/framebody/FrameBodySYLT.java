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

import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.valuepair.Languages;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.nio.ByteBuffer;


public class FrameBodySYLT extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * 
     */
    LinkedList lines = new LinkedList();

    /**
     * 
     */
    String description = "";

    /**
     * 
     */
    String language = "";

    /**
     * 
     */
    byte contentType = (byte) 0;

    /**
     * 
     */
    byte textEncoding = (byte) 0;

    /**
     * 
     */
    byte timeStampFormat = (byte) 0;

    /**
     * Creates a new FrameBodySYLT datatype.
     */
    public FrameBodySYLT()
    {
        //        this.textEncoding = 0;
        //        this.language = "";
        //        this.timeStampFormat = 0;
        //        this.contentType = 0;
        //        this.description = "";
    }

    public FrameBodySYLT(FrameBodySYLT copyObject)
    {
        super(copyObject);
        this.description = new String(copyObject.description);
        this.language = new String(copyObject.language);
        this.contentType = copyObject.contentType;
        this.textEncoding = copyObject.textEncoding;
        this.timeStampFormat = copyObject.timeStampFormat;

        ID3v2LyricLine newLine;

        for (int i = 0; i < copyObject.lines.size(); i++)
        {
            newLine = new ID3v2LyricLine((ID3v2LyricLine) copyObject.lines.get(i));
            this.lines.add(newLine);
        }
    }

    /**
     * Creates a new FrameBodySYLT datatype.
     *
     * @param textEncoding    
     * @param language        
     * @param timeStampFormat 
     * @param contentType     
     * @param description     
     */
    public FrameBodySYLT(byte textEncoding, String language, byte timeStampFormat, byte contentType, String description)
    {
        this.textEncoding = textEncoding;
        this.language = language;
        this.timeStampFormat = timeStampFormat;
        this.contentType = contentType;
        this.description = description;
    }

    /**
     * Creates a new FrameBodySYLT datatype.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    public FrameBodySYLT(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * 
     *
     * @return 
     */
    public byte getContentType()
    {
        return contentType;
    }

    /**
     * 
     *
     * @return 
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * 
     *
     * @return 
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_SYNC_LYRIC ;
    }

    /**
     * 
     *
     * @return 
     */
    public String getLanguage()
    {
        return language;
    }

    /**
     * 
     *
     * @return 
     */
    public String getLyric()
    {
        String lyrics = "";

        for (int i = 0; i < lines.size(); i++)
        {
            lyrics += lines.get(i);
        }

        return lyrics;
    }

    /**
     * 
     *
     * @return 
     */
    public int getSize()
    {
        int size;

        size = 1 + 3 + 1 + 1 + description.length();

        for (int i = 0; i < lines.size(); i++)
        {
            size += ((ID3v2LyricLine) lines.get(i)).getSize();
        }

        return size;
    }

    /**
     * 
     *
     * @return 
     */
    public byte getTextEncoding()
    {
        return textEncoding;
    }

    /**
     * 
     *
     * @return 
     */
    public byte getTimeStampFormat()
    {
        return timeStampFormat;
    }

    /**
     * 
     *
     * @param timeStamp 
     * @param text      
     */
    public void addLyric(int timeStamp, String text)
    {
        ID3v2LyricLine line = new ID3v2LyricLine("Lyric Line", this);
        line.setTimeStamp((long) timeStamp);
        line.setText(text);
        lines.add(line);
    }

    /**
     * 
     *
     * @param line 
     */
    public void addLyric(Lyrics3Line line)
    {
        Iterator iterator = line.getTimeStamp();
        Lyrics3TimeStamp timeStamp;
        String lyric = line.getLyric();
        long time;
        ID3v2LyricLine id3Line;
        id3Line = new ID3v2LyricLine("Lyric Line", this);

        if (iterator.hasNext() == false)
        {
            // no time stamp, give it 0
            time = 0L;
            id3Line.setTimeStamp(time);
            id3Line.setText(lyric);
            lines.add(id3Line);
        }
        else
        {
            while (iterator.hasNext())
            {
                timeStamp = (Lyrics3TimeStamp) iterator.next();
                time = (timeStamp.getMinute() * 60L) + timeStamp.getSecond(); // seconds
                time *= 1000L; // milliseconds
                id3Line.setTimeStamp(time);
                id3Line.setText(lyric);
                lines.add(id3Line);
            }
        }
    }

    /**
     * This method is not yet supported.
     *
     * @throws java.lang.UnsupportedOperationException
     *          This method is not yet
     *          supported
     */
    public boolean equals(Object obj)
    {
        /**
         * @todo Implement this java.lang.Object method
         */
        throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
    }

    /**
     * 
     *
     * @return 
     */
    public Iterator iterator()
    {
        return lines.iterator();
    }


    /**
     * 
     *
     * @return 
     */
    public String toString()
    {
        String str;
        str = getIdentifier() + " " + textEncoding + " " + language + " " + timeStampFormat + " " + contentType + " " + description;

        for (int i = 0; i < lines.size(); i++)
        {
            str += ((ID3v2LyricLine) lines.get(i)).toString();
        }

        return str;
    }


    /**
     * Setup Object List
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE));
        objectList.add(new StringHashMap(DataTypes.OBJ_LANGUAGE, this, Languages.LANGUAGE_FIELD_SIZE));
        objectList.add(new StringFixedLength(DataTypes.OBJ_DESCRIPTION, this, 1));
        objectList.add(new StringFixedLength(DataTypes.OBJ_TEXT, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_TEXT, this));
    }
}
