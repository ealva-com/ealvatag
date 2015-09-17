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
package org.jaudiotagger.audio.wav;

import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Reads the fmt header, this contains the information required for constructing Audio header
 */
public class WavFormatHeader
{

    public static final String FMT_SIGNATURE = "fmt ";
    private static final int   STANDARD_DATA_SIZE = 24;
    private static final int   EXTENSIBLE_DATA_SIZE = 22;
    private static final int   EXTENSIBLE_DATA_SIZE_WE_NEED = 10;

    private boolean isValid = false;

    private int channels, sampleRate, bytesPerSecond, blockAlign, bitsPerSample, validBitsPerSample, channelMask;
    private WavSubFormat wsf;
    public WavFormatHeader(RandomAccessFile raf) throws IOException
    {
        ByteBuffer headerBuffer = ByteBuffer.allocate(STANDARD_DATA_SIZE + EXTENSIBLE_DATA_SIZE_WE_NEED);
        headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
        raf.getChannel().read(headerBuffer);
        headerBuffer.position(0);

        String sig = Utils.readFourBytesAsChars(headerBuffer);
        if(sig.equals(FMT_SIGNATURE))
        {
            headerBuffer.getInt();
            wsf = WavSubFormat.getByCode( Utils.u(headerBuffer.getShort()));
            if (wsf!=null)
            {
                channels        = Utils.u(headerBuffer.getShort());
                sampleRate      = headerBuffer.getInt();
                bytesPerSecond  = headerBuffer.getInt();
                blockAlign      = Utils.u(headerBuffer.getShort());
                bitsPerSample   = Utils.u(headerBuffer.getShort());
                if (wsf == WavSubFormat.FORMAT_EXTENSIBLE)
                {
                    int extensibleSize = Utils.u(headerBuffer.getShort());
                    if(extensibleSize == EXTENSIBLE_DATA_SIZE)
                    {
                        validBitsPerSample = Utils.u(headerBuffer.getShort());
                        channelMask = headerBuffer.getInt();

                        //If Extensible then the actual formatCode is held here
                        wsf = WavSubFormat.getByCode(Utils.u(headerBuffer.getShort()));
                    }
                }
                isValid = true;
            }
        }
    }


    public int getFormat()
    {
        return wsf.getCode();
    }

    public boolean isValid()
    {
        return isValid;
    }

    public int getChannelMask()
    {
        return channelMask;
    }


    public int getValidBitsPerSample()
    {
        return validBitsPerSample;
    }

    public int getChannelNumber()
    {
        return channels;
    }

    public int getSamplingRate()
    {
        return sampleRate;
    }

    public int getBytesPerSecond()
    {
        return bytesPerSecond;
    }

    public int getBitsPerSample()
    {
        return bitsPerSample;
    }


    public String toString()
    {
        String out = "RIFF-WAVE Header:\n";
        out += "Is valid?: " + isValid;
        return out;
    }
}
