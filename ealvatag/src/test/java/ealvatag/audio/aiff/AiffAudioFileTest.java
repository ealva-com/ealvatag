package ealvatag.audio.aiff;


import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.audio.Utils;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class AiffAudioFileTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadAifcNotCompressed() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test119.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("128", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(128, ah.getBitRate());
        assertEquals("2", String.valueOf(ah.getChannelCount()));
        assertEquals(8000, ah.getSampleRate());
        assertEquals(3, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("not compressed", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2936625000L, ah.getDuration(TimeUnit.NANOSECONDS, false));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAiff2() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test120.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        assertEquals(1411, ah.getBitRate());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2, ah.getChannelCount());
        assertEquals(44100, ah.getSampleRate());
        assertEquals(1, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("not compressed", ah.getEncodingType());
        assertEquals(840, ah.getDuration(TimeUnit.MILLISECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertTrue(aah.getAnnotations().isEmpty());
    }

    @Test public void testReadAiff3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test121.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        AiffAudioHeader aah = (AiffAudioHeader)ah;

        System.out.println(ah);

        assertEquals("1411", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(1411, ah.getBitRate());
        assertFalse(ah.isVariableBitRate());
        assertEquals("2", String.valueOf(ah.getChannelCount()));
        assertEquals("44100", String.valueOf(ah.getSampleRate()));
        assertEquals(5, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("not compressed", ah.getEncodingType());
        assertEquals(5L, ah.getDuration(TimeUnit.SECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertTrue(aah.getAnnotations().isEmpty());
    }

    @Test public void testReadAifcCompressedAlaw() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test132.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals(128, ah.getBitRate());
        assertEquals(2, ah.getChannelCount());
        assertEquals(8000, ah.getSampleRate());
        assertEquals(3, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("Alaw 2:1", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2936625000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAifcCompressedUlaw() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test133.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("128", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(128, ah.getBitRate());
        assertEquals("2", String.valueOf(ah.getChannelCount()));
        assertEquals("8000", String.valueOf(ah.getSampleRate()));
        assertEquals(3, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("µlaw 2:1", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2936625000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));
    }

    @Test public void testReadAifcFloating64() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test134.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("1024", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(1024, ah.getBitRate());
        assertEquals("2", String.valueOf(ah.getChannelCount()));
        assertEquals("8000", String.valueOf(ah.getSampleRate()));
        assertEquals(3, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("PCM 64-bit", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(2936625000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(1, aah.getAnnotations().size());
        //assertEquals("AFspdate: 2003-01-30 03:28:35 UTC\u0000user: kabal@CAPELLA\u0000program: CopyAudio ", aah.getAnnotations().get(0));

    }

    @Test public void testReadAifcSSNDBeforeCOMMChunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test135.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("199", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(199, ah.getBitRate());
        assertEquals("1", String.valueOf(ah.getChannelCount()));
        assertEquals("8000", String.valueOf(ah.getSampleRate()));
        assertEquals(0, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(1125000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAifcWithOddChunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test136.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals(199, ah.getBitRate());
        assertEquals(1, ah.getChannelCount());
        assertEquals(8000, ah.getSampleRate());
        assertEquals(0, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(1125000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAifcWithJunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test137.aif");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("199", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(199, ah.getBitRate());
        assertEquals("1", String.valueOf(ah.getChannelCount()));
        assertEquals("8000", String.valueOf(ah.getSampleRate()));
        assertEquals(0, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("CCITT G.711 u-law", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(1125000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertNull(aah.getName());
        assertNull(aah.getCopyright());
        assertNull(aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }

    @Test public void testReadAiffUnknownCompressionAndNameChunkAndCopyrightChunks() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test138.aiff");
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        AiffAudioHeader aah = (AiffAudioHeader)ah;
        assertEquals("19", Utils.formatBitRate(ah, ah.getBitRate()));
        assertEquals(19, ah.getBitRate());
        assertEquals("1", String.valueOf(ah.getChannelCount()));
        assertEquals("8000", String.valueOf(ah.getSampleRate()));
        assertEquals(0, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals("CCITT G.728", ah.getEncodingType());
        assertFalse(ah.isVariableBitRate());
        assertEquals(36750000L, aah.getDuration(TimeUnit.NANOSECONDS, true));
        assertEquals("woodblock", aah.getName());
        assertEquals("Copyright 1991, Prosonus", aah.getCopyright());
        assertEquals("Prosonus", aah.getAuthor());
        assertTrue(aah.getComments().isEmpty());
        assertEquals(0, aah.getAnnotations().size());
    }
}
