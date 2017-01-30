package ealvatag.tag.mp4;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp4.EncoderType;
import ealvatag.audio.mp4.Mp4AudioHeader;
import ealvatag.audio.mp4.atom.Mp4EsdsBox;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 */
public class M4aReadDrmTagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test to read all metadata from an Apple iTunes encoded mp4 file, note also uses fixed genre rather than
     * custom genre
     */
    @Test public void testReadFile() {
        Exception exceptionCaught = null;
        try {
            File orig = new File("testdata", "test9.m4p");
            if (!orig.isFile()) {
                return;
            }

            File testFile = TestUtil.copyAudioToTmp("test9.m4p");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(tag);

            //AudioInfo
            //Time in seconds
            Assert.assertEquals(329, f.getAudioHeader().getTrackLength());
            Assert.assertEquals(44100, f.getAudioHeader().getSampleRateAsNumber());
            Assert.assertEquals("2", f.getAudioHeader().getChannels());
            Assert.assertEquals(128, f.getAudioHeader().getBitRateAsNumber());
            Assert.assertEquals(EncoderType.DRM_AAC.getDescription(), f.getAudioHeader().getEncodingType());

            //MPEG Specific
            Mp4AudioHeader audioheader = (Mp4AudioHeader)f.getAudioHeader();
            Assert.assertEquals(Mp4EsdsBox.Kind.MPEG4_AUDIO, audioheader.getKind());
            Assert.assertEquals(Mp4EsdsBox.AudioProfile.LOW_COMPLEXITY, audioheader.getProfile());

            //Ease of use methods for common fields
            Assert.assertEquals("The King Of The Slums", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Barbarous English Fayre", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("Simpering Blonde Bombshell", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("1990-01-01T08:00:00Z", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("12", tag.getFirst(FieldKey.TRACK_TOTAL));
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));

            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;

            //Lookup by mp4 key
            Assert.assertEquals("The King Of The Slums", mp4tag.getFirst(Mp4FieldKey.ARTIST));
            Assert.assertEquals("Barbarous English Fayre", mp4tag.getFirst(Mp4FieldKey.ALBUM));
            Assert.assertEquals("Simpering Blonde Bombshell", mp4tag.getFirst(Mp4FieldKey.TITLE));
            List coverart = mp4tag.get(Mp4FieldKey.ARTWORK);
            //Should be one image
            Assert.assertEquals(1, coverart.size());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }
}
