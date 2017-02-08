package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v1Tag;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Testing that adding large artwork doesn't overwrite mp3 audio data
 */
public class Issue374Test {
    @After
    public void teardown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test
    public void testIssue() throws Exception {
        File testdatadir = new File("testdata");
        int count = 0;
        for (File next : testdatadir.listFiles(new MP3FileFilter())) {
            count++;
            File orig = new File("testdata", next.getName());
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp(next.getName(),
                                            new File(next.getName().substring(0, next.getName().length() - 4) + count + ".mp3"));


            AudioFile af = AudioFileIO.read(testFile);
            String s1 = Utils.formatBitRate(af.getAudioHeader(), af.getAudioHeader().getBitRate());
            String s2 = String.valueOf(af.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));
            String s3 = String.valueOf(af.getAudioHeader().isVariableBitRate());

            Tag tag = af.getTag().orNull();
            if (tag == null || tag instanceof ID3v1Tag) {
                TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
                tag = af.setNewDefaultTag();
            }
            tag.addArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata", "coverart_large.jpg")));
            af.save();
            System.out.println("Checking:" + testFile);
            af = AudioFileIO.read(testFile);
            String s11 = Utils.formatBitRate(af.getAudioHeader(), af.getAudioHeader().getBitRate());
            String s22 = String.valueOf(af.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));
            String s33 = String.valueOf(af.getAudioHeader().isVariableBitRate());

            Assert.assertEquals(s1, s11);
            Assert.assertEquals(s2, s22);
            Assert.assertEquals(s3, s33);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COVER_ART).size() > 0);
        }
    }

    final class MP3FileFilter implements java.io.FileFilter {

        /**
         * allows Directories
         */
        private final boolean allowDirectories;

        /**
         * Create a default MP3FileFilter.  The allowDirectories field will
         * default to false.
         */
        public MP3FileFilter() {
            this(false);
        }

        /**
         * Create an MP3FileFilter.  If allowDirectories is true, then this filter
         * will accept directories as well as mp3 files.  If it is false then
         * only mp3 files will be accepted.
         *
         * @param allowDirectories whether or not to accept directories
         */
        private MP3FileFilter(final boolean allowDirectories) {
            this.allowDirectories = allowDirectories;
        }

        /**
         * Determines whether or not the file is an mp3 file.  If the file is
         * a directory, whether or not is accepted depends upon the
         * allowDirectories flag passed to the constructor.
         *
         * @param file the file to test
         *
         * @return true if this file or directory should be accepted
         */
        public final boolean accept(final File file) {
            final String fileName = file.getName();
            if ("corrupt.mp3".equals(fileName) ||
                    "Issue79.mp3".equals(fileName) ||
                    "test22.mp3".equals(fileName) ||
                    "test92.mp3".equals(fileName) ||
                    "issue52.mp3".equals(fileName) ||
                    "Issue81.mp3".equals(fileName)) {
                return false;
            }

            if (fileName.startsWith("testNon")) {
                System.err.println("WTF? =" + fileName);
                return false;
            }

            return ((fileName.toLowerCase().endsWith(".mp3")) || (file.isDirectory() && (this.allowDirectories)));
        }

    }

}
