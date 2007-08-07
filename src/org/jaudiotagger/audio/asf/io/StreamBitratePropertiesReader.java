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
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.StreamBitratePropertiesChunk;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This class reads the chunk containing the stream bitrate properties.<br>
 * 
 * @author Christian Laireiter
 */
public class StreamBitratePropertiesReader {

	/**
	 * This reads the current data and interprets it as an "stream bitrate
	 * properties" chunk. <br>
	 * 
	 * @param raf
	 *            Input source
	 * @param candidate
	 *            Chunk which possibly contains encoding data.
	 * @return StreamBitratePropertiesChunk, <code>null</code> if its not a
	 *         valid one. <br>
	 * @throws IOException
	 *             read errors.
	 */
	public static StreamBitratePropertiesChunk read(RandomAccessFile raf,
			Chunk candidate) throws IOException {
		if (raf == null || candidate == null) {
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		if (GUID.GUID_STREAM_BITRATE_PROPERTIES.equals(candidate.getGuid())) {
			raf.seek(candidate.getPosition());
			return new StreamBitratePropertiesReader().parseData(raf);
		}
		return null;
	}

	/**
	 * Should not be used for now.
	 */
	protected StreamBitratePropertiesReader() {
		// NOTHING toDo
	}

	/**
	 * see {@link #read(RandomAccessFile, Chunk)}
	 * 
	 * @param raf
	 *            input source.
	 * @return StreamBitratePropertiesChunk, <code>null</code> if its not a
	 *         valid one. <br>
	 * @throws IOException
	 *             read errors.
	 */
	private StreamBitratePropertiesChunk parseData(RandomAccessFile raf)
			throws IOException {
		StreamBitratePropertiesChunk result = null;
		long chunkStart = raf.getFilePointer();
		GUID guid = Utils.readGUID(raf);
		if (GUID.GUID_STREAM_BITRATE_PROPERTIES.equals(guid)) {
			BigInteger chunkLen = Utils.readBig64(raf);
			result = new StreamBitratePropertiesChunk(chunkStart, chunkLen);

			/*
			 * Read the amount of bitrate records
			 */
			long recordCount = Utils.readUINT16(raf);
			for (int i = 0; i < recordCount; i++) {
				int flags = Utils.readUINT16(raf);
				long avgBitrate = Utils.readUINT32(raf);
				result.addBitrateRecord(flags & 0x00FF, avgBitrate);
			}

		}
		return result;
	}

}