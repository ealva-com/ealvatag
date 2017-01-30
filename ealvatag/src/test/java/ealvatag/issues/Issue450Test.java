package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Frames;
import ealvatag.tag.id3.framebody.FrameBodyWOAR;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class Issue450Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testSaveUrl() throws Exception {
        File orig = new File("testdata", "test108.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }
        File testFile = TestUtil.copyAudioToTmp("test108.mp3");
        MP3File mp3file = (MP3File)AudioFileIO.read(testFile);
        AbstractID3v2Frame frame = (AbstractID3v2Frame)mp3file.getID3v2TagAsv24().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        Assert.assertNotNull(frame);

        frame = (AbstractID3v2Frame)mp3file.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        Assert.assertNotNull(frame);
        Assert.assertEquals(FrameBodyWOAR.class, frame.getBody().getClass());
        FrameBodyWOAR fb = (FrameBodyWOAR)frame.getBody();
        System.out.println(fb.getUrlLink());

        mp3file.setID3v2Tag(new ID3v23Tag(mp3file.getID3v2TagAsv24()));
        frame = (AbstractID3v2Frame)mp3file.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        Assert.assertNotNull(frame);
        Assert.assertEquals(FrameBodyWOAR.class, frame.getBody().getClass());
        fb = (FrameBodyWOAR)frame.getBody();
        System.out.println(fb.getUrlLink());
        mp3file.save();


    }

}
