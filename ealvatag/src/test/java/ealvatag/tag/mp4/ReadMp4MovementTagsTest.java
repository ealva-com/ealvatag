package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ReadMp4MovementTagsTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadMovementFieldsFromITunes() throws Exception {
        File orig = new File("testdata", "test161.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test161.m4a");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            Assert.assertEquals("I. Preludium (Pastorale). Allegro moderato", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("2", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("0", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.MOVEMENT, "fred");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.MOVEMENT_NO, "1");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.MOVEMENT_TOTAL, "7");

            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("7", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fred", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT));
            Assert.assertEquals("1", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_NO));
            Assert.assertEquals("7", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.MOVEMENT_TOTAL));

        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }
}
