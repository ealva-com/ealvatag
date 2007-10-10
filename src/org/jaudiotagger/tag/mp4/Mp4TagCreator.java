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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Iterator;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.generic.AbstractTagCreator;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;

/**
 * Create raw content of mp4 tag data, concerns itself with atoms upto the ilst atom
 *
 * This is because the ilst atom canbe recreated without reference to existing mp4 fields, but fields
 * above this level are dependent upon other information that is not held in the tag
 *
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |....................|.. ..|
 * |....................|.....|---- @nam (Optional for each metadatafield)
 * |....................|.....|.......|-- data
 * |....................|.....|....... ecetera
 * |....................|.....|---- ---- (Optional for reverse dns field)
 * |....................|.............|-- mean
 * |....................|.............|-- name
 * |....................|.............|-- data
 * |....................|................ ecetere
 * |....................|-- free
 * |--- free
 * |--- mdat
 */
public class Mp4TagCreator extends AbstractTagCreator
{

    protected int getFixedTagLength(Tag tag) throws UnsupportedEncodingException
    {
        //Fixed size of ilst atom
        return Mp4BoxHeader.HEADER_LENGTH;
    }

    protected Tag getCompatibleTag(Tag tag)
    {
        if (! (tag instanceof Mp4Tag))
        {
            Mp4Tag mp4Tag = new Mp4Tag();
            //TODO this method doesnt really work
            mp4Tag.merge(tag);
            return mp4Tag;
        }
        return tag;
    }

    /**
     *
     * @param tag
     * @param buf
     * @param rawDataFields
     * @param tagSize
     * @param padding
     * @throws UnsupportedEncodingException
     */
    protected void create(Tag tag, ByteBuffer buf, List<byte[]> rawDataFields, int tagSize, int padding)
            throws UnsupportedEncodingException
    {
        try
        {
            //Put metadata into baos
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Iterator<byte[]> it = rawDataFields.iterator();
            while (it.hasNext())
            {
                baos.write(it.next());
            }

            //Wrap into ilst box
            ByteArrayOutputStream ilst = new ByteArrayOutputStream();
            ilst.write(Utils.getSizeBigEndian(Mp4BoxHeader.HEADER_LENGTH + baos.size()));
            ilst.write(Utils.getDefaultBytes(Mp4NotMetaFieldKey.ILST.getFieldName()));
            ilst.write(baos.toByteArray());

            //Put into ByteBuffer
            buf.put(ilst.toByteArray());
        }
        catch(IOException ioe)
        {
            //Should never happen as not writng to file at the moment
            throw new RuntimeException(ioe);
        }
    }

}
