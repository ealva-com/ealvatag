/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.EncryptionChunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This class reads the chunk containing encoding data <br>
 * <b>Warning:<b><br>
 * Implementation is not completed. More analysis of this chunk is needed.
 *
 * @author Christian Laireiter
 */
public class EncryptionChunkReader
{

    /**
     * This reads the current data and interprets it as an encoding chunk. <br>
     * <b>Warning:<b><br>
     * Implementation is not completed. More analysis of this chunk is needed.
     *
     * @param raf       Input source
     * @param candidate Chunk which possibly contains encoding data.
     * @return Encryption info. <code>null</code> if its not a valid encoding
     *         chunk. <br>
     * @throws IOException read errors.
     */
    public static EncryptionChunk read(RandomAccessFile raf, Chunk candidate)
            throws IOException
    {
        if (raf == null || candidate == null)
        {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
        if (GUID.GUID_CONTENT_ENCRYPTION.equals(candidate.getGuid()))
        {
            raf.seek(candidate.getPosition());
            return new EncryptionChunkReader().parseData(raf);
        }
        return null;
    }

    /**
     * Should not be used for now.
     */
    protected EncryptionChunkReader()
    {
        // NOTHING toDo
    }

    /**
     * see {@link #read(RandomAccessFile,Chunk)}
     *
     * @param raf input source.
     * @return Encryption info. <code>null</code> if its not a valid encoding
     *         chunk. <br>
     * @throws IOException read errors.
     */
    private EncryptionChunk parseData(RandomAccessFile raf) throws IOException
    {
        EncryptionChunk result = null;
        long chunkStart = raf.getFilePointer();
        GUID guid = Utils.readGUID(raf);
        if (GUID.GUID_CONTENT_ENCRYPTION.equals(guid))
        {
            BigInteger chunkLen = Utils.readBig64(raf);
            result = new EncryptionChunk(chunkStart, chunkLen);

            // Can't be interpreted
            /*
            * Object ID	GUID	128
            * Object Size	QWORD	64
            * Secret Data Length	DWORD	32
            * Secret Data	BYTE	varies
            * Protection Type Length	DWORD	32
            * Protection Type	char	varies
            * Key ID Length	DWORD	32
            * Key ID	char	varies
            * License URL Length	DWORD	32
            * License URL	char	varies			 * Read the
                */
            byte[] secretData;
            byte[] protectionType;
            byte[] keyID;
            byte[] licenseURL;

            // Secret Data length
            int fieldLength = 0;
            fieldLength = (int) Utils.readUINT32(raf);
            // Secret Data
            secretData = new byte[fieldLength + 1];
            raf.read(secretData, 0, (int) fieldLength);
            secretData[fieldLength] = 0;

            // Protection type Length
            fieldLength = 0;
            fieldLength = (int) Utils.readUINT32(raf);
            // Protection Data Length
            protectionType = new byte[fieldLength + 1];
            raf.read(protectionType, 0, (int) fieldLength);
            protectionType[fieldLength] = 0;

            // Key ID length
            fieldLength = 0;
            fieldLength = (int) Utils.readUINT32(raf);
            // Key ID
            keyID = new byte[fieldLength + 1];
            raf.read(keyID, 0, (int) fieldLength);
            keyID[fieldLength] = 0;

            // License URL length
            fieldLength = 0;
            fieldLength = (int) Utils.readUINT32(raf);
            // License URL
            licenseURL = new byte[fieldLength + 1];
            raf.read(licenseURL, 0, (int) fieldLength);
            licenseURL[fieldLength] = 0;

            result.setSecretData(new String(secretData));
            result.setProtectionType(new String(protectionType));
            result.setKeyID(new String(keyID));
            result.setLicenseURL(new String(licenseURL));
			
		}
		return result;
	}

}