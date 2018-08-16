/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
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
package ealvatag.audio.wav;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.Chunk;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.ChunkSummary;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.audio.wav.chunk.WavId3Chunk;
import ealvatag.audio.wav.chunk.WavListChunk;
import ealvatag.logging.Hex;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.wav.WavInfoTag;
import ealvatag.tag.wav.WavTag;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Read the Wav file chunks, until finds WavFormatChunk and then generates AudioHeader from it
 */
public class WavTagReader {
  private static JLogger LOG = JLoggers.get(WavTagReader.class, EalvaTagLog.MARKER);

  private String loggingName;

  public WavTagReader(String loggingName) {
    this.loggingName = loggingName;
  }


  public WavTag read(FileChannel fc) throws CannotReadException, IOException {
    LOG.log(DEBUG, loggingName + " Read Tag:start");
    WavTag tag = new WavTag(TagOptionSingleton.getInstance().getWavOptions());
    if (WavRIFFHeader.isValidHeader(fc)) {
      while (fc.position() < fc.size()) {
        if (!readChunk(fc, tag)) {
          break;
        }
      }
    } else {
      throw new CannotReadException(loggingName + " Wav RIFF Header not valid");
    }
    createDefaultMetadataTagsIfMissing(tag);
    LOG.log(DEBUG, loggingName + " Read Tag:end");
    return tag;
  }

  /**
   * So if the file doesn't contain (both) types of metadata we construct them so data can be
   * added and written back to file on save
   *
   * @param tag
   */
  private void createDefaultMetadataTagsIfMissing(WavTag tag) {
    if (!tag.isExistingId3Tag()) {
      tag.setID3Tag(WavTag.createDefaultID3Tag());
    }
    if (!tag.isExistingInfoTag()) {
      tag.setInfoTag(new WavInfoTag());
    }
  }

  /**
   * Reads Wavs Chunk that contain tag metadata
   * <p>
   * If the same chunk exists more than once in the file we would just use the last occurence
   *
   * @param tag
   *
   * @return
   *
   * @throws IOException
   */
  protected boolean readChunk(FileChannel fc, WavTag tag) throws IOException, CannotReadException {
    Chunk chunk;
    ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
    if (!chunkHeader.readHeader(fc)) {
      return false;
    }

    String id = chunkHeader.getID();
    LOG.log(DEBUG, loggingName + " Next Id is:" + id + ":FileLocation:" + fc.position() + ":Size:" +
        chunkHeader.getSize());
    final WavChunkType chunkType = WavChunkType.get(id);
    if (chunkType != null) {
      switch (chunkType) {
        case LIST:
          tag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));
          if (tag.getInfoTag() == null) {
            chunk = new WavListChunk(loggingName,
                                     Utils.readFileDataIntoBufferLE(fc, (int)chunkHeader.getSize()),
                                     chunkHeader,
                                     tag);
            if (!chunk.readChunk()) {
              return false;
            }
          } else {
            LOG.log(WARN,
                    loggingName + " Ignoring LIST chunk because already have one:" + chunkHeader.getID()
                        + ":" + Hex.asDecAndHex(chunkHeader.getStartLocationInFile() - 1)
                        + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
          }
          break;

        case CORRUPT_LIST:
          LOG.log(ERROR,
                  loggingName + " Found Corrupt LIST Chunk, starting at Odd Location:" + chunkHeader.getID() +
                      ":" + chunkHeader.getSize());

          if (tag.getInfoTag() == null && tag.getID3Tag() == null) {
            tag.setIncorrectlyAlignedTag(true);
          }
          fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE - 1));
          return true;

        case ID3:
          tag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));
          if (tag.getID3Tag() == null) {
            chunk = new WavId3Chunk(Utils.readFileDataIntoBufferLE(fc, (int)chunkHeader.getSize()),
                                    chunkHeader,
                                    tag);
            if (!chunk.readChunk()) {
              return false;
            }
          } else {
            LOG.log(WARN,
                    loggingName + " Ignoring id3 chunk because already have one:" + chunkHeader.getID() +
                        ":"
                        + Hex.asDecAndHex(chunkHeader.getStartLocationInFile())
                        + ":sizeIncHeader:" + (chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE));
          }
          break;

        case CORRUPT_ID3_EARLY:
          LOG.log(ERROR,
                  loggingName + " Found Corrupt id3 chunk, starting at Odd Location:" + chunkHeader.getID() +
                      ":" + chunkHeader.getSize());
          if (tag.getInfoTag() == null && tag.getID3Tag() == null) {
            tag.setIncorrectlyAlignedTag(true);
          }
          fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE - 1));
          return true;

        case CORRUPT_ID3_LATE:
          LOG.log(ERROR,
                  loggingName + " Found Corrupt id3 chunk, starting at Odd Location:" + chunkHeader.getID() +
                      ":" + chunkHeader.getSize());
          if (tag.getInfoTag() == null && tag.getID3Tag() == null) {
            tag.setIncorrectlyAlignedTag(true);
          }
          fc.position(fc.position() - (ChunkHeader.CHUNK_HEADER_SIZE - 1));
          return true;

        default:
          tag.addChunkSummary(new ChunkSummary(chunkHeader.getID(),
                                               chunkHeader.getStartLocationInFile(),
                                               chunkHeader.getSize()));
          fc.position(fc.position() + chunkHeader.getSize());
      }
    }
    //Unknown chunk type just skip
    else {
      if (chunkHeader.getSize() < 0) {
        String msg = loggingName + " Not a valid header, unable to read a sensible size:Header"
            + chunkHeader.getID() + "Size:" + chunkHeader.getSize();
        LOG.log(ERROR, msg);
        throw new CannotReadException(msg);
      }
      LOG.log(DEBUG, loggingName + " Skipping chunk bytes:" + chunkHeader.getSize() + "for" + chunkHeader.getID());
      fc.position(fc.position() + chunkHeader.getSize());
      if (fc.position() > fc.size()) {
        String msg = loggingName + " Failed to move to invalid position to " + fc.position() +
            " because file length is only " + fc.size()
            + " indicates invalid chunk";
        LOG.log(ERROR, msg);
        throw new CannotReadException(msg);
      }
    }
    IffHeaderChunk.ensureOnEqualBoundary(fc, chunkHeader);
    return true;
  }
}
