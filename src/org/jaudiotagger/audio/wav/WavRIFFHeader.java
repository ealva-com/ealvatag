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
import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class WavRIFFHeader
{
    public static final String RIFF_SIGNATURE = "RIFF";
    public static final String WAVE_SIGNATURE = "WAVE";

    public static final int RIFF_SIGNATURE_LENGTH = 4;
    public static final int CHUNK_SIZE_LENGTH = 4;
    public static final int WAVE_SIGNATURE_LENGTH = 4;

    public static boolean isValidHeader(RandomAccessFile raf) throws IOException, CannotReadException
    {
        if (raf.length() < RIFF_SIGNATURE_LENGTH + CHUNK_SIZE_LENGTH + WAVE_SIGNATURE_LENGTH)
        {
            throw new CannotReadException("This is not a WAV File (<12 bytes)");
        }

        ByteBuffer headerBuffer = ByteBuffer.allocate(RIFF_SIGNATURE_LENGTH + CHUNK_SIZE_LENGTH + WAVE_SIGNATURE_LENGTH);
        raf.getChannel().read(headerBuffer);
        headerBuffer.position(0);
        if(Utils.readFourBytesAsChars(headerBuffer).equals(RIFF_SIGNATURE))
        {
            headerBuffer.getInt();
            if(Utils.readFourBytesAsChars(headerBuffer).equals(WAVE_SIGNATURE))
            {
                return true;
            }
        }
        return false;
    }

}