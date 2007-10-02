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

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.audio.generic.Utils;

/**
 * This abstract class represents the data contents of an MP4Box.
 *
 * There are various subclasses that represent different types of fields
 */
public abstract class Mp4TagField implements TagField
{

    protected String id;

    public Mp4TagField(String id)
    {
        this.id = id;
    }

    public Mp4TagField(String id, byte[] raw) throws UnsupportedEncodingException
    {
        this(id);
        build(raw);
    }

    public String getId()
    {
        return id;
    }

    public void isBinary(boolean b)
    {
        /* One cannot choose if an arbitrary block can be binary or not */
    }

    public boolean isCommon()
    {
        return id.equals("ART") || id.equals("alb") || id.equals("nam") || id.equals("trkn") || id.equals("day") || id.equals("cmt") || id.equals("gen");
    }

    protected byte[] getIdBytes()
    {
        return Utils.getDefaultBytes(getId());
    }

    protected abstract void build(byte[] raw) throws UnsupportedEncodingException;
}
