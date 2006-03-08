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
 * Abstract Superclass of all Frame Bodys
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.InvalidFrameException;
import org.jaudiotagger.tag.InvalidTagException;

import java.util.*;
import java.io.*;
import java.nio.*;

public abstract class AbstractID3v2FrameBody
    extends AbstractTagFrameBody
{
    protected static final String TYPE_BODY = "body";

    /**
     * Frame Body Size, originally this is size as indicated in frame header
     * when we come to writing data we recalculate it.
     */
    private int size;

    /**
     * Create Empty Body. Super Constructor sets up Object list
     */
    protected AbstractID3v2FrameBody()
    {
    }

    /**
     * Create Body based on another body
     */
    protected AbstractID3v2FrameBody(AbstractID3v2FrameBody copyObject)
    {
        super(copyObject);
    }

    /**
     * Creates a new FrameBody datatype from file. The super
     * Constructor sets up the Object list for the frame.
     *
     * @param byteBuffer The MP3File to read the frame from.
     * @throws java.io.IOException DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    protected AbstractID3v2FrameBody(ByteBuffer byteBuffer, int frameSize)
        throws java.io.IOException, InvalidFrameException
    {
        super();
        setSize(frameSize);
        this.read(byteBuffer);
    }

    /**
     * Return identifier
     *
     * @return DOCUMENT ME!
     * @todo Make this abstract. Can't do that yet because not all frame bodies
     * have been finished.
     */
    public String getIdentifier()
    {
        return "";
    }

    /**
     * Return size of frame body
     *
     * @return estimated size in bytes of this datatype
     * @todo get this working 100% of the time
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Set size based on size passed as parameter from frame header,
     * done before read
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    /**
     * Set size based on size of the MP3Objects,done after write
     */
    public void setSize()
    {
        size = 0;
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            size += object.getSize();
        }
        ;
    }

    /**
     * Are two bodies equal
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof AbstractID3v2FrameBody) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * This reads a frame body from its file into the appropriate FrameBody class
     * Read the data from the given file into this datatype. The file needs to
     * have its file pointer in the correct location. The size as indicated in the
     * header is passed to the frame constructor when reading from file.
     *
     * @param byteBuffer file to read
     * @throws IOException         on any I/O error
     * @throws InvalidTagException if there is any error in the data format.
     */
    public void read(ByteBuffer byteBuffer)
        throws IOException, InvalidFrameException
    {
        int size = getSize();
        logger.info("Reading body for" + this.getIdentifier() + ":" + size);
        //Allocate a buffer to the size of the Frame Body and read from file
        byte[] buffer = new byte[size];
        byteBuffer.get(buffer);
        //Offset into buffer, incremented by length of previous MP3Object
        int offset = 0;
        //Go through the ObjectList of the Frame reading the data into the
        //correct datatype.
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            //The read has extended further than the defined frame size ok to extend upto
            //because the next datatype may be of length 0.
            if (offset > (size))
            {
                logger.warning("Invalid Size for FrameBody");
                throw new InvalidFrameException("Invalid size for Frame Body");
            }
            //Get next Object and load it with data from the Buffer
            object = (AbstractDataType) iterator.next();
            object.readByteArray(buffer, offset);
            //Increment Offset to start of next datatype.
            offset += object.getSize();
        }
    }

    /**
     * Write the contents of this datatype to the byte array
     *
     * @throws IOException on any I/O error
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        logger.info("Writing frame body for" + this.getIdentifier() + ":Est Size:" + size);
        //Write the various fields to file in order
        AbstractDataType object;
        Iterator iterator = objectList.listIterator();
        while (iterator.hasNext())
        {
            object = (AbstractDataType) iterator.next();
            byte[] objectData = object.writeByteArray();
            if (objectData != null)
            {
                tagBuffer.write(objectData);
            }
        }
        setSize();
        logger.info("Written frame body for" + this.getIdentifier() + ":Real Size:" + size);

    }

    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_BODY, "");
        for (ListIterator li = objectList.listIterator(); li.hasNext();)
        {
            AbstractDataType nextObject = (AbstractDataType) li.next();
            nextObject.createStructure();
        }
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_BODY);

    }

}
