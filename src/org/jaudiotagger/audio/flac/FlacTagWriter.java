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

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.flac.metadatablock.*;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.flac.FlacTag;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Write Flac Tag
 */
public class FlacTagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.flac");

    private FlacTagCreator tc = new FlacTagCreator();

    private static final String TEMP_FILENAME_SUFFIX = ".tmp";

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

        //Maybe needed if not enough
        File tmpFile = null;

        try(FileChannel fc = FileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.READ))
        {
            MetadataBlockInfo blockInfo = new MetadataBlockInfo();

            //Read existing data
            FlacStreamReader flacStream = new FlacStreamReader(fc);
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
                logger.config(file + "Room to Rerwrite");
                //Jump over Id3 (if exists) Flac and StreamInfoBlock
                fc.position(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);

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

                //Write tag (and padding)
                fc.write(tc.convert(tag, availableRoom - neededRoom));
            }
            //Need to move audio
            else
            {
                logger.config(file + " No Room to Rerwrite");
                tmpFile = File.createTempFile(file.toFile().getName().replace('.', '_'), TEMP_FILENAME_SUFFIX, file.getParent().toFile());
                try(FileChannel fTmp = FileChannel.open(tmpFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ))
                {
                    //If Flac tag contains ID3header or something before start of official Flac header copy it over
                    if (flacStream.getStartOfFlacInFile() > 0)
                    {
                        fc.position(0);
                        fTmp.transferFrom(fc, 0, flacStream.getStartOfFlacInFile());
                        fTmp.position(flacStream.getStartOfFlacInFile());
                    }
                    fTmp.write(ByteBuffer.wrap(FlacStreamReader.FLAC_STREAM_IDENTIFIER.getBytes(StandardCharsets.US_ASCII)));
                    fTmp.write(ByteBuffer.allocate(1));  //To ensure never set Last-metadata-block flag even if was before

                    int uptoStreamHeaderSize = flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.BLOCK_TYPE_LENGTH;
                    fTmp.position(uptoStreamHeaderSize);
                    fc.position(uptoStreamHeaderSize);

                    fTmp.transferFrom(fc, uptoStreamHeaderSize, MetadataBlockHeader.BLOCK_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH);

                    int dataStartSize = flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.HEADER_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH;
                    fTmp.position(dataStartSize);

                    //Write all the metadatablocks
                    for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication)
                    {
                        fTmp.write(ByteBuffer.wrap(aMetadataBlockApplication.getHeader().getBytesWithoutIsLastBlockFlag()));
                        fTmp.write(aMetadataBlockApplication.getData().getBytes());
                    }

                    for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable)
                    {
                        fTmp.write(ByteBuffer.wrap(aMetadataBlockSeekTable.getHeader().getBytesWithoutIsLastBlockFlag()));
                        fTmp.write(aMetadataBlockSeekTable.getData().getBytes());
                    }

                    for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet)
                    {
                        fTmp.write(ByteBuffer.wrap(aMetadataBlockCueSheet.getHeader().getBytesWithoutIsLastBlockFlag()));
                        fTmp.write(aMetadataBlockCueSheet.getData().getBytes());
                    }

                    //Write tag data use default padding
                    fTmp.write(tc.convert(tag, FlacTagCreator.DEFAULT_PADDING));
                    //Write audio to new file
                    fc.position(dataStartSize + availableRoom);

                    //Issue #385
                    //Transfer 'size' bytes from raf at its current position to rafTemp at position but do it in batches
                    //to prevent OutOfMemory exceptions
                    long amountToBeWritten = fc.size() - fc.position();
                    long written = 0;
                    long chunksize = TagOptionSingleton.getInstance().getWriteChunkSize();
                    long count = amountToBeWritten / chunksize;
                    long mod = amountToBeWritten % chunksize;
                    for (int i = 0; i < count; i++)
                    {
                        written += fTmp.transferFrom(fc, fTmp.position(), chunksize);
                        fTmp.position(fTmp.position() + chunksize);
                    }
                    written += fTmp.transferFrom(fc, fTmp.position(), mod);
                    if (written != amountToBeWritten)
                    {
                        throw new CannotWriteException("Was meant to write " + amountToBeWritten + " bytes but only written " + written + " bytes");
                    }
                }
            }
        }
        catch(IOException ioe)
        {
            throw new CannotWriteException(file + ":" + ioe.getMessage());
        }

        //If we had to create a new file so now replace
        if (tmpFile != null)
        {
            swapTempFileAndOriginalFile(file.toFile(), tmpFile);
        }
    }

    /**
     * newFile contains the modified flac file and originalFile contains the original file, want to swap them round.
     *
     * @param originalFile
     * @param newFile
     * @throws CannotWriteException
     */
    private void swapTempFileAndOriginalFile(File originalFile, File newFile) throws CannotWriteException
    {
        // Rename Original File
        // Can fail on Vista if have Special Permission 'Delete' set Deny
        File originalFileBackup = new File(originalFile.getAbsoluteFile().getParentFile().getPath(),
                AudioFile.getBaseFilename(originalFile) + ".old");

        //If already exists modify the suffix
        int count=1;
        while(originalFileBackup.exists())
        {
            originalFileBackup = new File(originalFile.getAbsoluteFile().getParentFile().getPath(), AudioFile.getBaseFilename(originalFile)+ ".old"+count);
            count++;
        }

        boolean renameResult = Utils.rename(originalFile,originalFileBackup);
        if (!renameResult)
        {
            logger
                    .log(Level.SEVERE, ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP
                            .getMsg(originalFile.getAbsolutePath(), originalFileBackup.getName()));
            //Delete the temp file because write has failed
            if(newFile!=null)
            {
                newFile.delete();
            }
            throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP
                    .getMsg(originalFile.getPath(), originalFileBackup.getName()));
        }

        // Rename Temp File to Original File
        renameResult = Utils.rename(newFile,originalFile);
        if (!renameResult)
        {
            // Renamed failed so lets do some checks rename the backup back to the original file
            // New File doesnt exist
            if (!newFile.exists())
            {
                logger
                        .warning(ErrorMessage.GENERAL_WRITE_FAILED_NEW_FILE_DOESNT_EXIST
                                .getMsg(newFile.getAbsolutePath()));
            }

            // Rename the backup back to the original
            if (!originalFileBackup.renameTo(originalFile))
            {
                // TODO now if this happens we are left with testfile.old
                // instead of testfile.mp4
                logger
                        .warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL
                                .getMsg(originalFileBackup.getAbsolutePath(), originalFile.getName()));
            }

            logger
                    .warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE
                            .getMsg(originalFile.getAbsolutePath(), newFile.getName()));
            throw new CannotWriteException(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE
                    .getMsg(originalFile.getAbsolutePath(), newFile
                            .getName()));
        }
        else
        {
            // Rename was okay so we can now delete the backup of the
            // original
            boolean deleteResult = originalFileBackup.delete();
            if (!deleteResult)
            {
                // Not a disaster but can't delete the backup so make a
                // warning
                logger
                        .warning(ErrorMessage.GENERAL_WRITE_WARNING_UNABLE_TO_DELETE_BACKUP_FILE
                                .getMsg(originalFileBackup
                                        .getAbsolutePath()));
            }
        }

        // Delete the temporary file if still exists
        if (newFile.exists())
        {
            if (!newFile.delete())
            {
                // Non critical failed deletion
                logger
                        .warning(ErrorMessage.GENERAL_WRITE_FAILED_TO_DELETE_TEMPORARY_FILE
                                .getMsg(newFile.getPath()));
            }
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

