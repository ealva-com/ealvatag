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
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.lyrics3.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.FileConstants;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import java.util.Iterator;
import java.nio.*;


/** Represents an ID3v2.4 frame */
public class ID3v24Frame
    extends ID3v23Frame
{

    public ID3v24Frame()
    {
    }

    /**
     * Creates a new ID3v2_4Frame of type identifier. An empty
     * body of the correct type will be automatically created.
     * This constructor should be used when wish to create a new
     * frame from scratch using user input
     *
     * @param identifier defines the type of body to be created
     */
    public ID3v24Frame(String identifier)
    {
        //Super Constructor creates a frame with empty body of type specified
        super(identifier);
        statusFlags = new StatusFlags();
        encodingFlags = new EncodingFlags();

    }

    /**
     * Copy Constructor:Creates a new ID3v2_4Frame datatype based on another frame.
     */
    public ID3v24Frame(ID3v24Frame frame)
    {
        super(frame);
        statusFlags = new StatusFlags(((ID3v23Frame) frame).getStatusFlags().getOriginalFlags());
        encodingFlags = new EncodingFlags(((ID3v23Frame) frame).getEncodingFlags().getFlags());
    }

    /**
     * Creates a new ID3v2_4Frame datatype based on another frame of different version
     * Converts the framebody to the equivalent v24 framebody or to UnsupportedFrameBody if identifier
     * is unknown.
     *
     * @param frame to construct a new frame from
     */
    public ID3v24Frame(AbstractID3v2Frame frame)  throws InvalidFrameException
    {
        //Should not be called
        if ((frame instanceof ID3v24Frame == true))
        {
            throw new UnsupportedOperationException("Copy Constructor not called. Please type cast the argument");
        }
        //Flags
        if (frame instanceof ID3v23Frame)
        {
            statusFlags = new StatusFlags((ID3v23Frame.StatusFlags) ((ID3v23Frame) frame).getStatusFlags());
            encodingFlags = new EncodingFlags(((ID3v23Frame) frame).getEncodingFlags().getFlags());
        }
        else
        {
            statusFlags = new StatusFlags();
            encodingFlags = new EncodingFlags();
        }

        /** Convert Identifier. If the id was a known id for the original
         *  version we should be able to convert it to an v24 frame, although it may mean minor
         *  modification to the data. If it was not recognised originally it should remain
         *  unknown.
         */
        if (frame instanceof ID3v23Frame)
        {
            /** Is it a straight conversion e.g TALB - TALB */
            identifier = ID3Tags.convertFrameID23To24(frame.getIdentifier());
            if (identifier != null)
            {
                logger.info("V4:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                this.frameBody = (AbstractID3v2FrameBody) ID3Tags.copyObject(frame.getBody());
                return;
            }
            /** Is it a known v3 frame which needs forcing to v4 frame e.g. TYER - TDRC */
            else if (ID3Tags.isID3v23FrameIdentifier(frame.getIdentifier()) == true)
            {
                identifier = ID3Tags.forceFrameID23To24(frame.getIdentifier());
                if(identifier!=null)
                {
                    logger.info("V3:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                    this.frameBody = this.readBody(identifier, (AbstractID3v2FrameBody) frame.getBody());
                    return;
                }
                /* No mechanism exists to convert it to a v24 frame */
                else
                {
                    throw new InvalidFrameException("Unable to convert v23 frame:"+frame.getIdentifier()+" to a v24 frame");
                }

            }
            /** Unknown Frame e.g NCON */
            else
            {
                this.frameBody = new FrameBodyUnsupported((FrameBodyUnsupported) frame.getBody());
                identifier = frame.getIdentifier();
                logger.info("UNKNOWN:Orig id is:" + frame.getIdentifier() + ":New id is:" + identifier);
                return;
            }
        }
        else if (frame instanceof ID3v22Frame)
        {
            /** Is it a straight conversion from v2 to v4 (e.g TAl - TALB) */
            identifier = ID3Tags.convertFrameID22To24(frame.getIdentifier());
            if (identifier != null)
            {
                logger.info("Orig id is:" + frame.getIdentifier() + "New id is:" + identifier);
                this.frameBody = (AbstractID3v2FrameBody) ID3Tags.copyObject(frame.getBody());
                return;
            }
            /** Can we convert from v2 to v3 easily (e.g TYE - TYER) */
            identifier = ID3Tags.convertFrameID22To23(frame.getIdentifier());
            if (identifier != null)
            {
                //Convert from v2 to v3
                logger.info("Orig id is:" + frame.getIdentifier() + "New id is:" + identifier);
                this.frameBody = (AbstractID3v2FrameBody) ID3Tags.copyObject(frame.getBody());
                //Force v3 to v4
                identifier = ID3Tags.forceFrameID23To24(identifier);
                this.frameBody = this.readBody(identifier, (AbstractID3v2FrameBody) this.getBody());
                return;
            }
            /** Is it a known v2 frame which needs forcing to v4 frame e.g PIC - APIC */
            else if (ID3Tags.isID3v22FrameIdentifier(frame.getIdentifier()) == true)
            {
                //Force v2 to v3
                identifier = ID3Tags.forceFrameID22To23(frame.getIdentifier());
                if(identifier!=null)
                {
                    logger.info("Orig id is:" + frame.getIdentifier() + "New id is:" + identifier);
                    this.frameBody = this.readBody(identifier, (AbstractID3v2FrameBody) frame.getBody());
                    return;
                }
                /* No mechanism exists to convert it to a v24 frame */
                else
                {
                    throw new InvalidFrameException("Unable to convert v22 frame:"+frame.getIdentifier()+" to a v24 frame");
                }
            }
            /** Unknown Frame */
            else
            {
                this.frameBody = new FrameBodyUnsupported((FrameBodyUnsupported) frame.getBody());
                identifier = frame.getIdentifier();
                return;
            }
        }

    }

    /**
     * Creates a new ID3v2_4Frame datatype based on Lyrics3.
     *
     * @param field
     * @throws InvalidTagException
     */
    public ID3v24Frame(Lyrics3v2Field field)
        throws InvalidTagException
    {
        String id = field.getIdentifier();
        String value;
        if (id.equals("IND"))
        {
            throw new InvalidTagException("Cannot create ID3v2.40 frame from Lyrics3 indications field.");
        }
        else if (id.equals("LYR"))
        {
            FieldFrameBodyLYR lyric = (FieldFrameBodyLYR) field.getBody();
            Lyrics3Line line;
            Iterator iterator = lyric.iterator();
            FrameBodySYLT sync;
            FrameBodyUSLT unsync;
            boolean hasTimeStamp = lyric.hasTimeStamp();
            // we'll create only one frame here.
            // if there is any timestamp at all, we will create a sync'ed frame.
            sync = new FrameBodySYLT((byte) 0, "ENG", (byte) 2, (byte) 1, "");
            unsync = new FrameBodyUSLT((byte) 0, "ENG", "", "");
            while (iterator.hasNext())
            {
                line = (Lyrics3Line) iterator.next();
                if (hasTimeStamp)
                {
                    sync.addLyric(line);
                }
                else
                {
                    unsync.addLyric(line);
                }
            }
            if (hasTimeStamp)
            {
                this.frameBody = sync;
            }
            else
            {
                this.frameBody = unsync;
            }
        }
        else if (id.equals("INF"))
        {
            value = ((FieldFrameBodyINF) field.getBody()).getAdditionalInformation();
            this.frameBody = new FrameBodyCOMM((byte) 0, "ENG", "", value);
        }
        else if (id.equals("AUT"))
        {
            value = ((FieldFrameBodyAUT) field.getBody()).getAuthor();
            this.frameBody = new FrameBodyTCOM((byte) 0, value);
        }
        else if (id.equals("EAL"))
        {
            value = ((FieldFrameBodyEAL) field.getBody()).getAlbum();
            this.frameBody = new FrameBodyTALB((byte) 0, value);
        }
        else if (id.equals("EAR"))
        {
            value = ((FieldFrameBodyEAR) field.getBody()).getArtist();
            this.frameBody = new FrameBodyTPE1((byte) 0, value);
        }
        else if (id.equals("ETT"))
        {
            value = ((FieldFrameBodyETT) field.getBody()).getTitle();
            this.frameBody = new FrameBodyTIT2((byte) 0, value);
        }
        else if (id.equals("IMG"))
        {
            throw new InvalidTagException("Cannot create ID3v2.40 frame from Lyrics3 image field.");
        }
        else
        {
            throw new InvalidTagException("Cannot caret ID3v2.40 frame from " + id + " Lyrics3 field");
        }
    }

    /**
     * Creates a new ID3v2_4Frame datatype from specified byteBuffer.
     *
     * @param byteBuffer
     */
    public ID3v24Frame(ByteBuffer byteBuffer)
        throws InvalidFrameException
    {
        this.read(byteBuffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof ID3v24Frame) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Read the frame from the specified file.
     * Read the frame header then delegate reading of data to frame body.
     *
     * @param byteBuffer to read the frame from
     */
    public void read(ByteBuffer byteBuffer)
        throws InvalidFrameException
    {
        byte[] buffer = new byte[FRAME_ID_SIZE];
   
        if(byteBuffer.position()+ FRAME_HEADER_SIZE >= byteBuffer.limit())
        {
            logger.warning("No space to find another frame:");
            throw new InvalidFrameException(" No space to find another frame");
        }

        // Read the Frame ID Identifier
        byteBuffer.get(buffer, 0, FRAME_ID_SIZE);
        identifier = new String(buffer);
        logger.fine("Identifier is" + identifier);
        // Is this a valid identifier?
        if (isValidID3v2FrameIdentifier(identifier) == false)
        {
            //If not valid move file pointer back to one byte after
            //the original check so can try again.
            logger.info("Invalid identifier:" + identifier);
            byteBuffer.position(byteBuffer.position() - (FRAME_ID_SIZE - 1));
            throw new InvalidFrameException(identifier + " is not a valid ID3v2.40 frame");
        }
        //Read the size field
        frameSize = byteBuffer.getInt();
        if (frameSize < 0)
        {
            logger.warning("Invalid Frame size:" + identifier);
            throw new InvalidFrameException(identifier + " is invalid frame");
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
        
        //Read the flag bytes
        statusFlags = new StatusFlags(byteBuffer.get());
        encodingFlags = new EncodingFlags(byteBuffer.get());
        //Read the body data
        frameBody = readBody(identifier, byteBuffer, frameSize);
    }

    /**
     * Write the frame. Writes the frame header but writing the data is delegated to the
     * frame body.
     *
     * @throws IOException
     */
    public void write(ByteArrayOutputStream tagBuffer)
        throws IOException
    {
        logger.info("Writing frame to file:" + getIdentifier());
        //This is where we will write header, move position to where we can
        //write body

       ByteBuffer headerBuffer = ByteBuffer.allocate(FRAME_HEADER_SIZE);

        //Write Frame Body Data
        ByteArrayOutputStream bodyOutputStream = new ByteArrayOutputStream();
        ((AbstractID3v2FrameBody) frameBody).write(bodyOutputStream);
        //Write Frame Header
        //Write Frame ID, the identifier must be 4 bytes bytes long it may not be
        //because converted an unknown v2.2 id (only 3 bytes long)
        if (getIdentifier().length() == 3)
        {
            identifier = identifier + ' ';
        }
        headerBuffer.put(getIdentifier().getBytes(), 0, FRAME_ID_SIZE);
        //Write Frame Size
        int size = frameBody.getSize();
        logger.fine("Frame Size Is:" + size);
        headerBuffer.putInt(frameBody.getSize());
        //Write the Flags
        //@todo What about adjustments to header based on encoding flag
        headerBuffer.put(statusFlags.getWriteFlags());
        headerBuffer.put(encodingFlags.getFlags());

        //Add header to the Byte Array Output Stream
        tagBuffer.write(headerBuffer.array());

        //Add body to the Byte Array Output Stream
        tagBuffer.write(bodyOutputStream.toByteArray());
    }

    /**
     * Get Status Flags Object
     */
    protected AbstractID3v2Frame.StatusFlags getStatusFlags()
    {
        return statusFlags;
    }

    /**
     * Get Encoding Flags Object
     */
    protected AbstractID3v2Frame.EncodingFlags getEncodingFlags()
    {
        return encodingFlags;
    }

    /**
     * Member Class This represents a frame headers Status Flags
     * Make adjustments if necessary based on frame type and specification.
     */
    class StatusFlags
        extends ID3v23Frame.StatusFlags
    {
        /** Note these are in a different location to v2.3*/
        /**
         * Discard frame if tag altered
         */
        public static final int MASK_TAG_ALTER_PRESERVATION = FileConstants.BIT6;

        /**
         * Discard frame if audio part of file altered
         */
        public static final int MASK_FILE_ALTER_PRESERVATION = FileConstants.BIT5;

        /**
         * Frame tagged as read only
         */
        public static final int MASK_READ_ONLY = FileConstants.BIT4;

        /**
         * Use this when creating a frame from scratch
         */
        StatusFlags()
        {
            super();
        }

        /**
         * Use this constructor when reading from file or from another v4 frame
         */
        StatusFlags(byte flags)
        {
            originalFlags = flags;
            writeFlags = flags;
            modifyFlags();
        }

        /**
         * Use this constructor when convert a v23 frame
         */
        StatusFlags(ID3v23Frame.StatusFlags statusFlags)
        {
            originalFlags = convertV3ToV4Flags(statusFlags.getOriginalFlags());
            writeFlags = originalFlags;
            modifyFlags();
        }

        /**
         * Convert V3 Flags to equivalent V4 Flags
         */
        private byte convertV3ToV4Flags(byte v3Flag)
        {
            byte v4Flag = 0;
            if ((v3Flag & ID3v23Frame.StatusFlags.MASK_FILE_ALTER_PRESERVATION) != 0)
            {
                v4Flag |= MASK_FILE_ALTER_PRESERVATION;
            }
            if ((v3Flag & ID3v23Frame.StatusFlags.MASK_TAG_ALTER_PRESERVATION) != 0)
            {
                v4Flag |= MASK_TAG_ALTER_PRESERVATION;
            }
            return v4Flag;
        }

        /**
         * Makes modifications to flags based on specification and frameid
         */
        protected void modifyFlags()
        {
            String str = getIdentifier();
            if (ID3v24Frames.getInstanceOf().isDiscardIfFileAltered(str) == true)
            {
                writeFlags |= MASK_FILE_ALTER_PRESERVATION;
                writeFlags &= ~MASK_TAG_ALTER_PRESERVATION;
            }
            else
            {
                writeFlags &= ~MASK_FILE_ALTER_PRESERVATION;
                writeFlags &= ~MASK_TAG_ALTER_PRESERVATION;
            }
        }

        public void createStructure()
        {
            MP3File.getStructureFormatter().openHeadingElement(TYPE_FLAGS, "");
            MP3File.getStructureFormatter().addElement(TYPE_TAGALTERPRESERVATION, originalFlags & MASK_TAG_ALTER_PRESERVATION);
            MP3File.getStructureFormatter().addElement(TYPE_FILEALTERPRESERVATION, originalFlags & MASK_FILE_ALTER_PRESERVATION);
            MP3File.getStructureFormatter().addElement(TYPE_READONLY, originalFlags & MASK_READ_ONLY);
            MP3File.getStructureFormatter().closeHeadingElement(TYPE_FLAGS);
        }
    }

    /**
     * This represents a frame headers Encoding Flags
     */
    class EncodingFlags
        extends ID3v23Frame.EncodingFlags
    {
        public static final String TYPE_FRAMEUNSYNCHRONIZATION = "frameUnsynchronisation";
        public static final String TYPE_DATALENGTHINDICATOR = "dataLengthIndicator";

        /** Note these are in a different location to v2.3*/

        /**
         * Frame is compressed
         */
        public static final int MASK_COMPRESSION = FileConstants.BIT2;

        /**
         * Frame is encrypted
         */
        public static final int MASK_ENCRYPTION = FileConstants.BIT3;

        /**
         * Frame is part of a group
         */
        public static final int MASK_GROUPING_IDENTITY = FileConstants.BIT6;

        /**
         * Unsyncronisation
         */
        public static final int MASK_FRAME_UNSYNCHRONIZATION = FileConstants.BIT2;

        /**
         * Length
         */
        public static final int MASK_DATA_LENGTH_INDICATOR = FileConstants.BIT1;

        private byte flags;

        /**
         * Use this when creating a frame from scratch
         */
        EncodingFlags()
        {
            this((byte) 0);
        }

        /**
         * Use this when creating a frame from existing flags in another v4 frame
         */
        EncodingFlags(byte flags)
        {
            super(flags);
            if ((flags & MASK_FRAME_UNSYNCHRONIZATION) != 0)
            {
                logger.warning("this frame is unsynchronised");
            }

        }

        public byte getFlags()
        {
            return flags;
        }

        public void createStructure()
        {
            MP3File.getStructureFormatter().openHeadingElement(TYPE_FLAGS, "");
            MP3File.getStructureFormatter().addElement(TYPE_COMPRESSION, flags & MASK_COMPRESSION);
            MP3File.getStructureFormatter().addElement(TYPE_ENCRYPTION, flags & MASK_ENCRYPTION);
            MP3File.getStructureFormatter().addElement(TYPE_GROUPIDENTITY, flags & MASK_GROUPING_IDENTITY);
            MP3File.getStructureFormatter().addElement(TYPE_FRAMEUNSYNCHRONIZATION, flags & MASK_FRAME_UNSYNCHRONIZATION);
            MP3File.getStructureFormatter().addElement(TYPE_DATALENGTHINDICATOR, flags & MASK_DATA_LENGTH_INDICATOR);
            MP3File.getStructureFormatter().closeHeadingElement(TYPE_FLAGS);
        }
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
