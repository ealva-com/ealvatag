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
 */
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.AbstractTagFrameBody;

public class ByteArraySizeTerminated extends AbstractDataType
{
    public ByteArraySizeTerminated(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public ByteArraySizeTerminated(ByteArraySizeTerminated object)
    {
        super(object);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int len = 0;

        if (value != null)
        {
            len = ((byte[]) value).length;
        }

        return len;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof ByteArraySizeTerminated == false)
        {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr    DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readByteArray(byte[] arr, int offset)
    {
        if (arr == null)
        {
            throw new NullPointerException("Byte array is null");
        }

        if (offset < 0)
        {
            throw new IndexOutOfBoundsException("Offset to byte array is out of bounds: offset = " + offset + ", array.length = " + arr.length);
        }

        //Empty Byte Array
        if (offset >= arr.length)
        {
            value = null;
            return;
        }

        int len = arr.length - offset;
        value = new byte[len];
        System.arraycopy(arr, offset, value, 0, len);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return getSize() + " bytes";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] writeByteArray()
    {
        logger.info("Writing byte array" + this.getIdentifier());
        return (byte[]) value;
    }
}
