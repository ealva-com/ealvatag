package org.jaudiotagger.tag.id3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.datatype.DataTypes;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1Test;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 */
public class Unicode23TagTest extends TestCase
{
    /**
     * Constructor
     *
     * @param arg0
     */
    public Unicode23TagTest(String arg0)
    {
        super(arg0);
    }

    /**
     * Command line entrance.
     *
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(Unicode23TagTest.suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    protected void setUp()
    {
        TagOptionSingleton.getInstance().setToDefault();
    }

    /**
     *
     */
    protected void tearDown()
    {
    }

    /**
     *
     */
//    protected void runTest()
//    {
//    }

    /**
     * Builds the Test Suite.
     *
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(Unicode23TagTest.class);
    }


    /**
     * Create a String that only contains text within IS8859 charset so should be
     * as ISO_88859
     *
     * @throws Exception
     */
    public void testCreateISO8859EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }

    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16BOMLEEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomle.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }

    /**
     * Can explicitly uses UTF-16 even if not required
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16BOMBEEncodedSizeTerminatedString() throws Exception
    {
        TagOptionSingleton.getInstance().setEncodeUTF16BomAsLittleEndian(false);
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testv23utf16bombe.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());
    }


    /**
     * Create a String that contains text outside of the IS8859 charset should be written
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16AutoEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomberequired.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings cannot be written to UTF16BE even if text encoding explicitly set
     *
     * @throws Exception
     */
    public void testCreateUTF16BEEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_16BE);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16BE
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings cannot be written to UTF8 even if text encoding explicitly set, because invalid for v23
     *
     * @throws Exception
     */
    public void testCreateUTF8EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getUnicodeRequiredInitialisedBody();
            fb.setTextEncoding(TextEncoding.UTF_8);
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF8
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }


    public void testFixv23TagsWithInvalidEncoding() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue109.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        ID3v23Frame artistFrame = (ID3v23Frame) v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) artistFrame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());

        //Save
        mp3File.save();

        //Read file after save
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        artistFrame = (ID3v23Frame) v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        body = (FrameBodyTPE1) artistFrame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());

        //Text Encoding has been corrected
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    public void testFixv23TagsWithInvalidEncodingAndDefaultOverridden() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue109.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v23Tag v23tag = (ID3v23Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        ID3v23Frame artistFrame = (ID3v23Frame) v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) artistFrame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());

        //Modify tag options
        //So will default to default on save (default is ISO8859)
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);

        //Save
        mp3File.save();

        //Read file after save
        mp3File = new MP3File(testFile);
        v23tag = (ID3v23Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        artistFrame = (ID3v23Frame) v23tag.getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        body = (FrameBodyTPE1) artistFrame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());

        //Text Encoding has been corrected ( note the text could use ISO_8859 but because the user has selected
        //a Unicode text encoding the default behaviour is to just conver to a valid text encoding for this id3 version
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
    }

    /**
     * @throws Exception
     */
    public void testCreateUTF16BOMLEEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedString() throws Exception
    {
        //Modify tag options
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("testv23utf16bomleoption.mp3"));
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try
        {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

//Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int count = fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'E');
        assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        assertTrue((buffer.get(21) & 0xff) == 0xFF);
        assertTrue((buffer.get(22) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        assertTrue((buffer.get(23) & 0xff) == 'b');
        assertTrue((buffer.get(24) & 0xff) == 0x00);
        assertTrue((buffer.get(25) & 0xff) == 'e');
        assertTrue((buffer.get(26) & 0xff) == 0x00);
        assertTrue((buffer.get(27) & 0xff) == 'c');
        assertTrue((buffer.get(28) & 0xff) == 0x00);
        assertTrue((buffer.get(29) & 0xff) == 'k');
        assertTrue((buffer.get(28) & 0xff) == 0x00);

    }

    /**
     * @throws Exception
     */
    public void testCreateUTF16AndResetEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedString() throws Exception
    {

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try
        {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

        //Modify tag options so rewrites all frames even if already created
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int count = fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'E');
        assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        assertTrue((buffer.get(21) & 0xff) == 0xFF);
        assertTrue((buffer.get(22) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        assertTrue((buffer.get(23) & 0xff) == 'b');
        assertTrue((buffer.get(24) & 0xff) == 0x00);
        assertTrue((buffer.get(25) & 0xff) == 'e');
        assertTrue((buffer.get(26) & 0xff) == 0x00);
        assertTrue((buffer.get(27) & 0xff) == 'c');
        assertTrue((buffer.get(28) & 0xff) == 0x00);
        assertTrue((buffer.get(29) & 0xff) == 'k');
        assertTrue((buffer.get(28) & 0xff) == 0x00);

    }

    /**
     * @throws Exception
     */
    public void testCreateUTF16AndResetEvenIfNotNeededIfDefaultSetEncodedSizeTerminatedStringUnsnced() throws Exception
    {

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        try
        {
            frame.getBody().setObjectValue(DataTypes.OBJ_TEXT, FrameBodyTPE1Test.TPE1_TEST_STRING);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

        //Modify tag options so rewrites all frames even if already created
        //So will default to default on save (default is ISO8859) has to be done before the frame is created
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);
        TagOptionSingleton.getInstance().setResetTextEncodingForExistingFrames(true);
        TagOptionSingleton.getInstance().setUnsyncTags(true);


        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because setField in tag options
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int count = fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'E');
        assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset
        assertTrue((buffer.get(20) & 0xff) == 0x01);

        //BOM
        assertTrue((buffer.get(21) & 0xff) == 0xFF);
        assertTrue((buffer.get(22) & 0xff) == 0x00);        //Unsync applied
        assertTrue((buffer.get(23) & 0xff) == 0xFE);

        //Data, least significant byte (which contaisn the data is first in each pair)
        assertTrue((buffer.get(24) & 0xff) == 'b');
        assertTrue((buffer.get(25) & 0xff) == 0x00);
        assertTrue((buffer.get(26) & 0xff) == 'e');
        assertTrue((buffer.get(27) & 0xff) == 0x00);
        assertTrue((buffer.get(28) & 0xff) == 'c');
        assertTrue((buffer.get(29) & 0xff) == 0x00);
        assertTrue((buffer.get(30) & 0xff) == 'k');
        assertTrue((buffer.get(31) & 0xff) == 0x00);

    }


    /**
     * @throws Exception
     */
    public void testDoesntCreateUTF16IfDefaultSetEncodedSizeTerminatedStringifOverriddenUsingSetBody() throws Exception
    {

        //Modify tag options
        //So will default to default on save (default is ISO8859)
        TagOptionSingleton.getInstance().setId3v23DefaultTextEncoding(TextEncoding.UTF_16);

        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v23Frame frame = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        Exception exceptionCaught = null;
        FrameBodyTPE1 fb = null;
        try
        {
            fb = FrameBodyTPE1Test.getInitialisedBody();
            frame.setBody(fb);
        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }


        assertNull(exceptionCaught);
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v23Tag tag = new ID3v23Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, still ISO8859-1
        mp3File = new MP3File(testFile);
        frame = (ID3v23Frame) mp3File.getID3v2Tag().getFrame(ID3v23Frames.FRAME_ID_V3_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, body.getText());

        //Check Bytes
        FileChannel fc = new RandomAccessFile(testFile, "r").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int count = fc.read(buffer);
        fc.close();

        //Frame Header
        assertTrue((buffer.get(10) & 0xff) == 'T');
        assertTrue((buffer.get(11) & 0xff) == 'P');
        assertTrue((buffer.get(12) & 0xff) == 'E');
        assertTrue((buffer.get(13) & 0xff) == '1');

        //Charset ISO8859
        assertTrue((buffer.get(20) & 0xff) == 0x00);

        //Data
        assertTrue((buffer.get(21) & 0xff) == 'b');
        assertTrue((buffer.get(22) & 0xff) == 'e');
        assertTrue((buffer.get(23) & 0xff) == 'c');
        assertTrue((buffer.get(24) & 0xff) == 'k');
    }

}
