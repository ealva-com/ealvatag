package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTSOC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTSOCTest;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXXTest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test TXXX  Frame
 */
public class FrameTXXXTest extends AbstractTestCase
{
    public static ID3v24Frame getV24InitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
        FrameBodyTXXX fb = FrameBodyTXXXTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v23Frame getV23InitialisedFrame()
    {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
        FrameBodyTXXX fb = FrameBodyTXXXTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v22Frame getV22InitialisedFrame()
    {
        ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO);
        FrameBodyTXXX fb = FrameBodyTXXXTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        try
        {
            frame = getV24InitialisedFrame();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));

    }


    public void testCreateID3v23Frame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;

        try
        {
            frame = getV23InitialisedFrame();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }

    public void testCreateID3v22Frame()
    {
        Exception exceptionCaught = null;
        ID3v22Frame frame = null;

        try
        {
            frame = getV22InitialisedFrame();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(getV24InitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }



    public void testConvertV24ToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(getV24InitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v23Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, body.getText());
    }

    public void testConvertV24ToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTXXXTest.getV24InitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
          assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, body.getText());
    }

    public void testConvertV23ToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(FrameTXXXTest.getV23InitialisedFrame());

        mp3File.setID3v2TagOnly((ID3v23Tag) tag);
        mp3File.save();

        //Reload and convert from v23 to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, body.getText());
    }


    public void testConvertV22ToV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v22Tag tag = new ID3v22Tag();

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        ID3v22Frame id3v22frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_USER_DEFINED_INFO);
        ((FrameBodyTXXX) id3v22frame.getBody()).setText(FrameBodyTXXXTest.TXXX_TEST_STRING);
        tag.setFrame(id3v22frame);

        mp3File.setID3v2TagOnly((ID3v22Tag) tag);
        mp3File.save();

        //Reload and convert from v22 to v24 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v24Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_INFO);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_STRING, body.getText());
    }
}