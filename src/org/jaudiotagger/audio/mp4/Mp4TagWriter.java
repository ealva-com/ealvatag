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
package org.jaudiotagger.audio.mp4;

import java.io.*;
import java.util.logging.Logger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.mp4.Mp4TagCreator;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.atom.*;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;


/**
 * Writes metadata from mp4, the metadata tags are held under the ilst atom as shown below
 * <p/>
 * <p/>
 * When writing changes the size of all the atoms upto ilst has to be recalculated, then if the size of
 * the metadata is increased the size of the free atom (below meta) should be reduced accordingly or vice versa.
 * If the size of the metadata has increased by more than the size of the free atom then the size of meta, udta
 * and moov should be recalculated and the top level free atom reduced accordingly
 * If there is not enough space even if using both of the free atoms, then the mdat atom has to be shifted down
 * accordingly to make space, and the stco atom has to have its offsets to mdat chunks table adjusted accordingly.
 * <p/>
 * <p/>
 * <pre>
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
 * </pre>
 */
public class Mp4TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.tag.mp4");

    private Mp4TagCreator tc = new Mp4TagCreator();


    /**
     * Replace the ilst metadata
     *
     * Because it is the same size as the original data nothing else has to be modified
     *
     * @param rawIlstData
     * @param oldIlstSize
     * @param startIstWithinFile
     * @param fileReadChannel
     * @param fileWriteChannel
     * @throws CannotWriteException
     * @throws IOException
     */
    private void writeMetadataSameSize(ByteBuffer rawIlstData,
                                       long oldIlstSize,
                                       long startIstWithinFile,
                                       FileChannel fileReadChannel,
                                       FileChannel fileWriteChannel)
             throws CannotWriteException, IOException
    {
        fileReadChannel.position(0);
        fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
        fileWriteChannel.position(startIstWithinFile);
        fileWriteChannel.write(rawIlstData);
        fileReadChannel.position(startIstWithinFile + oldIlstSize);
        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                fileReadChannel.size() - fileReadChannel.position());
    }

    /** When the size of the metadat has changed and it cant be compensated for by free atom
     *  we have to adjust the size of the size field upto the moovheader level
     *
     * @param moovBuffer
     * @param sizeAdjustment can be negative or positive     *
     * @return
     */
       private void adjustSizeOfMoovHeader( Mp4BoxHeader moovHeader,ByteBuffer moovBuffer,int sizeAdjustment)
               throws IOException
       {
           //Adjust header size
           moovHeader.setLength(moovHeader.getLength() + sizeAdjustment);
           //Edit the fields in moovBuffer (note moovbuffer doesnt include header), then write moovbuffer
           //upto ilst header
           //Level 2-Searching for "udta" within "moov"
           moovBuffer.rewind();
           Mp4BoxHeader udtaHdr = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());
           udtaHdr.setLength(udtaHdr.getLength() + sizeAdjustment);
           moovBuffer.position(moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH);
           moovBuffer.put(udtaHdr.getHeaderData());

           //Level 3-Searching for "meta" within udta
           Mp4BoxHeader metaHdr = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
           metaHdr.setLength(metaHdr.getLength() + sizeAdjustment);
           moovBuffer.position(moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH);
           moovBuffer.put(metaHdr.getHeaderData());
       }

    /**
     * Adjust the offsets in the stco box , required when mdat box is shifted due to lack of enough
     * free space to accomodate increases in size of metadata
     *
     * @param moovBuffer
     * @param sizeAdjustment
     * @throws IOException
     */
    private void adjustOffsetsInStcoBox( ByteBuffer moovBuffer,int sizeAdjustment)
               throws IOException
   {
        moovBuffer.rewind();
        //Find the stco box
        //Level 2-Searching for "trak" within "moov"
        Mp4BoxHeader boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.TRAK.getFieldName());

        //Level 3-Searching for "mdia" within "trak"
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.MDIA.getFieldName());

        //Level 4-Searching for "minf" within "mdia"
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.MINF.getFieldName());

        //Level 5-Searching for "stbl within "minf"
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.STBL.getFieldName());

        //Level 6-Searching for stco, within "stbl"
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer,Mp4NotMetaFieldKey.STCO.getFieldName());
        Mp4StcoBox stco = new Mp4StcoBox(boxHeader,moovBuffer,sizeAdjustment);

   }

    /**
     * Write tag to rafTemp file
     *
     * @param tag     tag data
     * @param raf     current file
     * @param rafTemp temporary file for writing
     * @throws CannotWriteException
     * @throws IOException
     */
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        logger.info("Started writing tag data");
        //Go through every field constructing the data that will appear starting from ilst box
        ByteBuffer rawIlstData = tc.convert(tag);
        rawIlstData.rewind();

        //Write channel
        FileChannel fileWriteChannel = rafTemp.getChannel();

        //Read the various boxes that we may have to adjust, everything contained under moov atom
        Mp4BoxHeader moovHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MOOV.getFieldName());
        ByteBuffer moovBuffer = ByteBuffer.allocate(moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
        FileChannel fileReadChannel = raf.getChannel();
        long positionWithinFileAfterFindingMoovHeader = fileReadChannel.position();
        fileReadChannel.read(moovBuffer);
        moovBuffer.rewind();

        //Level 2-Searching for "udta" within "moov" within moovBuffer
        Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());

        //Level 3-Searching for "meta" within udta within moovBuffer
        Mp4BoxHeader boxHeader;
        try
        {
            boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
            Mp4MetaBox meta = new Mp4MetaBox(boxHeader, moovBuffer);
            meta.processData();
        }
        catch (CannotReadException cre)
        {
            throw new CannotWriteException("Problem finding meta field");
        }

        //Level 4- Search for "ilst" within meta within moovBuffer, may not exist (for example AP --metaenema)
        int startPositionInMoovBufferForSearchingIlst = moovBuffer.position();
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.ILST.getFieldName());
        int oldIlstSize = 0;
        int relativeIlstposition ;
        int relativeIlstEndPosition ;
        if(boxHeader!=null)
        {
            oldIlstSize = boxHeader.getLength();
            relativeIlstposition = moovBuffer.position() - Mp4BoxHeader.HEADER_LENGTH;
            moovBuffer.position(moovBuffer.position() + boxHeader.getDataLength());
            relativeIlstEndPosition = moovBuffer.position();
        }
        else
        {
            //Go back to where started looking for ilst
            moovBuffer.position(startPositionInMoovBufferForSearchingIlst);
            relativeIlstposition = startPositionInMoovBufferForSearchingIlst;
            relativeIlstEndPosition = startPositionInMoovBufferForSearchingIlst;
        }

        int newIlstSize = rawIlstData.limit();

        //Level 4 - Search for "free" within moovBuffer, (it may not exist)
        boxHeader = Mp4BoxHeader.seekWithinLevel(moovBuffer, Mp4NotMetaFieldKey.FREE.getFieldName());
        int oldFreeSize     = 0;
        int extraDataSize   = 0;
        if(boxHeader==null)
        {
            oldFreeSize = 0;
            //Is there anything else here that needs to be preserved such as uuid atoms
            extraDataSize = moovBuffer.limit() - relativeIlstEndPosition;
        }
        else
        {
            oldFreeSize = boxHeader.getLength();
            //Is there anything else here that needs to be preserved such as uuid atoms
            extraDataSize = moovBuffer.limit() - (moovBuffer.position() + boxHeader.getDataLength());
        }

        //Calculate absolute location in file of start of ilst atom
        long startIstWithinFile = positionWithinFileAfterFindingMoovHeader + relativeIlstposition;

        //Search for Level-1 free atom within file
        long topLevelFreePosition = fileReadChannel.position();
        Mp4BoxHeader topLevelFreeHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.FREE.getFieldName());
        int topLevelFreeSize     = 0;
        if(topLevelFreeHeader==null)
        {
            topLevelFreeSize = 0;
            fileReadChannel.position(topLevelFreePosition);
        }
        else
        {
             topLevelFreeSize = topLevelFreeHeader.getLength();
             topLevelFreePosition = fileReadChannel.position() - Mp4BoxHeader.HEADER_LENGTH;
             fileReadChannel.position(topLevelFreePosition+topLevelFreeHeader.getLength());
        }
         //Search for Level-1 Mdat atom
        Mp4BoxHeader mdatHeader = Mp4BoxHeader.seekWithinLevel(raf, Mp4NotMetaFieldKey.MDAT.getFieldName());
        if(mdatHeader==null)
        {
             throw new CannotWriteException("Unable to safetly find audio data");
        }

        logger.info("Read header successfully ready for writing");

        //The easiest option since no difference in the size of the metadata so all we have to do is
        //create a new file identical to first file but with replaced metadata
        if (oldIlstSize == newIlstSize)
        {
            logger.info("Writing:Option 1:Same Size");
            writeMetadataSameSize(rawIlstData,
                                  oldIlstSize,
                                  startIstWithinFile,
                                  fileReadChannel,
                                  fileWriteChannel);
        }
        //.. we just need to increase the size of the free atom below the meta atom, and replace the metadata
        //no other changes neccessary and total file size remains the same
        else if (oldIlstSize > newIlstSize)
        {
            //Create an amended freeBaos atom and write it if it previously existed
            if(oldFreeSize>0)
            {
                logger.info("Writing:Option 2:Smaller Size have free atom");
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
                fileWriteChannel.position(startIstWithinFile);
                fileWriteChannel.write(rawIlstData);
                fileReadChannel.position(startIstWithinFile + oldIlstSize);

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
            //No free atom we need to create a new one or adjust top level free atom
            else
            {
                int newFreeSize = (oldIlstSize - newIlstSize ) - Mp4BoxHeader.HEADER_LENGTH ;
                //We need to create a new one, so dont have to adjust all the headers but only works if the size
                //of tags has decreased by more 8 characters so there is enough room for the free boxes header we take
                //into account size of new header in calculating size of box
                if(newFreeSize > Mp4BoxHeader.HEADER_LENGTH)
                {
                    logger.info("Writing:Option 3:Smaller Size can create free atom");
                    fileReadChannel.position(0);
                    fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
                    fileWriteChannel.position(startIstWithinFile);
                    fileWriteChannel.write(rawIlstData);
                    fileReadChannel.position(startIstWithinFile + oldIlstSize);

                    //Create new free box
                    Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize);
                    fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                    fileWriteChannel.write(newFreeBox.getData());

                    //Now write the rest of the file which wont have changed
                    fileWriteChannel.transferFrom(fileReadChannel,
                    fileWriteChannel.position(),
                    fileReadChannel.size() - fileReadChannel.position());
                }
                //Ok everything in this bit of tree has to be recalculated
                else
                {
                    //Size will be this amount smaller
                    int sizeReducedBy = oldIlstSize - newIlstSize;

                    //Write stuff before Moov (ftyp)
                    fileReadChannel.position(0);
                    fileWriteChannel.transferFrom(fileReadChannel,
                            0,
                            positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                    fileWriteChannel.position(positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);

                    //Edit stco atom within moov header, if there is there is no top level header already adn not
                    //enough bytes have changed to allow the creation of a free atom that would be correct size

                    if (sizeReducedBy <  Mp4BoxHeader.HEADER_LENGTH)
                    {
                        //We dont bother using the top level free atom coz not big enough anyway, we need to adjust offsets
                        //by the amount mdat is going to be shifted
                        adjustOffsetsInStcoBox(moovBuffer,-sizeReducedBy);
                    }

                    //Edit and rewrite the Moov header and buffer upto ilst atom
                    adjustSizeOfMoovHeader(moovHeader,moovBuffer,-sizeReducedBy);
                    fileWriteChannel.write(moovHeader.getHeaderData());
                    moovBuffer.rewind();
                    moovBuffer.limit(relativeIlstposition);
                    fileWriteChannel.write(moovBuffer);

                    //Now write ilst data
                    fileWriteChannel.write(rawIlstData);

                    fileReadChannel.position(startIstWithinFile + oldIlstSize);

                    //Writes any extra info such as uuid fields at the end of the meta atom after the ilst atom
                    if(extraDataSize>0)
                    {
                        fileWriteChannel.transferFrom( fileReadChannel,
                                                        fileWriteChannel.position(),
                                                        extraDataSize);
                        fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                    }

                    //Adjust size of free box, make it larger to take up the space lost by the metadata
                    if (topLevelFreeHeader!=null && sizeReducedBy > Mp4BoxHeader.HEADER_LENGTH)
                    {
                        logger.info("Writing:Option 4:Smaller Size cannot create free atom but have top level free atom");
                        Mp4FreeBox freeBox = new Mp4FreeBox((topLevelFreeHeader.getLength()
                                            - Mp4BoxHeader.HEADER_LENGTH) + sizeReducedBy);
                        fileWriteChannel.write(freeBox.getHeader().getHeaderData());
                        fileWriteChannel.write(freeBox.getData());

                        //Skip over the read channel old free atom
                        fileReadChannel.position(fileReadChannel.position() + topLevelFreeHeader.getLength());

                        //Write Mdat
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                                fileReadChannel.size() - fileReadChannel.position());
                    }
                    else
                    {
                        //Currently no top level but large nough change to create a free box to take up slack
                        if(sizeReducedBy > Mp4BoxHeader.HEADER_LENGTH )
                        {
                            logger.info("Writing:Option 5:Smaller Size cannot create free atom but can create top level free atom");
                            Mp4FreeBox freeBox = new Mp4FreeBox(sizeReducedBy - Mp4BoxHeader.HEADER_LENGTH);
                            fileWriteChannel.write(freeBox.getHeader().getHeaderData());
                            fileWriteChannel.write(freeBox.getData());

                            //Now write the rest of the file which won't have changed
                            fileWriteChannel.transferFrom(fileReadChannel,
                            fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());
                        }
                        //Size has decreased by less than the size required to create/hold a free
                        //atom so cant create a free atom or use existing one
                        else
                        {
                            logger.info("Writing:Option 6:Smaller Size cannot create free atoms");

                            //Now write the rest of the file which won't have changed
                            fileWriteChannel.transferFrom(fileReadChannel,
                            fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());
                        }
                    }
                }
            }
        }
        //The most complex situation, more atoms affected
        else
        {
            int additionalSpaceRequiredForMetadata = newIlstSize - oldIlstSize;

            //We can fit the metadata in under the meta item just by using some of the padding available in the free
            //atom under the meta atom need to take of the side of free header otherwise might end up with
            //soln where can fit in data, but cant fit in free atom header
            if (additionalSpaceRequiredForMetadata <= (oldFreeSize - Mp4BoxHeader.HEADER_LENGTH))
            {
                int newFreeSize = oldFreeSize - (additionalSpaceRequiredForMetadata);
                logger.info("Writing:Option 7;Larger Size can use meta free atom need extra:"+newFreeSize + "bytes");
                            
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
                fileWriteChannel.position(startIstWithinFile);
                fileWriteChannel.write(rawIlstData);
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
                //There is not enough padding in the metadata free atom anyway
                //Size meta needs to be increased by (if not writing a free atom)
                //Special Case this could actually be negative (upto -8)if  is actually enough space but would
                //not be able to write free atom properly, it doesnt matter the parent atoms would still
                //need their sizes adjusted.
                int additionalMetaSize
                        = additionalSpaceRequiredForMetadata - (oldFreeSize);

                //Write stuff before Moov (ftyp)
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel,
                        0,
                        positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.position(positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);

                //Edit stco atom within moov header, if there is not enough space in the top level free atom
                //OR special case of matching exaclty the free atom plus header
                if (
                        (topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH < additionalMetaSize)
                        &&
                        (topLevelFreeSize != additionalMetaSize))        
                {
                    //We dont bother using the top level free atom coz not big enough anyway, we need to adjust offsets
                    //by the amount mdat is going to be shifted
                    adjustOffsetsInStcoBox(moovBuffer,additionalMetaSize);
                }

                //Edit and rewrite the Moov header
                adjustSizeOfMoovHeader(moovHeader,moovBuffer,additionalMetaSize);
                fileWriteChannel.write(moovHeader.getHeaderData());

                //Now write from this edited buffer up until ilst atom
                moovBuffer.rewind();
                moovBuffer.limit(relativeIlstposition);
                fileWriteChannel.write(moovBuffer);
            
                //Now write ilst data
                fileWriteChannel.write(rawIlstData);

                //Skip over the read channel old free atom because now used up
                fileReadChannel.position(startIstWithinFile + oldIlstSize);
                fileReadChannel.position(fileReadChannel.position() + oldFreeSize);
                //Writes any extra info such as uuid fields at the end of the meta atom after the ilst atom
                if(extraDataSize>0)
                {
                    fileWriteChannel.transferFrom( fileReadChannel,
                                                    fileWriteChannel.position(),
                                                    extraDataSize);
                    fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                }
                fileReadChannel.position(topLevelFreePosition);


                //If the shift is less than the space available in this second free atom data size we should
                //minimize the free atom accordingly (then we don't have to update stco atom)
                //note could be a double negative as additionalMetaSize could be -1 to -8 but thats ok stills works
                //ok
                if (topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH >= additionalMetaSize)
                {
                    logger.info("Writing:Option 8;Larger Size can use top free atom");
                    Mp4FreeBox freeBox = new Mp4FreeBox((topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH) - additionalMetaSize);
                    fileWriteChannel.write(freeBox.getHeader().getHeaderData());
                    fileWriteChannel.write(freeBox.getData());

                    //Skip over the read channel old free atom
                    fileReadChannel.position(fileReadChannel.position() + topLevelFreeSize);

                    //Write Mdat
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());
                }
                //If the space required is identical to total size of the free space (inc header)
                //we Could just remove the header
                else if (topLevelFreeSize == additionalMetaSize)
                {
                    logger.info("Writing:Option 9;Larger Size uses top free atom including header");
                    //Skip over the read channel old free atom
                    fileReadChannel.position(fileReadChannel.position() + topLevelFreeSize);

                    //Write Mdat
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());
                }
                //Mdat is going to have to move anyway, so keep free atom as is and write it and mdat
                //(have already updated stco above)
                else
                {
                    logger.info("Writing:Option 9;Larger Size cannot use top free atom");
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());

                }
            }
        }
        //Close all channels
        fileReadChannel.close();
        raf.close();

        logger.info("Checking file has been written correctly");                           
        //Renamechannel coz confusing otherwise
        FileChannel newFileReadChannel = fileWriteChannel;
        try
        {
            //Double check we havent lost anything before returning success, this is going to slow things down a little
            //but it is important to be extra careful with mp4 due to its lack of a formal specification
            //So search for the expected atoms, and check mdat size is retained           
            newFileReadChannel.position(0);

            Mp4BoxHeader newMoovHeader = Mp4BoxHeader.seekWithinLevel(rafTemp, Mp4NotMetaFieldKey.MOOV.getFieldName());
            ByteBuffer newMoovBuffer = ByteBuffer.allocate(newMoovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH);
            newFileReadChannel.read(newMoovBuffer);
            newMoovBuffer.rewind();

            //Level 2-Searching for "trak" within "moov"
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer,Mp4NotMetaFieldKey.TRAK.getFieldName());

            //Level 3-Searching for "mdia" within "trak"
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer,Mp4NotMetaFieldKey.MDIA.getFieldName());

            //Level 4-Searching for "minf" within "mdia"
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer,Mp4NotMetaFieldKey.MINF.getFieldName());

            //Level 5-Searching for "stbl within "minf"
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer,Mp4NotMetaFieldKey.STBL.getFieldName());

            //Level 6-Searching for stco, within "stbl"
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer,Mp4NotMetaFieldKey.STCO.getFieldName());
            Mp4StcoBox stco = new Mp4StcoBox(boxHeader,newMoovBuffer);
            newMoovBuffer.rewind(); //Rewind so just after moov header ready for searching for level2 again

            //Level 2-Searching for "udta" within "moov" within moovBuffer
            Mp4BoxHeader.seekWithinLevel(newMoovBuffer, Mp4NotMetaFieldKey.UDTA.getFieldName());

            //Level 3-Searching for "meta" within udta within moovBuffer
            try
            {
                boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer, Mp4NotMetaFieldKey.META.getFieldName());
                Mp4MetaBox meta = new Mp4MetaBox(boxHeader, newMoovBuffer);
                meta.processData();
            }
            catch (CannotReadException cre)
            {
                throw new CannotWriteException("Unable to find metafield in written file");
            }

            //Level 4- Search for "ilst" within meta within moovBuffer
            boxHeader = Mp4BoxHeader.seekWithinLevel(newMoovBuffer, Mp4NotMetaFieldKey.ILST.getFieldName());
            newMoovBuffer.position(newMoovBuffer.position() + boxHeader.getDataLength());

            //TODO:what about checking the correct amount of fields have actually been written
            
            //Search for Level-1 free atom within file (it may not exist)
            topLevelFreePosition = newFileReadChannel.position();
            topLevelFreeHeader = Mp4BoxHeader.seekWithinLevel(rafTemp, Mp4NotMetaFieldKey.FREE.getFieldName());
            topLevelFreeSize     = 0;
            if(topLevelFreeHeader==null)
            {
                topLevelFreeSize = 0;
                newFileReadChannel.position(topLevelFreePosition);
            }
            else
            {
                 topLevelFreeSize = topLevelFreeHeader.getLength();
                 topLevelFreePosition = newFileReadChannel.position() - Mp4BoxHeader.HEADER_LENGTH;
                 newFileReadChannel.position(newFileReadChannel.position()+topLevelFreeHeader.getDataLength());
            }

            //Search for Level-1 MDat Atom
            Mp4BoxHeader newMdatHeader = Mp4BoxHeader.seekWithinLevel(rafTemp, Mp4NotMetaFieldKey.MDAT.getFieldName());
            int mDataDataOffset = (int)rafTemp.getFilePointer();
            if(newMdatHeader==null)
            {
                 throw new CannotWriteException("Unable to safetly find audio data in file");
            }
            if(mdatHeader.getDataLength()!=newMdatHeader.getDataLength())
            {
                throw new CannotWriteException("Unable to write same length of audio data in file");
            }

            //Check offsets are correct
            //TODO what about if wrong in the original file
            if(stco.getFirstOffSet()!=mDataDataOffset)
            {
                throw new CannotWriteException("Unable to adjust offsets in audio data");
            }
        }
        catch(Exception e)
        {               
            if(e instanceof CannotWriteException)
            {

                throw (CannotWriteException)e;
            }
            else
            {
                 throw new CannotWriteException("Unable to make changes to file");
            }
        }
        finally
        {
             //Close references to new file
            rafTemp.close();
            newFileReadChannel.close();
        }
    }


    /**
     * Delete the tag
     * 
     * <p/>
     * <p>This is achieved by writing an empty ilst atom
     *
     * @param raf
     * @param rafTemp
     * @throws IOException
     */
    public void delete(RandomAccessFile raf, RandomAccessFile rafTemp) throws IOException
    {
        Mp4Tag tag = new Mp4Tag();

        try
        {
            write(tag,raf,rafTemp);
        }
        catch(CannotWriteException cwe)
        {
            throw new IOException(cwe.getMessage());
        }
    }
}
