package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Frames;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.framebody.FrameBodyTCOP;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test read large mp3 with extended header
 */
public class Issue269Test {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Test read mp3 that says it has extended header but doesnt really
     */
    @Test public void testReadMp3WithExtendedHeaderFlagSetButNoExtendedHeader() {
        File orig = new File("testdata", "test46.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test46.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("00000", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.BPM));
            Assert.assertEquals("thievery corporation - Om Lounge", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read mp3 with extended header and crc-32 check
     */
    @Test public void testReadID3v23Mp3WithExtendedHeaderAndCrc() {
        File orig = new File("testdata", "test47.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test47.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("tonight (instrumental)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Young Gunz", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(156497728, id3v23Tag.getCrc32());
            Assert.assertEquals(0, id3v23Tag.getPaddingSize());

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read and stores encrypted frames separately, and that they are preserved when writing frame
     */
    @Test public void testReadMp3WithEncryptedField() {
        File orig = new File("testdata", "test48.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test48.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("Don't Leave Me", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("All-American Rejects", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(0, id3v23Tag.getPaddingSize());

            AbstractID3v2Frame frame1 = (AbstractID3v2Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_COPYRIGHTINFO);
            Assert.assertNotNull(frame1);
            Assert.assertEquals("", ((FrameBodyTCOP)frame1.getBody()).getText());

            //This frame is marked as encrypted(Although it actually isn't) so we cant decode it we should just store it
            //as a special encrypted frame.
            ID3v23Frame frame = (ID3v23Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            Assert.assertNull(frame);
            frame = (ID3v23Frame)id3v23Tag.getEncryptedFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            Assert.assertNotNull(frame);
            Assert.assertEquals(0x22, frame.getEncryptionMethod());
            Assert.assertEquals(0, frame.getGroupIdentifier());
            Assert.assertEquals(0, frame.getStatusFlags().getOriginalFlags());
            //ealvatag converts to this because encodeby frame should be updated if the audio is changed and so
            //this falg should be set
            Assert.assertEquals(0x40, frame.getStatusFlags().getWriteFlags());
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

            //The frame is preserved and still encrypted
            frame = (ID3v23Frame)id3v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            Assert.assertNull(frame);
            frame = (ID3v23Frame)id3v23Tag.getEncryptedFrame(ID3v23Frames.FRAME_ID_V3_ENCODEDBY);
            Assert.assertNotNull(frame);
            //Encryption Method Preserved
            Assert.assertEquals(0x22, frame.getEncryptionMethod());
            Assert.assertEquals(0, frame.getGroupIdentifier());
            //Note fiel preservation flag now set
            Assert.assertEquals(0x40, frame.getStatusFlags().getOriginalFlags());
            Assert.assertEquals(0x40, frame.getStatusFlags().getWriteFlags());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test read mp3 with extended header and crc-32 check
     */
    @Test public void testReadID3v24Mp3WithExtendedHeaderAndCrc() {
        File orig = new File("testdata", "test47.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try {
            testFile = TestUtil.copyAudioToTmp("test47.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("tonight (instrumental)", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Young Gunz", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));

            ID3v23Tag id3v23Tag = (ID3v23Tag)af.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals(156497728, id3v23Tag.getCrc32());
            Assert.assertEquals(0, id3v23Tag.getPaddingSize());

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "FRED");
            af.save();
            af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().or(NullTag.INSTANCE).toString());
            Assert.assertEquals("FRED", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
    }

}
