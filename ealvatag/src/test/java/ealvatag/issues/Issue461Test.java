package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23FieldKey;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24FieldKey;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.TyerTdatAggregatedFrame;
import ealvatag.tag.id3.framebody.FrameBodyTDAT;
import ealvatag.tag.id3.framebody.FrameBodyTDRC;
import ealvatag.tag.id3.framebody.FrameBodyTYER;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

/**
 * Test
 */
public class Issue461Test {
    @Test public void testV23DateConversionFromGeneric() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23DateConversionFromGeneric.mp3"));
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v2Tag = new ID3v23Tag();
        v2Tag.setField(FieldKey.YEAR, "2004-01-30");
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        Set<AbstractID3v2Frame> frames = frame.getFrames();
        Iterator<AbstractID3v2Frame> i = frames.iterator();
        FrameBodyTYER fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("3001", fbTdat.getText());
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        frames = frame.getFrames();
        i = frames.iterator();
        fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("3001", fbTdat.getText());
    }

    @Test public void testV23DateConversionFromV24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23DateConversionFromV24.mp3"));
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR, "2004-01-30");
        Assert.assertEquals("2004-01-30", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        Set<AbstractID3v2Frame> frames = frame.getFrames();
        Iterator<AbstractID3v2Frame> i = frames.iterator();
        FrameBodyTYER fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("3001", fbTdat.getText());
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(FieldKey.YEAR));
        Assert.assertEquals("2004-01-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        frames = frame.getFrames();
        i = frames.iterator();
        fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("3001", fbTdat.getText());
    }

    @Test public void testV23DateConversionFromV24YearOnly() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23DateConversionFromV24YearOnly.mp3"));
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR, "2004");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC fbTdrc = (FrameBodyTDRC)v24Frame.getBody();
        Assert.assertEquals("2004", fbTdrc.getText());
        Assert.assertEquals("2004", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        AbstractID3v2Frame frame = ((AbstractID3v2Frame)v2Tag.getFrame("TYER"));
        Assert.assertNotNull(frame);
        FrameBodyTYER fbTyer = ((FrameBodyTYER)frame.getBody());
        Assert.assertEquals("2004", fbTyer.getText());
    }

    @Test public void testV23DateConversionFromV24YearOnly2() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23DateConversionFromV24YearOnly2.mp3"));
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        ID3v24Frame v24Frame = new ID3v24Frame("TDRC");
        FrameBodyTDRC fbTdrc = new FrameBodyTDRC((byte)1, "2004");
        v24Frame.setBody(fbTdrc);
        v24Tag.addFrame(v24Frame);
        v24Frame = (ID3v24Frame)v24Tag.getFrame("TDRC");
        fbTdrc = (FrameBodyTDRC)v24Frame.getBody();
        Assert.assertEquals("2004", fbTdrc.getText());
        Assert.assertEquals("2004", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
    }

    @Test public void testV23DayMonthYearConversionFromV24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23DayMonthYearConversionFromV24.mp3"));
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR, "2004-06-30");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC fbTdrc = (FrameBodyTDRC)v24Frame.getBody();
        Assert.assertEquals("2004-06-30", fbTdrc.getText());
        Assert.assertEquals("2004-06-30", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);
        //TODO shoudnt this be 2004-06-30 it is after save
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2TagOnly(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004-06-30", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        //v24Tag = (ID3v24Tag)mp3File.getID3v2TagAsv24();
        v24Tag = new ID3v24Tag(v2Tag);
        Iterator<AbstractID3v2Frame> i = v24Tag.iterator();

        Assert.assertEquals("2004-06-30", v24Tag.getFirst(FieldKey.YEAR));
        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        i = frame.getFrames().iterator();
        FrameBodyTYER fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("3006", fbTdat.getText());
    }

    @Test public void testV23MonthYearConversionFromV24() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testV23MonthYearConversionFromV24.mp3"));

        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24Tag = new ID3v24Tag();
        v24Tag.setField(FieldKey.YEAR, "2004-06");
        AbstractID3v2Frame v24Frame = (AbstractID3v2Frame)v24Tag.getFrame("TDRC");
        FrameBodyTDRC fbTdrc = (FrameBodyTDRC)v24Frame.getBody();
        Assert.assertEquals("2004-06", fbTdrc.getText());
        Assert.assertEquals("2004-06", v24Tag.getFirst(ID3v24FieldKey.YEAR));
        ID3v23Tag v2Tag = new ID3v23Tag(v24Tag);

        //TODO shoudnt this be 2004-06-01 it is after save
        Assert.assertEquals("2004", v2Tag.getFirst(ID3v23FieldKey.YEAR));
        mp3File.setID3v2Tag(v2Tag);
        mp3File.saveMp3();

        mp3File = new MP3File(testFile);
        v2Tag = (ID3v23Tag)mp3File.getID3v2Tag();
        Assert.assertEquals("2004-06-01", v2Tag.getFirst(ID3v23FieldKey.YEAR));

        TyerTdatAggregatedFrame frame = ((TyerTdatAggregatedFrame)v2Tag.getFrame("TYERTDAT"));
        Assert.assertNotNull(frame);
        Set<AbstractID3v2Frame> frames = frame.getFrames();
        Iterator<AbstractID3v2Frame> i = frames.iterator();
        FrameBodyTYER fbTyer = ((FrameBodyTYER)i.next().getBody());
        Assert.assertEquals("2004", fbTyer.getText());
        FrameBodyTDAT fbTdat = ((FrameBodyTDAT)i.next().getBody());
        Assert.assertNotNull(fbTdat);
        Assert.assertEquals("0106", fbTdat.getText());
    }

}
