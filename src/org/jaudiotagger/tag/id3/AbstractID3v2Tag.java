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
 * ID3v2 Description: This is the abstract base class for all
 * ID3v2 tags
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.tag.AbstractTag;

import java.io.*;
import java.io.RandomAccessFile;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagNotFoundException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.tag.id3.valuepair.*;

import java.util.*;
import java.nio.*;
import java.nio.channels.*;

public abstract class AbstractID3v2Tag
    extends AbstractID3Tag
{
    protected static final String TYPE_HEADER = "header";
    protected static final String TYPE_BODY = "body";

    //Tag ID as held in file
    protected static final byte[] TAG_ID =
        {'I', 'D', '3'};

    //The tag header is the same for ID3v2 versions
    protected static int TAG_HEADER_LENGTH = 10;
    protected static int FIELD_TAGID_LENGTH = 3;
    protected static int FIELD_TAG_MAJOR_VERSION_LENGTH = 1;
    protected static int FIELD_TAG_MINOR_VERSION_LENGTH = 1;
    protected static int FIELD_TAG_FLAG_LENGTH = 1;
    protected static int FIELD_TAG_SIZE_LENGTH = 4;

    protected static final int FIELD_TAGID_POS = 0;
    protected static final int FIELD_TAG_MAJOR_VERSION_POS = 3;
    protected static final int FIELD_TAG_MINOR_VERSION_POS = 4;
    protected static final int FIELD_TAG_FLAG_POS = 5;
    protected static final int FIELD_TAG_SIZE_POS = 6;

    protected static final int TAG_SIZE_INCREMENT = 100;

    //The initial size of the buffer to be used when creating the tag
    private static final int INITIAL_TAG_BODY_SIZE = 1000000;

    /**
     * Map of all frames for this tag
     */
    protected HashMap frameMap = null;

    /**
     * Holds the ids of invalid duplicate frames
     */
    protected static final String TYPE_DUPLICATEFRAMEID = "duplicateFrameId";
    protected String duplicateFrameId = "";

    /**
     * Holds byte count of invalid duplicate frames
     */
    protected static final String TYPE_DUPLICATEBYTES = "duplicateBytes";
    protected int duplicateBytes = 0;

    /**
     * Holds byte count of empty frames
     */
    protected static final String TYPE_EMPTYFRAMEBYTES = "emptyFrameBytes";
    protected int emptyFrameBytes = 0;

    /**
     * DOCUMENT ME!
     */
    protected static final String TYPE_FILEREADSIZE = "fileReadSize";
    protected int fileReadSize = 0;

    /**
     * Holds byte count of invalid frames
     */
    protected static final String TYPE_INVALIDFRAMEBYTES = "invalidFrameBytes";
    protected int invalidFrameBytes = 0;

    /**
     * Empty Constructor
     */
    public AbstractID3v2Tag()
    {
    }

    /**
     * Copy primitives apply to all tags
     */
    protected void copyPrimitives(AbstractID3v2Tag copyObject)
    {
        logger.info("copying Primitives");
        //Primitives type variables common to all IDv2 Tags
        this.duplicateFrameId = new String(copyObject.duplicateFrameId);
        this.duplicateBytes = copyObject.duplicateBytes;
        this.emptyFrameBytes = copyObject.emptyFrameBytes;
        this.fileReadSize = copyObject.fileReadSize;
        this.invalidFrameBytes = copyObject.invalidFrameBytes;
    }

    /**
     * Copy frames from objects of same type
     */
    protected void copyFrames(AbstractID3v2Tag copyObject)
    {
        logger.info("Copying Frames,ther are:" + copyObject.frameMap.keySet().size());
        /* Go through the tags frameset and create a new Frame based on the frame by
         * calling the frames copy constructor v1.1 create a v2.2 frame from another v2.2 frame
         * or create a v2.3 frame from another v2.3 frame.
         */
        Iterator iterator = copyObject.frameMap.keySet().iterator();
        String identifier;
        AbstractID3v2Frame newFrame;
        while (iterator.hasNext())
        {
            identifier = (String) iterator.next();
            newFrame = (AbstractID3v2Frame) ID3Tags.copyObject(copyObject.frameMap.get(identifier));
            if (newFrame.getBody() != null)
            {
                this.frameMap.put(newFrame.getIdentifier(), newFrame);
            }
        }
    }

    /**
     * This constructor is used when a tag is created as a duplicate of another
     * tag of the same type and version.
     *
     * @todo have i understood this.
     */
    public AbstractID3v2Tag(AbstractID3v2Tag copyObject)
    {
    }

    /**
     * Returns the number of bytes which come from duplicate frames
     *
     * @return DOCUMENT ME!
     */
    public int getDuplicateBytes()
    {
        return duplicateBytes;
    }

    /**
     * Return the string which holds the ids of all
     * duplicate frames.
     *
     * @return DOCUMENT ME!
     */
    public String getDuplicateFrameId()
    {
        return duplicateFrameId;
    }

    /**
     * Returns the number of bytes which come from duplicate frames
     *
     * @return DOCUMENT ME!
     */
    public int getEmptyFrameBytes()
    {
        return emptyFrameBytes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFileReadBytes()
    {
        return fileReadSize;
    }

    /**
     * Add a frame to this tag
     *
     * @param frame DOCUMENT ME!
     * @todo needs to be overridden, shouldn't be able to add a frame of
     * one version to a tag of another version.
     */
    public void setFrame(AbstractID3v2Frame frame)
    {
        frameMap.put(frame.getIdentifier(), frame);
    }

    /**
     * Used for setting multiple frames
     */
    public void setFrame(String identifier, ArrayList multiFrame)
    {
        frameMap.put(identifier, multiFrame);
    }

    /**
     * Return frame in this tag with given identifier if it exists.
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Object getFrame(String identifier)
    {
        return frameMap.get(identifier);
    }

    /**
     * Return the number of frames in this tag
     *
     * @return DOCUMENT ME!
     */
    public int getFrameCount()
    {
        if (frameMap == null)
        {
            return 0;
        }
        else
        {
            return frameMap.size();
        }
    }

    /**
     * Return all frames which start with the identifier, this
     * can be more than one which is useful if trying to retrieve
     * similar frames e.g TIT1,TIT2,TIT3 ... and dont know exaclty
     * which ones there are.
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public Iterator getFrameOfType(String identifier)
    {
        Iterator iterator = frameMap.keySet().iterator();
        HashSet result = new HashSet();
        String key;
        while (iterator.hasNext())
        {
            key = (String) iterator.next();
            if (key.startsWith(identifier))
            {
                result.add(frameMap.get(key));
            }
        }
        return result.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInvalidFrameBytes()
    {
        return invalidFrameBytes;
    }

    /**
     * Add any frames that this tag doesnt already have.
     *
     * @param tag The tag containing extra frames to be added to this tag
     * @todo doesnt handles differences in identifiers between versions
     */
    public void append(AbstractTag tag)
    {
        AbstractID3v2Tag oldTag = this;
        AbstractID3v2Tag newTag = null;
        if (tag != null)
        {
            if (tag instanceof AbstractID3v2Tag)
            {
                newTag = (AbstractID3v2Tag) tag;
            }
            else
            {
                newTag = new ID3v24Tag(tag);
            }
            Iterator iterator = newTag.frameMap.values().iterator();
            AbstractID3v2Frame frame;
            while (iterator.hasNext())
            {
                frame = (AbstractID3v2Frame) iterator.next();
                if (oldTag.hasFrame(frame.getIdentifier()) == false)
                {
                    oldTag.setFrame(frame);
                }
            }
        }
    }

    /**
     * Read existing tag from file and write
     * back to file with any extra frames
     * that this tag has.
     *
     * @param file The file to write to.
     * @throws IOException  DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     */
    public void append(RandomAccessFile file)
        throws IOException, TagException
    {
        AbstractID3v2Tag oldTag;
        try
        {
            oldTag = new ID3v24Tag(file);
            oldTag.append(this);
            oldTag.write(file);
        }
        catch (TagNotFoundException ex)
        {
            try
            {
                oldTag = new ID3v23Tag(file);
                oldTag.append(this);
                oldTag.write(file);
            }
            catch (TagNotFoundException ex2)
            {
                try
                {
                    oldTag = new ID3v22Tag(file);
                    oldTag.append(this);
                    oldTag.write(file);
                }
                catch (TagNotFoundException ex3)
                {
                    this.write(file);
                }
            }
        }
    }

    /**
     * Delete Tag
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @todo should clear all data and preferably recover lost space.
     */
    public void delete(RandomAccessFile file)
        throws IOException
    {
        // this works by just erasing the "TAG" tag at the beginning
        // of the file
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        if (seek(file))
        {
            file.seek(0);
            file.write(buffer);
        }
    }

    /**
     * Is this tag equivalent to another
     *
     * @param obj DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj instanceof AbstractID3v2Tag) == false)
        {
            return false;
        }
        AbstractID3v2Tag object = (AbstractID3v2Tag) obj;
        if (this.frameMap.equals(object.frameMap) == false)
        {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Return whether tag has frame with this identifier
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean hasFrame(String identifier)
    {
        return frameMap.containsKey(identifier);
    }

    /**
     * Return whether tag has frame starting
     * with this identifier
     *
     * @param identifier DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean hasFrameOfType(String identifier)
    {
        Iterator iterator = frameMap.keySet().iterator();
        String key;
        boolean found = false;
        while (iterator.hasNext() && !found)
        {
            key = (String) iterator.next();
            if (key.startsWith(identifier))
            {
                found = true;
            }
        }
        return found;
    }

    /**
     * Return the frames in the order they were added
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator()
    {
        return frameMap.values().iterator();
    }

    /**
     * Add all frames to this tag overwriting the frames if they
     * already exist.
     *
     * @param tag The tag containing extra frames to be added to this tag
     * @todo doesnt handles differences in identifiers between versions
     */
    public void overwrite(AbstractTag tag)
    {
        AbstractID3v2Tag oldTag = this;
        AbstractID3v2Tag newTag = null;
        if (tag != null)
        {
            if (tag instanceof AbstractID3v2Tag)
            {
                newTag = (AbstractID3v2Tag) tag;
            }
            else
            {
                newTag = new ID3v24Tag(tag);
            }
            Iterator iterator = newTag.frameMap.values().iterator();
            AbstractID3v2Frame frame;
            while (iterator.hasNext())
            {
                frame = (AbstractID3v2Frame) iterator.next();
                oldTag.setFrame(frame);
            }
        }
        //super.overwrite(newTag);
    }

    /**
     * Overwrite the tag in this file with this
     * tag.
     *
     * @param file DOCUMENT ME!
     * @throws IOException  DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     */
    public void overwrite(RandomAccessFile file)
        throws IOException, TagException
    {
        AbstractID3v2Tag oldTag;
        try
        {
            oldTag = new ID3v24Tag(file);
            oldTag.overwrite(this);
            oldTag.write(file);
        }
        catch (TagNotFoundException ex)
        {
            try
            {
                oldTag = new ID3v23Tag(file);
                oldTag.overwrite(this);
                oldTag.write(file);
            }
            catch (TagNotFoundException ex2)
            {
                try
                {
                    oldTag = new ID3v22Tag(file);
                    oldTag.overwrite(this);
                    oldTag.write(file);
                }
                catch (TagNotFoundException ex3)
                {
                    this.write(file);
                }
            }
        }
    }

    /**
     * Remove frame with this identifier from tag
     *
     * @param identifier DOCUMENT ME!
     */
    public void removeFrame(String identifier)
    {
        frameMap.remove(identifier);
    }

    /**
     * Remove any frames starting with this
     * identifier from tag
     *
     * @param identifier DOCUMENT ME!
     */
    public void removeFrameOfType(String identifier)
    {
        Iterator iterator = this.getFrameOfType(identifier);
        while (iterator.hasNext())
        {
            AbstractID3v2Frame frame = (AbstractID3v2Frame) iterator.next();
            frameMap.remove(frame.getIdentifier());
        }
    }

    /**
     * Return frames as a collection
     *
     * @return DOCUMENT ME!
     */
    public java.util.Collection values()
    {
        return frameMap.values();
    }

    /**
     * Remove all frame from this tag and replace
     * with frames in the parameter tag.
     *
     * @param tag DOCUMENT ME!
     * @todo doesnt handles differences in identifiers between versions
     * @todo doesnt make much sense because nothing
     * will exist of original tag apart from a now
     * defunct header.
     */
    public void write(AbstractTag tag)
    {
        AbstractID3v2Tag oldTag = this;
        AbstractID3v2Tag newTag = null;
        if (tag != null)
        {
            if (tag instanceof AbstractID3v2Tag)
            {
                newTag = (AbstractID3v2Tag) tag;
            }
            else
            {
                newTag = new ID3v24Tag(tag);
            }
            Iterator iterator = newTag.frameMap.values().iterator();
            oldTag.frameMap.clear();
            AbstractID3v2Frame frame;
            while (iterator.hasNext())
            {
                frame = (AbstractID3v2Frame) iterator.next();
                oldTag.setFrame(frame);
            }
        }
        //super.write(newTag);
    }

    public void write(File file, long audioStartByte)
        throws IOException
    {
    }

    public void write(RandomAccessFile file)
        throws IOException
    {
    }

    /**
     * Read Tag Size from byteArray in format specified in spec and convert to int.
     *
     * @param buffer DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected int byteArrayToSize(byte[] buffer)
    {
        /**
         * the decided not to use the top bit of the 4 bytes so we need to
         * convert the size back and forth
         */
        return (int) (buffer[0] << 21) + (buffer[1] << 14) + (buffer[2] << 7) + (buffer[3]);
    }

    /**
     * Write Tag Size to Byte array to format as required in Tag Header.
     *
     * @param size DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected byte[] sizeToByteArray(int size)
    {
        byte[] buffer = new byte[FIELD_TAG_SIZE_LENGTH];
        buffer[0] = (byte) ((size & 0x0FE00000) >> 21);
        buffer[1] = (byte) ((size & 0x001FC000) >> 14);
        buffer[2] = (byte) ((size & 0x00003F80) >> 7);
        buffer[3] = (byte) (size & 0x0000007F);
        return buffer;
    }

    /**
     * Does a ID3v2_40 tag exist in this file.
     *
     * @param file file to search for
     * @return true if tag exists.
     * @throws IOException DOCUMENT ME!
     */
    public boolean seek(RandomAccessFile file)
        throws IOException
    {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        file.seek(0);
        // read the TAG value
        file.read(buffer, 0, FIELD_TAGID_LENGTH);
        if (!(Arrays.equals(buffer, TAG_ID)))
        {
            return false;
        }
        //Major Version
        if (file.readByte() != this.getMajorVersion())
        {
            return false;
        }
        //Minor Version
        if (file.readByte() != this.getRevision())
        {
            return false;
        }
        return true;
    }

    /**
     * This method determines the total tag size taking into account
     * where the audio file starts, the size of the tagging data and
     * user options for defining how tags should shrink or grow.
     */
    protected int calculateTagSize(int tagSize, int audioStart)
    {
        /** We can fit in the tag so no adjustments required */
        if (tagSize <= audioStart)
        {
            return audioStart;
        }
        /** There is not enough room as we need to move the audio file we might
         *  as well increase it more than neccessary for future changes
         */
        return tagSize + TAG_SIZE_INCREMENT;
    }

    /**
     * Adjust the length of the ID3v2 padding at the beginning of the MP3 file
     * this datatype refers to. A new file will be created with enough size to fit
     * the <code>ID3v2</code> tag. The old file will be deleted, and the new file renamed.
     *
     * @param paddingSize Initial padding size. This size is doubled until the
     *                    ID3v2 tag will fit. A paddingSize of zero will create a padding
     *                    length exactly equal to the tag size.
     * @param file        The file to adjust the padding length of
     * @return DOCUMENT ME!
     * @throws FileNotFoundException if the file exists but is a directory
     *                               rather than a regular file or cannot be opened for any other
     *                               reason
     * @throws IOException           on any I/O error
     * @throws TagException          on any exception generated by this library.
     */
    public void adjustPadding(File file, int paddingSize, long audioStart)
        throws FileNotFoundException, IOException
    {
        logger.finer("Need to move audio file to accomodate tag");
        FileChannel fcIn = null;
        FileChannel fcOut = null;
        /** Create buffer holds the neccessary padding */
        ByteBuffer paddingBuffer = ByteBuffer.wrap(new byte[paddingSize]);
        /** Create Temporary File and write channel*/
        File paddedFile = File.createTempFile("temp", ".mp3",
            file.getParentFile());
        fcOut = new FileOutputStream(paddedFile).getChannel();
        //Create read channel from original file
        fcIn = new FileInputStream(file).getChannel();
        //Write padding
        long written = fcOut.write(paddingBuffer);
        //Write rest of file starting from audio
        logger.finer("Copying:" + (file.length() - audioStart) + "bytes");
        long written2 = fcIn.transferTo(audioStart, file.length() - audioStart, fcOut);
        logger.finer("Written padding:" + written + " Data:" + written2);
        //Store original modification time
        long lastModified = file.lastModified();
        //Close Channels
        fcIn.close();
        fcOut.close();
        //Delete original File
        file.delete();
        //Rename temporary file and set modification time to original time.
        paddedFile.renameTo(file);
        paddedFile.setLastModified(lastModified);

    }

    /**
     * Add frame to HashMap used when converting between tag versions, take into account
     * occurences when two frame may both map to a single frame when converting between
     * versions
     */
    protected void copyFrameIntoMap(String id, AbstractID3v2Frame newFrame)
    {
        /* The frame already exists this shouldnt normally happen because frames
         * that are allowed to be multiple don't call this method. Frames that
         * arent allowed to be multiple arent added to hashmap in first place when
         * originally added.
         * However converting some frames from tag of one version to another may
         * mean that two different frames both get converted to one frame, this
         * particulary applies to DateTime fields which were originally two fields
         * in v2.3 but are one field in v2.4.
         */
        if (frameMap.containsKey(newFrame.getIdentifier()))
        {
            //Retrieve the frame with the same id we have already loaded into the map
            AbstractID3v2Frame firstFrame = (AbstractID3v2Frame) frameMap.get(newFrame.getIdentifier());

            //Two different frames both converted to TDRCFrames
            if(newFrame.getBody() instanceof FrameBodyTDRC)
            {
                FrameBodyTDRC body = (FrameBodyTDRC) firstFrame.getBody();
                FrameBodyTDRC newBody = (FrameBodyTDRC) newFrame.getBody();
                //Just add the data to the frame
                if (newBody.getOriginalID().equals(ID3v23Frames.FRAME_ID_V3_TYER))
                {
                    body.setYear(newBody.getText());
                }
                else if (newBody.getOriginalID().equals(ID3v23Frames.FRAME_ID_V3_TDAT))
                {
                    body.setDate(newBody.getText());
                }
                else if (newBody.getOriginalID().equals(ID3v23Frames.FRAME_ID_V3_TIME))
                {
                    body.setTime(newBody.getText());
                }
                else if (newBody.getOriginalID().equals(ID3v23Frames.FRAME_ID_V3_TRDA))
                {
                    body.setReco(newBody.getText());
                }
            }
            else
            {
                logger.warning("Found duplicate frame in invalid situation:" + newFrame.getIdentifier());
            }
        }
        else
        //Just add frame to map
        {
            frameMap.put(newFrame.getIdentifier(), newFrame);
        }
    }

    /**
     * Decides what to with the frame that has just be read from file.
     * If the frame is an allowable duplicate frame and is a duplicate we add all
     * frames into an ArrayList and add the Arraylist to the hashmap. if not allowed
     * to be duplictae we store bytes in the duplicateBytes variable.
     */
    protected void loadFrameIntoMap(String id, AbstractID3v2Frame next)
    {
        if (
            (ID3v24Frames.getInstanceOf().isMultipleAllowed(id)) ||
            (ID3v23Frames.getInstanceOf().isMultipleAllowed(id)) ||
            (ID3v22Frames.getInstanceOf().isMultipleAllowed(id))
        )
        {
            //If a frame already exists of this type
            if (frameMap.containsKey(id))
            {
                Object o = frameMap.get(id);
                if (o instanceof ArrayList)
                {
                    ArrayList multiValues = (ArrayList) o;
                    multiValues.add(next);
                    logger.finer("Adding Multi Frame(1)" + id);
                }
                else
                {
                    ArrayList multiValues = new ArrayList();
                    multiValues.add(o);
                    multiValues.add(next);
                    frameMap.put(id, multiValues);
                    logger.finer("Adding Multi Frame(2)" + id);
                }
            }
            else
            {
                logger.finer("Adding Multi FrameList(3)" + id);
                frameMap.put(id, next);
            }
        }
        //If duplicate frame just stores it somewhere else @todo ?
        else if (frameMap.containsKey(id))
        {
            logger.warning("Duplicate Frame" + id);
            this.duplicateFrameId += (id + "; ");
            this.duplicateBytes += ((AbstractID3v2Frame) frameMap.get(id)).getSize();
        }
        else
        {
            logger.finer("Adding Frame" + id);
            frameMap.put(id, next);
        }

    }

    /**
     * Return frame size based upon the sizes of the tags rather than the physical
     * no of bytes between start of ID3Tag and start of Audio Data.Should be extended
     * by subclasses to incude header.
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        int size = 0;
        Iterator iterator = frameMap.values().iterator();
        AbstractID3v2Frame frame;
        while (iterator.hasNext())
        {
            Object o = iterator.next();
            if (o instanceof AbstractID3v2Frame)
            {
                frame = (AbstractID3v2Frame) o;
                size += frame.getSize();
            }
            else
            {
                ArrayList multiFrames = (ArrayList) o;
                for (ListIterator li = multiFrames.listIterator(); li.hasNext();)
                {
                    frame = (AbstractID3v2Frame) li.next();
                    size += frame.getSize();
                }
            }
        }
        return size;
    }

    /**
     * Write all the frames to the buffer, we do not know the size of the buffer required until we call the framebodies
     * write method because the size required depends on the data and what encoding is required,
     * and the buffer cannot be resized so how do we solve this problem.
     * <p/>
     * We could do a psuedo write first to correctly calculate the size before writing to the buffer but performance
     * would be poor, or we could allocate a large Buffer to start with. Unfortunately there is no guaranteed upper limit
     * for example if a user add alots of APIC Picture frames with very large pictures the tags could run to several
     * megabytes but most tags will be much smaller.
     * <p/>
     * We take a pragmatic approach, we allocate a reasonably large buffer which willl be ok for most cases, but we catch
     * the exception that could occur and if so we increase the size of the buffer and retry.
     *
     * @return ByteBuffer Contains all the frames written within the tag ready for writing to file
     * @throws IOException
     */
    protected ByteBuffer writeFramesToBuffer() throws IOException
    {
        ByteBuffer bodyBuffer = null;

        int bufferSize = getSize();
        logger.finer("Estimated Tag Buffer Size Required is "+bufferSize);
        if( bufferSize<INITIAL_TAG_BODY_SIZE )
        {
            bufferSize=INITIAL_TAG_BODY_SIZE;
        }


        while (true)
        {
            try
            {
                bodyBuffer = ByteBuffer.allocate(bufferSize);
                this.write(bodyBuffer);
                break;
            }
            catch (java.nio.BufferOverflowException boe)
            {
                //Double Size and try again.
                bufferSize = bufferSize * 2;
                logger.finer("Estimated Tag Buffer Size Increased To "+bufferSize);
                bodyBuffer = ByteBuffer.allocate(bufferSize);
                continue;
            }
        }
        return bodyBuffer;
    }

    /**
     * Write tag frames to buffer, must be overidden by superclasses
     */
    public void write(ByteBuffer bodyBuffer)
        throws IOException
    {
        //Write all frames, defaults to the order in which they were loaded, newly
        //created frames will be at end of tag.
        AbstractID3v2Frame frame;
        Iterator iterator;
        iterator = frameMap.values().iterator();
        while (iterator.hasNext())
        {
            Object o = iterator.next();
            if (o instanceof AbstractID3v2Frame)
            {
                frame = (AbstractID3v2Frame) o;
                frame.write(bodyBuffer);
            }
            else
            {
                ArrayList multiFrames = (ArrayList) o;
                for (ListIterator li = multiFrames.listIterator(); li.hasNext();)
                {
                    frame = (AbstractID3v2Frame) li.next();
                    frame.write(bodyBuffer);
                }
            }
        }
    }

    public void createStructure()
    {
        createStructureHeader();
        createStructureBody();
    }

    public void createStructureHeader()
    {
        MP3File.getStructureFormatter().addElement(this.TYPE_DUPLICATEBYTES, this.duplicateBytes);
        MP3File.getStructureFormatter().addElement(this.TYPE_DUPLICATEFRAMEID, this.duplicateFrameId);
        MP3File.getStructureFormatter().addElement(this.TYPE_EMPTYFRAMEBYTES, this.emptyFrameBytes);
        MP3File.getStructureFormatter().addElement(this.TYPE_FILEREADSIZE, this.fileReadSize);
        MP3File.getStructureFormatter().addElement(this.TYPE_INVALIDFRAMEBYTES, this.invalidFrameBytes);
    }

    public void createStructureBody()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_BODY, "");

        AbstractID3v2Frame frame;
        Iterator iterator = frameMap.values().iterator();
        while (iterator.hasNext())
        {
            Object o = iterator.next();
            if (o instanceof AbstractID3v2Frame)
            {
                frame = (AbstractID3v2Frame) o;
                frame.createStructure();
            }
            else
            {
                ArrayList multiFrames = (ArrayList) o;
                for (ListIterator li = multiFrames.listIterator(); li.hasNext();)
                {
                    frame = (AbstractID3v2Frame) li.next();
                    frame.createStructure();
                }
            }
        }
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_BODY);

    }
}
