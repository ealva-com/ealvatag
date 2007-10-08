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

/**
 * Abstract class for creating the raw content that represents the tag so it can be written
 * to file.
 */
public abstract class AbstractTagCreator
{

    /**
     *
     * @param tag
     * @return
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException
    {
        return convert(tag, 0);
    }

    /**
     *
     * @param tag
     * @param padding
     * @return
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer convert(Tag tag, int padding) throws UnsupportedEncodingException
    {
        Tag compatibleTag = getCompatibleTag(tag);

        List<byte[]> fields = createFields(compatibleTag);
        int tagSize = computeTagLength(compatibleTag, fields);

        ByteBuffer buf = ByteBuffer.allocate(tagSize + padding);
        create(compatibleTag, buf, fields, tagSize, padding);

        buf.rewind();
        return buf;
    }

    /**
     *
     * @param tag
     * @return
     * @throws UnsupportedEncodingException
     */
    protected List<byte[]> createFields(Tag tag) throws UnsupportedEncodingException
    {
        List <byte[]> fields = new LinkedList<byte[]>();

        Iterator it = tag.getFields();
        while (it.hasNext())
        {
            TagField frame = (TagField) it.next();
            fields.add(frame.getRawContent());
        }

        return fields;
    }

    /**
     * Compute the number of bytes the tag will be.
     *
     * @param tag
     * @param rawFieldData
     * @return
     * @throws UnsupportedEncodingException
     */
    protected int computeTagLength(Tag tag, List<byte[]> rawFieldData) throws UnsupportedEncodingException
    {
        int length = getFixedTagLength(tag);

        Iterator it = rawFieldData.iterator();
        while (it.hasNext())
        {
            length += ((byte[]) it.next()).length;
        }

        return length;
    }

    /**
     *
     * @param tag
     * @return
     * @throws UnsupportedEncodingException
     */
    public int getTagLength(Tag tag) throws UnsupportedEncodingException
    {
        Tag compatibleTag = getCompatibleTag(tag);
        List fields = createFields(compatibleTag);
        return computeTagLength(compatibleTag, fields);
    }

    /**
     * Calculate the minimum tag size or fixed size
     * @param tag
     * @return
     * @throws UnsupportedEncodingException
     */
    protected abstract int getFixedTagLength(Tag tag) throws UnsupportedEncodingException;

    /**
     * Get a suitable tag for this format, converting the provided one if neccessary
     *
     * @param tag
     * @return
     */
    protected abstract Tag getCompatibleTag(Tag tag);

    /**
     * Populate the buffer with the raw tag data
     * @param tag
     * @param buf
     * @param rawFieldData
     * @param tagSize
     * @param padding
     * @throws UnsupportedEncodingException
     */
    protected abstract void create(Tag tag, ByteBuffer buf, List<byte[]> rawFieldData, int tagSize, int padding) throws UnsupportedEncodingException;
}
