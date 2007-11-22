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
package org.jaudiotagger.audio.flac.metadatablock;

/**
 * Metadata Block Header
 */
public class MetadataBlockHeader
{
    private boolean isLastBlock;
    private int  dataLength;
    private byte[] bytes;
    private BlockType blockType;

    public MetadataBlockHeader(byte[] b)
    {
        isLastBlock = ((b[0] & 0x80) >>> 7) == 1;

        int type = b[0] & 0x7F;
        if(type<BlockType.values().length)
        {
            blockType = BlockType.values()[type];
        }


        dataLength = (u(b[1]) << 16) + (u(b[2]) << 8) + (u(b[3]));

        bytes = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            bytes[i] = b[i];
        }
        ;
    }

    private int u(int i)
    {
        return i & 0xFF;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public BlockType getBlockType()
    {
        return blockType;
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
