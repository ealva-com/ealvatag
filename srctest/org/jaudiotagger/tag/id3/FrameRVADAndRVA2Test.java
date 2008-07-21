package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.framebody.FrameBodyRVA2;
import org.jaudiotagger.tag.id3.framebody.FrameBodyRVA2Test;
import org.jaudiotagger.tag.id3.framebody.FrameBodyRVAD;
import org.jaudiotagger.tag.id3.framebody.FrameBodyRVADTest;

import java.io.File;

/**
 * Test RVAD (v23) and RVA2 (V24) frames
 */
public class FrameRVADAndRVA2Test extends AbstractTestCase
{
    public static String cmp(byte[] a, byte[] b)
    {
        if (a.length != b.length)
        {
            return "length of byte arrays differ (" + a.length + "!=" + b.length + ")";
        }
        for (int i = 0; i < a.length; i++)
        {
            if (a[i] != b[i])
            {
                return "byte arrays differ at offset " + i + " (" + a[i] + "!=" + b[i] + ")";
            }
        }
        return null;
    }

    public static ID3v24Frame getInitialisedFrame()
    {
        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2);
        FrameBodyRVA2 fb = FrameBodyRVA2Test.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v23Frame getV23InitialisedFrame()
    {
        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT);
        FrameBodyRVAD fb = FrameBodyRVADTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public static ID3v22Frame getV22InitialisedFrame()
    {
        ID3v22Frame frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_RELATIVE_VOLUME_ADJUSTMENT);
        FrameBodyRVAD fb = FrameBodyRVADTest.getInitialisedBody();
        frame.setBody(fb);
        return frame;
    }

    public void testCreateID3v24Frame()
    {
        Exception exceptionCaught = null;
        ID3v24Frame frame = null;
        FrameBodyRVA2 fb = null;
        try
        {
            frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2);
            fb = FrameBodyRVA2Test.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2, frame.getIdentifier());
        assertFalse(ID3v24Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v24Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyRVA2Test.TEST_BYTES, frame.getBody().getObjectValue(DataTypes.OBJ_DATA));
    }


    public void testCreateID3v23Frame()
    {
        Exception exceptionCaught = null;
        ID3v23Frame frame = null;
        FrameBodyRVAD fb = null;
        try
        {
            frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT);
            fb = FrameBodyRVADTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT, frame.getIdentifier());
        assertFalse(ID3v23Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v23Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyRVADTest.TEST_BYTES, frame.getBody().getObjectValue(DataTypes.OBJ_DATA));

    }


    public void testCreateID3v22Frame()
    {
        Exception exceptionCaught = null;
        ID3v22Frame frame = null;
        FrameBodyRVAD fb = null;
        try
        {
            frame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_RELATIVE_VOLUME_ADJUSTMENT);
            fb = FrameBodyRVADTest.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v22Frames.FRAME_ID_V2_RELATIVE_VOLUME_ADJUSTMENT, frame.getIdentifier());
        assertFalse(ID3v22Frames.getInstanceOf().isExtensionFrames(frame.getIdentifier()));
        assertTrue(ID3v22Frames.getInstanceOf().isSupportedFrames(frame.getIdentifier()));
        assertEquals(FrameBodyRVADTest.TEST_BYTES, frame.getBody().getObjectValue(DataTypes.OBJ_DATA));

    }

    public void testSaveToFile() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameRVADAndRVA2Test.getInitialisedFrame());
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2);
        assertTrue(frame != null);
        FrameBodyRVA2 body = (FrameBodyRVA2) frame.getBody();
        assertTrue(body instanceof FrameBodyRVA2);
        assertTrue(cmp(FrameBodyRVA2Test.TEST_BYTES, (byte[]) body.getObjectValue(DataTypes.OBJ_DATA)) == null);

    }


    public void testConvertV24ToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameRVADAndRVA2Test.getInitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v23 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v23Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v23Frame frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_RELATIVE_VOLUME_ADJUSTMENT);
        assertTrue(frame != null);
        FrameBodyRVAD body = (FrameBodyRVAD) frame.getBody();
        assertTrue(body instanceof FrameBodyRVAD);
        assertTrue(cmp(FrameBodyRVA2Test.TEST_BYTES, (byte[]) body.getObjectValue(DataTypes.OBJ_DATA)) == null);

    }

    public void testConvertV24ToV22() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(FrameRVADAndRVA2Test.getInitialisedFrame());

        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload and convert to v22 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v22Tag(mp3File.getID3v2TagAsv24()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v22Frame frame = (ID3v22Frame) mp3File.getID3v2Tag().getFrame(ID3v22Frames.FRAME_ID_V2_RELATIVE_VOLUME_ADJUSTMENT);
        assertTrue(frame != null);
        FrameBodyRVAD body = (FrameBodyRVAD) frame.getBody();
        assertTrue(body instanceof FrameBodyRVAD);
    }


    public void testConvertV22ToV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        //Create and Save
        ID3v22Tag tag = new ID3v22Tag();

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        tag.setFrame(getV22InitialisedFrame());

        mp3File.setID3v2TagOnly((ID3v22Tag) tag);
        mp3File.save();

        //Reload and convert from v22 to v24 and save
        mp3File = new MP3File(testFile);
        mp3File.setID3v2TagOnly(new ID3v24Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        //Reload
        mp3File = new MP3File(testFile);
        ID3v24Frame frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_RELATIVE_VOLUME_ADJUSTMENT2);
        assertTrue(frame != null);
        FrameBodyRVA2 body = (FrameBodyRVA2) frame.getBody();
        assertTrue(body instanceof FrameBodyRVA2);
        assertTrue(cmp(FrameBodyRVADTest.TEST_BYTES, (byte[]) body.getObjectValue(DataTypes.OBJ_DATA)) == null);

    }
}
