package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.framebody.FrameBodyDeprecated;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDAT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTIME;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTYER;

import java.io.File;
import java.util.List;

/**
 * Test DeprecatedFrames
 */
public class DeprecatedFrameTest extends AbstractTestCase
{
    public void testv24TagWithDeprecatedFrameShouldCreateAsDeprecated() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue88.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame v24frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        assertNotNull(v24frame);
        assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);
    }

    public void testConvertTagWithDeprecatedFrameToTagWhereFrameShouldNoLongerBeDeprecated() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue88.id3", "testV1.mp3");

        MP3File mp3File = new MP3File(testFile);

        ID3v23Tag v23Tag = new ID3v23Tag(mp3File.getID3v2Tag());
        ID3v23Frame v23frame = (ID3v23Frame) ((List)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).get(0);
        assertTrue(v23frame.getBody() instanceof FrameBodyTYER);
        v23frame = (ID3v23Frame) ((List)v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER)).get(1);
        assertTrue(v23frame.getBody() instanceof FrameBodyTYER);

        mp3File.setID3v2Tag(v23Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
        v23frame = (ID3v23Frame) v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER);
        assertTrue(v23frame.getBody() instanceof FrameBodyTYER);
    }

    public void testSavingV24DeprecatedTIMETagToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue122-1.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = (ID3v24Tag) mp3File.getID3v2Tag();
        ID3v24Frame v24frame = (ID3v24Frame) v24Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TIME);
        assertNotNull(v24frame);
        assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);

        //Save as V23
        ID3v23Tag v23Tag = new ID3v23Tag((AbstractID3v2Tag) v24Tag);
        mp3File.setID3v2Tag(v23Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
        ID3v23Frame v23frame = (ID3v23Frame) v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TIME);
        assertTrue(v23frame.getBody() instanceof FrameBodyTIME);
    }

    public void testSavingV24DeprecatedTDATTagToV23() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue122-2.id3", "testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = (ID3v24Tag) mp3File.getID3v2Tag();
        ID3v24Frame v24frame = (ID3v24Frame) v24Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TDAT);
        assertNotNull(v24frame);
        assertTrue(v24frame.getBody() instanceof FrameBodyDeprecated);

        //Save as V23
        ID3v23Tag v23Tag = new ID3v23Tag((AbstractID3v2Tag) v24Tag);
        mp3File.setID3v2Tag(v23Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v23Tag = (ID3v23Tag) mp3File.getID3v2Tag();
        Object v23frame = v23Tag.getFrame(ID3v23Frames.FRAME_ID_V3_TYER+ID3v23Frames.FRAME_ID_V3_TDAT);
        assertNotNull(v23frame);
        assertTrue(v23frame instanceof TyerTdatAggregatedFrame);
    }


}
