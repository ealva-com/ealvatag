package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTagField;
import ealvatag.tag.Tag;
import ealvatag.tag.TagField;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.TagTextField;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.framebody.FrameBodyPOPM;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;
import java.util.List;

/**
 * Support For Custom fields
 */
public class Issue345Test extends AbstractTestCase {
    /**
     * Test writing Custom fields
     */
    public void testWriteFieldsToMp3ID3v24() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v24.mp3"));

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            af.setNewDefaultTag();
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.RATING).or(NullTagField.INSTANCE);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
            }
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.RATING).or(NullTagField.INSTANCE);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
            }

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ENGINEER);
            assertEquals("", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));

            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("", af.getTag().getFirst(FieldKey.DJMIXER));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields
     */
    public void testWriteFieldsToMp3ID3v23() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v23.mp3"));

            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("50", tag.getFirst(FieldKey.RATING));
            assertEquals("mixer", tag.getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", tag.getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", tag.getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", tag.getFirst(FieldKey.ARRANGER));

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ARRANGER);
            assertEquals(0, tag.getFields(FieldKey.ARRANGER).size());
            assertEquals("", tag.getFirst(FieldKey.ARRANGER));
            assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", af.getTag().getFirst(FieldKey.ARRANGER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.ENGINEER);
            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("", af.getTag().getFirst(FieldKey.DJMIXER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v22() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testWriteFieldsToMp3ID3v22.mp3"));

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
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("50", tag.getFirst(FieldKey.RATING));
            assertEquals("mixer", tag.getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", tag.getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", tag.getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", tag.getFirst(FieldKey.ARRANGER));

            List<TagField> fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            tag.deleteField(FieldKey.ENGINEER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }

            assertEquals(0, tag.getFields(FieldKey.ENGINEER).size());
            assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", tag.getFirst(FieldKey.DJMIXER));

            fields = tag.getFields(FieldKey.PRODUCER);
            for (TagField field : fields) {
                System.out.println(((TagTextField)field).getContent());
            }


            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));

            tag.deleteField(FieldKey.PRODUCER);
            tag.deleteField(FieldKey.MIXER);
            tag.deleteField(FieldKey.DJMIXER);
            tag.deleteField(FieldKey.ARRANGER);

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("", tag.getFirst(FieldKey.ENGINEER));
            assertEquals("", tag.getFirst(FieldKey.DJMIXER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Ogg Vorbis
     */
    public void testWriteFieldsToOggVorbis() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Flac
     */
    public void testWriteFieldsToFlac() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


    /**
     * Test writing Custom fields to Wma
     */
    public void testWriteFieldsToWma() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Mp4
     */
    public void testWriteFieldsToMp4() {
        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(FieldKey.RATING, "50");
            tag.setField(FieldKey.MIXER, "mixer");
            tag.setField(FieldKey.ENGINEER, "engineervalue");
            tag.setField(FieldKey.DJMIXER, "djmixervalue");
            tag.setField(FieldKey.PRODUCER, "producervalue");
            tag.setField(FieldKey.ARRANGER, "arrangervalue");
            tag.setField(FieldKey.ACOUSTID_FINGERPRINT, "acousticfingerprint");
            assertEquals("acousticfingerprint", af.getTag().getFirst(FieldKey.ACOUSTID_FINGERPRINT));
            tag.setField(FieldKey.COUNTRY, "France");
            assertEquals("France", af.getTag().getFirst(FieldKey.COUNTRY));

            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("50", af.getTag().getFirst(FieldKey.RATING));
            assertEquals("mixer", af.getTag().getFirst(FieldKey.MIXER));
            assertEquals("engineervalue", af.getTag().getFirst(FieldKey.ENGINEER));
            assertEquals("djmixervalue", af.getTag().getFirst(FieldKey.DJMIXER));
            assertEquals("producervalue", af.getTag().getFirst(FieldKey.PRODUCER));
            assertEquals("arrangervalue", af.getTag().getFirst(FieldKey.ARRANGER));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }


}
