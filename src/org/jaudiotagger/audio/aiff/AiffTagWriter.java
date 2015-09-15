/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.flac.FlacStreamReader;
import org.jaudiotagger.audio.flac.FlacTagCreator;
import org.jaudiotagger.audio.flac.metadatablock.*;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.flac.FlacTag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Write Flac Tag
 */
public class AiffTagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");


    /**
     * Delete Tag from file
     *
     * @param raf
     * @param tempRaf
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
       //Find ID3 tag chunk
       //and delete
    }


    /**
     * Write tag to file
     *
     * @param tag
     * @param raf
     * @param rafTemp
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {

        //Find existing location of tag if any
        //Is it last tag if so
        //Seek to that location
        //Write new tag at location
    }
}

