package org.jaudiotagger.tag.id3;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWOAF;
import org.jaudiotagger.tag.id3.framebody.FrameBodyWORS;

import java.io.File;

/**
 * Issue #183:Tests that tags containiing empty frames (which are not really allowed by ID3 spec, but do exist) dont
 * prevent subsequent framesthat contain data from being read.
 * Date: 01-Jan-2008
 */
public class EmptyFrameTest extends TestCase
{
    public void testWriteID3v23TagWithEmptyFrameFirst() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;
        mp3File = new MP3File(testFile);
        assertTrue(mp3File.hasID3v2Tag());
        assertEquals(8, mp3File.getID3v2Tag().getFieldCount());
        ID3v23Frame emptyFrame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_URL_FILE_WEB);
        ((FrameBodyWOAF) emptyFrame.getBody()).setUrlLink("");
        ID3v23Frame nonemptyFrame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_URL_OFFICIAL_RADIO);
        ((FrameBodyWORS) nonemptyFrame.getBody()).setUrlLink("something");

        mp3File.getID3v2Tag().setFrame(emptyFrame);
        mp3File.getID3v2Tag().setFrame(nonemptyFrame);
        mp3File.save();
        mp3File = new MP3File(testFile);
        assertTrue(mp3File.hasID3v2Tag());

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag().getFieldCount());
        assertEquals(0, mp3File.getID3v2Tag().getFields("WOAF").size());
        assertEquals(1, mp3File.getID3v2Tag().getFields("WORS").size());
    }

    public void testWriteID3v24TagWithEmptyFrameFirst() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Convert to v24
        mp3File.setID3v2Tag(new ID3v24Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        assertTrue(mp3File.hasID3v2Tag());
        assertEquals(8, mp3File.getID3v2Tag().getFieldCount());

        ID3v24Frame emptyFrame = new ID3v24Frame(ID3v24Frames.FRAME_ID_URL_FILE_WEB);
        ((FrameBodyWOAF) emptyFrame.getBody()).setUrlLink("");
        ID3v24Frame nonemptyFrame = new ID3v24Frame(ID3v24Frames.FRAME_ID_URL_OFFICIAL_RADIO);
        ((FrameBodyWORS) nonemptyFrame.getBody()).setUrlLink("something");

        mp3File.getID3v2Tag().setFrame(emptyFrame);
        mp3File.getID3v2Tag().setFrame(nonemptyFrame);
        mp3File.save();
        mp3File = new MP3File(testFile);
        assertTrue(mp3File.hasID3v2Tag());

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag().getFieldCount());
        assertEquals(0, mp3File.getID3v2Tag().getFields("WOAF").size());
        assertEquals(1, mp3File.getID3v2Tag().getFields("WORS").size());
    }

    public void testWriteID3v22TagWithEmptyFrameFirst() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128ID3v2.mp3");

        MP3File mp3File = null;
        mp3File = new MP3File(testFile);

        //Convert to v24
        mp3File.setID3v2Tag(new ID3v22Tag(mp3File.getID3v2Tag()));
        mp3File.save();

        assertTrue(mp3File.hasID3v2Tag());
        assertEquals(8, mp3File.getID3v2Tag().getFieldCount());

        ID3v22Frame emptyFrame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_URL_FILE_WEB);
        ((FrameBodyWOAF) emptyFrame.getBody()).setUrlLink("");
        ID3v22Frame nonemptyFrame = new ID3v22Frame(ID3v22Frames.FRAME_ID_V2_URL_OFFICIAL_RADIO);
        ((FrameBodyWORS) nonemptyFrame.getBody()).setUrlLink("something");

        mp3File.getID3v2Tag().setFrame(emptyFrame);
        mp3File.getID3v2Tag().setFrame(nonemptyFrame);
        mp3File.save();
        mp3File = new MP3File(testFile);
        assertTrue(mp3File.hasID3v2Tag());

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag().getFieldCount());
        assertEquals(0, mp3File.getID3v2Tag().getFields("WAF").size());
        assertEquals(1, mp3File.getID3v2Tag().getFields("WRS").size());


    }
}
