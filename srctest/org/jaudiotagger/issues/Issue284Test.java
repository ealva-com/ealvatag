package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIPL;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Converting FrameBodyUnsupported with known identifier to FrameBodyIPLS (v23) causing NoSuchMethodException.
 * Not really sure why this is happening but we should check and take action instead of failing as we currently do  
 */
public class Issue284Test extends AbstractTestCase
{
    public void testConvertv23v24()
    {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
            frame.setBody(fb);
            assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
            ID3v23Frame framev23 = new ID3v23Frame(frame);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testConvertv22v24()
    {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v24Frames.FRAME_ID_INVOLVED_PEOPLE);
            frame.setBody(fb);
            assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
            ID3v22Frame framev22 = new ID3v22Frame(frame);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testConvertv24v23()
    {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_IPLS);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v23Frames.FRAME_ID_V3_IPLS);
            frame.setBody(fb);
            assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
            ID3v24Frame framev24 = new ID3v24Frame(frame);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testConvertv24v22()
    {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_TITLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v22Frames.FRAME_ID_V2_TITLE);
            frame.setBody(fb);
            assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
            ID3v24Frame framev24 = new ID3v24Frame(frame);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }

    public void testConvertv22v23()
    {
        File orig = new File("testdata", "testV1.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
            MP3File af = (MP3File)AudioFileIO.read(testFile);

            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_TITLE);
            FrameBodyUnsupported fb = new FrameBodyUnsupported(ID3v23Frames.FRAME_ID_V3_TITLE);
            frame.setBody(fb);
            assertTrue(frame.getBody() instanceof FrameBodyUnsupported);
            ID3v22Frame framev22 = new ID3v22Frame(frame);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
    }
}
