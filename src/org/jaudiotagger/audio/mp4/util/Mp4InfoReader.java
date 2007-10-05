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
 * |...............|----- mdia
 * |.......................|---- mdhd
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

        //Level 2-Searching for "mvhd" somewhere within "moov", we make a slice after finding header
        //so all get() methods will be relative to mvdh positions
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.MVHD.getFieldName());
        ByteBuffer mvhdBuffer = moovBuffer.slice();
        Mp4MvhdBox mvhd = new Mp4MvhdBox(boxHeader,mvhdBuffer);
        info.setLength(mvhd.getLength());
        //Advance position, TODO should we put this in box code ?
        mvhdBuffer.position(mvhdBuffer.position()+boxHeader.getDataLength());

        //Level 2-Searching for "trak" within "moov"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.TRAK.getFieldName());

        //Level 3-Searching for "mdia" within "trak"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MDIA.getFieldName());

        //Level 4-Searching for "mdhd" within "mdia"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MDHD.getFieldName());
        Mp4MdhdBox mdhd = new Mp4MdhdBox(boxHeader,mvhdBuffer.slice());
        info.setSamplingRate(mdhd.getSampleRate());

        //TODO: These are just defaults as dont know how to calculate them at the moment
        info.setChannelNumber(2);
        info.setEncodingType("AAC");
        info.setBitrate(128);
        logger.info(info.toString());
        return info;
    }


}
