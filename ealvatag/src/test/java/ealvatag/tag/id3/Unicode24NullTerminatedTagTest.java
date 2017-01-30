package ealvatag.tag.id3;

import ealvatag.TestUtil;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.framebody.FrameBodyCOMM;
import ealvatag.tag.id3.framebody.FrameBodyWXXX;
import ealvatag.tag.id3.framebody.FrameBodyWXXXTest;
import ealvatag.tag.id3.valuepair.TextEncoding;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class Unicode24NullTerminatedTagTest {
    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testUTF8WithNullTerminator() throws Exception {
        MP3File mp3File = new MP3File(TestUtil.copyAudioToTmp("testV24-comments-utf8.mp3"));
        AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
        Assert.assertNotNull(id3v2Tag);
        AbstractID3v2Frame frame = (AbstractID3v2Frame)id3v2Tag.getFrame("COMM");
        Assert.assertNotNull(frame);
        AbstractTagFrameBody frameBody = frame.getBody();
        Assert.assertTrue(frameBody instanceof FrameBodyCOMM);
        FrameBodyCOMM commFrameBody = (FrameBodyCOMM)frameBody;

        //String borodin = "\u0411\u043e\u0440\u043e\u0434\u0438\u043d";
        byte UTF8_ENCODING = TextEncoding.UTF_8;
        String language = "eng";
        String comment = "some comment here";
        String description = "cc";
        Assert.assertEquals(UTF8_ENCODING, commFrameBody.getTextEncoding());
        Assert.assertEquals(language, commFrameBody.getLanguage());
        Assert.assertEquals(description, commFrameBody.getDescription());
        Assert.assertEquals(comment, commFrameBody.getText());

        ID3v24Frame newFrame = new ID3v24Frame(ID3v24Frames.FRAME_ID_COMMENT);
        FrameBodyCOMM targetFrameBody = (FrameBodyCOMM)newFrame.getBody();
        targetFrameBody.setTextEncoding(UTF8_ENCODING);
        targetFrameBody.setLanguage(language);
        targetFrameBody.setDescription(description);
        targetFrameBody.setText(comment);
        Assert.assertEquals(UTF8_ENCODING, targetFrameBody.getTextEncoding());
        Assert.assertEquals(language, targetFrameBody.getLanguage());
        Assert.assertEquals(description, targetFrameBody.getDescription());
        Assert.assertEquals(comment, targetFrameBody.getText());

        Assert.assertEquals(targetFrameBody, commFrameBody);
    }

    /**
     * Create a String that only contains text within IS8859 charset so should be
     * as ISO_88859
     */
    @Test public void testCreateISO8859EncodedNullTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }

    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     */
    @Test public void testCreateUTF16BOMLEEncodedNullTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }


    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     */
    @Test public void testCreateUTF16BOMBEEncodedNullTerminatedString() throws Exception {
        TagOptionSingleton.getInstance().setEncodeUTF16BomAsLittleEndian(false);
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, fb.getDescription());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }

    /**
     * Create a String that contains text outside of the IS8859 charset should be written
     * as UTf16 by default
     */
    @Test public void testCreateUTF16AutoEncodedNullTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }

    /**
     * Strings can bbe written to UTF16BE if text encoding explicitly set
     */
    @Test public void testCreateUTF16BEEncodedNullTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16BE);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF16BE
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_16BE, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }

    /**
     * Strings can bbe written to UTF8 if text encoding explicitly set
     */
    @Test public void testCreateUTF8EncodedNullTerminatedString() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        Exception exceptionCaught = null;
        FrameBodyWXXX fb = null;
        try {
            fb = FrameBodyWXXXTest.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_8);
            frame.setBody(fb);
        } catch (Exception e) {
            exceptionCaught = e;
        }

        Assert.assertNull(exceptionCaught);
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, fb.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, fb.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, fb.getUrlLink());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.saveMp3();

        //Reload, should be written as UTF8
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame)mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_USER_DEFINED_URL);
        FrameBodyWXXX body = (FrameBodyWXXX)frame.getBody();
        Assert.assertEquals(ID3v24Frames.FRAME_ID_USER_DEFINED_URL, body.getIdentifier());
        Assert.assertEquals(TextEncoding.UTF_8, body.getTextEncoding());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_UNICODE_REQUIRED_TEST_STRING, body.getDescription());
        Assert.assertEquals(FrameBodyWXXXTest.WXXX_TEST_URL, body.getUrlLink());

    }
}
