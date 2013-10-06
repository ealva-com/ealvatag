package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTYER;
import org.jaudiotagger.tag.reference.ID3V2Version;

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
            af.getTagOrCreateAndSetDefault();
            af.getTag().setField(FieldKey.YEAR, "2004-10-12");
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-12",af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v24Tag) af.getTag()).getFrame("TYER"));
            assertNotNull(((ID3v24Tag) af.getTag()).getFrame("TDRC"));

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
            af.getTagOrCreateAndSetDefault();
            af.getTag().setField(FieldKey.YEAR, "2004-10-12");
            assertEquals("2004-10-12", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("1210", i.next().getContent());
            assertEquals("2004-10-12",aggframe.getContent());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-12", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
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
            af.getTagOrCreateAndSetDefault();
            af.getTag().setField(FieldKey.YEAR, "2004-10");
            assertEquals("2004-10-01", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

            TyerTdatAggregatedFrame aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
            Iterator<AbstractID3v2Frame> i = aggframe.getFrames().iterator();
            assertEquals("2004", i.next().getContent());
            assertEquals("0110", i.next().getContent());
            assertEquals("2004-10-01",aggframe.getContent());
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("2004-10-01", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
            aggframe = (TyerTdatAggregatedFrame)(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));
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
            af.getTagOrCreateAndSetDefault();
            af.getTag().setField(FieldKey.YEAR, "2004");
            assertEquals("2004", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("2004", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));
            assertNotNull(((ID3v23Tag) af.getTag()).getFrame("TYER"));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDAT"));
            assertNull(((ID3v23Tag) af.getTag()).getFrame("TYERTDAT"));

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
            af.getTagOrCreateAndSetDefault();
            af.getTag().setField(FieldKey.YEAR, "20");
            assertEquals("0020", af.getTag().getFirst(FieldKey.YEAR));
            assertNull(((ID3v23Tag)af.getTag()).getFrame("TDRC"));

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
