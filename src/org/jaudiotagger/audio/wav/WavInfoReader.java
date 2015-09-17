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

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.GenericAudioHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 */
public class WavInfoReader
{
    private static final String WAV_RIFF_ENCODING_PREPEND = "WAV-RIFF ";
    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        GenericAudioHeader info = new GenericAudioHeader();
        if(WavRIFFHeader.isValidHeader(raf))
        {
            WavFormatHeader wfh = new WavFormatHeader(raf);
            if (wfh.isValid())
            {
                //TODO if 36 refers to header needs beter calculating
                info.setPreciseLength(((float) raf.length() - (float) 36) / wfh.getBytesPerSecond());
                info.setChannelNumber(wfh.getChannelNumber());
                info.setSamplingRate(wfh.getSamplingRate());
                info.setBitsPerSample(wfh.getBitsPerSample() );
                info.setEncodingType(WAV_RIFF_ENCODING_PREPEND + wfh.getBitsPerSample() + " bits");
                info.setExtraEncodingInfos("");
                info.setBitrate(wfh.getBytesPerSecond() * 8 / 1000);
                info.setVariableBitRate(false);
            }
            else
            {
                throw new CannotReadException("Wav Format Header not valid");
            }
        }
        else
        {
            throw new CannotReadException("Wav RIFF Header not valid");
        }

        return info;
    }
}
