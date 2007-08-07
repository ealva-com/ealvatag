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
package org.jaudiotagger.audio.flac.util;

import org.jaudiotagger.tag.vorbiscomment.VorbisCommentCreator;
import org.jaudiotagger.tag.Tag;

import java.io.UnsupportedEncodingException;
import java.nio.*;

public class FlacTagCreator {
	
	public static final int DEFAULT_PADDING = 4000;
	private static final VorbisCommentCreator creator = new VorbisCommentCreator();
	
	//Creates the ByteBuffer for the ogg tag
	public ByteBuffer convert(Tag tag, int paddingSize) throws UnsupportedEncodingException {
		ByteBuffer ogg = creator.convert(tag);
		int tagLength = ogg.capacity() + 4;
		
		ByteBuffer buf = ByteBuffer.allocate( tagLength + paddingSize );

		//CREATION OF CVORBIS COMMENT METADATA BLOCK HEADER
		//If we have padding, the comment is not the last block (bit[0] = 0)
		//If there is no padding, the comment is the last block (bit[0] = 1)
		byte type =  (paddingSize > 0) ? (byte)0x04 : (byte) 0x84;
		buf.put(type);
		int commentLength = tagLength - 4; //Comment length
		buf.put( new byte[] { (byte)((commentLength & 0xFF0000) >>> 16), (byte)((commentLength & 0xFF00) >>> 8) , (byte)(commentLength&0xFF)  } );

		//The actual tag
		buf.put(ogg);
		
		//PADDING
		if(paddingSize >=4) {
			int paddingDataSize = paddingSize - 4;
			buf.put((byte)0x81); //Last frame, padding 0x81
			buf.put(new byte[]{ (byte)((paddingDataSize&0xFF0000)>>>16),(byte)((paddingDataSize&0xFF00)>>>8),(byte)(paddingDataSize&0xFF) });
			for(int i = 0; i< paddingDataSize; i++)
				buf.put((byte)0);
		}
		buf.rewind();
		
		return buf;
	}
	
	public int getTagLength(Tag tag) throws UnsupportedEncodingException {
		ByteBuffer ogg = creator.convert(tag);
		return ogg.capacity() + 4;
	}
}
