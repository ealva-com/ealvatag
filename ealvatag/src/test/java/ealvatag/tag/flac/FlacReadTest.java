package ealvatag.tag.flac;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.flac.FlacInfoReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * basic Flac tests
 */
public class FlacReadTest {
    @Test public void testReadTwoChannelFile() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test2.flac", new File("test2read.flac"));
        AudioFile f = AudioFileIO.read(testFile);

        Assert.assertEquals("192", f.getAudioHeader().getBitRate());
        Assert.assertEquals("FLAC 16 bits", f.getAudioHeader().getEncodingType());
        Assert.assertEquals("2", f.getAudioHeader().getChannels());
        Assert.assertEquals("44100", f.getAudioHeader().getSampleRate());
        Assert.assertEquals(5, f.getAudioHeader().getTrackLength());
    }

    @Test public void testReadSingleChannelFile() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test3.flac", new File("test3read.flac"));
        AudioFile f = AudioFileIO.read(testFile);

        Assert.assertEquals("FLAC 8 bits", f.getAudioHeader().getEncodingType());
        Assert.assertEquals("1", f.getAudioHeader().getChannels());
        Assert.assertEquals("16000", f.getAudioHeader().getSampleRate());
        Assert.assertEquals(1, f.getAudioHeader().getTrackLength());
        Assert.assertEquals("47", f.getAudioHeader().getBitRate());       //is this correct value
    }

    @Test(expected = CannotReadException.class)
    public void testNotFlac() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testV1noFlac.flac"));
        AudioFileIO.read(testFile);
    }

    /**
     * Reading file that contains cuesheet
     */
    @Test public void testReadCueSheet() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test3.flac");
        AudioFile f = AudioFileIO.read(testFile);
        FlacInfoReader infoReader = new FlacInfoReader();
        Assert.assertEquals(5, infoReader.countMetaBlocks(f.getFile()));
    }

// TODO: 1/14/17
//    /**
//     * test read flac file with preceding ID3 header
//     */
//    @Test public void testReadFileWithId3Header() throws Exception {
//        File testFile = AbstractTestCase.copyAudioToTmp("test22.flac", new File("testreadFlacWithId3.flac"));
//        AudioFile f = AudioFileIO.read(testFile);
//        FlacInfoReader infoReader = new FlacInfoReader();
//        Assert.assertEquals(4, infoReader.countMetaBlocks(f.getFile()));
//    }

// TODO: 1/14/17
//    /**
//     * test read flac file with no header
//     */
//    @Test public void testReadFileWithOnlyStreamInfoAndPaddingHeader() throws Exception {
//        File testFile = AbstractTestCase.copyAudioToTmp("test102.flac", new File("test102.flac"));
//        AudioFile f = AudioFileIO.read(testFile);
//        FlacInfoReader infoReader = new FlacInfoReader();
//        Assert.assertEquals(2, infoReader.countMetaBlocks(f.getFile()));
//    }

// TODO: 1/14/17
//    /**
//     * test read flac file with no header
//     */
//    @Test public void testReadArtwork() throws Exception {
//        File testFile = AbstractTestCase.copyAudioToTmp("test154.flac", new File("test154.flac"));
//        AudioFile f = AudioFileIO.read(testFile);
//        MetadataBlockDataPicture mbdp = (((FlacTag)f.getTag()).getImages().get(0));
//        System.out.println(mbdp);
//    }
}
