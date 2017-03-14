package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.tag.NullTag;
import ealvatag.tag.images.ArtworkFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Problem when relative filename has been specified
 */
public class Issue282Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWriteToRelativeWmaFile() {
        File orig = new File("testdata", "test1.wma");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test1.wma");

            //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
            File outputFile = new File(testFile.getName());
            boolean result = Utils.copy(testFile, outputFile);
            Assert.assertTrue(result);

            //make Relative
            Assert.assertTrue(outputFile.exists());
            //Read File okay
            AudioFile af = AudioFileIO.read(outputFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());

            //Change File
            af.getTag().or(NullTag.INSTANCE).setArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata/coverart.jpg")));

            af.save();
            //noinspection ResultOfMethodCallIgnored
            outputFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteToRelativeMp3File() {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available" + orig);
            return;
        }

        File testFile;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("testV1.mp3");

            //Copy up a level coz we need it to be in same folder as working directory so can just specify filename
            File outputFile = new File(testFile.getName());
            boolean result = Utils.copy(testFile, outputFile);
            Assert.assertTrue(result);

            //make Relative
            Assert.assertTrue(outputFile.exists());
            //Read File okay
            AudioFile af = AudioFileIO.read(outputFile);

            //Create tag and Change File
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setArtwork(ArtworkFactory.createArtworkFromFile(new File("testdata/coverart.jpg")));
            af.save();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

}
