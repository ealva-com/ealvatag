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
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ealvatag.audio.mp3;

import ealvatag.TestUtil;
import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagOptionSingleton;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MP3AudioHeaderTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testReadV1L3VbrOld() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~127", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV1L3VbrNew() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~127", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV1L3Cbr128() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1Cbr128.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("128", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV1L3Cbr192() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1Cbr192.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("192", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }


    @Test public void testReadV2L3VbrOld() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV2vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("22050", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~127", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV2L3MonoVbrNew() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV2vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("22050", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~127", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV1L2Stereo() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1L2stereo.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        //assertEquals("00:13", mp3AudioHeader.getTrackLengthAsString()); Incorrectly returning 6
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_II), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_STEREO), mp3AudioHeader.getChannels());
        Assert.assertFalse(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("192", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV1L2Mono() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV1L2mono.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("44100", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:13", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_II), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertFalse(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("192", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV25L3VbrOld() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV25vbrOld0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("12000", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2_5), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~128", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV25L3() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV25.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("12000", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2_5), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("16", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("", mp3AudioHeader.getEncoder());   //No Lame header so blank
    }

    @Test public void testReadV25L3VbrNew() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV25vbrNew0.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("12000", mp3AudioHeader.getSampleRate());
        Assert.assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2_5), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_MONO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("~128", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.96r", mp3AudioHeader.getEncoder());
    }

    @Test public void testReadV2L2() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV2L2.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("24000", mp3AudioHeader.getSampleRate());

        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_II), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_JOINT_STEREO), mp3AudioHeader.getChannels());
        //assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString()); not working returning 0
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertTrue(mp3AudioHeader.isProtected());
        Assert.assertEquals("16", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("", mp3AudioHeader.getEncoder());   //No Lame header so blank
    }

    @Test public void testReadV2L3Stereo() {
        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("testV2L3Stereo.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
        Assert.assertEquals("24000", mp3AudioHeader.getSampleRate());
        //assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString());
        Assert.assertFalse(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_2), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_JOINT_STEREO), mp3AudioHeader.getChannels());
        //assertEquals("00:14", mp3AudioHeader.getTrackLengthAsString()); not working returning 0
        Assert.assertTrue(mp3AudioHeader.isOriginal());
        Assert.assertFalse(mp3AudioHeader.isCopyrighted());
        Assert.assertFalse(mp3AudioHeader.isPrivate());
        Assert.assertFalse(mp3AudioHeader.isProtected());
        Assert.assertEquals("64", mp3AudioHeader.getBitRate());
        Assert.assertEquals("mp3", mp3AudioHeader.getEncodingType());
        Assert.assertEquals("LAME3.97 ", mp3AudioHeader.getEncoder());   //TODO should we be removing trailing space
    }


    /**
     * Test trying to parse an mp3 file which is not a valid MP3 fails gracefully with expected exception
     */
    @Test(expected = InvalidAudioFrameException.class)
    public void testIssue79() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue79.mp3");
        new MP3File(testFile).getMP3AudioHeader();
    }


    /**
     * Test trying to parse an mp3 file which is not a valid MP3 and is extremely small
     * Should fail gracefully
     */
    @Test(expected = InvalidAudioFrameException.class)
    public void testIssue81() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("Issue81.mp3");
        new MP3File(testFile).getMP3AudioHeader();
    }

    /**
     * Test trying to parse an mp3 file which is a valid MP3 but problems with frame
     */
    @Test
    public void testIssue199() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("testV2L2.mp3");
        new MP3File(testFile).getMP3AudioHeader();
    }

    /**
     * Test mp3s display tracks over an hour correctly, dont actually have any such track so have to emulate
     * the mp3 rather than calling it directly.
     */
    @Test public void testIssue85() {
        Exception exceptionCaught = null;

        SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
        SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");
        SimpleDateFormat timeOutOverAnHourFormat = new SimpleDateFormat("kk:mm:ss");

        try {
            int lengthLessThanHour = 3500;
            Date timeIn = timeInFormat.parse(String.valueOf(lengthLessThanHour));
            Assert.assertEquals("58:20", timeOutFormat.format(timeIn));

            int lengthIsAnHour = 3600;
            timeIn = timeInFormat.parse(String.valueOf(lengthIsAnHour));
            Assert.assertEquals("01:00:00", timeOutOverAnHourFormat.format(timeIn));

            int lengthMoreThanHour = 4000;
            timeIn = timeInFormat.parse(String.valueOf(lengthMoreThanHour));
            Assert.assertEquals("01:06:40", timeOutOverAnHourFormat.format(timeIn));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught == null);
    }

    /**
     * Test trying to parse an mp3 file with a ID3 tag header reporting to short causing
     * ealvatag to end up reading mp3 header from too early causing audio header to be
     * read incorrectly
     */
    @Test public void testIssue110() {
        File orig = new File("testdata", "test28.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("test28.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught == null);
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_JOINT_STEREO), mp3AudioHeader.getChannels());
    }

    @Test public void testReadVRBIFrame() {
        File orig = new File("testdata", "test30.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("test30.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        try {
            mp3AudioHeader = new MP3File(testFile).getMP3AudioHeader();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught == null);
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_STEREO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(147, mp3AudioHeader.getBitRateAsNumber());
        Assert.assertEquals("Fraunhofer", mp3AudioHeader.getEncoder());

    }

    @Test public void testWriteToFileWithVRBIFrame() {
        File orig = new File("testdata", "test30.mp3");
        if (!orig.isFile()) {
            return;
        }

        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("test30.mp3");
        MP3AudioHeader mp3AudioHeader = null;
        MP3File mp3file = null;
        try {
            mp3file = new MP3File(testFile);
            mp3AudioHeader = mp3file.getMP3AudioHeader();

            //make change to file
            mp3file.getID3v2Tag().setField(FieldKey.TITLE, "FREDDY");
            mp3file.getID3v2Tag().deleteField(FieldKey.COVER_ART);
            mp3file.getID3v2Tag().removeFrame("PRIV");
            final TagOptionSingleton tagOptions = TagOptionSingleton.getInstance();
            tagOptions.setToDefault();

            mp3file.saveMp3();

            mp3file = new MP3File(testFile);
            mp3AudioHeader = mp3file.getMP3AudioHeader();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        //change has been made and VBRI Frame is left intact
        assertThat(mp3file, is(not(null)));
        //noinspection ConstantConditions
        Assert.assertEquals("FREDDY", mp3file.getID3v2Tag().getFirst(FieldKey.TITLE));

        Assert.assertTrue(exceptionCaught == null);
        Assert.assertEquals(MPEGFrameHeader.mpegVersionMap.get(MPEGFrameHeader.VERSION_1), mp3AudioHeader.getMpegVersion());
        Assert.assertEquals(MPEGFrameHeader.mpegLayerMap.get(MPEGFrameHeader.LAYER_III), mp3AudioHeader.getMpegLayer());
        Assert.assertEquals(MPEGFrameHeader.modeMap.get(MPEGFrameHeader.MODE_STEREO), mp3AudioHeader.getChannels());
        Assert.assertTrue(mp3AudioHeader.isVariableBitRate());
        Assert.assertEquals(147, mp3AudioHeader.getBitRateAsNumber());
        Assert.assertEquals("Fraunhofer", mp3AudioHeader.getEncoder());

    }
}
