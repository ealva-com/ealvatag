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
 *  Class that represents an ID3v2.20 tag
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.audio.mp3.*;

import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.jaudiotagger.FileConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.*;
import java.nio.channels.*;
import java.io.*;
import java.util.*;

/** Represents an ID3v2.2 tag */
public class ID3v22Tag
    extends AbstractID3v2Tag
{

    protected static final String TYPE_COMPRESSION = "compression";
    protected static final String TYPE_UNSYNCHRONISATION = "unsyncronisationr";

    /**
     * ID3v2.2 Header bit mask
     */
    public static final int MASK_V22_UNSYNCHRONIZATION = FileConstants.BIT7;

    /**
     * ID3v2.2 Header bit mask
     */
    public static final int MASK_V22_COMPRESSION = FileConstants.BIT7;

    /**
     * The tag is compressed
     */
    protected boolean compression = false;

    /**
     * All frames in the tag uses unsynchronisation
     */
    protected boolean unsynchronization = false;

    /**
     * Creates a new empty ID3v2_2 tag.
     */
    public ID3v22Tag()
    {
        frameMap = new HashMap();
        release = 2;
        majorVersion = 2;
        revision = 0;
    }

    /**
     * Copy primitives applicable to v2.2
     */
    protected void copyPrimitives(AbstractID3v2Tag copyObj)
    {
        logger.info("Copying primitives");
        super.copyPrimitives(copyObj);
        ID3v22Tag copyObject = (ID3v22Tag) copyObj;
        release = 2;
        majorVersion = 2;
        revision = 0;
        //Set the primitive types specific to v2_2.
        if (copyObj instanceof ID3v22Tag)
        {
            this.compression = copyObject.compression;
            this.unsynchronization = copyObject.unsynchronization;
        }
    }

    /**
     * Copy frames from one tag into a v2.2 tag
     */
    protected void copyFrames(AbstractID3v2Tag copyObject)
    {
        logger.info("Copying Frames,there are:" + copyObject.frameMap.keySet().size());
        frameMap = new LinkedHashMap();
        //Copy Frames that are a valid 2.2 type
        Iterator iterator = copyObject.frameMap.keySet().iterator();
        AbstractID3v2Frame frame;
        ID3v22Frame newFrame = null;
        while (iterator.hasNext())
        {
            String id = (String) iterator.next();
            Object o = copyObject.frameMap.get(id);
            if (o instanceof AbstractID3v2Frame)
            {
                frame = (AbstractID3v2Frame) o;
                //Special case v24 TDRC (FRAME_ID_YEAR) may need converting to multiple frames
                if (
                    (frame.getIdentifier().equals(ID3v24Frames.FRAME_ID_YEAR))
                    &&
                    (frame.getBody() instanceof FrameBodyTDRC)
                   )
                {
                    translateFrame(frame);                    
                }
                else
                {
                    newFrame = new ID3v22Frame(frame);
                    if (newFrame.getBody() != null)
                    {
                        frameMap.put(newFrame.getIdentifier(), newFrame);
                    }
                }
            }
            else if (o instanceof ArrayList)
            {
                ArrayList multiFrame = new ArrayList();
                for (ListIterator li = ((ArrayList) o).listIterator(); li.hasNext();)
                {
                    frame = (AbstractID3v2Frame) li.next();
                    newFrame = new ID3v22Frame(frame);
                    multiFrame.add(newFrame);
                }
                if (newFrame != null)
                {
                    frameMap.put(newFrame.getIdentifier(), multiFrame);
                }
            }
        }
    }

    /**
     * Copy Constructor, creates a new ID3v2_2 Tag based on another ID3v2_2 Tag
     */
    public ID3v22Tag(ID3v22Tag copyObject)
    {
        //This doesnt do anything.
        super(copyObject);
        if ((copyObject instanceof ID3v23Tag == true))
        {
            throw new UnsupportedOperationException("Do not use Copy Constructor, these are different versions");
        }
        logger.info("Creating tag from another tag of same type");
        copyPrimitives(copyObject);
        copyFrames(copyObject);
    }

    /**
     * Constructs a new tag based upon another tag of different version/type
     */
    public ID3v22Tag(AbstractTag mp3tag)
    {
        logger.info("Creating tag from a tag of a different version");
        //Default Superclass constructor does nothing
        if (mp3tag != null)
        {
            ID3v24Tag convertedTag;
            //Should use the copy constructor instead
            if ((mp3tag instanceof ID3v23Tag == false) && (mp3tag instanceof ID3v22Tag == true))
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            //If v2.4 can get variables from this
            else if (mp3tag instanceof ID3v24Tag)
            {
                convertedTag = (ID3v24Tag) mp3tag;
            }
            //Any tag (e.g lyrics3 and idv1.1,idv2.3 can be converted to id32.4 so do that
            //to simplify things
            else
            {
                convertedTag = new ID3v24Tag(mp3tag);
            }
            //Set the primitive types specific to v2_2.
            copyPrimitives(convertedTag);
            //Set v2.2 Frames
            copyFrames(convertedTag);
            logger.info("Created tag from a tag of a different version");
        }
    }

    /**
     * Creates a new ID3v2_2 datatype by reading it from file.
     *
     * @param byteBuffer DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     * @throws IOException  DOCUMENT ME!
     */
    public ID3v22Tag(ByteBuffer byteBuffer)
        throws TagException, IOException
    {
        this.majorVersion = 2;
        this.revision = 0;
        this.read(byteBuffer);
    }

    /**
     *
     *
     * @return an indentifier of the tag type
     */
    public String getIdentifier()
    {
        return "ID3v2_2.20";
    }

    /**
     * Return frame size based upon the sizes of the frames rather than the size
     * including paddign recorded in the tag header
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = TAG_HEADER_LENGTH;
        size += super.getSize();
        return size;
    }


    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof ID3v22Tag) == false)
        {
            return false;
        }
        ID3v22Tag object = (ID3v22Tag) obj;
        if (this.compression != object.compression)
        {
            return false;
        }
        if (this.unsynchronization != object.unsynchronization)
        {
            return false;
        }
        return super.equals(obj);
    }


    /**
     * Read tag from the ByteBuffer
     *
     * @param byteBuffer to read the tag from
     * @throws TagException         DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     */
    public void read(ByteBuffer byteBuffer)
        throws TagException, IOException
    {
        int size;
        ID3v22Frame next;
        if (seek(byteBuffer) == false)
        {
            throw new TagNotFoundException("ID3v2.20 tag not found");
        }
        logger.info("Reading tag from file");
        //Read the flags
        byte flags = byteBuffer.get();
        unsynchronization = (flags & MASK_V22_UNSYNCHRONIZATION) != 0;
        compression = (flags & MASK_V22_COMPRESSION) != 0;
        //@todo if unsynchronization bit set what do we do

        //@todo if compression bit set should we ignore

        // Read the size
        byte[] buffer = new byte[FIELD_TAG_SIZE_LENGTH];
        byteBuffer.get(buffer, 0, FIELD_TAG_SIZE_LENGTH);
        size = byteArrayToSize(buffer);

        readFrames(byteBuffer.slice(),size);
    }

    /**
     * Read frames from tag
     */
    protected void readFrames(ByteBuffer byteBuffer, int size)
        throws IOException
    {
        //Now start looking for frames
        ID3v22Frame next;
        frameMap = new LinkedHashMap();
        //Read the size from the Tag Header
        this.fileReadSize = size;
        logger.finest("Start of frame body at:" + byteBuffer.position() + ",frames sizes and padding is:" + size);
        /* @todo not done yet. Read the first Frame, there seems to be quite a
         ** common case of extra data being between the tag header and the first
         ** frame so should we allow for this when reading first frame, but not subsequent frames
         */
        // Read the frames until got to upto the size as specified in header
        while (byteBuffer.position()<size)
        {
            String id = null;
            try
            {
                //Read Frame
                logger.finest("looking for next frame at:" + byteBuffer.position());
                next = new ID3v22Frame(byteBuffer);
                id = next.getIdentifier();
                loadFrameIntoMap(id, next);
            }
                //Found Empty Frame
            catch (EmptyFrameException ex)
            {
                this.emptyFrameBytes += ID3v22Frame.FRAME_HEADER_SIZE;
            }
                //Problem trying to find frame
            catch (InvalidFrameException ex)
            {
                logger.warning("Invalid Frame" + id);
                this.invalidFrameBytes++;
                //Dont try and find any more frames
                break;
            }
            ;
        }
    }

    /**
     * This is used when we need to translate a single frame into multiple frames,
     * currently required for TDRC frames.
     */
    protected void translateFrame(AbstractID3v2Frame frame)
    {
        FrameBodyTDRC tmpBody = (FrameBodyTDRC) frame.getBody();
        ID3v22Frame newFrame = null;
        if (!tmpBody.getYear().equals(""))
        {
            //Create Year frame (v2.2 id,but uses v2.3 body)
            newFrame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_TYER);
            ((AbstractFrameBodyTextInfo) newFrame.getBody()).setText(tmpBody.getYear());
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
        if (!tmpBody.getTime().equals(""))
        {
            //Create Time frame (v2.2 id,but uses v2.3 body)
            newFrame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_TIME);
            ((AbstractFrameBodyTextInfo) newFrame.getBody()).setText(tmpBody.getTime());
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
    }


    /**
     * Write tag to file
     *
     * @param file The file to write to
     * @throws IOException
     */
    public void write(File file, long audioStartLocation)
        throws IOException
    {
        logger.info("Writing tag to file");

        /** Write Body Buffer */
        byte[] bodyByteBuffer = writeFramesToBuffer().toByteArray();

        /** @todo Unsynchronisation support required.
         * We never compress tags as was never defined,
         * currently we do not support Unsyncronisation so should not be set.
         */
        unsynchronization = false;
        compression = false;

        /** Create Header Buffer */
        ByteBuffer headerBuffer = ByteBuffer.allocate(this.TAG_HEADER_LENGTH);
        //TAGID
        headerBuffer.put(TAG_ID);
        //Major Version
        headerBuffer.put(majorVersion);
        //Minor Version
        headerBuffer.put(revision);
        //Flags
        byte flags = (byte) 0;
        if (unsynchronization == true)
        {
            flags |= MASK_V22_UNSYNCHRONIZATION;
        }
        if (compression == true)
        {
            flags |= MASK_V22_COMPRESSION;
        }
        headerBuffer.put(flags);
        /** Calculate Tag Size including Padding */
        int sizeIncPadding = calculateTagSize(getSize(),(int) audioStartLocation);
        int padding = sizeIncPadding - getSize();
        headerBuffer.put(sizeToByteArray((int) sizeIncPadding - TAG_HEADER_LENGTH));
        
        /** We need to adjust location of audio File */
        if (sizeIncPadding > audioStartLocation)
        {
            logger.finest("Adjusting Pattern");
            adjustPadding(file, sizeIncPadding, audioStartLocation);
        }

        //Write changes to file
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        headerBuffer.flip();
        fc.write(headerBuffer);
        fc.write(ByteBuffer.wrap(bodyByteBuffer));
        fc.write(ByteBuffer.wrap(new byte[padding]));
        fc.close();

    }

    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_TAG, getIdentifier());

        super.createStructureHeader();

        //Header
        MP3File.getStructureFormatter().openHeadingElement(TYPE_HEADER, "");
        MP3File.getStructureFormatter().addElement(TYPE_COMPRESSION, this.compression);
        MP3File.getStructureFormatter().addElement(TYPE_UNSYNCHRONISATION, this.unsynchronization);
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_HEADER);
        //Body
        super.createStructureBody();

        MP3File.getStructureFormatter().closeHeadingElement(TYPE_TAG);
    }
}
