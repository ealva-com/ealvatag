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
package ealvatag.audio.flac;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.TRACE;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.flac.metadatablock.MetadataBlock;
import ealvatag.audio.flac.metadatablock.MetadataBlockData;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataApplication;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataCueSheet;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPadding;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataSeekTable;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataStreamInfo;
import ealvatag.audio.flac.metadatablock.MetadataBlockHeader;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.Tag;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.flac.FlacTag;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Write Flac Tag
 */
public class FlacTagWriter {
  // Logger Object
  public static JLogger LOG = JLoggers.get(FlacTagWriter.class, EalvaTagLog.MARKER);
  private FlacTagCreator tc = new FlacTagCreator();

  /**
   * @param tag
   * @param channel
   * @param fileName
   *
   * @throws CannotWriteException
   */
  public void delete(Tag tag, FileChannel channel, final String fileName) throws CannotWriteException {
    //This will save the file without any Comment or PictureData blocks
    FlacTag emptyTag = new FlacTag(null, new ArrayList<MetadataBlockDataPicture>(), false);
    write(emptyTag, channel, fileName);
  }

  private static class MetadataBlockInfo {
    private MetadataBlock streamInfoBlock;
    private List<MetadataBlock> metadataBlockPadding = new ArrayList<>(1);
    private List<MetadataBlock> metadataBlockApplication = new ArrayList<>(1);
    private List<MetadataBlock> metadataBlockSeekTable = new ArrayList<>(1);
    private List<MetadataBlock> metadataBlockCueSheet = new ArrayList<>(1);
  }

  /**
   * @param tag
   * @param fc
   * @param fileName
   *
   * @throws CannotWriteException
   */
  public void write(TagFieldContainer tag, FileChannel fc, final String fileName) throws CannotWriteException {
    LOG.log(TRACE, "%s Writing tag", fileName);
    try {
      MetadataBlockInfo blockInfo = new MetadataBlockInfo();

      //Read existing data
      FlacStreamReader flacStream = new FlacStreamReader(fc, fileName + " ");
      try {
        flacStream.findStream();
      } catch (CannotReadException cre) {
        throw new CannotWriteException(cre.getMessage());
      }

      boolean isLastBlock = false;
      while (!isLastBlock) {
        try {
          MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
          if (mbh.getBlockType() != null) {
            switch (mbh.getBlockType()) {
              case STREAMINFO: {
                blockInfo.streamInfoBlock =
                    new MetadataBlock(mbh, new MetadataBlockDataStreamInfo(mbh, fc));
                break;
              }

              case VORBIS_COMMENT:
              case PADDING:
              case PICTURE: {
                //All these will be replaced by the new metadata so we just treat as padding in order
                //to determine how much space is already allocated in the file
                fc.position(fc.position() + mbh.getDataLength());
                MetadataBlockData mbd = new MetadataBlockDataPadding(mbh.getDataLength());
                blockInfo.metadataBlockPadding.add(new MetadataBlock(mbh, mbd));
                break;
              }
              case APPLICATION: {
                MetadataBlockData mbd = new MetadataBlockDataApplication(mbh, fc);
                blockInfo.metadataBlockApplication.add(new MetadataBlock(mbh, mbd));
                break;
              }
              case SEEKTABLE: {
                MetadataBlockData mbd = new MetadataBlockDataSeekTable(mbh, fc);
                blockInfo.metadataBlockSeekTable.add(new MetadataBlock(mbh, mbd));
                break;
              }
              case CUESHEET: {
                MetadataBlockData mbd = new MetadataBlockDataCueSheet(mbh, fc);
                blockInfo.metadataBlockCueSheet.add(new MetadataBlock(mbh, mbd));
                break;
              }
              default: {
                //What are the consequences of doing this
                fc.position(fc.position() + mbh.getDataLength());
                break;
              }
            }
          }
          isLastBlock = mbh.isLastBlock();
        } catch (CannotReadException cre) {
          throw new CannotWriteException(cre.getMessage());
        }
      }

      //Number of bytes in the existing file available before audio data
      int availableRoom = computeAvailableRoom(blockInfo);

      //Minimum Size of the New tag data without padding
      int newTagSize = tc.convert(tag).limit();

      //Other blocks required size
      int otherBlocksRequiredSize = computeNeededRoom(blockInfo);

      //Number of bytes required for new tagdata and other metadata blocks
      int neededRoom = newTagSize + otherBlocksRequiredSize;

      //Go to start of Flac within file
      fc.position(flacStream.getStartOfFlacInFile());

      LOG.log(TRACE, "%s:Writing tag available bytes:%s needed bytes:%s", fileName, availableRoom, neededRoom);

      //There is enough room to fit the tag without moving the audio just need to
      //adjust padding accordingly need to allow space for padding header if padding required
      if ((availableRoom == neededRoom) || (availableRoom > neededRoom + MetadataBlockHeader.HEADER_LENGTH)) {
        LOG.log(TRACE, "%s Room to Rewrite", fileName);
        //Jump over Id3 (if exists) and flac header
        fc.position(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);

        //Write stream info and other non metadata blocks
        writeOtherMetadataBlocks(fc, blockInfo);

        //Write tag (and padding)
        fc.write(tc.convert(tag, availableRoom - neededRoom));
      }
      //Need to move audio
      else {
        LOG.log(TRACE, "%s:Audio must be shifted NewTagSize:%s:AvailableRoom:%s:MinimumAdditionalRoomRequired:%s",
                fileName,
                newTagSize,
                availableRoom,
                neededRoom - availableRoom);
        //As we are having to both anyway may as well put in the default padding
        insertUsingChunks(fileName,
                          tag,
                          fc,
                          blockInfo,
                          flacStream,
                          neededRoom + FlacTagCreator.DEFAULT_PADDING,
                          availableRoom);
      }
    } catch (IOException ioe) {
      LOG.log(ERROR, "Cannot write %s %s", fileName, ioe.getMessage());
      throw new CannotWriteException(fileName + ":" + ioe.getMessage());
    }
  }


  /**
   * Insert metadata into space that is not large enough
   * <p>
   * We do this by reading/writing chunks of data allowing it to work on low memory systems
   * <p>
   * Chunk size defined by TagOptionSingleton.getInstance().getWriteChunkSize()
   */
  private void insertUsingChunks(String file,
                                 TagFieldContainer tag,
                                 FileChannel fc,
                                 MetadataBlockInfo blockInfo,
                                 FlacStreamReader flacStream,
                                 int neededRoom,
                                 int availableRoom) throws IOException {
    long originalFileSize = fc.size();

    //Find end of metadata blocks (start of Audio), i.e start of Flac + 4 bytes for 'fLaC', 4 bytes for
    // streaminfo header and
    //34 bytes for streaminfo and then size of all the other existing blocks
    long audioStart = flacStream.getStartOfFlacInFile()
        + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH
        + MetadataBlockHeader.HEADER_LENGTH
        + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH
        + availableRoom;

    //Extra Space Required for larger metadata block
    int extraSpaceRequired = neededRoom - availableRoom;
    LOG.log(TRACE, "%s Audio needs shifting:%s", file, extraSpaceRequired);

    //ChunkSize must be at least as large as the extra space required to write the metadata
    int chunkSize = (int)TagOptionSingleton.getInstance().getWriteChunkSize();
    if (chunkSize < extraSpaceRequired) {
      chunkSize = extraSpaceRequired;
    }

    Queue<ByteBuffer> queue = new LinkedBlockingQueue<>();

    //Read first chunk of audio
    fc.position(audioStart);
    {
      ByteBuffer audioBuffer = ByteBuffer.allocateDirect(chunkSize);
      fc.read(audioBuffer);
      audioBuffer.flip();
      queue.add(audioBuffer);
    }
    long readPosition = fc.position();

    //Jump over Id3 (if exists) and Flac Header
    fc.position(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);
    writeOtherMetadataBlocks(fc, blockInfo);
    fc.write(tc.convert(tag, FlacTagCreator.DEFAULT_PADDING));
    long writePosition = fc.position();


    fc.position(readPosition);
    while (fc.position() < originalFileSize) {
      //Read next chunk
      ByteBuffer audioBuffer = ByteBuffer.allocateDirect(chunkSize);
      fc.read(audioBuffer);
      readPosition = fc.position();
      audioBuffer.flip();
      queue.add(audioBuffer);

      //Write previous chunk
      fc.position(writePosition);
      fc.write(queue.remove());
      writePosition = fc.position();

      fc.position(readPosition);
    }
    fc.position(writePosition);
    fc.write(queue.remove());
  }

  /**
   * Write all metadata blocks except for the the actual tag metadata
   * <p>
   * We always write blocks in this order
   *
   * @param fc
   * @param blockInfo
   *
   * @throws IOException
   */
  private void writeOtherMetadataBlocks(FileChannel fc, MetadataBlockInfo blockInfo) throws IOException {
    //Write StreamInfo, we always write this first even if wasn't first in original spec
    fc.write(ByteBuffer.wrap(blockInfo.streamInfoBlock.getHeader().getBytesWithoutIsLastBlockFlag()));
    fc.write(blockInfo.streamInfoBlock.getData().getBytes());

    //Write Application Blocks
    for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication) {
      fc.write(ByteBuffer.wrap(aMetadataBlockApplication.getHeader().getBytesWithoutIsLastBlockFlag()));
      fc.write(aMetadataBlockApplication.getData().getBytes());
    }

    //Write Seek Table Blocks
    for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable) {
      fc.write(ByteBuffer.wrap(aMetadataBlockSeekTable.getHeader().getBytesWithoutIsLastBlockFlag()));
      fc.write(aMetadataBlockSeekTable.getData().getBytes());
    }

    //Write Cue sheet Blocks
    for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet) {
      fc.write(ByteBuffer.wrap(aMetadataBlockCueSheet.getHeader().getBytesWithoutIsLastBlockFlag()));
      fc.write(aMetadataBlockCueSheet.getData().getBytes());
    }
  }

  /**
   * @param blockInfo
   *
   * @return space currently available for writing all Flac metadatablocks except for StreamInfo which is fixed size
   */
  private int computeAvailableRoom(MetadataBlockInfo blockInfo) {
    int length = 0;

    for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication) {
      length += aMetadataBlockApplication.getLength();
    }

    for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable) {
      length += aMetadataBlockSeekTable.getLength();
    }

    for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet) {
      length += aMetadataBlockCueSheet.getLength();
    }

    //Note when reading metadata has been put into padding as well for purposes of write
    for (MetadataBlock aMetadataBlockPadding : blockInfo.metadataBlockPadding) {
      length += aMetadataBlockPadding.getLength();
    }

    return length;
  }

  /**
   * @param blockInfo
   *
   * @return space required to write the metadata blocks that are part of Flac but are not part of tagdata in the normal sense.
   */
  private int computeNeededRoom(MetadataBlockInfo blockInfo) {
    int length = 0;

    for (MetadataBlock aMetadataBlockApplication : blockInfo.metadataBlockApplication) {
      length += aMetadataBlockApplication.getLength();
    }

    for (MetadataBlock aMetadataBlockSeekTable : blockInfo.metadataBlockSeekTable) {
      length += aMetadataBlockSeekTable.getLength();
    }

    for (MetadataBlock aMetadataBlockCueSheet : blockInfo.metadataBlockCueSheet) {
      length += aMetadataBlockCueSheet.getLength();
    }

    return length;
  }
}

