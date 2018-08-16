/*
 * Entagged Audio Tag library
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package ealvatag.audio.asf;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileImpl;
import ealvatag.audio.AudioFileReader;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.asf.data.AsfHeader;
import ealvatag.audio.asf.data.AudioStreamChunk;
import ealvatag.audio.asf.data.MetadataContainer;
import ealvatag.audio.asf.data.MetadataDescriptor;
import ealvatag.audio.asf.io.AsfExtHeaderReader;
import ealvatag.audio.asf.io.AsfHeaderReader;
import ealvatag.audio.asf.io.ChunkReader;
import ealvatag.audio.asf.io.ContentBrandingReader;
import ealvatag.audio.asf.io.ContentDescriptionReader;
import ealvatag.audio.asf.io.FileHeaderReader;
import ealvatag.audio.asf.io.FullRequestInputStream;
import ealvatag.audio.asf.io.LanguageListReader;
import ealvatag.audio.asf.io.MetadataReader;
import ealvatag.audio.asf.io.StreamChunkReader;
import ealvatag.audio.asf.util.TagConverter;
import ealvatag.audio.asf.util.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.logging.ErrorMessage;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagException;
import ealvatag.tag.asf.AsfTag;

import static com.ealva.ealvalog.LogLevel.ERROR;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * This reader can read ASF files containing any content (stream type). <br>
 *
 * @author Christian Laireiter
 */
public class AsfFileReader extends AudioFileReader {

  /**
   * Logger instance
   */
  private final static JLogger LOG = JLoggers.get(AsfFileReader.class, EalvaTagLog.MARKER);

  /**
   * This reader will be configured to read tag and audio header information.<br>
   */
  private final static AsfHeaderReader HEADER_READER;

  static {
    final List<Class<? extends ChunkReader>> readers = new ArrayList<>();
    readers.add(ContentDescriptionReader.class);
    readers.add(ContentBrandingReader.class);
    readers.add(MetadataReader.class);
    readers.add(LanguageListReader.class);

    // Create the header extension object reader with just content
    // description reader as well
    // as extended content description reader.
    final AsfExtHeaderReader extReader = new AsfExtHeaderReader(readers, true);
    readers.add(FileHeaderReader.class);
    readers.add(StreamChunkReader.class);
    HEADER_READER = new AsfHeaderReader(readers, true);
    HEADER_READER.setExtendedHeaderReader(extReader);
  }

  /**
   * Determines if the &quot;isVbr&quot; field is set in the extended content
   * description.<br>
   *
   * @param header the header to look up.
   *
   * @return <code>true</code> if &quot;isVbr&quot; is present with a <code>true</code> value.
   */
  private boolean determineVariableBitrate(final AsfHeader header) {
    assert header != null;
    boolean result = false;
    final MetadataContainer extDesc = header.findExtendedContentDescription();
    if (extDesc != null) {
      final List<MetadataDescriptor> descriptors = extDesc.getDescriptorsByName("IsVBR");
      if (descriptors != null && !descriptors.isEmpty()) {
        result = Boolean.TRUE.toString().equals(descriptors.get(0).getString());
      }
    }
    return result;
  }

  /**
   * Creates a generic audio header instance with provided data from header.
   *
   * @param header ASF header which contains the information.
   *
   * @return generic audio header representation.
   *
   * @throws CannotReadException If header does not contain mandatory information. (Audio stream chunk and file header chunk)
   */
  private GenericAudioHeader getAudioHeader(final AsfHeader header) throws CannotReadException {
    final GenericAudioHeader info = new GenericAudioHeader();
    if (header.getFileHeader() == null) {
      throw new CannotReadException("Invalid ASF/WMA file. File header object not available.");
    }
    if (header.getAudioStreamChunk() == null) {
      throw new CannotReadException("Invalid ASF/WMA file. No audio stream contained.");
    }
    info.setBitRate(header.getAudioStreamChunk().getKbps());
    info.setChannelNumber((int)header.getAudioStreamChunk().getChannelCount());
    info.setEncodingType("ASF (audio): " + header.getAudioStreamChunk().getCodecDescription());
    info.setLossless(header.getAudioStreamChunk().getCompressionFormat() == AudioStreamChunk.WMA_LOSSLESS);
    info.setPreciseLength(header.getFileHeader().getPreciseDuration());
    info.setSamplingRate((int)header.getAudioStreamChunk().getSamplingRate());
    info.setVariableBitRate(determineVariableBitrate(header));
    info.setBitsPerSample(header.getAudioStreamChunk().getBitsPerSample());
    return info;
  }

  /**
   * (overridden)
   *
   * @see AudioFileReader#getEncodingInfo(java.io.RandomAccessFile)
   */
  @Override
  protected GenericAudioHeader getEncodingInfo(final RandomAccessFile raf) throws CannotReadException, IOException {
    raf.seek(0);
    GenericAudioHeader info;
    try {
      final AsfHeader header = AsfHeaderReader.readInfoHeader(raf);
      if (header == null) {
        throw new CannotReadException(
            "Some values must have been " + "incorrect for interpretation as asf with wma content.");
      }
      info = getAudioHeader(header);
    } catch (final Exception e) {
      if (e instanceof IOException) {
        throw (IOException)e;
      } else if (e instanceof CannotReadException) {
        throw (CannotReadException)e;
      } else {
        throw new CannotReadException(e, "Failed to read. Cause: " + e.getMessage());
      }
    }
    return info;
  }

  /**
   * Creates a tag instance with provided data from header.
   *
   * @param header ASF header which contains the information.
   *
   * @return generic audio header representation.
   */
  private AsfTag getTag(final AsfHeader header) {
    return TagConverter.createTagOf(header);
  }

  /**
   * (overridden)
   *
   * @see AudioFileReader#getTag(RandomAccessFile, boolean)
   */
  @Override
  protected AsfTag getTag(final RandomAccessFile raf, final boolean ignoreArtwork) throws CannotReadException, IOException {
    raf.seek(0);
    AsfTag tag;
    try {
      final AsfHeader header = AsfHeaderReader.readTagHeader(raf);
      if (header == null) {
        throw new CannotReadException(
            "Some values must have been " + "incorrect for interpretation as asf with wma content.");
      }

      tag = TagConverter.createTagOf(header);

    } catch (final RuntimeException e) {
      LOG.log(ERROR, e, "Unexpected error");
      throw new CannotReadException("Failed to read", e);
    }
    return tag;
  }

  @Override
  public AudioFile read(final File f, final String extension, final boolean ignoreArtwork)
      throws CannotReadException, IOException, TagException, InvalidAudioFrameException {
    try (InputStream stream = new FullRequestInputStream(new BufferedInputStream(new FileInputStream(f)))) {
      final AsfHeader header = HEADER_READER.read(Utils.readGUID(stream), stream, 0);
      if (header == null) {
        throw new CannotReadException(ErrorMessage.ASF_HEADER_MISSING, f);
      }
      if (header.getFileHeader() == null) {
        throw new CannotReadException(ErrorMessage.ASF_FILE_HEADER_MISSING, f);
      }

      // Just log a warning because file seems to play okay
      if (header.getFileHeader().getFileSize().longValue() != f.length()) {
        LOG.log(WARN, ErrorMessage.ASF_FILE_HEADER_SIZE_DOES_NOT_MATCH_FILE_SIZE, f, header.getFileHeader().getFileSize(), f.length());
      }

      return new AudioFileImpl(f, extension, getAudioHeader(header), getTag(header));
    }
  }

}
