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

import java.io.UnsupportedEncodingException;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;

/**
 * This class encapsulates the name and content of a tag entry in ogg-files.
 * <br>
 * 
 * @author @author Raphael Slinckx (KiKiDonK)
 * @author Christian Laireiter (liree)
 */
public class VorbisCommentTagField implements TagTextField {

    /**
     * If <code>true</code>, the id of the current encapsulated tag field is
     * specified as a common field. <br>
     * Example is "ARTIST" which should be interpreted by any application as the
     * artist of the media content. <br>
     * Will be set during construction with {@link #checkCommon()}.
     */
    private boolean common;

    /**
     * Stores the content of the tag field. <br>
     */
    private String content;

    /**
     * Stores the id (name) of the tag field. <br>
     */
    private String id;

    /**
     * Creates an instance.
     * 
     * @param raw
     *                   Raw byte data of the tagfield.
     * @throws UnsupportedEncodingException
     *                    If the data doesn't conform "UTF-8" specification.
     */
    public VorbisCommentTagField(byte[] raw) throws UnsupportedEncodingException {
        String field = new String(raw, "UTF-8");

        String[] splitField = field.split("=");
        if (splitField.length > 1) {
            this.id = splitField[0].toUpperCase();
            this.content = splitField[1];
        } else {
            //Either we have "XXXXXXX" without "="
            //Or we have "XXXXXX=" with nothing after the "="
            int i = field.indexOf("="); 
            if(i != -1) {
                this.id = field.substring(0, i+1);
                this.content = "";
            }
            else {
	            //Beware that ogg ID, must be capitalized and contain no space..
	            this.id = "ERRONEOUS";
	            this.content = field;
            }
        }

        checkCommon();
    }

    /**
     * Creates an instance.
     * 
     * @param fieldId
     *                   ID (name) of the field.
     * @param fieldContent
     *                   Content of the field.
     */
    public VorbisCommentTagField(String fieldId, String fieldContent) {
        this.id = fieldId.toUpperCase();
        this.content = fieldContent;
        checkCommon();
    }

    /**
     * This method examines the ID of the current field and modifies
     * {@link #common}in order to reflect if the tag id is a commonly used one.
     * <br>
     */
    private void checkCommon() {
        this.common = id.equals("TITLE") || id.equals("ALBUM")
                || id.equals("ARTIST") || id.equals("GENRE")
                || id.equals("TRACKNUMBER") || id.equals("DATE")
                || id.equals("DESCRIPTION") || id.equals("COMMENT")
                || id.equals("TRACK");
    }

    /**
     * This method will copy all bytes of <code>src</code> to <code>dst</code>
     * at the specified location.
     * 
     * @param src
     *                   bytes to copy.
     * @param dst
     *                   where to copy to.
     * @param dstOffset
     *                   at which position of <code>dst</code> the data should be
     *                   copied.
     */
    protected void copy(byte[] src, byte[] dst, int dstOffset) {
        //        for (int i = 0; i < src.length; i++)
        //            dst[i + dstOffset] = src[i];
        /*
         * Heared that this method is optimized and does its job very near of
         * the system.
         */
        System.arraycopy(src, 0, dst, dstOffset, src.length);
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#copyContent(entagged.audioformats.generic.TagField)
     */
    public void copyContent(TagField field) {
        if (field instanceof TagTextField)
            this.content = ((TagTextField) field).getContent();
    }

    /**
     * This method will try to return the byte representation of the given
     * string after it has been converted to the given encoding. <br>
     * 
     * @param s
     *                   The string whose converted bytes should be returned.
     * @param encoding
     *                   The encoding type to which the string should be converted.
     * @return If <code>encoding</code> is supported the byte data of the
     *               given string is returned in that encoding.
     * @throws UnsupportedEncodingException
     *                    If the requested encoding is not available.
     */
    protected byte[] getBytes(String s, String encoding)
            throws UnsupportedEncodingException {
        return s.getBytes(encoding);
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagTextField#getContent()
     */
    public String getContent() {
        return content;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagTextField#getEncoding()
     */
    public String getEncoding() {
        return "UTF-8";
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#getId()
     */
    public String getId() {
        return this.id;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#getRawContent()
     */
    public byte[] getRawContent() throws UnsupportedEncodingException {
        byte[] size = new byte[4];
        byte[] idBytes = this.id.getBytes();
        byte[] contentBytes = getBytes(this.content, "UTF-8");
        byte[] b = new byte[4 + idBytes.length + 1 + contentBytes.length];

        int length = idBytes.length + 1 + contentBytes.length;
        size[3] = (byte) ((length & 0xFF000000) >> 24);
        size[2] = (byte) ((length & 0x00FF0000) >> 16);
        size[1] = (byte) ((length & 0x0000FF00) >> 8);
        size[0] = (byte) (length & 0x000000FF);

        int offset = 0;
        copy(size, b, offset);
        offset += 4;
        copy(idBytes, b, offset);
        offset += idBytes.length;
        b[offset] = (byte) 0x3D;
        offset++;// "="
        copy(contentBytes, b, offset);

        return b;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#isBinary()
     */
    public boolean isBinary() {
        return false;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#isBinary(boolean)
     */
    public void isBinary(boolean b) {
        if (b) {
            // Only throw if binary = true requested.
            throw new UnsupportedOperationException(
                    "OggTagFields cannot be changed to binary.\n"
                            + "binary data should be stored elsewhere"
                            + " according to Vorbis_I_spec.");
        }
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#isCommon()
     */
    public boolean isCommon() {
        return common;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagField#isEmpty()
     */
    public boolean isEmpty() {
        return this.content.equals("");
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagTextField#setContent(java.lang.String)
     */
    public void setContent(String s) {
        this.content = s;
    }

    /**
     * (overridden)
     * 
     * @see entagged.audioformats.generic.TagTextField#setEncoding(java.lang.String)
     */
    public void setEncoding(String s) {
        if (s == null || !s.equalsIgnoreCase("UTF-8"))
            throw new UnsupportedOperationException(
                    "The encoding of OggTagFields cannot be "
                            + "changed.(specified to be UTF-8)");
    }
    
    public String toString() {
        return getContent();
    }
}