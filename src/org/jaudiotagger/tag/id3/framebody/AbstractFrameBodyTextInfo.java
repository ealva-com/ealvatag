/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.NumberHashMap;
import org.jaudiotagger.tag.datatype.StringSizeTerminated;

import java.io.*;
import java.nio.*;

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

/* Abstract representation of a Text Frame
*/
public abstract class AbstractFrameBodyTextInfo
    extends AbstractID3v2FrameBody
{

    /**
     * Creates a new FrameBodyTextInformation datatype. The super.super
     * Constructor sets up the Object list for the frame.
     */
    protected AbstractFrameBodyTextInfo()
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(TextEncoding.ISO_8859_1));
        setObjectValue(DataTypes.OBJ_TEXT, "");
    }

    /**
     * Copy Constructor
     *
     * @param body AbstractFrameBodyTextInformation
     */
    protected AbstractFrameBodyTextInfo(AbstractFrameBodyTextInfo body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyTextInformation datatype. This is used when user
     * wants to create a new frame based on data in a user interface.
     *
     * @param textEncoding Specifys what encoding should be used to write
     *                     text to file.
     * @param text         Specifies the text String.
     */
    protected AbstractFrameBodyTextInfo(byte textEncoding, String text)
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    /**
     * Creates a new FrameBodyTextInformation datatype from file. The super.super
     * Constructor sets up the Object list for the frame.
     *
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    protected AbstractFrameBodyTextInfo(ByteBuffer byteBuffer,
                                        int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * Set the Text String, used to replace the value within the frame
     *
     * @param text to set
     */
    public void setText(String text)
    {
        setObjectValue(DataTypes.OBJ_TEXT, text);
    }

    /**
     * Retrieve the Text String.
     *
     * @return the text string
     */
    public String getText()
    {
        return (String) getObjectValue(DataTypes.OBJ_TEXT);
    }

    /**
     * Because Text frames have a text encoding we need to check the text
     * String does not contain characters that cannot be encoded in
     * current encoding before we write data. If there are change the
     * encoding.
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        if (((AbstractString) getObject(DataTypes.OBJ_TEXT)).canBeEncoded() == false)
        {
            this.setTextEncoding(TextEncoding.UTF_16BE);
        }
        super.write(tagBuffer);
    }

    /**
     * Setup the Object List. All text frames contain a text encoding
     * and then a text string.
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE ));
        objectList.add(new StringSizeTerminated(DataTypes.OBJ_TEXT, this));
    }

}
