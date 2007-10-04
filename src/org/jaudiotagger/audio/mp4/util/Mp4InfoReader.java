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
package org.jaudiotagger.audio.mp4.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;

/**
 * Read audio info from file.
 *
 * The info is held in the mvdh field as shown below
 *
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta 
 * |
 * |--- mdat
 */
public class Mp4InfoReader
{
    // Logger Object
    public static Logger logger = Logger.getLogger(" org.jaudiotagger.audio.mp4.util");

    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        GenericAudioHeader info = new GenericAudioHeader();

        //Get to the facts everything we are interested in is within the moov box, so just load data from file
        //once so no more file I/O needed
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf,Mp4NotMetaFieldKey.MOOV.getFieldName());

        ByteBuffer   moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();

        //Level 2-Searching for "mvhd" within "moov"
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.MVHD.getFieldName());
        Mp4MvhdBox mvhd = new Mp4MvhdBox(boxHeader,moovBuffer.slice());
        info.setLength(mvhd.getLength());

        logger.info(info.toString());
        return info;
    }


}
