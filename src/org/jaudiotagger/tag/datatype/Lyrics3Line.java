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
package org.jaudiotagger.tag.datatype;

import java.util.Iterator;
import java.util.LinkedList;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.AbstractTagFrameBody;

public class Lyrics3Line
    extends AbstractDataType
{
    /**
     * DOCUMENT ME!
     */
    private LinkedList timeStamp = new LinkedList();

    /**
     * DOCUMENT ME!
     */
    private String lyric = "";

    /**
     * Creates a new ObjectLyrics3Line datatype.
     *
     * @param identifier DOCUMENT ME!
     */
    public Lyrics3Line(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public Lyrics3Line(Lyrics3Line copy)
    {
        super(copy);
        this.lyric = new String(copy.lyric);
        Lyrics3TimeStamp newTimeStamp;
        for (int i = 0; i < copy.timeStamp.size(); i++)
        {
            newTimeStamp = new Lyrics3TimeStamp((Lyrics3TimeStamp) copy.timeStamp.get(i));
            this.timeStamp.add(newTimeStamp);
        }
    }

    public void setLyric(String lyric)
    {
        this.lyric = lyric;
    }

    public void setLyric(ID3v2LyricLine line)
    {
        this.lyric = line.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLyric()
    {
        return lyric;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = 0;
        for (int i = 0; i < timeStamp.size(); i++)
        {
            size += ((Lyrics3TimeStamp) timeStamp.get(i)).getSize();
        }
        return size + lyric.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTimeStamp(Lyrics3TimeStamp time)
    {
        timeStamp.clear();
        timeStamp.add(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getTimeStamp()
    {
        return timeStamp.iterator();
    }

    public void addLyric(String newLyric)
    {
        this.lyric += newLyric;
    }

    public void addLyric(ID3v2LyricLine line)
    {
        this.lyric += line.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void addTimeStamp(Lyrics3TimeStamp time)
    {
        timeStamp.add(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof Lyrics3Line) == false)
        {
            return false;
        }
        Lyrics3Line object = (Lyrics3Line) obj;
        if (this.lyric.equals(object.lyric) == false)
        {
            return false;
        }
        if (this.timeStamp.equals(object.timeStamp) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasTimeStamp()
    {
        if (timeStamp.isEmpty())
        {
            return false;
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lineString DOCUMENT ME!
     * @param offset     DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readString(String lineString, int offset)
    {
        if (lineString == null)
        {
            throw new NullPointerException("Image is null");
        }
        if ((offset < 0) || (offset >= lineString.length()))
        {
            throw new IndexOutOfBoundsException("Offset to line is out of bounds: offset = " + offset +
                ", line.length()" + lineString.length());
        }
        int delim = 0;
        Lyrics3TimeStamp time;
        timeStamp = new LinkedList();
        delim = lineString.indexOf("[", offset);
        while (delim >= 0)
        {
            offset = lineString.indexOf("]", delim) + 1;
            time = new Lyrics3TimeStamp("Time Stamp");
            time.readString(lineString.substring(delim, offset));
            timeStamp.add(time);
            delim = lineString.indexOf("[", offset);
        }
        lyric = lineString.substring(offset);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        String str = "";
        for (int i = 0; i < timeStamp.size(); i++)
        {
            str += timeStamp.get(i).toString();
        }
        return "timeStamp = " + str + ", lyric = " + lyric + "\n";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String writeString()
    {
        String str = "";
        Lyrics3TimeStamp time;
        for (int i = 0; i < timeStamp.size(); i++)
        {
            time = (Lyrics3TimeStamp) timeStamp.get(i);
            str += time.writeString();
        }
        return str + lyric;
    }

    public void readByteArray(byte[] arr, int offset)
    {
        readString(arr.toString(), offset);
    }

    public byte[] writeByteArray()
    {
        return writeString().getBytes();
    }


}
