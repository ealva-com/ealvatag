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

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.mp4.Mp4TagTextField;

public class Mp4TagTextNumberField extends Mp4TagTextField
{

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
        this.content = Utils.getNumberBigEndian(raw, 16, 19) + "";
    }
}
