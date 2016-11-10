package org.jaudiotagger.audio.dsf;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.reference.ID3V2Version;

import java.io.File;

public class DsfAudioFileTest extends TestCase {

    public void testReadDsfTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf",new File("test122read.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertEquals("DSF", ah.getEncodingType());
            assertEquals("5644800", ah.getBitRate());
            assertEquals(5644800,ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("2822400",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertFalse(ah.isLossless());
            Tag tag = f.getTag();
            System.out.println(tag);
            assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
            assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Album Artist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Crossover",tag.getFirst(FieldKey.GENRE));
            assertEquals("comments",tag.getFirst(FieldKey.COMMENT));
            assertEquals("Publisher",tag.getFirst(FieldKey.RECORD_LABEL));
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        

    }

    public void testWriteDsfTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf",new File("test122write.dsf"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("fred",tag.getFirst(FieldKey.ARTIST));
            assertEquals("test3",tag.getFirst(FieldKey.TITLE));
            assertEquals("Album",tag.getFirst(FieldKey.ALBUM));
            assertEquals("Album Artist",tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("Crossover",tag.getFirst(FieldKey.GENRE));
            assertEquals("comments",tag.getFirst(FieldKey.COMMENT));
            assertEquals("Publisher",tag.getFirst(FieldKey.RECORD_LABEL));


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testDeleteDsfTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf",new File("test122delete.dsf"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            f.delete();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testReadDsfNoTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test156.dsf",new File("test156read.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            System.out.println(ah);
            assertEquals("5644800", ah.getBitRate());
            assertEquals(5644800,ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("2822400",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertFalse(ah.isLossless());
            Tag tag = f.getTag();
            assertNull(tag);
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testWriteDsfNoTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test156.dsf",new File("test156write.dsf"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            assertNull(f.getTag());
            f.getTagOrCreateAndSetDefault().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("fred",tag.getFirst(FieldKey.ARTIST));


        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);


    }

    public void testDeleteDsfNoTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test156.dsf",new File("test156delete.dsf"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            assertNull(f.getTag());
            f.getTagOrCreateAndSetDefault().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag();
            System.out.println(tag);
            f.delete();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testCreateDefaultTag() throws Exception
    {
        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            assertTrue(AudioFileIO.read(testFile).createDefaultTag() instanceof ID3v24Tag);
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            assertTrue(AudioFileIO.read(testFile).createDefaultTag() instanceof ID3v23Tag);
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            assertTrue(AudioFileIO.read(testFile).createDefaultTag() instanceof ID3v22Tag);
        }

        TagOptionSingleton.getInstance().setToDefault();
    }

    /*
    public void testRemoveTagData() throws Exception
    {
        File dir = new File("C:\\Users\\Paul\\Music\\1983 - David Bowie - Let's Dance [SACD DSF][2003]");
        for(File file:dir.listFiles())
        {
            AudioFile af = AudioFileIO.read(file);
            af.delete();
        }
    }
*/
}
