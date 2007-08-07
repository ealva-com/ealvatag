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

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.mp4.Mp4TagField;
import org.jaudiotagger.audio.generic.Utils;

public class Mp4TagTextField extends Mp4TagField implements TagTextField {

	protected String content;

	public Mp4TagTextField(String id, byte[] raw)
			throws UnsupportedEncodingException {
		super(id, raw);
	}

	public Mp4TagTextField(String id, String content) {
		super(id);
		this.content = content;
	}

	protected void build(byte[] raw) throws UnsupportedEncodingException {
		int dataSize = Utils.getNumberBigEndian(raw, 0, 3);
		this.content = Utils
				.getString(raw, 16, dataSize - 8 - 8, getEncoding());
	}


	public void copyContent(TagField field) {
		if (field instanceof Mp4TagTextField) {
			this.content = ((Mp4TagTextField) field).getContent();
		}
	}

	public String getContent() {
		return content;
	}

	// This is overriden in the number data box
	protected byte[] getDataBytes() throws UnsupportedEncodingException {
		return content.getBytes(getEncoding());
	}

	public String getEncoding() {
		return "ISO-8859-1";
	}

	public byte[] getRawContent() throws UnsupportedEncodingException {
		byte[] data = getDataBytes();
		byte[] b = new byte[4 + 4 + 4 + 4 + 4 + 4 + data.length];

		int offset = 0;
		Utils.copy(Utils.getSizeBigEndian(b.length), b, offset);
		offset += 4;

		Utils.copy(Utils.getDefaultBytes(getId()), b, offset);
		offset += 4;

		Utils.copy(Utils.getSizeBigEndian(4 + 4 + 4 + 4 + data.length), b,
				offset);
		offset += 4;

		Utils.copy(Utils.getDefaultBytes("data"), b, offset);
		offset += 4;

		Utils.copy(new byte[] { 0, 0, 0, (byte) (isBinary() ? 0 : 1) }, b,
				offset);
		offset += 4;

		Utils.copy(new byte[] { 0, 0, 0, 0 }, b, offset);
		offset += 4;

		Utils.copy(data, b, offset);
		offset += data.length;

		return b;
	}

	public boolean isBinary() {
		return false;
	}

	public boolean isEmpty() {
		return this.content.trim().equals("");
	}

	public void setContent(String s) {
		this.content = s;
	}

	public void setEncoding(String s) {
		/* Not allowed */
	}

	public String toString() {
		return content;
	}
}
