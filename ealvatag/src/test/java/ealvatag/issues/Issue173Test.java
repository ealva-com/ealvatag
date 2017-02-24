package ealvatag.issues;

import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Frame;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.framebody.FrameBodyTCON;
import ealvatag.tag.mp4.Mp4FieldKey;
import ealvatag.tag.mp4.Mp4Tag;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class Issue173Test {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testMp4GenresUsingGenericInterface() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        File orig = new File("testdata", "test.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        try {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("test.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag)mp4File.getTag().or(NullTag.INSTANCE);
            //Set valid value
            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            mp4File.save();

            //Rereads as value
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag)mp4File.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            //Set Genre using integer
            tag.setField(FieldKey.GENRE, "1");
            //Read back as integer
            //TODO should read back as Blues here I think
            Assert.assertEquals("1", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("1", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.save();
            //On fresh reread shows as mapped value
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag)mp4File.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Classic Rock", tag.getFirst(Mp4FieldKey.GENRE));

            //Set value that can only be stored as a custom
            //because using generic interface the genre field is removed automtically
            tag.setField(FieldKey.GENRE, "FlapFlap");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            Assert.assertEquals("FlapFlap", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("FlapFlap", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.save();
            Assert.assertEquals("FlapFlap", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("FlapFlap", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));

            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.save();
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));

            //Always use custom
            TagOptionSingleton.getInstance().setWriteMp4GenresAsText(true);
            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.save();
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            Assert.assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testMp4GenresUsingMp4Interface() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        File orig = new File("testdata", "test.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        try {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("test.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag)mp4File.getTag().or(NullTag.INSTANCE);
            //Set valid value
            tag.setField(Mp4FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            //Doesnt remove CUSTOM field as we are using mp4 interface
            Assert.assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            mp4File.save();
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            //Doesnt remove CUSTOM field as we are using mp4 interface
            Assert.assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test public void testMp4InvalidGenresUsingMp4Interface() {
        TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);

        File orig = new File("testdata", "test.m4a");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        try {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("test.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag)mp4File.getTag().or(NullTag.INSTANCE);
            //Set valid value
            tag.setField(Mp4FieldKey.GENRE, "Rocky");
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof IllegalArgumentException);
            Assert.assertTrue(ex.getMessage().equals(ErrorMessage.NOT_STANDARD_MP$_GENRE));
        }
    }

    @Test public void testMp3ID3v24sGenresUsingGenericInterface() {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            AudioFile mp3File = null;
            ID3v24Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrSetNewDefault();
            tag = (ID3v24Tag)mp3File.getTag().or(NullTag.INSTANCE);

            //Set string  representation of standard value
            tag.setField(FieldKey.GENRE, "Rock");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            FrameBodyTCON body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("17", body.getText());

            //Set Integral value directly, gets converted
            tag.setField(FieldKey.GENRE, "1");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("1", body.getText());

            //Set Integral value > 125 directly, gets converted
            tag.setField(FieldKey.GENRE, "127");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            // because we explicitly set integer value, use it
            Assert.assertEquals("127", body.getText());

            //Set string representation of Integral value > 125
            tag.setField(FieldKey.GENRE, "Drum & Bass");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            // because we actually set string, write string instead of integer
            Assert.assertEquals("Drum & Bass", body.getText());

            //Set Invalid Integral value directly,taken literally
            tag.setField(FieldKey.GENRE, "250");
            Assert.assertEquals("250", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("250", body.getText());

            tag.setField(FieldKey.GENRE, "Rock");
            tag.addField(FieldKey.GENRE, "Musical");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Musical", tag.getFieldAt(FieldKey.GENRE, 1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("17\u000077", body.getText());
            tag.setField(FieldKey.GENRE, "1");
            tag.addField(FieldKey.GENRE, "2");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Classic Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Country", tag.getFieldAt(FieldKey.GENRE, 1));
            List<String> results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("1\u00002", body.getText());
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v24Tag)mp3File.getTag().or(NullTag.INSTANCE);
            results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("1\u00002", body.getText());

            tag.setField(FieldKey.GENRE, "Remix");
            tag.addField(FieldKey.GENRE, "CR");
            Assert.assertEquals("Remix", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Remix", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Cover", tag.getFieldAt(FieldKey.GENRE, 1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("RX\u0000CR", body.getText());
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v24Tag)mp3File.getTag().or(NullTag.INSTANCE);
            Assert.assertEquals("Remix", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Remix", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Cover", tag.getFieldAt(FieldKey.GENRE, 1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("RX\u0000CR", body.getText());
            tag.addField(FieldKey.GENRE, "67");
            Assert.assertEquals("Cover", tag.getFieldAt(FieldKey.GENRE, 1));
            Assert.assertEquals("RX\u0000CR\u000067", body.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Test public void testMp3ID3v22sGenresUsingGenericInterface() {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            AudioFile mp3File = null;
            ID3v22Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrSetNewDefault();
            tag = (ID3v22Tag)mp3File.getTag().or(NullTag.INSTANCE);

            //Set string  representation of standard value
            tag.setField(FieldKey.GENRE, "Rock");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            FrameBodyTCON body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(17)", body.getText());

            //Set Integral value directly, gets converted
            tag.setField(FieldKey.GENRE, "1");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(1)", body.getText());

            //Set Integral value > 125 directly, gets converted
            tag.setField(FieldKey.GENRE, "127");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            // because we explicitly set integer value, use it
            Assert.assertEquals("(127)", body.getText());

            //Set string representation of Integral value > 125
            tag.setField(FieldKey.GENRE, "Drum & Bass");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            // because we actually set string, write string instead of integer
            Assert.assertEquals("Drum & Bass", body.getText());

            //Set Invalid Integral value directly,taken literally
            tag.setField(FieldKey.GENRE, "250");
            Assert.assertEquals("250", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("250", body.getText());

            tag.setField(FieldKey.GENRE, "Rock");
            tag.addField(FieldKey.GENRE, "Musical");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Musical", tag.getFieldAt(FieldKey.GENRE, 1));

            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(17)(77)", body.getText());
            tag.setField(FieldKey.GENRE, "1");
            tag.addField(FieldKey.GENRE, "2");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Classic Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Country", tag.getFieldAt(FieldKey.GENRE, 1));
            List<String> results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(1)(2)", body.getText());
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v22Tag)mp3File.getTag().or(NullTag.INSTANCE);
            results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("(1)(2)", body.getText());
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            tag.setField(FieldKey.GENRE, "Remix");
            tag.addField(FieldKey.GENRE, "CR");
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(RX)(CR)", body.getText());
//            assertEquals("Remix",tag.getFirst(FieldKey.GENRE));
//            assertEquals("Remix",tag.getValue(FieldKey.GENRE, 0));
//            assertEquals("Cover",tag.getValue(FieldKey.GENRE, 1));
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v22Tag)mp3File.getTag().or(NullTag.INSTANCE);
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCO")).getBody();
            Assert.assertEquals("(RX)(CR)", body.getText());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }

    @Test public void testMp3ID3v23sGenresUsingGenericInterface() {
        File orig = new File("testdata", "01.mp3");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e = null;
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            AudioFile mp3File = null;
            ID3v23Tag tag = null;
            File testFile = TestUtil.copyAudioToTmp("01.mp3");
            mp3File = AudioFileIO.read(testFile);
            mp3File.getTagOrSetNewDefault();
            tag = (ID3v23Tag)mp3File.getTag().or(NullTag.INSTANCE);

            //Set string  representation of standard value
            tag.setField(FieldKey.GENRE, "Rock");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            FrameBodyTCON body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(17)", body.getText());

            //Set Integral value directly, gets converted
            tag.setField(FieldKey.GENRE, "1");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(1)", body.getText());

            //Set Integral value > 125 directly, gets converted
            tag.setField(FieldKey.GENRE, "127");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            // because we explicitly set integer value, use it
            Assert.assertEquals("(127)", body.getText());

            //Set string representation of Integral value > 125
            tag.setField(FieldKey.GENRE, "Drum & Bass");
            Assert.assertEquals("Drum & Bass", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            // because we actually set string, write string instead of integer
            Assert.assertEquals("Drum & Bass", body.getText());

            //Set Invalid Integral value directly,taken literally
            tag.setField(FieldKey.GENRE, "250");
            Assert.assertEquals("250", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("250", body.getText());

            tag.setField(FieldKey.GENRE, "Rock");
            tag.addField(FieldKey.GENRE, "Musical");
            Assert.assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Musical", tag.getFieldAt(FieldKey.GENRE, 1));

            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(17)(77)", body.getText());
            tag.setField(FieldKey.GENRE, "1");
            tag.addField(FieldKey.GENRE, "2");
            Assert.assertEquals("Classic Rock", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Classic Rock", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Country", tag.getFieldAt(FieldKey.GENRE, 1));
            List<String> results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(1)(2)", body.getText());
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)mp3File.getTag().or(NullTag.INSTANCE);
            results = tag.getAll(FieldKey.GENRE);
            Assert.assertEquals("(1)(2)", body.getText());
            Assert.assertEquals("Classic Rock", results.get(0));
            Assert.assertEquals("Country", results.get(1));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            tag.setField(FieldKey.GENRE, "Remix");
            tag.addField(FieldKey.GENRE, "CR");
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(RX)(CR)", body.getText());
            Assert.assertEquals("Remix", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("Remix", tag.getFieldAt(FieldKey.GENRE, 0));
            Assert.assertEquals("Cover", tag.getFieldAt(FieldKey.GENRE, 1));
            mp3File.save();
            mp3File = AudioFileIO.read(testFile);
            tag = (ID3v23Tag)mp3File.getTag().or(NullTag.INSTANCE);
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(RX)(CR)", body.getText());

            tag.setField(FieldKey.GENRE, "Cover");
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(CR)", body.getText());
            tag.addField(FieldKey.GENRE, "FlapFlap");
            Assert.assertEquals("Cover", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            Assert.assertEquals("(CR)\u0000FlapFlap", body.getText());
            tag.setField(FieldKey.GENRE, "Country Shoegaze");
            Assert.assertEquals("Country Shoegaze", tag.getFirst(FieldKey.GENRE));
            body = (FrameBodyTCON)((AbstractID3v2Frame)tag.getFrame("TCON")).getBody();
            //TODO cannot handle setting v23 refinements in generic interface, but does that really matter
            //ID3v24Tag doesnt really have the convcept OutOfMemoryError refinements just multiple values
            Assert.assertEquals("Country Shoegaze", body.getText());
        } catch (Exception ex) {
            e = ex;
        }
        Assert.assertNull(e);
    }
}
