package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPOPM;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPOPMTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test POPMFrameBody
 */
public class FramePOPMTest extends AbstractTestCase
{
    private static final String ISSUE_72_TEST_EMAIL = "Windows Media Player 9 Series";
    private static final int ISSUE_72_TEST_RATING = 255;
    private static final int ISSUE_72_TEST_COUNTER = 0;


    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_POPULARIMETER);
        FrameBodyPOPM fb = FrameBodyPOPMTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyPOPM fb = null;
        try
        {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_POPULARIMETER);
            fb = FrameBodyPOPMTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_POPULARIMETER, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, ((FrameBodyPOPM) frame.getBody()).getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, ((FrameBodyPOPM) frame.getBody()).getRating());
        assertEquals(FrameBodyPOPMTest.POPM_COUNTER, ((FrameBodyPOPM) frame.getBody()).getCounter());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }


    public void testCreateID3v23Frame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyPOPM fb = null;
        try
        {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_POPULARIMETER);
            fb = FrameBodyPOPMTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_POPULARIMETER, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, ((FrameBodyPOPM) frame.getBody()).getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, ((FrameBodyPOPM) frame.getBody()).getRating());
        assertEquals(FrameBodyPOPMTest.POPM_COUNTER, ((FrameBodyPOPM) frame.getBody()).getCounter());
    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePOPMTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_POPULARIMETER);
        FrameBodyPOPM body = (FrameBodyPOPM) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyPOPMTest.POPM_EMAIL, body.getEmailToUser());
        assertEquals(FrameBodyPOPMTest.POPM_RATING, body.getRating());
        assertEquals(FrameBodyPOPMTest.POPM_COUNTER, body.getCounter());
    }

    public void testSaveEmptyFrameToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_POPULARIMETER);
        frame.setBody(new FrameBodyPOPM());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_POPULARIMETER);
        FrameBodyPOPM body = (FrameBodyPOPM) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals("", body.getEmailToUser());
        assertEquals(0, body.getRating());
        assertEquals(0, body.getCounter());
    }

    public void testReadFileContainingPOMFrameWithoutCounter() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue72.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_POPULARIMETER);
        FrameBodyPOPM body = (FrameBodyPOPM) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(ISSUE_72_TEST_EMAIL, body.getEmailToUser());
        assertEquals(ISSUE_72_TEST_RATING, body.getRating());
        assertEquals(ISSUE_72_TEST_COUNTER, body.getCounter());
    }


}
