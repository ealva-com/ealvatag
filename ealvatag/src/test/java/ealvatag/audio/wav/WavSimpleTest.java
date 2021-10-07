package ealvatag.audio.wav;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.tag.NullTag;
import ealvatag.tag.wav.WavTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavSimpleTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testRead8bitMonoFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals("176", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals(22050, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV PCM 8 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(8, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(14, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testRead24BitMonoFile() {
        File orig = new File("testdata", "test105.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available " + orig);
            return;
        }

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test105.wav");
            AudioFile f = AudioFileIO.read(testFile);


            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals(66150, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV PCM 24 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(24, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(14, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    @Test public void testReadingOfShort() {
        ByteBuffer headerBuffer = ByteBuffer.allocate(2);
        headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
        headerBuffer.put((byte)0xFE);
        headerBuffer.put((byte)0xFF);
        headerBuffer.position(0);
        headerBuffer.get();
		headerBuffer.get();
        headerBuffer.position(0);
        headerBuffer.getShort();
    }

    @Test public void testRead8bitStereoFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test127.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals(46986, f.getAudioHeader().getAudioDataLength());
            Assert.assertEquals("128", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals(16000, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV A-LAW 8 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("8000", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(8, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(3, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testGoldstarCompressedStereoFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test129.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals(4875, f.getAudioHeader().getAudioDataLength());
            Assert.assertEquals("13", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals(1625, f.getAudioHeader().getByteRate());
            Assert.assertEquals("GSM_COMPRESSED", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("8000", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(0, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(3, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testRead8bitStereoFileExtensible() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test128.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals(46986, f.getAudioHeader().getAudioDataLength());
            Assert.assertEquals("128", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals(16000, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV A-LAW 8 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals("2", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("8000", String.valueOf(f.getAudioHeader().getSampleRate()));
            Assert.assertEquals(8, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(3, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadStereoFloatingPointFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test130.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals(232128, f.getAudioHeader().getAudioDataLength());
            Assert.assertEquals(1411, f.getAudioHeader().getBitRate());
            Assert.assertEquals(176400, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV IEEE_FLOAT 32 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals(2, f.getAudioHeader().getChannelCount());
            Assert.assertEquals(22050, f.getAudioHeader().getSampleRate());
            Assert.assertEquals(32, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(0, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(0.32897958159446716, f.getAudioHeader().getDurationAsDouble(), 0.000000000000000001);
            Assert.assertEquals(328979582L, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, false));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testReadQuadChannelFile() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test131.wav");
            AudioFile f = AudioFileIO.read(testFile);

            Assert.assertEquals(844056, f.getAudioHeader().getAudioDataLength());
            Assert.assertEquals(1411, f.getAudioHeader().getBitRate());
            Assert.assertEquals(176400, f.getAudioHeader().getByteRate());
            Assert.assertEquals("WAV PCM 16 bits", f.getAudioHeader().getEncodingType());
            Assert.assertEquals(4, f.getAudioHeader().getChannelCount());
            Assert.assertEquals(22050, f.getAudioHeader().getSampleRate());
            Assert.assertEquals(16, f.getAudioHeader().getBitsPerSample());
            Assert.assertEquals(5, f.getAudioHeader().getDuration(TimeUnit.SECONDS, true));
            Assert.assertEquals(4.784897804260254, f.getAudioHeader().getDurationAsDouble(), 0.000000000000000001);
            Assert.assertEquals(4784897804L, f.getAudioHeader().getDuration(TimeUnit.NANOSECONDS, true));
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


}
