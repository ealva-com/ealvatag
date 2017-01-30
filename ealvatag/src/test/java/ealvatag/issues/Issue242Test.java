package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Test Writing to new urls with common interface
 */
public class Issue242Test {
    /**
     * Test New Urls ID3v24
     */
    @Test public void testWriteNewUrlsFilev24() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1030.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            //Checking not overwriting audio when have to pad to fix data
            long mp3AudioLength = testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();
            mp3File.setID3v2Tag(new ID3v24Tag());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(mp3AudioLength, testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());

            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());

            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://test1");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_RELEASE_SITE, "http://test2");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://test3");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "http://test4");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "http://test5");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test6");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_LYRICS_SITE, "http://test7");
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertTrue(mp3File.getTag().or(NullTag.INSTANCE) instanceof ID3v24Tag);
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_LYRICS_SITE);
            mp3File.saveMp3();
            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());
        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test New Urls ID3v23
     */
    @Test public void testWriteNewUrlsFilev23() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1031.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;
            //Checking not overwriting audio when have to pad to fix data
            long mp3AudioLength = testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();

            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(mp3AudioLength, testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());

            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());


            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://test1");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_RELEASE_SITE, "http://test2");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://test3");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "http://test4");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "http://test5");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test6");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_LYRICS_SITE, "http://test7");

            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_LYRICS_SITE);
            mp3File.saveMp3();
            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());

        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Test New Urls ID3v24
     */
    @Test public void testWriteNewUrlsFilev22() {
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("test1032.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File = (MP3File)af;

            //Checking not overwriting audio when have to pad to fix data
            long mp3AudioLength = testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();

            mp3File.setID3v2Tag(new ID3v22Tag());
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(mp3AudioLength, testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());

            //Check mapped okay ands empty
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());


            //Now write these fields
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_RELEASE_SITE, "http://test1");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_RELEASE_SITE, "http://test2");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_DISCOGS_ARTIST_SITE, "http://test3");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, "http://test4");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, "http://test5");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_OFFICIAL_ARTIST_SITE, "http://test6");
            mp3File.getTag().or(NullTag.INSTANCE).setField(FieldKey.URL_LYRICS_SITE, "http://test7");
            mp3File.saveMp3();

            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            //Check mapped okay ands empty
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(1, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.URL_LYRICS_SITE);

            mp3File.saveMp3();
            af = AudioFileIO.read(testFile);
            mp3File = (MP3File)af;
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            Assert.assertEquals(0, mp3File.getTag().or(NullTag.INSTANCE).getFields(FieldKey.URL_LYRICS_SITE).size());
        } catch (Exception e) {
            exceptionCaught = e;
            e.printStackTrace();
        }
        Assert.assertNull(exceptionCaught);
    }

}
