/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 *
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.*;

import org.jaudiotagger.tag.id3.ID3v24Frames;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class FrameBodyASPI extends AbstractID3v2FrameBody implements ID3v24FrameBody
{
    /**
     * DOCUMENT ME!
     */
    short[] fraction = null;

    /**
     * DOCUMENT ME!
     */
    int bitsPerPoint = 0;

    /**
     * DOCUMENT ME!
     */
    int dataLength = 0;

    /**
     * DOCUMENT ME!
     */
    int dataStart = 0;

    /**
     * DOCUMENT ME!
     */
    int indexPoints = 0;

    /**
     * Creates a new FrameBodyASPI datatype.
     */
    public FrameBodyASPI()
    {
        //        this.dataStart = 0;
        //        this.dataLength = 0;
        //        this.indexPoints = 0;
        //        this.bitsPerPoint = 0;
        //        this.fraction = new short[0];
    }

    public FrameBodyASPI(FrameBodyASPI copyObject)
    {
        super(copyObject);
        this.fraction = (short[]) copyObject.fraction.clone();
        this.bitsPerPoint = copyObject.bitsPerPoint;
        this.dataLength = copyObject.dataLength;
        this.dataStart = copyObject.dataStart;
        this.indexPoints = copyObject.indexPoints;
    }

    /**
     * Creates a new FrameBodyASPI datatype.
     *
     * @param dataStart    DOCUMENT ME!
     * @param dataLength   DOCUMENT ME!
     * @param indexPoints  DOCUMENT ME!
     * @param bitsPerPoint DOCUMENT ME!
     * @param fraction     DOCUMENT ME!
     */
    public FrameBodyASPI(int dataStart, int dataLength, int indexPoints, int bitsPerPoint, short[] fraction)
    {
        this.dataStart = dataStart;
        this.dataLength = dataLength;
        this.indexPoints = indexPoints;
        this.bitsPerPoint = bitsPerPoint;
        this.fraction = fraction;
    }

    /**
     * Creates a new FrameBodyASPI datatype.          *

     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyASPI(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

      /**
      * The ID3v2 frame identifier
      *
      * @return the ID3v2 frame identifier  for this frame type
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_AUDIO_SEEK_POINT_INDEX;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        return 4 + 4 + 2 + 1 + (this.fraction.length * 2);
    }

    /**
     * This method is not yet supported.
     *
     * @throws java.lang.UnsupportedOperationException
     *          This method is not yet
     *          supported
     */
    public boolean equals(Object obj)
    {
        /**
         * @todo Implement this java.lang.Object method
         */
        throw new java.lang.UnsupportedOperationException("Method equals() not yet implemented.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param byteBuffer
     * @throws InvalidTagException
     */
    public void read(ByteBuffer byteBuffer)
        throws InvalidTagException
    {
        int size = getSize();

        if (size == 0)
        {
            throw new EmptyFrameException("Empty Frame");
        }

        this.dataStart = byteBuffer.getInt();
        this.dataLength = byteBuffer.getInt();
        this.indexPoints = (int) byteBuffer.getShort();
        this.bitsPerPoint = (int) byteBuffer.get();

        fraction = new short[indexPoints];

        for (int i = 0; i < indexPoints; i++)
        {
            if (bitsPerPoint == 8)
            {
                fraction[i] = (short) byteBuffer.get();
            }
            else if (bitsPerPoint == 16)
            {
                fraction[i] = byteBuffer.getShort();
            }
            else
            {
                throw new InvalidFrameException("ASPI bits per point wasn't 8 or 16");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return getIdentifier() + " " + this.dataStart + " " + this.dataLength + " " + this.indexPoints + " " + this.bitsPerPoint + " " + this.fraction.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void write(RandomAccessFile file)
        throws IOException
    {
        file.writeInt(this.dataStart);
        file.writeInt(this.dataLength);
        file.writeShort(this.indexPoints);
        file.writeByte(16);

        for (int i = 0; i < indexPoints; i++)
        {
            file.writeShort(this.fraction[i]);
        }
    }

    /** TODO */
    protected void setupObjectList()
    {

    }
}
