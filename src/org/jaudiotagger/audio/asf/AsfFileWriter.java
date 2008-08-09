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
package org.jaudiotagger.audio.asf;

import org.jaudiotagger.audio.asf.util.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.audio.asf.data.Chunk;
import org.jaudiotagger.audio.asf.data.ContentDescription;
import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.data.ExtendedContentDescription;
import org.jaudiotagger.audio.asf.data.GUID;
import org.jaudiotagger.audio.asf.io.AsfHeaderReader;
import org.jaudiotagger.audio.asf.io.RandomAccessFileOutputStream;
import org.jaudiotagger.audio.asf.tag.AsfTag;
import org.jaudiotagger.audio.asf.util.ChunkPositionComparator;
import org.jaudiotagger.audio.asf.util.TagConverter;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.tag.Tag;

/**
 * This class writes given tags to ASF files containing WMA content. <br>
 * <br>
 * 
 * @author Christian Laireiter
 */
public class AsfFileWriter extends AudioFileWriter
{

    /**
     * This method copies <code>number</code> of bytes from <code>source</code>
     * to <code>destination</code>.<br>
     * 
     * @param source
     *            Source for copy
     * @param destination
     *            Destination for copy
     * @param number
     *            number of bytes to copy.
     * @throws IOException
     *             read or write errors.
     */
    private void copy(RandomAccessFile source, RandomAccessFile destination, long number) throws IOException
    {
        byte[] buffer = new byte[8192];
        long bytesCopied = 0;
        int read = -1;
        while ((read = source.read(buffer, 0, ((bytesCopied + 8192 > number) ? (int) (number - bytesCopied) : 8192))) > 0)
        {
            bytesCopied += read;
            destination.write(buffer, 0, read);
        }
    }

    /**
     * This method creates a modified copy of the input <code>raf</code> and
     * writes it to <code>rafTemp</code>.<br>
     * If the source contains no extended content description but one is needed
     * ( {@link #isExtendedContentDescriptionMandatory(Tag)}) it will be
     * created. If the new file doesn't require such a chunk and the source
     * didn't contain optional values in it, it will be deleted.
     * 
     * @param tag
     *            Tag containing the new values.
     * @param header
     *            Read header of <code>raf</code>.
     * @param raf
     *            source
     * @param rafTemp
     *            destination.
     * @param ignoreDescriptions
     *            if <code>true</code> the content description and the extended
     *            content description won't be written. (For deleting tags.).
     * @throws IOException
     *             read or write errors.
     */
    private void createModifiedCopy(AsfTag tag, AsfHeader header, RandomAccessFile raf, RandomAccessFile rafTemp, boolean ignoreDescriptions) throws IOException
    {
        /*
         * First of all Copy the first 30 bytes of the file. The GUID and one
         * unknown field is needed. (All other values [chunkcount, chunklength]
         * will be adjusted at the end of the method)
         */
        raf.seek(0);
        copy(raf, rafTemp, 30);
        // Get all chunks in order of their position
        Chunk[] chunks = getOrderedChunks(header);
        // We need to know the amount of bytes that differs from the original
        long fileSizeDifference = 0;
        long chunkCount = header.getChunkCount();
        // This field stores the location of the fileheader at the destination
        long newFileHeaderPos = -1;
        /*
         * Now check if the source doesn't contain a property chunk but one is
         * needed to store all tag values.
         */
        if (header.getExtendedContentDescription() == null && isExtendedContentDescriptionMandatory(tag) && !ignoreDescriptions)
        {
            // The source had no property chunk but one is written. So on chunk
            // more.
            chunkCount++;
            // Create the property chunk (A new one will always be placed at
            // first).
            ExtendedContentDescription extDesc = createNewExtendedContentDescription(tag);
            fileSizeDifference += extDesc.writeInto(new RandomAccessFileOutputStream(rafTemp));
        }
        /*
         * Now check if the source doesn't contain a content description but one
         * is needed to store all tag values. Content description stores title,
         * author, copyright, description and rating of the file
         */
        if (header.getContentDescription() == null && isContentdescriptionMandatory(tag) && !ignoreDescriptions)
        {
            // The source had no content description chunk but one is written.
            // So on chunk more.
            chunkCount++;
            // Now create the new content description.
            ContentDescription contentDesc = TagConverter.createContentDescription(tag);
            fileSizeDifference += contentDesc.writeInto(new RandomAccessFileOutputStream(rafTemp));
        }
        // Now copy chunks (and modify if necessary)
        for (int i = 0; i < chunks.length; i++)
        {
            if (chunks[i] == header.getContentDescription())
            {
                if (ignoreDescriptions || !isContentdescriptionMandatory(tag))
                {
                    // Remove file size and so on
                    chunkCount--;
                    fileSizeDifference -= header.getContentDescription().getChunkLength().longValue();
                }
                else
                {
                    // Now write new content description , because of
                    // modifications
                    ContentDescription contentDesc = TagConverter.createContentDescription(tag);
                    // Store the difference of sizes between old and new
                    // contentdescription.
                    fileSizeDifference += contentDesc.writeInto(new RandomAccessFileOutputStream(rafTemp)) - header.getContentDescription().getChunkLength().longValue();

                }
            }
            else if (chunks[i] == header.getExtendedContentDescription())
            {
                if (deleteExtendedContentDescription(header.getExtendedContentDescription(), tag) || ignoreDescriptions)
                {
                    /*
                     * No property chunk will be written. So lower the
                     * chunkCount and store the amount of bytes lost from the
                     * original.
                     */
                    chunkCount--;
                    fileSizeDifference -= header.getExtendedContentDescription().getChunkLength().longValue();
                }
                else
                {
                    /*
                     * write a modified copy of the property chunk;
                     */
                    fileSizeDifference += createNewExtendedContentDescription(tag).writeInto(new RandomAccessFileOutputStream(rafTemp)) - header.getExtendedContentDescription().getChunkLength().longValue();
                }
            }
            else
            {
                // No special handling required. This chunk can simply be
                // copied.
                if (GUID.GUID_FILE.equals(chunks[i].getGuid()))
                {
                    newFileHeaderPos = rafTemp.getFilePointer();
                }
                raf.seek(chunks[i].getPosition());
                copy(raf, rafTemp, chunks[i].getChunkLength().longValue());
            }
        }
        // Now the rest of the file needs to be copied
        raf.seek(header.getChunckEnd());
        copy(raf, rafTemp, raf.length() - raf.getFilePointer());
        // Now the asf header and the fileheader need to be adjusted.
        // First the number of chunks may differ, and second the size of the
        // file might have been changed. From second comes third: all changes
        // of the filesize occur in the asf header.
        rafTemp.seek(24);
        write16UINT(chunkCount, rafTemp);
        // Seek to the new file header and 40 more to reach the filesize
        // property
        rafTemp.seek(newFileHeaderPos + 40);
        write32UINT(header.getFileHeader().getFileSize().longValue() + fileSizeDifference, rafTemp);
        // Seekt to the asf header size and write the new value
        rafTemp.seek(16);
        write32UINT(header.getChunkLength().longValue() + fileSizeDifference, rafTemp);
    }

    /**
    * This method writes a completely new extended content description to
    * <code>rafTemp</code>.<br>
    * Some values of {@link Tag}are nested within this chunk. Those values of
    * <code>tagChunk</code> which do not belong to tag will be kept, the rest
    * replaced or even added. <br>
    * 
    * @param tag
    *            contains new Elements.
    *            File to write to.
    * @return A new extended content description.
    */
    private ExtendedContentDescription createNewExtendedContentDescription(AsfTag tag)
    {
        final ExtendedContentDescription result = new ExtendedContentDescription();
        TagConverter.assignCommonTagValues(tag, result);
        TagConverter.assignOptionalTagValues(tag, result);
        return result;
    }

    /**
     * This method decides if the extended content description may be ignored
     * for the modified file. <br>
     * This is the case if the extended content description doesn't contain any
     * additional tags which are not contained in <code>tag</code> and
     * {@link #isExtendedContentDescriptionMandatory(Tag)}returns
     * <code>false</code>.
     * 
     * @param tagHeader
     *            ContentDescriptor chunk from source.
     * @param tag
     *            Tag that is written.
     * @return <code>true</code>, if property chunk can be skipped.
     */
    private boolean deleteExtendedContentDescription(ExtendedContentDescription tagHeader, Tag tag)
    {
        HashSet<String> ignoreDescriptors = new HashSet<String>(Arrays.asList(new String[]{ContentDescriptor.ID_GENRE, ContentDescriptor.ID_GENREID, ContentDescriptor.ID_TRACKNUMBER, ContentDescriptor.ID_ALBUM, ContentDescriptor.ID_YEAR}));
        Iterator<ContentDescriptor> it = tagHeader.getDescriptors().iterator();
        boolean found = false;
        /*
         * Now search for a content descriptor whose id is not one of
         * ignoreDescriptors. These descriptors cannot be modified by entagged
         * and will be preserved
         */
        while (it.hasNext() && !found)
        {
            ContentDescriptor current = it.next();
            found = !ignoreDescriptors.contains(current.getName());
        }
        return !found && !isExtendedContentDescriptionMandatory(tag);
    }

    /**
     * (overridden)
     * 
     * @see org.jaudiotagger.audio.generic.AudioFileWriter#deleteTag(java.io.RandomAccessFile,
     *      java.io.RandomAccessFile)
     */
    protected void deleteTag(RandomAccessFile raf, RandomAccessFile tempRaf) throws CannotWriteException, IOException
    {
        try
        {
            AsfHeader header = AsfHeaderReader.readHeader(raf);
            if (header == null)
            {
                throw new NullPointerException("Header is null, so file couldn't be read properly. " + "(Interpretation of data, not file access rights.)");
            }
            createModifiedCopy(new AsfTag(), header, raf, tempRaf, true);
        } catch (IOException ioe)
        {
            throw ioe;
        } catch (Exception cre)
        {
            throw new CannotWriteException("Cannot modify tag because exception occured:\n   " + cre.getMessage());
        }
    }

    /**
     * This method returns all chunks contained within the given
     * <code>header</code> and returns them in an array. Additionally the chunks
     * in the array are sorted ascending by their appearance in the file. <br>
     * This should make it easy to create a copy an modify just specified
     * chunks.
     * 
     * @param header
     *            The header object containing all chunks.
     * @return All chunks ordered by position.
     */
    private Chunk[] getOrderedChunks(AsfHeader header)
    {
        Collection<Chunk> result = header.getChunks();
        Chunk[] tmp = result.toArray(new Chunk[result.size()]);
        Arrays.sort(tmp, new ChunkPositionComparator());
        return tmp;
    }

    /**
     * This method decides if a content description chunk is needed in order to
     * store selected values of <code>tag</code>.<br>
     * The selected values are: <br> {@link Tag#getTitle()}<br> {@link Tag#getComment()}<br>
     * {@link Tag#getArtist()}<br> {@link AsfTag#getCopyright()}<br>
     * {@link AsfTag#getRating()}<br>
     * 
     * @param tag
     *            Tag which should be written.
     * @return <code>true</code>, if a property chunk must be written in order
     *         to store all needed values of tag.
     */
    private boolean isContentdescriptionMandatory(AsfTag tag)
    {
        // TODO Create Unit tests which will show, that ContentDescriptions will
        // disappear if no value provided
        // and exist if at least one value is provided.
        return !Utils.isBlank(tag.getFirstArtist()) || !Utils.isBlank(tag.getFirstComment()) || !Utils.isBlank(tag.getFirstTitle()) || !Utils.isBlank(tag.getFirstCopyright()) || !Utils.isBlank(tag.getFirstRating());
    }

    /**
     * This method decides if an extended property chunk is needed in order to
     * store selected values of <code>tag</code>.<br>
     * 
     * @param tag
     *            Tag which should be written.
     * @return <code>true</code>, if an extended property chunk must be written
     *         in order to store tag values.
     */
    private boolean isExtendedContentDescriptionMandatory(Tag tag)
    {
        /*
         * last changes store all values in extended content description. Even
         * those which are normally stored in the content description chunk. The
         * content description chunk is still provided for legacy applications.
         */
        return !tag.isEmpty();
    }

    /**
     * This method writes the lower 16 Bit of <code>value</code> to raf. <br>
     * 
     * @param value
     *            value
     * @param raf
     *            output
     * @return number of bytes written (Here always 2)
     * @throws IOException
     *             Write errrors.
     */
    private int write16UINT(long value, RandomAccessFile raf) throws IOException
    {
        raf.write(Utils.getBytes(value, 2));
        return 2;
    }

    /**
     * This method writes the lower 32 Bit of <code>value</code> to raf. <br>
     * 
     * @param value
     *            value
     * @param raf
     *            output
     * @return number of bytes written (Here always 4)
     * @throws IOException
     *             Write errrors.
     */
    private int write32UINT(long value, RandomAccessFile raf) throws IOException
    {
        raf.write(Utils.getBytes(value, 4));
        return 4;
    }

    /**
     * Writes the tag into <code>rafTemp</code>.<br>
     * First the file <code>raf</code> is read as an asf file. If it could be
     * interpreted correctly it should be possible to properly write the
     * modifications.
     * 
     * @see org.jaudiotagger.audio.generic.AudioFileWriter#writeTag(org.jaudiotagger.audio.Tag,
     *      java.io.RandomAccessFile,java.io.RandomAccessFile)
     */
    protected void writeTag(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        try
        {
            AsfHeader header = AsfHeaderReader.readHeader(raf);
            if (header == null)
            {
                throw new NullPointerException("Header is null, so file couldn't be read properly. " + "(Interpretation of data, not file access rights.)");
            }
            createModifiedCopy(new AsfTag(tag, true), header, raf, rafTemp, false);
        } catch (IOException ioe)
        {
            throw ioe;
        } catch (Exception cre)
        {
            throw new CannotWriteException("Cannot modify tag because exception occured:\n   " + cre.getMessage());
        }
    }

}