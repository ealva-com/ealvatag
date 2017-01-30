package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Cannot cretaeTagField for creating artwork field
 */
public class Issue263Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test writing Artwork  to Mp3 ID3v24
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            Tag tag = af.setNewDefaultTag();
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v22
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23
     */
    @Test public void testWriteArtworkFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            Tag tag = af.setNewDefaultTag();
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test reading/writing artwork to Ogg
     */
    @Test public void testReadWriteArtworkFieldsToOggVorbis() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test3.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Flac
     */
    @Test public void testReadWriteArtworkFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test reading/writing artwork to Wma
     */
    @Test public void testReadWriteArtworkFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test5.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test reading/writing artwork to Mp4
     */
    @Test public void testReadWriteArtworkFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test2.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test Artwork cannot be written to Wav
     */
    @Test public void testReadWriteArtworkFieldsToWav() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.wav");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test Artwork cannot be written to Real
     */
    @Test public void testReadWriteArtworkFieldsToReal() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test01.ra");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNotNull(exceptionCaught);
        Assert.assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

}
