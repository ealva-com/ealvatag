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
package org.jaudiotagger.audio.asf.data;

import java.math.BigInteger;
import java.util.ArrayList;

import org.jaudiotagger.audio.asf.util.Utils;

/**
 * This class represents the "Stream Bitrate Properties" chunk of an asf media
 * file. <br>
 * It is optional, but contains useful information about the streams bitrate.<br>
 * 
 * @author Christian Laireiter
 */
public class StreamBitratePropertiesChunk extends Chunk {

	/**
	 * For each call of {@link #addBitrateRecord(int, long)} an {@link Long}
	 * object is appended, which represents the average bitrage.
	 */
	private final ArrayList bitRates;

	/**
	 * For each call of {@link #addBitrateRecord(int, long)} an {@link Integer}
	 * object is appended, which represents the stream-number.
	 */
	private final ArrayList streamNumbers;

	/**
	 * Creates an instance.
	 * 
	 * @param pos
	 *            Position of the chunk within file or stream
	 * @param chunkLen
	 *            Length of current chunk.
	 */
	public StreamBitratePropertiesChunk(long pos, BigInteger chunkSize) {
		super(GUID.GUID_STREAM_BITRATE_PROPERTIES, pos, chunkSize);
		this.bitRates = new ArrayList();
		this.streamNumbers = new ArrayList();
	}

	/**
	 * Adds the public values of a stream-record.
	 * 
	 * @param streamNum
	 *            The number of the refered stream.
	 * @param averageBitrate
	 *            Its average Bitrate.
	 */
	public void addBitrateRecord(int streamNum, long averageBitrate) {
		this.streamNumbers.add(new Integer(streamNum));
		this.bitRates.add(new Long(averageBitrate));
	}

	/**
	 * Returns the average bitrate of the given stream.<br>
	 * 
	 * @param streamNumber
	 *            Number of the stream whose bitrate to determine.
	 * @return The average bitrate of the numbered stream. <code>-1</code> if
	 *         no information was given.
	 */
	public long getAvgBitrate(int streamNumber) {
		Integer seach = new Integer(streamNumber);
		int index = streamNumbers.indexOf(seach);
		if (index != -1) {
			return ((Long) bitRates.get(index)).longValue();
		}
		return -1;
	}

	/**
	 * (overridden)
	 * 
	 * @see entagged.audioformats.asf.data.Chunk#prettyPrint()
	 */
	public String prettyPrint() {
		StringBuffer result = new StringBuffer(super.prettyPrint());
		result.insert(0, Utils.LINE_SEPARATOR + "Stream Bitrate Properties:"
				+ Utils.LINE_SEPARATOR);
		for (int i = 0; i < bitRates.size(); i++) {
			result.append("   Stream no. \"" + streamNumbers.get(i)
					+ "\" has an average bitrate of \"" + bitRates.get(i)
					+ "\"" + Utils.LINE_SEPARATOR);
		}
		result.append(Utils.LINE_SEPARATOR);
		return result.toString();
	}

}
