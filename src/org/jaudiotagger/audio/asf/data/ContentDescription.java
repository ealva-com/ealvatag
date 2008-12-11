/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.io.WriteableChunk;
import org.jaudiotagger.audio.asf.util.Utils;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.logging.ErrorMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * This class represents the data of a chunk which contains title, author,
 * copyright, description and the rating of the file. <br>
 * It is optional within ASF files. But if, exists only once.
 *
 * @author Christian Laireiter
 */
public class ContentDescription extends Chunk implements WriteableChunk
{
    /**
     * File artist.
     */
    private String author = null;

    /**
     * File copyright.
     */
    private String copyRight = null;

    /**
     * File comment.
     */
    private String description = null;

    /**
     * File rating.
     */
    private String rating = null;

    /**
     * File title.
     */
    private String title = null;

    /**
     * Creates an instance. <br>
     */
    public ContentDescription()
    {
        this(0, BigInteger.valueOf(0));
    }

    /**
     * Creates an instance.
     *
     * @param pos      Position of content description within file or stream
     * @param chunkLen Length of content description.
     */
    public ContentDescription(long pos, BigInteger chunkLen)
    {
        super(GUID.GUID_CONTENTDESCRIPTION, pos, chunkLen);
    }

    /**
     * @return Returns the author.
     */
    public String getAuthor()
    {
        if (author == null)
        {
            return "";
        }
        return author;
    }

    /**
     * @return Returns the comment.
     */
    public String getComment()
    {
        if (description == null)
        {
            return "";
        }
        return description;
    }

    /**
     * @return Returns the copyRight.
     */
    public String getCopyRight()
    {
        if (copyRight == null)
        {
            return "";
        }
        return copyRight;
    }

    /**
     * {@inheritDoc}
     */
    public long getCurrentAsfChunkSize()
    {
        long result = 44; // GUID + UINT64 for size + 5 times string length (each
        // 2 bytes) + 5 times zero term char (2 bytes each).
        result += getAuthor().length() * 2; // UTF-16LE
        result += getComment().length() * 2;
        result += getRating().length() * 2;
        result += getTitle().length() * 2;
        result += getCopyRight().length() * 2;
        return result;
    }

    /**
     * @return returns the rating.
     */
    public String getRating()
    {
        if (rating == null)
        {
            return "";
        }
        return rating;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        if (title == null)
        {
            return "";
        }
        return title;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty()
    {
        return Utils.isBlank(author) && Utils.isBlank(copyRight) && Utils.isBlank(description) && Utils.isBlank(rating) && Utils
                        .isBlank(title);
    }

    /**
     * 
     * {@inheritDoc}
     */
    public String prettyPrint(final String prefix)
    {
        StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        result.append(prefix + "  |->Title      : " + getTitle() + Utils.LINE_SEPARATOR);
        result.append(prefix + "  |->Author     : " + getAuthor() + Utils.LINE_SEPARATOR);
        result.append(prefix + "  |->Copyright  : " + getCopyRight() + Utils.LINE_SEPARATOR);
        result.append(prefix + "  |->Description: " + getComment() + Utils.LINE_SEPARATOR);
        result.append(prefix + "  |->Rating     :" + getRating() + Utils.LINE_SEPARATOR);
        return result.toString();
    }

    /**
     * @param fileAuthor The author to set.
     * @throws IllegalArgumentException If "UTF-16LE"-byte-representation would take more than 65535
     *                                  bytes.
     */
    public void setAuthor(String fileAuthor) throws IllegalArgumentException
    {
        if(Utils.isStringLengthValidNullSafe(fileAuthor))
        {
            this.author = fileAuthor;
        }
        else if(TagOptionSingleton.getInstance().isTruncateTextWithoutErrors())
        {
            this.author = fileAuthor.substring(0,Utils.MAXIMUM_STRING_LENGTH_ALLOWED);
        }
        else
        {
            throw new IllegalArgumentException(ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE.getMsg((fileAuthor.length()*2)));
        }
    }

    /**
     * @param tagComment The comment to set.
     * @throws IllegalArgumentException If "UTF-16LE"-byte-representation would take more than 65535
     *                                  bytes.
     */
    public void setComment(String tagComment) throws IllegalArgumentException
    {
        if(Utils.isStringLengthValidNullSafe(tagComment))
        {
            this.description = tagComment;
        }
        else if(TagOptionSingleton.getInstance().isTruncateTextWithoutErrors())
        {
            this.description = tagComment.substring(0,Utils.MAXIMUM_STRING_LENGTH_ALLOWED);
        }
        else
        {
            throw new IllegalArgumentException(ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE.getMsg((tagComment.length()*2)));
        }
    }

    /**
     * @param cpright The copyRight to set.
     * @throws IllegalArgumentException If "UTF-16LE"-byte-representation would take more than 65535
     *                                  bytes.
     */
    public void setCopyRight(String cpright) throws IllegalArgumentException
    {
        if(Utils.isStringLengthValidNullSafe(cpright))
        {
            this.copyRight = cpright;
        }
        else if(TagOptionSingleton.getInstance().isTruncateTextWithoutErrors())
        {
            this.copyRight = cpright.substring(0,Utils.MAXIMUM_STRING_LENGTH_ALLOWED);
        }
        else
        {
            throw new IllegalArgumentException(ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE.getMsg((cpright.length()*2)));
        }
    }

    /**
     * @param ratingText The rating to be set.
     * @throws IllegalArgumentException If "UTF-16LE"-byte-representation would take more than 65535
     *                                  bytes.
     */
    public void setRating(String ratingText) throws IllegalArgumentException
    {
        if(Utils.isStringLengthValidNullSafe(ratingText))
        {
            this.rating = ratingText;
        }
        else if(TagOptionSingleton.getInstance().isTruncateTextWithoutErrors())
        {
            this.rating = ratingText.substring(0,Utils.MAXIMUM_STRING_LENGTH_ALLOWED);
        }
        else
        {
            throw new IllegalArgumentException(ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE.getMsg((ratingText.length()*2)));
        }
    }

    /**
     * @param songTitle The title to set.
     * @throws IllegalArgumentException If "UTF-16LE"-byte-representation would take more than 65535
     *                                  bytes.
     */
    public void setTitle(String songTitle) throws IllegalArgumentException
    {
        if(Utils.isStringLengthValidNullSafe(songTitle))
        {
            this.title = songTitle;
        }
        else if(TagOptionSingleton.getInstance().isTruncateTextWithoutErrors())
        {
            this.title = songTitle.substring(0,Utils.MAXIMUM_STRING_LENGTH_ALLOWED);
        }
        else
        {
            throw new IllegalArgumentException(ErrorMessage.WMA_LENGTH_OF_STRING_IS_TOO_LARGE.getMsg((songTitle.length()*2)));
        }        
    }

    /**
     * {@inheritDoc}
     */
    public long writeInto(OutputStream out) throws IOException
    {
        long chunkSize = getCurrentAsfChunkSize();

        out.write(this.getGuid().getBytes());
        Utils.writeUINT64(getCurrentAsfChunkSize(), out);
        // write the sizes of the string representations plus 2 bytes zero term
        // character
        Utils.writeUINT16(getTitle().length() * 2 + 2, out);
        Utils.writeUINT16(getAuthor().length() * 2 + 2, out);
        Utils.writeUINT16(getCopyRight().length() * 2 + 2, out);
        Utils.writeUINT16(getComment().length() * 2 + 2, out);
        Utils.writeUINT16(getRating().length() * 2 + 2, out);
        // write the Strings
        out.write(Utils.getBytes(getTitle(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getAuthor(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getCopyRight(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getComment(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        out.write(Utils.getBytes(getRating(), AsfHeader.ASF_CHARSET));
        out.write(AsfHeader.ZERO_TERM);
        return chunkSize;
    }
}