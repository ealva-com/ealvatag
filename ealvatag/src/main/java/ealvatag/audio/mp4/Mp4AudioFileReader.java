/*
 * Copyright (c) 2017 Eric A. Snell
 *
 * This file is part of eAlvaTag.
 *
 * eAlvaTag is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * eAlvaTag is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with eAlvaTag.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package ealvatag.audio.mp4;

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileImpl;
import ealvatag.audio.AudioFileReader;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.atom.Mp4BoxHeader;
import ealvatag.audio.mp4.atom.Mp4FtypBox;
import ealvatag.logging.ErrorMessage;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.TagFieldContainer;
import okio.BufferedSource;
import okio.Okio;

import static com.ealva.ealvalog.LogLevel.DEBUG;
import static com.ealva.ealvalog.LogLevel.WARN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Reads an Mp4 and parses it into an AudioFile
 * <p>
 * The audio header information we need is in the mvdh, mdhd, and minf boxes. General outline is:
 * <pre>
 * |--- ftyp
 * |--- moov
 * |......|
 * |......|----- mvdh
 * |......|----- trak
 * |...............|----- mdia
 * |.......................|---- mdhd
 * |.......................|---- minf
 * |..............................|---- smhd
 * |..............................|---- stbl
 * |......................................|--- stsd
 * |.............................................|--- mp4a
 * |......|----- udta
 * |
 * |--- mdat
 * </pre>
 * See <a href="http://l.web.umkc.edu/lizhu/teaching/2016sp.video-communication/ref/mp4.pdf">ISO/IEC 14496-12</a> for details
 * <p>
 * <p>
 * The metadata tags are usually held under the ilst box as shown below
 * <list>
 * Valid Exceptions to the rule:
 * <li>May be no udta box with meta. Instead meta is directly under moov</li>
 * <li>May be no udta/meta box at all</li>
 * </list>
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
 * |.........................|
 * |.........................|---- @nam (Optional for each metadatafield)
 * |.........................|.......|-- data
 * |.........................|....... ecetera
 * |.........................|---- ---- (Optional for reverse dns field)
 * |.................................|-- mean
 * |.................................|-- name
 * |.................................|-- data
 * |.................................... ecetere
 * |
 * |--- mdat
 * </pre
 *
 * See <a href="https://developer.apple.com/library/mac/documentation/QuickTime/QTFF/QTFFPreface/qtffPreface.html">Quick Time File Format
 * Specification</a> for details on metadata
 *
 * Created by Eric A. Snell on 2/3/17.
 */
public class Mp4AudioFileReader extends AudioFileReader {
  private static final JLogger LOG = JLoggers.get(Mp4AudioFileReader.class, EalvaTagLog.MARKER);

  // Almost a complete rewrite from the original which artificially separated header from tag parsing. This was causing the entire moov
  // box to be read into memory TWICE! I have seen this larger than 500KB in my own music library. That's opening a file, reading 500KB
  // and  parsing part of it, throwing it away, reloading the exact same data, and parsing a different part. It's now one pass. The
  // results on Android are, needless to say, quite an improvement. In batch processing of thousands of files, this makes a
  // big difference - no matter the platform.
  @Override protected GenericAudioHeader getEncodingInfo(final RandomAccessFile raf) throws CannotReadException, IOException {
    throw new UnsupportedOperationException("");
  }

  @Override protected TagFieldContainer getTag(final RandomAccessFile raf, final boolean ignoreArtwork)
      throws CannotReadException, IOException {
    throw new UnsupportedOperationException("");
  }

  public AudioFile read(final File file,
                        final String extension,
                        final boolean ignoreArtwork) throws CannotReadException, FileNotFoundException {
    try (BufferedSource bufferedSource = Okio.buffer(Okio.source(file))) {
      Mp4FtypBox mp4FtypBox = new Mp4FtypBox(bufferedSource);
      LOG.log(DEBUG, "%s", mp4FtypBox);

      Mp4BoxHeader boxHeader = new Mp4BoxHeader(bufferedSource);
      while (!Mp4AtomIdentifier.MOOV.getFieldName().equals(boxHeader.getId())) {
        LOG.log(WARN, "Expected %s found %s", Mp4AtomIdentifier.MOOV, boxHeader);
        bufferedSource.skip(boxHeader.getDataLength());
        boxHeader = new Mp4BoxHeader(bufferedSource);
      }
      Mp4MoovBox moovBox = new Mp4MoovBox(boxHeader, bufferedSource, mp4FtypBox, file.length(), ignoreArtwork);
      return new AudioFileImpl(file, extension, moovBox.getAudioHeader(), moovBox.getMp4Tag());
    } catch (FileNotFoundException e) {
      throw e;
    } catch (IOException e) {
      throw new CannotReadException(e, ErrorMessage.MP4_FILE_NOT_CONTAINER);
    }
  }
}
