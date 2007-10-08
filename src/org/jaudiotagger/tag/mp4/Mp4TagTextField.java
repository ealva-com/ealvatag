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
import java.nio.charset.Charset;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
import org.jaudiotagger.tag.mp4.Mp4TagField;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

/**
 * Represents a single text field
 *
 * Mp4 metadata normally held as follows:
 * MP4Box Parent contains
 *      :length (includes length of data child)  (4 bytes)
 *      :name         (4 bytes)
 *      :child with
 *          :length          (4 bytes)
 *          :name 'Data'     (4 bytes)
 *          :atom version    (1 byte)
 *          :atom type flags (3 bytes)
 *          :null field      (4 bytes)
 *          :data
 *
 * Note:This class is initilized with the child data atom only, the parent data has already been processed, this may
 * change as it seems that code should probably be enscapulated into this.
 *
 */
public class Mp4TagTextField extends Mp4TagField implements TagTextField
{
    protected int    dataSize;
    protected String content;

    /**
     *
     * @param id  parent id
     * @param data atom data
     * @throws UnsupportedEncodingException
     */
    public Mp4TagTextField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }

    /**
     *
     * @param id  parent id
     * @param content data atom data
     */
    public Mp4TagTextField(String id, String content)
    {
        super(id);
        this.content = content;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header  = new Mp4BoxHeader(data);
        Mp4DataBox   databox = new Mp4DataBox(header,data);
        dataSize = header.getDataLength();
        content  = databox.getContent();
    }

    public void copyContent(TagField field)
    {
        if (field instanceof Mp4TagTextField)
        {
            this.content = ((Mp4TagTextField) field).getContent();
        }
    }

    public String getContent()
    {
        return content;
    }

    // This is overriden in the number data box
    protected byte[] getDataBytes() throws UnsupportedEncodingException
    {
        return content.getBytes(getEncoding());
    }

    public String getEncoding()
    {
        return "UTF8";
    }

    /** Convert back to raw content, includes parent and data atom as views as one thing externally
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] getRawContent() throws UnsupportedEncodingException
    {
        byte[] data = getDataBytes();
        byte[] b = new byte[Mp4BoxHeader.HEADER_LENGTH + Mp4DataBox.DATA_HEADER_LENGTH + data.length];

        int offset = Mp4BoxHeader.OFFSET_POS;
        Utils.copy(Utils.getSizeBigEndian(b.length), b, offset);
        offset += Mp4BoxHeader.OFFSET_LENGTH;

        Utils.copy(Utils.getDefaultBytes(getId()), b, offset);
        offset += Mp4BoxHeader.IDENTIFIER_LENGTH;

        Utils.copy(Utils.getSizeBigEndian(Mp4DataBox.DATA_HEADER_LENGTH + data.length), b, offset);
        offset += Mp4BoxHeader.OFFSET_LENGTH;

        Utils.copy(Utils.getDefaultBytes(Mp4DataBox.IDENTIFIER), b, offset);
        offset += Mp4BoxHeader.IDENTIFIER_LENGTH;

        Utils.copy(new byte[]{0, 0, 0}, b, offset);
        offset += Mp4DataBox.VERSION_LENGTH;

        //TODO this is wrong have to set the correct class
        Utils.copy(new byte[]{(byte) (isBinary() ? 0 : 1)}, b, offset);
        offset += Mp4DataBox.TYPE_LENGTH;

        Utils.copy(new byte[]{0, 0, 0, 0}, b, offset);
        offset += Mp4DataBox.NULL_LENGTH;

        Utils.copy(data, b, offset);
        offset += data.length;

        return b;
    }

    public boolean isBinary()
    {
        return false;
    }

    public boolean isEmpty()
    {
        return this.content.trim().equals("");
    }

    public void setContent(String s)
    {
        this.content = s;
    }

    public void setEncoding(String s)
    {
        /* Not allowed */
    }

    public String toString()
    {
        return content;
    }
}
