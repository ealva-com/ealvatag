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
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagField;

import java.io.*;

public class VorbisCommentReader {

	public VorbisCommentTag read( RandomAccessFile raf ) throws IOException {
		VorbisCommentTag tag = new VorbisCommentTag();
		
		byte[] b = new byte[4];
		raf.read( b );
		int vendorStringLength = Utils.getNumber( b, 0, 3);
		b = new byte[vendorStringLength];
		raf.read( b );

		tag.setVendor( new String( b, "UTF-8" ) );
		
		b = new byte[4];
		raf.read( b );
		int userComments = Utils.getNumber( b, 0, 3);

		for ( int i = 0; i < userComments; i++ ) {
			b = new byte[4];
			raf.read( b );
			int commentLength = Utils.getNumber( b, 0, 3);
			b = new byte[commentLength];
			raf.read( b );
			
			VorbisCommentTagField fieldComment = new VorbisCommentTagField(b);
			tag.add(fieldComment);
		}
		
		return tag;
	}
}

