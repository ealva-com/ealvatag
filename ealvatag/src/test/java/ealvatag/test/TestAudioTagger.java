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
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileFilter;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Simple class that will attempt to recusively read all files within a directory, flags
 * errors that occur.
 */
public class TestAudioTagger {

    private static int count = 0;
    private static int failed = 0;

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
        count = deque.size();
        System.out.println("Will read " + Integer.toString(count) + " files");
        File file = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        while (!deque.isEmpty()) {
            try {
                file = deque.pollLast();
                final AudioFile audioFile = AudioFileIO.read(file);
                final Tag tag = audioFile.getTag().or(NullTag.INSTANCE);
                System.out.println("Title:  " + tag.getFirst(FieldKey.TITLE));
                System.out.println("Artist: " + tag.getFirst(FieldKey.ARTIST));
                System.out.println("Album:  " + tag.getFirst(FieldKey.ALBUM));
                System.out.println();

            } catch (Throwable t) {
                System.err.println("Unable to read file:" + count + " path:" + (file != null ? file.getPath() : ""));
                failed++;
                t.printStackTrace();
            }
        }
        System.out.println("Elapsed: " + Double.toString(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0));
        System.out.println("Attempted  to read:" + count);
        System.out.println("Successful to read:" + (count - failed));
        System.out.println("Failed     to read:" + failed);

    }

    private void scanSingleDir(final File dir, final ArrayDeque<File> deque) {
        final File[] files = dir.listFiles(new AudioFileFilter(false));
        if (files != null) {
            deque.addAll(Arrays.asList(files));
        }

        final File[] audioFileDirs = dir.listFiles(new DirFilter());
        if (audioFileDirs != null && audioFileDirs.length > 0) {
            for (File audioFileDir : audioFileDirs) {
                scanSingleDir(audioFileDir, deque);
            }
        }
    }


    public final class DirFilter implements java.io.FileFilter {
        public final boolean accept(final java.io.File file) {
            return file.isDirectory() && !file.isHidden();
        }
    }
}
