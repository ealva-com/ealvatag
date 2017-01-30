package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v1Tag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.io.File;

/**
 * Default ID3 Tag when have ID3v1 but no id3 v2 tag
 */
public class Issue443Test {
    @Before
    public void setup() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testID3v2DefaultCreateOrConvertWhenOnlyHasID3v1() {
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            Assert.assertNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            Assert.assertNotNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            test.getConvertedTagOrSetNewDefault();
            Assert.assertNotNull(test.getID3v1Tag());
            Assert.assertNotNull(test.getID3v2Tag());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testID3v2DefaultCreatedWhenOnlyHasID3v1() {
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            Assert.assertNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            Assert.assertNotNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            test.getTagOrSetNewDefault();
            Assert.assertNotNull(test.getID3v1Tag());
            Assert.assertNotNull(test.getID3v2Tag());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testID3v2CreatedWhenOnlyHasID3v1() {
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            Assert.assertNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            Assert.assertNotNull(test.getID3v1Tag());
            Assert.assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            Tag tag = test.getTagOrSetNewDefault();
            assertThat(tag, instanceOf(ID3v23Tag.class));


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
