/*
 * Entagged Audio Tag library
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
package org.jaudiotagger.audio.asf.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.TagTextField;

/**
 * This class represents the copyright field of asf files. <br>
 * The copyright cannot be accessed like an ordinary "name"-"value"-property,
 * since it has a defined position within the asf structure. <br>
 * <b>Use: </b> <br>
 * If you want to modify the copyright field of asf files using the entagged
 * audio library, you should access it using this class. <br>
 * You can get a it using {@link entagged.audioformats.Tag#get(String)}. The
 * argument must be the value of {@link #FIELD_ID}, since the reading facility
 * of asf files will place the copyright there. (consider that the copyright
 * field of asf fils is only supported once, you may place several copyright
 * fields into {@link entagged.audioformats.Tag}, however only the first one
 * will be written.) <br>
 * It is recommended to use the {@link #setString(String)}method if possible
 * because only then you will get feedback of validity checks. If your
 * environment doesn't support "UTF-16LE" encoding or the
 * "UTF-16LE"-representation would exceed 65533 Bytes you will be notified.
 * Otherwise if you use the standard method {@link #setContent(String)}, the
 * value "Conversion Exception occured." will be placed on errors. <br>
 * <b>Alternative: </b> <br>
 * You can use your own implementation of
 * {@link entagged.audioformats.generic.TagField}, then you must remember using
 * {@link #FIELD_ID}as the "ID", since the asf writing methods will look at
 * that for writing the file. Additional isCommon() must return
 * <code>true</code><br>
 * <b>Example </b>: <br>
 * <i>Reading copyright </i> <br>
 * 
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Get the copyright field
 * TagTextField copyright = AsfCopyrightField.getCopyright(file.getTag());
 * // Will be null, if no copyright is specified.
 * if (copyright != null)
 * 	System.out.println(copyright.getContent());
 * </code> 
 * </pre>
 * 
 * <br>
 * <br>
 * <i>Modifying copyright </i> <br>
 * 
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Get the copyright field
 * TagTextField copyright = AsfCopyrightField.getCopyright(file.getTag());
 * // Will be null, if no copyright is specified.
 * if (copyright != null) {
 * 	System.out.println(copyright.getContent());
 * 	// setThe value
 * 	copyright.setContent(&quot;It belongs to me now.&quot;);
 * }
 * AudioFileIO.write(file);
 * </code> 
 * </pre>
 * 
 * <br>
 * <br>
 * <i>Creating new copyright </i> <br>
 * 
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Create field
 * TagTextField copyright = new AsfCopyrightField();
 * // setThe value
 * copyright.setContent(&quot;My intellectual property&quot;);
 * // Add the new field
 * file.getTag().set(copyright);
 * AudioFileIO.write(file);
 * </code> 
 * </pre>
 * 
 * <br>
 * <br>
 * <i>Delete copyright </i> <br>
 * 
 * <pre>
 * <code>
 * AudioFile file = AudioFileIO.read(new File(&quot;/somewhere/test.wma&quot;));
 * // Create field
 * TagTextField copyright = new AsfCopyrightField();
 * // Without a value, the field will be deleted.
 * file.getTag().set(copyright);
 * AudioFileIO.write(file);
 * </code> 
 * </pre>
 * 
 * <br>
 * <br>
 * <br>
 * <br>
 * <b>Programming internals </b> <br>
 * This class's method {@link #isCommon()}will always return <code>true</code>,
 * so that the represented value will not be handled as a
 * "name"-"value"-property. The
 * {@link entagged.audioformats.asf.util.TagConverter}will identify this field
 * at
 * {@link entagged.audioformats.asf.util.TagConverter#createContentDescription(Tag)}
 * and place the value into the corresponding java object (
 * {@link entagged.audioformats.asf.data.ContentDescription}).
 * 
 * @author Christian Laireiter
 */
public final class AsfCopyrightField implements TagTextField {

    /**
     * This constant represents the field id of the currently represented
     * copyright field. <br>
     * This value will be filtered at {@link TagConverter}.<br>
     */
    public final static String FIELD_ID = "SPECIAL/WM/COPYRIGHT";

    /**
     * This method should help programmers to extract the copyright field out of
     * asf files. <br>
     *
     * @param tag
     *            The tag which contains the copyright field.
     *
     * @return <code>null</code> if the tag represents a file which is not a
     *         asf file, or no copyright has been entered.
     */
    public static TagTextField getCopyright(Tag tag) {
        TagTextField result = null;
        List list = tag.get(FIELD_ID);
        if (list != null && list.size() > 0) {
            TagField field = (TagField) list.get(0);
            if (field instanceof TagTextField) {
                result = (TagTextField) field;
            }
        }
        return result;
    }

    /**
     * This field contains the value of the copyright field. <br>
     * In other word, the copyright is stored in this field.
     */
    private String value = "";

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagField#copyContent(entagged.audioformats.generic.TagField)
     */
    public void copyContent(TagField field) {
        if (field instanceof TagTextField) {
            value = ((TagTextField) field).getContent();
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagTextField#getContent()
     */
    public String getContent() {
        return value;
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagTextField#getEncoding()
     */
    public String getEncoding() {
        return "UTF-16LE";
    }

    /**
     * (overridden) Will return {@link #FIELD_ID}.
     *
     * @see entagged.audioformats.generic.TagField#getId()
     */
    public String getId() {
        return FIELD_ID;
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagField#getRawContent()
     */
    public byte[] getRawContent() throws UnsupportedEncodingException {
        return value.getBytes("UTF-16LE");
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
            throw new UnsupportedOperationException(
                    "No conversion supported. Copyright is a String");
        }
    }

    /**
     * (overridden) This Method will return true, so it won't be used for the
     * extended content description chunk, where the "name"-"value" properties
     * are stored. <br>
     *
     * @see TagConverter#assignOptionalTagValues(Tag,
     *      ExtendedContentDescription)
     *
     * @see entagged.audioformats.generic.TagField#isCommon()
     */
    public boolean isCommon() {
        // Return true, to let this field be ignored at assignOptionalTagValues
        return true;
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagField#isEmpty()
     */
    public boolean isEmpty() {
        return value.length() == 0;
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagTextField#setContent(java.lang.String)
     */
    public void setContent(String s) {
        try {
            setString(s);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            value = "Conversion Exception occured.";
        }
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagTextField#setEncoding(java.lang.String)
     */
    public void setEncoding(String s) {
        if (s == null || !s.equalsIgnoreCase("UTF-16LE"))
            throw new UnsupportedOperationException(
                    "The encoding of Asf tags cannot be "
                            + "changed.(specified to be UTF-16LE)");
    }

    /**
     * This method sets the content of the current copyright field. <br>
     * The reason for existence of this method is that you will get Exceptions
     * if the given value cannot be used for asf files. <br>
     * The restriction for asf field contents is that they must not exceed 65535
     * bytes including the zero termination character and they must be stored in
     * "UTF-16LE" encoding.
     *
     * @param s
     *            The new copyright.
     *
     * @throws IllegalArgumentException
     *             If the size of the value would exceed 65535 bytes in UTF-16LE
     *             encoding including two bytes for zero termination.
     */
    public void setString(String s) {
        value = s;
        Utils.checkStringLengthNullSafe(value);
    }

    /**
     * (overridden)
     *
     * @see entagged.audioformats.generic.TagField#toString()
     */
    public String toString() {
        return FIELD_ID + ":\"" + getContent() + "\"";
    }
}