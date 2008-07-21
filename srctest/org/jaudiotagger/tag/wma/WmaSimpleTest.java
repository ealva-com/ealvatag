package org.jaudiotagger.tag.wma;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericTag;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WmaSimpleTest extends AbstractTestCase
{
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed concept hould be asftag
            GenericTag tag = (GenericTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("artist", tag.getFirstArtist());
            assertEquals("album", tag.getFirstAlbum());
            assertEquals("tracktitle", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("3", tag.getFirstTrack());
            assertEquals("genre", tag.getFirstGenre());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test1.wma", new File("testwrite1.wma"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("32", f.getAudioHeader().getBitRate());
            assertEquals("ASF (audio): 0x0161 (Windows Media Audio (ver 7,8,9))", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("32000", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed sconcept hould be asftag
            GenericTag tag = (GenericTag) f.getTag();

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
            tag = (GenericTag) f.getTag();

            assertEquals("artist2", tag.getFirstArtist());
            assertEquals("album2", tag.getFirstAlbum());
            assertEquals("tracktitle2", tag.getFirstTitle());
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("genre2", tag.getFirstGenre());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
