package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.id3.framebody.FrameBodyTRCK;
import ealvatag.tag.id3.framebody.FrameBodyTRCKTest;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test TRCKFrame
 */
public class FrameTRCKTest {
    private static ID3v24Frame getInitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_TRACK);
        FrameBodyTRCK fb = FrameBodyTRCKTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testCreateID3v24Frame() {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyTRCK fb = null;
        try {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_TRACK);
            fb = FrameBodyTRCKTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_TRACK, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("1/11", ((FrameBodyTRCK)frame.getBody()).getText());
        Assert.assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }


    @Test public void testCreateID3v23Frame() {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyTRCK fb = null;
        try {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TRACK);
            fb = FrameBodyTRCKTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_TRACK, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals("1/11", ((FrameBodyTRCK)frame.getBody()).getText());

    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTRCKTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_TRACK);
        FrameBodyTRCK body = (FrameBodyTRCK)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals("1/11", ((FrameBodyTRCK)frame.getBody()).getText());

    }

    @Test public void testSaveEmptyFrameToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_TRACK);
        frame.setBody(new FrameBodyTRCK());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_TRACK);
        FrameBodyTRCK body = (FrameBodyTRCK)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals("", ((FrameBodyTRCK)frame.getBody()).getText());
    }

    @Test public void testMergingMultipleTrackFrames() throws Exception {
        ID3v24Tag tag = new ID3v24Tag();
        tag.setField(tag.createField(FieldKey.TRACK, "1"));
        tag.setField(tag.createField(FieldKey.TRACK_TOTAL, "10"));
        Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
        Assert.assertEquals("10", tag.getFirst(FieldKey.TRACK_TOTAL));
        Assert.assertTrue(tag.getFrame("TRCK") instanceof AbstractID3v2Frame);
    }

}
