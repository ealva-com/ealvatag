package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodySYTC;
import ealvatag.tag.id3.framebody.FrameBodySYTCTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Testing Sytc.
 */
public class FrameSYTCTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    private static ID3v24Frame getInitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC fb = FrameBodySYTCTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        final ID3v24Frame referenceFrame = FrameSYTCTest.getInitialisedFrame();
        final FrameBodySYTC referenceBody = (FrameBodySYTC)referenceFrame.getBody();
        tag.setFrame(referenceFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC body = (FrameBodySYTC)frame.getBody();
        Assert.assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());

        final Iterator<Map.Entry<Long, Integer>> reference = referenceBody.getTempi().entrySet().iterator();
        final Iterator<Map.Entry<Long, Integer>> loaded = body.getTempi().entrySet().iterator();
        while (reference.hasNext() && loaded.hasNext()) {
            final Map.Entry<Long, Integer> refEntry = reference.next();
            final Map.Entry<Long, Integer> loadedEntry = loaded.next();
            Assert.assertEquals(refEntry.getKey(), loadedEntry.getKey());
            Assert.assertEquals(refEntry.getValue(), loadedEntry.getValue());
        }
        Assert.assertFalse(reference.hasNext());
        Assert.assertFalse(loaded.hasNext());
    }

    @Test public void testSaveEmptyFrameToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        final FrameBodySYTC referenceBody = new FrameBodySYTC();
        frame.setBody(referenceBody);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_SYNC_TEMPO);
        FrameBodySYTC body = (FrameBodySYTC)frame.getBody();
        Assert.assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());
        Assert.assertEquals(referenceBody.getTempi(), body.getTempi());
    }
}
