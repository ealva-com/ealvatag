package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Mp3s can handle writing multiple fields which actually map to a single field using generic interface
 * but retrieval shows them as just one field, contrast with Flac
 */
public class Issue411Test {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }


    @Test public void testIssue() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.COMPOSER, "fred");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMPOSER, "john");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.COMPOSER, 0));
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("john", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.COMPOSER, 1));

            //No of Composer Values
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.COMPOSER).size());

            //Actual No Of Fields used to store Composer
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMPOSER).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testIssue2() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File orig = new File("testdata", "test.flac");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.COMPOSER, "fred");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMPOSER, "john");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("john", af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMPOSER).get(1).toString());
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMPOSER).size());
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testIssue3() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3", new File("issue411TestIssue3.mp3"));
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.GENRE, "rock");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("Rock", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.GENRE, "dance");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("Rock", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
            Assert.assertEquals("Dance", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.GENRE, 1));
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.GENRE).size());
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testIssue4() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.ENGINEER, "fred");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.ENGINEER, "john");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ENGINEER));
            Assert.assertEquals("john", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.ENGINEER, 1));
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.ENGINEER).size());
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testIssue5() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.BARCODE, "BARCODE1");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.BARCODE));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.BARCODE, "BARCODE2");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.BARCODE, 0));
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.BARCODE));
            Assert.assertEquals("BARCODE2", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.BARCODE, 1));

            //No of Barcode Values
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testDeletions() throws Exception {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            Tag tag = af.getTagOrSetNewDefault();
            tag.setField(FieldKey.BARCODE, "BARCODE1");
            Assert.assertTrue(tag instanceof ID3v23Tag);
            tag.addField(FieldKey.BARCODE, "BARCODE2");
            Assert.assertEquals(2, tag.getAll(FieldKey.BARCODE).size());
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            tag.deleteField(FieldKey.BARCODE);
            Assert.assertEquals(0, tag.getAll(FieldKey.BARCODE).size());

        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
