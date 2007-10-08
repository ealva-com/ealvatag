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
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Iterator;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.generic.AbstractTagCreator;

public class Mp4TagCreator extends AbstractTagCreator
{

    protected int getFixedTagLength(Tag tag) throws UnsupportedEncodingException
    {
        return 0;
    }

    protected Tag getCompatibleTag(Tag tag)
    {
        if (! (tag instanceof VorbisCommentTag))
        {
            Mp4Tag mp4Tag = new Mp4Tag();
            mp4Tag.merge(tag);
            return mp4Tag;
        }
        return tag;
    }

    //This method is always called with a compatible tag, as returned from getCompatibleTag()
    protected void create(Tag tag, ByteBuffer buf, List<byte[]> rawDataFields, int tagSize, int padding)
            throws UnsupportedEncodingException
    {
        Iterator<byte[]> it = rawDataFields.iterator();
        while (it.hasNext())
        {
            buf.put(it.next());
        }
    }

}
