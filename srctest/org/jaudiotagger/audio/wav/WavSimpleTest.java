package org.jaudiotagger.audio.wav;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.generic.GenericTag;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavSimpleTest extends AbstractTestCase
{
    public void testRead8bitMonoFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("176", f.getAudioHeader().getBitRate());
            assertEquals(22050, f.getAudioHeader().getByteRate().intValue());
            assertEquals("WAV-RIFF 8 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());
            assertEquals(8, f.getAudioHeader().getBitsPerSample());
            assertEquals(14, f.getAudioHeader().getTrackLength());
            assertTrue(f.getTag() instanceof WavTag);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testRead24BitMonoFile()
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
            System.out.println(f.getAudioHeader());


            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals(66150, f.getAudioHeader().getByteRate().intValue());
            assertEquals("WAV-RIFF 24 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());
            assertEquals(24, f.getAudioHeader().getBitsPerSample());
            assertEquals(14, f.getAudioHeader().getTrackLength());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }



    public void testReadingOfShort()
    {
        ByteBuffer headerBuffer = ByteBuffer.allocate(2);
        headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
        headerBuffer.put((byte) 0xFE);
        headerBuffer.put((byte)0xFF);
        headerBuffer.position(0);
        int format =  headerBuffer.get() & 0xff + (headerBuffer.get() & 0xff) * 256;
        headerBuffer.position(0);
        int formatNew = headerBuffer.getShort() & 0xffff;
        System.out.println("Format:"+format+"("+ Hex.asHex(format)+")"+":FormatNew:"
                +formatNew+"("+Hex.asHex(formatNew)+")");
    }



}
