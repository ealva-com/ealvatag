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
        Mp4AudioHeader info = new Mp4AudioHeader();

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

        //Level 5-Searching for "stbl within "minf"
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.STBL.getFieldName());
        if(boxHeader==null)
        {
            throw new CannotReadException("This file does not appear to be an audio file");
        }
        
        //Level 6-Searching for "stsd within "stbl" and process it direct data, dont think these are mandatory so dont throw
        //exception if unable to find
        boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.STSD.getFieldName());
        if(boxHeader!=null)
        {
            Mp4StsdBox stsd = new Mp4StsdBox(boxHeader,mvhdBuffer);
            stsd.processData();
            int positionAfterStsdHeaderAndData = mvhdBuffer.position();
            
            ///Level 7-Searching for "mp4a within "stsd"
            boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.MP4A.getFieldName());
            if(boxHeader!=null)
            {
                ByteBuffer mp4aBuffer = mvhdBuffer.slice();
                Mp4Mp4aBox mp4a = new Mp4Mp4aBox(boxHeader, mp4aBuffer);
                mp4a.processData();
                //Level 8-Searching for "esds" within mp4a to get No Of Channels and bitrate
                boxHeader = Mp4BoxHeader.seekWithinLevel( mp4aBuffer,Mp4NotMetaFieldKey.ESDS.getFieldName());
                if(boxHeader!=null)
                {
                    Mp4EsdsBox esds = new Mp4EsdsBox(boxHeader,mp4aBuffer.slice());

                    //Set Bitrate in kbps
                    info.setBitrate(esds.getAvgBitrate()/1000);

                    //Set Number of Channels
                    info.setChannelNumber(esds.getNumberOfChannels());

                    info.setKind(esds.getKind());
                    info.setProfile(esds.getAudioProfile());
                }
            }
            else
            {
                //Level 7 -Searching for drms within stsd instead (m4p files)
                mvhdBuffer.position(positionAfterStsdHeaderAndData);
                boxHeader = Mp4BoxHeader.seekWithinLevel(mvhdBuffer,Mp4NotMetaFieldKey.DRMS.getFieldName());
                if(boxHeader!=null)
                {
                    Mp4DrmsBox drms = new Mp4DrmsBox(boxHeader,mvhdBuffer);
                    drms.processData();
                    
                    //Level 8-Searching for "esds" within drms to get No Of Channels and bitrate
                    boxHeader = Mp4BoxHeader.seekWithinLevel( mvhdBuffer,Mp4NotMetaFieldKey.ESDS.getFieldName());
                    if(boxHeader!=null)
                    {
                        Mp4EsdsBox esds = new Mp4EsdsBox(boxHeader,mvhdBuffer.slice());

                        //Set Bitrate in kbps
                        info.setBitrate(esds.getAvgBitrate()/1000);

                        //Set Number of Channels
                        info.setChannelNumber(esds.getNumberOfChannels());

                        info.setKind(esds.getKind());
                        info.setProfile(esds.getAudioProfile());
                    }
                }
            }


        }
        //Set default channels if couldnt calculate it
        if(info.getChannelNumber()==-1)
        {
             info.setChannelNumber(2);
        }

        //Set default bitrate if couldnt calculate it
        if(info.getBitRateAsNumber()== -1)
        {
            info.setBitrate(128);
        }

        //This is always the same I think
        info.setEncodingType("AAC");

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
