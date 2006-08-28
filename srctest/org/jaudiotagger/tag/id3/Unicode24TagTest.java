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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.InvalidAudioFrameException;
import org.jaudiotagger.audio.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.AbstractTagFrameBody;

/**
 * 
 */
public class Unicode24TagTest extends TestCase {
    /**
     * Constructor
     * @param arg0
     */
    public Unicode24TagTest(String arg0) {
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
        return new TestSuite(Unicode24TagTest.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // Tests
    /////////////////////////////////////////////////////////////////////////

    /**
     * 
     */
    public void testUTF8WithNullTerminator () throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, RuntimeException {
        MP3File mp3File  = new MP3File (AbstractTestCase.copyAudioToTmp("testV24-comments-utf8.mp3"));
        AbstractID3v2Tag id3v2Tag = mp3File.getID3v2Tag();
        assertNotNull (id3v2Tag);
        AbstractID3v2Frame frame  = (AbstractID3v2Frame) id3v2Tag.getFrame("COMM");
        assertNotNull (frame);
        AbstractTagFrameBody frameBody = frame.getBody();
        assertTrue (frameBody instanceof FrameBodyCOMM);
        FrameBodyCOMM commFrameBody = (FrameBodyCOMM) frameBody;

        //String borodin = "\u0411\u043e\u0440\u043e\u0434\u0438\u043d";
        byte UTF8_ENCODING = (byte) TextEncoding.UTF_8;
        String language = "eng";
        String comment = "some comment here";
        String description = "cc";
        assertEquals (UTF8_ENCODING,commFrameBody.getTextEncoding());
        assertEquals (language,commFrameBody.getLanguage ());
        assertEquals (description,commFrameBody.getDescription () );
        assertEquals (comment,commFrameBody.getText () );

        ID3v24Frame newFrame = new ID3v24Frame(ID3v24Frames.FRAME_ID_COMMENT);
        FrameBodyCOMM targetFrameBody  = ( FrameBodyCOMM)newFrame.getBody();
        targetFrameBody.setTextEncoding(UTF8_ENCODING);
        targetFrameBody.setLanguage(language);
        targetFrameBody.setDescription(description);
        targetFrameBody.setText(comment);
        assertEquals (UTF8_ENCODING,targetFrameBody.getTextEncoding() );
        assertEquals (language,targetFrameBody.getLanguage ());
        assertEquals (description,targetFrameBody.getDescription () );
        assertEquals (comment,targetFrameBody.getText());

        assertEquals (targetFrameBody,commFrameBody );
    }
}
