package org.jaudiotagger.audio.flac;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

import junit.framework.TestCase;

/**
 * basic Flac tests
 */
public class FlacHeaderTest extends TestCase
{
     public void testReadFileWithVorbisComment()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("206",f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag)f.getTag();
              //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirstArtist());
            assertEquals("Album", tag.getFirstAlbum());
            assertEquals("test3", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("Crossover", tag.getFirstGenre());

            //Lookup by generickey
            assertEquals("Artist", tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("test3", tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(TagFieldKey.YEAR));
            assertEquals("4", tag.getFirst(TagFieldKey.TRACK));
            assertEquals("Composer", tag.getFirst(TagFieldKey.COMPOSER));           
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Only contains vorbis comment with minimum encoder info 
     */
     public void testReadFileWithOnlyVorbisCommentEncoder()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("206",f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof VorbisCommentTag);
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
