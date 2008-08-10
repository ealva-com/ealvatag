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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

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

    /**
     * ASF reader configured to extract all information.
     */
    private final static AsfHeaderReader FULL_READER;

    /**
     * ASF reader configured to just extract information about audio streams.<br>
     * If the ASF file only contains one audio stream it works fine.<br>
     */
    private final static AsfHeaderReader INFO_READER;

    /**
     * Logger
     */
    public static Logger logger = Logger.getLogger("org.jaudiotabgger.audio"); //$NON-NLS-1$

    /**
     * ASF reader configured to just extract metadata information.<br>
     */
    private final static AsfHeaderReader TAG_READER;

    static
    {
        List<Class<? extends ChunkReader>> readers = new ArrayList<Class<? extends ChunkReader>>();
        readers.add(FileHeaderReader.class);
        readers.add(StreamChunkReader.class);
        INFO_READER = new AsfHeaderReader(readers, true);
        readers.clear();
        readers.add(ContentDescriptionReader.class);
        readers.add(ExtContentDescReader.class);
        TAG_READER = new AsfHeaderReader(readers, true);
        readers.add(FileHeaderReader.class);
        readers.add(StreamChunkReader.class);
        readers.add(EncodingChunkReader.class);
        readers.add(EncryptionChunkReader.class);
        readers.add(StreamBitratePropertiesReader.class);
        FULL_READER = new AsfHeaderReader(readers, false);
    }

    /**
     * Creates a Stream that will read from the specified
     * {@link RandomAccessFile};<br>
     * 
     * @param raf
     *            data source to read from.
     * @return a stream which accesses the source.
     */
    private static InputStream createStream(RandomAccessFile raf) 
    {
        return new FullRequestInputStream(new BufferedInputStream(new RandomAccessFileInputstream(raf)));
    }

    /**
     * This method extracts the full ASF-Header from the given file.<br>
     * If no header could be extracted <code>null</code> is returned. <br>
     * 
     * @param file the ASF file to read.<br>
     * @return  AsfHeader-Wrapper, or <code>null</code> if no supported ASF
     *         header was found.
     * @throws IOException on I/O Errors.
     */
    public static AsfHeader readHeader(File file) throws IOException
    {
        AsfHeader result = null;
        InputStream stream = new FileInputStream(file);
        result = FULL_READER.parseData(stream, 0);
        stream.close();
        return result;
    }

    /**
     * This method tries to extract a full ASF-header out of the given stream. <br>
     * If no header could be extracted <code>null</code> is returned. <br>
     * 
     * @param in
     *            File which contains the ASF header.
     * @return AsfHeader-Wrapper, or <code>null</code> if no supported Asf
     *         header was found.
     * @throws IOException
     *             Read errors
     */
    public static AsfHeader readHeader(RandomAccessFile in) throws IOException
    {
        return FULL_READER.parseData(createStream(in), 0);
    }

    /**
     * This method tries to extract an ASF-header out of the given stream, which
     * only contains information about the audio stream.<br>
     * If no header could be extracted <code>null</code> is returned. <br>
     * 
     * @param in
     *            File which contains the ASF header.
     * @return AsfHeader-Wrapper, or <code>null</code> if no supported Asf
     *         header was found.
     * @throws IOException
     *             Read errors
     */
    public static AsfHeader readInfoHeader(RandomAccessFile in) throws IOException
    {
        return INFO_READER.parseData(createStream(in), 0);
    }

    /**
     * This method tries to extract an ASF-header out of the given stream, which
     * only contains metadata.<br>
     * If no header could be extracted <code>null</code> is returned. <br>
     * 
     * @param in
     *            File which contains the ASF header.
     * @return AsfHeader-Wrapper, or <code>null</code> if no supported Asf
     *         header was found.
     * @throws IOException
     *             Read errors
     */
    public static AsfHeader readTagHeader(RandomAccessFile in) throws IOException
    {
        return TAG_READER.parseData(createStream(in), 0);
    }

    /**
     * If <code>true</code> each chunk type will only be read once.<br>
     */
    private final boolean eachChunkOnce;

    /**
     * Registers GUIDs to their reader classes.<br>
     */
    private final HashMap<GUID, ChunkReader> readerMap = new HashMap<GUID, ChunkReader>();

    /**
     * Creates a reader instance, which only utilizes the given list of chunk
     * readers.<br>
     * 
     * @param toRegister
     *            List of {@link ChunkReader} class instances, which are to be
     *            utilized by the instance.
     * @param readChunkOnce
     *            if <code>true</code>, each chunk type (identified by chunk
     *            GUID) will handled only once, if a reader is available, other
     *            chunks will be discarded.
     */
    public AsfHeaderReader(List<Class<? extends ChunkReader>> toRegister, boolean readChunkOnce)
    {
        this.eachChunkOnce = readChunkOnce;
        for (Class<? extends ChunkReader> curr : toRegister)
        {
            register(curr);
        }
    }

    private ChunkReader getReader(GUID guid)
    {
        return readerMap.get(guid);
    }

    private boolean isReaderAvailable(GUID guid)
    {
        return this.readerMap.containsKey(guid);
    }

    /**
     * This Method implements the reading of the header block. <br>
     * 
     * @param stream
     *            Stream which contains an ASF header.
     * @param inputStart
     *            The start of the ASF header from stream start.<br>
     *            For direct file streams one can assume <code>0</code> here.
     * @return <code>null</code> if no valid data found, else a Wrapper
     *         containing all supported data.
     * @throws IOException
     *             Read errors.
     */
    public AsfHeader parseData(final InputStream stream, final long inputStart) throws IOException
    {
        AsfHeader result = null;
        final long chunkStart = inputStart;
        GUID possibleGuid = Utils.readGUID(stream);

        if (GUID.GUID_HEADER.equals(possibleGuid))
        {
            // For Know the file pointer pointed to an ASF header chunk.
            BigInteger chunkLen = Utils.readBig64(stream);

            long chunkCount = Utils.readUINT32(stream);
            /*
             * 2 reserved bytes. first should be equal to 0x01 and second 0x02. ASF specification
             * suggests to not read the content if second byte is not 0x02.
             */
            if (stream.read() != 1)
            {
                throw new IOException("No ASF");
            }
            if (stream.read() != 2)
            {
                throw new IOException("No ASF");
            }

            /*
             * Creating the resulting object
             */
            result = new AsfHeader(chunkStart, chunkLen, chunkCount);
            HashSet<GUID> alreadyRead = new HashSet<GUID>();
            long currentPosition = chunkStart + 30;
            /*
             * Now reading header of chuncks.
             */
            for (int j = 0; j < chunkCount && alreadyRead.size() != this.readerMap.size(); j++)
            {
                GUID currentGUID = Utils.readGUID(stream);
                boolean skip = this.eachChunkOnce && (!isReaderAvailable(currentGUID) || !alreadyRead.add(currentGUID));
                Chunk chunk;
                if (!skip && isReaderAvailable(currentGUID))
                {
                    chunk = getReader(currentGUID).read(stream);
                }
                else
                {
                    chunk = new ChunkHeaderReader(currentGUID).read(stream);
                }
                if (!skip)
                {
                    result.addChunk(chunk);
                }
                chunk.setPosition(currentPosition);
                currentPosition = chunk.getChunckEnd();
            }
            stream.close();
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