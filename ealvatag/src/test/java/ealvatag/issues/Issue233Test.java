package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Deleting v2 tags
 */
public class Issue233Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testDeletingID3v2Tag() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //No Tags
            MP3File mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v24 tag
            mp3File.setID3v2Tag(new ID3v24Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v23 tag
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v22 tag
            mp3File.setID3v2Tag(new ID3v22Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            Assert.assertFalse(mp3File.hasID3v1Tag());
            Assert.assertFalse(mp3File.hasID3v2Tag());

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testDeletingID3v1Tag() {
        File orig = new File("testdata", "test32.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test32.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.deleteFileTag();
        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadingID3v1Tag() {
        File orig = new File("testdata", "test32.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test32.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mf = (MP3File)af;
            Assert.assertEquals("The Ides Of March", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Iron Maiden", mf.getID3v1Tag().getFirst(FieldKey.ARTIST));
            Assert.assertEquals("", mf.getID3v2Tag().getFirst(FieldKey.ARTIST));
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));


        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }
}
