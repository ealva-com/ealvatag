/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.audio.ogg;

import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.ogg.OggVorbisTagReader;
import org.jaudiotagger.audio.ogg.util.*;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.nio.*;

/**
 * Write Vorbis Tag within an ogg
 * <p/>
 * VorbisComment holds the tag information within an ogg file
 */
public class OggVorbisTagWriter
{

    private OggVorbisCommentTagCreator tc = new OggVorbisCommentTagCreator();
    private OggVorbisTagReader reader = new OggVorbisTagReader();
    private OggFileReader      oggReader = new OggFileReader();
    public void delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotReadException,CannotWriteException
    {
        VorbisCommentTag tag = null;
        try
        {
            tag = (VorbisCommentTag) reader.read(raf);
        }
        catch (CannotReadException e)
        {
            write(new VorbisCommentTag(), raf, tempRaf);
            return;
        }

        VorbisCommentTag emptyTag = new VorbisCommentTag();
        emptyTag.setVendor(tag.getVendor());

        write(emptyTag, raf, tempRaf);
    }

    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotReadException,CannotWriteException, IOException
    {
        //1st Page:Identification Header
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        raf.seek(0);

        //Write 1st page (unchanged) and place writer pointer at end of data
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0,
            pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes(pageHeader.getPageLength()
            + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH
            + pageHeader.getSegmentTable().length);

        //2nd page:Comment and Setup if there is enough room
        OggPageHeader secondPageHeader = OggPageHeader.read (raf);

        //2nd Page:Store the end of Header
        long secondPageHeaderEndPos = raf.getFilePointer();

        //Get header sizes
        raf.seek(0);
        OggVorbisTagReader.OggVorbisHeaderSizes vorbisHeaderSizes = reader.readOggVorbisHeaderSizes(raf);

        //Convert the OggVorbisComment header to raw packet data
        ByteBuffer newComment = tc.convert(tag);

        //Compute new comment length(this may need to be spread over multiple pages)
        int newCommentLength = newComment.capacity();

        //Calculate size of setupheader
        int setupHeaderLength = vorbisHeaderSizes.getSetupHeaderSize();
        int oldCommentLength  = vorbisHeaderSizes.getCommentHeaderSize();
        //Calculate new size of new 2nd page
        int newSecondPageLength = setupHeaderLength + newCommentLength;
        //System.err.println("Old Page size: "  +secondPageHeader.getPageLength());
        //System.err.println("Setup Header Size: "+setupHeaderLength);
        //System.err.println("Old comment: "    +oldCommentLength);
        //System.err.println("New comment: "    +newCommentLength);
        //System.err.println("New Page Size: "+newSecondPageLength);

        //TODO what about if exact match for page size

        //If new 2nd page can fit on both comment and setup header
        if(newSecondPageLength < OggPageHeader.MAXIMUM_PAGE_DATA_SIZE)
        {
             //And If orginal 2nd page could fit on both comment and setup header
             if(secondPageHeader.getPageLength()<OggPageHeader.MAXIMUM_PAGE_DATA_SIZE)
             {
                 replaceSecondPageOnly(
                     oldCommentLength,
                     setupHeaderLength,
                     newCommentLength,
                     newSecondPageLength,
                     secondPageHeader,
                     newComment,
                     secondPageHeaderEndPos,
                     raf,
                     rafTemp);
             }
             //Orginal 2nd page spanned multiple pages so more work to do
            else
             {
                 replaceSecondPageOnly(
                     vorbisHeaderSizes,
                     newCommentLength,
                     newSecondPageLength,
                     secondPageHeader,
                     newComment,
                     secondPageHeaderEndPos,
                     raf,
                     rafTemp);
             }
        }
        else
        {
             throw new CannotWriteException("In this special case we need to "
                + "create a new page, since we still hadn't the time for that "
                + "we won't write because it wouldn't create an ogg file.");
        }
    }

    /**
     * Usually can use this method, previously comment and setup header all fir on page 2
     * and they still do, so just replace this page. And copy further pages as is.
     *
     * @param setupHeaderLength
     * @param oldCommentLength
     * @param newCommentLength
     * @param newSecondPageLength
     * @param secondPageHeader
     * @param newComment
     * @param secondPageHeaderEndPos
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    private void replaceSecondPageOnly(int oldCommentLength, int setupHeaderLength, int newCommentLength, int newSecondPageLength, OggPageHeader secondPageHeader, ByteBuffer newComment, long secondPageHeaderEndPos, RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException
    {
        byte[] segmentTable = createSegmentTable(newCommentLength, setupHeaderLength);
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;

        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);

        //Build the new 2nd page header, can mostly be taken from the original upto the segment length
        //OggS capture
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);

        //Number of page Segments
        secondPageBuffer.put((byte) segmentTable.length);
        //Page segment table
        for (int i = 0; i < segmentTable.length; i++)
        {
            secondPageBuffer.put(segmentTable[i]);
        }
        //Add New VorbisComment
        secondPageBuffer.put(newComment);

        //Add setup Header
        raf.seek(secondPageHeaderEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);

        //CRC should be zero before calculating it
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

        //Compute CRC over the new second page
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++)
        {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }

        //Transfer the second page bytebuffer content and write to temp file
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);

        //Write the rest of the original file
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());

        //System.err.println(tempOGG);
        //System.err.println(f.getAbsolutePath());
    }

    /**
     * Usually can use this method, previously comment and setup header all fir on page 2
     * and they still do, so just replace this page. And copy further pages as is.
     *
      * @param originalHeaderSizes
     * @param newCommentLength
     * @param newSecondPageLength
     * @param secondPageHeader
     * @param newComment
     * @param secondPageHeaderEndPos
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    private void replaceSecondPageOnly(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes,
                                       int newCommentLength,
                                       int newSecondPageLength,
                                       OggPageHeader secondPageHeader,
                                       ByteBuffer newComment,
                                       long secondPageHeaderEndPos,
                                       RandomAccessFile raf,
                                       RandomAccessFile rafTemp) throws IOException,CannotReadException,CannotWriteException
    {
        byte[] segmentTable = createSegmentTable(newCommentLength, originalHeaderSizes.getSetupHeaderSize());
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;

        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);

        //Build the new 2nd page header, can mostly be taken from the original upto the segment length
        //OggS capture
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
        secondPageBuffer.order(ByteOrder.LITTLE_ENDIAN);

        //Number of page Segments
        secondPageBuffer.put((byte) segmentTable.length);
        //Page segment table
        for (int i = 0; i < segmentTable.length; i++)
        {
            secondPageBuffer.put(segmentTable[i]);
        }
        //Add New VorbisComment
        secondPageBuffer.put(newComment);

        //Now find the setupheader which is on a different page
        raf.seek(originalHeaderSizes.getSetupHeaderStartPosition());
        OggPageHeader setupPageHeader;

        int pageSequence = secondPageHeader.getPageSequence();

        setupPageHeader = OggPageHeader.read (raf);
        if(setupPageHeader.getPacketList().size()>1)
        {
            raf.skipBytes(setupPageHeader.getPacketList().get(0).getLength());
            //Now should be at start of next packet , check this is the vorbis setup header
            byte[]b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
            raf.read(b);
            if(!reader.isVorbisSetupHeader(b))
            {
                throw new CannotWriteException("Unable to find setup header(2), unable to write ogg file");
            }
            raf.seek(raf.getFilePointer()
                -(VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
        }

        //Add setup Header
        raf.getChannel().read(secondPageBuffer);

        //CRC should be zero before calculating it
        secondPageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

        //Compute CRC over the new second page
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++)
        {
            secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
        }

        //Transfer the second page bytebuffer content and write to temp file
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);

        //Write the rest of the original file

        //Now the Page Sequence Number for all the subsequent pages (containing audio frames) are out because there are
        //less pages before then there used to be, so need to adjust
        long startAudio = raf.getFilePointer();
        long startAudioWritten = rafTemp.getFilePointer();
        while(raf.getFilePointer()<raf.length())
        {
            OggPageHeader nextPage = OggPageHeader.read (raf);

            //Create buffer large enough foor next page (header and data) and set byte order to LE so we can use
            //putInt method
            ByteBuffer nextPageHeaderBuffer = ByteBuffer.allocate(
                nextPage.getRawHeaderData().length + nextPage.getPageLength());
            nextPageHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);

            nextPageHeaderBuffer.put(nextPage.getRawHeaderData());
            raf.getChannel().read(nextPageHeaderBuffer);

            //Recalculate Page Sequence Number
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS,++pageSequence);

            //CRC should be zero before calculating it
            nextPageHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

            //Compute CRC over the modified page
            crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++)
            {
                nextPageHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }

            nextPageHeaderBuffer.rewind();
            rafTemp.getChannel().write(nextPageHeaderBuffer);
        }
        if((raf.length() - startAudio)!=(rafTemp.length() - startAudioWritten))
        {
            throw new CannotWriteException("File written counts dont macth, file not written");
        }

    }

    /**
     * This method creates a new segment table for the second page (header).
     *
     * @param newCommentLength Of this value the start of the segment table
     *                         will be created.
     * @param setupHeaderLength The length of setup table
     * @return new segment table.
     */
    private byte[] createSegmentTable(int newCommentLength, int setupHeaderLength)
    {
        byte[] newStart = createSegments(newCommentLength, true);
        byte[] restShouldBe = createSegments(setupHeaderLength, false);
        byte[] result = new byte[newStart.length + restShouldBe.length];
        System.arraycopy(newStart, 0, result, 0, newStart.length);
        System.arraycopy(restShouldBe, 0, result, newStart.length, restShouldBe.length);
        return result;
    }

    /**
     * This method creates a byte array of values whose sum should
     * be the value of <code>length</code>.<br>
     *
     * @param length     Size of the page which should be
     *                   represented as 255 byte packets.
     * @param quitStream If true and a length is a multiple of 255 we need another
     *                   segment table entry with the value of 0. Else it's the last stream of the
     *                   table which is already ended.
     * @return Array of packet sizes. However only the last packet will
     *         differ from 255.
     */
    private byte[] createSegments(int length, boolean quitStream)
    {
        byte[] result = new byte[length / OggPageHeader.MAXIMUM_SEGMENT_SIZE
            + ((length %  OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0 && !quitStream) ? 0 : 1)];
        int i = 0;
        for (; i < result.length - 1; i++)
        {
            result[i] = (byte) 0xFF;
        }
        result[result.length - 1] = (byte) (length - (i *  OggPageHeader.MAXIMUM_SEGMENT_SIZE));
        return result;
    }


}