package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.audio.aiff.AiffAudioHeader;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * #119:https://bitbucket.org/ijabz/jaudiotagger/issues/119/wav-aiff-add-padding-byte-if-missing-in
 */
public class Issue119Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWriteAiffWithOddLengthDataChunk() {

        Exception exceptionCaught = null;

        File orig = new File("testdata", "test151.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }


        File testFile = TestUtil.copyAudioToTmp("test151.aif", new File("test151MissingByte.aiff"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "fred");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception ex) {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteFileWithOddLengthLastDataChunkInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);

        File orig = new File("testdata", "test153.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test153.wav", new File("test153_odd_data_length_info.wav"));
            AudioFile f = AudioFileIO.read(testFile);

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "freddy");
            f.save();
            f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteFileWithOddLengthLastDataChunkId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);

        File orig = new File("testdata", "test153.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test153.wav", new File("test153_odd_data_length_id3.wav"));
            AudioFile f = AudioFileIO.read(testFile);

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "freddy");
            f.save();
            f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteFileWithOddLengthLastDataChunkId3AndInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);

        File orig = new File("testdata", "test153.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test153.wav", new File("test153_odd_data_length_id3_and_info.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "freddy");
            f.save();
            f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            Assert.assertEquals("freddy", tag.getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
