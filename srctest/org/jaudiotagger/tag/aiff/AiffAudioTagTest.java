package org.jaudiotagger.tag.aiff;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;

import java.io.File;

public class AiffAudioTagTest extends TestCase {


    public void testReadAiff1() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif", new File("test119ReadAiffWithoutTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(((AiffTag) tag).getID3Tag() instanceof ID3v22Tag);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff2() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test120.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test120.aif", new File("test120ReadAiffWithTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(tag.getFieldCount() == 10);
            assertEquals("Gary McGath", tag.getFirst(FieldKey.ARTIST));
            assertEquals("None", tag.getFirst(FieldKey.ALBUM));
            assertTrue(tag.getFirst(FieldKey.TITLE).indexOf("Short sample") == 0);
            assertEquals("This is actually a comment.", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2012", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121ReadAiffWithoutItunesTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(ah);
            System.out.println(ah.getBitRate());
            assertEquals("2",ah.getChannels());
            System.out.println(ah.getEncodingType());
            assertEquals("44100",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals(5.0f,((AiffAudioHeader) ah).getPreciseLength());

            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(tag.getFieldCount() == 6);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
            assertEquals("2002", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testReadAiff4() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124ReadAiffWithoutItunesTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(ah);
            System.out.println(ah.getBitRate());
            assertEquals("2",ah.getChannels());
            System.out.println(ah.getEncodingType());
            assertEquals("44100",ah.getSampleRate());
            assertEquals(5,ah.getTrackLength());
            assertEquals(5.0f,((AiffAudioHeader) ah).getPreciseLength());

            System.out.println(tag);
            assertNotNull(tag);
            assertTrue(tag instanceof AiffTag);
            assertTrue(tag.getFieldCount() == 6);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
            assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
            assertEquals("2002", tag.getFirst(FieldKey.YEAR));
            assertEquals("1", tag.getFirst(FieldKey.TRACK));
            assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121WriteAiffWithTagAddPadding.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertNotNull(tag);
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getTag());

            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            tag.setField(FieldKey.ARTIST, "Warmplayer");
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(f.getTag());
            assertEquals("Warmplayer", tag.getFirst(FieldKey.ARTIST));


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteAiffWithoutTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif", new File("test119WriteAiffWithoutTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertNotNull(tag);
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            System.out.println(tag);
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteAiff3() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test121.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test121.aif", new File("test121DeleteTag.aif"));
        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertNotNull(((AiffTag)tag).getID3Tag());
            assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            AudioFileIO.delete(f);

            f = null;
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag();
            System.out.println(tag2);
            assertNotNull(tag2);
            assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteAiff4() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File testFile = AbstractTestCase.copyAudioToTmp("test124.aif", new File("test124DeleteTag.aif"));

        try
        {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            System.out.println(tag);
            assertNotNull(tag);
            assertNotNull(((AiffTag)tag).getID3Tag());
            assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            AudioFileIO.delete(f);

            f = null;
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag();
            System.out.println(tag2);
            assertNotNull(tag2);
            assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        assertNull(exceptionCaught);
    }

}
