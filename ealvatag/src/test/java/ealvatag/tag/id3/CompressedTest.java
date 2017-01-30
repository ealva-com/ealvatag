package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.framebody.FrameBodyAPIC;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import ealvatag.tag.id3.framebody.FrameBodyTIT2;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Testing of reading compressed frames
 */
public class CompressedTest {
    /**
     * This tests reading a v23tag that contains a compressed COMM frame
     *
     * @throws Exception
     */
    @Test public void testv23TagReadCompressedCommentFrame() throws Exception {
        final String COMM_TEXT = "[P-M-S] Teampms [P-M-S]";

        File testFile = TestUtil.copyAudioToTmp("Issue98-1.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        Assert.assertTrue(v23tag.hasFrame(ID3v23Frames.FRAME_ID_V3_COMMENT));

        ID3v23Frame frame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_COMMENT);
        Assert.assertTrue(((ID3v23Frame.EncodingFlags)frame.getEncodingFlags()).isCompression());
        FrameBodyCOMM frameBody = (FrameBodyCOMM)frame.getBody();
        Assert.assertEquals(COMM_TEXT, frameBody.getText());
        Assert.assertEquals("", frameBody.getDescription());


    }

    /**
     * This tests reading a v23 tag that contains a compressed APIC frame
     *
     * @throws Exception
     */
    @Test public void testv23TagReadCompressedAPICFrame() throws Exception {
        final int FRAME_SIZE = 3220;
        final String TITLE_TEXT = "Crazy Train";
        File testFile = TestUtil.copyAudioToTmp("Issue98-2.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        Assert.assertTrue(v23tag.hasFrame(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE));

        ID3v23Frame frame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ATTACHED_PICTURE);
        Assert.assertTrue(((ID3v23Frame.EncodingFlags)frame.getEncodingFlags()).isCompression());
        FrameBodyAPIC frameBody = (FrameBodyAPIC)frame.getBody();
        Assert.assertEquals("", frameBody.getDescription());
        Assert.assertEquals(FRAME_SIZE, frameBody.getSize());

        //Check got to end of frame
        Assert.assertTrue(v23tag.hasFrame(ID3v23Frames.FRAME_ID_V3_TITLE));
        frame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_TITLE);
        FrameBodyTIT2 frameBodyTitle = (FrameBodyTIT2)frame.getBody();
        Assert.assertEquals(TITLE_TEXT, frameBodyTitle.getText());
    }

    /**
     * This tests reading a v24tag that contains a compressed Picture frame
     *
     * @throws Exception
     */
    @Test public void testv24TagReadCompressedPictureFrame() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue98-3.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag)mp3File.getID3v2Tag();

        Assert.assertTrue(v24tag.hasFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE));
        ID3v24Frame frame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ATTACHED_PICTURE);
        Assert.assertTrue(((ID3v24Frame.EncodingFlags)frame.getEncodingFlags()).isCompression());
        Assert.assertEquals(27, v24tag.getFieldCount());
    }

}
