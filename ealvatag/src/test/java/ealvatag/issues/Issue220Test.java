package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.Mp4AtomTree;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue220Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test read mp4 ok without any udta atom (but does have meta atom under trak)
     */
    @Test public void testReadMp4WithoutUdta() {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test41.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok without any udta atom (but does have meta atom under trak)
     */
    @Test public void testWriteMp4WithoutUdta() {
        File orig = new File("testdata", "test41.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test41.m4a");

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "FREDDYCOUGAR");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE, "genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "year");
            af.save();

            //Read file again okay

            new Mp4AtomTree(new RandomAccessFile(testFile, "r"));

          af = AudioFileIO.read(testFile);
            Assert.assertEquals("FREDDYCOUGAR", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("genre", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            Assert.assertEquals("year", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
     * before mvhd atom (and still has the meta atom under trak)
     */
    @Test public void testReadMp4WithUdtaAndMetaHierachy() {
        File orig = new File("testdata", "test42.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test42.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals("artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test42", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok which originally had just meta under trak, then processed in picard and now has a seperate udta atom
     * before mvhd atom (and still has the meta atom under trak)
     */
    @Test public void testWriteMp4WithUdtaAndMetaHierachy() {
        File orig = new File("testdata", "test42.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test42.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "KARENTAYLORALBUM");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "KARENTAYLORTITLE");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE, "KARENTAYLORGENRE");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.AMAZON_ID, "12345678");

            af.save();
            System.out.println("All is going well");
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("KARENTAYLORALBUM", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            Assert.assertEquals("KARENTAYLORTITLE", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("KARENTAYLORGENRE", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            Assert.assertEquals("12345678", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.AMAZON_ID));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok without any udta atom (but does have meta atom under trak)
     */
    @Test public void testWriteMp4WithUdtaAfterTrackSmaller() {
        File orig = new File("testdata", "test44.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test44.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);


            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "ti");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("ti", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test write mp4 ok without any udta atom (but does have meta atom under trak)
     */
    @Test public void testWriteMp4WithUdtaAfterTrack() {
        File orig = new File("testdata", "test44.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test44.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);


            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "FREDDYCOUGAR");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE, "genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "year");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("FREDDYCOUGAR", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("album", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            Assert.assertEquals("title", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("genre", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            Assert.assertEquals("year", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

}
