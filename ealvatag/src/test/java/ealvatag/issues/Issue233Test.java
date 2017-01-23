package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * Test Deleting v2 tags
 */
public class Issue233Test extends AbstractTestCase
{
    public void testDeletingID3v2Tag()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //No Tags
            MP3File mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v24 tag
            mp3File.setID3v2Tag(new ID3v24Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v23 tag
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

            //Save and deleteField v22 tag
            mp3File.setID3v2Tag(new ID3v22Tag());
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertTrue(mp3File.hasID3v2Tag());

            mp3File.setID3v2Tag(null);
            mp3File.saveMp3();
            mp3File = new MP3File(testFile);
            assertFalse(mp3File.hasID3v1Tag());
            assertFalse(mp3File.hasID3v2Tag());

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testDeletingID3v1Tag()
    {
        File orig = new File("testdata", "test32.mp3");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File        testFile    = AbstractTestCase.copyAudioToTmp("test32.mp3");
            AudioFile   af          = AudioFileIO.read(testFile);
            af.deleteFileTag();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

    public void testReadingID3v1Tag()
    {
        File orig = new File("testdata", "test32.mp3");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File        testFile    = AbstractTestCase.copyAudioToTmp("test32.mp3");
            AudioFile   af          = AudioFileIO.read(testFile);
            MP3File     mf          = (MP3File)af;
            assertEquals("The Ides Of March",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("Iron Maiden",mf.getID3v1Tag().getFirst(FieldKey.ARTIST));
            assertEquals("",mf.getID3v2Tag().getFirst(FieldKey.ARTIST));
            assertEquals("",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));


        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }
}
