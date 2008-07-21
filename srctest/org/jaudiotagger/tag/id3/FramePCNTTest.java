package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPCNT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPCNTTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test PCNTFrameBody
 */
public class FramePCNTTest extends AbstractTestCase
{
    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT fb = FrameBodyPCNTTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyPCNT fb = null;
        try
        {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
            fb = FrameBodyPCNTTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_PLAY_COUNTER, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, ((FrameBodyPCNT) frame.getBody()).getCounter());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }


    public void testCreateID3v23Frame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyPCNT fb = null;
        try
        {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER);
            fb = FrameBodyPCNTTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_PLAY_COUNTER, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, ((FrameBodyPCNT) frame.getBody()).getCounter());
    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FramePCNTTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT body = (FrameBodyPCNT) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, body.getCounter());
    }

    public void testSaveEmptyFrameToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        frame.setBody(new FrameBodyPCNT());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_PLAY_COUNTER);
        FrameBodyPCNT body = (FrameBodyPCNT) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(0, body.getCounter());
    }
}
