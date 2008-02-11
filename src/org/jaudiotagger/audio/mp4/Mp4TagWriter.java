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
import org.jaudiotagger.logging.ErrorMessage;

import javax.swing.tree.DefaultMutableTreeNode;


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
     * <p/>
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

    /**
     * When the size of the metadata has changed and it cant be compensated for by free atom
     * we have to adjust the size of the size field upto the moovheader level for the udta atom and
     * its child meta atom.
     *
     * @param moovBuffer
     * @param sizeAdjustment can be negative or positive     *
     * @return
     */
    private void adjustSizeOfMoovHeader(Mp4BoxHeader moovHeader, ByteBuffer moovBuffer, int sizeAdjustment)
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

        //Read Channel for reading from old file
        FileChannel fileReadChannel = raf.getChannel();

        //Write channel for writing to new file
        FileChannel fileWriteChannel = rafTemp.getChannel();

        //Reference to a Box Header
        Mp4BoxHeader boxHeader;

        //TODO we shouldn't need all these variables, and some are very badly named - used by new and old methods
        int oldIlstSize = 0;
        int relativeIlstposition;
        int relativeIlstEndPosition;
        int startIstWithinFile;
        int newIlstSize;
        int oldMetaLevelFreeAtomSize = 0;
        long extraDataSize = 0;
        int level1SearchPosition = 0;
        int topLevelFreePosition = 0;
        int topLevelFreeSize     = 0;
        boolean topLevelFreeAtomComesBeforeMdatAtom = true;
        Mp4BoxHeader topLevelFreeHeader;

        Mp4AtomTree atomTree;
        //Make tree
        try
        {
            atomTree = new Mp4AtomTree(raf,false);
        }
        catch(CannotReadException cre)
        {
            throw new CannotWriteException("Problem finding meta field");
        }


        //Moov
        Mp4BoxHeader moovHeader = atomTree.getBoxHeader(atomTree.getMoovNode());
        long positionWithinFileAfterFindingMoovHeader = moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH;
        Mp4StcoBox stco = atomTree.getStco();

        Mp4BoxHeader ilstHeader = atomTree.getBoxHeader(atomTree.getIlstNode());
        Mp4BoxHeader metaHeader = atomTree.getBoxHeader(atomTree.getMetaNode());
        Mp4BoxHeader mdatHeader = atomTree.getBoxHeader(atomTree.getMdatNode());
        ByteBuffer   moovBuffer = atomTree.getMoovBuffer();

        //Ilst
        if (ilstHeader != null)
        {
            oldIlstSize = ilstHeader.getLength();

            //Relative means - 40,relative to moov buffer after header 
            relativeIlstposition    = (int)(ilstHeader.getFilePos() - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH));
            relativeIlstEndPosition = relativeIlstposition + ilstHeader.getLength();
            startIstWithinFile = (int)ilstHeader.getFilePos();
        }
        else
        {
            //There no ilst header so we set to position where it would be if it existed
            relativeIlstposition = (int)
                      ((metaHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH)
                    - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH)) ;
            relativeIlstEndPosition = relativeIlstposition;
            startIstWithinFile = (int)metaHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH;
        }

        newIlstSize = rawIlstData.limit();

        //Level 4 - Free
        oldMetaLevelFreeAtomSize = 0;
        extraDataSize = 0;
        for(DefaultMutableTreeNode freeNode: atomTree.getFreeNodes())
        {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)freeNode.getParent();

            if(!parentNode.isRoot())
            {
                Mp4BoxHeader header     = ((Mp4BoxHeader)parentNode.getUserObject());
                Mp4BoxHeader freeHeader = ((Mp4BoxHeader)freeNode.getUserObject());

                if(header.getId().equals(Mp4NotMetaFieldKey.META.getFieldName()))
                {
                    oldMetaLevelFreeAtomSize = freeHeader.getLength();

                    //Is there anything else here that needs to be preserved such as uuid atoms
                    extraDataSize=moovHeader.getFilePos() + moovHeader.getLength() - (freeHeader.getFilePos() + freeHeader.getLength());
                    break;
                }
            }
        }
        //If no free atom, still check for unexpected items within meta, after ilst
        //TODO this logic probably incomplete
        if(oldMetaLevelFreeAtomSize==0)
        {
            extraDataSize=moovHeader.getDataLength() - relativeIlstEndPosition;
        }

        //Level-1 free atom
        level1SearchPosition = 0;
        topLevelFreePosition = 0;
        topLevelFreeSize     = 0;
        topLevelFreeAtomComesBeforeMdatAtom = true;
        for(DefaultMutableTreeNode freeNode: atomTree.getFreeNodes())
        {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)freeNode.getParent();
            if(parentNode.isRoot())
            {
                topLevelFreeHeader = ((Mp4BoxHeader)freeNode.getUserObject());
                topLevelFreeSize     = topLevelFreeHeader.getLength();
                topLevelFreePosition = (int)topLevelFreeHeader.getFilePos();
                level1SearchPosition = topLevelFreePosition;
                break;
            }
        }

        if(topLevelFreeSize>0)
        {
            if(topLevelFreePosition>mdatHeader.getFilePos())
            {
                topLevelFreeAtomComesBeforeMdatAtom = false;
                level1SearchPosition=(int)mdatHeader.getFilePos();
            }
        }
        else
        {
            topLevelFreePosition=(int)mdatHeader.getFilePos();
            level1SearchPosition=topLevelFreePosition;
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
            if (oldMetaLevelFreeAtomSize > 0)
            {
                logger.info("Writing:Option 2:Smaller Size have free atom:"+oldIlstSize+":"+newIlstSize);
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIstWithinFile);
                fileWriteChannel.position(startIstWithinFile);
                fileWriteChannel.write(rawIlstData);
                fileReadChannel.position(startIstWithinFile + oldIlstSize);

                int newFreeSize = oldMetaLevelFreeAtomSize + (oldIlstSize - newIlstSize);
                Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                fileWriteChannel.write(newFreeBox.getData());

                //Skip over the read channel old free atom
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);

                //Now write the rest of the file which won't have changed
                fileWriteChannel.transferFrom(fileReadChannel,
                        fileWriteChannel.position(),
                        fileReadChannel.size() - fileReadChannel.position());
            }
            //No free atom we need to create a new one or adjust top level free atom
            else
            {
                int newFreeSize = (oldIlstSize - newIlstSize) - Mp4BoxHeader.HEADER_LENGTH;
                //We need to create a new one, so dont have to adjust all the headers but only works if the size
                //of tags has decreased by more 8 characters so there is enough room for the free boxes header we take
                //into account size of new header in calculating size of box
                if (newFreeSize > 0)
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
                //Ok everything in this bit of tree has to be recalculated because eight or less bytes smaller
                else
                {
                    logger.info("Writing:Option 4:Smaller Size <=8 cannot create free atoms");

                    //Size will be this amount smaller
                    int sizeReducedBy = oldIlstSize - newIlstSize;

                    //Write stuff before Moov (ftyp)
                    fileReadChannel.position(0);
                    fileWriteChannel.transferFrom(fileReadChannel,
                            0,
                            moovHeader.getFilePos());
                    fileWriteChannel.position(moovHeader.getFilePos());

                    //Edit stco atom within moov header,  we need to adjust offsets by the amount mdat is going to be shifted
                    //unless mdat is at start of file
                    if(mdatHeader.getFilePos()>moovHeader.getFilePos())
                    {
                        stco.adjustOffsets(-sizeReducedBy);
                    }

                    //Edit and rewrite the Moov,Udta and Ilst header in moov buffer
                    adjustSizeOfMoovHeader(moovHeader, moovBuffer, -sizeReducedBy);
                    fileWriteChannel.write(moovHeader.getHeaderData());
                    moovBuffer.rewind();
                    moovBuffer.limit(relativeIlstposition);
                    fileWriteChannel.write(moovBuffer);

                    //Now write ilst data
                    fileWriteChannel.write(rawIlstData);

                    fileReadChannel.position(startIstWithinFile + oldIlstSize);

                    //Writes any extra info such as uuid fields at the end of the meta atom after the ilst atom
                    if (extraDataSize > 0)
                    {
                        fileWriteChannel.transferFrom(fileReadChannel,
                                fileWriteChannel.position(),
                                extraDataSize);
                        fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                    }

                    //Now write the rest of the file which won't have changed
                    fileWriteChannel.transferFrom(fileReadChannel,
                            fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());

                }
            }
        }
        //Size of metadata has increased, the most complex situation, more atoms affected
        else
        {
            int additionalSpaceRequiredForMetadata = newIlstSize - oldIlstSize;

            //We can fit the metadata in under the meta item just by using some of the padding available in the free
            //atom under the meta atom need to take of the side of free header otherwise might end up with
            //solution where can fit in data, but cant fit in free atom header
            if (additionalSpaceRequiredForMetadata <= (oldMetaLevelFreeAtomSize - Mp4BoxHeader.HEADER_LENGTH))
            {
                int newFreeSize = oldMetaLevelFreeAtomSize - (additionalSpaceRequiredForMetadata);
                logger.info("Writing:Option 5;Larger Size can use meta free atom need extra:" + newFreeSize + "bytes");

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
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);

                //Now write the rest of the file which won't have changed
                fileWriteChannel.transferFrom(fileReadChannel,
                        fileWriteChannel.position(),
                        fileReadChannel.size() - fileReadChannel.position());
            }
            //There is not enough padding in the metadata free atom anyway
            //Size meta needs to be increased by (if not writing a free atom)
            //Special Case this could actually be negative (upto -8)if  is actually enough space but would
            //not be able to write free atom properly, it doesnt matter the parent atoms would still
            //need their sizes adjusted.
            else
            {

                int additionalMetaSizeThatWontFitWithinMetaAtom
                        = additionalSpaceRequiredForMetadata - (oldMetaLevelFreeAtomSize);

                //Write stuff before Moov (ftyp)
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel,
                        0,
                        positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.position(positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);

                //Edit stco atom within moov header, if the free atom comes after mdat OR
                //(there is not enough space in the top level free atom
                //or special case of matching exactly the free atom plus header)
                if (
                        (!topLevelFreeAtomComesBeforeMdatAtom)
                                ||
                                (
                                (topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH < additionalMetaSizeThatWontFitWithinMetaAtom)
                                        &&
                                        (topLevelFreeSize != additionalMetaSizeThatWontFitWithinMetaAtom))
                        )
                {
                    //We dont bother using the top level free atom coz not big enough anyway, we need to adjust offsets
                    //by the amount mdat is going to be shifted
                    if(mdatHeader.getFilePos()>moovHeader.getFilePos())
                    {
                        stco.adjustOffsets(additionalMetaSizeThatWontFitWithinMetaAtom);
                    }
                }

                //Edit and rewrite the Moov header
                adjustSizeOfMoovHeader(moovHeader, moovBuffer, additionalMetaSizeThatWontFitWithinMetaAtom);
                fileWriteChannel.write(moovHeader.getHeaderData());

                //Now write from this edited buffer up until ilst atom
                moovBuffer.rewind();
                moovBuffer.limit(relativeIlstposition);
                fileWriteChannel.write(moovBuffer);

                //Now write ilst data
                fileWriteChannel.write(rawIlstData);

                //Skip over the read channel old meta level free atom because now used up
                fileReadChannel.position(startIstWithinFile + oldIlstSize);
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);

                //Writes any extra info such as uuid fields at the end of the meta atom after the ilst atom
                if (extraDataSize > 0)
                {
                    fileWriteChannel.transferFrom(fileReadChannel,
                            fileWriteChannel.position(),
                            extraDataSize);
                    fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                }

                //fileReadChannel.position(topLevelFreePosition);
                //fileReadChannel.position(level1SearchPosition);

                //If we have top level free atom that comes before mdat we might be able to use it
                if (topLevelFreeAtomComesBeforeMdatAtom)
                {
                    //If the shift is less than the space available in this second free atom data size we should
                    //minimize the free atom accordingly (then we don't have to update stco atom)
                    //note could be a double negative as additionalMetaSizeThatWontFitWithinMetaAtom could be -1 to -8 but thats ok stills works
                    //ok
                    if (topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH >= additionalMetaSizeThatWontFitWithinMetaAtom)
                    {
                        logger.info("Writing:Option 6;Larger Size can use top free atom");
                        Mp4FreeBox freeBox = new Mp4FreeBox((topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH) - additionalMetaSizeThatWontFitWithinMetaAtom);
                        fileWriteChannel.write(freeBox.getHeader().getHeaderData());
                        fileWriteChannel.write(freeBox.getData());

                        //Skip over the read channel old free atom
                        fileReadChannel.position(fileReadChannel.position() + topLevelFreeSize);

                        //Write Mdat
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                                fileReadChannel.size() - fileReadChannel.position());


                    }
                    //If the space required is identical to total size of the free space (inc header)
                    //we could just remove the header
                    else if (topLevelFreeSize == additionalMetaSizeThatWontFitWithinMetaAtom)
                    {
                        logger.info("Writing:Option 7;Larger Size uses top free atom including header");
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
                        logger.info("Writing:Option 8;Larger Size cannot use top free atom");
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                                fileReadChannel.size() - fileReadChannel.position());
                    }
                }
                else
                {
                    logger.info("Writing:Option 9;Top Level Free comes after Mdat so cant use it");
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(),
                            fileReadChannel.size() - fileReadChannel.position());
                }
            }
        }
        //Close all channels to original file
        fileReadChannel.close();
        raf.close();

        logger.info("Checking file has been written correctly");         

        try
        {
            //Create a tree from the new file
            Mp4AtomTree newAtomTree;           
            newAtomTree = new Mp4AtomTree(rafTemp,false);

            //Check we still have audio data file, and check length
            Mp4BoxHeader newMdatHeader = newAtomTree.getBoxHeader(newAtomTree.getMdatNode());
            if(newMdatHeader==null)
            {
                  throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED_NO_DATA.getMsg());
            }            
            if(newMdatHeader.getLength()!=mdatHeader.getLength())
            {
                 throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED_DATA_CORRUPT.getMsg());
            }

            //Check we still have all the key atoms
            Mp4BoxHeader newMetaHeader = newAtomTree.getBoxHeader(atomTree.getMetaNode());
            if(newMetaHeader==null)
            {
                throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED_NO_TAG_DATA.getMsg());
            }

            //Check offsets are correct, may not match exactly in original file so just want to make
            //sure that the discrepancy if any is preserved
            Mp4StcoBox   newStco       = newAtomTree.getStco();
            
            logger.finer("stco:Original First Offset"+stco.getFirstOffSet());
            logger.finer("stco:Original Diff"+(int)(stco.getFirstOffSet() - mdatHeader.getFilePos()));
            logger.finer("stco:Original Mdat Pos"+mdatHeader.getFilePos());
            logger.finer("stco:New First Offset"+newStco.getFirstOffSet());
            logger.finer("stco:New Diff"+(int)((newStco.getFirstOffSet() - newMdatHeader.getFilePos())));
            logger.finer("stco:New Mdat Pos"+newMdatHeader.getFilePos());
            int diff = (int)(stco.getFirstOffSet() - mdatHeader.getFilePos());
            if ((newStco.getFirstOffSet() - newMdatHeader.getFilePos()) != diff)
            {
               throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED_INCORRECT_OFFSETS.getMsg());
            }
      
        }
        catch (Exception e)
        {
            if (e instanceof CannotWriteException)
            {
                throw (CannotWriteException) e;
            }
            else
            {
                throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED.getMsg()+":"+e.getMessage());
            }
        }
        finally
        {
            //Close references to new file
            rafTemp.close();
            fileWriteChannel.close();

        }
        logger.info("File has been written correctly");
    }


    /**
     * Delete the tag
     * <p/>
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
            write(tag, raf, rafTemp);
        }
        catch (CannotWriteException cwe)
        {
            throw new IOException(cwe.getMessage());
        }
    }
}
