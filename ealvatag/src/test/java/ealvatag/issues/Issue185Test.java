package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Creating Null fields
 */
public class Issue185Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testDefaultTagMp3() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            //No Tag
            Assert.assertNull(af.getTag().orNull());

            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();

            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);

            //Save changes
            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    @Test public void testDefaultTagMp3AndCreate() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            //No Tag
            Assert.assertNull(af.getTag().orNull());

            //Tag Created and setField
            Tag tag = af.getTagOrSetNewDefault();
            Assert.assertTrue(tag instanceof ID3v23Tag);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);

            //Save changes
            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
