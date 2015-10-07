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
package org.jaudiotagger.audio.flac.metadatablock;

import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 * Stream Info
 *
 * This block has information about the whole stream, like sample rate, number of channels, total number of samples,
 * etc. It must be present as the first metadata block in the stream. Other metadata blocks may follow, and ones
 * that the decoder doesn't understand, it will skip.
 * Format:
 * Size in bits Info
 * 16 The minimum block size (in samples) used in the stream.
 * 16 The maximum block size (in samples) used in the stream. (Minimum blocksize == maximum blocksize) implies a fixed-blocksize stream.
 * 24 The minimum frame size (in bytes) used in the stream. May be 0 to imply the value is not known.
 * 24 The maximum frame size (in bytes) used in the stream. May be 0 to imply the value is not known.
 * 20 Sample rate in Hz. Though 20 bits are available, the maximum sample rate is limited by the structure of frame headers to 655350Hz. Also,
 * a value of 0 is invalid.
 * 3 	(number of channels)-1. FLAC supports from 1 to 8 channels
 * 5 	(bits per sample)-1. FLAC supports from 4 to 32 bits per sample. Currently the reference encoder and decoders only support up to 24 bits per sample.
 * 36 	Total samples in stream. 'Samples' means inter-channel sample,
 * i.e. one second of 44.1Khz audio will have 44100 samples regardless of the number of channels.
 * A value of zero here means the number of total samples is unknown.
 * 128 	MD5 signature of the unencoded audio data. This allows the decoder to determine if an error exists in the audio data
 * even when the error does not result in an invalid bitstream.
 * NOTES
 * * FLAC specifies a minimum block size of 16 and a maximum block size of 65535, meaning the bit patterns corresponding to the numbers 0-15 in the minimum blocksize and maximum blocksize fields are invalid.
 */
public class MetadataBlockDataStreamInfo  implements MetadataBlockData
{
    public static final int STREAM_INFO_DATA_LENGTH = 34;

    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac.MetadataBlockDataStreamInfo");

    private int minBlockSize, maxBlockSize, minFrameSize, maxFrameSize, samplingRate, samplingRatePerChannel, bitsPerSample, noOfChannels, noOfSamples;
    private float trackLength;
    private String md5;
    private boolean isValid = true;

    private ByteBuffer rawdata;

    public MetadataBlockDataStreamInfo(MetadataBlockHeader header, RandomAccessFile raf) throws IOException
    {
        rawdata = ByteBuffer.allocate(header.getDataLength());
        rawdata.order(ByteOrder.BIG_ENDIAN);
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength())
        {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();

        minBlockSize = Utils.u(rawdata.getShort());
        maxBlockSize = Utils.u(rawdata.getShort());
        minFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());
        maxFrameSize = readThreeByteInteger(rawdata.get(), rawdata.get(), rawdata.get());

        /*
         TODO this codeextract comes from TagLib would like to tidy up our impl
         const uint flags = data.toUInt(pos, true);
         pos += 4;
         d->sampleRate    = flags >> 12;
         d->channels      = ((flags >> 9) &  7) + 1;
         d->bitsPerSample = ((flags >> 4) & 31) + 1;
         */
        samplingRate = readSamplingRate(rawdata.get(), rawdata.get(), rawdata.get());
        noOfChannels = ((u(rawdata.get(12)) & 0x0E) >>> 1) + 1;
        samplingRatePerChannel = samplingRate / noOfChannels;
        bitsPerSample = ((u(rawdata.get(12)) & 0x01) << 4) + ((u(rawdata.get(13)) & 0xF0) >>> 4) + 1;

        // The last 4 bits are the most significant 4 bits for the 36 bit
        // stream length in samples. (Audio files measured in days)
        /*
          TODO this codeextract comes from TagLib would like to tidy up our impl
        const ulonglong hi = flags & 0xf;
        const ulonglong lo = data.toUInt(pos, true);
        pos += 4;

        d->sampleFrames = (hi << 32) | lo;

        if(d->sampleFrames > 0 && d->sampleRate > 0) {
            const double length = d->sampleFrames * 1000.0 / d->sampleRate;
            d->length  = static_cast<int>(length + 0.5);
        }

        if(data.size() >= pos + 16)
            d->signature = data.mid(pos, 16);
        }*/

        noOfSamples = readTotalNumberOfSamples(rawdata.get(13), rawdata.get(14), rawdata.get(15), rawdata.get(16), rawdata.get(17));
        md5 = readMd5();
        trackLength = (float) ((double) noOfSamples / samplingRate);
    }

    private String readMd5()
    {
        StringBuilder sb = new StringBuilder();
        if(rawdata.limit()>=34)
        {
            for (int i = 18; i < 34; i++)
            {
                byte dataByte = rawdata.get(i);
                sb.append(String.format("%x", dataByte));
            }
        }
        return sb.toString();
    }

    /**
     * @return the rawdata as it will be written to file
     */
    public byte[] getBytes()
    {
        return rawdata.array();
    }

    public int getLength()
    {
        return getBytes().length;
    }

    

    public String toString()
    {

        return "MinBlockSize:" + minBlockSize + "MaxBlockSize:" + maxBlockSize + "MinFrameSize:" + minFrameSize + "MaxFrameSize:" + maxFrameSize + "SampleRateTotal:" + samplingRate + "SampleRatePerChannel:" + samplingRatePerChannel + ":Channel number:" + noOfChannels + ":Bits per sample: " + bitsPerSample + ":TotalNumberOfSamples: " + noOfSamples + ":Length: " + trackLength;

    }

    public float getPreciseLength()
    {
        return trackLength;
    }

    public int getNoOfChannels()
    {
        return noOfChannels;
    }

    public int getSamplingRate()
    {
        return samplingRate;
    }

    public int getSamplingRatePerChannel()
    {
        return samplingRatePerChannel;
    }

    public String getEncodingType()
    {
        return "FLAC " + bitsPerSample + " bits";
    }
    
    public int getBitsPerSample()
    {
    	return bitsPerSample;
    }

    public long getNoOfSamples()
    {
        return noOfSamples;
    }

    public String getMD5Signature()
    {
        return md5;
    }

    public boolean isValid()
    {
        return isValid;
    }

    private int readThreeByteInteger(byte b1, byte b2, byte b3)
    {
        int rate = (u(b1) << 16) + (u(b2) << 8) + (u(b3));
        return rate;
    }

    //TODO this code seems to be give a sampling rate over 21 bytes instead of 20 bytes but attempt to change
    //to 21 bytes give wrong value
    private int readSamplingRate(byte b1, byte b2, byte b3)
    {
        int rate = (u(b1) << 12) + (u(b2) << 4) + ((u(b3) & 0xF0) >>> 4);
        return rate;

    }

    private int readTotalNumberOfSamples(byte b1, byte b2, byte b3, byte b4, byte b5)
    {
        int nb = u(b5);
        nb += u(b4) << 8;
        nb += u(b3) << 16;
        nb += u(b2) << 24;
        nb += (u(b1) & 0x0F) << 32;
        return nb;
    }

    private int u(int i)
    {
        return i & 0xFF;
    }
}
