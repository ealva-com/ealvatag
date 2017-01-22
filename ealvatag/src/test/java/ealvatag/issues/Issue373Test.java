package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Test writing of new files
 */
public class Issue373Test extends AbstractTestCase {
    public void testIssue() throws Exception {
        Exception caught = null;
        try {
            File orig = new File("testdata", "test94.mp3");
            if (!orig.isFile()) {
                System.err.println("Unable to test file - not available");
                return;
            }

            File testFile = AbstractTestCase.copyAudioToTmp("test94.mp3");


            AudioFile af = AudioFileIO.read(testFile);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();
            af.getTag().setField(FieldKey.ARTIST, "artist");

            Thread.sleep(20000);
            //Now open in another program to lock it, cannot reproduce programtically
            //FileChannel channel = new RandomAccessFile(testFile, "rw").getChannel();
            //FileLock lock = channel.lock();

            af.save();
        } catch (Exception e) {
            caught = e;
            e.printStackTrace();
        }
        assertNull(caught);
    }
}
