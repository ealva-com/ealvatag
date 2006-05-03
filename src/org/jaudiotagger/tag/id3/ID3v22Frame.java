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
 * This class is the frame header used in ID3v2.2 tags
 *
 */
package org.jaudiotagger.tag.id3;

import java.io.*;
import java.nio.*;
import java.util.regex.*;
import java.util.Arrays;
import java.math.BigInteger;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;

/** Represents an ID3v2.2 frame */
public class ID3v22Frame
    extends AbstractID3v2Frame
{
    Pattern validFrameIdentifier = Pattern.compile("[A-Z][0-9A-Z]{2}");

    protected static int FRAME_ID_SIZE = 3;
    protected static int FRAME_SIZE_SIZE = 3;
    protected static int FRAME_HEADER_SIZE = FRAME_ID_SIZE + FRAME_SIZE_SIZE;

    public ID3v22Frame()
    {

    }

    /**
     * Creates a new ID3v2_2Frame datatype with given body
     *
     * @param body New body and frame is based on this
     */
    public ID3v22Frame(AbstractID3v2FrameBody body)
    {
        super(body);
    }

    /**
     * Creates a new ID3v2_2Frame of type identifier. An empty
     * body of the correct type will be automatically created.
     * This constructor should be used when wish to create a new
     * frame from scratch using user values
     *
     * @param body DOCUMENT ME!
     */
    public ID3v22Frame(String identifier)
    {
        logger.info("Creating empty frame of type" + identifier);
        String bodyIdentifier = identifier;
        this.identifier       = identifier;
        //Messy fix for datetime
        if (
            (bodyIdentifier == ID3v22Frames.FRAME_ID_V2_TYER)
            ||
            (bodyIdentifier == ID3v22Frames.FRAME_ID_V2_TIME)
        )
        {
            bodyIdentifier = ID3v24Frames.FRAME_ID_YEAR;
        }
        /* Have to check for 2.2 because most dont have own bodythey use 2.3 or 2.4
         * body to hold the data, the frame is identified by its identifier, the body identifier
         * is just to create a body suitable for writing the data to
         */
        else if (ID3Tags.isID3v22FrameIdentifier(bodyIdentifier))
        {
             bodyIdentifier = (String) ID3Tags.convertFrameID22To24(bodyIdentifier);
        }
  
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date.
         */
        try
        {
            Class c = Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + bodyIdentifier);
            frameBody = (AbstractID3v2FrameBody) c.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            logger.severe(cnfe.getMessage());
            frameBody = new FrameBodyUnsupported();
        }
            //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            throw new RuntimeException(ie.getMessage());
        }
            //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            throw new RuntimeException(iae.getMessage());
        }
        logger.info("Created empty frame of type" + this.identifier + "with frame body of" + bodyIdentifier);

    }

    /**
     * Copy Constructor:
     * Creates a new v2.2 frame datatype based on another v2.2 frame
     */
    public ID3v22Frame(ID3v22Frame frame)
    {
        super(frame);
        logger.info("Creating frame from a frame of same version");
    }

    /**
     * Creates a new ID3v2_2Frame datatype from another frame.
     *
     * @param frame DOCUMENT ME!
     */
    public ID3v22Frame(AbstractID3v2Frame frame)
    {
        logger.info("Creating frame from a frame of a different version");
        if ((frame instanceof ID3v22Frame == true) && (frame instanceof ID3v23Frame == false))
        {
            throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
        }
        try
        {
            /** If it is a v2.4 frame is it possible to convert it into a v2.2 frame*/
            if (frame instanceof ID3v24Frame)
            {
                /** Version between v4 and v2 */
                identifier = ID3Tags.convertFrameID24To22(frame.getIdentifier());
                if (identifier != null)
                {
                    logger.info("V2:Convert:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                    this.frameBody = (AbstractID3v2FrameBody) ID3Tags.copyObject(frame.getBody());
                    return;
                }
                /** Is it a known v4 frame which needs forcing to v2 frame e.g. APIC - PIC */
                else if (ID3Tags.isID3v24FrameIdentifier(frame.getIdentifier()) == true)
                {
                    identifier = ID3Tags.forceFrameID24To22(frame.getIdentifier());
                    logger.info("V2:Force:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                    this.frameBody = this.readBody(identifier, (AbstractID3v2FrameBody) frame.getBody());
                    return;
                }
                /** Unknown Frame e.g NCON */
                this.frameBody = new FrameBodyUnsupported((FrameBodyUnsupported) frame.getBody());
                identifier = frame.getIdentifier();
                logger.info("V2:Unknown:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                return;
            }
            /** If it is a v2.3 frame is it possible to convert it into a v2.2 frame*/
            else if (frame instanceof ID3v23Frame)
            {
                identifier = ID3Tags.convertFrameID23To22(frame.getIdentifier());
                if (identifier != null)
                {
                    logger.info("V2:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                    this.frameBody = (AbstractID3v2FrameBody) ID3Tags.copyObject(frame.getBody());
                    return;
                }
                /** Is it a known v3 frame which needs forcing to v2 frame e.g. APIC - PIC */
                else if (ID3Tags.isID3v23FrameIdentifier(frame.getIdentifier()) == true)
                {
                    identifier = ID3Tags.forceFrameID23To22(frame.getIdentifier());
                    logger.info("V2:Force:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                    this.frameBody = this.readBody(identifier, (AbstractID3v2FrameBody) frame.getBody());
                    return;
                }
                /** Unknown Frame e.g NCON */
                this.frameBody = new FrameBodyUnsupported((FrameBodyUnsupported) frame.getBody());
                identifier = frame.getIdentifier();
                logger.info("v2:UNKNOWN:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                return;
            }
        }
        catch (Exception e)
        {
            logger.warning("Unable to convert to v23 Frame:Frame Identifier" + frame.getIdentifier());
        }
        logger.info("Created frame from a frame of a different version");
    }

    /**
     * Creates a new ID3v2_2Frame datatype from byteBuffer.
     *
     * @param byteBuffer
     * @throws IOException

     */
    public ID3v22Frame(ByteBuffer byteBuffer)
        throws IOException, InvalidFrameException
    {
        logger.info("Reading frame from byteBuffer");
        this.read(byteBuffer);
        logger.info("Read frame from byteBuffer");
    }

    /**
     * Return size of frame
     *
     * @return int size of frame
     */
    public int getSize()
    {
        return frameBody.getSize() + FRAME_HEADER_SIZE;
    }

    /**
     * Read frame from file.
     * Read the frame header then delegate reading of data to frame body.
     *
     * @param byteBuffer
     * @throws IOException

     */
    public void read(ByteBuffer byteBuffer)
        throws IOException, InvalidFrameException
    {
        byte[] buffer = new byte[FRAME_ID_SIZE];

        if(byteBuffer.position()+ FRAME_HEADER_SIZE >= byteBuffer.limit())
        {
            logger.warning("No space to find another frame:");
            throw new InvalidFrameException(" No space to find another frame");
        }

        // Read the FrameID Identifier
        byteBuffer.get(buffer, 0, FRAME_ID_SIZE);
        identifier = new String(buffer);
        logger.info("Read Frame from file identifier is:"+identifier);

        // Is this a valid identifier?
        if (isValidID3v2FrameIdentifier(identifier) == false)
        {
            logger.info("Invalid identifier:" + identifier);
            byteBuffer.position(byteBuffer.position() - (FRAME_ID_SIZE - 1));
            throw new InvalidFrameException(identifier + " is not a valid ID3v2.20 frame");
        }
        //Read Frame Size (same size as Frame ID so reuse buffer)
        byteBuffer.get(buffer, 0, FRAME_SIZE_SIZE);
        frameSize=decodeSize(buffer);
        if (frameSize < 0)
        {
            throw new InvalidFrameException(identifier + " has invalid size of:" + frameSize);
        }
        else if (frameSize == 0)
        {
            logger.warning("Empty Frame:" + identifier);
            throw new EmptyFrameException(identifier + " is empty frame");
        }
        else if (frameSize > byteBuffer.remaining())
        {
            logger.warning("Invalid Frame size larger than size before mp3 audio:" + identifier);
            throw new InvalidFrameException(identifier + " is invalid frame");
        }
        else
        {
            logger.fine("Frame Size Is:" + frameSize);
            //Convert v2.2 to v2.4 id just for reading the data
            String id = (String) ID3Tags.convertFrameID22To24(identifier);
            if (id == null)
            {
                //OK,it may be convertable to a v.3 id even though not valid v.4
                id = (String) ID3Tags.convertFrameID22To23(identifier);
                if (id == null)
                {
                    /** Is it a valid v22 identifier so should be able to find a
                     *  frame body for it.
                     */
                    if (ID3Tags.isID3v22FrameIdentifier(identifier) == true)
                    {
                        id = identifier;
                    }
                    /** Unknown so will be created as FrameBodyUnsupported
                     */
                    else
                    {
                        id = UNSUPPORTED_ID;
                    }
                }
            }
            logger.fine("Identifier was:" + identifier + " reading using:" + id);
            frameBody = readBody(id, byteBuffer, frameSize);
        }
    }

    /** Read Frame Size, which has to be decoded
     */
    private int decodeSize(byte[] buffer)
    {
        BigInteger  bi = new BigInteger(buffer);
        int tmpSize = bi.intValue();
        if(tmpSize<0)
        {
            logger.warning("Invalid Frame Size of:" + tmpSize + "Decoded from bin:"+Integer.toBinaryString(tmpSize)+"Decoded from hex:"+Integer.toHexString(tmpSize));
        }
        return tmpSize;
    }

    /**
     * Write Frame to file
     *
     * @throws IOException
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        logger.info("Write Frame to Buffer" + getIdentifier());
        //This is where we will write header, move position to where we can
        //write body
        ByteBuffer headerBuffer = ByteBuffer.allocate(FRAME_HEADER_SIZE);

        //Write Frame Body Data
        ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
        ((AbstractID3v2FrameBody) frameBody).write(bodyOutputStream);

        //Write Frame Header
        //Write Frame ID must adjust can only be 3 bytes long
        headerBuffer.put(getIdentifier().substring(0, FRAME_ID_SIZE).getBytes(), 0, FRAME_ID_SIZE);
        encodeSize(headerBuffer,frameBody.getSize());

         //Add header to the Byte Array Output Stream
        tagBuffer.write(headerBuffer.array());

        //Add body to the Byte Array Output Stream
        tagBuffer.write(bodyOutputStream.toByteArray());

    }

    /** Write Frame Size (can now be accurately calculated, have to convert 4 byte int
      * to 3 byte format.
     */
    private void encodeSize(ByteBuffer headerBuffer,int size)
    {
        headerBuffer.put((byte) ((size & 0x00FF0000) >> 16));
        headerBuffer.put((byte) ((size & 0x0000FF00) >> 8));
        headerBuffer.put((byte) (size & 0x000000FF));
        logger.fine("Frame Size Is Actual:" + size + ":Encoded bin:"+Integer.toBinaryString(size)+":Encoded Hex"+Integer.toHexString(size));
    }

    /**
     * Does the frame identifier meet the syntax for a idv3v2 frame identifier.
     * must start with a capital letter and only contain capital letters and numbers
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean isValidID3v2FrameIdentifier(String identifier)
    {
        Matcher m = validFrameIdentifier.matcher(identifier);
        return m.matches();
    }

    public String toString()
    {
        return "";
    }

    /**
     * Return String Representation of body
     *                     
     */
    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_FRAME, getIdentifier());
        MP3File.getStructureFormatter().addElement(TYPE_FRAME_SIZE, frameSize);
        statusFlags.createStructure();
        encodingFlags.createStructure();
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_FRAME);

    }


}
