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

import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidDataTypeException;

public class BooleanString extends AbstractDataType
{
    /**
     * Creates a new ObjectBooleanString datatype.
     *
     * @param identifier DOCUMENT ME!
     */
    public BooleanString(String identifier, AbstractTagFrameBody frameBody)
    {
        super(identifier, frameBody);
    }

    public BooleanString(BooleanString object)
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
        return 1;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof BooleanString == false)
        {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     * @throws NullPointerException      DOCUMENT ME!
     * @throws IndexOutOfBoundsException DOCUMENT ME!
     */
    public void readByteArray(byte[] arr, int offset) throws InvalidDataTypeException
    {
        byte b = arr[offset];
        value = new Boolean(b != '0');
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return "" + value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] writeByteArray()
    {
        byte[] booleanValue = new byte[1];
        if (value == null)
        {
            booleanValue[0] = '0';
        }
        else
        {
            if (((Boolean) value).booleanValue() == true)
            {
                booleanValue[0] = '0';
            }
            else
            {
                booleanValue[0] = '1';
            }
        }
        return booleanValue;
    }
}
