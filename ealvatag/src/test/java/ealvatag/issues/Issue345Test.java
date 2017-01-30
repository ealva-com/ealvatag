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
import ealvatag.tag.TagTextField;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.framebody.FrameBodyPOPM;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Support For Custom fields
 */
public class Issue345Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test writing Custom fields
     */
    @Test public void testWriteFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v24.mp3"));

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.RATING).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
            }
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            {
                TagField tagField = af.getTag().or(NullTag.INSTANCE).getFirstField(FieldKey.RATING).or(NullTagField.INSTANCE);
                Assert.assertTrue(tagField instanceof ID3v24Frame);
                Assert.assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
            }

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ENGINEER);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));

            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields
     */
    @Test public void testWriteFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v23.mp3"));

            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("50", tag.getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", tag.getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", tag.getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", tag.getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", tag.getFirst(FieldKey.ARRANGER));

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ARRANGER);
            Assert.assertEquals(0, tag.getFields(FieldKey.ARRANGER).size());
            Assert.assertEquals("", tag.getFirst(FieldKey.ARRANGER));
            Assert.assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.ENGINEER);
            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp3 ID3v23
     */
    @Test public void testWriteFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v22.mp3"));

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            Tag tag = af.setNewDefaultTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("50", tag.getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", tag.getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", tag.getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", tag.getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", tag.getFirst(FieldKey.ARRANGER));

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ENGINEER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            Assert.assertEquals(0, tag.getFields(FieldKey.ENGINEER).size());
            Assert.assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));

            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("", tag.getFirst(FieldKey.DJMIXER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Ogg Vorbis
     */
    @Test public void testWriteFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Flac
     */
    @Test public void testWriteFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


    /**
     * Test writing Custom fields to Wma
     */
    @Test public void testWriteFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp4
     */
    @Test public void testWriteFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            Assert.assertEquals("acousticfingerprint", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            Assert.assertEquals("France", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COUNTRY));

            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("50", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RATING));
            Assert.assertEquals("mixer", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MIXER));
            Assert.assertEquals("engineervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("djmixervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.DJMIXER));
            Assert.assertEquals("producervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.PRODUCER));
            Assert.assertEquals("arrangervalue", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }


}
