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
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * Default reader, Reads GUID and size out of an inputsream and creates a
 * {@link entagged.audioformats.asf.data.Chunk}object.
 * 
 * @author Christian Laireiter
 */
class ChunkHeaderReader {

	/**
	 * Interprets current data as a header of a chunk.
	 * 
	 * @param input
	 *                  inputdata
	 * @return Chunk.
	 * @throws IOException
	 *                   Access errors.
	 */
	public static Chunk readChunckHeader(RandomAccessFile input)
			throws IOException {
		long pos = input.getFilePointer();
		GUID guid = Utils.readGUID(input);
		BigInteger chunkLength = Utils.readBig64(input);
		return new Chunk(guid, pos, chunkLength);
	}

}