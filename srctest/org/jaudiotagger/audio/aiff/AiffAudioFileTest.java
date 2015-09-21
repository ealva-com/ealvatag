package org.jaudiotagger.audio.aiff;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;

public class AiffAudioFileTest extends TestCase {

    public void testReadAiff1() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("128000",ah.getBitRate());
            assertEquals(128000, ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(2,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getAudioEncoding());
            assertFalse(ah.isVariableBitRate());
            //assertEquals(2.936625,((AiffAudioHeader) ah).getPreciseLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(1, aah.getAnnotations().size());
            //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
            System.out.println(ah);
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        

    }

    public void testReadAiff2() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test120.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test120.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            AiffAudioHeader aah = (AiffAudioHeader)ah;

            System.out.println(ah);

            assertEquals("1411200",ah.getBitRate());
            assertEquals(1411200,ah.getBitRateAsNumber());
            assertFalse(ah.isVariableBitRate());
            assertEquals("2",ah.getChannels());
            assertEquals("44100", ah.getSampleRate());
            assertEquals(0,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getAudioEncoding());
            //assertEquals(2.936625,((AiffAudioHeader) ah).getPreciseLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertTrue(aah.getAnnotations().isEmpty());

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            AiffAudioHeader aah = (AiffAudioHeader)ah;

            System.out.println(ah);

            assertEquals("1411200",ah.getBitRate());
            assertEquals(1411200,ah.getBitRateAsNumber());
            assertFalse(ah.isVariableBitRate());
            assertEquals("2",ah.getChannels());
            assertEquals("44100", ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getAudioEncoding());
            //assertEquals(2.936625,((AiffAudioHeader) ah).getPreciseLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertTrue(aah.getAnnotations().isEmpty());

        }
        catch (Exception e  ) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

}
