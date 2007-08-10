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
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.audio.ogg.util.OggCRCFactory;
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
        //1st page:Identification Header
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        raf.seek(0);
        //write 1st page (unchanged) and ensure at end of data
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0,
            pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes((int) (pageHeader.getPageLength() + raf.getFilePointer()));

        //2nd page:comment
        OggPageHeader secondPageHeader = OggPageHeader.read (raf);

        //Store the end of 2nd Page header
        long secondPageEndPos = raf.getFilePointer();

        //Compute old comment length
        raf.seek(0);
        int oldCommentLength  = reader.readOggVorbisRawSize(raf);

        //Convert the OggVorbisComment header to raw packet data
        ByteBuffer newComment = tc.convert(tag);

        //Calculate size of new 2nd page
        int newCommentLength = newComment.capacity();
        int newSecondPageLength = secondPageHeader.getPageLength() - oldCommentLength + newCommentLength;
        byte[] segmentTable = createSegmentTable(oldCommentLength, newCommentLength, secondPageHeader);
        int newSecondPageHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;

        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        //System.err.println("Page size: "+secondPageHeader.getPageLength());
        //System.err.println("Old comment: "+oldCommentLength);
        //System.err.println("New comment: "+newCommentLength);
        //System.err.println("New page length: "+(secondPageHeader.getPageLength()-oldCommentLength+newCommentLength));

        //Build the new 2nd page header, can mostly be taken from the original upto the segment length
        //OggS capture
        secondPageBuffer.put(secondPageHeader.getRawHeaderData(),0,OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);

        if (segmentTable.length > OggPageHeader.MAXIMUM_SEGMENT_SIZE)
        {
            throw new CannotWriteException("In this special case we need to "
                + "create a new page, since we still hadn't the time for that "
                + "we won't write because it wouldn't create an ogg file.");
        }
        //Number of page Segments
        secondPageBuffer.put((byte) segmentTable.length);
        //Page segment table
        for (int i = 0; i < segmentTable.length; i++)
        {
            secondPageBuffer.put(segmentTable[i]);
        }
        //Add New VorbisComment
        secondPageBuffer.put(newComment);

        //Add the next packet if exists on this page
        raf.seek(secondPageEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);

        //CRC should be zero before calculating it
        secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS,(byte) 0);
        secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + 1,(byte) 0);
        secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + 2,(byte) 0);
        secondPageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + 3,(byte) 0);

        //Compute CRC over the new second page
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++)
        {
            secondPageBuffer.put( OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
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
     * This method creates a new segment table for the second page (header).
     *
     * @param oldCommentLength The lentgh of the old comment section, used to verify
     *                         the old secment table
     * @param newCommentLength Of this value the start of the segment table
     *                         will be created.
     * @param secondPage       The second page from the source file.
     * @return new segment table.
     */
    private byte[] createSegmentTable(int oldCommentLength, int newCommentLength, OggPageHeader secondPage)
    {
        int totalLenght = secondPage.getPageLength();
        byte[] restShouldBe = createSegments(totalLenght - oldCommentLength, false);
        byte[] newStart = createSegments(newCommentLength, true);
        byte[] result = new byte[newStart.length + restShouldBe.length];
        System.arraycopy(newStart, 0, result, 0, newStart.length);
        System.arraycopy(restShouldBe, 0, result, newStart.length, restShouldBe.length);
        return result;
    }

    /**
     * This method Creates a byte array of values whose sum should
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
        byte[] result = new byte[length / 255 + ((length % 255 == 0 && !quitStream) ? 0 : 1)];
        int i = 0;
        for (; i < result.length - 1; i++)
        {
            result[i] = (byte) 0xFF;
        }
        result[result.length - 1] = (byte) (length - (i * 255));
        return result;
    }


}