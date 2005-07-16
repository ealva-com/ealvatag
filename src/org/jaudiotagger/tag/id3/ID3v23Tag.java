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
 * This class represents an ID3v2.30 tag
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.AbstractTag;
import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.FileConstants;

import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.*;
import java.nio.*;
import java.io.*;
import java.nio.channels.*;

public class ID3v23Tag
    extends ID3v22Tag
{
    protected static final String TYPE_CRCDATA = "crcdata";
    protected static final String TYPE_EXPERIMENTAL = "experimental";
    protected static final String TYPE_EXTENDED = "extended";
    protected static final String TYPE_PADDINGSIZE = "paddingsize";


    protected static int TAG_EXT_HEADER_LENGTH = 10;
    protected static int TAG_EXT_HEADER_CRC_LENGTH = 4;
    protected static int FIELD_TAG_EXT_SIZE_LENGTH = 4;
    protected static int TAG_EXT_HEADER_DATA_LENGTH = TAG_EXT_HEADER_LENGTH - FIELD_TAG_EXT_SIZE_LENGTH;

    /**
     * ID3v2.3 Header bit mask
     */
    public static final int MASK_V23_UNSYNCHRONIZATION = FileConstants.BIT7;

    /**
     * ID3v2.3 Header bit mask
     */
    public static final int MASK_V23_EXTENDED_HEADER = FileConstants.BIT6;

    /**
     * ID3v2.3 Header bit mask
     */
    public static final int MASK_V23_EXPERIMENTAL = FileConstants.BIT5;

    /**
     * ID3v2.3 Extended Header bit mask
     */
    public static final int MASK_V23_CRC_DATA_PRESENT = FileConstants.BIT7;

    /**
     * ID3v2.3 RBUF frame bit mask
     */
    public static final int MASK_V23_EMBEDDED_INFO_FLAG = FileConstants.BIT1;

    /**
     * CRC Checksum calculated
     */
    protected boolean crcDataFlag = false;

    /**
     * Experiemntal tag
     */
    protected boolean experimental = false;

    /**
     * Contains extended header
     */
    protected boolean extended = false;

    /**
     * CRC Checksum
     */
    protected int crcData = 0;

    /**
     * Tag padding
     */
    protected int paddingSize = 0;

    /**
     * Creates a new empty ID3v2_3 datatype.
     */
    public ID3v23Tag()
    {
        release = 2;
        majorVersion = 3;
        revision = 0;
    }

    /**
     * Copy primitives applicable to v2.3
     */
    protected void copyPrimitives(AbstractID3v2Tag copyObj)
    {
        logger.info("Copying primitives");
        super.copyPrimitives(copyObj);
        //Set the primitive types specific to v2_3.
        release = 2;
        majorVersion = 3;
        revision = 0;
        if (copyObj instanceof ID3v23Tag)
        {
            ID3v23Tag copyObject = (ID3v23Tag) copyObj;
            this.crcDataFlag = copyObject.crcDataFlag;
            this.experimental = copyObject.experimental;
            this.extended = copyObject.extended;
            this.crcData = copyObject.crcData;
            this.paddingSize = copyObject.paddingSize;
        }
    }

    /**
     * Copy frames from one tag into a v2.3 tag
     */
    protected void copyFrames(AbstractID3v2Tag copyObject)
    {
        logger.info("Copying Frames,there are:" + copyObject.frameMap.keySet().size());
        frameMap = new LinkedHashMap();
        //Copy Frames that are a valid 2.3 type
        Iterator iterator = copyObject.frameMap.keySet().iterator();
        AbstractID3v2Frame frame;
        ID3v23Frame newFrame = null;
        while (iterator.hasNext())
        {
            String id = (String) iterator.next();
            Object o = copyObject.frameMap.get(id);
            if (o instanceof AbstractID3v2Frame)
            {
                frame = (AbstractID3v2Frame) o;
                logger.info("Frame is:"+frame.getIdentifier());
                //Special case v24 tdrc may need converting to multiple frames
                if (frame.getIdentifier().equals(ID3v24Frames.FRAME_ID_YEAR))
                {
                    translateFrame(frame);
                }
                //Usual Case
                else
                {
                    newFrame = new ID3v23Frame(frame);
                    if (newFrame.getBody() != null)
                    {
                        logger.info("Adding Frame:"+newFrame.getIdentifier());
                        frameMap.put(newFrame.getIdentifier(), newFrame);
                    }
                }
            }
            //Multi Frames
            else if (o instanceof ArrayList)
            {
                ArrayList multiFrame = new ArrayList();
                for (ListIterator li = ((ArrayList) o).listIterator(); li.hasNext();)
                {
                    frame = (AbstractID3v2Frame) li.next();
                    logger.info("Frame is MultiFrame:"+frame.getIdentifier());
                    newFrame = new ID3v23Frame(frame);
                    multiFrame.add(newFrame);
                }
                if (newFrame != null)
                {
                    logger.info("Adding MultiFrame:"+newFrame.getIdentifier());
                    frameMap.put(newFrame.getIdentifier(), multiFrame);
                }

            }
        }
    }

    /**
     * This is used when we need to translate a single frame into multiple frames,
     * currently required for v24 TDRC frames.
     */
    protected void translateFrame(AbstractID3v2Frame frame)
    {
        FrameBodyTDRC tmpBody = (FrameBodyTDRC) frame.getBody();
        ID3v23Frame newFrame = null;
        if (!tmpBody.getYear().equals(""))
        {
            newFrame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TYER);
            ((FrameBodyTYER) newFrame.getBody()).setText(tmpBody.getYear());
            logger.info("Adding Frame:"+newFrame.getIdentifier());
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
        if (!tmpBody.getDate().equals(""))
        {
            newFrame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TDAT);
            ((FrameBodyTDAT) newFrame.getBody()).setText(tmpBody.getDate());
            logger.info("Adding Frame:"+newFrame.getIdentifier());
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
        if (!tmpBody.getTime().equals(""))
        {
            newFrame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TIME);
            ((FrameBodyTIME) newFrame.getBody()).setText(tmpBody.getTime());
            logger.info("Adding Frame:"+newFrame.getIdentifier());
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
    }

    /**
     * Copy Constructor, creates a new ID3v2_3 Tag based on another ID3v2_3 Tag
     */
    public ID3v23Tag(ID3v23Tag copyObject)
    {
        //This doesnt do anything.
        super(copyObject);
        if ((copyObject instanceof ID3v24Tag == true))
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
    public ID3v23Tag(AbstractTag mp3tag)
    {
        logger.info("Creating tag from a tag of a different version");
        if (mp3tag != null)
        {
            ID3v24Tag convertedTag;
            //Should use simpler copy constructor
            if ((mp3tag instanceof ID3v24Tag == false) && (mp3tag instanceof ID3v23Tag == true))
            {
                throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
            }
            if (mp3tag instanceof ID3v24Tag)
            {
                convertedTag = (ID3v24Tag) mp3tag;
            }
            //All tags types can be converted to v2.4 so do this to simplify things
            else
            {
                convertedTag = new ID3v24Tag(mp3tag);
            }
            //Copy Primitives
            copyPrimitives(convertedTag);
            //Copy Frames
            copyFrames(convertedTag);
            logger.info("Created tag from a tag of a different version");
        }
    }

    /**
     * Creates a new ID3v2_3 datatype.
     *
     * @param file DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     * @throws IOException  DOCUMENT ME!
     */
    public ID3v23Tag(RandomAccessFile file)
        throws TagException, IOException
    {
        this.majorVersion = 3;
        this.revision = 0;
        this.read(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return "ID3v2.30";
    }

    /**
     * Return frame size based upon the sizes of the tags rather than the physical
     * no of bytes between start of ID3Tag and start of Audio Data.
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = TAG_HEADER_LENGTH;
        if (extended)
        {
            size += this.TAG_EXT_HEADER_LENGTH;
            if (crcDataFlag)
            {
                size += this.TAG_EXT_HEADER_CRC_LENGTH;
            }
        }
        size += super.getSize();
        return size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     */
    public void append(AbstractTag tag)
    {
        if (tag instanceof ID3v23Tag)
        {
            this.experimental = ((ID3v23Tag) tag).experimental;
            this.extended = ((ID3v23Tag) tag).extended;
            this.crcDataFlag = ((ID3v23Tag) tag).crcDataFlag;
            this.paddingSize = ((ID3v23Tag) tag).paddingSize;
            this.crcData = ((ID3v23Tag) tag).crcData;
        }
        super.append(tag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

//    public void append(RandomAccessFile file)
//                throws IOException, TagException {
//        ID3v2_3 oldTag;
//
//        try {
//            oldTag = new ID3v2_3(file);
//            oldTag.append(this);
//            oldTag.write(file);
//        } catch (TagNotFoundException ex) {
//            oldTag = null;
//        }
//    }

    /**
     * Is Tag Equivalent to another tag
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof ID3v23Tag) == false)
        {
            return false;
        }
        ID3v23Tag object = (ID3v23Tag) obj;
        if (this.crcData != object.crcData)
        {
            return false;
        }
        if (this.crcDataFlag != object.crcDataFlag)
        {
            return false;
        }
        if (this.experimental != object.experimental)
        {
            return false;
        }
        if (this.extended != object.extended)
        {
            return false;
        }
        if (this.paddingSize != object.paddingSize)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Add all frames to this tag overwriting the frames even if they
     * already exist.
     *
     * @param tag DOCUMENT ME!
     */
    public void overwrite(AbstractTag tag)
    {
        if (tag instanceof ID3v23Tag)
        {
            this.experimental = ((ID3v23Tag) tag).experimental;
            this.extended = ((ID3v23Tag) tag).extended;
            this.crcDataFlag = ((ID3v23Tag) tag).crcDataFlag;
            this.paddingSize = ((ID3v23Tag) tag).paddingSize;
            this.crcData = ((ID3v23Tag) tag).crcData;
        }
        super.overwrite(tag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @throws TagException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */

//    public void overwrite(RandomAccessFile file)
//                   throws IOException, TagException {
//        ID3v2_3 oldTag;
//
//        try {
//            oldTag = new ID3v2_3(file);
//            oldTag.overwrite(this);
//            oldTag.write(file);
//        } catch (TagNotFoundException ex) {
//	    super.overwrite(file);
//        }
//    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws TagException         DOCUMENT ME!
     * @throws IOException          DOCUMENT ME!
     * @throws TagNotFoundException DOCUMENT ME!
     * @throws InvalidTagException  DOCUMENT ME!
     */
    public void read(RandomAccessFile file)
        throws TagException, IOException
    {
        int size;
        if (seek(file) == false)
        {
            throw new TagNotFoundException(getIdentifier() + " tag not found");
        }
        logger.info("Reading tag from file");
        //Flags
        byte flags = file.readByte();
        unsynchronization = (flags & MASK_V23_UNSYNCHRONIZATION) != 0;
        extended = (flags & MASK_V23_EXTENDED_HEADER) != 0;
        experimental = (flags & MASK_V23_EXPERIMENTAL) != 0;
        if (unsynchronization == true)
        {
            logger.warning("this tag is unsynchronised");
        }
        // Read the size, this is size of tag apart from tag header
        byte[] sizeBuffer = new byte[FIELD_TAG_SIZE_LENGTH];
        file.read(sizeBuffer, 0, FIELD_TAG_SIZE_LENGTH);
        size = byteArrayToSize(sizeBuffer);
        //Extended Header
        if (extended == true)
        {
            // Int is 4 bytes.
            int extendedHeaderSize = file.readInt();
            // Extended header without CRC Data
            if (extendedHeaderSize == TAG_EXT_HEADER_DATA_LENGTH)
            {
                //Flag
                byte extFlag = file.readByte();
                crcDataFlag = (extFlag & MASK_V23_CRC_DATA_PRESENT) != 0;
                if (crcDataFlag == true)
                {
                    throw new InvalidTagException("CRC Data flag not set correctly.");
                }
                //Flag Byte (not used)
                file.readByte();
                //Take padding and ext header size off size to be read
                size = size - (file.readInt() + TAG_EXT_HEADER_LENGTH);
            }
            else if (extendedHeaderSize == TAG_EXT_HEADER_DATA_LENGTH + TAG_EXT_HEADER_CRC_LENGTH)
            {
                //Flag
                byte extFlag = file.readByte();
                crcDataFlag = (extFlag & MASK_V23_CRC_DATA_PRESENT) != 0;
                if (crcDataFlag == false)
                {
                    throw new InvalidTagException("CRC Data flag not set correctly.");
                }
                //Flag Byte (not used)
                file.readByte();
                //Take padding size of size to be read
                size = size - (file.readInt() + TAG_EXT_HEADER_LENGTH + TAG_EXT_HEADER_CRC_LENGTH);
                //CRC Data
                crcData = file.readInt();
            }
            else
            {
                throw new InvalidTagException("Invalid Extended Header Size.");
            }
        }
        long filePointer = file.getFilePointer();
        //Note if there was an extended header the size value has padding taken
        //off so we dont search it.
        readFrames(file, filePointer, size);
        logger.info("Loaded Frames,there are:" + frameMap.keySet().size());

    }

    /**
     * Read frames from tag
     */
    protected void readFrames(RandomAccessFile file, long filePointer, int size)
        throws IOException
    {
        //Now start looking for frames
        ID3v23Frame next;
        frameMap = new LinkedHashMap();
        //Read the size from the Tag Header
        this.fileReadSize = size;
        logger.finest("Start of frame body at:" + file.getFilePointer() + ",frames data size is:" + size);
        // Read the frames until got to upto the size as specified in header or until
        // we hit an invalid frame identifier
        while ((file.getFilePointer() - filePointer) < size)
        {
            String id = null;
            try
            {
                //Read Frame
                logger.finest("looking for next frame at:" + file.getFilePointer());
                next = new ID3v23Frame(file);
                id = next.getIdentifier();
                loadFrameIntoMap(id, next);
            }
                //Found Empty Frame
            catch (EmptyFrameException ex)
            {
                logger.warning("Empty Frame" + id);
                this.emptyFrameBytes += ID3v23Frame.FRAME_HEADER_SIZE;
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
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     */
    public void write(AbstractTag tag)
    {
        if (tag instanceof ID3v23Tag)
        {
            this.experimental = ((ID3v23Tag) tag).experimental;
            this.extended = ((ID3v23Tag) tag).extended;
            this.crcDataFlag = ((ID3v23Tag) tag).crcDataFlag;
            this.paddingSize = ((ID3v23Tag) tag).paddingSize;
            this.crcData = ((ID3v23Tag) tag).crcData;
        }
        super.write(tag);
    }

    /**
     * Write tag to file
     *
     * @param file The file to write to
     * @throws IOException DOCUMENT ME!
     */
    public void write(File file, long audioStartLocation)
        throws IOException
    {
        logger.info("Writing tag to file");

        /** Write Body Buffer */
        ByteBuffer bodyBuffer = writeFramesToBuffer();

        /** @todo Calculate the CYC Data Check */
        /** @todo Calculate UnSynchronisation */
        /** @todo Reintroduce Extended Header */
        /** Flags,currently we never do unsynchronisation or calculate the CRC
         *  and if we dont calculate them cant keep orig values. Tags are not
         *  experimental and we never create extended header to keep things simple.
         */
        unsynchronization = false;
        extended = false;
        experimental = false;
        crcDataFlag = false;
        /** Create Header Buffer,allocate maximum possible size for the header*/
        ByteBuffer headerBuffer = ByteBuffer.
            allocate(TAG_HEADER_LENGTH + TAG_EXT_HEADER_LENGTH + TAG_EXT_HEADER_CRC_LENGTH);
        //TAGID
        headerBuffer.put(TAG_ID);
        //Major Version
        headerBuffer.put(majorVersion);
        //Minor Version
        headerBuffer.put(revision);
        //Flags
        byte flagsByte = 0;
        if (unsynchronization == true)
        {
            flagsByte |= MASK_V23_UNSYNCHRONIZATION;
        }
        if (extended == true)
        {
            flagsByte |= MASK_V23_EXTENDED_HEADER;
        }
        if (experimental == true)
        {
            flagsByte |= MASK_V23_EXPERIMENTAL;
        }
        headerBuffer.put(flagsByte);
        /** Calculate Tag Size including Padding */
        int sizeIncPadding = calculateTagSize(getSize(), (int) audioStartLocation);
        /** Add padding as necessary */
        int padding = sizeIncPadding - getSize();
        bodyBuffer.put(new byte[padding]);
        //Size As Recorded in Header, don't include the main header length
        headerBuffer.put(sizeToByteArray((int) sizeIncPadding - TAG_HEADER_LENGTH));
        /** Write Extended Header */
        if (extended == true)
        {
            byte extFlagsByte1 = 0;
            byte extFlagsByte2 = 0;
            /** Contains CRC data */
            if (crcDataFlag == true)
            {
                headerBuffer.putInt(TAG_EXT_HEADER_DATA_LENGTH + TAG_EXT_HEADER_CRC_LENGTH);
                extFlagsByte1 |= MASK_V23_CRC_DATA_PRESENT;
                headerBuffer.put(extFlagsByte1);
                headerBuffer.put(extFlagsByte2);
                headerBuffer.putInt(paddingSize);
                headerBuffer.putInt(crcData);
            }
            /** Only Extended Header */
            else
            {
                headerBuffer.putInt(TAG_EXT_HEADER_DATA_LENGTH);
                headerBuffer.put(extFlagsByte1);
                headerBuffer.put(extFlagsByte2);
                //Newly Calculated Padding As Recorded in Extended Header
                headerBuffer.putInt(padding);
            }
        }
        /** We need to adjust location of audio File */
        if (sizeIncPadding > audioStartLocation)
        {
            logger.finest("Adjusting Padding");
            adjustPadding(file, sizeIncPadding, audioStartLocation);
        }
        //Write changes to file
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        headerBuffer.flip();
        fc.write(headerBuffer);
        bodyBuffer.flip();
        fc.write(bodyBuffer);
        fc.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public void createStructure()
    {

        MP3File.getStructureFormatter().openHeadingElement(TYPE_TAG, getIdentifier());

        super.createStructureHeader();

        //Header
        MP3File.getStructureFormatter().openHeadingElement(TYPE_HEADER, "");
        MP3File.getStructureFormatter().addElement(TYPE_UNSYNCHRONISATION, this.unsynchronization);
        MP3File.getStructureFormatter().addElement(TYPE_EXTENDED, this.extended);
        MP3File.getStructureFormatter().addElement(TYPE_EXPERIMENTAL, this.experimental);
        MP3File.getStructureFormatter().addElement(TYPE_COMPRESSION, this.compression);

        MP3File.getStructureFormatter().addElement(TYPE_CRCDATA, this.crcData);

        MP3File.getStructureFormatter().addElement(TYPE_PADDINGSIZE, this.paddingSize);
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_HEADER);
        //Body
        super.createStructureBody();
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_TAG);

    }
}
