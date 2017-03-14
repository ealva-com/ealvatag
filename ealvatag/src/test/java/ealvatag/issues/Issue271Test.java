package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * ID3 Tag Specific flags
 */
public class Issue271Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test read mp3 that says it has extended header but doesn't really
     */
    @Test public void testReadMp3WithExtendedHeaderFlagSetButNoExtendedHeader() {
        File orig = new File("testdata", "test46.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test46.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("00000", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.BPM));
            Assert.assertEquals("*thievery corporation - Om Lounge*",
                                "*" + af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST) + "*");

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read mp3 with extended header and crc-32 check
     */
    @Test public void testReadMp3WithExtendedHeaderAndCrc() {
        File orig = new File("testdata", "test47.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test47.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("tonight (instrumental)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Young Gunz", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(156497728, id3v23Tag.getCrc32());
            Assert.assertEquals(0, id3v23Tag.getPaddingSize());

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test doesnt fail when read mp3 that has an encrypted field
     * <p>
     * TODO currently we cant decrypt it, that will come later
     */
    @Test public void testReadMp3WithEncryptedField() {
        File orig = new File("testdata", "test48.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test48.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("Don't Leave Me", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("All-American Rejects", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, id3v23Tag.getPaddingSize());

            AbstractID3v2Frame frame = (AbstractID3v2Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);


            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read flags
     */
    @Test public void testReadFlagsCompressed() {
        File orig = new File("testdata", "test51.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test51.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(v22tag.isCompression());
            Assert.assertFalse(v22tag.isUnsynchronization());

            //Now write to tag we dont preservbe compressed flag because cant actualy define compression
            v22tag.setField(FieldKey.TITLE, "A new start");
            Assert.assertEquals("A new start", v22tag.getFirst(FieldKey.TITLE));
            af.save();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertFalse(v22tag.isUnsynchronization());

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setField(FieldKey.TITLE, "B new start");
            af.save();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertTrue(v22tag.isUnsynchronization());
            Assert.assertEquals("B new start", v22tag.getFirst(FieldKey.TITLE));
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

    /**
     * Test read flags
     */
    @Test public void testReadFlagsUnsyced() {
        File orig = new File("testdata", "test52.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test52.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertTrue(v22tag.isUnsynchronization());

            //Now write to tag we dont preservbe compressed flag because cant actualy define compression
            //We dont preserve compression because based on TagOptionDinglton which is false by default
            v22tag.setField(FieldKey.TITLE, "A new start");
            af.save();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertFalse(v22tag.isUnsynchronization());
            Assert.assertEquals("A new start", v22tag.getFirst(FieldKey.TITLE));

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setField(FieldKey.TITLE, "B new start");
            af.save();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertTrue(v22tag.isUnsynchronization());
            Assert.assertEquals("B new start", v22tag.getFirst(FieldKey.TITLE));
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

    /**
     * Test read flags
     */
    @Test public void testReadFlagsUnsycedCompressed() {
        File orig = new File("testdata", "test53.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test53.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(v22tag.isCompression());
            Assert.assertTrue(v22tag.isUnsynchronization());

            //Now write to tag we dont preserve compressed flag because cant actually define compression
            //We dont preserve compression because based on TagOptionDinglton which is false by default
            v22tag.setField(FieldKey.TITLE, "A new start");
            af.save();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertFalse(v22tag.isUnsynchronization());
            Assert.assertEquals("A new start", v22tag.getFirst(FieldKey.TITLE));

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setField(FieldKey.TITLE, "B new start");
            af.save();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertFalse(v22tag.isCompression());
            Assert.assertTrue(v22tag.isUnsynchronization());
            Assert.assertEquals("B new start", v22tag.getFirst(FieldKey.TITLE));
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);

    }

}
