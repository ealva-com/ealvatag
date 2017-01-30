package ealvatag.tag.vorbiscomment;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.ogg.OggFileReader;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * User: paul
 * Date: 21-Feb-2008
 */
public class VorbisReadTagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test reading a file with corrupt vorbis comment tag, however the ogg paging is actually correct
     * so no error found in this test.
     */
    @Test public void testReadCorruptOgg() {
        File orig = new File("testdata", "test6.ogg");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
//Can summarize file
            File testFile = TestUtil.copyAudioToTmp("test6.ogg");
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            oggFileReader.summarizeOggPageHeaders(testFile);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading corrupt file, because vorbis comment has an error (says no of comments is 5 but actually there
     * are 6 it should throw appropriate error
     */
    @Test public void testReadCorruptOgg2() {
        File orig = new File("testdata", "test6.ogg");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
//Can summarize file
            File testFile = TestUtil.copyAudioToTmp("test6.ogg");
            AudioFileIO.read(testFile);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
    }

    /**
     * Create Value with empty value and then read back, then try and create another field
     * Was expecting to fail but works ok
     */
    @Test public void testCreateCorruptFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.ogg", new File("testWithEmptyField.ogg"));
            AudioFile file = AudioFileIO.read(testFile);
            file.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "");
            file.save();

            file = AudioFileIO.read(testFile);
            file.getTag().or(NullTag.INSTANCE).setField(FieldKey.TITLE, "testtitle");
            file.save();

            file = AudioFileIO.read(testFile);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


}
