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
 * This class is for a ID3v1.1 Tag
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.*;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.Iterator;
import java.util.regex.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ID3v11Tag
    extends ID3v1Tag
{

    //For writing output
    protected static final String TYPE_TRACK = "track";


    /**
     * Track is held as a single byte in v1.1
     */
    protected byte track = (byte) 0;
    protected static final int TRACK_MAX_VALUE = 255;
    protected static final int TRACK_MIN_VALUE = 1;

    protected static final int FIELD_COMMENT_LENGTH = 28;
    protected static final int FIELD_COMMENT_POS = 97;

    protected static final int FIELD_TRACK_INDICATOR_LENGTH = 1;
    protected static final int FIELD_TRACK_INDICATOR_POS = 125;

    protected static final int FIELD_TRACK_LENGTH = 1;
    protected static final int FIELD_TRACK_POS = 126;

    /**
     * Creates a new ID3v1_1 datatype.
     */
    public ID3v11Tag()
    {
        release = (byte) 1;
        majorVersion = (byte) 1;
        revision = (byte) 0;
    }

    public ID3v11Tag(ID3v11Tag copyObject)
    {
        super(copyObject);
        release = (byte) 1;
        majorVersion = (byte) 1;
        revision = (byte) 0;
        this.track = copyObject.track;
    }

    /**
     * Creates a new ID3v1_1 datatype.
     *
     * @param mp3tag DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public ID3v11Tag(AbstractTag mp3tag)
    {
        release = (byte) 1;
        majorVersion = (byte) 1;
        revision = (byte) 0;
        if (mp3tag != null)
        {
            if (mp3tag instanceof ID3v1Tag)
            {
                if (mp3tag instanceof ID3v11Tag)
                {
                    throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
                }
                if (mp3tag instanceof ID3v1Tag)
                {
                    // id3v1_1 objects are also id3v1 objects
                    ID3v1Tag id3old = (ID3v1Tag) mp3tag;
                    this.title = new String(id3old.title);
                    this.artist = new String(id3old.artist);
                    this.album = new String(id3old.album);
                    this.comment = new String(id3old.comment);
                    this.year = new String(id3old.year);
                    this.genre = id3old.genre;
                }
            }
            else
            {
                // first change the tag to ID3v2_4 tag.
                // id3v2_4 can take any tag.
                ID3v24Tag id3tag;
                id3tag = new ID3v24Tag(mp3tag);
                ID3v24Frame frame;
                String text;
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_TITLE))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_TITLE);
                    text = (String) ((FrameBodyTIT2) frame.getBody()).getText();
                    this.title = ID3Tags.truncate(text, FIELD_TITLE_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_ARTIST))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST);
                    text = (String) ((FrameBodyTPE1) frame.getBody()).getText();
                    this.artist = ID3Tags.truncate(text, FIELD_ARTIST_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_ALBUM))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_ALBUM);
                    text = (String) ((FrameBodyTALB) frame.getBody()).getText();
                    this.album = ID3Tags.truncate(text, FIELD_ALBUM_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_YEAR))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_YEAR);
                    text = (String) ((FrameBodyTDRC) frame.getBody()).getText();
                    this.year = ID3Tags.truncate(text, FIELD_YEAR_LENGTH);
                }

                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_COMMENT))
                {
                    Iterator iterator = id3tag.getFrameOfType(ID3v24Frames.FRAME_ID_COMMENT);
                    text = "";
                    while (iterator.hasNext())
                    {
                        frame = (ID3v24Frame) iterator.next();
                        text += (((FrameBodyCOMM) frame.getBody()).getText() + " ");
                    }
                    this.comment = ID3Tags.truncate(text, FIELD_COMMENT_LENGTH);
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_GENRE))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_GENRE);
                    text = (String) ((FrameBodyTCON) frame.getBody()).getText();
                    try
                    {
                        this.genre = (byte) ID3Tags.findNumber(text);
                    }
                    catch (TagException ex)
                    {
                    }
                }
                if (id3tag.hasFrame(ID3v24Frames.FRAME_ID_TRACK))
                {
                    frame = (ID3v24Frame) id3tag.getFrame(ID3v24Frames.FRAME_ID_TRACK);
                    text = (String) ((FrameBodyTRCK) frame.getBody()).getText();
                    try
                    {
                        this.track = (byte) ID3Tags.findNumber(text);
                    }
                    catch (TagException ex)
                    {
                    }
                }
            }
        }
    }

    /**
     * Creates a new ID3v1_1 datatype.
     *
     * @param file DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     */
    public ID3v11Tag(RandomAccessFile file)
        throws TagNotFoundException, IOException
    {
        release = (byte) 1;
        majorVersion = (byte) 1;
        revision = (byte) 0;

        FileChannel fc = null;
        ByteBuffer  byteBuffer = ByteBuffer.allocate(TAG_LENGTH);

        try
        {
            fc = file.getChannel();
            fc.position(file.length() - TAG_LENGTH);
            byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
            fc.read(byteBuffer);
            byteBuffer.flip();
            read(byteBuffer);
        }
        finally
        {
            fc.close();
        }
    }

    /**
     * Set Comment
     *
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = ID3Tags.truncate(comment, this.FIELD_COMMENT_LENGTH);
    }

    /**
     * Get Comment
     *
     * @return comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Set the track, v1_1 stores track numbers in a single byte value so can only
     * handle a simple number in the range 0-255.
     *
     * @param trackValue
     */
    public void setTrack(String trackValue)
    {
        int trackAsInt;
        //Try and convert String representaion of track into an integer
        try
        {
            trackAsInt = Integer.parseInt(trackValue);
        }
        catch (Exception e)
        {
            trackAsInt = 0;
        }
        //@todo also deal with 3/10 format and take first value.

        //This value cannot be held in v1_1
        if ((trackAsInt > TRACK_MAX_VALUE) || (trackAsInt < TRACK_MIN_VALUE))
        {
            this.track = (byte) 0x00;
        }
        else
        {
            this.track = (byte) Integer.parseInt(trackValue);
        }
    }

    /**
     * Return the track number as a String.
     *
     * @return track
     */
    public String getTrack()
    {
        return String.valueOf(track & BYTE_TO_UNSIGNED);
    }

    /**
     * Compares Object with this only returns true if both v1_1 tags with all
     * fields set to same value
     *
     * @param obj Comparing Object
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof ID3v11Tag) == false)
        {
            return false;
        }
        ID3v11Tag object = (ID3v11Tag) obj;
        if (this.track != object.track)
        {
            return false;
        }
        return super.equals(obj);
    }


    /**
     * Find identifer within byteBuffer to indicate that a v11 tag exists within the buffer
     * @param byteBuffer
     * @return true if find header for v11 tag within buffer
     */
    public boolean seek(ByteBuffer byteBuffer)
    {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        // read the TAG value
        byteBuffer.get(buffer, 0, FIELD_TAGID_LENGTH);
        if (!(Arrays.equals(buffer, TAG_ID)))
        {
            return false;
        }

        // Check for the empty byte before the TRACK
        byteBuffer.position(this.FIELD_TRACK_INDICATOR_POS);
        if (byteBuffer.get() != END_OF_FIELD)
        {
            return false;
        }
        //Now check for TRACK if the next byte is also null byte then not v1.1
        //tag,however this means cannot have v1_1 tag with track set to zero/undefined
        //because on next read will be v1 tag.
        if (byteBuffer.get() == END_OF_FIELD)
        {
            return false;
        }
        return true;
    }

    /**
     * Read in a tag from the ByteBuffer
     *
     * @param byteBuffer from where to read in a tag
     * @throws TagNotFoundException if unable to read a tag in the byteBuffer
     */
    public void read(ByteBuffer byteBuffer)
        throws TagNotFoundException
    {
        if (seek(byteBuffer) == false)
        {
            throw new TagNotFoundException("ID3v1 tag not found");
        }
        logger.finer("Reading v1.1 tag");

        //Do single file read of data to cut down on file reads
        byte[] dataBuffer = new byte[TAG_LENGTH];
        byteBuffer.position(0);
        byteBuffer.get(dataBuffer, 0, TAG_LENGTH);
        title = new String(dataBuffer, FIELD_TITLE_POS, this.FIELD_TITLE_LENGTH).trim();
        Matcher m = endofStringPattern.matcher(title);
        if (m.find() == true)
        {
            title = title.substring(0, m.start());
        }
        artist = new String(dataBuffer, FIELD_ARTIST_POS, this.FIELD_ARTIST_LENGTH).trim();
        m = endofStringPattern.matcher(artist);
        if (m.find() == true)
        {
            artist = artist.substring(0, m.start());
        }
        album = new String(dataBuffer, FIELD_ALBUM_POS, this.FIELD_ALBUM_LENGTH).trim();
        m = endofStringPattern.matcher(album);
        if (m.find() == true)
        {
            album = album.substring(0, m.start());
        }
        year = new String(dataBuffer, FIELD_YEAR_POS, this.FIELD_YEAR_LENGTH).trim();
        m = endofStringPattern.matcher(year);
        if (m.find() == true)
        {
            year = year.substring(0, m.start());
        }
        comment = new String(dataBuffer, FIELD_COMMENT_POS, this.FIELD_COMMENT_LENGTH).trim();
        m = endofStringPattern.matcher(comment);
        if (m.find() == true)
        {
            comment = comment.substring(0, m.start());
        }
        track = dataBuffer[FIELD_TRACK_POS];
        genre = dataBuffer[FIELD_GENRE_POS];
    }


    /**
     * Write this representation of tag to the file indicated
     *
     * @param file that this tag should be written to
     * @throws IOException thrown if there were problems writing to the file
     */
    public void write(RandomAccessFile file)
        throws IOException
    {
        logger.info("Saving file");
        byte[] buffer = new byte[TAG_LENGTH];
        int i;
        String str;
        delete(file);
        file.seek(file.length());
        System.arraycopy(TAG_ID, this.FIELD_TAGID_POS, buffer, this.FIELD_TAGID_POS, TAG_ID.length);
        int offset = this.FIELD_TITLE_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveTitle())
        {
            str = ID3Tags.truncate(title, this.FIELD_TITLE_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = this.FIELD_ARTIST_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveArtist())
        {
            str = ID3Tags.truncate(artist, this.FIELD_ARTIST_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = this.FIELD_ALBUM_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveAlbum())
        {
            str = ID3Tags.truncate(album, this.FIELD_ALBUM_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = this.FIELD_YEAR_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveYear())
        {
            str = ID3Tags.truncate(year, this.FIELD_YEAR_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = this.FIELD_COMMENT_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveComment())
        {
            str = ID3Tags.truncate(comment, this.FIELD_COMMENT_LENGTH);
            for (i = 0; i < str.length(); i++)
            {
                buffer[i + offset] = (byte) str.charAt(i);
            }
        }
        offset = this.FIELD_TRACK_POS;
        buffer[offset] = track; // skip one byte extra blank for 1.1 definition
        offset = this.FIELD_GENRE_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveGenre())
        {
            buffer[offset] = genre;
        }
        file.write(buffer);
    }


    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_TAG, getIdentifier());
        //Header
        MP3File.getStructureFormatter().addElement(TYPE_TITLE, this.title);
        MP3File.getStructureFormatter().addElement(TYPE_ARTIST, this.artist);
        MP3File.getStructureFormatter().addElement(TYPE_ALBUM, this.album);
        MP3File.getStructureFormatter().addElement(TYPE_YEAR, this.year);
        MP3File.getStructureFormatter().addElement(TYPE_COMMENT, this.comment);
        MP3File.getStructureFormatter().addElement(TYPE_TRACK, (int) this.track);
        MP3File.getStructureFormatter().addElement(TYPE_GENRE, (int) this.genre);
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_TAG);

    }
}
