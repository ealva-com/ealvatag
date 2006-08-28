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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * 
 */
public class ID3v24TagTest extends TestCase
{
    /**
     * Constructor
     * @param arg0
     */
    public ID3v24TagTest(String arg0) {
        super(arg0);
    }

    /**
     * Command line entrance.
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
     * @return the Test Suite.
     */
    public static Test suite()
    {
        return new TestSuite(ID3v24TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    public void testCreateTag()
    {
        ID3v24Tag tag = new ID3v24Tag();
        assertNotNull(tag);
    }


    public void testCreateID3v24FromID3v11AndSave()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

        MP3File mp3File = null;

        try
        {
            mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }

        assertNull(exceptionCaught);

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        assertFalse(mp3File.hasID3v1Tag());
        assertFalse(mp3File.hasID3v2Tag());
        mp3File.setID3v1Tag(v1Tag);
        mp3File.setID3v2Tag(mp3File.getID3v1Tag());
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        try
        {
            mp3File.save();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);

        //Reload
        try
        {
            mp3File = new MP3File(testFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(mp3File.hasID3v1Tag());
        assertTrue(mp3File.hasID3v2Tag());
        assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
    }

    public void testCreateID3v24FromID3v11()
    {

        ID3v11Tag v1Tag = ID3v11TagTest.getInitialisedTag();


        ID3v24Tag v2Tag = new ID3v24Tag(v1Tag);
        assertNotNull(v2Tag);
        assertEquals(ID3v11TagTest.ARTIST,((FrameBodyTPE1)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_ARTIST)).getBody()).getText());
        assertEquals(ID3v11TagTest.ALBUM,((FrameBodyTALB)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_ALBUM)).getBody()).getText());
        assertEquals(ID3v11TagTest.COMMENT,((FrameBodyCOMM)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_COMMENT)).getBody()).getText());
        assertEquals(ID3v11TagTest.TITLE,((FrameBodyTIT2)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_TITLE)).getBody()).getText());
        assertEquals(ID3v11TagTest.TRACK_VALUE,((FrameBodyTRCK)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_TRACK)).getBody()).getText());
        assertTrue(((FrameBodyTCON)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));
        assertEquals(ID3v11TagTest.YEAR,((FrameBodyTDRC)((ID3v24Frame)v2Tag.getFrame(ID3v24Frames.FRAME_ID_YEAR)).getBody()).getText());
    }
}
