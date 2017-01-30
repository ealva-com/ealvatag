package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyPCNT;
import ealvatag.tag.id3.framebody.FrameBodyPCNTTest;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test PCNTFrameBody
 */
public class FramePCNTTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    private static ID3v24Frame getInitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT fb = FrameBodyPCNTTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    @Test public void testCreateID3v24Frame() {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyPCNT fb = null;
        try {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
            fb = FrameBodyPCNTTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, ((FrameBodyPCNT)frame.getBody()).getCounter());
        Assert.assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }


    @Test public void testCreateID3v23Frame() {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyPCNT fb = null;
        try {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER);
            fb = FrameBodyPCNTTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, ((FrameBodyPCNT)frame.getBody()).getCounter());
    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePCNTTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT body = (FrameBodyPCNT)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, body.getCounter());
    }

    @Test public void testSaveEmptyFrameToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        frame.setBody(new FrameBodyPCNT());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT body = (FrameBodyPCNT)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(0, body.getCounter());
    }
}
