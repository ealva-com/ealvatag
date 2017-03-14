package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 *
 */
public class Issue417Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Multiple WOAR frames ARE allowed
     *
     * @throws Exception
     */
    @Test public void testWOARMultiples() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "01.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault().setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test1.html");
            Assert.assertTrue(af.getTag().or(NullTag.INSTANCE) instanceof ID3v23Tag);
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("http://test1.html", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test2.html");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test3.html");
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test4.html");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("http://test1.html", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_OFFICIAL_ARTIST_SITE, 0));
            Assert.assertEquals("http://test1.html", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.URL_OFFICIAL_ARTIST_SITE));
            Assert.assertEquals("http://test2.html", af.getTag().or(NullTag.INSTANCE).getFieldAt(FieldKey.URL_OFFICIAL_ARTIST_SITE, 1));

            //No of WOAR Values
            Assert.assertEquals(4, af.getTag().or(NullTag.INSTANCE).getAll(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());

            //Actual No Of Fields used to store WOAR frames
            Assert.assertEquals(4, af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());


        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        Assert.assertNull(caught);
    }
}
