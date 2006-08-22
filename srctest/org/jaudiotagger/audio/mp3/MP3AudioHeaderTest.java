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
package org.jaudiotagger.audio.mp3;

import java.io.*;

import junit.framework.TestCase;

import org.jaudiotagger.AbstractTestCase;

/**
 */
public class MP3AudioHeaderTest extends TestCase
{

    public void testReadV1VbrOld()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("44100", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~127",mp3AudioHeader.getBitRate());
         assertEquals("mp3",mp3AudioHeader.getType());
    }

    public void testReadV1VbrNew()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("44100", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~127",mp3AudioHeader.getBitRate());
         assertEquals("mp3",mp3AudioHeader.getType());
    }

    public void testReadV1Cbr128()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr128.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("44100", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("128",mp3AudioHeader.getBitRate());
        assertEquals("mp3",mp3AudioHeader.getType());
    }

    public void testReadV1Cbr192()
        {
            Exception exceptionCaught = null;
            File testFile = AbstractTestCase.copyAudioToTmp("testV1Cbr192.mp3");
            MP3AudioHeader mp3AudioHeader = null;
            try
            {
                mp3AudioHeader = new MP3AudioHeader(testFile);

            }
            catch (Exception e)
            {
                exceptionCaught = e;
            }
            assertNull(exceptionCaught);
            assertEquals("44100", mp3AudioHeader.getSampleRate());
            assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
            assertFalse(mp3AudioHeader.isVariableBitRate());
            assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
            assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
            assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
            assertTrue(mp3AudioHeader.isOriginal());
            assertFalse(mp3AudioHeader.isCopyrighted());
            assertFalse(mp3AudioHeader.isPrivate());
            assertFalse(mp3AudioHeader.isProtected());
            assertEquals("192",mp3AudioHeader.getBitRate());
             assertEquals("mp3",mp3AudioHeader.getType());
        }


    public void testReadV2VbrOld()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV2vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("22050", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~127",mp3AudioHeader.getBitRate());
         assertEquals("mp3",mp3AudioHeader.getType());
    }

    public void testReadV2VbrNew()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV2vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("22050", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~127",mp3AudioHeader.getBitRate());
        assertEquals("mp3",mp3AudioHeader.getType());
    }

    public void testReadV25VbrOld()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV25vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("12000", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2_5)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~128",mp3AudioHeader.getBitRate());
        assertEquals("mp3",mp3AudioHeader.getType());
    }


    public void testReadV25VbrNew()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV25vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        assertEquals("12000", mp3AudioHeader.getSampleRate());
        assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        assertTrue(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2_5)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("~128",mp3AudioHeader.getBitRate());
        assertEquals("mp3",mp3AudioHeader.getType());
    }
}
