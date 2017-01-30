package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.ArtworkFactory;
import ealvatag.tag.mp4.Mp4Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test Writing to mp4 with top level free data atoms but free atoms and mdat are before ilst so not useful
 */
public class Issue240Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWritelargeDataToFile() {
        File orig = new File("testdata", "test34.m4a");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test34.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertEquals(0, ((Mp4Tag)af.getTag().or(NullTag.INSTANCE)).getFields(FieldKey.COVER_ART).size());

            //Add new image
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            af.getTag().or(NullTag.INSTANCE).addArtwork(ArtworkFactory.getNew().setBinaryData(imagedata));
            af.save();

            //Read File back
            af = AudioFileIO.read(testFile);
            Assert.assertEquals(1, ((Mp4Tag)af.getTag().or(NullTag.INSTANCE)).getFields(FieldKey.COVER_ART).size());
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
