package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * Test
 */
public class Issue478Test extends AbstractTestCase
{
    public void testKeepPodcastTags() throws Exception
    {
        File orig = new File("testdata", "test115.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test115.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag());
            MP3File mp3File = (MP3File) af;
            ID3v23Tag tag   = (ID3v23Tag)mp3File.getID3v2Tag();
            assertTrue(tag.hasFrame("TGID"));
            assertTrue(tag.hasFrame("PCST"));
            assertNotNull(tag.getFrame("PCST"));
            assertNotNull(tag.getFrame("TGID"));
            mp3File.commit();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File) af;
            tag   = (ID3v23Tag)mp3File.getID3v2Tag();

            assertTrue(tag.hasFrame("PCST"));
            assertTrue(tag.hasFrame("TGID"));
            assertNotNull(tag.getFrame("PCST"));
            assertNotNull(tag.getFrame("TGID"));

            //Now get v24 Version
            ID3v24Tag v24tag   = (ID3v24Tag)mp3File.getID3v2TagAsv24();
            assertTrue(v24tag.hasFrame("TGID"));
            assertTrue(v24tag.hasFrame("PCST"));
            assertNotNull(v24tag.getFrame("PCST"));
            assertNotNull(v24tag.getFrame("TGID"));

            //Convert V24tag back to v23 tag
            mp3File.setID3v2Tag(new ID3v23Tag(v24tag));
            tag   = (ID3v23Tag)mp3File.getID3v2Tag();
            assertTrue(tag.hasFrame("TGID"));
            assertTrue(tag.hasFrame("PCST"));
            assertNotNull(tag.getFrame("PCST"));
            assertNotNull(tag.getFrame("TGID"));

            //Save v23 tag constructed from v24 tag
            mp3File.commit();
            af = AudioFileIO.read(testFile);
            mp3File = (MP3File) af;
            tag   = (ID3v23Tag)mp3File.getID3v2Tag();
            //and check still has values
            assertTrue(tag.hasFrame("TGID"));
            assertTrue(tag.hasFrame("PCST"));
            assertNotNull(tag.getFrame("PCST"));
            assertNotNull(tag.getFrame("TGID"));


        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
