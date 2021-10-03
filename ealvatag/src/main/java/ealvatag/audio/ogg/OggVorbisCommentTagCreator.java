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
package ealvatag.audio.ogg;

import ealvatag.audio.ogg.util.VorbisHeader;
import ealvatag.audio.ogg.util.VorbisPacketType;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.vorbiscomment.VorbisCommentCreator;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Creates an OggVorbis Comment Tag from a VorbisComment for use within an OggVorbis Container
 * <p>
 * When a Vorbis Comment is used within OggVorbis it additionally has a vorbis header and a framing
 * bit.
 */
public class OggVorbisCommentTagCreator {

    public static final int FIELD_FRAMING_BIT_LENGTH = 1;
    public static final byte FRAMING_BIT_VALID_VALUE = (byte)0x01;
    private VorbisCommentCreator creator = new VorbisCommentCreator();

    //Creates the ByteBuffer for the ogg tag
    public ByteBuffer convert(TagFieldContainer tag) throws UnsupportedEncodingException {
        ByteBuffer ogg = creator.convert(tag);
        int tagLength =
                ogg.capacity() + VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH +
                        OggVorbisCommentTagCreator.FIELD_FRAMING_BIT_LENGTH;

        ByteBuffer buf = ByteBuffer.allocate(tagLength);

        //[packet type=comment0x03]['vorbis']
        buf.put((byte)VorbisPacketType.COMMENT_HEADER.getType());
        buf.put(VorbisHeader.CAPTURE_PATTERN_AS_BYTES);

        //The actual tag
        buf.put(ogg);

        //Framing bit = 1
        buf.put(FRAMING_BIT_VALID_VALUE);

        buf.rewind();
        return buf;
    }
}
