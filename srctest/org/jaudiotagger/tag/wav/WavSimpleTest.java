package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericTag;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavSimpleTest extends AbstractTestCase
{
     public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.wav");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("176",f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 8 bits",f.getAudioHeader().getEncodingType());
            assertEquals("1",f.getAudioHeader().getChannels());
            assertEquals("22050",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed concept should be asftag
            GenericTag tag = (GenericTag)f.getTag();


              //Ease of use methods for common fields
            assertEquals("", tag.getFirstArtist());
            assertEquals("", tag.getFirstAlbum());
            assertEquals("", tag.getFirstTitle());
            assertEquals("", tag.getFirstComment());
            assertEquals("", tag.getFirstYear());
            assertEquals("", tag.getFirstTrack());
            assertEquals("", tag.getFirstGenre());
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /* Doesnt support writing currently
     public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.wav",new File("testwrite1.wav"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("176",f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 8 bits",f.getAudioHeader().getEncodingType());
            assertEquals("1",f.getAudioHeader().getChannels());
            assertEquals("22050",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed concept hould be wavtag
            GenericTag tag = (GenericTag)f.getTag();

            //Write some new values and save
            tag.setArtist("artist2");
            tag.setAlbum("album2");
            tag.setTitle("tracktitle2");
            tag.setComment("comments2");
            tag.setYear("1972");
            tag.setGenre("genre2");
            tag.setTrack("4");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (GenericTag)f.getTag();

            assertEquals("artist2", tag.getFirstArtist());
            assertEquals("album2", tag.getFirstAlbum());
            assertEquals("tracktitle2", tag.getFirstTitle());
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("genre2", tag.getFirstGenre());
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
    */
}
