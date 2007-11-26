/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.audio.ogg.util.VorbisPacketType;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;

import java.io.*;
import java.nio.IntBuffer;
import java.util.logging.Logger;

/**
 * Create the VorbisCommentTag by reading from the raw packet data
 *
 * <p>This is in the same format whether encoded with Ogg or Flac
 * except the framing bit is only present when used within Ogg Vorbis
 *
 * <pre>
 * From the http://xiph.org/vorbis/doc/Vorbis_I_spec.html#vorbis-spec-comment
 * Read decodes the packet data using the following algorithm:
 *  [vendor_length] = read an unsigned integer of 32 bits
 *  [vendor_string] = read a UTF-8 vector as [vendor_length] octets
 *  [user_comment_list_length] = read an unsigned integer of 32 bits
 *  iterate [user_comment_list_length] times {
 *      5) [length] = read an unsigned integer of 32 bits
 *      6) this iteration's user comment = read a UTF-8 vector as [length] octets
 *    }
 *  [framing_bit] = read a single bit as boolean
 *  if ( [framing_bit] unset or end-of-packet ) then ERROR
 *  done.
 * </pre>
 */
public class VorbisCommentReader
{
      // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.vorbiscomment.VorbisCommentReader");

    public static final int FIELD_VENDOR_LENGTH_POS = 0;
    public static final int FIELD_VENDOR_STRING_POS = 4;

    public static final int FIELD_VENDOR_LENGTH_LENGTH      = 4;
    public static final int FIELD_USER_COMMENT_LIST_LENGTH  = 4;
    public static final int FIELD_COMMENT_LENGTH_LENGTH     = 4;

    /**
     *
     * @param rawdata
     * 
     * @return logical representation of VorbisCommentTag
     *
     * @throws IOException
     * @throws CannotReadException
     */
    public VorbisCommentTag read(byte[] rawdata,boolean isFramingBit) throws IOException, CannotReadException
    {

        VorbisCommentTag tag = new VorbisCommentTag();

        byte[] b = new byte[FIELD_VENDOR_LENGTH_LENGTH];
        System.arraycopy(rawdata,FIELD_VENDOR_LENGTH_POS,b,FIELD_VENDOR_LENGTH_POS,FIELD_VENDOR_LENGTH_LENGTH);
        int pos = FIELD_VENDOR_LENGTH_LENGTH;
        int vendorStringLength = Utils.getNumberLittleEndian(b);

        b = new byte[vendorStringLength];
        System.arraycopy(rawdata,pos,b,0,vendorStringLength);
        pos+=vendorStringLength;
        tag.setVendor(new String(b, VorbisHeader.CHARSET_UTF_8));

        b = new byte[FIELD_USER_COMMENT_LIST_LENGTH];
        System.arraycopy(rawdata,pos,b,0,FIELD_USER_COMMENT_LIST_LENGTH);
        pos+=FIELD_USER_COMMENT_LIST_LENGTH;

        int userComments = Utils.getNumberLittleEndian(b);
        for (int i = 0; i < userComments; i++)
        {
            b = new byte[FIELD_COMMENT_LENGTH_LENGTH];
            System.arraycopy(rawdata,pos,b,0,FIELD_COMMENT_LENGTH_LENGTH);
            pos+=FIELD_COMMENT_LENGTH_LENGTH;

            int commentLength = Utils.getNumberLittleEndian(b);
            b = new byte[commentLength];
            System.arraycopy(rawdata,pos,b,0,commentLength);
            pos+=commentLength;

            VorbisCommentTagField fieldComment = new VorbisCommentTagField(b);
            logger.info("Adding:"+fieldComment.getId());
            tag.add(fieldComment);
        }

        //Check framing bit, only exists when vorbisComment used within OggVorbis       
        if(isFramingBit)
        {
            if ((rawdata[pos]&0x01)!=1)
            {
                throw new CannotReadException("Error: The OGG Stream isn't valid, Vorbis tag valid flag is wrong");
            }
        }
        return tag;
    }
}

