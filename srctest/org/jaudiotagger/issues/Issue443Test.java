package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTRDA;
import org.jaudiotagger.tag.reference.ID3V2Version;

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
            test.commit();

            test = new MP3File(testFile);
            test.getTagAndConvertOrCreateAndSetDefault();
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
            test.commit();

            test = new MP3File(testFile);
            test.getTagOrCreateAndSetDefault();
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
            test.commit();

            test = new MP3File(testFile);
            Tag tag = test.getTagOrCreateDefault();
            assertTrue(tag instanceof ID3v23Tag);


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
