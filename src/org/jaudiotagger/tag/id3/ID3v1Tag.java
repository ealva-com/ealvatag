/**
 *  @author : Paul Taylor
 *  @author : Eric Farng
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
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.valuepair.GenreTypes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * Represents an ID3v1 tag.
 *  
 * @author : Eric Farng
 * @author : Paul Taylor
 *
 */
public class ID3v1Tag
    extends AbstractID3v1Tag
{
    //For writing output
    protected static final String TYPE_COMMENT = "comment";


    protected static final int FIELD_COMMENT_LENGTH = 30;
    protected static final int FIELD_COMMENT_POS = 97;
    protected static final int BYTE_TO_UNSIGNED = 0xff;

    protected static final int GENRE_UNDEFINED = 0xff;

    /**
     * 
     */
    protected String album = "";

    /**
     * 
     */
    protected String artist = "";

    /**
     * 
     */
    protected String comment = "";

    /**
     * 
     */
    protected String title = "";

    /**
     * 
     */
    protected String year = "";

    /**
     * 
     */
    protected byte genre = (byte) -1;


    private static final byte RELEASE  = 1;
    private static final byte MAJOR_VERSION = 0;
    private static final byte REVISION = 0;

    /**
     * Retrieve the Release
     */
    public byte getRelease()
    {
        return RELEASE;
    }

    /**
     * Retrieve the Major Version
     */
    public byte getMajorVersion()
    {
        return MAJOR_VERSION;
    }

    /**
     * Retrieve the Revision
     */
    public byte getRevision()
    {
        return REVISION;
    }

    /**
     * Creates a new ID3v1 datatype.
     */
    public ID3v1Tag()
    {

    }

    public ID3v1Tag(ID3v1Tag copyObject)
    {
        super(copyObject);

        this.album = new String(copyObject.album);
        this.artist = new String(copyObject.artist);
        this.comment = new String(copyObject.comment);
        this.title = new String(copyObject.title);
        this.year = new String(copyObject.year);
        this.genre = copyObject.genre;
    }

    public ID3v1Tag(AbstractTag mp3tag)
    {

        if (mp3tag != null)
        {
            ID3v11Tag convertedTag;
            if (mp3tag instanceof ID3v1Tag)
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            if (mp3tag instanceof ID3v11Tag)
            {
                convertedTag = (ID3v11Tag) mp3tag;
            }
            else
            {
                convertedTag = new ID3v11Tag(mp3tag);
            }
            this.album = new String(convertedTag.album);
            this.artist = new String(convertedTag.artist);
            this.comment = new String(convertedTag.comment);
            this.title = new String(convertedTag.title);
            this.year = new String(convertedTag.year);
            this.genre = convertedTag.genre;
        }
    }

     /**
     * Creates a new ID3v1 datatype.
     *
     * @param file
     * @param loggingFilename
     * @throws TagNotFoundException
     * @throws IOException
     */
    public ID3v1Tag(RandomAccessFile file,String loggingFilename)
        throws TagNotFoundException, IOException
    {
        setLoggingFilename(loggingFilename);
        FileChannel fc;
        ByteBuffer  byteBuffer;

        fc = file.getChannel();
        fc.position(file.length() - TAG_LENGTH);
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH);
        fc.read(byteBuffer);
        byteBuffer.flip();
        read(byteBuffer);
    }

    /**
     * Creates a new ID3v1 datatype.
     *
     * @param file
     * @throws TagNotFoundException
     * @throws IOException
     *
     * @deprecated use {@link #ID3v1Tag(RandomAccessFile,String)} instead
     */
    public ID3v1Tag(RandomAccessFile file)
        throws TagNotFoundException, IOException
    {
        this(file,"");
    }

    /**
     * Set Album
     *
     * @param album
     */
    public void setAlbum(String album)
    {
        this.album = ID3Tags.truncate(album, this.FIELD_ALBUM_LENGTH);
    }

    /**
     * Get Album
     *
     * @return album
     */
    public String getAlbum()
    {
        return album;
    }

    /**
     * Set Artist
     *
     * @param artist
     */
    public void setArtist(String artist)
    {
        this.artist = ID3Tags.truncate(artist, this.FIELD_ARTIST_LENGTH);
    }

    /**
     * Get Artist
     *
     * @return artist
     */
    public String getArtist()
    {
        return artist;
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
     * Sets the genreID, id3v1 only supports genres defined in a predefined list
     * so if unable to find value in list set 255, which seems to be the value
     * winamp uses for undefined.
     *
     * @param genreVal
     */
    public void setGenre(String genreVal)
    {
        Integer genreID = GenreTypes.getInstanceOf().getIdForValue(genreVal);
        if (genreID != null)
        {
            this.genre = genreID.byteValue();
        }
        else
        {
            this.genre = (byte) GENRE_UNDEFINED;
        }
    }

    /**
     * Get Genre
     *
     * @return genre
     */
    public String getGenre()
    {
        Integer genreId = genre & this.BYTE_TO_UNSIGNED;
        return GenreTypes.getInstanceOf().getValueForId(genreId);
    }


    /**
     * Set Title
     *
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = ID3Tags.truncate(title, this.FIELD_TITLE_LENGTH);
    }

    /**
     * Get title
     *
     * @return Title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Set year
     *
     * @param year
     */
    public void setYear(String year)
    {
        this.year = ID3Tags.truncate(year, this.FIELD_YEAR_LENGTH);
    }

    /**
     * Get year
     *
     * @return year
     */
    public String getYear()
    {
        return year;
    }


    /**
     * 
     *
     * @param obj 
     * @return true if this and obj are equivalent
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof ID3v1Tag) == false)
        {
            return false;
        }
        ID3v1Tag object = (ID3v1Tag) obj;
        if (this.album.equals(object.album) == false)
        {
            return false;
        }
        if (this.artist.equals(object.artist) == false)
        {
            return false;
        }
        if (this.comment.equals(object.comment) == false)
        {
            return false;
        }
        if (this.genre != object.genre)
        {
            return false;
        }
        if (this.title.equals(object.title) == false)
        {
            return false;
        }
        if (this.year.equals(object.year) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * 
     *
     * @return  an iterator to iterate through the fields of the tag
     */
    public Iterator iterator()
    {
        return new ID3v1Iterator(this);
    }




    /**
     *
     * @param byteBuffer 
     * @throws TagNotFoundException
     */
    public void read(ByteBuffer byteBuffer)
        throws TagNotFoundException
    {
        if (seek(byteBuffer) == false)
        {
            throw new TagNotFoundException(getLoggingFilename()+":"+"ID3v1 tag not found");
        }
        logger.finer(getLoggingFilename()+":"+"Reading v1 tag");
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
        logger.finest(getLoggingFilename()+":"+"Orig Album is:" + comment + ":");
        if (m.find() == true)
        {
            album = album.substring(0, m.start());
            logger.finest(getLoggingFilename()+":"+"Album is:" + album + ":");
        }
        year = new String(dataBuffer, FIELD_YEAR_POS, this.FIELD_YEAR_LENGTH).trim();
        m = endofStringPattern.matcher(year);
        if (m.find() == true)
        {
            year = year.substring(0, m.start());
        }
        comment = new String(dataBuffer, FIELD_COMMENT_POS, FIELD_COMMENT_LENGTH).trim();
        m = endofStringPattern.matcher(comment);
        logger.finest(getLoggingFilename()+":"+"Orig Comment is:" + comment + ":");
        if (m.find() == true)
        {
            comment = comment.substring(0, m.start());
            logger.finest(getLoggingFilename()+":"+"Comment is:" + comment + ":");
        }
        genre = dataBuffer[this.FIELD_GENRE_POS];

    }

    /**
     * Does a tag of this version exist within the byteBuffer
     *
     * @return whether tag exists within the byteBuffer
     */
    public boolean seek(ByteBuffer byteBuffer)
    {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        // read the TAG value
        byteBuffer.get(buffer, 0, FIELD_TAGID_LENGTH);        
        return (Arrays.equals(buffer, TAG_ID));
    }

    /**
     * Write this tag to the file, replacing any tag previously existing
     *
     * @param file 
     * @throws IOException 
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
        //Copy the TAGID into new buffer
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
            str = ID3Tags.truncate(year, AbstractID3v1Tag.FIELD_YEAR_LENGTH);
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
        offset = this.FIELD_GENRE_POS;
        if (TagOptionSingleton.getInstance().isId3v1SaveGenre())
        {
            buffer[offset] = genre;
        }
        file.write(buffer);
    }

    /**
     * 
     *
     * Create strcutured representation of this item.
     */
    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_TAG, getIdentifier());
        //Header
        MP3File.getStructureFormatter().addElement(TYPE_TITLE, this.title);
        MP3File.getStructureFormatter().addElement(TYPE_ARTIST, this.artist);
        MP3File.getStructureFormatter().addElement(TYPE_ALBUM, this.album);
        MP3File.getStructureFormatter().addElement(TYPE_YEAR, this.year);
        MP3File.getStructureFormatter().addElement(TYPE_COMMENT, this.comment);
        MP3File.getStructureFormatter().addElement(TYPE_GENRE, (int) this.genre);
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_TAG);
    }
}
