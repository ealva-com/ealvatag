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
package org.jaudiotagger.tag.mp4;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.util.Mp4Box;

public class Mp4TagReader {
    /*
     * Tree a descendre:
     * moov
     *  udta
     *   meta
     *    ilst
     *     [
     *      xxxx <-  id de l'info
     *       data
     *      ]
     */
    public Mp4Tag read( RandomAccessFile raf ) throws CannotReadException, IOException {
        Mp4Tag tag = new Mp4Tag();

        Mp4Box box = new Mp4Box();
        byte[] b = new byte[4];

        //Get to the facts
        //1-Searching for "moov"
        seek(raf, box, "moov");

        //2-Searching for "udta"
        seek(raf, box, "udta");

        //3-Searching for "meta"
        seek(raf, box, "meta");

        //4-skip the meta flags
        raf.read(b);
        if(b[0] != 0)
            throw new CannotReadException();

        //5-Seek the "ilst"
        seek(raf, box, "ilst");
        int length = box.getOffset() - 8;

        int read = 0;
        while(read < length) {
            b = new byte[8];
            raf.read(b);
            box.update(b);

            int fieldLength = box.getOffset() - 8;
            b = new byte[fieldLength];
            raf.read(b);

            tag.add(createMp4Field(box.getId(), b));
            read += 8+fieldLength;
        }

        System.out.println(tag);
        return tag;
    }

    private Mp4TagField createMp4Field(String id, byte[] raw) throws UnsupportedEncodingException {
        if(id.equals("trkn") || id.equals("tmpo") )
            return new Mp4TagTextNumberField(id, raw);

        else if(id.equals("\u00A9ART")  ||
                id.equals("\u00A9alb")  ||
                id.equals("\u00A9nam")  ||
                id.equals("\u00A9day")  ||
                id.equals("\u00A9cmt")  ||
                id.equals("\u00A9gen")  ||
                id.equals("\u00A9too")  ||
                id.equals("\u00A9wrt")    )
            return new Mp4TagTextField(id, raw);

        else if(id.equals("covr"))
            return new Mp4TagCoverField(raw);

        return new Mp4TagBinaryField(id, raw);
    }

    private void seek(RandomAccessFile raf, Mp4Box box, String id) throws IOException {
        byte[] b = new byte[8];
        raf.read(b);
        box.update(b);
        while(!box.getId().equals(id)) {
            raf.skipBytes(box.getOffset()-8);
            raf.read(b);
            box.update(b);
        }
    }

    public static void main(String[] args) throws Exception {
        new Mp4TagReader().read(new RandomAccessFile(new File("/home/kikidonk/test.mp4"), "r"));
    }
}
