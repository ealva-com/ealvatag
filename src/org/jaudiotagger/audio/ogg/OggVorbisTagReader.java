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

import org.jaudiotagger.tag.vorbiscomment.VorbisCommentReader;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.audio.ogg.util.VorbisHeader;
import org.jaudiotagger.audio.ogg.util.VorbisPacketType;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Read Vorbis Comment Tag within ogg
 *
 * Vorbis is the audiostream within an ogg file, Vorbis uses VorbisComments as its tag
 */
public class OggVorbisTagReader
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.ogg");

    private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();

    /**
     * Read the Logical VorbisComment Tag from the file
     *
     * <p>Read the CommenyTag, within an OggVorbis file the VorbisCommentTag is mandatory 
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.info("Starting to read ogg vorbis tag from file:");
        byte[] rawVorbisCommentData = readRawPacketData(raf);

        //Begin tag reading
        VorbisCommentTag tag = vorbisCommentReader.read(rawVorbisCommentData,true);
        logger.fine("CompletedReadCommentTag");
        return tag;
    }

    /**
     * Retrieve the Size of the VorbisComment packet including the oggvorbis header
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public int readOggVorbisRawSize(RandomAccessFile raf) throws CannotReadException, IOException
    {
         byte[] rawVorbisCommentData = readRawPacketData(raf);
         return rawVorbisCommentData.length
             + VorbisHeader.FIELD_PACKET_TYPE_LENGTH
             + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH;
    }

    /**
     * Retrieve the raw VorbisComment packet data, does not include the OggVorbis header
     *
     * @param raf
     * @return
     * @throws CannotReadException if unable to find vorbiscomment header
     * @throws IOException
     */
    public byte[] readRawPacketData(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.fine("Read 1st page");
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

        logger.fine("Read 2nd page");
        //2nd page = comment, may extend to additional pages or not , may also have setup header
        pageHeader = OggPageHeader.read (raf);

        //Now at start of packets on page 2 , check this is the vorbis comment header 
        byte [] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if(!isVorbisCommentHeader (b))
        {
            throw new CannotReadException("Cannot find comment block (no vorbiscomment header)");
        }


        //Convert the comment raw data which maybe over many pages back into raw packet
        byte[] rawVorbisCommentData = convertToVorbisCommentPacket(pageHeader,raf);
        return rawVorbisCommentData;
    }



    /**
     * Is this a Vorbis Comment header, check
     *
     * Note this check only applies to Vorbis Comments embedded within an OggVorbis File which is why within here
     *
     * @param headerData
     *
     * @return true if the headerData matches a VorbisComment header i.e is a Vorbis header of type COMMENT_HEADER
     */
    public boolean isVorbisCommentHeader (byte[] headerData)
    {
        String vorbis = Utils.getString(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH,"ISO-8859-1");
        if (headerData[VorbisHeader.FIELD_PACKET_TYPE_POS] != VorbisPacketType.COMMENT_HEADER.getType()
            || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN))
        {
            return false;
        }
        return true;
    }

    /**
     * Is this a Vorbis SetupHeader check
     *
     * @param headerData
     * @return true if matches vorbis setupheader
     */
    public boolean isVorbisSetupHeader (byte[] headerData)
    {
        String vorbis = Utils.getString(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH,"ISO-8859-1");
        if (headerData[VorbisHeader.FIELD_PACKET_TYPE_POS] != VorbisPacketType.SETUP_HEADER.getType()
            || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN))
        {
            return false;
        }
        return true;
    }

    /**
     * The Vorbis Comment may span multiple pages so we we need to identify the pages they contain and then
     * extract the packet data from the pages
     */
    private byte[] convertToVorbisCommentPacket(OggPageHeader startVorbisCommentPage,RandomAccessFile raf)
        throws IOException,CannotReadException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte [] b = new byte[startVorbisCommentPage.getPacketList().get(0).getLength()
            - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH) ];
        raf.read(b);
        baos.write(b);

        //Because there is at least one other packet (SetupHeaderPacket) this means the Comment Packet has finished
        //on this page so thats all we need and we can return
        if(startVorbisCommentPage.getPacketList().size()>1)
        {
            logger.info("Comments finish on 2nd Page because there is another packet on this page");
            return baos.toByteArray();
        }

        //There is only the VorbisComment packet on page if it has completed on this page we can return
        if(!startVorbisCommentPage.isLastPacketIncomplete())
        {
            logger.info("Comments finish on 2nd Page because this packet is complete");
            return baos.toByteArray();
        }

        //The VorbisComment extends to the next page, so should be at end of page already
        //so carry on reading pages until we get to the end of comment
        while(true)
        {
            logger.info("Reading next page");
            OggPageHeader nextPageHeader = OggPageHeader.read (raf);
            b = new byte[ nextPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);

            //Because there is at least one other packet (SetupHeaderPacket) this means the Comment Packet has finished
            //on this page so thats all we need and we can return
            if(nextPageHeader.getPacketList().size()>1)
            {
                logger.info("Comments finish on Page because there is another packet on this page");
                return baos.toByteArray();
            }

            //There is only the VorbisComment packet on page if it has completed on this page we can return
            if(!nextPageHeader.isLastPacketIncomplete())
            {
                logger.info("Comments finish on Page because this packet is complete");
                return baos.toByteArray();
            }
        }
    }

       /**
         * The Vorbis Seup Header may span multiple(2) pages, athough it doesnt normally. We pass the start of the
         * file offset of the OggPage it belongs on, it probably wont be first packet.
         */
        public byte[] convertToVorbisSetupHeaderPacket(long fileOffsetOfStartingOggPage,RandomAccessFile raf)
            throws IOException,CannotReadException
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //Seek to specified offset
            raf.seek(fileOffsetOfStartingOggPage);

            //Read Page
            OggPageHeader setupPageHeader = OggPageHeader.read (raf);

            //Assume that if multiple packets first packet is VorbisComment and second packet
            //is setupheader
            if(setupPageHeader.getPacketList().size()>1)
            {
                raf.skipBytes(setupPageHeader.getPacketList().get(0).getLength());
            }

            //Now should be at start of next packet, check this is the vorbis setup header
            byte[]b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
            raf.read(b);
            if(!isVorbisSetupHeader(b))
            {
                throw new CannotReadException("Unable to find setup header(2), unable to write ogg file");
            }

            //Go back to start of setupheader data
            raf.seek(raf.getFilePointer()
                -(VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));


            //Read data
            if(setupPageHeader.getPacketList().size()>1)
            {
                b = new byte[ setupPageHeader.getPacketList().get(1).getLength()];
                raf.read(b);
                baos.write(b);
            }
            else
            {
                b = new byte[ setupPageHeader.getPacketList().get(0).getLength()];
                raf.read(b);
                baos.write(b);
            }


            //Return Data
             if(!setupPageHeader.isLastPacketIncomplete())
            {
                logger.info("Setupheader finishes on this page");
                return baos.toByteArray();
            }

            //The Setupheader extends to the next page, so should be at end of page already
            //so carry on reading pages until we get to the end of comment
            while(true)
            {
                logger.info("Reading another page");
                OggPageHeader nextPageHeader = OggPageHeader.read (raf);
                b = new byte[ nextPageHeader.getPacketList().get(0).getLength()];
                raf.read(b);
                baos.write(b);

                //Because there is at least one other packet this means the Setupheader Packet has finished
                //on this page so thats all we need and we can return
                if(nextPageHeader.getPacketList().size()>1)
                {
                    logger.info("Setupheader finishes on this page");
                    return baos.toByteArray();
                }

                //There is only the Setupheader packet on page if it has completed on this page we can return
                if(!nextPageHeader.isLastPacketIncomplete())
                {
                    logger.info("Setupheader finish on Page because this packet is complete");
                    return baos.toByteArray();
                }
            }
        }


    /** Calculate the size of the packet data for the comment and setup headers
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public OggVorbisHeaderSizes readOggVorbisHeaderSizes(RandomAccessFile raf) throws CannotReadException, IOException
    {
        logger.fine("Started to read comment and setup header sizes:");
        //Stores filepointers so return file in same state
        long filepointer = raf.getFilePointer();

        long commentHeaderStartPosition=0;
        long setupHeaderStartPosition=0;
        int commentHeaderSize=0;
        int setupHeaderSize=0;
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

        //2nd page = comment, may extend to additional pages or not , may also have setup header
        pageHeader = OggPageHeader.read (raf);
        commentHeaderStartPosition = raf.getFilePointer()
            - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length);

        //Now at start of packets on page 2 , check this is the vorbis comment header
        byte [] b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if(!isVorbisCommentHeader (b))
        {
            throw new CannotReadException("Cannot find comment block (no vorbiscomment header)");
        }
        raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
        logger.fine("Found start of comment header at:"+raf.getFilePointer());

        //Calculate Comment Size (not inc header)
        while(true)
        {
            List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();
            commentHeaderSize+=packetList.get(0).getLength();
            raf.skipBytes(packetList.get(0).getLength());

            if(packetList.size()>1 ||!pageHeader.isLastPacketIncomplete())
            {
                //done comment size
                logger.fine("Found end of comment:size:"+commentHeaderSize+"finishes at:"+raf.getFilePointer());
                break;
            }
            pageHeader = OggPageHeader.read (raf);
        }

        //Calculate setupHeader Size (should be straight afterwards)
        //TODO need to be more robust in case setup starts on next page
        List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();

        //Now at start of next packet , check this is the vorbis setup header
        b = new byte[VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH];
        raf.read(b);
        if(!isVorbisSetupHeader(b))
        {
            throw new CannotReadException("Cannot find vorbis setup header)");
        }
        raf.seek(raf.getFilePointer() - (VorbisHeader.FIELD_PACKET_TYPE_LENGTH + VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH));
        logger.fine("Found start of vorbis setup header at:"+raf.getFilePointer());

        //Set this to the  start of the OggPage that setupheader was found on
        setupHeaderStartPosition = raf.getFilePointer()
                   - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length)
                   - packetList.get(0).getLength();


        //Add packet data to size to the setup header size
        setupHeaderSize=packetList.get(1).getLength();
        logger.fine("Adding:"+packetList.get(1).getLength() + " to setup header size");
        //Skip over the packet data
        raf.skipBytes(packetList.get(1).getLength());

        if(!pageHeader.isLastPacketIncomplete())
        {
            logger.fine("Found end of setupheader:size:"+setupHeaderSize+"finishes at:"+raf.getFilePointer());
            //done setup size
        }
        else
        {
            pageHeader = OggPageHeader.read (raf);
            packetList = pageHeader.getPacketList();
            while(true)
            {
                setupHeaderSize+=packetList.get(0).getLength();
                logger.fine("Adding:"+packetList.get(0).getLength() + " to setup header size");
                raf.skipBytes(packetList.get(0).getLength());
                if(!pageHeader.isLastPacketIncomplete())
                {
                    //done setup size
                    logger.fine("Found end of setupheader:size:"+setupHeaderSize+"finishes at:"+raf.getFilePointer());
                    break;
                }
                pageHeader = OggPageHeader.read (raf);
            }
        }

        raf.seek(filepointer);
        return new OggVorbisHeaderSizes(commentHeaderStartPosition,setupHeaderStartPosition,commentHeaderSize,setupHeaderSize);
    }

    /**
     * Find the length of the raw packet data and the start position of the ogg page header they start in
     * for the two OggVorbisHeader we need to know about when writing data (sizes included vorbis header)
     */
    public static class OggVorbisHeaderSizes
    {
        private long commentHeaderStartPosition;
        private long setupHeaderStartPosition;
        private int commentHeaderSize;
        private int setupHeaderSize;

        OggVorbisHeaderSizes(long commentHeaderStartPosition,long setupHeaderStartPosition,int commentHeaderSize,int setupHeaderSize)
        {
            this.commentHeaderStartPosition=commentHeaderStartPosition;
            this.setupHeaderStartPosition=setupHeaderStartPosition;
            this.commentHeaderSize=commentHeaderSize;
            this.setupHeaderSize=setupHeaderSize;
        }

        /**
         *
         * @return the size of the raw packet data for the vorbis comment header (includes vorbis header)
         */
        public int getCommentHeaderSize()
        {
            return commentHeaderSize;
        }

        /**
         *
         * @return he size of the raw packet data for the vorbis setup header (includes vorbis header)
         */
        public int getSetupHeaderSize()
        {
            return setupHeaderSize;
        }

        /**
         *
         * @return the start position in the file of the ogg header which contains the start of the Vorbis Comment
         */
        public long getCommentHeaderStartPosition()
        {
            return commentHeaderStartPosition;
        }

        /**
         *
         * @return the start position in the file of the ogg header which contains the start of the Setup Header
         */
        public long getSetupHeaderStartPosition()
        {
            return setupHeaderStartPosition;
        }
    }
}

