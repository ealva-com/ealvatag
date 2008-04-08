package org.jaudiotagger.tag.flac;

import junit.framework.TestCase;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.reference.PictureTypes;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.flac.FlacInfoReader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.ImageIO;

/**
 * basic Flac tests
 */
public class FlacReadTest extends TestCase
{
    /**
     * Read Flac File
     */
    public void testReadTwoChannelFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.flac", new File("test2read.flac"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192", f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
            assertEquals("2", f.getAudioHeader().getChannels());
            assertEquals("44100", f.getAudioHeader().getSampleRate());
            assertEquals(5, f.getAudioHeader().getTrackLength());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read Flac File
     */
    public void testReadSingleChannelFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.flac", new File("test3read.flac"));
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("FLAC 8 bits", f.getAudioHeader().getEncodingType());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("16000", f.getAudioHeader().getSampleRate());
            assertEquals(0, f.getAudioHeader().getTrackLength());
            assertEquals("47", f.getAudioHeader().getBitRate());       //is this correct value
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test can identify file that isnt flac
     */
    public void testNotFlac()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testV1noFlac.flac"));
            AudioFile f = AudioFileIO.read(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof CannotReadException);
    }

    /**
     * Reading file that contains cuesheet
     */
    public void testReadCueSheet()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.flac");
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * test read flac file with preceding ID3 header
     */
    public void testReadFileWithId3Header()
    {
        Exception exceptionCaught = null;
        try
        {
            File orig = new File("testdata", "test22.flac");
            if (!orig.isFile())
            {
                System.out.println("Test cannot be run because test file not available");
                return;
            }
            File testFile = AbstractTestCase.copyAudioToTmp("test22.flac", new File("testreadFlacWithId3.flac"));
            AudioFile f = AudioFileIO.read(testFile);
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
