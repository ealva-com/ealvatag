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
 * Abstract superclass of all URL Frames
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.*;

abstract public class AbstractID3v1Tag
    extends AbstractID3Tag
{
    public AbstractID3v1Tag()
    {
    }

    public AbstractID3v1Tag(AbstractID3v1Tag copyObject)
    {
        super(copyObject);
    }

    //If field is less than maximum field length this is how it is terminated
    protected static final byte END_OF_FIELD = 0;

    //Used to detect end of field in String constructed from Data
    protected Pattern endofStringPattern = Pattern.compile("\\x00");

    //Tag ID as held in file
    protected static final byte[] TAG_ID =
        {'T', 'A', 'G'};

    //Fields Lengths common to v1 and v1.1 tags
    protected static final int TAG_LENGTH = 128;
    protected static final int TAG_DATA_LENGTH = 125;
    protected static final int FIELD_TAGID_LENGTH = 3;
    protected static final int FIELD_TITLE_LENGTH = 30;
    protected static final int FIELD_ARTIST_LENGTH = 30;
    protected static final int FIELD_ALBUM_LENGTH = 30;
    protected static final int FIELD_YEAR_LENGTH = 4;
    protected static final int FIELD_GENRE_LENGTH = 1;

    //Field Positions, starting from zero so fits in with Java Terminology
    protected static final int FIELD_TAGID_POS = 0;
    protected static final int FIELD_TITLE_POS = 3;
    protected static final int FIELD_ARTIST_POS = 33;
    protected static final int FIELD_ALBUM_POS = 63;
    protected static final int FIELD_YEAR_POS = 93;
    protected static final int FIELD_GENRE_POS = 127;

    //For writing output
    protected static final String TYPE_TITLE = "title";
    protected static final String TYPE_ARTIST = "artist";
    protected static final String TYPE_ALBUM = "album";
    protected static final String TYPE_YEAR = "year";
    protected static final String TYPE_GENRE = "genre";

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        return TAG_LENGTH;
    }

    /**
     * Delete tag
     * Looks for tag and if found lops it off the file.
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void delete(RandomAccessFile file)
        throws IOException
    {
        if (seek(file))
        {
            file.setLength(file.length() - TAG_LENGTH);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException  DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     */
    public void append(RandomAccessFile file)
        throws IOException, TagException
    {
        AbstractID3v1Tag oldTag;
        try
        {
            oldTag = new ID3v11Tag(file);
            oldTag.append(this);
            oldTag.write(file);
        }
        catch (TagNotFoundException ex)
        {
            try
            {
                oldTag = new ID3v1Tag(file);
                oldTag.append(this);
                oldTag.write(file);
            }
            catch (TagNotFoundException ex2)
            {
                this.write(file);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException  DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     */
    public void overwrite(RandomAccessFile file)
        throws IOException, TagException
    {
        AbstractID3v1Tag oldTag;
        try
        {
            oldTag = new ID3v11Tag(file);
            oldTag.overwrite(this);
            oldTag.write(file);
        }
        catch (TagNotFoundException ex)
        {
            try
            {
                oldTag = new ID3v1Tag(file);
                oldTag.overwrite(this);
                oldTag.write(file);
            }
            catch (TagNotFoundException ex2)
            {
                this.write(file);
            }
        }
    }
}
