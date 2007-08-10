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

import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentReader;

import java.io.*;

/**
 * Read Flac Tag, uses VorbisComment
 */
public class FlacTagReader
{

    private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();

    public VorbisCommentTag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        //Begins tag parsing
        if (raf.length() == 0)
        {
            //Empty File
            throw new CannotReadException("Error: File empty");
        }
        raf.seek(0);

        //FLAC Header string
        byte[] b = new byte[4];
        raf.read(b);
        String flac = new String(b);
        if (!flac.equals("fLaC"))
        {
            throw new CannotReadException("fLaC Header not found, not a flac file");
        }

        VorbisCommentTag tag = null;

        //Seems like we hava a valid stream
        boolean isLastBlock = false;
        while (!isLastBlock)
        {
            b = new byte[4];
            raf.read(b);
            MetadataBlockHeader mbh = new MetadataBlockHeader(b);

            switch (mbh.getBlockType())
            {
                //We got a vorbiscomment comment block, parse it
                case MetadataBlockHeader.VORBIS_COMMENT :
                    tag = handleVorbisComment(mbh, raf);
                    mbh = null;
                    return tag; //We have it, so no need to go further

                    //This is not a vorbiscomment comment block, we skip to next block
                default :
                    raf.seek(raf.getFilePointer() + mbh.getDataLength());
                    break;
            }

            isLastBlock = mbh.isLastBlock();
            mbh = null;
        }
        //FLAC not found...
        throw new CannotReadException("FLAC Tag could not be found or read..");
    }

    private VorbisCommentTag handleVorbisComment(MetadataBlockHeader mbh, RandomAccessFile raf) throws IOException, CannotReadException
    {
        byte[] commentHeaderRawPacket = new byte[mbh.getDataLength()];
        raf.read(commentHeaderRawPacket);
        VorbisCommentTag tag = vorbisCommentReader.read(commentHeaderRawPacket);
        return tag;                                                                     
    }
}

