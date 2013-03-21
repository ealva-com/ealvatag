package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDAT;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTDRC;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTYER;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

/**
 * Test
 */
public class Issue461Test extends AbstractTestCase
{
    public void testV23DateConversionFromGeneric() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        v2Tag.setField(FieldKey.YEAR,"2004-01-30");
        assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        Set<AbstractID3v2Frame> frames =  frame.getFrames();
        Iterator<AbstractID3v2Frame> i = frames.iterator();
        FrameBodyTYER fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("3001", fbTdat.getText());
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag=(ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004-01-30", v2Tag.getFirst(FieldKey.YEAR));
        assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        frames =  frame.getFrames();
        i = frames.iterator();
        fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("3001", fbTdat.getText());
    }

    public void testV23DateConversionFromV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR,"2004-01-30");
        assertEquals("2004-01-30", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        Set<AbstractID3v2Frame> frames =  frame.getFrames();
        Iterator<AbstractID3v2Frame>   i = frames.iterator();
        FrameBodyTYER      fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT      fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("3001", fbTdat.getText());
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag=(ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004-01-30", v2Tag.getFirst(FieldKey.YEAR));
        assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        frames =  frame.getFrames();
        i = frames.iterator();
        fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("3001", fbTdat.getText());
    }

    public void testV23DateConversionFromV24YearOnly() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR,"2004");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC fbTdrc    = (FrameBodyTDRC)v24Frame.getBody();
        assertEquals("2004", fbTdrc.getText());
        assertEquals("2004", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        AbstractID3v2Frame frame = ((AbstractID3v2Frame)v2Tag.getFrame("TYER"));
        assertNotNull(frame);
        FrameBodyTYER fbTyer    = ((FrameBodyTYER)frame.getBody());
        assertEquals("2004", fbTyer.getText());
    }

    public void testV23DateConversionFromV24YearOnly2() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag   v24Tag   = new ID3v24Tag();
        ID3v24Frame v24Frame = new ID3v24Frame("TDRC");
        FrameBodyTDRC fbTdrc = new FrameBodyTDRC((byte)1,"2004");
        v24Frame.setBody(fbTdrc);
        v24Tag.addFrame(v24Frame);
        v24Frame = (ID3v24Frame)v24Tag.getFrame("TDRC");
        fbTdrc    = (FrameBodyTDRC)v24Frame.getBody();
        assertEquals("2004", fbTdrc.getText());
        assertEquals("2004", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
    }

    public void testV23DayMonthYearConversionFromV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR,"2004-06-30");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC      fbTdrc    = (FrameBodyTDRC)v24Frame.getBody();
        assertEquals("2004-06-30", fbTdrc.getText());
        assertEquals("2004-06-30", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        //TODO shoudnt this be 2004-06-30 it is after save
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2TagOnly(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004-06-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        //v24Tag = (ID3v24Tag)mp3File.getID3v2TagAsv24();
        v24Tag = new ID3v24Tag(v2Tag);
        Iterator<AbstractID3v2Frame>   i = v24Tag.iterator();

        assertEquals("2004-06-30", v24Tag.getFirst(FieldKey.YEAR));
        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        i = frame.getFrames().iterator();
        FrameBodyTYER      fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT      fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("3006", fbTdat.getText());
    }

    public void testV23MonthYearConversionFromV24() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR,"2004-06");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC      fbTdrc    = (FrameBodyTDRC)v24Frame.getBody();
        assertEquals("2004-06", fbTdrc.getText());
        assertEquals("2004-06", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);

        //TODO shoudnt this be 2004-06-01 it is after save
        assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.save();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        assertEquals("2004-06-01", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        assertNotNull(frame);
        Set<AbstractID3v2Frame> frames =  frame.getFrames();
        Iterator<AbstractID3v2Frame>   i = frames.iterator();
        FrameBodyTYER      fbTyer    = ((FrameBodyTYER)i.next().getBody());
        assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT      fbTdat    = ((FrameBodyTDAT)i.next().getBody());
        assertNotNull(fbTdat);
        assertEquals("0106", fbTdat.getText());
    }

}
