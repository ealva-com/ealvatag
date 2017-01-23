package ealvatag.issues;

import ealvatag.AbstractTestCase;
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

import java.io.File;

/**
 * Support For LANGUAGE and INITIAL_KEY
 */
public class Issue241Test extends AbstractTestCase {
    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

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
            assertEquals("eng", tag.getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", tag.getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(tag.getFirst(FieldKey.LANGUAGE)));
            TagField tagField = tag.getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName()).or(NullTagField.INSTANCE);
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            assertTrue(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tagField = tag.getFirstField(ID3v24FieldKey.KEY.getFieldName()).or(NullTagField.INSTANCE);
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            assertTrue(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());

            tag.setField(FieldKey.LANGUAGE, "fred");
            af.save();
            assertEquals("fred", tag.getFirst(FieldKey.LANGUAGE));
            tagField = tag.getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName()).or(NullTagField.INSTANCE);
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            assertFalse(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tag.setField(FieldKey.KEY, "keys");
            af.save();
            assertEquals("keys", tag.getFirst(FieldKey.KEY));
            tagField = tag.getFirstField(ID3v24FieldKey.KEY.getFieldName()).or(NullTagField.INSTANCE);
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            assertFalse(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Ogg Vorbis
     */
    public void testWriteFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Flac
     */
    public void testWriteFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


    /**
     * Test writing INITIAL_KEY and LANGUAGE to Wma
     */
    public void testWriteFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp4
     */
    public void testWriteFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.LANGUAGE, "eng");
            tag.setField(FieldKey.KEY, "C#");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("eng", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertEquals("English", Languages.getInstanceOf().getValue(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE)));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Key Validation
     */
    public void testValidateMusicalKey() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.setField(FieldKey.KEY, "G");
            assertEquals("G", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C#");
            assertEquals("C#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "Cm");
            assertEquals("Cm", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C#m");
            assertEquals("C#m", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C");
            assertEquals("C", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "o");
            assertEquals("o", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "R#");
            assertEquals("R#", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "Cp");
            assertEquals("Cp", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

            tag.setField(FieldKey.KEY, "C##");
            assertEquals("C##", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(FieldKey.KEY)));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


}
