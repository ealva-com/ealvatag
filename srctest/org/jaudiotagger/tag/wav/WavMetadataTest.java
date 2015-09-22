package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataTest extends AbstractTestCase
{


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testReadFileWithMetadata()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 24 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("artistName\0",tag.getFirst(FieldKey.ARTIST));
            assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            assertEquals("conductor\0", tag.getFirst(FieldKey.CONDUCTOR));
            assertEquals("lyricist\0", tag.getFirst(FieldKey.LYRICIST));
            assertEquals("composer\0", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("albumArtist\0", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("100\0", tag.getFirst(FieldKey.RATING));
            assertEquals("encoder\0", tag.getFirst(FieldKey.ENCODER));
            assertEquals("ISRC\0", tag.getFirst(FieldKey.ISRC));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadata()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 24 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("artistName\0",tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST,"fred");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag)tag).getInfoTag());
            assertEquals("fred",tag.getFirst(FieldKey.ARTIST));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadata()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("WAV-RIFF 24 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof WavTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("artistName\0",tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST,"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST,"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.commit();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof WavTag);
            tag = (WavTag) f.getTag();
            System.out.println(((WavTag)tag).getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",tag.getFirst(FieldKey.ALBUM_ARTIST));


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
