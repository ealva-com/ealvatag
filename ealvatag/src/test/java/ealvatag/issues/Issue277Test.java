package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.mp4.Mp4FieldKey;
import ealvatag.tag.mp4.Mp4Tag;

import java.io.File;

/**
 * Test writing booleans to mp4
 */
public class Issue277Test extends AbstractTestCase
{

    /**
     * Set isCompilation
     */
    public void testSetIsCompilation()
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(0,af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.IS_COMPILATION).size());

            //Old way
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.IS_COMPILATION,"1");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals(1,af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.IS_COMPILATION).size());
            assertEquals("1",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_COMPILATION));


        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Set isCompilation new way
     */
    public void testSetIsCompilation2()
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            assertEquals(0,af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.IS_COMPILATION).size());

            //Old way
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.IS_COMPILATION,"true");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals(1,af.getTag().or(NullTag.INSTANCE).getFields(FieldKey.IS_COMPILATION).size());
            assertEquals("1",af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.IS_COMPILATION));


        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Set isCompilation and rating fields
     */
    public void testSetRating()
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)af.getTag().or(NullTag.INSTANCE);
            assertEquals(0,tag.get(Mp4FieldKey.RATING).size());

            //Old way
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.RATING,"1");
            af.save();
            af = AudioFileIO.read(testFile);
            assertEquals(1,tag.get(Mp4FieldKey.RATING).size());
            assertEquals("1",tag.getFirst(Mp4FieldKey.RATING));


        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Set rating is one byte but not true and false so should fail
     */
    public void testSetRating2()
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

            AudioFile af = AudioFileIO.read(testFile);
            Mp4Tag tag = (Mp4Tag)af.getTag().or(NullTag.INSTANCE);
            assertEquals(0,tag.get(Mp4FieldKey.RATING).size());

            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.RATING,"true");


        }
        catch(Exception e)
        {
            exceptionCaught=e;
        }

        assertNotNull(exceptionCaught);
    }
}
