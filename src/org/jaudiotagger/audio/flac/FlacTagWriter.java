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
package org.jaudiotagger.audio.flac;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.NoWritePermissionsException;
import org.jaudiotagger.audio.flac.metadatablock.*;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Write Flac Tag
 */
public class FlacTagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac");
    private FlacTagCreator tc = new FlacTagCreator();

    /**
     *
     * @param tag
     * @param file
     * @throws IOException
     * @throws CannotWriteException
     */
    public void delete(Tag tag, Path file) throws CannotWriteException
    {
        //This will save the file without any Comment or PictureData blocks  
        FlacTag emptyTag = new FlacTag(null, new ArrayList<MetadataBlockDataPicture>());
        write(emptyTag, file);
    }

    private static class MetadataBlockInfo {
	    private MetadataBlock       streamInfoBlock;
	    private List<MetadataBlock> metadataBlockPadding = new ArrayList<MetadataBlock>(1);
	    private List<MetadataBlock> metadataBlockApplication = new ArrayList<MetadataBlock>(1);
	    private List<MetadataBlock> metadataBlockSeekTable = new ArrayList<MetadataBlock>(1);
	    private List<MetadataBlock> metadataBlockCueSheet = new ArrayList<MetadataBlock>(1);
    }

    /**
     *
     * @param tag
     * @param file
     * @throws CannotWriteException
     * @throws IOException
     */
    public void write(Tag tag, Path file) throws CannotWriteException
    {
        logger.config(file + " Writing tag");
        try(FileChannel fc = FileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.READ))
        {
            MetadataBlockInfo blockInfo = new MetadataBlockInfo();

            //Read existing data
            FlacStreamReader flacStream = new FlacStreamReader(fc, file.toString() + " ");
            try
            {
                flacStream.findStream();
            }
            catch (CannotReadException cre)
            {
                throw new CannotWriteException(cre.getMessage());
            }

            boolean isLastBlock = false;
            while (!isLastBlock)
            {
                try
                {
                    MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
                    if (mbh.getBlockType() != null)
                    {
                        switch (mbh.getBlockType())
                        {
                            case STREAMINFO:
                            {
                                blockInfo.streamInfoBlock = new MetadataBlock(mbh, new MetadataBlockDataStreamInfo(mbh, fc));
                                break;
                            }

                            case VORBIS_COMMENT:
                            case PADDING:
                            case PICTURE:
                            {
                                //All these will be replaced by the new metadata so we just treat as padding in order
                                //to determine how much space is already allocated in the file
                                fc.position(fc.position() + mbh.getDataLength());
                                MetadataBlockData mbd = new MetadataBlockDataPadding(mbh.getDataLength());
                                blockInfo.metadataBlockPadding.add(new MetadataBlock(mbh, mbd));
                                break;
                            }
                            case APPLICATION:
                            {
                                MetadataBlockData mbd = new MetadataBlockDataApplication(mbh, fc);
                                blockInfo.metadataBlockApplication.add(new MetadataBlock(mbh, mbd));
                                break;
                            }
                            case SEEKTABLE:
                            {
                                MetadataBlockData mbd = new MetadataBlockDataSeekTable(mbh, fc);
                                blockInfo.metadataBlockSeekTable.add(new MetadataBlock(mbh, mbd));
                                break;
                            }
                            case CUESHEET:
                            {
                                MetadataBlockData mbd = new MetadataBlockDataCueSheet(mbh, fc);
                                blockInfo.metadataBlockCueSheet.add(new MetadataBlock(mbh, mbd));
                                break;
                            }
                            default:
                            {
                                //What are the consequences of doing this
                                fc.position(fc.position() + mbh.getDataLength());
                                break;
                            }
                        }
                    }
                    isLastBlock = mbh.isLastBlock();
                }
                catch (CannotReadException cre)
                {
                    throw new CannotWriteException(cre.getMessage());
                }
            }

            //Number of bytes in the existing file available before audio data
            int availableRoom = computeAvailableRoom(blockInfo);

            //Minimum Size of the New tag data without padding
            int newTagSize = tc.convert(tag).limit();
            //Number of bytes required for new tagdata and other metadata blocks
            int neededRoom = newTagSize + computeNeededRoom(blockInfo);

            //Go to start of Flac within file
            fc.position(flacStream.getStartOfFlacInFile());

            logger.config(file + "Writing tag available bytes:" + availableRoom + ":needed bytes:" + neededRoom);

            //There is enough room to fit the tag without moving the audio just need to
            //adjust padding accordingly need to allow space for padding header if padding required
            if ((availableRoom == neededRoom) || (availableRoom > neededRoom + MetadataBlockHeader.HEADER_LENGTH))
            {
                logger.config(file + "Room to Rewrite");
                //Jump over Id3 (if exists) and flac header
                fc.position(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);
                writeOtherMetadataBlocks(fc, blockInfo);

                //Write tag (and padding)
                fc.write(tc.convert(tag, availableRoom - neededRoom));
            }
            //Need to move audio
            else
            {
                logger.config(file + " No Room to Rewrite");
                //Find end of metadata bloacks (start of Audio)
                fc.position(flacStream.getStartOfFlacInFile()
                        + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH
                        + MetadataBlockHeader.HEADER_LENGTH // this should be the length of the block header for the stream info
                        + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH
                        + availableRoom);
                
                //And copy into Buffer, because direct buffer doesnt use heap, Flacs can be alrge
                //and this will require some memory but it is alot simpler and faster tahn faffing about
                //with temporary files
                ByteBuffer audioData = ByteBuffer.allocateDirect((int)(fc.size() - fc.position()));
                fc.read(audioData);
                audioData.flip();

                //Jump over Id3 (if exists) Flac Header
                fc.position(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);
                writeOtherMetadataBlocks(fc, blockInfo);
                
                //Write tag (and add some default padding)
                fc.write(tc.convert(tag,  FlacTagCreator.DEFAULT_PADDING));

                //Write Audio
                fc.write(audioData);
            }
        }
        catch(AccessDeniedException ade)
        {
            throw new NoWritePermissionsException(file + ":" + ade.getMessage());
        }
        catch(IOException ioe)
        {
            throw new CannotWriteException(file + ":" + ioe.getMessage());
        }
    }

    /**
     * Write all metadata blocks except for the the actual tag metadata
     *
     * We always write blocks in this order
     *
     * @param fc
     * @param blockInfo
     * @throws IOException
     */
    private void writeOtherMetadataBlocks(FileChannel fc, MetadataBlockInfo blockInfo) throws IOException
    {
        //Write StreamInfo, we always write this first even if wasn't first in original spec
        fc.write(ByteBuffer.wrap(blockInfo.streamInfoBlock.getHeader().getBytesWithoutIsLastBlockFlag()));
        fc.write(blockInfo.streamInfoBlock.getData().getBytes());

        //Write Application Blocks
        for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication)
        {
            fc.write(ByteBuffer.wrap(aMetadataBlockApplication.getHeader().getBytesWithoutIsLastBlockFlag()));
            fc.write(aMetadataBlockApplication.getData().getBytes());
        }

        //Write Seek Table Blocks
        for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable)
        {
            fc.write(ByteBuffer.wrap(aMetadataBlockSeekTable.getHeader().getBytesWithoutIsLastBlockFlag()));
            fc.write(aMetadataBlockSeekTable.getData().getBytes());
        }

        //Write Cue sheet Blocks
        for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet)
        {
            fc.write(ByteBuffer.wrap(aMetadataBlockCueSheet.getHeader().getBytesWithoutIsLastBlockFlag()));
            fc.write(aMetadataBlockCueSheet.getData().getBytes());
        }
    }

    /**
     * @param blockInfo 
     * @return space currently available for writing all Flac metadatablocks except for StreamInfo which is fixed size
     */
    private int computeAvailableRoom(MetadataBlockInfo blockInfo)
    {
        int length = 0;

        for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication)
        {
            length += aMetadataBlockApplication.getLength();
        }

        for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable)
        {
            length += aMetadataBlockSeekTable.getLength();
        }

        for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet)
        {
            length += aMetadataBlockCueSheet.getLength();
        }

        for (MetadataBlock aMetadataBlockPadding : blockInfo.metadataBlockPadding)
        {
            length += aMetadataBlockPadding.getLength();
        }

        return length;
    }

    /**
     * @param blockInfo 
     * @return space required to write the metadata blocks that are part of Flac but are not part of tagdata
     *         in the normal sense.
     */
    private int computeNeededRoom(MetadataBlockInfo blockInfo)
    {
        int length = 0;

        for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication)
        {
            length += aMetadataBlockApplication.getLength();
        }

        for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable)
        {
            length += aMetadataBlockSeekTable.getLength();
        }

        for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet)
        {
            length += aMetadataBlockCueSheet.getLength();
        }

        return length;
    }
}

