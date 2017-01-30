package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test read m4a without udta/meta atom
 */
public class Issue260Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test read mp4 ok without any udta/meta atoms
     */
    @Test public void testReadMp4WithoutUdta() {
        File orig = new File("testdata", "test40.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test40.m4a");

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
     * Test write mp4 ok without any udta/meta atoms
     */
    @Test public void testWriteMp4WithoutUdta() {
        File orig = new File("testdata", "test40.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test40.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE).isEmpty());

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "artist");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE, "genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "year");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
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
     * Test read mp4 ok with udta at start of moov (created by picardqt)
     */
    @Test public void testReadMp4WithUdtaAtStart() {
        File orig = new File("testdata", "test43.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test43.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals("test43", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test write mp4 ok udta at start of moov (created by picardqt)
     */
    @Test public void testWriteMp4WithUdtaAtStart() {
        File orig = new File("testdata", "test43.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test43.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals("test43", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));

            //Write file
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "artist");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "album");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "title");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.GENRE, "genre");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "year");
            af.save();

            //Read file again okay
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("artist", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
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
