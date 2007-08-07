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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;

import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.FileHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * Reads and interprets the data of the file header. <br>
 * 
 * @author Christian Laireiter
 */
public class FileHeaderReader {

	/**
	 * Creates and fills a {@link FileHeader}from given file. <br>
	 * 
	 * @param raf
	 *                  Input
	 * @param candidate
	 *                  Chunk which possibly is a file header.
	 * @return FileHeader if filepointer of <code>raf</code> is at valid
	 *              fileheader.
	 * @throws IOException
	 *                   Read errors.
	 */
	public static FileHeader read(RandomAccessFile raf, Chunk candidate)
			throws IOException {
		if (raf == null || candidate == null) {
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		if (GUID.GUID_FILE.equals(candidate.getGuid())) {
			raf.seek(candidate.getPosition());
			return new FileHeaderReader().parseData(raf);
		}
		return null;
	}

	/**
	 * Should not be used for now.
	 *  
	 */
	protected FileHeaderReader() {
		// NOTHING toDo
	}

	/**
	 * Tries to extract an ASF file header object out of the given input.
	 * 
	 * @param raf
	 * @return <code>null</code> if no valid file header object.
	 * @throws IOException
	 */
	private FileHeader parseData(RandomAccessFile raf) throws IOException {
		FileHeader result = null;
		long fileHeaderStart = raf.getFilePointer();
		GUID guid = Utils.readGUID(raf);
		if (GUID.GUID_FILE.equals(guid)) {
			BigInteger chunckLen = Utils.readBig64(raf);
			// Skip client GUID.
			raf.skipBytes(16);

			BigInteger fileSize = Utils.readBig64(raf);
			if (fileSize.intValue() != raf.length()) {
				System.err
						.println("Filesize of file doesn't match len of Fileheader. ("
								+ fileSize.toString() + ", file: "+raf.length()+")");
			}
			// fileTime in 100 ns since midnight of 1st january 1601 GMT
			BigInteger fileTime = Utils.readBig64(raf);

			BigInteger packageCount = Utils.readBig64(raf);

			BigInteger timeEndPos = Utils.readBig64(raf);
			BigInteger duration = Utils.readBig64(raf);
			BigInteger timeStartPos = Utils.readBig64(raf);

			long flags = Utils.readUINT32(raf);

			long minPkgSize = Utils.readUINT32(raf);
			long maxPkgSize = Utils.readUINT32(raf);
			long uncompressedFrameSize = Utils.readUINT32(raf);

			result = new FileHeader(fileHeaderStart, chunckLen, fileSize,
					fileTime, packageCount, duration, timeStartPos, timeEndPos,
					flags, minPkgSize, maxPkgSize, uncompressedFrameSize);
		}
		return result;
	}

}