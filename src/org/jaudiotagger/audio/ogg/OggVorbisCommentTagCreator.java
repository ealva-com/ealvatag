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
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentCreator;

import java.io.UnsupportedEncodingException;
import java.nio.*;

/**
 * Creates an OggVorbis Comment Tag from a VorbisComment for use within an OggVorbis Container
 */
public class OggVorbisCommentTagCreator
{
    private VorbisCommentCreator creator = new VorbisCommentCreator();

    //Creates the ByteBuffer for the ogg tag
    public ByteBuffer convert(Tag tag) throws UnsupportedEncodingException
    {
        ByteBuffer ogg = creator.convert(tag);
        int tagLength = ogg.capacity() + 8;

        ByteBuffer buf = ByteBuffer.allocate(tagLength);

        //[packet type=comment0x03]['vorbiscomment']
        buf.put(new byte[]{(byte) 0x03, (byte) 0x76, (byte) 0x6f, (byte) 0x72, (byte) 0x62, (byte) 0x69, (byte) 0x73});

        //The actual tag
        buf.put(ogg);

        //Framing bit = 1
        buf.put((byte) 0x01);

        buf.rewind();
        return buf;
    }
}
