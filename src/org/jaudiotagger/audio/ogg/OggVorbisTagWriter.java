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
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.nio.*;
import java.util.logging.Logger;

/**
 * Write Vorbis Tag within an ogg
 * <p/>
 * VorbisComment holds the tag information within an ogg file
 */
public class OggVorbisTagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

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
        logger.info("Starting to write file:");

        //1st Page:Identification Header
        logger.fine("Read identificationHeader:");
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        raf.seek(0);

        //Write 1st page (unchanged) and place writer pointer at end of data
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0,
            pageHeader.getPageLength() + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);
        rafTemp.skipBytes(pageHeader.getPageLength()
            + OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH
            + pageHeader.getSegmentTable().length);
        logger.fine("Written identificationHeader:");

        //2nd page:Comment and Setup if there is enough room
        logger.fine("Read 2ndpage:");
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
        logger.fine("Old Page size: "  +secondPageHeader.getPageLength());
        logger.fine("Setup Header Size: "+setupHeaderLength);
        logger.fine("Old comment: "    +oldCommentLength);
        logger.fine("New comment: "    +newCommentLength);
        logger.fine("New Page Size: "+newSecondPageLength);

        if(isCommentAndSetupHeaderFitsOnASinglePage(newCommentLength,setupHeaderLength))
        {
             //And if comment and setup header originally fitted on both, the length of the 2nd
             //page must be less than maximum size allowed
             //AND
             //there must be two packets with last being complete because they may have
             //elected to split the data over multiple pages instead of using up whole page - (as long
             //as the last lacing value is 255 they can do this)
             if(
                 (secondPageHeader.getPageLength()<OggPageHeader.MAXIMUM_PAGE_DATA_SIZE)
                 &&
                 (secondPageHeader.getPacketList().size()==2)
                 &&
                 (!secondPageHeader.isLastPacketIncomplete())
               )
             {
                 logger.info("Header and Setup remain on single page:");
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
            //Original 2nd page spanned multiple pages so more work to do
            else
             {
                 logger.info("Header and Setup now on single page:");
                 replaceSecondPageAndRenumberPageSeqs(
                     vorbisHeaderSizes,
                     newCommentLength,
                     newSecondPageLength,
                     secondPageHeader,
                     newComment,
                     raf,
                     rafTemp);
             }
        }
        //Bit more complicated, have to create a load of new pages and renumber audio
        else
        {
              logger.info("Header and Setup remain on multiple page:");
              replacePagesAndRenumberPageSeqs(
                     vorbisHeaderSizes,
                     newCommentLength,
                     secondPageHeader,
                     newComment,
                     raf,
                     rafTemp);
        }
    }

    /**
     * Usually can use this method, previously comment and setup header all fit on page 2
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


    }

    /**
     * Previously comment and/or setup header on a number of pages
     * now can just replace this page fitting al on 2nd page, and renumber subsequent sequence pages
     *
     * @param originalHeaderSizes
     * @param newCommentLength
     * @param newSecondPageLength
     * @param secondPageHeader
     * @param newComment
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    private void replaceSecondPageAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes,
                                                      int newCommentLength,
                                                      int newSecondPageLength,
                                                      OggPageHeader secondPageHeader,
                                                      ByteBuffer newComment,
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



        int pageSequence = secondPageHeader.getPageSequence();

        //Now find the setupheader which is on a different page (or extends over pages) and get it with
        //page header stripped out (it has already been worked it will all fir in so should be no buffer overflow)
        byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(),raf);
        logger.finest(setupHeaderData.length + ":"+ secondPageBuffer.position() + ":"+ secondPageBuffer.capacity() );
        secondPageBuffer.put(setupHeaderData);

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
        logger.finest("About to read Audio starting from:"+startAudio);
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
            throw new CannotWriteException("File written counts dont match, file not written");
        }
    }

    /**
     * CommentHeader extends over multiple pages
     *
     * @param originalHeaderSizes
     * @param newCommentLength
     * @param secondPageHeader
     * @param newComment
     * @param raf
     * @param rafTemp
     *
     * @throws IOException
     * @throws CannotReadException
     * @throws CannotWriteException
     */
    private void replacePagesAndRenumberPageSeqs(OggVorbisTagReader.OggVorbisHeaderSizes originalHeaderSizes,
                                                 int newCommentLength,
                                                 OggPageHeader secondPageHeader,
                                                 ByteBuffer newComment,
                                                 RandomAccessFile raf,
                                                 RandomAccessFile rafTemp) throws IOException,CannotReadException,CannotWriteException
    {
        int pageSequence = secondPageHeader.getPageSequence();

        //We need to work out how to split the newcommentlength over the pages
        int noOfPagesNeededForComment = newCommentLength / OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.info("Comment requires:"+noOfPagesNeededForComment+" complete pages");
        //Create the Pages
        int newCommentOffset=0;
        for(int i=0;i< noOfPagesNeededForComment;i++)
        {
            //Create ByteBuffer for the New page
            byte[] segmentTable   = this.createSegments(OggPageHeader.MAXIMUM_PAGE_DATA_SIZE,false);
            int pageHeaderLength  = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer pageBuffer = ByteBuffer.allocate(pageHeaderLength + OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            pageBuffer.order(ByteOrder.LITTLE_ENDIAN);

            //Now create the page basing it on the existing 2ndpageheader
            pageBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            //Number of page Segments
            pageBuffer.put((byte) segmentTable.length);
            //Page segment table
            for (int j = 0; j < segmentTable.length; j++)
            {
               pageBuffer.put(segmentTable[j]);
            }
            //Put in first bit of Comment
            pageBuffer.put(newComment.array(),newCommentOffset,OggPageHeader.MAXIMUM_PAGE_DATA_SIZE);
            //Recalculate Page Sequence Number
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS,pageSequence);
            pageSequence++;


            //Set Header Flag to indicate continous (except for first flag)
            if(i!=0)
            {
                pageBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS,OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());
            }

            //CRC should be zero before calculating it
            pageBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

            //Compute CRC over the page
            byte[] crc = OggCRCFactory.computeCRC(pageBuffer.array());
            for (int j = 0; j < crc.length; j++)
            {
                pageBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + j, crc[j]);
            }

            pageBuffer.rewind();
            rafTemp.getChannel().write(pageBuffer);
            newCommentOffset+=OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        }


        int lastPageCommentPacketSize = newCommentLength % OggPageHeader.MAXIMUM_PAGE_DATA_SIZE;
        logger.fine("Last comment packet size:"+lastPageCommentPacketSize );
        if(!isCommentAndSetupHeaderFitsOnASinglePage(lastPageCommentPacketSize,originalHeaderSizes.getSetupHeaderSize()))
        {
            logger.fine("Spread over two pages");
            //We need to spread over two pages

            //This page contains last bit of comment header and part of setup header
            byte[] commentSegmentTable = createSegments(lastPageCommentPacketSize, true);

            //Cant just add minus the packet data from the max packet size because space available to header affected
            //by size of last comment segment. i.e if it is 1 then 254 is not avilable from the max size
            int remainingSegmentSlots = OggPageHeader.MAXIMUM_NO_OF_SEGMENT_SIZE  - commentSegmentTable.length;
            int firstHalfOfHeaderSize = remainingSegmentSlots * OggPageHeader.MAXIMUM_SEGMENT_SIZE;
            byte[] firstHalfofSegmentHeaderTable = createSegments(firstHalfOfHeaderSize,false);

            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize,firstHalfOfHeaderSize);
            int lastCommentHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH
                + commentSegmentTable.length
                + firstHalfofSegmentHeaderTable.length;

            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastCommentHeaderLength
                + lastPageCommentPacketSize
                + firstHalfOfHeaderSize);

            //Build the last comment page header
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);

            //Number of page Segments
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            //Page segment table
            for (int i = 0; i < segmentTable.length; i++)
            {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            //Add last bit of Comment
            lastCommentHeaderBuffer.put(newComment.array(),newCommentOffset,lastPageCommentPacketSize);

            //Now find the setupheader which is on a different page (or extends over pages) and get it with
            //page header stripped out
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(),raf);
            logger.finest(setupHeaderData.length + ":"+ lastCommentHeaderBuffer.position() + ":"+ lastCommentHeaderBuffer.capacity() );
            int copyAmount = setupHeaderData.length;
            if( setupHeaderData.length>lastCommentHeaderBuffer.remaining())
            {
                copyAmount = lastCommentHeaderBuffer.remaining();
                logger.finest("Copying :"+ copyAmount);
            }
            lastCommentHeaderBuffer.put(setupHeaderData,0,copyAmount);

            //Page Sequence No
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS,pageSequence);

            //Continuous
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS,OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());

            pageSequence++;

            //CRC should be zero before calculating it
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

            //Compute CRC over the new second page
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++)
            {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }

            //Transfer the second page bytebuffer content and write to temp file
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);

            //This page contains the remainder of the setup header -----------------------------------------------
            int secondHalfOfHeaderSize =
                originalHeaderSizes.getSetupHeaderSize() - firstHalfOfHeaderSize;
            segmentTable = createSegmentTable(secondHalfOfHeaderSize,0);
            int lastSetupHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastSetupHeaderBuffer = ByteBuffer.allocate(lastSetupHeaderLength
                + secondHalfOfHeaderSize);


            //Build the last header page
            lastSetupHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastSetupHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);

            //Number of page Segments
            lastSetupHeaderBuffer.put((byte) segmentTable.length);
            //Page segment table
            for (int i = 0; i < segmentTable.length; i++)
            {
                lastSetupHeaderBuffer.put(segmentTable[i]);
            }

            //Add last bit of Setup header should match remaining buffer size
            logger.finest(setupHeaderData.length - copyAmount + ":"+ lastSetupHeaderBuffer.position() + ":"+ lastSetupHeaderBuffer.capacity() );

            //Copy remainder of setupheader
            lastSetupHeaderBuffer.put(setupHeaderData,copyAmount,setupHeaderData.length - copyAmount);

            //Page Sequence No
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS,pageSequence);

            //Continuous
            lastSetupHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS,OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());


            //CRC should be zero before calculating it
            lastSetupHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

            //Compute CRC over the new second page
            crc = OggCRCFactory.computeCRC(lastSetupHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++)
            {
                lastSetupHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }

            //Transfer the second page bytebuffer content and write to temp file
            lastSetupHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastSetupHeaderBuffer);
        }
        //End of Comment and SetupHeader can fit on one page
        else
        {
            logger.fine("Setupheader fits on comment page");
            byte[] segmentTable = createSegmentTable(lastPageCommentPacketSize, originalHeaderSizes.getSetupHeaderSize());
            int lastHeaderLength = OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + segmentTable.length;
            ByteBuffer lastCommentHeaderBuffer = ByteBuffer.allocate(lastHeaderLength
                + lastPageCommentPacketSize
                + originalHeaderSizes.getSetupHeaderSize());

            //Build the last comment page header base on original secondpageheader
            //OggS capture
            lastCommentHeaderBuffer.put(secondPageHeader.getRawHeaderData(), 0, OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH - 1);
            lastCommentHeaderBuffer.order(ByteOrder.LITTLE_ENDIAN);

            //Number of page Segments
            lastCommentHeaderBuffer.put((byte) segmentTable.length);
            //Page segment table
            for (int i = 0; i < segmentTable.length; i++)
            {
                lastCommentHeaderBuffer.put(segmentTable[i]);
            }
            //Add last bit of Comment
            lastCommentHeaderBuffer.put(newComment.array(),newCommentOffset,lastPageCommentPacketSize);


            //Now find the setupheader which is on a different page
            raf.seek(originalHeaderSizes.getSetupHeaderStartPosition());
            OggPageHeader setupPageHeader;




            //Add setup Header (although it will fit in this page, it may be over multiple pages in its original form
            //so need to use this function to convert to raw data 
            byte[] setupHeaderData = reader.convertToVorbisSetupHeaderPacket(originalHeaderSizes.getSetupHeaderStartPosition(),raf);
            lastCommentHeaderBuffer.put(setupHeaderData);

            //Page Sequence No
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_SEQUENCE_NO_POS,pageSequence);

            //Set Header Flag to indicate continuous (except for first flag)
            lastCommentHeaderBuffer.put(OggPageHeader.FIELD_HEADER_TYPE_FLAG_POS,OggPageHeader.HeaderTypeFlag.CONTINUED_PACKET.getFileValue());

            //CRC should be zero before calculating it
            lastCommentHeaderBuffer.putInt(OggPageHeader.FIELD_PAGE_CHECKSUM_POS, 0);

            //Compute CRC over the new second page
            byte[] crc = OggCRCFactory.computeCRC(lastCommentHeaderBuffer.array());
            for (int i = 0; i < crc.length; i++)
            {
                lastCommentHeaderBuffer.put(OggPageHeader.FIELD_PAGE_CHECKSUM_POS + i, crc[i]);
            }

            //Transfer the second page bytebuffer content and write to temp file
            lastCommentHeaderBuffer.rewind();
            rafTemp.getChannel().write(lastCommentHeaderBuffer);
        }

        //Write the rest of the original file

        //Now the Page Sequence Number for all the subsequent pages (containing audio frames) are out because there are
        //less pages before then there used to be, so need to adjust
        long startAudio = raf.getFilePointer();
        long startAudioWritten = rafTemp.getFilePointer();
        logger.fine("Writing audio, audio starts in original file at :"+startAudio+":Written to:"+startAudioWritten);
        while(raf.getFilePointer()<raf.length())
        {
            logger.fine("Reading Ogg Page");
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
            byte[] crc = OggCRCFactory.computeCRC(nextPageHeaderBuffer.array());
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
     * @param setupHeaderLength The length of setup table, zero if comment String extends
     * over multiple pages and this is not the last page.
     * @return new segment table.
     */
    private byte[] createSegmentTable(int newCommentLength, int setupHeaderLength)
    {

        byte[] newStart;

        if( setupHeaderLength==0)
        {
            //Comment Stream continues onto next page so last lacing value can be 255
            newStart=createSegments(newCommentLength, false);
            return newStart;
        }
        else
        {
             //Comment Stream finishes on this page so if is a multiple of 255
             //have to add an extra entry.
             newStart=createSegments(newCommentLength, true);
        }

        //TODO Why false, what about is header length fixed, cvant seto to true because may go onto
        //next page.
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
     *
     * TODO if pass is data of max length (65025 bytes) and have quitStream==true
     * this will return 256 segments which is illegal, should be checked somewhere
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

    /**
     *
     * @param commentLength
     * @param setupHeaderLength
     * @return true if there is enough room to fit the comment and the setup headers on one page taking into
     * account the maximum no of segment s allowed per page and zero lacing values.
     */
    private boolean isCommentAndSetupHeaderFitsOnASinglePage(int commentLength,int setupHeaderLength)
    {
        int additionalZeroLacingRequiredForComment=0;
        int additionalZeroLacingRequiredForHeader=0;

        if((commentLength % OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0))
        {
            additionalZeroLacingRequiredForComment=1;
        }
        if(setupHeaderLength % OggPageHeader.MAXIMUM_SEGMENT_SIZE == 0)
        {
            additionalZeroLacingRequiredForHeader=1;
        }

        if( ((commentLength / OggPageHeader.MAXIMUM_SEGMENT_SIZE)
            +
            (setupHeaderLength / OggPageHeader.MAXIMUM_SEGMENT_SIZE)
            + additionalZeroLacingRequiredForComment
            + additionalZeroLacingRequiredForHeader
        )
            <=OggPageHeader.MAXIMUM_NO_OF_SEGMENT_SIZE
        )
        {
            return true;
        }
        return false;
    }

}