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
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

/**
 * Synchronised lyrics/text frame.
 * 
 * <p>
 * This is another way of incorporating the words, said or sung lyrics,
 * in the audio file as text, this time, however, in sync with the
 * audio. It might also be used to describing events e.g. occurring on a
 * stage or on the screen in sync with the audio. The header includes a
 * content descriptor, represented with as terminated textstring. If no
 * descriptor is entered, 'Content descriptor' is $00 (00) only.
 * </p><p><center><table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Synchronised lyrics/text', ID: "SYLT"&gt;</td></tr>
 * <tr><td>Text encoding</td><td width="80%">$xx</td></tr>
 * <tr><td>Language</td><td>$xx xx xx</td></tr>
 * <tr><td>Time stamp format</td><td>$xx</td></tr>
 * <tr><td>Content type</td><td>$xx</td></tr>
 * <tr><td>Content descriptor</td><td>&lt;text string according to encoding&gt; $00 (00)</td></tr>
 * </table></center></p>
 * <p><center><table border=0 width="70%">
 * <tr><td rowspan=2 valign=top>Encoding:</td><td>$00</td><td>ISO-8859-1 character set is used => $00 is sync identifier.</td></tr>
 * <tr><td>$01</td><td>Unicode character set is used => $00 00 is sync identifier.</td></tr>
 * </table></center></p>
 * <p><center><table border=0 width="70%">
 * <tr><td rowspan=7 valign=top>Content type:</td><td>$00</td><td width="80%">is other</td></tr>
 * <tr><td>$01</td><td>is lyrics</td></tr>
 * <tr><td>$02</td><td>is text transcription</td></tr>
 * <tr><td>$03</td><td>is movement/part name (e.g. "Adagio")</td></tr>
 * <tr><td>$04</td><td>is events (e.g. "Don Quijote enters the stage")</td></tr>
 * <tr><td>$05</td><td>is chord (e.g. "Bb F Fsus")</td></tr>
 * <tr><td>$06</td><td>is trivia/'pop up' information</td></tr>
 * </table></center></p>
 * <p>
 * Time stamp format is:
 * </p><p>
 *  $01 Absolute time, 32 bit sized, using MPEG frames as unit<br>
 *  $02 Absolute time, 32 bit sized, using milliseconds as unit
 * </p><p>
 * Abolute time means that every stamp contains the time from the
 * beginning of the file.
 * </p><p>
 * The text that follows the frame header differs from that of the
 * unsynchronised lyrics/text transcription in one major way. Each
 * syllable (or whatever size of text is considered to be convenient by
 * the encoder) is a null terminated string followed by a time stamp
 * denoting where in the sound file it belongs. Each sync thus has the
 * following structure:
 * </p><p><table border=0 width="70%">
 * <tr><td colspan=2>Terminated text to be synced (typically a syllable)</td></tr>
 * <tr><td nowrap>Sync identifier (terminator to above string)</td><td width="80%">$00 (00)</td></tr>
 * <tr><td>Time stamp</td><td>$xx (xx ...)</td></tr>
 * </table></p>
 * <p>
 * The 'time stamp' is set to zero or the whole sync is omitted if
 * located directly at the beginning of the sound. All time stamps
 * should be sorted in chronological order. The sync can be considered
 * as a validator of the subsequent string.
 * </p><p>
 * Newline ($0A) characters are allowed in all "SYLT" frames and should
 * be used after every entry (name, event etc.) in a frame with the
 * content type $03 - $04.
 * </p><p>
 * A few considerations regarding whitespace characters: Whitespace
 * separating words should mark the beginning of a new word, thus
 * occurring in front of the first syllable of a new word. This is also
 * valid for new line characters. A syllable followed by a comma should
 * not be broken apart with a sync (both the syllable and the comma
 * should be before the sync).
 * </p><p>
 * An example: The "USLT" passage
 * </p><p>
 * "Strangers in the night" $0A "Exchanging glances"
 * </p><p>would be "SYLT" encoded as:
 * </p><p>
 *  "Strang" $00 xx xx "ers" $00 xx xx " in" $00 xx xx " the" $00 xx xx
 *  " night" $00 xx xx 0A "Ex" $00 xx xx "chang" $00 xx xx "ing" $00 xx
 *  xx "glan" $00 xx xx "ces" $00 xx xx
 * </p><p>There may be more than one "SYLT" frame in each tag, but only one
 * with the same language and content descriptor.</p>
 * 
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li>http://www.id3.org/id3v2.3.0.txt
 * </ul>
 * 
 * Amended @author : Paul Taylor
 * Initial @author : Eric Farng
 * Version @version:$Id$
 */
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
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(copyObject.getTextEncoding()));
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
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
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
        str = getIdentifier() + " " + getTextEncoding() + " " + language + " " + timeStampFormat + " " + contentType + " " + description;

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
