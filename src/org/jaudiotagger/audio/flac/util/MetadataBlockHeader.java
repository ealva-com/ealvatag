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
package org.jaudiotagger.audio.flac.util;

public class MetadataBlockHeader
{

    public final static int STREAMINFO = 0, PADDING = 1, APPLICATION = 2, SEEKTABLE = 3, VORBIS_COMMENT = 4, CUESHEET = 5, UNKNOWN = 6;
    private boolean isLastBlock;
    private int blockType, dataLength;
    private byte[] bytes;

    public MetadataBlockHeader(byte[] b)
    {
        isLastBlock = ((b[0] & 0x80) >>> 7) == 1;

        int type = b[0] & 0x7F;
        switch (type)
        {
            case 0:
                blockType = STREAMINFO;
                break;
            case 1:
                blockType = PADDING;
                break;
            case 2:
                blockType = APPLICATION;
                break;
            case 3:
                blockType = SEEKTABLE;
                break;
            case 4:
                blockType = VORBIS_COMMENT;
                break;
            case 5:
                blockType = CUESHEET;
                break;
            default:
                blockType = UNKNOWN;
        }

        dataLength = (u(b[1]) << 16) + (u(b[2]) << 8) + (u(b[3]));

        bytes = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            bytes[i] = b[i];
        }
    }

    private int u(int i)
    {
        return i & 0xFF;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getBlockType()
    {
        return blockType;
    }

    public String getBlockTypeString()
    {
        switch (blockType)
        {
            case 0:
                return "STREAMINFO";
            case 1:
                return "PADDING";
            case 2:
                return "APPLICATION";
            case 3:
                return "SEEKTABLE";
            case 4:
                return "VORBIS_COMMENT";
            case 5:
                return "CUESHEET";
            default:
                return "UNKNOWN-RESERVED";
        }
    }

    public boolean isLastBlock()
    {
        return isLastBlock;
    }

    public byte[] getBytes()
    {
        bytes[0] = (byte) (bytes[0] & 0x7F);
        return bytes;
    }
}
