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
 * This class represents the "Stream Bitrate Properties" chunk of an ASF media
 * file. <br>
 * It is optional, but contains useful information about the streams bitrate.<br>
 *
 * @author Christian Laireiter
 */
public class StreamBitratePropertiesChunk extends Chunk
{

    /**
     * For each call of {@link #addBitrateRecord(int,long)} an {@link Long}
     * object is appended, which represents the average bitrate.
     */
    private final ArrayList<Long> bitRates;

    /**
     * For each call of {@link #addBitrateRecord(int,long)} an {@link Integer}
     * object is appended, which represents the stream-number.
     */
    private final ArrayList<Integer> streamNumbers;

    /**
     * Creates an instance.
     *
     * @param chunkLen Length of current chunk.
     */
    public StreamBitratePropertiesChunk(BigInteger chunkLen)
    {
        super(GUID.GUID_STREAM_BITRATE_PROPERTIES, chunkLen);
        this.bitRates = new ArrayList<Long>();
        this.streamNumbers = new ArrayList<Integer>();
    }

    /**
     * Adds the public values of a stream-record.
     *
     * @param streamNum      The number of the referred stream.
     * @param averageBitrate Its average bitrate.
     */
    public void addBitrateRecord(int streamNum, long averageBitrate)
    {
        this.streamNumbers.add(streamNum);
        this.bitRates.add(averageBitrate);
    }

    /**
     * Returns the average bitrate of the given stream.<br>
     *
     * @param streamNumber Number of the stream whose bitrate to determine.
     * @return The average bitrate of the numbered stream. <code>-1</code> if
     *         no information was given.
     */
    public long getAvgBitrate(int streamNumber)
    {
        Integer seach = streamNumber;
        int index = streamNumbers.indexOf(seach);
        if (index != -1)
        {
            return bitRates.get(index);
        }
        return -1;
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.audio.asf.data.Chunk#prettyPrint(String)
     */
    public String prettyPrint(final String prefix)
    {
        StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        for (int i = 0; i < bitRates.size(); i++)
        {
            result.append(prefix).append("  |-> Stream no. \"").append(streamNumbers.get(i)).append("\" has an average bitrate of \"").append(bitRates.get(i)).append("\"").append(Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }

}
