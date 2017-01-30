package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Deleteing comments with common interface
 */
public class Issue239Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test Deleting Plain Comments
     */
    @Test public void testDeletingCOMMFrames() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMMENT).size());

            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.COMMENT, "comment1");
            mp3File.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMMENT, "comment2");

            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertTrue(mp3File.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            Assert.assertEquals(2, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMMENT).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.COMMENT);
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMMENT).size());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMMENT).size());

        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test Deleting  Comments with Description field
     */
    @Test public void testDeletingFieldThatUsesCOMMFrames() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());

            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.CUSTOM1, "comment1");
            mp3File.getTag().or(NullTag.INSTANCE).addField(FieldKey.CUSTOM1, "comment2");

            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertTrue(mp3File.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            Assert.assertEquals(2, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.CUSTOM1);
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());

        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test Deleting  Comments with Description field only deletes the correct comments
     */
    @Test public void testDeletingFieldThatUsesCOMMFramesDoesntDeleteOtherCOMMFrame() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());

            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.CUSTOM1, "comment1");
            mp3File.getTag().or(NullTag.INSTANCE).addField(FieldKey.CUSTOM2, "comment2");

            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertTrue(mp3File.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.CUSTOM1);
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM2).size());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM1).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CUSTOM2).size());

        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }
}
