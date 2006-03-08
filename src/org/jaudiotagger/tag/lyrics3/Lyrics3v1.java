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
 */

package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.tag.id3.ID3Tags;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3Tags;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.*;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.Iterator;
import java.util.Arrays;
import java.nio.ByteBuffer;

public class Lyrics3v1 extends AbstractLyrics3
{
    /**
     * DOCUMENT ME!
     */
    private String lyric = "";

    /**
     * Creates a new Lyrics3v1 datatype.
     */
    public Lyrics3v1()
    {
    }

    public Lyrics3v1(Lyrics3v1 copyObject)
    {
        super(copyObject);
        this.lyric = new String(copyObject.lyric);
    }

    public Lyrics3v1(AbstractTag mp3Tag)
    {
        if (mp3Tag != null)
        {
            Lyrics3v2 lyricTag;

            if (mp3Tag instanceof Lyrics3v1)
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            else if (mp3Tag instanceof Lyrics3v2)
            {
                lyricTag = (Lyrics3v2) mp3Tag;
            }
            else
            {
                lyricTag = new Lyrics3v2(mp3Tag);
            }

            FieldFrameBodyLYR lyricField;
            lyricField = (FieldFrameBodyLYR) lyricTag.getField("LYR").getBody();
            this.lyric = new String(lyricField.getLyric());
        }
    }

    /**
     * Creates a new Lyrics3v1 datatype.
     *
     * @param file DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws java.io.IOException  DOCUMENT ME!
     */
    public Lyrics3v1(ByteBuffer byteBuffer)
        throws TagNotFoundException, java.io.IOException
    {
        this.read(byteBuffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "Lyrics3v1.00";
    }

    /**
     * DOCUMENT ME!
     *
     * @param lyric DOCUMENT ME!
     */
    public void setLyric(String lyric)
    {
        this.lyric = ID3Tags.truncate(lyric, 5100);
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
        return "LYRICSBEGIN".length() + lyric.length() + "LYRICSEND".length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isSubsetOf(Object obj)
    {
        if ((obj instanceof Lyrics3v1) == false)
        {
            return false;
        }

        return (((Lyrics3v1) obj).lyric.indexOf(this.lyric) >= 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof Lyrics3v1) == false)
        {
            return false;
        }

        Lyrics3v1 object = (Lyrics3v1) obj;

        if (this.lyric.equals(object.lyric) == false)
        {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws java.lang.UnsupportedOperationException
     *          DOCUMENT ME!
     */
    public Iterator iterator()
    {
        /**
         * @todo Implement this org.jaudiotagger.tag.AbstractMP3Tag abstract method
         */
        throw new java.lang.UnsupportedOperationException("Method iterator() not yet implemented.");
    }

    /**
     * TODO implement
     *
     * @param byteBuffer
     * @return
     * @throws IOException
     */
    public boolean seek(ByteBuffer byteBuffer)
        throws IOException
    {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param byteBuffer DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     */
    public void read(ByteBuffer byteBuffer)
        throws TagNotFoundException, IOException
    {
        byte[] buffer = new byte[5100 + 9 + 11];
        String lyricBuffer;

        if (seek(byteBuffer) == false)
        {
            throw new TagNotFoundException("ID3v1 tag not found");
        }

        byteBuffer.get(buffer);
        lyricBuffer = new String(buffer);

        lyric = lyricBuffer.substring(0, lyricBuffer.indexOf("LYRICSEND"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public boolean seek(RandomAccessFile file)
        throws IOException
    {
        byte[] buffer = new byte[5100 + 9 + 11];
        String lyricsEnd = "";
        String lyricsStart = "";
        long offset = 0;

        // check right before the ID3 1.0 tag for the lyrics tag
        file.seek(file.length() - 128 - 9);
        file.read(buffer, 0, 9);
        lyricsEnd = new String(buffer, 0, 9);

        if (lyricsEnd.equals("LYRICSEND"))
        {
            offset = file.getFilePointer();
        }
        else
        {
            // check the end of the file for a lyrics tag incase an ID3
            // tag wasn't placed after it.
            file.seek(file.length() - 9);
            file.read(buffer, 0, 9);
            lyricsEnd = new String(buffer, 0, 9);

            if (lyricsEnd.equals("LYRICSEND"))
            {
                offset = file.getFilePointer();
            }
            else
            {
                return false;
            }
        }

        // the tag can at most only be 5100 bytes
        offset -= (5100 + 9 + 11);
        file.seek(offset);
        file.read(buffer);
        lyricsStart = new String(buffer);

        // search for the tag
        int i = lyricsStart.indexOf("LYRICSBEGIN");

        if (i == -1)
        {
            return false;
        }

        file.seek(offset + i + 11);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        String str = getIdentifier() + " " + this.getSize() + "\n";

        return str + lyric;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void write(RandomAccessFile file)
        throws IOException
    {
        String str = "";
        int offset = 0;
        byte[] buffer;
        ID3v1Tag id3v1tag = null;

        id3v1tag = null;

        delete(file);
        file.seek(file.length());

        buffer = new byte[lyric.length() + 11 + 9];

        str = "LYRICSBEGIN";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i] = (byte) str.charAt(i);
        }

        offset = str.length();

        str = ID3Tags.truncate(lyric, 5100);

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        str = "LYRICSEND";

        for (int i = 0; i < str.length(); i++)
        {
            buffer[i + offset] = (byte) str.charAt(i);
        }

        offset += str.length();

        file.write(buffer, 0, offset);

        if (id3v1tag != null)
        {
            id3v1tag.write(file);
        }
    }

}