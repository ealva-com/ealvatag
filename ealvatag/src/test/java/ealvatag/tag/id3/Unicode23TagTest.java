package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.datatype.DataTypes;
import ealvatag.tag.id3.framebody.FrameBodyTPE1;
import ealvatag.tag.id3.framebody.FrameBodyTPE1Test;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@SuppressWarnings("resource")
public class Unicode23TagTest {
    @Before
    public void setup() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Create a String that only contains text within IS8859 charset so should be
     * as ISO_88859
     */
    @Test public void testCreateISO8859EncodedSizeTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }

    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     *
     */
    @Test public void testCreateUTF16BOMLEEncodedSizeTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomle.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }

    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     *
     */
    @Test public void testCreateUTF16BOMBEEncodedSizeTerminatedString() throws Exception {
        TagOptionSingleton.getInstance().setEncodeUTF16BomAsLittleEndian(false);
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testv23utf16bombe.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }


    /**
     * Create a String that contains text outside of the IS8859 charset should be written
     * as UTf16 by default
     *
     */
    @Test public void testCreateUTF16AutoEncodedSizeTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomberequired.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings cannot be written to UTF16BE even if text encoding explicitly set
     *
     */
    @Test public void testCreateUTF16BEEncodedSizeTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16BE);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16BE
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings cannot be written to UTF8 even if text encoding explicitly set, because invalid for v23
     *
     */
    @Test public void testCreateUTF8EncodedSizeTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_8);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF8
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }


    @Test public void testFixv23TagsWithInvalidEncoding() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue109.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        ID3v23Frame artistFrame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)artistFrame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_8, body.getTextEncoding());

        //Save
        mp3File.saveMp3();

        //Read file after save
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        artistFrame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        body = (FrameBodyTPE1)artistFrame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());

        //Text Encoding has been corrected
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    @Test public void testFixv23TagsWithInvalidEncodingAndDefaultOverridden() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue109.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        ID3v23Frame artistFrame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)artistFrame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_8, body.getTextEncoding());

        //Modify tag options
        //So will default to default on save (default is ISO8859)
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);

        //Save
        mp3File.saveMp3();

        //Read file after save
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag)mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        artistFrame = (ID3v23Frame)v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        body = (FrameBodyTPE1)artistFrame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());

        //Text Encoding has been corrected ( note the text could use ISO_8859 but because the user has selected
        //a Unicode text encoding the default behaviour is to just conver to a valid text encoding for this id3 version
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    @Test public void testCreateUTF16BOMLEEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedString() throws Exception {
        //Modify tag options
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomleoption.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

//Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'E');
        Assert.assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        Assert.assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        Assert.assertTrue((buffer.get(21) & 0xff) == 0xFF);
        Assert.assertTrue((buffer.get(22) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        Assert.assertTrue((buffer.get(23) & 0xff) == 'b');
        Assert.assertTrue((buffer.get(24) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(25) & 0xff) == 'e');
        Assert.assertTrue((buffer.get(26) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(27) & 0xff) == 'c');
        Assert.assertTrue((buffer.get(28) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(29) & 0xff) == 'k');
        Assert.assertTrue((buffer.get(28) & 0xff) == 0x00);

    }

    @Test public void testCreateUTF16AndResetEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedString() throws Exception {

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);

        //Modify tag options so rewrites all frames even if already created
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'E');
        Assert.assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        Assert.assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        Assert.assertTrue((buffer.get(21) & 0xff) == 0xFF);
        Assert.assertTrue((buffer.get(22) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        Assert.assertTrue((buffer.get(23) & 0xff) == 'b');
        Assert.assertTrue((buffer.get(24) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(25) & 0xff) == 'e');
        Assert.assertTrue((buffer.get(26) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(27) & 0xff) == 'c');
        Assert.assertTrue((buffer.get(28) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(29) & 0xff) == 'k');
        Assert.assertTrue((buffer.get(28) & 0xff) == 0x00);

    }

    @Test public void testCreateUTF16AndResetEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedStringUnsnced() throws Exception {

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);

        //Modify tag options so rewrites all frames even if already created
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);
        TagOptionSingleton.getInstance().setUnsyncTags(true);


        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'E');
        Assert.assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        Assert.assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        Assert.assertTrue((buffer.get(21) & 0xff) == 0xFF);
        Assert.assertTrue((buffer.get(22) & 0xff) == 0x00);        //Unsync applied
        Assert.assertTrue((buffer.get(23) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        Assert.assertTrue((buffer.get(24) & 0xff) == 'b');
        Assert.assertTrue((buffer.get(25) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(26) & 0xff) == 'e');
        Assert.assertTrue((buffer.get(27) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(28) & 0xff) == 'c');
        Assert.assertTrue((buffer.get(29) & 0xff) == 0x00);
        Assert.assertTrue((buffer.get(30) & 0xff) == 'k');
        Assert.assertTrue((buffer.get(31) & 0xff) == 0x00);

    }


    @Test public void testDoesntCreateUTF16IfDefaultSetEncodedSizeTerminatedStringifOverriddenUsingSetBody() throws Exception {

        //Modify tag options
        //So will default to default on save (default is ISO8859)
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);

        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }


        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, still ISO8859-1
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame)mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)frame.getBody();
        Assert.assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        fc.read(buffer);
        fc.close();

        //Frame Header
        Assert.assertTrue((buffer.get(10) & 0xff) == 'T');
        Assert.assertTrue((buffer.get(11) & 0xff) == 'P');
        Assert.assertTrue((buffer.get(12) & 0xff) == 'E');
        Assert.assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset ISO8859
        Assert.assertTrue((buffer.get(20) & 0xff) == 0x00);

        //Data
        Assert.assertTrue((buffer.get(21) & 0xff) == 'b');
        Assert.assertTrue((buffer.get(22) & 0xff) == 'e');
        Assert.assertTrue((buffer.get(23) & 0xff) == 'c');
        Assert.assertTrue((buffer.get(24) & 0xff) == 'k');
    }

}
