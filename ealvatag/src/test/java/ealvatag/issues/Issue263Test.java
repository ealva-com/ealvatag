package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.UnsupportedFieldException;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Cannot cretaeTagField for creating artwork field
 */
public class Issue263Test extends AbstractTestCase {
    /**
     * Test writing Artwork  to Mp3 ID3v24
     */
    public void testWriteArtworkFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            Tag tag = af.setNewDefaultTag();
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v22
     */
    public void testWriteArtworkFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

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


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test writing Artwork  to Mp3 ID3v23
     */
    public void testWriteArtworkFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            Tag tag = af.setNewDefaultTag();
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }


        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test reading/writing artwork to Ogg
     */
    public void testReadWriteArtworkFieldsToOggVorbis() {

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test3.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test reading/writing artwork to Flac
     */
    public void testReadWriteArtworkFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test reading/writing artwork to Wma
     */
    public void testReadWriteArtworkFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test5.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test reading/writing artwork to Mp4
     */
    public void testReadWriteArtworkFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test2.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }


    /**
     * Test Artwork cannot be written to Wav
     */
    public void testReadWriteArtworkFieldsToWav() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.wav");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

    /**
     * Test Artwork cannot be written to Real
     */
    public void testReadWriteArtworkFieldsToReal() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test01.ra");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.createField(FieldKey.COVER_ART, "test");
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught instanceof UnsupportedFieldException);
    }

}
