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
package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.audio.generic.AbstractTagCreator;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import java.io.UnsupportedEncodingException;
import java.nio.*;
import java.util.*;

/**
 * Create the raw packet data for a Vorbis Comment Tag
 */
public class VorbisCommentCreator extends AbstractTagCreator
{
    /**
     * Populate the buffer with the raw contents of the tag
     *
     * @param tag
     * @param buf
     * @param rawDataFields
     * @param tagSize
     * @param padding
     * @throws UnsupportedEncodingException
     */
    public void create(Tag tag, ByteBuffer buf, List <byte[]>rawDataFields, int tagSize, int padding)
            throws UnsupportedEncodingException
    {
        String vendorString = ((VorbisCommentTag) tag).getVendor();
        int vendorLength = Utils.getUTF8Bytes(vendorString).length;
        buf.put(new byte[]{
            (byte) (vendorLength & 0xFF), 
            (byte) ((vendorLength & 0xFF00) >> 8),
            (byte) ((vendorLength & 0xFF0000) >> 16),
            (byte) ((vendorLength & 0xFF000000) >> 24)});
        buf.put(Utils.getUTF8Bytes(vendorString));

        //[user comment list length]
        int listLength = rawDataFields.size();
        byte[] b = new byte[VorbisCommentReader.FIELD_USER_COMMENT_LIST_LENGTH];
        b[3] = (byte) ((listLength & 0xFF000000) >> 24);
        b[2] = (byte) ((listLength & 0x00FF0000) >> 16);
        b[1] = (byte) ((listLength & 0x0000FF00) >> 8);
        b[0] = (byte) (listLength & 0x000000FF);
        buf.put(b);

        Iterator it = rawDataFields.iterator();
        while (it.hasNext())
        {
            buf.put((byte[]) it.next());
        }
    }

    protected Tag getCompatibleTag(Tag tag)
    {
        if (! (tag instanceof VorbisCommentTag))
        {
            VorbisCommentTag vorbisCommentTag = new VorbisCommentTag();
            vorbisCommentTag.merge(tag);
            return vorbisCommentTag;
        }
        return tag;
    }

    protected int getFixedTagLength(Tag tag) throws UnsupportedEncodingException
    {
        return  VorbisCommentReader.FIELD_VENDOR_LENGTH_LENGTH
                + Utils.getUTF8Bytes(((VorbisCommentTag) tag).getVendor()).length
                + VorbisCommentReader.FIELD_USER_COMMENT_LIST_LENGTH;
    }
}
