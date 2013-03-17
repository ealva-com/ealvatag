package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWOAR;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;
import java.net.URLEncoder;

/**
 * Test WOAR Frame
 */
public class FrameWOARTest extends AbstractTestCase
{
    public static final String NORMAL_LINK = "http:www.btinternet.com/~birdpoo/kots.htm";

    //Note cant put Japanese chars directly into code because the source code is not a UTF8 file
    public static final String UNICODE_LINK_START = "http://ja.wikipedia.org/wiki/";
    public static final String UNICODE_LINK_END = "\u5742\u672c\u4e5d";
    public static final String UNICODE_ENCODED = "http://ja.wikipedia.org/wiki/%E5%9D%82%E6%9C%AC%E4%B9%9D";
    public static final String UNICODE_LINK = "http://ja.wikipedia.org/wiki/\u5742\u672c\u4e5d";


    //http://ja.wikipedia.org/wiki/%E5%9D%82%E6%9C%AC%E4%B9%9D
    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        FrameBodyWOAR fb = new FrameBodyWOAR();
        fb.setUrlLink(NORMAL_LINK);
        frame.setBody(fb);
        return frame;
    }

    public static ID3v24Frame getInitialisedUnicodeFrame() throws Exception
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        FrameBodyWOAR fb = new FrameBodyWOAR();
        fb.setUrlLink(UNICODE_LINK_START + URLEncoder.encode(UNICODE_LINK_END, "utf8"));
        //fb.setUrlLink(URLEncoder.encode(UNICODE_LINK_START+UNICODE_LINK_END,"utf8"));

        frame.setBody(fb);
        return frame;
    }

    public static ID3v24Frame getRawUnicodeFrame() throws Exception
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        FrameBodyWOAR fb = new FrameBodyWOAR();
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
        assertTrue(frame.getBody() instanceof FrameBodyWOAR);
        assertEquals(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameWOARTest.NORMAL_LINK, ((FrameBodyWOAR) frame.getBody()).getUrlLink());
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
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertTrue(frame.getBody() instanceof FrameBodyWOAR);
        assertEquals(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameWOARTest.NORMAL_LINK, ((FrameBodyWOAR) frame.getBody()).getUrlLink());
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
        assertTrue(frame.getBody() instanceof FrameBodyWOAR);
        assertEquals(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameWOARTest.UNICODE_ENCODED, ((FrameBodyWOAR) frame.getBody()).getUrlLink());
    }

    //This fails beccause cant save Unicode to WOAR fields
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
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertTrue(frame.getBody() instanceof FrameBodyWOAR);
        assertEquals(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameWOARTest.UNICODE_ENCODED, ((FrameBodyWOAR) frame.getBody()).getUrlLink());
    }

    //This fails beccause cant save Unicode to WOAR fields
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
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB);
        assertTrue(frame.getBody() instanceof FrameBodyWOAR);
        assertEquals(ID3v24Frames.FRAME_ID_URL_ARTIST_WEB, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, frame.getBody().getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameWOARTest.UNICODE_ENCODED, ((FrameBodyWOAR) frame.getBody()).getUrlLink());
    }
}
