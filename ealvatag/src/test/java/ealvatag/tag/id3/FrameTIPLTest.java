package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyIPLS;
import ealvatag.tag.id3.framebody.FrameBodyTIPL;
import ealvatag.tag.id3.framebody.FrameBodyTIPLTest;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test TIPL Frame
 */
public class FrameTIPLTest {
    public static ID3v24Frame getInitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        FrameBodyTIPL fb = FrameBodyTIPLTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v24Frame getInitialisedFrameOdd() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        FrameBodyTIPL fb = FrameBodyTIPLTest.getInitialisedBodyOdd();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v23Frame getV23InitialisedFrame() {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE);
        FrameBodyTIPL fb = FrameBodyTIPLTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    @Test public void testCreateID3v24Frame() {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyTIPL fb = null;
        try {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
            fb = FrameBodyTIPLTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());

    }

    @Test public void testCreateID3v23Frame() {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyTIPL fb = null;
        try {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE);
            fb = FrameBodyTIPLTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE, frame.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertFalse(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, fb.getText());
    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1016.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTIPLTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        FrameBodyTIPL body = (FrameBodyTIPL)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    @Test public void testSaveToFileOdd() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1016.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTIPLTest.getInitialisedFrameOdd());
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        FrameBodyTIPL body = (FrameBodyTIPL)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    @Test public void testSaveEmptyFrameToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1004.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        frame.setBody(new FrameBodyTIPL());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
        FrameBodyTIPL body = (FrameBodyTIPL)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    @Test public void testConvertV24ToV23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1005.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTIPLTest.getInitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        ID3v23Tag v23Tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        mp3File.setID3v2TagOnly(v23Tag);

        Assert.assertTrue(v23Tag.hasFrame("IPLS"));
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_INVOLVED_PEOPLE);
        FrameBodyIPLS body = (FrameBodyIPLS)frame.getBody();
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTIPLTest.INVOLVED_PEOPLE, body.getText());
        Assert.assertEquals("producer", body.getKeyAtIndex(0));
        Assert.assertEquals("eno,lanois", body.getValueAtIndex(0));
    }
}
