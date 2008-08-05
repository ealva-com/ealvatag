/**
 * 
 */
package org.jaudiotagger.audio.asf.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author claireit
 * 
 */
public class FullRequestInputStream extends FilterInputStream {

	public FullRequestInputStream(InputStream in) {
		super(in);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int totalRead = 0;
		int read = -1;
		while (totalRead < len) {
			read = super.read(b, off + totalRead, len - totalRead);
			if (read >= 0) {
				totalRead += read;
			}
		}
		return totalRead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilterInputStream#skip(long)
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
					// number is extremly large, this can lead to a very long
					// running loop.
					return skipped;
				}
			}
			skipped += currSkipped;
		}
		return skipped;
	}

}
