package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.FieldKey;

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

            assertEquals("176", f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 8 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed concept should be asftag
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));
            assertEquals("", tag.getFirst(FieldKey.ALBUM));
            assertEquals("", tag.getFirst(FieldKey.TITLE));
            assertEquals("", tag.getFirst(FieldKey.COMMENT));
            assertEquals("", tag.getFirst(FieldKey.YEAR));
            assertEquals("", tag.getFirst(FieldKey.TRACK));
            assertEquals("", tag.getFirst(FieldKey.GENRE));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testRead24BitFile()
    {
        File orig = new File("testdata", "test105.wav");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test105.wav");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 24 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof GenericTag);        //TODO Flawed concept should be wavtag
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));
            assertEquals("", tag.getFirst(FieldKey.ALBUM));
            assertEquals("", tag.getFirst(FieldKey.TITLE));
            assertEquals("", tag.getFirst(FieldKey.COMMENT));
            assertEquals("", tag.getFirst(FieldKey.YEAR));
            assertEquals("", tag.getFirst(FieldKey.TRACK));
            assertEquals("", tag.getFirst(FieldKey.GENRE));
        }
        catch (Exception e)
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
            tag.setField(FieldKey.ARTIST,("artist2");
            tag.setField(FieldKey.ALBUM,"album2");
            tag.setField(FieldKey.TITLE,"tracktitle2");
            tag.setField(FieldKey.COMMENT,"comments2");
            tag.setField(FieldKey.YEAR,"1972");
            tag.setField(FieldKey.GENRE,("genre2");
            tag.setField(FieldKey.TRACK,"4");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (GenericTag)f.getTag();

            assertEquals("artist2", tag.getFirst(FieldKey.ARTIST));
            assertEquals("album2", tag.getFirst(FieldKey.ALBUM));
            assertEquals("tracktitle2", tag.getFirst(FieldKey.TITLE));
            assertEquals("comments2", tag.getFirstComment());
            assertEquals("1972", tag.getFirst(FieldKey.YEAR));
            assertEquals("4", tag.getFirst(FieldKey.TRACK));
            assertEquals("genre2", tag.getFirst(FieldKey.GENRE));
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
