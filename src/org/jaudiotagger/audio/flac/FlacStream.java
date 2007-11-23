package org.jaudiotagger.audio.flac;

import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.io.RandomAccessFile;
import java.io.IOException;

/**
 * Flac Stream
 * <p/>
 * Identifies this is in fact a flac stream
 */
public class FlacStream
{
    public static final int FLAC_STREAM_IDENTIFIER_LENGTH = 4;
    public static final String FLAC_STREAM_IDENTIFIER = "fLaC";

    /**
     * Reads the stream block to ensure it is a flac file
     * 
     * @param raf
     * @throws IOException
     * @throws CannotReadException
     */
    public static void findStream(RandomAccessFile raf)throws IOException,CannotReadException
    {
        //Begins tag parsing
        if (raf.length() == 0)
        {
            //Empty File
            throw new CannotReadException("Error: File empty");
        }
        raf.seek(0);

        //FLAC Stream
        byte[] b = new byte[FlacStream.FLAC_STREAM_IDENTIFIER_LENGTH];
        raf.read(b);
        String flac = new String(b);
        if (!flac.equals(FlacStream.FLAC_STREAM_IDENTIFIER))
        {
            throw new CannotReadException("fLaC Header not found, not a flac file");
        }

    }
}
