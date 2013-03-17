package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWXXX;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Test WXXX Frame
 */
public class FrameWXXXTest extends AbstractTestCase
{
    public static final String NORMAL_LINK = "http:www.btinternet.com/~birdpoo/kots.htm";

    //Note cant put Japanese chars directly into code because the source code is not a UTF8 file
    public static final String UNICODE_LINK_START = "http://ja.wikipedia.org/wiki/";
    public static final String UNICODE_LINK_END = "\u5742\u672c\u4e5d";
    public static final String UNICODE_ENCODED = "http://ja.wikipedia.org/wiki/%E5%9D%82%E6%9C%AC%E4%B9%9D";
    public static final String UNICODE_LINK = "http://ja.wikipedia.org/wiki/\u5742\u672c\u4e5d";


     /**
      *
      */
     protected void tearDown()
     {
     }

    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX fb = new FrameBodyWXXX();
        fb.setUrlLink(NORMAL_LINK);
        frame.setBody(fb);
        return frame;
    }

    public static ID3v24Frame getInitialisedUnicodeFrame() throws Exception
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX fb = new FrameBodyWXXX();
        fb.setUrlLink(UNICODE_LINK_START + URLEncoder.encode(UNICODE_LINK_END, "utf8"));
        frame.setBody(fb);
        return frame;
    }

    public static ID3v24Frame getRawUnicodeFrame() throws Exception
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX fb = new FrameBodyWXXX();
        fb.setUrlLink(UNICODE_LINK);
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        try
        {
            frame = getInitialisedFrame();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertTrue(frame.getBody() instanceof FrameBodyWXXX);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(NORMAL_LINK, ((FrameBodyWXXX) frame.getBody()).getUrlLink());
    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        assertTrue(frame.getBody() instanceof FrameBodyWXXX);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(NORMAL_LINK, ((FrameBodyWXXX) frame.getBody()).getUrlLink());
    }

    public void testCreateID3v24UnicodeFrame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        try
        {
            frame = getInitialisedUnicodeFrame();
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertTrue(frame.getBody() instanceof FrameBodyWXXX);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(UNICODE_ENCODED, ((FrameBodyWXXX) frame.getBody()).getUrlLink());
    }

    /**
     * Encoding already done
     *
     * @throws Exception
     */
    public void testSaveUnicodeToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(getInitialisedUnicodeFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        assertTrue(frame.getBody() instanceof FrameBodyWXXX);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(UNICODE_ENCODED, ((FrameBodyWXXX) frame.getBody()).getUrlLink());
    }

    /**
     * Encoding done by jaudiotagger itself
     *
     * @throws Exception
     */
    public void testSaveUnicodeToFile2() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(getRawUnicodeFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        assertTrue(frame.getBody() instanceof FrameBodyWXXX);
        assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(UNICODE_ENCODED, ((FrameBodyWXXX) frame.getBody()).getUrlLink());
    }


    public void testEncodeURL() throws UnsupportedEncodingException
    {
        String url = UNICODE_LINK;
        final String[] splitURL = url.split("(?<!/)/(?!/)", -1);
        final StringBuffer sb = new StringBuffer(splitURL[0]);
        for (int i = 1; i < splitURL.length; i++)
        {
            sb.append("/").append(URLEncoder.encode(splitURL[i], "utf-8"));
        }
        assertEquals(UNICODE_ENCODED, sb.toString());
    }

}
