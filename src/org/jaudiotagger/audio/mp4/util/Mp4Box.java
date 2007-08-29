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
package org.jaudiotagger.audio.mp4.util;

import org.jaudiotagger.audio.generic.Utils;

/**
 * Information in MP4s are held in boxes (formally known as atoms)
 *
 * All boxes consist of a 4 byte box length (big Endian), and then a 4 byte identifier.
 * The length includes the length of the box including the identifier and the length itself.
 * Then they may contain data and/or sub boxes, if they contain subboxes they are known as a parent box. Parent boxes
 * shouldn't really contain data, but sometimes they do.
 */
public class Mp4Box
{
    public static final int OFFSET_POS = 0;
    public static final int IDENTIFIER_POS = 4;
    public static final int OFFSET_LENGTH = 4;
    public static final int IDENTIFIER_LENGTH = 4;
    public static final int HEADER_LENGTH = OFFSET_LENGTH + IDENTIFIER_LENGTH;

    private String id;
    private int length;

    public void update(byte[] b)
    {
        //Calculate boxsize
        this.length = Utils.getNumberBigEndian(b, OFFSET_POS, OFFSET_LENGTH - 1);

        //Calculate box id
        this.id = Utils.getString(b,IDENTIFIER_POS, IDENTIFIER_LENGTH);

        System.err.println(toString());
    }

    public String getId()
    {
        return id;
    }

    public int getLength()
    {
        return length;
    }

    public String toString()
    {
        return "Box " + id + ":" + length;
    }
}
