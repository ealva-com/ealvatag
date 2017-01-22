package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Test Creating Null fields
 */
public class Issue185Test extends AbstractTestCase {

    public void testDefaultTagMp3() {
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            //No Tag
            assertNull(af.getTag());

            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            af.setNewDefaultTag();

            assertTrue(af.getTag() instanceof ID3v23Tag);

            //Save changes
            af.save();

            af = AudioFileIO.read(testFile);
            assertTrue(af.getTag() instanceof ID3v23Tag);

        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    public void testDefaultTagMp3AndCreate() {
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            AudioFile af = AudioFileIO.read(testFile);

            //No Tag
            assertNull(af.getTag());

            //Tag Created and setField
            Tag tag = af.getTagOrSetNewDefault();
            assertTrue(tag instanceof ID3v23Tag);
            assertTrue(af.getTag() instanceof ID3v23Tag);

            //Save changes
            af.save();

            af = AudioFileIO.read(testFile);
            assertTrue(af.getTag() instanceof ID3v23Tag);

        } catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
