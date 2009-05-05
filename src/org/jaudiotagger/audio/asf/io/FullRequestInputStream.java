package org.jaudiotagger.audio.asf.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This implementation repeatedly reads from the wrapped input stream until the requested amount
 * of bytes are read.<br> 
 * 
 * @author Christian Laireiter
 */
public class FullRequestInputStream extends FilterInputStream {

    /**
     * Creates an instance.
     * @param source stream to read from.
     */
	public FullRequestInputStream(InputStream source) {
		super(source);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int totalRead = 0;
		int read;
		while (totalRead < len) {
			read = super.read(b, off + totalRead, len - totalRead);
			if (read >= 0) {
				totalRead += read;
			}
			if (read == -1)
            {
                throw new IOException((len - totalRead) + " more bytes expected.");
            }
		}
		return totalRead;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public long skip(long n) throws IOException {
		long skipped = 0;
		int zeroSkipCnt = 0;
		long currSkipped;
		while (skipped < n) {
			currSkipped = super.skip(n - skipped);
			if (currSkipped == 0) {
				zeroSkipCnt++;
				if (zeroSkipCnt == 2) {
					// If the skip value exceeds streams size, this and the
					// number is extremely large, this can lead to a very long
					// running loop.
					return skipped;
				}
			}
			skipped += currSkipped;
		}
		return skipped;
	}

}
