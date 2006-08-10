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
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Involved People List ID3v23 Only
 *
 * Since there might be a lot of people contributing to an audio file in various ways, such as musicians and technicians,
 * the 'Text information frames' are often insufficient to list everyone involved in a project.
 * The 'Involved people list' is a frame containing the names of those involved, and how they were involved.
 * The body simply contains a terminated string with the involvement directly followed by a terminated string with
 * the involvee followed by a new involvement and so on. There may only be one "IPLS" frame in each tag.
 *
 * <Header for 'Involved people list', ID: "IPLS">
 * Text encoding	$xx
 * People list strings	<text strings according to encoding>
 *
 * @TODO currently just allows any number of values, should only really support pairs of values
 */
public class FrameBodyIPLS  extends AbstractID3v2FrameBody implements ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyIPLS datatype.
     */
    public FrameBodyIPLS()
    {
        super();
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(TextEncoding.ISO_8859_1));
    }

    public FrameBodyIPLS(ByteBuffer byteBuffer, int frameSize)
        throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

     /**
      * The ID3v23 frame identifier
      *
      * @return the ID3v23 frame identifier for this frame type
     */
    public String getIdentifier()
    {
        return ID3v23Frames.FRAME_ID_V3_IPLS;
    }

     /**
     * Convert from V4 to V3 Frame
     */
    public FrameBodyIPLS(FrameBodyTIPL body)
    {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING,  new Byte(body.getTextEncoding()));

        String valueAsCommaSeperatedString = (String)body.getObjectValue(DataTypes.OBJ_TEXT);

        PairedTextEncodedStringNullTerminated.ValuePairs  value
            = new PairedTextEncodedStringNullTerminated.ValuePairs();
        StringTokenizer stz = new StringTokenizer(valueAsCommaSeperatedString,",");
        while(stz.hasMoreTokens())
        {
            value.add(stz.nextToken());
        }
        setObjectValue(DataTypes.OBJ_TEXT, value);
    }

     /**
     * Because have a text encoding we need to check the data values do not contain characters that cannot be encoded in
     * current encoding before we write data. If they do change the encoding.
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        if (((PairedTextEncodedStringNullTerminated) getObject(DataTypes.OBJ_TEXT)).canBeEncoded() == false)
        {
            this.setTextEncoding(TextEncoding.UTF_16BE);
        }
        super.write(tagBuffer);
    }

    /** Consists of a text encoding , and then a series of null terminated Strings, there should be an even number
     *  of Strings as they are paired as involvement/involvee */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, TextEncoding.TEXT_ENCODING_FIELD_SIZE ));
        objectList.add(new PairedTextEncodedStringNullTerminated(DataTypes.OBJ_TEXT, this));
    }
}
