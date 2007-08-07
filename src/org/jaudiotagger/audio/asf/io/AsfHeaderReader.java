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
import java.util.ArrayList;
import java.util.Iterator;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.EncodingChunk;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.data.FileHeader;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.data.StreamBitratePropertiesChunk;
import org.jaudiotagger.audio.asf.data.StreamChunk;
import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This <i>class </i> reads an Asf header out of an inputstream an creates an
 * {@link entagged.audioformats.asf.data.AsfHeader}object if successfull. <br>
 * For now only ASF ver 1.0 is supported, till ver 2.0 seems not to be used
 * anywhere. <br>
 * Asf headers contains other chunks. As of this other readers of current
 * <b>package </b> are called from within.
 * 
 * @author Christian Laireiter
 */
public class AsfHeaderReader {

	/**
	 * This method tries to extract an ASF-header out of the given stream. <br>
	 * If no header could be extracted <code>null</code> is returned. <br>
	 * 
	 * @param in
	 *            File which contains the ASF header.
	 * @return AsfHeader-Wrapper, or <code>null</code> if no supported Asf
	 *         header was found.
	 * @throws IOException
	 *             Read errors
	 */
	public static AsfHeader readHeader(RandomAccessFile in) throws IOException {
		AsfHeaderReader reader = new AsfHeaderReader();
		return reader.parseData(in);
	}

	/**
	 * Protected default constructor. <br>
	 * At the time no special use.
	 */
	protected AsfHeaderReader() {
		// Nothing to do
	}

	/**
	 * This Method implements the reading of the header block. <br>
	 * 
	 * @param in
	 *            Stream which contains an Asf header.
	 * @return <code>null</code> if no valid data found, else a Wrapper
	 *         containing all supported data.
	 * @throws IOException
	 *             Read errors.
	 */
	private AsfHeader parseData(RandomAccessFile in) throws IOException {
		AsfHeader result = null;
		long chunkStart = in.getFilePointer();
		GUID possibleGuid = Utils.readGUID(in);

		if (GUID.GUID_HEADER.equals(possibleGuid)) {
			// For Know the filepointer pointed to an ASF header chunk.
			BigInteger chunkLen = Utils.readBig64(in);

			long chunkCount = Utils.readUINT32(in);
			// They are of unknown use.
			in.skipBytes(2);

			/*
			 * Now reading header of chuncks.
			 */
			ArrayList chunks = new ArrayList();
			while (chunkLen.compareTo(BigInteger.valueOf(in.getFilePointer())) > 0) {
				Chunk chunk = ChunkHeaderReader.readChunckHeader(in);
				chunks.add(chunk);
				in.seek(chunk.getChunckEnd());
			}

			/*
			 * Creating the resulting object because streamchunks will be added.
			 */
			result = new AsfHeader(chunkStart, chunkLen, chunkCount);
			/*
			 * Now we know all positions and guids of chunks which are contained
			 * whithin asf header. Further we need to identify the type of those
			 * chunks and parse the interesting ones.
			 */
			FileHeader fileHeader = null;
			ExtendedContentDescription extendedDescription = null;
			EncodingChunk encodingChunk = null;
			StreamChunk streamChunk = null;
			ContentDescription contentDescription = null;
			StreamBitratePropertiesChunk bitratePropertiesChunk = null;

			Iterator iterator = chunks.iterator();
			while (iterator.hasNext()) {
				Chunk currentChunk = (Chunk) iterator.next();
				if (fileHeader == null
						&& (fileHeader = FileHeaderReader
								.read(in, currentChunk)) != null) {
					continue;
				}
				if (extendedDescription == null
						&& (extendedDescription = ExtContentDescReader.read(in,
								currentChunk)) != null) {
					continue;
				}
				if (encodingChunk == null
						&& (encodingChunk = EncodingChunkReader.read(in,
								currentChunk)) != null) {
					continue;
				}
				if (streamChunk == null
						&& (streamChunk = StreamChunkReader.read(in,
								currentChunk)) != null) {
					result.addStreamChunk(streamChunk);
					streamChunk = null;
					continue;
				}
				if (contentDescription == null
						&& (contentDescription = ContentDescriptionReader.read(
								in, currentChunk)) != null) {
					continue;
				}
				if (bitratePropertiesChunk == null
						&& (bitratePropertiesChunk = StreamBitratePropertiesReader
								.read(in, currentChunk)) != null) {
					continue;
				}
				/*
				 * If none of the above statements executed the "continue", this
				 * chunk couldn't be interpreted. Despite this the chunk is
				 * remembered
				 */
				result.addUnspecifiedChunk(currentChunk);
			}
			/*
			 * Finally store the parsed chunks in the resulting ASFHeader
			 * object.
			 */
			result.setFileHeader(fileHeader);
			result.setEncodingChunk(encodingChunk);
			/*
			 * Warning, extendedDescription, contentDescription and
			 * bitratePropertiesChunk maybe null since they are optional fields.
			 */
			result.setExtendedContentDescription(extendedDescription);
			result.setContentDescription(contentDescription);
			result.setStreamBitratePropertiesChunk(bitratePropertiesChunk);
		}
		return result;
	}

}