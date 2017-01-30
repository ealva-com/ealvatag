package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyTPOS;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 *
 */
public class Issue412Test {
    @Before
    public void setup() throws Exception {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After
    public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testTXXXSameDescription() throws Exception {
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

    @Test public void testTXXXDifferentDescription() throws Exception {
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
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.CATALOG_NO, "CATALOGNO");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.BARCODE, 0));
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.BARCODE));
            Assert.assertEquals("CATALOGNO", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.CATALOG_NO, 0));

            //No of Barcode Values
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.BARCODE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.BARCODE).size());

            //No of Catalog Values
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.CATALOG_NO).size());

            //Actual No Of Fields used to store barcode, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.CATALOG_NO).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testWXXXSameDescription() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://www.wrathrecords.co.uk/afarm.htm");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("http://www.wrathrecords.co.uk/afarm.htm",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://www.wrathrecords.co.uk/bfarm.htm");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("http://www.wrathrecords.co.uk/afarm.htm",
                                af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_DISCOGS_ARTIST_SITE, 0));
            Assert.assertEquals("http://www.wrathrecords.co.uk/afarm.htm",
                                af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            Assert.assertEquals("http://www.wrathrecords.co.uk/bfarm.htm",
                                af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_DISCOGS_ARTIST_SITE, 1));

            //No of Url Values
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.URL_DISCOGS_ARTIST_SITE).size());

            //Actual No Of Fields used to store urls, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testTXXXSameDescriptionMultiples() throws Exception {
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
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.CATALOG_NO, "CATNO");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.ARTISTS, "ARTISTS");
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

    @Test public void testWXXXSameDescriptionMultiples() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "BARCODE1");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_DISCOGS_ARTIST_SITE, "BARCODE2");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "CATNO");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "ARTISTS");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_DISCOGS_ARTIST_SITE, 0));
            Assert.assertEquals("BARCODE1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_DISCOGS_ARTIST_SITE));
            Assert.assertEquals("BARCODE2", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_DISCOGS_ARTIST_SITE, 1));

            //No of Barcode Values
            Assert.assertEquals(2, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.URL_DISCOGS_ARTIST_SITE).size());

            //Actual No Of Fields used to store barcode, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }


    @Test public void testTCOMMultiples() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.COMPOSER, "composer1");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("composer1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMPOSER, "composer2");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMPOSER, "composer3");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.COMPOSER, "composer4");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("composer1", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.COMPOSER, 0));
            Assert.assertEquals("composer1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("composer2", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.COMPOSER, 1));

            //No of Composer Values
            Assert.assertEquals(4, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.COMPOSER).size());

            //Actual No Of Fields used to store barcode, Should be only one
            Assert.assertEquals(1, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.COMPOSER).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testTrackNoTotalCombinations() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.deleteField(FieldKey.TRACK);
            tag.setField(FieldKey.TRACK, "1");
            tag.deleteField(FieldKey.TRACK_TOTAL);
            tag.setField(FieldKey.TRACK_TOTAL, "11");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testTrackNoTotalAddCombinations() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.deleteField(FieldKey.TRACK);
            tag.addField(FieldKey.TRACK, "1");
            tag.deleteField(FieldKey.TRACK_TOTAL);
            tag.addField(FieldKey.TRACK_TOTAL, "11");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testDiscNoTotalCombinations() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.deleteField(FieldKey.DISC_NO);
            tag.setField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.setField(FieldKey.DISC_TOTAL, "11");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testDiscNoTotalAddCombinations() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            ID3v23Tag v23tag = (ID3v23Tag)tag;
            AbstractID3v2Frame frame = (AbstractID3v2Frame)v23tag.getFrame("TPOS");
            FrameBodyTPOS fbBody = (FrameBodyTPOS)frame.getBody();
            Assert.assertEquals(1, fbBody.getDiscNo().intValue());
            Assert.assertEquals(11, fbBody.getDiscTotal().intValue());

            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testDiscNoTotalAddCombinationsWithPadding() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            TagOptionSingleton.getInstance().setPadNumbers(true);
            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);
            ID3v23Tag v23tag = (ID3v23Tag)tag;


            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");

            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            v23tag = (ID3v23Tag)tag;
            AbstractID3v2Frame frame = (AbstractID3v2Frame)v23tag.getFrame("TPOS");
            FrameBodyTPOS fbBody = (FrameBodyTPOS)frame.getBody();
            Assert.assertEquals(1, fbBody.getDiscNo().intValue());
            Assert.assertEquals(11, fbBody.getDiscTotal().intValue());
            Assert.assertEquals("01/11", fbBody.getText());

            Assert.assertEquals("01", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }

    @Test public void testDiscNoTotalAddCombinationsWithPaddingFlac() {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test.flac");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            TagOptionSingleton.getInstance().setPadNumbers(true);
            File testFile = TestUtil.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.save();
            af = AudioFileIO.read(testFile);
            Tag tag = af.getTag().or(NullTag.INSTANCE);


            tag.deleteField(FieldKey.DISC_NO);
            tag.addField(FieldKey.DISC_NO, "1");
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.addField(FieldKey.DISC_TOTAL, "11");
            af.save();
            af = AudioFileIO.read(testFile);
            tag = af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("1", tag.getFirst(FieldKey.DISC_NO));
            Assert.assertEquals("11", tag.getFirst(FieldKey.DISC_TOTAL));
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
