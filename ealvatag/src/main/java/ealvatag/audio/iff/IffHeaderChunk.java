package ealvatag.audio.iff;

import ealvatag.audio.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Common to all IFF formats such as Wav and Aiff
 */
public class IffHeaderChunk
{
    public static Logger LOG = LoggerFactory.getLogger(IffHeaderChunk.class);

    public static int SIGNATURE_LENGTH = 4;
    public static int SIZE_LENGTH = 4;
    public static int TYPE_LENGTH = 4;
    public static int HEADER_LENGTH = SIGNATURE_LENGTH + SIZE_LENGTH + TYPE_LENGTH;

    /**
     * If Size is not even then we skip a byte, because chunks have to be aligned
     *
     * @param raf
     * @param chunkHeader
     * @throws java.io.IOException
     */
    public static void ensureOnEqualBoundary(final RandomAccessFile raf,ChunkHeader chunkHeader) throws IOException
    {
        if (Utils.isOddLength(chunkHeader.getSize()))
        {
            // Must come out to an even byte boundary unless at end of file
            if(raf.getFilePointer()<raf.length())
            {
                LOG.trace("Skipping Byte because on odd boundary");
                raf.skipBytes(1);
            }
        }
    }

    public static void ensureOnEqualBoundary(FileChannel fc,ChunkHeader chunkHeader) throws IOException
    {
        if (Utils.isOddLength(chunkHeader.getSize()))
        {
            // Must come out to an even byte boundary unless at end of file
            if(fc.position()<fc.size())
            {
                LOG.trace("Skipping Byte because on odd boundary");
                fc.position(fc.position() + 1);
            }
        }
    }
}
