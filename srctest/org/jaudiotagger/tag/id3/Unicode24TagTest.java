/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jaudiotagger.tag.id3;

import java.io.IOException;
import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.InvalidAudioFrameException;
import org.jaudiotagger.audio.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.AbstractTagFrameBody;

/**
 * 
 */
public class Unicode24TagTest extends TestCase
{
    /**
     * Constructor
     *
     * @param arg0
     */
    public Unicode24TagTest(String arg0)
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
        junit.textui.TestRunner.run(suite());
    }

    /////////////////////////////////////////////////////////////////////////
    // TestCase classes to override
    /////////////////////////////////////////////////////////////////////////

    /**
     *
     */
    protected void setUp()
    {
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
        return new TestSuite(Unicode24TagTest.class);
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

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
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
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());
    }

    /**
     * Can explictly uses UTF-16 even if not required
     * as UTf16 by default
     *
     * @throws Exception
     */
    public void testCreateUTF16EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
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
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_TEST_STRING, fb.getText());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, body.getIdentifier());
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
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
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
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.ISO_8859_1, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());



        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16 because of the text
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings can bbe written to UTF16BE if text encoding explicitly set
     *
     * @throws Exception
     */
    public void testCreateUTF16BEEncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
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
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_16BE, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF16BE
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_16BE, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

    /**
     * Strings can bbe written to UTF8 if text encoding explicitly set
     *
     * @throws Exception
     */
    public void testCreateUTF8EncodedSizeTerminatedString() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");
        MP3File mp3File = new MP3File(testFile);

        ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_ARTIST);
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
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, fb.getIdentifier());
        assertEquals(TextEncoding.UTF_8, fb.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, fb.getText());

        //Create and Save
        ID3v24Tag tag = new ID3v24Tag();
        tag.setFrame(frame);
        mp3File.setID3v2Tag(tag);
        mp3File.save();

        //Reload, should be written as UTF8
        mp3File = new MP3File(testFile);
        frame = (ID3v24Frame) mp3File.getID3v2Tag().getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1) frame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());
        assertEquals(FrameBodyTPE1Test.TPE1_UNICODE_REQUIRED_TEST_STRING, body.getText());
    }

     public void testv24TagsWithUTF8EncodingMaintainedOnSave()  throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("Issue109-2.id3", "testV1.mp3");

        //Read file as currently stands
        MP3File mp3File = new MP3File(testFile);
        ID3v24Tag v24tag = (ID3v24Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        ID3v24Frame artistFrame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        FrameBodyTPE1 body = (FrameBodyTPE1)artistFrame.getBody();
        assertEquals(ID3v23Frames.FRAME_ID_V3_ARTIST, body.getIdentifier());
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());

        //Save
        mp3File.save();

        //Read file after save
        mp3File = new MP3File(testFile);
        v24tag = (ID3v24Tag) mp3File.getID3v2Tag();

        //Currently contains tags with invalid textencodings
        artistFrame = (ID3v24Frame)v24tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST);
        body = (FrameBodyTPE1)artistFrame.getBody();
        assertEquals(ID3v24Frames.FRAME_ID_ARTIST, body.getIdentifier());

        //Text Encoding has been corrected ( note the text could use ISO_8859 but because the user has selected
        //a Unicode text encoding the default behaviour is to just conver to a valid text encoding for this id3 version
        assertEquals(TextEncoding.UTF_8, body.getTextEncoding());
    }
}
