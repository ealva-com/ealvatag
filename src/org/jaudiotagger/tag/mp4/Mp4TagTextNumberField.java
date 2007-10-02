/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.tag.mp4;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.mp4.Mp4TagTextField;

/**
 * Represents simple text field, but reads the data content as an arry of 15 bit unsigned numbers
 */
public class Mp4TagTextNumberField extends Mp4TagTextField
{
    public static final int NUMBER_LENGTH = 2;

    //Holds the numbers decoded
    private List <Integer> numbers;

    public Mp4TagTextNumberField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TagTextNumberField(String id, byte[] raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    protected byte[] getDataBytes()
    {
        return Utils.getSizeBigEndian(Integer.parseInt(content));
    }

    protected void build(byte[] raw) throws UnsupportedEncodingException
    {
        numbers = new ArrayList<Integer>();
        int dataSize = raw.length - DATA_HEADER_LENGTH;
        for(int i =0;i<(dataSize/ NUMBER_LENGTH);i++)
        {
            int number = Utils.getNumberBigEndian(raw,
                    DATA_HEADER_LENGTH + (i *  NUMBER_LENGTH),
                    DATA_HEADER_LENGTH + (i *  NUMBER_LENGTH) + (NUMBER_LENGTH - 1));
            numbers.add(number);
        }

        //Make String representation  (separate values with slash)
        StringBuffer sb = new StringBuffer();
        ListIterator iterator =  numbers.listIterator();
        while(iterator.hasNext())
        {
            sb.append(iterator.next());
            if(iterator.hasNext())
            {
                sb.append("/");
            }
        }
        content=sb.toString();
    }

    /**
     *
     * @return the individual numbers making up this field
     */
    public List<Integer> getNumbers()
    {
        return numbers;
    }
}
