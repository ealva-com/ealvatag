package org.jaudiotagger.tag.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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


            assertTrue(f.getTag() instanceof GenericTag);
            WavTag tag = (WavTag) f.getTag();

            //Ease of use methods for common fields
            assertEquals("*artistName\0*", "*"+tag.getFirst(FieldKey.ARTIST)+"*");
            assertEquals("*albumName\0*", "*"+tag.getFirst(FieldKey.ALBUM)+"*");
            assertEquals("*test123\0*", "*"+tag.getFirst(FieldKey.TITLE)+"*");
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
}
