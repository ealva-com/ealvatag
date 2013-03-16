package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;

import java.io.File;

/**
 * Test
 */
public class Issue455Test extends AbstractTestCase
{
    public void testMp4IsCompilationTrue() throws Exception
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        AudioFile mp4File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

        Exception e= null;
        try
        {

            mp4File = AudioFileIO.read(testFile);
            mp4File.getTag().setField(FieldKey.IS_COMPILATION,"true");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("1",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp4File.commit();
        mp4File = AudioFileIO.read(testFile);
        assertEquals("1",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

    }


    public void testMp4IsCompilationTrue2() throws Exception
    {

        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        AudioFile mp4File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");

        Exception e= null;
        try
        {

            mp4File = AudioFileIO.read(testFile);
            TagField tf = mp4File.getTag().createCompilationField(true);
            mp4File.getTag().setField(tf);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("1",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp4File.commit();
        mp4File = AudioFileIO.read(testFile);
        assertEquals("1",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

    }


    public void testMp4IsCompilationFalse() throws Exception
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        AudioFile mp4File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");
        Exception e= null;
        try
        {

            mp4File = AudioFileIO.read(testFile);
            mp4File.getTag().setField(FieldKey.IS_COMPILATION,"false");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("0",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp4File.commit();
        mp4File = AudioFileIO.read(testFile);
        assertEquals("0",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));
    }

    public void testMp4IsCompilationFalse2() throws Exception
    {
        File orig = new File("testdata", "test1.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        AudioFile mp4File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test1.m4a");
        Exception e= null;
        try
        {

            mp4File = AudioFileIO.read(testFile);
            TagField tf = mp4File.getTag().createCompilationField(false);
            mp4File.getTag().setField(tf);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("0",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp4File.commit();
        mp4File = AudioFileIO.read(testFile);
        assertEquals("0",mp4File.getTag().getFirst(FieldKey.IS_COMPILATION));
    }

    public void testMp3IsCompilationTrue() throws Exception
    {

        AudioFile mp3File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        Exception e= null;
        try
        {

            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrCreateAndSetDefault().setField(FieldKey.IS_COMPILATION,"true");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("true", mp3File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp3File.commit();
        mp3File = AudioFileIO.read(testFile);
        assertEquals("true",mp3File.getTagOrCreateAndSetDefault().getFirst(FieldKey.IS_COMPILATION));

    }

    /** set properly when use function */
    public void testMp3IsCompilationTrue2() throws Exception
    {
        AudioFile mp3File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        Exception e= null;
        try
        {

            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrCreateAndSetDefault();
            TagField tf = mp3File.getTag().createCompilationField(true);
            mp3File.getTag().setField(tf);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("1", mp3File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp3File.commit();
        mp3File = AudioFileIO.read(testFile);
        assertEquals("1",mp3File.getTagOrCreateAndSetDefault().getFirst(FieldKey.IS_COMPILATION));

    }


    public void testMp3IsCompilationFalse() throws Exception
    {
        AudioFile mp3File = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        Exception e= null;
        try
        {

            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrCreateAndSetDefault().setField(FieldKey.IS_COMPILATION,"false");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            e=ex;
        }
        assertNull(e);
        assertEquals("false", mp3File.getTag().getFirst(FieldKey.IS_COMPILATION));

        //After Save
        mp3File.commit();
        mp3File = AudioFileIO.read(testFile);
        assertEquals("false",mp3File.getTag().getFirst(FieldKey.IS_COMPILATION));
    }

}
