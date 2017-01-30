package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class Issue478Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testKeepPodcastTags() throws Exception {
        File orig = new File("testdata", "test115.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test115.mp3");
            AudioFile af = AudioFileIO.read(testFile);
            Assert.assertNotNull(af.getTag().orNull());
            MP3File mp3File = (MP3File)af;
            ID3v23Tag tag = (ID3v23Tag)mp3File.getID3v2Tag();
            Assert.assertTrue(tag.hasFrame("TGID"));
            Assert.assertTrue(tag.hasFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("TGID"));
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            tag = (ID3v23Tag)mp3File.getID3v2Tag();

            Assert.assertTrue(tag.hasFrame("PCST"));
            Assert.assertTrue(tag.hasFrame("TGID"));
            Assert.assertNotNull(tag.getFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("TGID"));

            //Now get v24 Version
            ID3v24Tag v24tag = (ID3v24Tag)mp3File.getID3v2TagAsv24();
            Assert.assertTrue(v24tag.hasFrame("TGID"));
            Assert.assertTrue(v24tag.hasFrame("PCST"));
            Assert.assertNotNull(v24tag.getFrame("PCST"));
            Assert.assertNotNull(v24tag.getFrame("TGID"));

            //Convert V24tag back to v23 tag
            mp3File.setID3v2Tag(new ID3v23Tag(v24tag));
            tag = (ID3v23Tag)mp3File.getID3v2Tag();
            Assert.assertTrue(tag.hasFrame("TGID"));
            Assert.assertTrue(tag.hasFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("TGID"));

            //Save v23 tag constructed from v24 tag
            mp3File.save();
            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            tag = (ID3v23Tag)mp3File.getID3v2Tag();
            //and check still has values
            Assert.assertTrue(tag.hasFrame("TGID"));
            Assert.assertTrue(tag.hasFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("PCST"));
            Assert.assertNotNull(tag.getFrame("TGID"));


        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
        }
        Assert.assertNull(ex);
    }
}
