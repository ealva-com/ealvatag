package ealvatag.audio.aiff;


import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class AiffAudioFileTest {

    @Test public void testReadAifcNotCompressed() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("128", ah.getBitRate());
        Assert.assertEquals(128, ah.getBitRateAsNumber());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(3, ah.getTrackLength());
        Assert.assertEquals("not compressed", ah.getEncodingType());
        Assert.assertFalse(ah.isVariableBitRate());
        Assert.assertEquals(2.936625d, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAiff2() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test120.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        Assert.assertEquals("1411", ah.getBitRate());
        Assert.assertEquals(1411, ah.getBitRateAsNumber());
        Assert.assertFalse(ah.isVariableBitRate());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("44100", ah.getSampleRate());
        Assert.assertEquals(1, ah.getTrackLength());
        Assert.assertEquals("not compressed", ah.getEncodingType());
        Assert.assertEquals(0.84d, ah.getPreciseTrackLength(), 0.001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertTrue(aah.getAnnotations().isEmpty());
    }

    @Test public void testReadAiff3() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        Assert.assertEquals("1411", ah.getBitRate());
        Assert.assertEquals(1411, ah.getBitRateAsNumber());
        Assert.assertFalse(ah.isVariableBitRate());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("44100", ah.getSampleRate());
        Assert.assertEquals(5, ah.getTrackLength());
        Assert.assertEquals("not compressed", ah.getEncodingType());
        Assert.assertEquals(5.0, ah.getPreciseTrackLength(), 0.01);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertTrue(aah.getAnnotations().isEmpty());
    }

    @Test public void testReadAifcCompressedAlaw() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test132.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("128", ah.getBitRate());
        Assert.assertEquals(128, ah.getBitRateAsNumber());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(3, ah.getTrackLength());
        Assert.assertEquals("Alaw 2:1", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(2.936625d, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAifcCompressedUlaw() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test133.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("128", ah.getBitRate());
        Assert.assertEquals(128, ah.getBitRateAsNumber());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(3, ah.getTrackLength());
        Assert.assertEquals("µlaw 2:1", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(2.936625d, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAifcFloating64() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test134.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("1024", ah.getBitRate());
        Assert.assertEquals(1024, ah.getBitRateAsNumber());
        Assert.assertEquals("2", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(3, ah.getTrackLength());
        Assert.assertEquals("PCM 64-bit", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(2.936625d, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

    }

    @Test public void testReadAifcSSNDBeforeCOMMChunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test135.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("199", ah.getBitRate());
        Assert.assertEquals(199, ah.getBitRateAsNumber());
        Assert.assertEquals("1", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(0, ah.getTrackLength());
        Assert.assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(0.001125, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAifcWithOddChunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test136.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("199", ah.getBitRate());
        Assert.assertEquals(199, ah.getBitRateAsNumber());
        Assert.assertEquals("1", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(0, ah.getTrackLength());
        Assert.assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(0.001125, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAifcWithJunk() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test137.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("199", ah.getBitRate());
        Assert.assertEquals(199, ah.getBitRateAsNumber());
        Assert.assertEquals("1", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(0, ah.getTrackLength());
        Assert.assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(0.001125, ah.getPreciseTrackLength(), 0.0000001);
        Assert.assertNull(aah.getName());
        Assert.assertNull(aah.getCopyright());
        Assert.assertNull(aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAiffUnknownCompressionAndNameChunkAndCopyrightChunks() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test138.aiff");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        Assert.assertEquals("19", ah.getBitRate());
        Assert.assertEquals(19, ah.getBitRateAsNumber());
        Assert.assertEquals("1", ah.getChannels());
        Assert.assertEquals("8000", ah.getSampleRate());
        Assert.assertEquals(0, ah.getTrackLength());
        Assert.assertEquals("CCITT G.728", ah.getEncodingType());
        Assert.assertTrue(ah.isVariableBitRate());
        Assert.assertEquals(0.03675, aah.getPreciseTrackLength(), 0.000001);
        Assert.assertEquals("woodblock", aah.getName());
        Assert.assertEquals("Copyright 1991, Prosonus", aah.getCopyright());
        Assert.assertEquals("Prosonus", aah.getAuthor());
        Assert.assertTrue(aah.getComments().isEmpty());
        Assert.assertEquals(0, aah.getAnnotations().size());
    }
}
