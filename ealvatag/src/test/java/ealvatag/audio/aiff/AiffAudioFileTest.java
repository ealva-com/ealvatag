package ealvatag.audio.aiff;


import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import junit.framework.TestCase;

import java.io.File;

public class AiffAudioFileTest extends TestCase {

    public void testReadAifcNotCompressed() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("128", ah.getBitRate());
        assertEquals(128, ah.getBitRateAsNumber());
        assertEquals("2", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(3, ah.getTrackLength());
        assertEquals("not compressed", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2.936625d, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    public void testReadAiff2() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test120.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        assertEquals("1411", ah.getBitRate());
        assertEquals(1411, ah.getBitRateAsNumber());
        assertFalse(ah.isVariableBitRate());
        assertEquals("2", ah.getChannels());
        assertEquals("44100", ah.getSampleRate());
        assertEquals(1, ah.getTrackLength());
        assertEquals("not compressed", ah.getEncodingType());
        assertEquals(0.84d, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertTrue(aah.getAnnotations().isEmpty());
    }

    public void testReadAiff3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        assertEquals("1411", ah.getBitRate());
        assertEquals(1411, ah.getBitRateAsNumber());
        assertFalse(ah.isVariableBitRate());
        assertEquals("2", ah.getChannels());
        assertEquals("44100", ah.getSampleRate());
        assertEquals(5, ah.getTrackLength());
        assertEquals("not compressed", ah.getEncodingType());
        assertEquals(5.0, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertTrue(aah.getAnnotations().isEmpty());
    }

    public void testReadAifcCompressedAlaw() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test132.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("128", ah.getBitRate());
        assertEquals(128, ah.getBitRateAsNumber());
        assertEquals("2", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(3, ah.getTrackLength());
        assertEquals("Alaw 2:1", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(2.936625d, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    public void testReadAifcCompressedUlaw() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test133.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("128", ah.getBitRate());
        assertEquals(128, ah.getBitRateAsNumber());
        assertEquals("2", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(3, ah.getTrackLength());
        assertEquals("µlaw 2:1", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(2.936625d, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    public void testReadAifcFloating64() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test134.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("1024", ah.getBitRate());
        assertEquals(1024, ah.getBitRateAsNumber());
        assertEquals("2", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(3, ah.getTrackLength());
        assertEquals("PCM 64-bit", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(2.936625d, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

    }

    public void testReadAifcSSNDBeforeCOMMChunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test135.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("199", ah.getBitRate());
        assertEquals(199, ah.getBitRateAsNumber());
        assertEquals("1", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(0, ah.getTrackLength());
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(0.001125, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    public void testReadAifcWithOddChunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("199", ah.getBitRate());
        assertEquals(199, ah.getBitRateAsNumber());
        assertEquals("1", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(0, ah.getTrackLength());
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(0.001125, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    public void testReadAifcWithJunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test137.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("199", ah.getBitRate());
        assertEquals(199, ah.getBitRateAsNumber());
        assertEquals("1", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(0, ah.getTrackLength());
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(0.001125, ah.getPreciseTrackLength());
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    public void testReadAiffUnknownCompressionAndNameChunkAndCopyrightChunks() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test138.aiff");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("19", ah.getBitRate());
        assertEquals(19, ah.getBitRateAsNumber());
        assertEquals("1", ah.getChannels());
        assertEquals("8000", ah.getSampleRate());
        assertEquals(0, ah.getTrackLength());
        assertEquals("CCITT G.728", ah.getEncodingType());
        assertTrue(ah.isVariableBitRate());
        assertEquals(0.03675, aah.getPreciseTrackLength());
        assertEquals("woodblock", aah.getName());
        assertEquals("Copyright 1991, Prosonus", aah.getCopyright());
        assertEquals("Prosonus", aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }
}
