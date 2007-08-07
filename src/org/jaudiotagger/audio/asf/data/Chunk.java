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

/**
 * This class represents a chunk within asf streams. <br>
 * Each chunk starts with a 16byte guid identifying the type. After that a
 * number (represented by 8 bytes) follows which shows the size in bytes of the
 * chunk. Finally there is the data of the chunk.
 * 
 * @author Christian Laireiter
 */
public class Chunk {

    /**
     * The length of current chunk. <br>
     */
    protected final BigInteger chunkLength;

    /**
     * The guid of represented chunk header.
     */
    protected final GUID guid;

    /**
     * The position of current header object within file or stream.
     */
    protected final long position;

    /**
     * Creates an instance
     * 
     * @param headerGuid
     *                  The GUID of header object.
     * @param pos
     *                  Position of header object within stream or file.
     * @param chunkLen
     *                  Length of current chunk.
     */
    public Chunk(GUID headerGuid, long pos, BigInteger chunkLen) {
        if (headerGuid == null) {
            throw new IllegalArgumentException(
                    "GUID must not be null nor anything else than "
                            + GUID.GUID_LENGTH + " entries long.");
        }
        if (pos < 0) {
            throw new IllegalArgumentException(
                    "Position of header can't be negative.");
        }
        if (chunkLen == null || chunkLen.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "chunkLen must not be null nor negative.");
        }
        this.guid = headerGuid;
        this.position = pos;
        this.chunkLength = chunkLen;
    }

    /**
     * This method returns the End of the current chunk introduced by current
     * header object.
     * 
     * @return Position after current chunk.
     */
    public long getChunckEnd() {
        return position + chunkLength.longValue();
    }

    /**
     * @return Returns the chunkLength.
     */
    public BigInteger getChunkLength() {
        return chunkLength;
    }

    /**
     * @return Returns the guid.
     */
    public GUID getGuid() {
        return guid;
    }

    /**
     * @return Returns the position.
     */
    public long getPosition() {
        return position;
    }

    /**
     * This method creates a String containing usefull information prepared to
     * be printed on stdout. <br>
     * This method is intended to be overwritten by inheriting classes.
     * 
     * @return Information of current Chunk Object.
     */
    public String prettyPrint() {
        StringBuffer result = new StringBuffer();
        result.append("GUID: " + GUID.getGuidDescription(guid));
        result.append("\n   Starts at position: " + getPosition() + "\n");
        result.append("   Last byte at: " + (getChunckEnd() - 1) + "\n\n");
        return result.toString();
    }

    /**
     * (overridden)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return prettyPrint();
    }

}