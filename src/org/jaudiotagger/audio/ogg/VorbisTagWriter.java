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
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.audio.ogg.VorbisTagReader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.audio.ogg.util.OggCRCFactory;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTagCreator;
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.nio.*;

/**
 * Write Vorbis Tag within ogg
 * <p/>
 * Vorbis is the audiostream within an ogg file and uses Vorbis Comments
 */
public class VorbisTagWriter
{

    private VorbisCommentTagCreator tc = new VorbisCommentTagCreator();
    private VorbisTagReader reader = new VorbisTagReader();

    public void delete(RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotWriteException
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

    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        //Read firstPage----------------------------------------------------
        raf.seek(26);
        byte[] b = new byte[4];
        int pageSegments = raf.readByte() & 0xFF; //Unsigned
        raf.seek(0);

        b = new byte[27 + pageSegments];
        raf.read(b);

        OggPageHeader firstPage = new OggPageHeader(b);
        //System.err.println(firstPage.isValid());
        //------------------------------------------------------------------

        raf.seek(0);
        //write 1st page (unchanged)----------------------------------------
        rafTemp.getChannel().transferFrom(raf.getChannel(), 0, firstPage.getPageLength() + 27 + pageSegments);
        rafTemp.skipBytes((int) (firstPage.getPageLength() + raf.getFilePointer()));
        //------------------------------------------------------------------

        //Read 2nd page-----------------------------------------------------
        long pos = raf.getFilePointer();
        raf.seek(raf.getFilePointer() + 26);
        pageSegments = raf.readByte() & 0xFF; //Unsigned
        raf.seek(pos);

        b = new byte[27 + pageSegments];
        raf.read(b);
        OggPageHeader secondPage = new OggPageHeader(b);
        //System.err.println(secondPage.isValid());
        long secondPageEndPos = raf.getFilePointer();
        //------------------------------------------------------------------

        //Compute old comment length----------------------------------------
        int oldCommentLength = 7; // [.vorbiscomment]
        raf.seek(raf.getFilePointer() + 7);

        b = new byte[4];
        raf.read(b);
        int vendorStringLength = Utils.getNumberLittleEndian(b, 0, 3);
        oldCommentLength += 4 + vendorStringLength;

        raf.seek(raf.getFilePointer() + vendorStringLength);

        b = new byte[4];
        raf.read(b);
        int userComments = Utils.getNumberLittleEndian(b, 0, 3);
        oldCommentLength += 4;

        for (int i = 0; i < userComments; i++)
        {
            b = new byte[4];
            raf.read(b);
            int commentLength = Utils.getNumberLittleEndian(b, 0, 3);
            oldCommentLength += 4 + commentLength;

            raf.seek(raf.getFilePointer() + commentLength);
        }

        byte isValid = raf.readByte();
        oldCommentLength += 1;
        if (isValid != 1)
        {
            throw new CannotWriteException("Unable to retreive old tag informations");
        }
        //System.err.println("oldCommentLength: "+oldCommentLength);
        //------------------------------------------------------------------

        //Get the new comment and create the container bytebuffer for 2nd page---
        ByteBuffer newComment = tc.convert(tag);

        int newCommentLength = newComment.capacity();
        int newSecondPageLength = secondPage.getPageLength() - oldCommentLength + newCommentLength;

//		byte pageSegmentsNb = (byte) (newSecondPageLength / 255 + 1);
        byte[] segmentTable = createSegmentTable(oldCommentLength, newCommentLength, secondPage);
        int newSecondPageHeaderLength = 27 + segmentTable.length;

        ByteBuffer secondPageBuffer = ByteBuffer.allocate(newSecondPageLength + newSecondPageHeaderLength);
        //System.err.println("Page size: "+secondPage.getPageLength());
        //System.err.println("Old comment: "+oldCommentLength);
        //System.err.println("New comment: "+newCommentLength);
        //System.err.println("New page length: "+(secondPage.getPageLength()-oldCommentLength+newCommentLength));
        //------------------------------------------------------------------

        //Build the new second page header----------------------------------
        //OggS capture
        b = new String("OggS").getBytes();
        secondPageBuffer.put(b);
        //Stream struct revision
        secondPageBuffer.put((byte) 0);
        //header_type_flag
        secondPageBuffer.put((byte) 0);
        //absolute granule position
        secondPageBuffer.put(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0});

        //stream serial number
        int serialNb = secondPage.getSerialNumber();
        //System.err.println(serialNb);
        b = new byte[4];
        b[3] = (byte) ((serialNb & 0xFF000000) >> 24);
        b[2] = (byte) ((serialNb & 0x00FF0000) >> 16);
        b[1] = (byte) ((serialNb & 0x0000FF00) >> 8);
        b[0] = (byte) (serialNb & 0x000000FF);
        secondPageBuffer.put(b);

        //page sequence no
        int seqNb = secondPage.getPageSequence();
        //System.err.println(seqNb);
        b = new byte[4];
        b[3] = (byte) ((seqNb & 0xFF000000) >> 24);
        b[2] = (byte) ((seqNb & 0x00FF0000) >> 16);
        b[1] = (byte) ((seqNb & 0x0000FF00) >> 8);
        b[0] = (byte) (seqNb & 0x000000FF);
        secondPageBuffer.put(b);

        //CRC (to be computed later)
        secondPageBuffer.put(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0});
        int crcOffset = 22;

        if (segmentTable.length > 255)
        {
            throw new CannotWriteException("In this special case we need to " + "create a new page, since we still hadn't the time for that " + "we won't write because it wouldn't create an ogg file.");
        }
        //page_segments nb.
        secondPageBuffer.put((byte) segmentTable.length);

        //page segment table
        for (int i = 0; i < segmentTable.length; i++)
        {
            secondPageBuffer.put(segmentTable[i]);
        }
        //------------------------------------------------------------------

        //Add to the new second page the new comment------------------------
        secondPageBuffer.put(newComment);
        //------------------------------------------------------------------

        //Add the remaining old second page (encoding infos, etc)-----------
        raf.seek(secondPageEndPos);
        raf.skipBytes(oldCommentLength);
        raf.getChannel().read(secondPageBuffer);
        //------------------------------------------------------------------

        //Compute CRC over the new second page------------------------------
        byte[] crc = OggCRCFactory.computeCRC(secondPageBuffer.array());
        for (int i = 0; i < crc.length; i++)
        {
            //System.err.println("Offset: "+(crcOffset+i) +"|"+crc[i]);
            secondPageBuffer.put(crcOffset + i, crc[i]);
            //System.err.println(secondPageBuffer.get(crcOffset+i));
        }
        //------------------------------------------------------------------

        //Transfer the second page bytebuffer content-----------------------
        secondPageBuffer.rewind();
        rafTemp.getChannel().write(secondPageBuffer);
        //rafTemp.skipBytes( newSecondPageLength+newSecondPageHeaderLength );
        //------------------------------------------------------------------

        //Write the rest of the original file-------------------------------
        rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getFilePointer(), raf.length() - raf.getFilePointer());
        //------------------------------------------------------------------
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
     * This method easily creates a byte Array of values whose sum should
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