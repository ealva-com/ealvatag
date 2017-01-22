package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.Tag;
import ealvatag.tag.id3.ID3v1Tag;
import ealvatag.tag.id3.ID3v23Tag;

import java.io.File;

/**
 * Default ID3 Tag when have ID3v1 but no id3 v2 tag
 */
public class Issue443Test extends AbstractTestCase
{
    public void testID3v2DefaultCreateOrConvertWhenOnlyHasID3v1()
    {
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            assertNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            assertNotNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            test.getConvertedTagOrSetNewDefault();
            assertNotNull(test.getID3v1Tag());
            assertNotNull(test.getID3v2Tag());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testID3v2DefaultCreatedWhenOnlyHasID3v1()
    {
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            assertNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            assertNotNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            test.getTagOrSetNewDefault();
            assertNotNull(test.getID3v1Tag());
            assertNotNull(test.getID3v2Tag());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testID3v2CreatedWhenOnlyHasID3v1()
    {
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            MP3File test = new MP3File(testFile);
            assertNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());

            test.setID3v1Tag(new ID3v1Tag());
            assertNotNull(test.getID3v1Tag());
            assertNull(test.getID3v2Tag());
            test.save();

            test = new MP3File(testFile);
            Tag tag = test.getTagOrSetNewDefault();
            assertTrue(tag instanceof ID3v23Tag);


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
