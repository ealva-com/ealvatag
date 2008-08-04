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

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This <i>class </i> reads an ASF header out of an input stream an creates an
 * {@link org.jaudiotagger.audio.asf.data.AsfHeader}object if successful. <br>
 * For now only ASF ver 1.0 is supported, because ver 2.0 seems not to be used
 * anywhere. <br>
 * ASF headers contains other chunks. As of this other readers of current
 * <b>package </b> are called from within.
 *
 * @author Christian Laireiter
 */
public class AsfHeaderReader
{

    //Logger
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio");

    /**
     * This method tries to extract an ASF-header out of the given stream. <br>
     * If no header could be extracted <code>null</code> is returned. <br>
     *
     * @param in File which contains the ASF header.
     * @return AsfHeader-Wrapper, or <code>null</code> if no supported Asf
     *         header was found.
     * @throws IOException Read errors
     */
    public static AsfHeader readHeader(RandomAccessFile in) throws IOException
    {
        AsfHeaderReader reader = new AsfHeaderReader();
        return reader.parseData(in);
    }

    /**
     * Registers GUIDs to their reader classes.<br>
     */
    private final HashMap<GUID, ChunkReader> readerMap;

    /**
     * Protected default constructor. <br>
     * At the time no special use.
     */
    protected AsfHeaderReader()
    {
        // Nothing to do
        readerMap = new HashMap<GUID, ChunkReader>();
        register(ContentDescriptionReader.class);
        register(FileHeaderReader.class);
        register(StreamBitratePropertiesReader.class);
        register(ExtContentDescReader.class);
        register(StreamChunkReader.class);
        register(EncryptionChunkReader.class);
    }

    public ChunkReader getReader(GUID guid)
    {
        return readerMap.get(guid);
    }

    public boolean isReaderAvailable(GUID guid)
    {
        return this.readerMap.containsKey(guid);
    }

    /**
     * This Method implements the reading of the header block. <br>
     *
     * @param in Stream which contains an Asf header.
     * @return <code>null</code> if no valid data found, else a Wrapper
     *         containing all supported data.
     * @throws IOException Read errors.
     */
    private AsfHeader parseData(RandomAccessFile in) throws IOException
    {
        AsfHeader result = null;
        long chunkStart = in.getFilePointer();
        GUID possibleGuid = Utils.readGUID(in);

        if (GUID.GUID_HEADER.equals(possibleGuid))
        {
            RandomAccessFileInputstream stream = new RandomAccessFileInputstream(in);
            // For Know the file pointer pointed to an ASF header chunk.
            BigInteger chunkLen = Utils.readBig64(in);

            long chunkCount = Utils.readUINT32(in);
            // They are of unknown use.
            in.skipBytes(2);

            /*
             * Creating the resulting object
             */
            result = new AsfHeader(chunkStart, chunkLen, chunkCount);

            /*
             * Now reading header of chuncks.
             */
            while (chunkLen.compareTo(BigInteger.valueOf(in.getFilePointer())) > 0)
            {
                long currentPosition = in.getFilePointer();
                GUID currentGUID = Utils.readGUID(stream);
                Chunk chunk;
                if (isReaderAvailable(currentGUID))
                {
                    chunk = getReader(currentGUID).read(stream);
                    result.addChunk(chunk);
                } else {
                    chunk = new ChunkHeaderReader(currentGUID).read(stream);
                    result.addUnspecifiedChunk(chunk);
                }
                chunk.setPosition(currentPosition);
                assert chunk.getChunckEnd() == in.getFilePointer();
            }
        }
        return result;
    }

    /**
     * @param class1
     */
    private <T extends ChunkReader> void register(Class<T> toRegister)
    {
        try
        {
            T reader = toRegister.newInstance();
            this.readerMap.put(reader.getApplyingId(), reader);
        } catch (Exception e)
        {
            logger.severe(e.getMessage());
        }

    }

}