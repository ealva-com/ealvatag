package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.framebody.FrameBodyETCO;
import ealvatag.tag.id3.framebody.FrameBodyETCOTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Testing Etco (Issue 187)
 */
public class FrameETCOTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    private static ID3v24Frame getInitialisedFrame() {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES);
        FrameBodyETCO fb = FrameBodyETCOTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    /**
     * This tests reading a file that contains an ETCO frame.
     *
     * @throws Exception
     */
    @Test public void testReadFile() throws Exception {
        File orig = new File("testdata", "test20.mp3");
        if (!orig.isFile()) {
            return;
        }
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test20.mp3");
            AudioFile f = AudioFileIO.read(testFile);
            final ID3v23Frame
                    frame =
                    ((ID3v23Frame)((ID3v23Tag)f.getTag().or(NullTag.INSTANCE)).getFrame(ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES));
            FrameBodyETCO body = (FrameBodyETCO)frame.getBody();
            Assert.assertEquals(2, body.getTimestampFormat());
            Assert.assertEquals(1, body.getTimingCodes().size());
            final Map.Entry<Long, int[]> entry = body.getTimingCodes().entrySet().iterator().next();
            Assert.assertEquals(224, entry.getValue()[0]);
            Assert.assertEquals(56963L, (long)entry.getKey());
        } catch (IOException e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testSaveToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        final ID3v24Frame referenceFrame = FrameETCOTest.getInitialisedFrame();
        final FrameBodyETCO referenceBody = (FrameBodyETCO)referenceFrame.getBody();
        tag.setFrame(referenceFrame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES);
        FrameBodyETCO body = (FrameBodyETCO)frame.getBody();
        Assert.assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());

        final Iterator<Map.Entry<Long, int[]>> reference = referenceBody.getTimingCodes().entrySet().iterator();
        final Iterator<Map.Entry<Long, int[]>> loaded = body.getTimingCodes().entrySet().iterator();
        while (reference.hasNext() && loaded.hasNext()) {
            final Map.Entry<Long, int[]> refEntry = reference.next();
            final Map.Entry<Long, int[]> loadedEntry = loaded.next();
            Assert.assertEquals(refEntry.getKey(), loadedEntry.getKey());
            Assert.assertTrue(Arrays.equals(refEntry.getValue(), loadedEntry.getValue()));
        }
        Assert.assertFalse(reference.hasNext());
        Assert.assertFalse(loaded.hasNext());
    }

    @Test public void testSaveEmptyFrameToFile() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES);
        final FrameBodyETCO referenceBody = new FrameBodyETCO();
        frame.setBody(referenceBody);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_EVENT_TIMING_CODES);
        FrameBodyETCO body = (FrameBodyETCO)frame.getBody();
        Assert.assertEquals(referenceBody.getTimestampFormat(), body.getTimestampFormat());
        Assert.assertEquals(referenceBody.getTimingCodes(), body.getTimingCodes());
    }
}
