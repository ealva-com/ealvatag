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
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.util.List;

/**
 * Read Vorbis Comment Tag within ogg
 *
 * Vorbis is the audiostream within an ogg file, Vorbis uses VorbisComments as its tag
 */
public class OggVorbisTagReader
{
    private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();

    /**
     * Read the Logical VorbisComment Tag from the file
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        byte[] rawVorbisCommentData = readRawPacketData(raf);
        //Begin tag reading
        VorbisCommentTag tag = vorbisCommentReader.read(rawVorbisCommentData);

        //Check framing bit, only exists when vorbisComment used within OggVorbis
        //TODO, do we need to check first bit of byte rather than just byte generally
        if (rawVorbisCommentData[rawVorbisCommentData.length-1]==0)
        {
            throw new CannotReadException("Error: The OGG Stream isn't valid, Vorbis tag valid flag is wrong");
        }
        //System.err.println("CompletedReadCommentTag");
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
     * @throws CannotReadException
     * @throws IOException
     */
    public byte[] readRawPacketData(RandomAccessFile raf) throws CannotReadException, IOException
    {
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

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
        String vorbis = new String(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH);
        if (headerData[VorbisHeader.FIELD_PACKET_TYPE_POS] != VorbisPacketType.COMMENT_HEADER.getType()
            || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN))
        {
            return false;
        }
        return true;
    }

     public boolean isVorbisSetupHeader (byte[] headerData)
    {
        String vorbis = new String(headerData, VorbisHeader.FIELD_CAPTURE_PATTERN_POS, VorbisHeader.FIELD_CAPTURE_PATTERN_LENGTH);
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
            return baos.toByteArray();
        }

        //There is only the VorbisComment packet on page if it has completed on this page we can return
        if(!startVorbisCommentPage.isLastPacketIncomplete())
        {
            return baos.toByteArray();
        }

        //The VorbisComment extends to the next page, so should be at end of page already
        //so carry on reading pages until we get to the end of comment
        while(true)
        {
            OggPageHeader nextPageHeader = OggPageHeader.read (raf);
            b = new byte[ nextPageHeader.getPacketList().get(0).getLength()];
            raf.read(b);
            baos.write(b);

            //Because there is at least one other packet (SetupHeaderPacket) this means the Comment Packet has finished
            //on this page so thats all we need and we can return
            if(nextPageHeader.getPacketList().size()>1)
            {
                return baos.toByteArray();
            }

            //There is only the VorbisComment packet on page if it has completed on this page we can return
            if(!nextPageHeader.isLastPacketIncomplete())
            {
                return baos.toByteArray();
            }
        }
    }

    /** Calculate the size of the pacvket data for the comment and setup headers
     *
     * @param raf
     * @return
     * @throws CannotReadException
     * @throws IOException
     */
    public OggVorbisHeaderSizes readOggVorbisHeaderSizes(RandomAccessFile raf) throws CannotReadException, IOException
    {
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

        //Calculate Comment Size (not inc header)
        while(true)
        {
            List<OggPageHeader.PacketStartAndLength> packetList = pageHeader.getPacketList();
            commentHeaderSize+=packetList.get(0).getLength();
            raf.skipBytes(packetList.get(0).getLength());

            if(packetList.size()>1 ||!pageHeader.isLastPacketIncomplete())
            {
                //done comment size
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
        setupHeaderStartPosition = raf.getFilePointer()
                   - (OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageHeader.getSegmentTable().length)
                   - packetList.get(0).getLength();


        setupHeaderSize=packetList.get(1).getLength();
        raf.skipBytes(packetList.get(1).getLength());

        if(!pageHeader.isLastPacketIncomplete())
        {
            //done setup size
        }
        else
        {
            pageHeader = OggPageHeader.read (raf);
            while(true)
            {
                setupHeaderSize+=packetList.get(0).getLength();
                raf.skipBytes(packetList.get(0).getLength());
                if(pageHeader.isLastPacketIncomplete())
                {
                    //done setup size
                    break;
                }
                pageHeader = OggPageHeader.read (raf);
            }
        }

        raf.seek(filepointer);
        return new OggVorbisHeaderSizes(commentHeaderStartPosition,setupHeaderStartPosition,commentHeaderSize,setupHeaderSize);
    }

    /**
     * Find the length of the raw packet data for the two OggVorbisHeader we need to know
     * about when writing data (sizes included vorbis header)
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

        public int getCommentHeaderSize()
        {
            return commentHeaderSize;
        }

        public int getSetupHeaderSize()
        {
            return setupHeaderSize;
        }


        public long getCommentHeaderStartPosition()
        {
            return commentHeaderStartPosition;
        }

        public long getSetupHeaderStartPosition()
        {
            return setupHeaderStartPosition;
        }
    }
}

