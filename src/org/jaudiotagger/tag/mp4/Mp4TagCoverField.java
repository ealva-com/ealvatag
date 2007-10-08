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

import org.jaudiotagger.tag.mp4.Mp4TagBinaryField;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * For storing coverart
 *  
 */
public class Mp4TagCoverField extends Mp4TagBinaryField
{
    private int type;
    public Mp4TagCoverField()
    {
        super(Mp4FieldKey.ARTWORK.getFieldName());
    }

    public Mp4TagCoverField(ByteBuffer raw,int type) throws UnsupportedEncodingException
    {
        super(Mp4FieldKey.ARTWORK.getFieldName(), raw);
        this.type=type;
    }

    public boolean isBinary()
    {
        return true;
    }

    /**
     * Identifies the image type, only jpg and png are supported.
     *
     * @return
     */
    public int getType()
    {
        return type;
    }
}
