package org.jaudiotagger.tag.id3;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1Test;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 *
 */
public class Unicode23TagTest extends TestCase
{
    /**
     * Constructor
     *
     * @param arg0
     */
    public Unicode23TagTest(String arg0)
    {
        super(arg0);
    }

    /**
     * Command line entrance.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(Unicode23TagTest.suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    protected void setUp()
    {
    }

    /**
     *
     */
    protected void tearDown()
    {
    }

    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /**
     * Builds the Test Suite.
     *
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(Unicode23TagTest.class);
    }



    /**
     * Create a String that only contains text within IS8859 charset so should be
     * as ISO_88859
     *
     * @throws Exception
     */
    public void testCreateISO8859EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }

    /**
     * Can explictly uses UTF-16 even if not required
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }


    /**
     * Create a String that contains text outside of the IS8859 charset should be written
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16AutoEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16,body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings can bbe written to UTF16BE if text encoding explicitly set
     *
     * @throws Exception
     */
    public void testCreateUTF16BEEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16BE);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16BE
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16BE, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings can bbe written to UTF8 if text encoding explicitly set
     *
     * @throws Exception
     */
    public void testCreateUTF8EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_8);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF8
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }
}
