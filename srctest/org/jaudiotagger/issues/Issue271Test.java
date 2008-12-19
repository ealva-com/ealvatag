package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;
import java.io.RandomAccessFile;

/** ID3 Tag Specific flags
 */
public class Issue271Test extends AbstractTestCase
{

    /**
     * Test read flags
     */
    public void testReadFlagsCompressed()
    {
        File orig = new File("testdata", "test51.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test51.mp3");


            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag();
            assertTrue(v22tag.isCompression());
            assertFalse(v22tag.isUnsynchronization());

            //Now write to tag we dont preservbe compressed flag because cant actualy define compression
            v22tag.setTitle("A new start");
            assertEquals("A new start",v22tag.getFirstTitle());
            af.commit();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertFalse(v22tag.isUnsynchronization());

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setTitle("B new start");
            af.commit();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertTrue(v22tag.isUnsynchronization());
            assertEquals("B new start",v22tag.getFirstTitle());
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);

    }

    /**
     * Test read flags
     */
    public void testReadFlagsUnsyced()
    {
        File orig = new File("testdata", "test52.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test52.mp3");


            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertTrue(v22tag.isUnsynchronization());

            //Now write to tag we dont preservbe compressed flag because cant actualy define compression
            //We dont preserve compression because based on TagOptionDinglton which is false by default
            v22tag.setTitle("A new start");
            af.commit();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertFalse(v22tag.isUnsynchronization());
            assertEquals("A new start",v22tag.getFirstTitle());

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setTitle("B new start");
            af.commit();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertTrue(v22tag.isUnsynchronization());
            assertEquals("B new start",v22tag.getFirstTitle());
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);

    }

    /**
     * Test read flags
     */
    public void testReadFlagsUnsycedCompressed()
    {
        File orig = new File("testdata", "test53.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test53.mp3");


            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            System.out.println(mp3File.displayStructureAsXML());

            ID3v22Tag v22tag = (ID3v22Tag)af.getTag();
            assertTrue(v22tag.isCompression());
            assertTrue(v22tag.isUnsynchronization());

            //Now write to tag we dont preserve compressed flag because cant actually define compression
            //We dont preserve compression because based on TagOptionDinglton which is false by default                     
            v22tag.setTitle("A new start");
            af.commit();

            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertFalse(v22tag.isUnsynchronization());
            assertEquals("A new start",v22tag.getFirstTitle());

            TagOptionSingleton.getInstance().setUnsyncTags(true);
            v22tag.setTitle("B new start");
            af.commit();
            af = AudioFileIO.read(testFile);
            v22tag = (ID3v22Tag)af.getTag();
            assertFalse(v22tag.isCompression());
            assertTrue(v22tag.isUnsynchronization());
            assertEquals("B new start",v22tag.getFirstTitle());
            TagOptionSingleton.getInstance().setUnsyncTags(false);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);

    }

}
