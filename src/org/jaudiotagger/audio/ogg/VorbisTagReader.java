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
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.util.Arrays;

/**
 * Read Vorbis Tag within ogg
 * <p/>
 * Vorbis is the audiostream within an ogg file, Vorbis uses VorbisComments as its tag

 *
 */
public class VorbisTagReader
{

    private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();

    public Tag read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        //1st page = codec infos
        OggPageHeader pageHeader = OggPageHeader.read (raf);
        System.out.println("Page is type:"+pageHeader.getHeaderType());
        //Skip over data to end of page header 1
        raf.seek(raf.getFilePointer() + pageHeader.getPageLength());

        //2nd page = comment, may extend to additional pages or not , may also have decode header
        long oldPos = raf.getFilePointer();
        pageHeader = OggPageHeader.read (raf);
        System.out.println("Page is type:"+pageHeader.getHeaderType());
             

        //Now at start of packets on page 2 , this should be the vorbis header
        byte [] b = new byte[7];
        raf.read(b);

        String vorbis = new String(b, 1, 6);
        if (b[0] != 3 || !vorbis.equals(VorbisHeader.CAPTURE_PATTERN))
        {
            throw new CannotReadException("Cannot find comment block (no vorbiscomment header)");
        }
        //Begin tag reading
        //System.err.println("Startedreadingcomment");
        VorbisCommentTag tag = vorbisCommentReader.read(raf);
        //System.err.println("Endedreadingcomment");
        byte isValid = raf.readByte();
        if (isValid == 0)
        {
            throw new CannotReadException("Error: The OGG Stream isn't valid, could not extract the tag");
        }
        //System.err.println("Finishedreadtag");
        return tag;
    }
}

