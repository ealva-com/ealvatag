package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyAPIC;
import ealvatag.tag.id3.framebody.FrameBodyAPICTest;
import ealvatag.tag.id3.framebody.FrameBodyPIC;
import ealvatag.tag.id3.framebody.FrameBodyPICTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test PIC (v22) and APIC (v23,v24) frames
 */
public class FramePICAndAPICTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    public static ID3v24Frame getV24InitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        FrameBodyAPIC fb = FrameBodyAPICTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v23Frame getV23InitialisedFrame() {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE);
        FrameBodyAPIC fb = FrameBodyAPICTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v22Frame getV22InitialisedFrame() {
        ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE);
        FrameBodyPIC fb = FrameBodyPICTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    @Test public void testCreateID3v24Frame() {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyAPIC fb = null;
        try {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
            fb = FrameBodyAPICTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE, frame.getIdentifier());
        Assert.assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        Assert.assertEquals(FrameBodyAPICTest.DESCRIPTION, fb.getDescription());
    }


    @Test public void testCreateID3v23Frame() {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyAPIC fb = null;
        try {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE);
            fb = FrameBodyAPICTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE, frame.getIdentifier());
        Assert.assertFalse(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        Assert.assertEquals(FrameBodyAPICTest.DESCRIPTION, fb.getDescription());
    }


    @Test public void testCreateID3v22Frame() {
        Exception exceptionCaught = null;
        ID3v22Frame frame = null;
        FrameBodyPIC fb = null;
        try {
            frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE);
            fb = FrameBodyPICTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE, frame.getIdentifier());
        Assert.assertFalse(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        Assert.assertTrue(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        Assert.assertEquals(FrameBodyPICTest.DESCRIPTION, fb.getDescription());
    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePICAndAPICTest.getV24InitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertTrue(frame != null);
        FrameBodyAPIC body = (FrameBodyAPIC)frame.getBody();
        Assert.assertTrue(body instanceof FrameBodyAPIC);
        Assert.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription());

    }


    @Test public void testConvertV24ToV23() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePICAndAPICTest.getV24InitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v23Tag(mp3File.getID3v2TagAsv24()));
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE);
        Assert.assertTrue(frame != null);
        FrameBodyAPIC body = (FrameBodyAPIC)frame.getBody();
        Assert.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription());
    }

    @Test public void testConvertV24ToV22() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePICAndAPICTest.getV24InitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload and convert to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2TagAsv24()));
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame)mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_ATTACHED_PICTURE);
        Assert.assertTrue(frame != null);
        FrameBodyPIC body = (FrameBodyPIC)frame.getBody();
        Assert.assertTrue(body instanceof FrameBodyPIC);
        Assert.assertEquals(FrameBodyAPICTest.DESCRIPTION, body.getDescription());
    }


    @Test public void testConvertV22ToV24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v22Tag tag = new ID3v22Tag();

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        tag.setFrame(FramePICAndAPICTest.getV22InitialisedFrame());

        mp3File.setID3v2TagOnly((ID3v22Tag)tag);
        mp3File.saveMp3();

        //Reload and convert from v22 to v24 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v24Tag(mp3File.getID3v2Tag()));
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertTrue(frame != null);
        FrameBodyAPIC body = (FrameBodyAPIC)frame.getBody();
        Assert.assertTrue(body instanceof FrameBodyAPIC);
        Assert.assertEquals(FrameBodyPICTest.DESCRIPTION, body.getDescription());

    }
}
