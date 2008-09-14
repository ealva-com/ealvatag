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

import org.jaudiotagger.audio.asf.util.Utils;

/**
 * @author Christian Laireiter
 */
public class VideoStreamChunk extends StreamChunk
{

    /**
     * Stores the codecs id. Normally the Four-CC (4-Bytes).
     */
    private byte[] codecId;

    /**
     * Thies field stores the height of the video stream.
     */
    private long pictureHeight;

    /**
     * This field stores the width of the video stream.
     */
    private long pictureWidth;

    /**
     * Creates an instance.
     *
     * @param chunkLen Length of the entire chunk (including guid and size)
     */
    public VideoStreamChunk(BigInteger chunkLen)
    {
        super(GUID.GUID_VIDEOSTREAM, chunkLen);
    }

    /**
     * @return Returns the codecId.
     */
    public byte[] getCodecId()
    {
        return this.codecId;
    }

    /**
     * Returns the {@link #getCodecId()}, as a String, where each byte has been
     * converted to a <code>char</code>.
     *
     * @return Codec Id as String.
     */
    public String getCodecIdAsString()
    {
        if (getCodecId() != null)
        {
            return new String(getCodecId());
        }
        return "Unknown";
    }

    /**
     * @return Returns the pictureHeight.
     */
    public long getPictureHeight()
    {
        return pictureHeight;
    }

    /**
     * @return Returns the pictureWidth.
     */
    public long getPictureWidth()
    {
        return pictureWidth;
    }

    /**
     * (overridden)
     *
     * @see org.jaudiotagger.audio.asf.data.StreamChunk#prettyPrint(String)
     */
    public String prettyPrint(final String prefix)
    {
        StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        result.insert(0, Utils.LINE_SEPARATOR + prefix + "|->VideoStream");
        result.append(prefix + "Video info:" + Utils.LINE_SEPARATOR);
        result.append(prefix + "      |->Width  : " + getPictureWidth()
                + Utils.LINE_SEPARATOR);
        result.append(prefix + "      |->Heigth : " + getPictureHeight()
                + Utils.LINE_SEPARATOR);
        result.append(prefix + "      |->Codec  : " + getCodecIdAsString()
                + Utils.LINE_SEPARATOR);
        return result.toString();
    }

    /**
     * @param codecIdentifier The codecId to set.
     */
    public void setCodecId(byte[] codecIdentifier)
    {
        this.codecId = codecIdentifier;
    }

    /**
     * @param picHeight
     */
    public void setPictureHeight(long picHeight)
    {
        this.pictureHeight = picHeight;
    }

    /**
     * @param picWidth
     */
    public void setPictureWidth(long picWidth)
    {
        this.pictureWidth = picWidth;
    }
}