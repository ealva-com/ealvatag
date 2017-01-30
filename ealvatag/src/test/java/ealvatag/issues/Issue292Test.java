package ealvatag.issues;

import com.google.common.io.Files;
import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Unable to save changes to file if backup .old file already exists
 */
public class Issue292Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSavingMp3File() {
        File testFile = TestUtil.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
        if (!testFile.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File originalFileBackup = null;

        Exception exceptionCaught = null;
        try {

            testFile = TestUtil.copyAudioToTmp("testV1Cbr128ID3v2.mp3");
            //Put file in backup location
            originalFileBackup =
                    new File(testFile.getAbsoluteFile().getParentFile().getPath(),
                             Files.getNameWithoutExtension(testFile.getPath()) + ".old");
            testFile.renameTo(originalFileBackup);

            //Copy over again
            testFile = TestUtil.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

            //Read and save chnages
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.AMAZON_ID, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");

            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        } finally {
            originalFileBackup.delete();
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testSavingMp4File() {
        File testFile = TestUtil.copyAudioToTmp("test8.m4a");
        if (!testFile.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File originalFileBackup = null;

        Exception exceptionCaught = null;
        try {

            testFile = TestUtil.copyAudioToTmp("test8.m4a");
            //Put file in backup location
            originalFileBackup =
                    new File(testFile.getAbsoluteFile().getParentFile().getPath(),
                             Files.getNameWithoutExtension(testFile.getPath()) + ".old");
            testFile.renameTo(originalFileBackup);

            //Copy over again
            testFile = TestUtil.copyAudioToTmp("test8.m4a");

            //Read and save chnages
            AudioFile af = AudioFileIO.read(testFile);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.AMAZON_ID, "fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");

            af.save();

            af = AudioFileIO.read(testFile);
            Assert.assertEquals("fredqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        } finally {
            originalFileBackup.delete();
        }
        Assert.assertNull(exceptionCaught);
    }

}
