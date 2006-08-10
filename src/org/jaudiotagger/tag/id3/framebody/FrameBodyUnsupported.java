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
 * Frame that is not currently suported by this application
 *
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidFrameException;
import org.jaudiotagger.tag.InvalidTagException;

import java.nio.ByteBuffer;

/** Represents a framebody for a frame identifier that is not defined for the tag version. It is possible that
 * the identifier is valid for a later version but not the version that this frame belongs to. If the identifier is
 * valid for an earlier tag version but not this version then this class should not be used instead use
 * FrameBodyDeprecated. The body consists  of an array of bytes representing all the bytes in the body.
 */
public class FrameBodyUnsupported extends AbstractID3v2FrameBody
{
    /**
     * Because used by any unknown frame identifier varies
     */
    private String identifier = "";

    /**
     * @deprecated  because no identifier set
     */
    public FrameBodyUnsupported()
    {

    }

    /**
     * Creates a new FrameBodyUnsupported
     *
     */
    public FrameBodyUnsupported(String identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Create a new FrameBodyUnsupported
     * @param identifier
     * @param value
     */
    public FrameBodyUnsupported(String identifier,byte[] value)
    {
        this.identifier = identifier;
        setObjectValue(DataTypes.OBJ_DATA, value);
    }

    /**
     * Creates a new FrameBodyUnsupported datatype.
     * @deprecated because no identifier set
     * @param value 
     */
    public FrameBodyUnsupported(byte[] value)
    {
        setObjectValue(DataTypes.OBJ_DATA, value);
    }

    /**
     * Copy constructor
     *
     * @param copyObject a copy is made of this
     */
    public FrameBodyUnsupported(FrameBodyUnsupported copyObject)
    {
        super(copyObject);
        this.identifier = new String(copyObject.identifier);

    }

    /**
     * Creates a new FrameBodyUnsupported datatype.
     *
     * @throws InvalidFrameException if unable to create framebody from buffer
     */
    public FrameBodyUnsupported(ByteBuffer byteBuffer, int frameSize)
    throws InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * Return the frame identifier
     *
     * @return the identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * 
     *
     * @param obj 
     * @return whether obj is equivalent to this object
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof FrameBodyUnsupported) == false)
        {
            return false;
        }

        FrameBodyUnsupported object = (FrameBodyUnsupported) obj;
        if (this.identifier.equals(object.identifier) == false)
        {
            return false;
        }
        return super.equals(obj);
    }


    /**
     * 
     * Because the contents of this frame are an array of bytes and could be large we just
     * return the identifier.
     *
     * @return  a string representation of this frame
     */
    public String toString()
    {
        return getIdentifier();
    }

    /**
     * Setup the Object List. A byte Array which will be read upto frame size
     * bytes.
     */
    protected void setupObjectList()
    {
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_DATA, this));
    }

}
