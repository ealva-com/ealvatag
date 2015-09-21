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
package org.jaudiotagger.audio.wav.chunk;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.wav.WavSubFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Reads the fmt header, this contains the information required for constructing Audio header
 */
public class WavFormatChunk extends Chunk
{
    private static final int   STANDARD_DATA_SIZE = 18;
    private static final int   EXTENSIBLE_DATA_SIZE = 14;
    private static final int   EXTENSIBLE_DATA_SIZE_WE_NEED = 10;

    private static final String WAV_RIFF_ENCODING_PREPEND = "WAV-RIFF ";

    private boolean isValid = false;

    private int blockAlign,  channelMask;
    private WavSubFormat wsf;
    private GenericAudioHeader info;

    public WavFormatChunk(ByteBuffer chunkData, ChunkHeader hdr, GenericAudioHeader info) throws IOException
    {
        super(chunkData, hdr);
        this.info=info;
    }

    public boolean readChunk() throws IOException
    {

        wsf = WavSubFormat.getByCode( Utils.u(chunkData.getShort()));
        if (wsf!=null)
        {
            info.setChannelNumber(Utils.u(chunkData.getShort()));
            info.setSamplingRate(chunkData.getInt());
            info.setBitrate(chunkData.getInt() * 8 / 1000);
            info.setVariableBitRate(false);
            info.setExtraEncodingInfos("");
            // info.setPreciseLength(((float) raf.length() - (float) 36) / wfh.getBytesPerSecond());
            blockAlign      = Utils.u(chunkData.getShort());
            info.setBitsPerSample(Utils.u(chunkData.getShort()));
            if (wsf == WavSubFormat.FORMAT_EXTENSIBLE)
            {
                int extensibleSize = Utils.u(chunkData.getShort());
                if(extensibleSize == EXTENSIBLE_DATA_SIZE)
                {
                    info.setBitsPerSample(Utils.u(chunkData.getShort()));
                    channelMask = chunkData.getInt();

                    //If Extensible then the actual formatCode is held here
                    wsf = WavSubFormat.getByCode(Utils.u(chunkData.getShort()));
                }
            }
            info.setEncodingType(WAV_RIFF_ENCODING_PREPEND + info.getBitsPerSample() + " bits");
            isValid = true;

            /*
             if (wfh.isValid())
            {
                //TODO if 36 refers to header needs beter calculating
                info.setPreciseLength(((float) raf.length() - (float) 36) / wfh.getBytesPerSecond());


            }
            else
             */
        }
        return true;
    }


    public String toString()
    {
        String out = "RIFF-WAVE Header:\n";
        out += "Is valid?: " + isValid;
        return out;
    }
}
