/**
 * 
 */
package org.jaudiotagger.audio.asf.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Wraps a {@link RandomAccessFile} into an {@link InputStream}.<br>
 * @author Christian Laireiter
 */
final class RandomAccessFileInputstream extends InputStream
{

    /**
     * The file access to read from.<br>
     */
    private final RandomAccessFile source;

    /**
     * Will increase by the read methods.<br>
     * Should represent the absolute position in a file.<br>
     */
    private long streamPosition;

    /**
     * Creates an instance that will provide {@link InputStream} functionality on the given
     * {@link RandomAccessFile} by delegating calls.<br>
     * 
     * @param file The file to read.
     */
    public RandomAccessFileInputstream(final RandomAccessFile file)
    {
        if (file == null)
        {
            throw new NullPointerException();
        }
        this.source = file;
    }

    /**
     * @return the streamPosition
     */
    public long getStreamPosition()
    {
        return this.streamPosition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException
    {
        streamPosition++;
        return source.read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int read = source.read(b, off, len);
        if (read != -1)
        {
            this.streamPosition += read;
        }
        return read;
    }

    /**
     * @param position the streamPosition to set
     */
    public void setStreamPosition(long position)
    {
        this.streamPosition = position;
    }

}
