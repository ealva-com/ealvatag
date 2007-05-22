package org.jaudiotagger.tag.id3;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.audio.mp3.MP3File;

/**
 *
 */
public class ID3v22TagTest extends TestCase
{
    /**
     * Constructor
     * @param arg0
     */
    public ID3v22TagTest(String arg0) {
        super(arg0);
    }

    /**
     * Command line entrance.
     * @param args
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(ID3v22TagTest.suite());
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
        return new TestSuite(ID3v22TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////


    public void testCreateIDv22Tag()
    {
        ID3v22Tag v2Tag = new ID3v22Tag();
        assertEquals((byte)2,v2Tag.getRelease());
        assertEquals((byte)2,v2Tag.getMajorVersion());
        assertEquals((byte)0,v2Tag.getRevision());
    }

    public void testCreateID3v22FromID3v11()
    {
           ID3v11Tag v11Tag = ID3v11TagTest.getInitialisedTag();
           ID3v22Tag v2Tag = new ID3v22Tag(v11Tag);
           assertNotNull(v11Tag);
           assertNotNull(v2Tag);
           assertEquals(ID3v11TagTest.ARTIST,((FrameBodyTPE1)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ARTIST)).getBody()).getText());
           assertEquals(ID3v11TagTest.ALBUM,((FrameBodyTALB)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_ALBUM)).getBody()).getText());
           assertEquals(ID3v11TagTest.COMMENT,((FrameBodyCOMM)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_COMMENT)).getBody()).getText());
           assertEquals(ID3v11TagTest.TITLE,((FrameBodyTIT2)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TITLE)).getBody()).getText());
           assertEquals(ID3v11TagTest.TRACK_VALUE,((FrameBodyTRCK)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TRACK)).getBody()).getText());
           assertTrue(((FrameBodyTCON)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_GENRE)).getBody()).getText().endsWith(ID3v11TagTest.GENRE_VAL));

            //TODO:Note confusingly V22 YEAR Frame shave v2 identifier but use TDRC behind the scenes, is confusing
            assertEquals(ID3v11TagTest.YEAR,((FrameBodyTDRC)((ID3v22Frame)v2Tag.getFrame(ID3v22Frames.FRAME_ID_V2_TYER)).getBody()).getText());

            assertEquals((byte)2,v2Tag.getRelease());
            assertEquals((byte)2,v2Tag.getMajorVersion());
            assertEquals((byte)0,v2Tag.getRevision());

    }

}
