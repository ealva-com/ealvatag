package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTSOA;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTSOATest;
import org.jaudiotagger.tag.id3.framebody.FrameBodyXSOA;
import org.jaudiotagger.tag.id3.framebody.FrameBodyXSOATest;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;

/**
 * Test TSOA and XSOA (Title Sort) Frame
 */
public class FrameTSOATest extends AbstractTestCase
{
    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
        FrameBodyTSOA fb = FrameBodyTSOATest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v23Frame getV23InitialisedFrame()
    {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ);
        FrameBodyXSOA fb = FrameBodyXSOATest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyTSOA fb = null;
        try
        {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
            fb = FrameBodyTSOATest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }


    public void testCreateID3v23ITunesFrame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyTSOA fb = null;
        try
        {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_ITUNES);
            fb = FrameBodyTSOATest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_ITUNES, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertTrue(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertFalse(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }

    public void testCreateID3v23MusicBrainzFrame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyXSOA fb = null;
        try
        {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ);
            fb = FrameBodyXSOATest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_MUSICBRAINZ, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertTrue(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertFalse(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }

    public void testCreateID3v22Frame()
    {
        Exception exceptionCaught = null;
        ID3v22Frame frame = null;
        FrameBodyTSOA fb = null;
        try
        {
            frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES);
            fb = FrameBodyTSOATest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertTrue(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertFalse(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, fb.getText());

    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTSOATest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    public void testSaveEmptyFrameToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
        frame.setBody(new FrameBodyTSOA());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    public void testConvertV24ToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTSOATest.getInitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v23Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ALBUM_SORT_ORDER_ITUNES);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, body.getText());
    }

    public void testConvertV24ToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTSOATest.getInitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, body.getText());
    }

    public void testConvertV23ITunesToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(FrameTSOATest.getInitialisedFrame());

        mp3File.setID3v2TagOnly((ID3v23Tag) tag);
        mp3File.save();

        //Reload and convert from v23 to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, body.getText());
    }

    public void testConvertV23MusicBrainzToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(FrameTSOATest.getV23InitialisedFrame());

        mp3File.setID3v2TagOnly((ID3v23Tag) tag);
        mp3File.save();

        //Reload and convert from v23 to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload will be converted to same TST version for v22
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, body.getText());
    }

    public void testConvertV22ToV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v22Tag tag = new ID3v22Tag();

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        ID3v22Frame id3v22frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ALBUM_SORT_ORDER_ITUNES);
        ((FrameBodyTSOA) id3v22frame.getBody()).setText(FrameBodyTSOATest.ALBUM_SORT);
        tag.setFrame(id3v22frame);

        mp3File.setID3v2TagOnly((ID3v22Tag) tag);
        mp3File.save();

        //Reload and convert from v22 to v24 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v24Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ALBUM_SORT_ORDER);
        FrameBodyTSOA body = (FrameBodyTSOA) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTSOATest.ALBUM_SORT, body.getText());
    }
}
