package org.jaudiotagger.audio.asf.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This implementation of {@link FilterInputStream} counts each read byte.<br>
 * So at each time, with {@link #getReadCount()} one can determine how many bytes have been read, by
 * this classes read and skip methods (mark and reset are also taken into account).<br>  
 * @author Christian Laireiter
 */
class CountingInputStream extends FilterInputStream
{

    /**
     * If {@link #mark(int)} has been called, the current value of {@link #readCount} is stored, in
     * order to reset it upon {@link #reset()}.
     */
    private long markPos = 0;

    /**
     * The amount of read or skipped bytes.
     */
    private long readCount = 0;

    /**
     * Creates an instance, which delegates the commands to the given stream.
     * @param stream stream to actually work with.
     */
    public CountingInputStream(InputStream stream)
    {
        super(stream);
    }

    /**
     * Counts the given amount of bytes.
     * @param i number of bytes to increase.
     */
    private void bytesRead(final long i)
    {
        synchronized (this)
        {
            if (i >= 0)
            {
                readCount += i;
            }
        }
    }

    /**
     * @return the readCount
     */
    public long getReadCount()
    {
        return this.readCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void mark(int readlimit)
    {
        super.mark(readlimit);
        this.markPos = readCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException
    {
        final int result = super.read();
        bytesRead(1);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        final int result = super.read(b, off, len);
        bytesRead(result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset() throws IOException
    {
        super.reset();
        synchronized (this)
        {
            this.readCount = markPos;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long n) throws IOException
    {
        long skipped = super.skip(n);
        bytesRead(skipped);
        return skipped;
    }

}
