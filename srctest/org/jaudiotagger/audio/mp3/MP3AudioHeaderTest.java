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

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;

import java.io.File;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 */
public class MP3AudioHeaderTest extends TestCase
{

    public void testReadV1L3VbrOld()
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
        assertEquals("~127", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV1L3VbrNew()
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
        assertEquals("~127", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV1L3Cbr128()
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
        assertEquals("128", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV1L3Cbr192()
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
        assertEquals("192", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }


    public void testReadV2L3VbrOld()
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
        assertEquals("~127", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV2L3VbrNew()
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
        assertEquals("~127", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV1L2Stereo()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1L2stereo.mp3");
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
        //assertEquals("00:13", mp3AudioHeader.getTrackLengthAsString()); Incorrectly returning 6
        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_II)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_STEREO)), mp3AudioHeader.getChannels());
        assertFalse(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("192", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("", mp3AudioHeader.getEncoder());
    }

    public void testReadV1L2Mono()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV1L2mono.mp3");
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
        assertEquals("00:13", mp3AudioHeader.getTrackLengthAsString());
        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_1)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_II)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertFalse(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("192", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("", mp3AudioHeader.getEncoder());
    }

    public void testReadV25L3VbrOld()
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
        assertEquals("~128", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV25L3()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV25.mp3");
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
        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2_5)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_III)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_MONO)), mp3AudioHeader.getChannels());
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertFalse(mp3AudioHeader.isProtected());
        assertEquals("16", mp3AudioHeader.getBitRate());  //TODO Might be wrong
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("", mp3AudioHeader.getEncoder());   //No Lame header so blank
    }

    public void testReadV25L3VbrNew()
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
        assertEquals("~128", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    public void testReadV2L2()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV2L2.mp3");
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
        assertEquals("24000", mp3AudioHeader.getSampleRate());

        assertFalse(mp3AudioHeader.isVariableBitRate());
        assertEquals(MPEGFrameHeader.mpegVersionMap.get(new Integer(MPEGFrameHeader.VERSION_2)), mp3AudioHeader.getMpegVersion());
        assertEquals(MPEGFrameHeader.mpegLayerMap.get(new Integer(MPEGFrameHeader.LAYER_II)), mp3AudioHeader.getMpegLayer());
        assertEquals(MPEGFrameHeader.modeMap.get(new Integer(MPEGFrameHeader.MODE_JOINT_STEREO)), mp3AudioHeader.getChannels());
        //assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString()); not working returning 0
        assertTrue(mp3AudioHeader.isOriginal());
        assertFalse(mp3AudioHeader.isCopyrighted());
        assertFalse(mp3AudioHeader.isPrivate());
        assertTrue(mp3AudioHeader.isProtected());
        assertEquals("16", mp3AudioHeader.getBitRate());
        assertEquals("mp3", mp3AudioHeader.getEncodingType());
        assertEquals("", mp3AudioHeader.getEncoder());   //No Lame header so blank
    }

    /**
     * Test trying to parse an mp3 file which is not a valid MP3 fails gracefully with expected exception
     */
    public void testIssue79()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("Issue79.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof InvalidAudioFrameException);
    }


    /**
     * Test trying to parse an mp3 file which is not a valid MP3 and is extremely small
     * Should fail gracefully
     */
    public void testIssue81()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("Issue81.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught instanceof InvalidAudioFrameException);
    }

    /**
     * Test trying to parse an mp3 file which is a valid MP3 but problems with frame
     */
    public void testIssue199()
    {
        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("testV2L2.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try
        {
            mp3AudioHeader = new MP3AudioHeader(testFile);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertTrue(exceptionCaught == null);
    }

    /**
     * Test mp3s display tracks over an hour correctly, dont actually have any such track so have to emulate
     * the mp3 rather than calling it directly.
     */
    public void testIssue85()
    {
        Exception exceptionCaught = null;

        int NO_SECONDS_IN_HOUR = 3600;
        SimpleDateFormat timeInFormat            = new SimpleDateFormat("ss");
        SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");
        SimpleDateFormat timeOutOverAnHourFormat = new SimpleDateFormat("kk:mm:ss");

        try
        {
            int lengthLessThanHour = 3500;
            Date timeIn = timeInFormat.parse(String.valueOf(lengthLessThanHour));
            assertEquals("58:20",timeOutFormat.format(timeIn));

            int lengthIsAnHour = 3600;
            timeIn = timeInFormat.parse(String.valueOf(lengthIsAnHour));
            assertEquals("01:00:00",timeOutOverAnHourFormat.format(timeIn));

            int lengthMoreThanHour = 4000;
            timeIn = timeInFormat.parse(String.valueOf(lengthMoreThanHour));
            assertEquals("01:06:40",timeOutOverAnHourFormat.format(timeIn));

        }
        catch (Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertTrue(exceptionCaught == null);
    }
}