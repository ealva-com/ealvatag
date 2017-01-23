package ealvatag.issues;

import ealvatag.AbstractTestCase;
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

import java.io.File;
import java.util.Iterator;

/**
 * ID3v23 Date needs splitting into frames
 */
public class Issue444Test extends AbstractTestCase
{
    public void testFullDateWrittenToID3v24()
    {

        try
        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10-12");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-12",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v24Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNotNull(((ID3v24Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testFullDateWrittenToID3v23NeedsToBeSplitIntoFrames()
    {
        try
        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10-12");
            assertEquals("2004-10-12", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("1210", i.next().getContent());
            assertEquals("2004-10-12",aggframe.getContent());
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-12", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("1210", i.next().getContent());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testYearAndMonthWrittenToID3v23NeedsToBeSplitIntoFrames()
    {
        try
        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004-10");
            assertEquals("2004-10-01", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("0110", i.next().getContent());
            assertEquals("2004-10-01",aggframe.getContent());
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-01", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));
            i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("0110", i.next().getContent());


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testYearWrittenToID3v23NeedsToBePutInTyerFrame()
    {
        try
        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "2004");
            assertEquals("2004", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals("2004", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));
            assertNotNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDAT"));
            assertNull(((ID3v23Tag) af.getTag().or(NullTag.INSTANCE)).getFrame("TYERTDAT"));

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testInvalidYearNotWrittenToID3v23()
    {
        Exception e=null;
        try
        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrSetNewDefault();
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.YEAR, "20");
            assertEquals("0020", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag().or(NullTag.INSTANCE)).getFrame("TDRC"));

        }
        catch(Exception ex)
        {
            e=ex;
        }
        assertNull(e);
    }

    public void testDuplicates()
    {
        File orig = new File("testdata", "test106.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test106.mp3");
            AudioFile af = AudioFileIO.read(testFile);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
    }
}
