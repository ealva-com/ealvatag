package org.jaudiotagger.audio.aiff;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;

public class AiffAudioFileTest extends TestCase {

    public void testReadAifcNotCompressed() {
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
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("128",ah.getBitRate());
            assertEquals(128, ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(3,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getEncodingType());
            assertFalse(ah.isVariableBitRate());
            assertEquals(2.936625d,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(1, aah.getAnnotations().size());
            //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

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

            assertEquals("1411",ah.getBitRate());
            assertEquals(1411,ah.getBitRateAsNumber());
            assertFalse(ah.isVariableBitRate());
            assertEquals("2",ah.getChannels());
            assertEquals("44100", ah.getSampleRate());
            assertEquals(1,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getEncodingType());
            assertEquals(0.84d,((AiffAudioHeader) ah).getPreciseTrackLength());
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

            assertEquals("1411",ah.getBitRate());
            assertEquals(1411,ah.getBitRateAsNumber());
            assertFalse(ah.isVariableBitRate());
            assertEquals("2",ah.getChannels());
            assertEquals("44100", ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getEncodingType());
            assertEquals(5.0,((AiffAudioHeader) ah).getPreciseTrackLength());
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

    public void testReadAifcCompressedAlaw() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test132.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test132.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("128",ah.getBitRate());
            assertEquals(128, ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(3,ah.getTrackLength());
            assertEquals("Alaw 2:1", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(2.936625d,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(1, aah.getAnnotations().size());
            //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAifcCompressedUlaw() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test133.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test133.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("128",ah.getBitRate());
            assertEquals(128, ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(3,ah.getTrackLength());
            assertEquals("µlaw 2:1", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(2.936625d,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(1, aah.getAnnotations().size());
            //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAifcFloating64() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test134.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test134.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("1024",ah.getBitRate());
            assertEquals(1024, ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(3,ah.getTrackLength());
            assertEquals("PCM 64-bit", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(2.936625d,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(1, aah.getAnnotations().size());
            //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAifcSSNDBeforeCOMMChunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test135.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test135.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("199",ah.getBitRate());
            assertEquals(199, ah.getBitRateAsNumber());
            assertEquals("1",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(0,ah.getTrackLength());
            assertEquals("CCITT G.711 u-law", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(0.001125,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(0, aah.getAnnotations().size());


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAifcWithOddChunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test136.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("199",ah.getBitRate());
            assertEquals(199, ah.getBitRateAsNumber());
            assertEquals("1",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(0,ah.getTrackLength());
            assertEquals("CCITT G.711 u-law", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(0.001125,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(0, aah.getAnnotations().size());


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAifcWithJunk() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test137.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test137.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("199",ah.getBitRate());
            assertEquals(199, ah.getBitRateAsNumber());
            assertEquals("1",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(0,ah.getTrackLength());
            assertEquals("CCITT G.711 u-law", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(0.001125,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertNull(aah.getName());
            assertNull(aah.getCopyright());
            assertNull(aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(0, aah.getAnnotations().size());


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadAiffUnknownCompressionAndNameChunkAndCopyrightChunks() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test138.aiff");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test138.aiff");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("19",ah.getBitRate());
            assertEquals(19, ah.getBitRateAsNumber());
            assertEquals("1",ah.getChannels());
            assertEquals("8000", ah.getSampleRate());
            assertEquals(0,ah.getTrackLength());
            assertEquals("CCITT G.728", ((AiffAudioHeader) ah).getEncodingType());
            assertTrue(ah.isVariableBitRate());
            assertEquals(0.03675,((AiffAudioHeader) ah).getPreciseTrackLength());
            assertEquals("woodblock",aah.getName());
            assertEquals("Copyright 1991, Prosonus",aah.getCopyright());
            assertEquals("Prosonus",aah.getAuthor());
            assertTrue(aah.getComments().isEmpty());
            assertEquals(0, aah.getAnnotations().size());


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }
}
