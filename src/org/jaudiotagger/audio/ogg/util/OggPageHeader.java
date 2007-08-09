/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
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
package org.jaudiotagger.audio.ogg.util;

import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.Arrays;


/**
 * $Id$
 *
 * reference:http://xiph.org/ogg/doc/framing.html
 *
 * @author Raphael Slinckx (KiKiDonK)
 * @version 16 décembre 2003
 */
public class OggPageHeader
{
    //Capture pattern at start of header
    public static final byte[] CAPTURE_PATTERN = {'O', 'g', 'g','S'};

    //Ogg Page header is always 27 bytes plus the size of the segment table which is variable
    public static final int OGG_PAGE_HEADER_FIXED_LENGTH = 27;
    public static final int FIELD_CAPTURE_PATTERN_POS  = 0;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_POS  = 4;
    public static final int FIELD_HEADER_TYPE_FLAG_POS  = 5;
    public static final int FIELD_ABSOLUTE_GRANULE_POS  = 6;
    public static final int FIELD_STREAM_SERIAL_NO_POS  = 14;
    public static final int FIELD_PAGE_SEQUENCE_NO_POS  = 18;
    public static final int FIELD_PAGE_CHECKSUM_POS     = 22;
    public static final int FIELD_PAGE_SEGMENTS_POS     = 26;
    public static final int FIELD_SEGMENT_TABLE_POS     = 27;

    public static final int FIELD_CAPTURE_PATTERN_LENGTH  = 4;
    public static final int FIELD_STREAM_STRUCTURE_VERSION_LENGTH  = 1;
    public static final int FIELD_HEADER_TYPE_FLAG_LENGTH  = 1;
    public static final int FIELD_ABSOLUTE_GRANULE_LENGTH  = 8;
    public static final int FIELD_STREAM_SERIAL_NO_LENGTH  = 4;
    public static final int FIELD_PAGE_SEQUENCE_NO_LENGTH  = 4;
    public static final int FIELD_PAGE_CHECKSUM_LENGTH     = 4;
    public static final int FIELD_PAGE_SEGMENTS_LENGTH     = 1;

    private double absoluteGranulePosition;
    private byte[] checksum;
    private byte headerTypeFlag;

    private boolean isValid = false;
    private int pageLength = 0;
    private int pageSequenceNumber, streamSerialNumber;
    private byte[] segmentTable;

    public static OggPageHeader read (RandomAccessFile raf) throws IOException,CannotReadException
    {
        long start = raf.getFilePointer();

        byte[] b = new byte[OggPageHeader.CAPTURE_PATTERN.length];
        raf.read(b);
        if (!(Arrays.equals(b, OggPageHeader.CAPTURE_PATTERN)))
        {
            throw new CannotReadException("OggS Header could not be found, not an ogg stream");
        }

        raf.seek(start + OggPageHeader.FIELD_PAGE_SEGMENTS_POS);
        int pageSegments = raf.readByte() & 0xFF; //unsigned
        raf.seek(start);

        b = new byte[OggPageHeader.OGG_PAGE_HEADER_FIXED_LENGTH + pageSegments];
        raf.read(b);

        OggPageHeader pageHeader = new OggPageHeader(b);

        //Now just after PageHeader, ready for Packet Data
        return pageHeader;
    }

    public OggPageHeader(byte[] b)
    {
        //System.err.println(new String(b, 0 , 4));
        int streamStructureRevision = b[FIELD_STREAM_STRUCTURE_VERSION_POS];
        //System.err.println("streamStructureRevision: " + streamStructureRevision);
        headerTypeFlag = b[FIELD_HEADER_TYPE_FLAG_POS];
        //System.err.println("headerTypeFlag: " + headerTypeFlag);
        if (streamStructureRevision == 0)
        {
            this.absoluteGranulePosition = 0;  //b[6] + (b[7]<<8) + (b[8]<<16) + (b[9]<<24) + (b[10]<<32) + (b[11]<<40) + (b[12]<<48) + (b[13]<<56);
            for (int i = 0; i < 8; i++)
            {
                this.absoluteGranulePosition += u(b[i + 6]) * Math.pow(2, 8 * i);
            }

            streamSerialNumber = u(b[14]) + (u(b[15]) << 8) + (u(b[16]) << 16) + (u(b[17]) << 24);
            //System.err.println("streamSerialNumber: " + streamSerialNumber);
            pageSequenceNumber = u(b[18]) + (u(b[19]) << 8) + (u(b[20]) << 16) + (u(b[21]) << 24);
            //System.err.println("pageSequenceNumber: " + pageSequenceNumber);
            checksum = new byte[]{b[22], b[23], b[24], b[25]};

            //int pageSegments = u( b[26] );
            //System.err.println("pageSegments: " + pageSegments);

            this.segmentTable = new byte[b.length - OGG_PAGE_HEADER_FIXED_LENGTH];
            //System.err.println("pagesegment length; "+ (b.length-27));
            for (int i = 0; i < segmentTable.length; i++)
            {
                segmentTable[i] = b[OGG_PAGE_HEADER_FIXED_LENGTH + i];
                this.pageLength += u(segmentTable[i]);
                //System.err.println("acc page length: "+this.pageLength);
                //System.err.println(segmentTable[i]);
            }

            isValid = true;
        }
    }

    private int u(int i)
    {
        return i & 0xFF;
    }


    public double getAbsoluteGranulePosition()
    {
        //System.err.println("Number Of Samples: "+absoluteGranulePosition);
        return this.absoluteGranulePosition;
    }


    public byte[] getCheckSum()
    {
        return checksum;
    }


    public byte getHeaderType()
    {
        return headerTypeFlag;
    }


    public int getPageLength()
    {
        //System.err.println("This page length: "+pageLength);
        return this.pageLength;
    }

    public int getPageSequence()
    {
        return pageSequenceNumber;
    }

    public int getSerialNumber()
    {
        return streamSerialNumber;
    }

    public byte[] getSegmentTable()
    {
        return this.segmentTable;
    }

    public boolean isValid()
    {
        return isValid;
    }

    public String toString()
    {
        String out = "Ogg Page Header:\n";

        out += "Is valid?: " + isValid + " | page length: " + pageLength + "\n";
        out += "Header type: " + headerTypeFlag;
        return out;
    }

    enum HeaderTypeFlag
    {
        CONTINUED,
        FIRST_PAGE,
        LAST_PAGE,
    }
}

