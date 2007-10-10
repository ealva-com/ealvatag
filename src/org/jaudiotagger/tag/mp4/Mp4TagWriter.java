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

import java.io.*;
import java.util.logging.Logger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.util.Mp4MetaBox;
import org.jaudiotagger.audio.mp4.util.Mp4FreeBox;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;
import org.jaudiotagger.audio.generic.Utils;


/**
 * Writes metadata from mp4, the metadata tags are held under the ilst atom as shown below
 * <p/>
 * When writing changes the size of all the atoms upto ilst has to be recalculated, then if the size of
 * the metadata is increased the size of the free atom (below meta) should be reduced accordingly or vice versa.
 * If the size of the metadata has increased by more than the size of the free atom then the size of meta, udta
 * and moov should be recalculated and the top level free atom reduced accordingly
 * <p/>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |......|----- udta
 * |..............|
 * |..............|-- meta
 * |....................|
 * |....................|-- hdlr
 * |....................|-- ilst
 * |....................|.. ..|
 * |....................|.....|---- @nam (Optional for each metadatafield)
 * |....................|.....|.......|-- data
 * |....................|.....|....... ecetera
 * |....................|.....|---- ---- (Optional for reverse dns field)
 * |....................|.............|-- mean
 * |....................|.............|-- name
 * |....................|.............|-- data
 * |....................|................ ecetere
 * |....................|-- free
 * |--- free
 * |--- mdat
 */
public class Mp4TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.mp4");

    private Mp4TagCreator tc = new Mp4TagCreator();

    /**
     * Write tag to rafTemp file
     *
     * @param tag tag data
     * @param raf current file
     * @param rafTemp temporary file for writing
     * @throws CannotWriteException
     * @throws IOException
     */
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        System.out.println("Writing mp4 tag to file");
        //Go through every field constructing the data that will appear starting from ilst box
        ByteBuffer rawData = tc.convert(tag);
        rawData.rewind();

        //Read the various boxes that we may have to adjust
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MOOV.getFieldName());
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        FileChannel fileReadChannel = raf.getChannel();
        long positionWithinFileAfterFindingMoovHeader = fileReadChannel.position();
        fileReadChannel.read(moovBuffer);
        moovBuffer.rewind();

        //Level 2-Searching for "udta" within "moov"
        Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());

        //Level 3-Searching for "meta" within udta
        Mp4BoxHeader boxHeader;
        try
        {
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
            Mp4MetaBox meta = new Mp4MetaBox(boxHeader, moovBuffer);
            meta.processData();
        }
        catch (CannotReadException cre)
        {
            throw new CannotWriteException("problem finding meta field");
        }

        //Level 4- Search for "ilst" within meta
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.ILST.getFieldName());
        System.out.println("ILST:Old size" + boxHeader.getLength() + ":New size:" + rawData.limit());
        int oldIlstSize = boxHeader.getLength();
        int newIlstSize = rawData.limit();
        int relativeIlstposition = moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH;

        //TODO maynot be a free atom?
        //Level 4 - Search for "free" within meta
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.FREE.getFieldName());
        int oldFreeSize = boxHeader.getLength();
        System.out.println("FREE:Old size" + boxHeader.getLength());

        //The easiest option since no difference in the size of the metadata so all we have to do is
        //create a new file identical to first file but with replaced metadata
        if (oldIlstSize == newIlstSize)
        {
            //Calculate absolute in fileof ilst atom
            long startIstWithinFile = positionWithinFileAfterFindingMoovHeader + relativeIlstposition;

            fileReadChannel.position(0);
            FileChannel fileWriteChannel = rafTemp.getChannel();
            fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
            fileWriteChannel.position(startIstWithinFile);
            fileWriteChannel.write(rawData);
            fileReadChannel.position(startIstWithinFile + oldIlstSize);
            fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                    fileReadChannel.size() - fileReadChannel.position());
        }
        //.. we just need to increase the size of the free atom below the meta atom, and replace the metadata
        //no other changes neccessary and total file size remains the same
        else if (oldIlstSize > newIlstSize)
        {               
            //Calculate relative position in file of ilst atom
            long startIstWithinFile = positionWithinFileAfterFindingMoovHeader + relativeIlstposition;

            fileReadChannel.position(0);
            FileChannel fileWriteChannel = rafTemp.getChannel();
            fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
            fileWriteChannel.position(startIstWithinFile);
            fileWriteChannel.write(rawData);
            fileReadChannel.position(startIstWithinFile + oldIlstSize);

            //Create an amended freeBaos atom and write it.
            int newFreeSize = oldFreeSize + (oldIlstSize - newIlstSize);
            Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize - Mp4BoxHeader.HEADER_LENGTH);
            fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
            fileWriteChannel.write(newFreeBox.getData());

            //Skip over the read channel old free atom
            fileReadChannel.position(fileReadChannel.position() + oldFreeSize);

            //Now write the rest of the file which wont have changed
            fileWriteChannel.transferFrom(fileReadChannel,
                                          fileWriteChannel.position(),
                                          fileReadChannel.size() - fileReadChannel.position());

        }
        //The most complex situation more atoms affected
        else
        {
            int additionalSpaceRequiredForMetadata = newIlstSize - oldIlstSize;
            //We can fit the metadata in under the meta item just by using some of the padding available in the free
            //atom under the meta atom need to take of the side of free header otherwise might end up with
            //soln where can fit in data, but cant fit in free atom
            if(additionalSpaceRequiredForMetadata<=(oldFreeSize - Mp4BoxHeader.HEADER_LENGTH))
            {
                int newFreeSize = oldFreeSize - (additionalSpaceRequiredForMetadata);

                //Calculate absolute position in file of ilst atom
                long startIstWithinFile = positionWithinFileAfterFindingMoovHeader + relativeIlstposition;

                fileReadChannel.position(0);              
                FileChannel fileWriteChannel = rafTemp.getChannel();
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
                fileWriteChannel.position(startIstWithinFile);
                fileWriteChannel.write(rawData);
                fileReadChannel.position(startIstWithinFile + oldIlstSize);

                //Create an amended smaller freeBaos atom and write it to file
                Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                fileWriteChannel.write(newFreeBox.getData());

                //Skip over the read channel old free atom
                fileReadChannel.position(fileReadChannel.position() + oldFreeSize);                

                //Now write the rest of the file which won't have changed
                fileWriteChannel.transferFrom(fileReadChannel,
                                              fileWriteChannel.position(),
                                              fileReadChannel.size() - fileReadChannel.position());
            }
            else
            {
                //There is not enough padding in the free atom anyway,
                //TODO Should we create a new one with some padding so changes at a later date are less likely to need

                //Size meta needs to be increased by (if not writing a free atom)
                //Special Case this could actually be negative (upto -8)if  is actually enough space but would
                //not be able to write free atom properly, it doesnt matter the parent atoms would still
                //need their sizes adjusted.
                int additionalMetaSize
                        = additionalSpaceRequiredForMetadata - (oldFreeSize);

                System.out.println("Additional size required:"+additionalMetaSize);

                //Calculate absolute position in file of ilst atom
                long startIstWithinFile = positionWithinFileAfterFindingMoovHeader + relativeIlstposition;

                //Write stuff before Moov (ftyp)
                System.out.println("Writing 1:");
                FileChannel fileWriteChannel = rafTemp.getChannel();
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel,
                        0,
                        positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.position( positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);

                //Edit and rewrite the Moov header
                moovHeader.setLength(moovHeader.getLength() + additionalMetaSize);
                System.out.println("Writing 2:"+fileWriteChannel.position());
                fileWriteChannel.write(moovHeader.getHeaderData());


                //Edit the fields in moovBuffer (note moovbuffer doesnt include header), then write moovbuffer
                //upto ilst header
                //Level 2-Searching for "udta" within "moov"
                moovBuffer.rewind();
                Mp4BoxHeader udtaHdr =Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());
                udtaHdr.setLength(udtaHdr.getLength() + additionalMetaSize);
                moovBuffer.position(moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH);
                moovBuffer.put(udtaHdr.getHeaderData());

                //Level 3-Searching for "meta" within udta
                Mp4BoxHeader metaHdr = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
                metaHdr.setLength(metaHdr.getLength() + additionalMetaSize);
                moovBuffer.position(moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH);
                moovBuffer.put(metaHdr.getHeaderData());

                //Now write from this edited buffer up until ilst atom
                moovBuffer.rewind();
                moovBuffer.limit(relativeIlstposition);
                System.out.println("Writing 3:"+fileWriteChannel.position());
                fileWriteChannel.write(moovBuffer);

                //Now write ilst data
                System.out.println("Writing 4:"+fileWriteChannel.position());
                fileWriteChannel.write(rawData);

                //Skip over the read channel old free atom
                fileReadChannel.position(startIstWithinFile + oldIlstSize);
                fileReadChannel.position(fileReadChannel.position() + oldFreeSize);

                //Now write toplevel free and mdat
                System.out.println("Writing 5:"+fileWriteChannel.position());
                fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                    fileReadChannel.size() - fileReadChannel.position());
                System.out.println("Writing 6:"+fileWriteChannel.position());     
            }
        }
    }

    public void delete(RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException
    {
        
    }

}
