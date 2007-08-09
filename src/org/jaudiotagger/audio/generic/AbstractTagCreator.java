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
package org.jaudiotagger.audio.generic;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;

public abstract class AbstractTagCreator
{

    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException
    {
        return convert(tag, 0);
    }

    public ByteBuffer convert(Tag tag, int padding) throws UnsupportedEncodingException
    {
        Tag compatibleTag = getCompatibleTag(tag);

        List fields = createFields(compatibleTag);
        int tagSize = computeTagLength(compatibleTag, fields);

        ByteBuffer buf = ByteBuffer.allocate(tagSize + padding);
        create(compatibleTag, buf, fields, tagSize, padding);

        buf.rewind();
        return buf;
    }

    protected List createFields(Tag tag) throws UnsupportedEncodingException
    {
        List fields = new LinkedList();

        Iterator it = tag.getFields();
        while (it.hasNext())
        {
            TagField frame = (TagField) it.next();
            fields.add(frame.getRawContent());
        }

        return fields;
    }

    //Compute the number of bytes the tag will be.
    protected int computeTagLength(Tag tag, List l) throws UnsupportedEncodingException
    {
        int length = getFixedTagLength(tag);

        Iterator it = l.iterator();
        while (it.hasNext())
        {
            length += ((byte[]) it.next()).length;
        }

        return length;
    }

    public int getTagLength(Tag tag) throws UnsupportedEncodingException
    {
        Tag compatibleTag = getCompatibleTag(tag);
        List fields = createFields(compatibleTag);
        return computeTagLength(compatibleTag, fields);
    }

    //This method is always called with a compatible tag, as returned from getCompatibleTag()
    protected abstract int getFixedTagLength(Tag tag) throws UnsupportedEncodingException;

    protected abstract Tag getCompatibleTag(Tag tag);

    //This method is always called with a compatible tag, as returned from getCompatibleTag()
    protected abstract void create(Tag tag, ByteBuffer buf, List fields, int tagSize, int padding) throws UnsupportedEncodingException;
}
