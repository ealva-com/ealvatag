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
package org.jaudiotagger.audio.mp4;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;
import org.jaudiotagger.audio.mp4.atom.*;

/**
 * Read audio info from file.
 *
 * <p>
 * The info is held in the mvdh and mdhd fields as shown below
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |...............|----- mdia
 * |.......................|---- mdhd
 * |.......................|---- minf
 * |..............................|---- smhd
 * |..............................|---- stbl
 * |......................................|--- stsd
 * |.............................................|--- mp4a
 * |......|----- udta 
 * |
 * |--- mdat
 * </pre>
 */
public class Mp4InfoReader
{
    // Logger Object
    public static Logger logger = Logger.getLogger(" org.jaudiotagger.audio.mp4.atom");

    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        GenericAudioHeader info = new GenericAudioHeader();

        //Get to the facts everything we are interested in is within the moov box, so just load data from file
        //once so no more file I/O needed
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf,Mp4NotMetaFieldKey.MOOV.getFieldName());
        if(moovHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        ByteBuffer   moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        raf.getChannel().read(moovBuffer);
        moovBuffer.rewind();

        //Level 2-Searching for "mvhd" somewhere within "moov", we make a slice after finding header
        //so all get() methods will be relative to mvdh positions
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.MVHD.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        ByteBuffer mvhdBuffer = moovBuffer.slice();
        Mp4MvhdBox mvhd = new Mp4MvhdBox(boxHeader,mvhdBuffer);
        info.setLength(mvhd.getLength());
        //Advance position, TODO should we put this in box code ?
        mvhdBuffer.position(mvhdBuffer.position()+boxHeader.getDataLength());

        //Level 2-Searching for "trak" within "moov"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.TRAK.getFieldName());
        int endOfFirstTrackInBuffer = mvhdBuffer.position() + boxHeader.getDataLength();
        
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        //Level 3-Searching for "mdia" within "trak"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MDIA.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        //Level 4-Searching for "mdhd" within "mdia"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MDHD.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        Mp4MdhdBox mdhd = new Mp4MdhdBox(boxHeader,mvhdBuffer.slice());
        info.setSamplingRate(mdhd.getSampleRate());

        //Level 4-Searching for "minf" within "mdia"
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MINF.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }

        //Level 5-Searching for "smhd" within "minf"
        //Only an audio track would have a smhd frame
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.SMHD.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        mvhdBuffer.position(mvhdBuffer.position() + boxHeader.getDataLength());

        //Level 5-Searching for "stbl within "minf" dont think these are mandatory so dont throw
        //exception if unable to find
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.STBL.getFieldName());
        if(boxHeader!=null)
        {
            //Level 6-Searching for "stsd within "stbl" and process it direct data
            boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.STSD.getFieldName());
            if(boxHeader!=null)
            {
                Mp4StsdBox stsd = new Mp4StsdBox(boxHeader,mvhdBuffer);
                stsd.processData();
                ///Level 7-Searching for "mp4a within "stsd" to get No Of Channels
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MP4A.getFieldName());
                if(boxHeader!=null)
                {
                    Mp4Mp4aBox mp4a = new Mp4Mp4aBox(boxHeader,mvhdBuffer.slice());
                    info.setChannelNumber(mp4a.getNumberOfChannels());
                }
            }
        }
        //Set default if couldnt calculate it
        if(info.getChannelNumber()==-1)
        {
             info.setChannelNumber(2);
        }

        //This is always the same I think
        info.setEncodingType("AAC");

        //TODO: These is defaults as dont know how to calculate
        info.setBitrate(128);

        logger.info(info.toString());


        //Level 2-Searching for another "trak" within "moov", if more than one track exists then we dont
        //know how to deal with it, so reject it.
        mvhdBuffer.position(endOfFirstTrackInBuffer);
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.TRAK.getFieldName());
        if(boxHeader!=null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");    
        }
        return info;
    }


}
