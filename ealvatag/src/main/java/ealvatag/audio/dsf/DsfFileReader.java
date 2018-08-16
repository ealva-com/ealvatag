/*
 * Created on 03.05.2015
 * Author: Veselin Markov.
 */
package ealvatag.audio.dsf;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.WARN;

import ealvatag.audio.AudioFileReader2;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagException;
import ealvatag.tag.TagFieldContainer;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;

import static ealvatag.audio.dsf.DsdChunk.CHUNKSIZE_LENGTH;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Reads the ID3 Tags as specified by <a href=
 * "http://dsd-guide.com/sites/default/files/white-papers/DSFFileFormatSpec_E.pdf"
 * /> DSFFileFormatSpec_E.pdf </a>.
 *
 * @author Veselin Markov (veselin_m84 a_t yahoo.com)
 */
public class DsfFileReader extends AudioFileReader2 {
  private static final JLogger LOG = JLoggers.get(DsfFileReader.class, EalvaTagLog.MARKER);

  @Override
  protected GenericAudioHeader getEncodingInfo(FileChannel fc, final String fileName) throws CannotReadException, IOException {
    DsdChunk dsd = DsdChunk.readChunk(Utils.readFileDataIntoBufferLE(fc, DsdChunk.DSD_HEADER_LENGTH));
    if (dsd != null) {
      ByteBuffer fmtChunkBuffer = Utils.readFileDataIntoBufferLE(fc, IffHeaderChunk.SIGNATURE_LENGTH + CHUNKSIZE_LENGTH);
      FmtChunk fmt = FmtChunk.readChunkHeader(fmtChunkBuffer);
      if (fmt != null) {
        return fmt.readChunkData(dsd, fc);
      } else {
        throw new CannotReadException(fileName + " Not a valid dsf file. Content does not include 'fmt ' chunk");
      }
    } else {
      throw new CannotReadException(fileName + " Not a valid dsf file. Content does not start with 'DSD '");
    }
  }

  @Override
  protected TagFieldContainer getTag(FileChannel fc, final String fileName, final boolean ignoreArtwork)
      throws CannotReadException, IOException {
    DsdChunk dsd = DsdChunk.readChunk(Utils.readFileDataIntoBufferLE(fc, DsdChunk.DSD_HEADER_LENGTH));
    if (dsd != null) {
      return readTag(fc, dsd, fileName, ignoreArtwork);
    } else {
      throw new CannotReadException(fileName + " Not a valid dsf file. Content does not start with 'DSD '.");
    }
  }

  /**
   * Reads the ID3v2 tag starting at the {@code tagOffset} position in the
   * supplied file.
   *
   * @param fc            the filechannel from which to read
   * @param dsd           the dsd chunk
   * @param fileName
   * @param ignoreArtwork
   *
   * @return the read tag or an empty tag if something went wrong. Never <code>null</code>.
   *
   * @throws IOException if cannot read file.
   */
  private TagFieldContainer readTag(FileChannel fc, DsdChunk dsd, String fileName, final boolean ignoreArtwork)
      throws CannotReadException, IOException {
    if (dsd.getMetadataOffset() > 0) {
      fc.position(dsd.getMetadataOffset());
      ID3Chunk id3Chunk = ID3Chunk.readChunk(Utils.readFileDataIntoBufferLE(fc, (int)(fc.size() - fc.position())));
      if (id3Chunk != null) {
        int version = id3Chunk.getDataBuffer().get(AbstractID3v2Tag.FIELD_TAG_MAJOR_VERSION_POS);
        try {
          switch (version) {
            case ID3v22Tag.MAJOR_VERSION:
              return new ID3v22Tag(id3Chunk.getDataBuffer(), "");
            case ID3v23Tag.MAJOR_VERSION:
              return new ID3v23Tag(id3Chunk.getDataBuffer(), "");
            case ID3v24Tag.MAJOR_VERSION:
              return new ID3v24Tag(id3Chunk.getDataBuffer(), "");
            default:
              LOG.log(WARN, "%s Unknown ID3v2 version %d. Returning an empty ID3v2 Tag.", fileName, version);
              return null;
          }
        } catch (TagException e) {
          throw new CannotReadException(fileName + " Could not read ID3v2 tag:corruption");
        }
      } else {
        LOG.log(WARN, "%s No existing ID3 tag(1)", fileName);
        return null;
      }
    } else {
      LOG.log(WARN, "%s No existing ID3 tag(2)", fileName);
      return null;
    }
  }
}
