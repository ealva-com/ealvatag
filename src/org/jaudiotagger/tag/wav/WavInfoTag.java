/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.tag.wav;

import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagField;

import java.util.EnumSet;

/**
 * Represent wav metadata found in the LISTINFO Chunk
 *
 * An LIST INFO chunk was the original way to store metadata but simailr to ID3v1 it suffers from a limited
 * set of fields, although non-standard extra field cannot be added, notably there is no support for images.
 *
 * Any Wavc editors now instead/addtionally add data with an ID3tag
 */
public class WavInfoTag extends GenericTag
{
    private Long startLocationInFile = null;

    //End location of this chunk
    private Long endLocationInFile = null;

    static
    {
        supportedKeys = EnumSet.of(

                FieldKey.ALBUM,
                FieldKey.ARTIST,
                FieldKey.ALBUM_ARTIST,
                FieldKey.TITLE,
                FieldKey.TRACK,
                FieldKey.GENRE,
                FieldKey.COMMENT,
                FieldKey.YEAR,
                FieldKey.RECORD_LABEL,
                FieldKey.ISRC,
                FieldKey.COMPOSER,
                FieldKey.LYRICIST,
                FieldKey.ENCODER,
                FieldKey.CONDUCTOR,
                FieldKey.RATING);
    }
    public String toString()
    {
        String output = "WAV " + super.toString();
        return output;
    }

    public TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException
    {
        return createField(FieldKey.IS_COMPILATION,String.valueOf(value));
    }

    public Long getStartLocationInFile()
    {
        return startLocationInFile;
    }

    public void setStartLocationInFile(long startLocationInFile)
    {
        this.startLocationInFile = startLocationInFile;
    }

    public Long getEndLocationInFile()
    {
        return endLocationInFile;
    }

    public void setEndLocationInFile(long endLocationInFile)
    {
        this.endLocationInFile = endLocationInFile;
    }
}