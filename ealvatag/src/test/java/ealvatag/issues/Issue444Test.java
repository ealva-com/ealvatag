package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.TyerTdatAggregatedFrame;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

/**
 * ID3v23 Date needs splitting into frames
 */
public class Issue444Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testFullDateWrittenToID3v24() {

        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10-12");
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("2004-10-12", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v24Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNotNull(((ID3v24Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testFullDateWrittenToID3v23NeedsToBeSplitIntoFrames() {
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10-12");
            Assert.assertEquals("2004-10-12", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame
                    aggframe =
                    (TyerTdatAggregatedFrame)(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            Assert.assertEquals("2004", i.next().getContent());
            Assert.assertEquals("1210", i.next().getContent());
            Assert.assertEquals("2004-10-12", aggframe.getContent());
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("2004-10-12", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            i = aggframe.getFrames().iterator();
            Assert.assertEquals("2004", i.next().getContent());
            Assert.assertEquals("1210", i.next().getContent());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testYearAndMonthWrittenToID3v23NeedsToBeSplitIntoFrames() {
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10");
            Assert.assertEquals("2004-10-01", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame
                    aggframe =
                    (TyerTdatAggregatedFrame)(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            Assert.assertEquals("2004", i.next().getContent());
            Assert.assertEquals("0110", i.next().getContent());
            Assert.assertEquals("2004-10-01", aggframe.getContent());
            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("2004-10-01", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            i = aggframe.getFrames().iterator();
            Assert.assertEquals("2004", i.next().getContent());
            Assert.assertEquals("0110", i.next().getContent());


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testYearWrittenToID3v23NeedsToBePutInTyerFrame() {
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004");
            Assert.assertEquals("2004", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            af.save();
            af = AudioFileIO.read(testFile);
            Assert.assertEquals("2004", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            Assert.assertNotNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testInvalidYearNotWrittenToID3v23() {
        Exception e = null;
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "20");
            Assert.assertEquals("0020", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            Assert.assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));

        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testDuplicates() {
        File orig = new File("testdata", "test106.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test106.mp3");
            AudioFile af = AudioFileIO.read(testFile);

        } catch (Exception ex) {
            ex.printStackTrace();
            e = ex;
        }
        Assert.assertNull(e);
    }
}
