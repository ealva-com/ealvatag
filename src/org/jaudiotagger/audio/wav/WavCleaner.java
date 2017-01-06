package org.jaudiotagger.audio.wav;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.logging.Hex;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

/**
 * Experimental, reads the length of data chiunk and removes all data after that, useful for removing screwed up tags at end of file, but
 * use with care, not very robust.
 */
public class WavCleaner
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.wav");

    private File path;
    private String loggingName;

    public WavCleaner(File path)
    {
        this.path=path;
        this.loggingName=path.getAbsolutePath();
    }

    /**
     * Delete all data after data chunk and print new length
     *
     * @throws Exception
     */
    public void clean() throws Exception
    {
        System.out.println("EndOfDataChunk:" + Hex.asHex(findEndOfDataChunk()));

    }

    /**
     * If find data chunk delete al data after it
     *
     * @return
     * @throws Exception
     */
    private int findEndOfDataChunk() throws Exception
    {
        try(FileChannel fc = new RandomAccessFile(path, "rw").getChannel())
        {
            if(WavRIFFHeader.isValidHeader(fc))
            {
                while (fc.position() < fc.size())
                {
                    int endOfChunk = readChunk(fc);
                    if(endOfChunk>0)
                    {
                        fc.truncate(fc.position());
                        return endOfChunk;
                    }
                }
            }
        }
        return 0;
    }

    /**
     *
     * @param fc
     * @return location of end of data chunk when chunk found
     *
     * @throws IOException
     * @throws CannotReadException
     */
    private int readChunk(FileChannel fc) throws IOException, CannotReadException
    {
        Chunk chunk;
        ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        if (!chunkHeader.readHeader(fc))
        {
            return 0;
        }

        String id = chunkHeader.getID();
        logger.config(loggingName + " Reading Chunk:" + id
                + ":starting at:" +Hex.asDecAndHex(chunkHeader.getStartLocationInFile())
                + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
        final WavChunkType chunkType = WavChunkType.get(id);

        //If known chunkType
        if (chunkType != null)
        {
            switch (chunkType)
            {
                case DATA:
                {

                    fc.position(fc.position() + chunkHeader.getSize());
                    return (int)fc.position();
                }

                //Dont need to do anything with these just skip
                default:
                    logger.config(loggingName + " Skipping chunk bytes:" + chunkHeader.getSize());
                    fc.position(fc.position() + chunkHeader.getSize());
            }
        }
        //Unknown chunk type just skip
        else
        {
            if(chunkHeader.getSize() < 0)
            {
                String msg = loggingName + " Not a valid header, unable to read a sensible size:Header"
                        + chunkHeader.getID()+"Size:"+chunkHeader.getSize();
                logger.severe(msg);
                throw new CannotReadException(msg);
            }
            logger.severe(loggingName + " Skipping chunk bytes:" + chunkHeader.getSize() + " for" + chunkHeader.getID());
            fc.position(fc.position() + chunkHeader.getSize());
        }
        IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
        return 0;
    }

    public static void main(final String[] args) throws Exception
    {
        recursiveDelete(new File("E:\\MQ\\Schubert"), new File("F\\The Last Six Years, vol 4-Imogen Cooper"));
    }

    private static void recursiveDelete(File... paths) throws Exception
    {
        for (File dir : paths)
        {
            for (File next : dir.listFiles(
                    new FileFilter()
                    {
                        @Override public boolean accept(final File file)
                        {
                            return file.isDirectory() || (file.getName().endsWith(".wav") || file.getName().endsWith(".WAV"));
                        }
                    }))
            {
                if (next.isDirectory())
                {
                    recursiveDelete(next);
                } else {
                    WavCleaner wc = new WavCleaner(next);
                    wc.clean();
                }

            }
        }
    }
}
