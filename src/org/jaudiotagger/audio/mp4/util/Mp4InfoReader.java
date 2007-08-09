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
package org.jaudiotagger.audio.mp4.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;

public class Mp4InfoReader
{
    public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException
    {
        GenericAudioHeader info = new GenericAudioHeader();

        Mp4Box box = new Mp4Box();

        //Get to the facts
        //1-Searching for "moov"
        seek(raf, box, "moov");

        //2-Searching for "udta"
        seek(raf, box, "mvhd");

        byte[] b = new byte[box.getOffset() - 8];
        raf.read(b);

        Mp4MvhdBox mvhd = new Mp4MvhdBox(b);
        info.setLength(mvhd.getLength());

        System.out.println(info);
        return info;
    }

    private void seek(RandomAccessFile raf, Mp4Box box, String id) throws IOException
    {
        byte[] b = new byte[8];
        raf.read(b);
        box.update(b);
        while (!box.getId().equals(id))
        {
            raf.skipBytes(box.getOffset() - 8);
            raf.read(b);
            box.update(b);
        }
    }

    public static void main(String[] args) throws Exception
    {
        new Mp4InfoReader().read(new RandomAccessFile(new File("/home/kikidonk/test.mp4"), "r"));
    }
}
