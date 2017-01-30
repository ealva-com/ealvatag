package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v24FieldKey;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.framebody.FrameBodyTKEY;
import ealvatag.tag.id3.framebody.FrameBodyTLAN;
import ealvatag.tag.reference.ID3V2Version;
import ealvatag.tag.reference.Languages;
import ealvatag.tag.reference.MusicalKey;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Support For LANGUAGE and INITIAL_KEY
 */
public class Issue241Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    @Test public void testWriteFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("eng", tag.getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", tag.getFirst(FieldKey.KEY));
            Assert.assertEquals("English", Languages.getInstanceOf().getValue(tag.getFirst(FieldKey.LANGUAGE)));
            TagField tagField = tag.getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertTrue(tagField instanceof ID3v24Frame);
            Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            Assert.assertTrue(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tagField = tag.getFirstField(ID3v24FieldKey.KEY.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertTrue(tagField instanceof ID3v24Frame);
            Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            Assert.assertTrue(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());

            tag.setField(FieldKey.LANGUAGE, "fred");
            af.save();
            Assert.assertEquals("fred", tag.getFirst(FieldKey.LANGUAGE));
            tagField = tag.getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertTrue(tagField instanceof ID3v24Frame);
            Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            Assert.assertFalse(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tag.setField(FieldKey.KEY, "keys");
            af.save();
            Assert.assertEquals("keys", tag.getFirst(FieldKey.KEY));
            tagField = tag.getFirstField(ID3v24FieldKey.KEY.getFieldName()).or(NullTagField.INSTANCE);
            Assert.assertTrue(tagField instanceof ID3v24Frame);
            Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            Assert.assertFalse(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    @Test public void testWriteFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    @Test public void testWriteFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Ogg Vorbis
     */
    @Test public void testWriteFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Flac
     */
    @Test public void testWriteFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test writing INITIAL_KEY and LANGUAGE to Wma
     */
    @Test public void testWriteFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp4
     */
    @Test public void testWriteFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertEquals("English",
                                Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Key Validation
     */
    @Test public void testValidateMusicalKey() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.setField(FieldKey.KEY, "G");
            Assert.assertEquals("G", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C#");
            Assert.assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "Cm");
            Assert.assertEquals("Cm", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C#m");
            Assert.assertEquals("C#m", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C");
            Assert.assertEquals("C", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "o");
            Assert.assertEquals("o", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "R#");
            Assert.assertEquals("R#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "Cp");
            Assert.assertEquals("Cp", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C##");
            Assert.assertEquals("C##", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            Assert.assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


}
