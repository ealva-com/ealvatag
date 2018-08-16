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
package ealvatag.audio.aiff;

import com.ealva.ealvalog.LogLevel;
import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.audio.aiff.chunk.AiffChunkSummary;
import ealvatag.audio.aiff.chunk.AiffChunkType;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.aiff.AiffTag;

import static ealvatag.audio.iff.IffHeaderChunk.SIGNATURE_LENGTH;
import static ealvatag.audio.iff.IffHeaderChunk.SIZE_LENGTH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;


/**
 * Write Aiff Tag.
 */
public class AiffTagWriter {
  private static JLogger LOG = JLoggers.get(AiffTagWriter.class, EalvaTagLog.MARKER);

  /**
   * Delete given {@link Tag} from file.
   */
  public void delete(final Tag tag, FileChannel fc, final String fileName) throws CannotWriteException {
    try {
      LOG.log(LogLevel.WARN, "%s Deleting tag from file", fileName);
      final AiffTag existingTag = getExistingMetadata(fc, fileName);
      fc.position(0);
      if (existingTag.isExistingId3Tag() && existingTag.getID3Tag().getStartLocationInFile() != null) {
        ChunkHeader chunkHeader = seekToStartOfMetadata(fc, existingTag, fileName);
        if (isAtEndOfFileAllowingForPaddingByte(existingTag, fc)) {
          LOG.log(LogLevel.WARN, "%s Setting new length to:%d", fileName, existingTag.getStartLocationInFileOfId3Chunk());
          fc.truncate(existingTag.getStartLocationInFileOfId3Chunk());
        } else {
          LOG.log(LogLevel.WARN, "%s Deleting tag chunk", fileName);
          deleteTagChunk(fc, existingTag, chunkHeader, fileName);
        }
        rewriteRiffHeaderSize(fc);
      }
      LOG.log(LogLevel.WARN, "%s Deleted tag from file", fileName);
    } catch (IOException ioe) {
      throw new CannotWriteException(fileName + ":" + ioe.getMessage());
    }
  }

  /**
   * Read existing metadata
   *
   * @param channel
   * @param fileName
   *
   * @return tags within Tag wrapper
   *
   * @throws IOException
   * @throws CannotWriteException
   */
  private AiffTag getExistingMetadata(FileChannel channel, final String fileName)
      throws IOException, CannotWriteException {
    try {
      //Find AiffTag (if any)
      AiffTagReader im = new AiffTagReader();
      return im.read(channel, fileName);
    } catch (CannotReadException ex) {
      throw new CannotWriteException(fileName + " Failed to read file");
    }
  }

  /**
   * Seek in file to start of LIST Metadata chunk
   *
   * @param fc
   * @param existingTag
   *
   * @throws IOException
   * @throws CannotWriteException
   */
  private ChunkHeader seekToStartOfMetadata(FileChannel fc, AiffTag existingTag, String fileName)
      throws IOException, CannotWriteException {
    fc.position(existingTag.getStartLocationInFileOfId3Chunk());
    final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
    chunkHeader.readHeader(fc);
    fc.position(fc.position() - ChunkHeader.CHUNK_HEADER_SIZE);

    if (!AiffChunkType.TAG.getCode().equals(chunkHeader.getID())) {
      throw new CannotWriteException(fileName + " Unable to find ID3 chunk at expected location:" +
                                         existingTag.getStartLocationInFileOfId3Chunk());
    }
    return chunkHeader;
  }

  /**
   * @param existingTag
   * @param fc
   *
   * @return true if at end of file (also take into account padding byte)
   *
   * @throws IOException
   */
  private boolean isAtEndOfFileAllowingForPaddingByte(AiffTag existingTag, FileChannel fc) throws IOException {
    return ((existingTag.getID3Tag().getEndLocationInFile() == fc.size()) ||
        (Utils.isOddLength(existingTag.getID3Tag().getEndLocationInFile()) &&
            existingTag.getID3Tag().getEndLocationInFile() + 1 == fc.size()));
  }

  /**
   * <p>Deletes the given ID3-{@link Tag}/{@link Chunk} from the file by moving all following chunks up.</p>
   * <pre>
   * [chunk][-id3-][chunk][chunk]
   * [chunk] &lt;&lt;--- [chunk][chunk]
   * [chunk][chunk][chunk]
   * </pre>
   *
   * @param fc,            filechannel
   * @param existingTag    existing tag
   * @param tagChunkHeader existing chunk header for the tag
   *
   * @throws IOException if something goes wrong
   */
  private void deleteTagChunk(FileChannel fc,
                              final AiffTag existingTag,
                              final ChunkHeader tagChunkHeader,
                              String fileName) throws IOException {
    int lengthTagChunk = (int)tagChunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE;
    if (Utils.isOddLength(lengthTagChunk)) {
      if (existingTag.getStartLocationInFileOfId3Chunk() + lengthTagChunk < fc.size()) {
        lengthTagChunk++;
      }
    }
    final long newLength = fc.size() - lengthTagChunk;
    LOG.log(LogLevel.ERROR, "%s Size of id3 chunk to delete is:%d:location:%d",
            fileName,
            lengthTagChunk,
            existingTag.getStartLocationInFileOfId3Chunk());

    // position for reading after the id3 tag
    fc.position(existingTag.getStartLocationInFileOfId3Chunk() + lengthTagChunk);

    deleteTagChunkUsingSmallByteBufferSegments(existingTag, fc, newLength, lengthTagChunk);
    // truncate the file after the last chunk
    LOG.log(LogLevel.ERROR, "%s Setting new length to %d", fileName, newLength);
    fc.truncate(newLength);
  }

  /**
   * Use ByteBuffers to copy a 4mb chunk, write the chunk and repeat until the rest of the file after the ID3 tag
   * is rewritten
   *
   * @param existingTag    existing tag
   * @param channel        channel
   * @param newLength      new length
   * @param lengthTagChunk length tag chunk
   *
   * @throws IOException if something goes wrong
   */
  // TODO: arguments are not used, position is implicit
  private void deleteTagChunkUsingSmallByteBufferSegments(final AiffTag existingTag,
                                                          final FileChannel channel,
                                                          final long newLength,
                                                          final long lengthTagChunk)
      throws IOException {
    final ByteBuffer buffer = ByteBuffer.allocateDirect((int)TagOptionSingleton.getInstance().getWriteChunkSize());
    while (channel.read(buffer) >= 0 || buffer.position() != 0) {
      buffer.flip();
      final long readPosition = channel.position();
      channel.position(readPosition - lengthTagChunk - buffer.limit());
      channel.write(buffer);
      channel.position(readPosition);
      buffer.compact();
    }
  }

  /**
   * Rewrite RAF header to reflect new file length
   *
   * @param fc
   *
   * @throws IOException
   */
  private void rewriteRiffHeaderSize(FileChannel fc) throws IOException {

    fc.position(IffHeaderChunk.SIGNATURE_LENGTH);
    ByteBuffer bb = ByteBuffer.allocateDirect(IffHeaderChunk.SIZE_LENGTH);
    bb.order(ByteOrder.BIG_ENDIAN);
    int size = ((int)fc.size()) - SIGNATURE_LENGTH - SIZE_LENGTH;
    bb.putInt(size);
    bb.flip();
    fc.write(bb);
  }

  /**
   * The following seems to work on Windows but hangs on OSX!
   * Bug is filed <a href="https://bugs.openjdk.java.net/browse/JDK-8140241">here</a>.
   *
   * @param existingTag existing tag
   * @param channel     channel
   * @param newLength   new length
   *
   * @throws IOException if something goes wrong
   */
  private void deleteTagChunkUsingChannelTransfer(final AiffTag existingTag,
                                                  final FileChannel channel,
                                                  final long newLength)
      throws IOException {
    long read;
    //Read from just after the ID3Chunk into the channel at where the ID3 chunk started, should usually only
    // require one transfer
    //but put into loop in case multiple calls are required
    for (long position = existingTag.getStartLocationInFileOfId3Chunk();
         (read = channel.transferFrom(channel, position, newLength - position)) < newLength - position;
         position += read) {
      ;//is this problem if loop called more than once do we need to update position of channel to modify
    }
    //where write to ?
  }

  public void write(final Tag tag, FileChannel fc, final String fileName) throws CannotWriteException {
    LOG.log(LogLevel.ERROR, "%s Writing Aiff tag to file", fileName);
    AiffTag existingTag;
    try {
      existingTag = getExistingMetadata(fc, fileName);
      fc.position(0);
    } catch (IOException ioe) {
      throw new CannotWriteException(fileName + ":" + ioe.getMessage());
    }

    try {
      long existingFileLength = fc.size();

      final AiffTag aiffTag = (AiffTag)tag;
      final ByteBuffer bb = convert(aiffTag, existingTag);

      //Replacing ID3 tag
      if (existingTag.isExistingId3Tag() && existingTag.getID3Tag().getStartLocationInFile() != null) {
        //Usual case
        if (!existingTag.isIncorrectlyAlignedTag()) {
          final ChunkHeader chunkHeader = seekToStartOfMetadata(fc, existingTag, fileName);
          LOG.log(LogLevel.INFO, " %s Current Space allocated:%d new tag requires:%d",
                  fileName,
                  existingTag.getSizeOfID3TagOnly(),
                  bb.limit());

          //Usual case ID3 is last chunk
          if (isAtEndOfFileAllowingForPaddingByte(existingTag, fc)) {
            writeDataToFile(fc, bb);
          }
          //Unusual Case where ID3 is not last chunk
          else {
            deleteTagChunk(fc, existingTag, chunkHeader, fileName);
            fc.position(fc.size());
            writeExtraByteIfChunkOddSize(fc, fc.size());
            writeDataToFile(fc, bb);
          }
        }
        //Existing ID3 tag is incorrectly aligned so if we can lets delete it and any subsequentially added
        //ID3 tags as we only want one ID3 tag.
        else if (AiffChunkSummary.isOnlyMetadataTagsAfterStartingMetadataTag(existingTag)) {
          deleteRemainderOfFile(fc, existingTag, fileName);
          fc.position(fc.size());
          writeExtraByteIfChunkOddSize(fc, fc.size());
          writeDataToFile(fc, bb);
        } else {
          throw new CannotWriteException(
              fileName + " Metadata tags are corrupted and not at end of fc so cannot be fixed");
        }
      }
      //New Tag
      else {
        fc.position(fc.size());
        if (Utils.isOddLength(fc.size())) {
          fc.write(ByteBuffer.allocateDirect(1));
        }
        writeDataToFile(fc, bb);
      }

      if (existingFileLength != fc.size()) {
        rewriteRiffHeaderSize(fc);
      }
    } catch (IOException ioe) {
      throw new CannotWriteException(fileName + ":" + ioe.getMessage());
    }
  }

  /**
   * If Metadata tags are corrupted and no other tags later in the file then just truncate ID3 tags and start again
   */
  private void deleteRemainderOfFile(FileChannel fc, final AiffTag existingTag, String fileName) throws IOException {
    ChunkSummary precedingChunk = AiffChunkSummary.getChunkBeforeStartingMetadataTag(existingTag);
    if (precedingChunk != null) {
      if (!Utils.isOddLength(precedingChunk.getEndLocation())) {
        LOG.log(LogLevel.ERROR, "%s Truncating corrupted ID3 tags from:%d", fileName, existingTag.getStartLocationInFileOfId3Chunk() - 1);
        fc.truncate(existingTag.getStartLocationInFileOfId3Chunk() - 1);
      } else {
        LOG.log(LogLevel.ERROR, "%s Truncating corrupted ID3 tags from:%d", fileName, existingTag.getStartLocationInFileOfId3Chunk());
        fc.truncate(existingTag.getStartLocationInFileOfId3Chunk());
      }
    } else {
      LOG.log(LogLevel.ERROR, "%s No preceding chunk trying to delete remainder of file", fileName);
    }
  }

  /**
   * Writes data as a {@link ealvatag.audio.aiff.chunk.AiffChunkType#TAG} chunk to the file.
   *
   * @param fc filechannel
   * @param bb data to write
   *
   * @throws IOException
   */
  private void writeDataToFile(FileChannel fc, final ByteBuffer bb)
      throws IOException {
    final ChunkHeader ch = new ChunkHeader(ByteOrder.BIG_ENDIAN);
    ch.setID(AiffChunkType.TAG.getCode());
    ch.setSize(bb.limit());
    fc.write(ch.writeHeader());
    fc.write(bb);
    writeExtraByteIfChunkOddSize(fc, bb.limit());
  }

  /**
   * Chunk must also start on an even byte so if our chunksize is odd we need
   * to write another byte. This should never happen as ID3Tag is now amended
   * to ensure always write padding byte if needed to stop it being odd sized
   * but we keep check in just incase.
   *
   * @param fc
   * @param size
   *
   * @throws IOException
   */
  private void writeExtraByteIfChunkOddSize(FileChannel fc, long size)
      throws IOException {
    if (Utils.isOddLength(size)) {
      fc.write(ByteBuffer.allocateDirect(1));
    }
  }

  /**
   * Converts tag to {@link ByteBuffer}.
   *
   * @param tag         tag
   * @param existingTag
   *
   * @return byte buffer containing the tag data
   *
   * @throws UnsupportedEncodingException
   */
  public ByteBuffer convert(final AiffTag tag, AiffTag existingTag) throws UnsupportedEncodingException {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      long existingTagSize = existingTag.getSizeOfID3TagOnly();

      //If existingTag is uneven size lets make it even
      if (existingTagSize > 0) {
        if ((existingTagSize & 1) != 0) {
          existingTagSize++;
        }
      }

      //Write Tag to buffer
      tag.getID3Tag().write(baos, (int)existingTagSize);

      //If the tag is now odd because we needed to increase size and the data made it odd sized
      //we redo adding a padding byte to make it even
      if ((baos.toByteArray().length & 1) != 0) {
        int newSize = baos.toByteArray().length + 1;
        baos = new ByteArrayOutputStream();
        tag.getID3Tag().write(baos, newSize);
      }
      final ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
      buf.rewind();
      return buf;
    } catch (IOException ioe) {
      //Should never happen as not writing to file at this point
      throw new RuntimeException(ioe);
    }
  }
}

