/*
 * @author : Paul Taylor
 * <p>
 * Version @version:$Id$
 * <p>
 * Jaudiotagger Copyright (C)2004,2005
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public  License as
 * published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, you can get a copy from
 * http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA 02110-1301 USA
 * <p>
 * Description:
 */
package ealvatag.test;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileFilter;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.audio.SupportedFileFormat;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagException;

import static ealvatag.audio.SupportedFileFormat.fromExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Simple class that will attempt to recursively read all files within a directory, flags
 * errors that occur.
 */
public class TestAudioTagger {

  private static final EnumMap<SupportedFileFormat, Long> formatCountMap = new EnumMap<>(SupportedFileFormat.class);

  /**
   * Define a property to configure the log level at runtime.
   * -Dorg.slf4j.simpleLogger.defaultLogLevel=off
   */
  public static void main(final String[] args) {
    TestAudioTagger test = new TestAudioTagger();

    if (args.length == 0) {
      System.err.println("usage TestAudioTagger Dirname");
      System.err.println("      You must enter the root directory");
      System.exit(1);
    } else if (args.length > 1) {
      System.err.println("usage TestAudioTagger Dirname");
      System.err.println("      Only one parameter accepted");
      System.exit(1);
    }
    File rootDir = new File(args[0]);
    if (!rootDir.isDirectory() || rootDir.isHidden()) {
      System.err.println("usage TestAudioTagger Dirname");
      System.err.println("      Directory " + args[0] + " could not be found or is hidden");
      System.exit(1);
    }
    ArrayDeque<File> deque = new ArrayDeque<>(2600);
    test.scanSingleDir(rootDir, deque);
    int failed = 0;
    final int count = deque.size();
    System.out.println("Will read " + Integer.toString(count) + " files");
    File file = null;

    // Timed but don't overly rely on results. Run multiple times to get caches filled, etc. Remove println for slight improvement
    Stopwatch stopwatch = Stopwatch.createStarted();
    while (!deque.isEmpty()) {
      try {
        file = deque.pollLast();
        final AudioFile audioFile = AudioFileIO.readIgnoreArtwork(file);
        final Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
        System.out.println("Title:  " + tag.getFirst(FieldKey.TITLE));
        System.out.println("Artist: " + tag.getFirst(FieldKey.ARTIST));
        System.out.println("Album:  " + tag.getFirst(FieldKey.ALBUM));
      } catch (Throwable t) {
        System.err.println("Unable to read file:" + count + " path:" + (file != null ? file.getPath() : ""));
        failed++;
        t.printStackTrace();
      }
    }
    final int processed = count - failed;

    printFileTypes(processed);

    System.out.println("Elapsed: " + Double.toString(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0));
    System.out.println("Attempted  to read:" + count);
    System.out.println("Successful to read:" + processed);
    System.out.println("Failed     to read:" + failed);

  }

  private static void printFileTypes(final int processed) {
    System.out.println();
    for (final Map.Entry<SupportedFileFormat, Long> entry : formatCountMap.entrySet()) {
      final long count = entry.getValue();
      final double percent = 100.0 * count / processed;
      System.out.println(String.format("Type=%s %6d %.0f%%",
                                       entry.getKey().toString(),
                                       count,
                                       percent));
    }
    System.out.println();
  }

  private void scanSingleDir(final File dir, final ArrayDeque<File> deque) {
    final File[] files = dir.listFiles(new AudioFileFilter(false));
    if (files != null) {
      final List<File> fileList = Arrays.asList(files);
      for (int i = 0, size = fileList.size(); i < size; i++) {
        incrementFormatCount(fromExtension(Files.getFileExtension(fileList.get(i).getPath())));
      }
      deque.addAll(fileList);
    }

    final File[] audioFileDirs = dir.listFiles(new DirFilter());
    if (audioFileDirs != null && audioFileDirs.length > 0) {
      for (File audioFileDir : audioFileDirs) {
        scanSingleDir(audioFileDir, deque);
      }
    }
  }

  private void incrementFormatCount(final SupportedFileFormat supportedFileFormat) {
    Long count = formatCountMap.get(supportedFileFormat);
    if (count == null) {
      formatCountMap.put(supportedFileFormat, 1L);
    } else {
      formatCountMap.put(supportedFileFormat, count + 1);
    }
  }


  public final class DirFilter implements java.io.FileFilter {
    public final boolean accept(final java.io.File file) {
      return file.isDirectory() && !file.isHidden();
    }
  }

  @SuppressWarnings("unused")
  private void forREADME() throws TagException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {
    File inputFile = new File("MyFavoriteSong.mp3");
    AudioFile audioFile = AudioFileIO.read(inputFile);

    final AudioHeader audioHeader = audioFile.getAudioHeader();
    final int channels = audioHeader.getChannelCount();
    final int bitRate = audioHeader.getBitRate();
    final String encodingType = audioHeader.getEncodingType();

    Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
    final String title = tag.getValue(FieldKey.TITLE).or("");
    if ("".equals(title)) {
      if (tag == NullTag.INSTANCE) {
        // there was no tag. set a new default tag for the file type
        tag = audioFile.setNewDefaultTag();
      }
    }

    tag.setField(FieldKey.TITLE, "My New Title");
    audioFile.save();

    final ImmutableSet<FieldKey> supportedFields = tag.getSupportedFields();
    if (supportedFields.contains(FieldKey.COVER_ART)) {
      System.out.println("File type supports Artwork");
    }
  }
}
