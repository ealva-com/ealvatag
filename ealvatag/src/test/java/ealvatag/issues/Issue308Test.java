package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.Artwork;
import ealvatag.tag.images.ArtworkFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Writing to Ogg file
 */
public class Issue308Test {
    public static int countExceptions = 0;

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testAddingLargeImageToOgg() throws Exception {
        File orig = new File("testdata", "test72.ogg");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        Exception e = null;
        try {
            final File testFile = TestUtil.copyAudioToTmp("test72.ogg");
            if (!testFile.isFile()) {
                System.err.println("Unable to test file - not available" + orig);
                return;
            }
            AudioFile af = AudioFileIO.read(testFile);
            Artwork artwork = ArtworkFactory.getNew();
            artwork.setFromFile(new File("testdata", "coverart_large.jpg"));

            af.getTag().or(NullTag.INSTANCE).setArtwork(artwork);
            af.save();

            //Reread
            System.out.println("Read Audio");
            af = AudioFileIO.read(testFile);
            System.out.println("Rewrite Audio");
            af.save();

            //Resave
            af.getTag().or(NullTag.INSTANCE).addField(FieldKey.TITLE, "TESTdddddddddddddddddddddddd");
            af.save();
        } catch (Exception ex) {
            e = ex;
            ex.printStackTrace();
        }
        Assert.assertNull(e);
    }
}
