package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Test TSOP and XSOP (Title Sort) Frame
 */
public class FrameTMOOTest extends AbstractTestCase
{
    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_MOOD);
        FrameBodyTMOO fb = FrameBodyTMOOTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    /*public static ID3v23Frame getV23InitialisedFrame()
    {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
        FrameBodyXSOP fb = FrameBodyXSOPTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    } */

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyTMOO fb = null;
        try
        {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_MOOD);
            fb = FrameBodyTMOOTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_MOOD, frame.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyTMOOTest.MOOD, fb.getText());

    }


    /*
public void testCreateID3v23Frame()
{
Exception exceptionCaught = null;
ID3v23Frame frame = null;
FrameBodyXSOP fb = null;
try
{
frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ);
fb = FrameBodyXSOPTest.getInitialisedBody();
frame.setBody(fb);
}
catch (Exception e)
{
exceptionCaught = e;
}

assertNull(exceptionCaught);
assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST_SORT_ORDER_MUSICBRAINZ, frame.getIdentifier());
assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
assertTrue(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
assertFalse(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());

}                             */
    /*
public void testCreateID3v22Frame()
{
  Exception exceptionCaught = null;
  ID3v22Frame frame = null;
  FrameBodyTSOP fb = null;
  try
  {
      frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_ARTIST_SORT_ORDER_ITUNES);
      fb = FrameBodyTSOPTest.getInitialisedBody();
      frame.setBody(fb);
  }
  catch (Exception e)
  {
      exceptionCaught = e;
  }

  assertNull(exceptionCaught);
  assertEquals(ID3v22Frames.FRAME_ID_V2_ARTIST_SORT_ORDER_ITUNES, frame.getIdentifier());
  assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
  assertTrue(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
  assertFalse(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
  assertEquals(FrameBodyTSOPTest.ARTIST_SORT, fb.getText());

}
    */
    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1016.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTMOOTest.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_MOOD);
        FrameBodyTMOO body = (FrameBodyTMOO) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    public void testSaveEmptyFrameToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1004.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_MOOD);
        frame.setBody(new FrameBodyTMOO());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_MOOD);
        FrameBodyTMOO body = (FrameBodyTMOO) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    /**
     * Testing not only conversion of MOOD but also what hapens when have two frame of different types (TMOO and TXXX) that
     * become the same type TXXX
     *
     * @throws Exception
     */
    public void testConvertV24ToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1005.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameTMOOTest.getInitialisedFrame());
        tag.setFrame(FrameTXXXTest.getV24InitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        ID3v23Tag v23Tag = new ID3v23Tag(mp3File.getID3v2TagAsv24());
        assertEquals(2, v23Tag.getFields("TXXX").size());
        mp3File.setID3v2TagOnly(v23Tag);

        Iterator i = v23Tag.getFields();
        while (i.hasNext())
        {
            System.out.println(((ID3v23Frame) i.next()).getIdentifier());
        }
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame) ((List) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO)).get(0);
        FrameBodyTXXX body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTXXXTest.TXXX_TEST_DESC, body.getText());

        frame = (ID3v23Frame) ((List) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO)).get(1);
        body = (FrameBodyTXXX) frame.getBody();
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTMOOTest.MOOD, body.getText());
    }

    /**
     * Testing not only conversion of MOOD but also what happens when have two frame of different types (TMOO and TXXX) that
     * become the same type TXXX
     *
     * @throws Exception
     */
    public void testConvertV23ToV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1005.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        FrameBodyTXXX frameBody = new FrameBodyTXXX();
        frameBody.setDescription(FrameBodyTXXX.MOOD);
        frameBody.setText("Tranquil");
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
        frame.setBody(frameBody);
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag(mp3File.getID3v2Tag());
        Iterator i = v24Tag.getFields();
        while (i.hasNext())
        {
            System.out.println("kkk" + ((ID3v24Frame) i.next()).getIdentifier());
        }
        assertEquals(1, v24Tag.getFieldCount());
        ID3v24Frame v24frame = (ID3v24Frame)v24Tag.getFrame("TMOO");
        assertTrue(v24frame.getBody() instanceof FrameBodyTMOO); 
        FrameBodyTMOO v24framebody = (FrameBodyTMOO)v24frame.getBody();
        assertEquals("Tranquil",v24framebody.getText());
    }

      public void testConvertMultiV23ToV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1005.mp3"));
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        ArrayList frames= new ArrayList();
        {
            FrameBodyTXXX frameBody = new FrameBodyTXXX();
            frameBody.setDescription(FrameBodyTXXX.MOOD);
            frameBody.setText("Tranquil");
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
            frame.setBody(frameBody);
            frames.add(frame);
        }
        {
            FrameBodyTXXX frameBody = new FrameBodyTXXX();
            frameBody.setDescription(FrameBodyTXXX.BARCODE);
            frameBody.setText("0123456789");
            ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_USER_DEFINED_INFO);
            frame.setBody(frameBody);
            frames.add(frame);
        }
        tag.setFrame("TXXX",frames);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag(mp3File.getID3v2Tag());
        Iterator i = v24Tag.getFields();
        while (i.hasNext())
        {
            System.out.println("kkk" + ((ID3v24Frame) i.next()).getIdentifier());
        }
        assertEquals(2, v24Tag.getFieldCount());
        ID3v24Frame v24frame = (ID3v24Frame)v24Tag.getFrame("TMOO");
        assertNotNull(v24frame);
        assertTrue(v24frame.getBody() instanceof FrameBodyTMOO);
        FrameBodyTMOO v24framebody = (FrameBodyTMOO)v24frame.getBody();
        assertEquals("Tranquil",v24framebody.getText());
    }
}