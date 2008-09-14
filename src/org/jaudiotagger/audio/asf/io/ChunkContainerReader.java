package org.jaudiotagger.audio.asf.io;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ChunkContainer;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;


/**
 * This class represents a reader implementation, which is able to read ASF objects (chunks) which
 * store other objects (chunks) within them.<br>
 * 
 * @author Christian Laireiter
 * @param <ChunkType> The {@link ChunkContainer} instance, the implementation will create.
 */
abstract class ChunkContainerReader<ChunkType extends ChunkContainer> implements ChunkReader
{

    /**
     * Within this range, a {@link ChunkReader} should be aware if it fails.
     */
    public final static int READ_LIMIT = 8192;

    /**
     * If <code>true</code> each chunk type will only be read once.<br>
     */
    protected final boolean eachChunkOnce;

    /**
     * If <code>true</code> due to a {@linkplain #register(Class) registered} 
     * chunk reader, all {@link InputStream} objects passed to {@link #read(GUID, InputStream, long)} must
     * support mark/reset.
     */
    protected boolean hasFailingReaders = false;

    /**
     * Logger
     */
    protected Logger logger = Logger.getLogger("org.jaudiotabgger.audio"); //$NON-NLS-1$

    /**
     * Registers GUIDs to their reader classes.<br>
     */
    protected final HashMap<GUID, ChunkReader> readerMap = new HashMap<GUID, ChunkReader>();

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
    protected ChunkContainerReader(List<Class<? extends ChunkReader>> toRegister, boolean readChunkOnce)
    {
        this.eachChunkOnce = readChunkOnce;
        for (Class<? extends ChunkReader> curr : toRegister)
        {
            register(curr);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean canFail()
    {
        return false;
    }

    /**
     * Checks for the constraints of this class.
     * @param stream stream to test.
     * @throws IllegalArgumentException If stream does not meet the requirements.
     */
    protected void checkStream(InputStream stream) throws IllegalArgumentException
    {
        if (this.hasFailingReaders && !stream.markSupported())
        {
            throw new IllegalArgumentException("Stream has to support mark/reset.");
        }
    }

    /**
     * This method is called by {@link #read(GUID, InputStream, long)} in order to create the
     * resulting object. Implementations of this class should now return a new instance
     * of their implementation specific result <b>AND</b> all data should be read, until the list
     * of chunks starts. (The {@link ChunkContainer#getChunckEnd()} must return a sane result, too)<br>
     * 
     * @param streamPosition position of the stream, the chunk starts. 
     * @param chunkLength the length of the chunk (from chunk header)
     * @param stream to read the implementation specific information.
     * @return instance of the implementations result.
     * @throws IOException On I/O Errors and Invalid data.
     */
    abstract protected ChunkType createContainer(long streamPosition, BigInteger chunkLength, InputStream stream) throws IOException;

    /**
     * Gets a configured {@linkplain ChunkReader reader} instance for ASF objects (chunks) with the specified <code>guid</code>.
     * @param guid GUID which identifies the chunk to be read.
     * @return an appropriate reader implementation, <code>null</code> if not {@linkplain #register(Class) registered}.
     */
    protected ChunkReader getReader(GUID guid)
    {
        return readerMap.get(guid);
    }

    /**
     * Tests whether {@link #getReader(GUID)} won't return <code>null</code>.<br>
     * @param guid GUID which identifies the chunk to be read.
     * @return <code>true</code> if a reader is available.
     */
    protected boolean isReaderAvailable(GUID guid)
    {
        return this.readerMap.containsKey(guid);
    }

    /**
     * This Method implements the reading of a chunk container.<br>
     * 
     * @param guid GUID of the currently read container.
     * @param stream
     *            Stream which contains the chunk container.
     * @param chunkStart
     *            The start of the chunk container from stream start.<br>
     *            For direct file streams one can assume <code>0</code> here.
     * @return <code>null</code> if no valid data found, else a Wrapper
     *         containing all supported data.
     * @throws IOException
     *             Read errors.
     * @throws IllegalArgumentException If one used {@link ChunkReader} could 
     *  {@linkplain ChunkReader#canFail() fail} and the stream source
     *   doesn't support mark/reset.
     */
    public ChunkType read(final GUID guid, final InputStream stream, final long chunkStart) throws IOException, IllegalArgumentException
    {
        checkStream(stream);
        final CountingInputStream cis = new CountingInputStream(stream);
        if (!getApplyingId().equals(guid))
        {
            throw new IllegalArgumentException("provided GUID is not supported by this reader.");
        }
        // For Know the file pointer pointed to an ASF header chunk.
        final BigInteger chunkLen = Utils.readBig64(cis);
        /*
         * now read implementation specific information until the chunk 
         * collection starts and create the resulting object. 
         */
        ChunkType result = createContainer(chunkStart, chunkLen, cis);
        // 16 bytes have already been for providing the GUID
        long currentPosition = chunkStart + cis.getReadCount() + 16;

        HashSet<GUID> alreadyRead = new HashSet<GUID>();
        /*
         * Now reading header of chuncks.
         */
        while (currentPosition < result.getChunckEnd())
        {
            GUID currentGUID = Utils.readGUID(cis);
            boolean skip = this.eachChunkOnce && (!isReaderAvailable(currentGUID) || !alreadyRead.add(currentGUID));
            Chunk chunk;
            /*
             * If one reader tells it could fail (new method), then check
             * the input stream for mark/reset. And use it if failed.
             */
            if (!skip && isReaderAvailable(currentGUID))
            {
                final ChunkReader reader = getReader(currentGUID);
                if (reader.canFail())
                {
                    cis.mark(READ_LIMIT);
                }
                chunk = getReader(currentGUID).read(currentGUID, cis, currentPosition);
            }
            else
            {
                chunk = new ChunkHeaderReader().read(currentGUID, cis, currentPosition);
            }
            if (chunk != null)
            {
                if (!skip)
                {
                    result.addChunk(chunk);
                }
                currentPosition = chunk.getChunckEnd();
                // Always take into account, that 16 bytes have been read prior to calling this method
                assert cis.getReadCount() + chunkStart + 16 == currentPosition;
            }
            else
            {
                /*
                 * Reader failed
                 */
                cis.reset();
            }
        }

        return result;
    }

    /**
     * Registers the given reader.<br>
     * @param <T> The actual reader implementation.
     * 
     * @param toRegister chunk reader which is to be registered.
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
