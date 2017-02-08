package ealvatag.audio.dsf;


import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.audio.Utils;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DsfAudioFileTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadDsfTag() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertEquals("DSF", ah.getEncodingType());
        Assert.assertEquals("5644800", Utils.formatBitRate(ah, ah.getBitRate()));
        Assert.assertEquals(5644800, ah.getBitRate());
        Assert.assertEquals("2", String.valueOf(ah.getChannelCount()));
        Assert.assertEquals("2822400", String.valueOf(ah.getSampleRate()));
        Assert.assertEquals(5, ah.getDuration(TimeUnit.SECONDS, true));
        Assert.assertFalse(ah.isLossless());
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertEquals("Artist", tag.getFirst(FieldKey.ARTIST));
        Assert.assertEquals("test3", tag.getFirst(FieldKey.TITLE));
        Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
        Assert.assertEquals("Album Artist", tag.getFirst(FieldKey.ALBUM_ARTIST));
        Assert.assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));
        Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
        Assert.assertEquals("Publisher", tag.getFirst(FieldKey.RECORD_LABEL));
    }

    @Test public void testWriteDsfTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122write.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("test3", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("Album", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("Album Artist", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("Crossover", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("comments", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("Publisher", tag.getFirst(FieldKey.RECORD_LABEL));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testDeleteDsfTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122delete.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().or(NullTag.INSTANCE).addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            f.deleteFileTag();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testReadDsfNoTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = TestUtil.copyAudioToTmp("test156.dsf", new File("test156read.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            System.out.println(ah);
            Assert.assertEquals("5644800", Utils.formatBitRate(ah, ah.getBitRate()));
            Assert.assertEquals(5644800, ah.getBitRate());
            Assert.assertEquals("2", String.valueOf(ah.getChannelCount()));
            Assert.assertEquals("2822400", String.valueOf(ah.getSampleRate()));
            Assert.assertEquals(5, ah.getDuration(TimeUnit.SECONDS, true));
            Assert.assertFalse(ah.isLossless());
            Tag tag = f.getTag().orNull();
            Assert.assertNull(tag);
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testWriteDsfNoTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test156.dsf", new File("test156write.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            Assert.assertNull(f.getTag().orNull());
            f.getTagOrSetNewDefault().addField(FieldKey.ARTIST, "fred");
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test
    public void testDeleteDsfNoTag() throws Exception {
        File orig = new File("testdata", "test156.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test156.dsf", new File("test156delete.dsf"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertNull(f.getTag().orNull());
        f.getTagOrSetNewDefault().addField(FieldKey.ARTIST, "fred");
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        f.deleteFileTag();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
    }

    @Test public void testCreateDefaultTag() throws Exception {
        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        // code blocks are simply to scope variables LOL This is hilarious. Leaving for someone else to find and get some giggles

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            Assert.assertTrue(AudioFileIO.read(testFile).setNewDefaultTag() instanceof ID3v24Tag);
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            Assert.assertTrue(AudioFileIO.read(testFile).setNewDefaultTag() instanceof ID3v23Tag);
        }

        {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            File testFile = TestUtil.copyAudioToTmp("test122.dsf", new File("test122read.dsf"));
            Assert.assertTrue(AudioFileIO.read(testFile).setNewDefaultTag() instanceof ID3v22Tag);
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
