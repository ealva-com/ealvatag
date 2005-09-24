/**
 *  Initial @author : Paul Taylor
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.logging.AbstractTagDisplayFormatter;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.audio.AbstractAudioHeader;
import org.jaudiotagger.FileConstants;
import org.jaudiotagger.logging.AbstractTagDisplayFormatter;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/* Represents the audio header of an MP3 File
 * Start of Audio id 0xFF (11111111) and then second byte anded with 0xE0(11100000).
 * For example 2nd byte doesnt have to be 0xE0 is just has to have the top 3 signicant
 * bits set. For example 0xFB (11111011) is a common occurence of the second match. The 2nd byte
 * defines flags to indicate various mp3 values.
*/
public final class MP3AudioHeader extends AbstractAudioHeader
{
    /**
     * Sync Value
     */
    private static final int SYNC_BYTE1 = 0xFF;
    private static final int SYNC_BYTE2 = 0xE0;

    /**
     * Constants for MPEG Version
     */
    private static final HashMap mpegVersionMap = new HashMap();
    private final static int VERSION_2_5 = 0;
    private final static int VERSION_2 = 2;
    private final static int VERSION_1 = 3;

    static
    {
        mpegVersionMap.put(new Integer(VERSION_2_5), "Version 2.5");
        mpegVersionMap.put(new Integer(VERSION_2), "Version 2");
        mpegVersionMap.put(new Integer(VERSION_1), "Version 1");
    }

    /**
     * Constants for MPEG Layer
     */
    private static final HashMap mpegLayerMap = new HashMap();
    private final static int LAYER_I = 3;
    private final static int LAYER_II = 2;
    private final static int LAYER_III = 1;


    static
    {
        mpegLayerMap.put(new Integer(LAYER_I), "Layer I");
        mpegLayerMap.put(new Integer(LAYER_II), "Layer II");
        mpegLayerMap.put(new Integer(LAYER_III), "Layer III");
    }

    /**
     * Slot Size is dependent on Layer
     */
    private final static int LAYER_I_SLOT_SIZE = 4;
    public final static int LAYER_II_SLOT_SIZE = 1;
    public final static int LAYER_III_SLOT_SIZE = 1;

    /**
     * Bit Rates
     */
    private static final HashMap bitrateMap = new HashMap();

    static
    {
        // MPEG-1, Layer I (E)
        bitrateMap.put(new Integer(0x1E), new Integer(32));
        bitrateMap.put(new Integer(0x2E), new Integer(64));
        bitrateMap.put(new Integer(0x3E), new Integer(96));
        bitrateMap.put(new Integer(0x4E), new Integer(128));
        bitrateMap.put(new Integer(0x5E), new Integer(160));
        bitrateMap.put(new Integer(0x6E), new Integer(192));
        bitrateMap.put(new Integer(0x7E), new Integer(224));
        bitrateMap.put(new Integer(0x8E), new Integer(256));
        bitrateMap.put(new Integer(0x9E), new Integer(288));
        bitrateMap.put(new Integer(0xAE), new Integer(320));
        bitrateMap.put(new Integer(0xBE), new Integer(352));
        bitrateMap.put(new Integer(0xCE), new Integer(384));
        bitrateMap.put(new Integer(0xDE), new Integer(416));
        bitrateMap.put(new Integer(0xEE), new Integer(448));
        // MPEG-1, Layer II (C)
        bitrateMap.put(new Integer(0x1C), new Integer(32));
        bitrateMap.put(new Integer(0x2C), new Integer(48));
        bitrateMap.put(new Integer(0x3C), new Integer(56));
        bitrateMap.put(new Integer(0x4C), new Integer(64));
        bitrateMap.put(new Integer(0x5C), new Integer(80));
        bitrateMap.put(new Integer(0x6C), new Integer(96));
        bitrateMap.put(new Integer(0x7C), new Integer(112));
        bitrateMap.put(new Integer(0x8C), new Integer(128));
        bitrateMap.put(new Integer(0x9C), new Integer(160));
        bitrateMap.put(new Integer(0xAC), new Integer(192));
        bitrateMap.put(new Integer(0xBC), new Integer(224));
        bitrateMap.put(new Integer(0xCC), new Integer(256));
        bitrateMap.put(new Integer(0xDC), new Integer(320));
        bitrateMap.put(new Integer(0xEC), new Integer(384));
        // MPEG-1, Layer III (A)
        bitrateMap.put(new Integer(0x1A), new Integer(32));
        bitrateMap.put(new Integer(0x2A), new Integer(40));
        bitrateMap.put(new Integer(0x3A), new Integer(48));
        bitrateMap.put(new Integer(0x4A), new Integer(56));
        bitrateMap.put(new Integer(0x5A), new Integer(64));
        bitrateMap.put(new Integer(0x6A), new Integer(80));
        bitrateMap.put(new Integer(0x7A), new Integer(96));
        bitrateMap.put(new Integer(0x8A), new Integer(112));
        bitrateMap.put(new Integer(0x9A), new Integer(128));
        bitrateMap.put(new Integer(0xAA), new Integer(160));
        bitrateMap.put(new Integer(0xBA), new Integer(192));
        bitrateMap.put(new Integer(0xCA), new Integer(224));
        bitrateMap.put(new Integer(0xDA), new Integer(256));
        bitrateMap.put(new Integer(0xEA), new Integer(320));
        // MPEG-2, Layer I (6)
        bitrateMap.put(new Integer(0x16), new Integer(32));
        bitrateMap.put(new Integer(0x26), new Integer(48));
        bitrateMap.put(new Integer(0x36), new Integer(56));
        bitrateMap.put(new Integer(0x46), new Integer(64));
        bitrateMap.put(new Integer(0x56), new Integer(80));
        bitrateMap.put(new Integer(0x66), new Integer(96));
        bitrateMap.put(new Integer(0x76), new Integer(112));
        bitrateMap.put(new Integer(0x86), new Integer(128));
        bitrateMap.put(new Integer(0x96), new Integer(144));
        bitrateMap.put(new Integer(0xA6), new Integer(160));
        bitrateMap.put(new Integer(0xB6), new Integer(176));
        bitrateMap.put(new Integer(0xC6), new Integer(192));
        bitrateMap.put(new Integer(0xD6), new Integer(224));
        bitrateMap.put(new Integer(0xE6), new Integer(256));
        // MPEG-2, Layer II (4)
        bitrateMap.put(new Integer(0x14), new Integer(8));
        bitrateMap.put(new Integer(0x24), new Integer(16));
        bitrateMap.put(new Integer(0x34), new Integer(24));
        bitrateMap.put(new Integer(0x44), new Integer(32));
        bitrateMap.put(new Integer(0x54), new Integer(40));
        bitrateMap.put(new Integer(0x64), new Integer(48));
        bitrateMap.put(new Integer(0x74), new Integer(56));
        bitrateMap.put(new Integer(0x84), new Integer(64));
        bitrateMap.put(new Integer(0x94), new Integer(80));
        bitrateMap.put(new Integer(0xA4), new Integer(96));
        bitrateMap.put(new Integer(0xB4), new Integer(112));
        bitrateMap.put(new Integer(0xC4), new Integer(128));
        bitrateMap.put(new Integer(0xD4), new Integer(144));
        bitrateMap.put(new Integer(0xE4), new Integer(160));
        // MPEG-2, Layer III (2)
        bitrateMap.put(new Integer(0x12), new Integer(8));
        bitrateMap.put(new Integer(0x22), new Integer(16));
        bitrateMap.put(new Integer(0x32), new Integer(24));
        bitrateMap.put(new Integer(0x42), new Integer(32));
        bitrateMap.put(new Integer(0x52), new Integer(40));
        bitrateMap.put(new Integer(0x62), new Integer(48));
        bitrateMap.put(new Integer(0x72), new Integer(56));
        bitrateMap.put(new Integer(0x82), new Integer(64));
        bitrateMap.put(new Integer(0x92), new Integer(80));
        bitrateMap.put(new Integer(0xA2), new Integer(96));
        bitrateMap.put(new Integer(0xB2), new Integer(112));
        bitrateMap.put(new Integer(0xC2), new Integer(128));
        bitrateMap.put(new Integer(0xD2), new Integer(144));
        bitrateMap.put(new Integer(0xE2), new Integer(160));
    }

    /**
     * Constants for Channel mode
     */
    private static final HashMap modeMap = new HashMap();
    private final static int MODE_STEREO = 0;
    private final static int MODE_JOINT_STEREO = 1;
    private final static int MODE_DUAL_CHANNEL = 2;
    private final static int MODE_MONO = 3;

    static
    {
        modeMap.put(new Integer(MODE_STEREO), "Stereo");
        modeMap.put(new Integer(MODE_JOINT_STEREO), "Joint Stereo");
        modeMap.put(new Integer(MODE_DUAL_CHANNEL), "Dual");
        modeMap.put(new Integer(MODE_MONO), "Mono");
    }


    /**
     * Constants for emphasis
     */
    private static final HashMap emphasisMap = new HashMap();
    private final static int EMPHASIS_NONE = 0;
    private final static int EMPHASIS_5015MS = 1;
    private final static int EMPHASIS_RESERVED = 2;
    private final static int EMPHASIS_CCITT = 3;

    static
    {
        emphasisMap.put(new Integer(EMPHASIS_NONE), "None");
        emphasisMap.put(new Integer(EMPHASIS_5015MS), "5015MS");
        emphasisMap.put(new Integer(EMPHASIS_RESERVED), "Reserved");
        emphasisMap.put(new Integer(EMPHASIS_CCITT), "CCITT");
    }

    /**
     * Constant for frequency
     */
    private static final HashMap frequencyMap = new HashMap();
    private static final HashMap frequencyV1Map = new HashMap();
    private static final HashMap frequencyV2Map = new HashMap();
    private static final HashMap frequencyV25Map = new HashMap();

    static
    {
        frequencyV1Map.put(new Integer(0), new Float(44.1));
        frequencyV1Map.put(new Integer(1), new Float(48.0));
        frequencyV1Map.put(new Integer(2), new Float(32.0));

        frequencyV2Map.put(new Integer(0), new Float(22.05));
        frequencyV2Map.put(new Integer(1), new Float(24.00));
        frequencyV2Map.put(new Integer(2), new Float(16.00));

        frequencyV25Map.put(new Integer(0), new Float(11.025));
        frequencyV25Map.put(new Integer(1), new Float(12.00));
        frequencyV25Map.put(new Integer(2), new Float(8.00));

        frequencyMap.put(new Integer(VERSION_1), frequencyV1Map);
        frequencyMap.put(new Integer(VERSION_2), frequencyV2Map);
        frequencyMap.put(new Integer(VERSION_2_5), frequencyV25Map);
    }


    /**
     * Constants for MP3 Header
     */
    private static final int BYTE_1 = 0;
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;
    private static final int HEADER_SIZE = 4;

    private static final SimpleDateFormat timeInFormat = new SimpleDateFormat("ss");
    private static final SimpleDateFormat timeOutFormat = new SimpleDateFormat("mm:ss");

    /**
     * MP3 Frame Header bit mask
     */
    private static final int MASK_MP3_ID = FileConstants.BIT3;

    /**
     * MP3 version, confusingly for MP3s the version is 1.
     */
    private static final int MASK_MP3_VERSION = FileConstants.BIT4 | FileConstants.BIT3;

    /**
     * MP3 Layer, for MP3s the Layer is 3
     */
    private static final int MASK_MP3_LAYER = FileConstants.BIT2 | FileConstants.BIT1;

    /**
     * Does it include a CRC Checksum at end of header, this can be used to check the header.
     */
    private static final int MASK_MP3_PROTECTION = FileConstants.BIT0;

    /**
     * The bitrate of this MP3
     */
    private static final int MASK_MP3_BITRATE = FileConstants.BIT7 | FileConstants.BIT6 | FileConstants.BIT5 |
        FileConstants.BIT4;

    /**
     * The sampling/frequency rate
     */
    private static final int MASK_MP3_FREQUENCY = FileConstants.BIT3 + FileConstants.BIT2;

    /**
     * An extra padding bit is sometimes used to make sure frames are exactly the right length
     */
    private static final int MASK_MP3_PADDING = FileConstants.BIT1;

    /**
     * Private bit set, for application specific
     */
    private static final int MASK_MP3_PRIVACY = FileConstants.BIT0;

    /**
     * Channel Mode, Stero/Mono/Dual Channel
     */
    private static final int MASK_MP3_MODE = FileConstants.BIT7 | FileConstants.BIT6;

    /**
     * MP3 Frame Header bit mask
     */
    private static final int MASK_MP3_MODE_EXTENSION = FileConstants.BIT5 | FileConstants.BIT4;

    /**
     * MP3 Frame Header bit mask
     */
    private static final int MASK_MP3_COPY = FileConstants.BIT3;

    /**
     * MP3 Frame Header bit mask
     */
    private static final int MASK_MP3_HOME = FileConstants.BIT2;

    /**
     * Emphasis
     */
    private static final int MASK_MP3_EMPHASIS = FileConstants.BIT1 | FileConstants.BIT0;

    private long fileSize;
    private long startByte;

    /**
     * value read from the MP3 Frame header
     */
    private boolean copyProtected;

    /**
     * Copy or Original
     */
    private boolean home;

    /**
     * Padding to make frame sthe same size
     */
    private boolean padding;

    /**
     * Private Set
     */
    private boolean privacy;

    /**
     * CRC Checksum
     */
    private boolean protection;

    /**
     * Uses VBR
     */
    private boolean variableBitRate = false;

    /**
     * Emphasis
     */
    private byte emphasis;

    /**
     * value read from the MP3 Frame header
     */
    private byte layer;
    private String mpegLayerAsString;

    /**
     * Mode
     */
    private byte mode;

    /**
     * Mode Extension ?
     */
    private byte modeExtension;

    /**
     * MPEG Version (1,2,2.5)
     */
    private byte mpegVersion;
    private String mpegVersionAsString;


    /**
     * Frequency determined from MP3 Version and frequency value read from the
     * MP3 Frame header
     */
    private float frequency;

    /**
     * Bitrate calculated from the frame MP3 Frame header, bitRate List is used to
     * allow more accurate calculation of Varaible Bit Rates.
     */
    private int bitRate;
    private final ArrayList bitRateList = new ArrayList();

    /**
     * Create Audioheader from another AudioHeader
     */
    public MP3AudioHeader(MP3AudioHeader copyObject)
    {
        this.copyProtected = copyObject.copyProtected;
        this.home = copyObject.home;
        this.padding = copyObject.padding;
        this.privacy = copyObject.privacy;
        this.protection = copyObject.protection;
        this.variableBitRate = copyObject.variableBitRate;
        this.emphasis = copyObject.emphasis;
        this.layer = copyObject.layer;
        this.mode = copyObject.mode;
        this.modeExtension = copyObject.modeExtension;
        this.mpegVersion = copyObject.mpegVersion;
        this.frequency = copyObject.frequency;
        this.bitRate = copyObject.bitRate;
    }

    /**
     * Read Audio Header from File
     */
    public MP3AudioHeader(RandomAccessFile seekFile)
        throws IOException
    {
        fileSize = seekFile.length();
        this.seek(seekFile);
    }

    /**
     * Does this use a variable bit rate
     *
     * @param variableBitRate DOCUMENT ME!
     */
    public final void setVariableBitRate(boolean variableBitRate)
    {
        this.variableBitRate = variableBitRate;
    }

    /**
     * Does this use a variable bit rate
     *
     * @return DOCUMENT ME!
     */
    public final boolean isVariableBitRate()
    {
        return variableBitRate;
    }

    /**
     * What is the bitrate for the frame
     *
     * @return DOCUMENT ME!
     */
    private int getBitRate()
    {
        return bitRate;
    }

    /**
     * For non VBR return bit rate, for VBR return range because not sampling enough headers
     */
    public final String getBitRateAsString()
    {
        if (isVariableBitRate() == false)
        {
            return String.valueOf(getBitRate());
        }
        else
        {
            Collections.sort(bitRateList);
            return "~" + String.valueOf(bitRateList.get(0)) + " - " + String.valueOf(bitRateList.get(bitRateList.size() - 1));
        }
    }

    /**
     * Return average bit Rate, for VBR this is not vaery accurate.
     *
     * @return
     */
    public final int getAverageBitRate()
    {
        if (isVariableBitRate() == false)
        {
            return getBitRate();
        }
        else
        {
            int averageBitRate = 0;
            for (ListIterator li = bitRateList.listIterator(); li.hasNext();)
            {
                averageBitRate = averageBitRate + ((Integer) li.next()).intValue();
            }
            return averageBitRate / bitRateList.size();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isCopyProtected()
    {
        return copyProtected;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final byte getEmphasis()
    {
        return emphasis;
    }

    /**
     * What is the recording frequency, depends partially on MPEG Version
     *
     * @return DOCUMENT ME!
     */
    public final double getFrequency()
    {
        return frequency;
    }

    /**
     * If this is true then this is the original file, if false it is
     * a copy of the file.
     *
     * @return DOCUMENT ME!
     */
    public final boolean isHome()
    {
        return home;
    }

    /**
     * Get MP3 Layer
     *
     * @return DOCUMENT ME!
     */
    public final byte getLayer()
    {
        return layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final byte getMode()
    {
        return mode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final byte getModeExtension()
    {
        return modeExtension;
    }

    /**
     * Get MPEG Version
     *
     * @return DOCUMENT ME!
     */
    public final byte getMpegVersion()
    {
        return mpegVersion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean isPadding()
    {
        return padding;
    }

    /**
     * Is it private
     *
     * @return DOCUMENT ME!
     */
    public final boolean isPrivacy()
    {
        return privacy;
    }

    /**
     * Is it protected
     *
     * @return DOCUMENT ME!
     */
    public final boolean isProtection()
    {
        return protection;
    }

    /**
     * Returns true if the first MP3 frame can be found for the MP3 file
     * argument. It tries to sync as many frame as defined in
     * <code>TagOptions.getNumberMP3SyncFrame</code> This is the first byte of
     * music data and not the ID3 Tag Frame.
     *
     * @param seekFile MP3 file to seek
     * @return true if the first MP3 frame can be found
     * @throws IOException on any I/O error
     */
    public final boolean seek(RandomAccessFile seekFile)
        throws IOException
    {
        boolean syncFound = false;
        byte first;
        byte second;
        long filePointer = 1;
        variableBitRate = false;
        try
        {
            seekFile.seek(0);
            do
            {
                first = seekFile.readByte();
                if (first == (byte) SYNC_BYTE1)
                {
                    filePointer = seekFile.getFilePointer();
                    second = (byte) (seekFile.readByte() & (byte) SYNC_BYTE2);
                    if (second == (byte) SYNC_BYTE2)
                    {
                        MP3File.logger.finest("Found matching mp3 header starting at" + filePointer);
                        //Return to just before the matching id
                        seekFile.seek(filePointer - 1);
                        /** Seek for the matching mp3 headers this is
                         ** done to ensure it is a valid mp3 file
                         */
                        syncFound = seekNextMP3Frame(seekFile,
                            TagOptionSingleton.getInstance().getNumberMP3SyncFrame());
                    }
                    seekFile.seek(filePointer);
                }
            }
            while (syncFound == false);
            seekFile.seek(filePointer - 1);
        }
        catch (EOFException ex)
        {
            syncFound = false;
        }
        catch (IOException ex)
        {
            syncFound = false;
            throw ex;
        }
        //Return to start of audio header
        MP3File.logger.finest("Return found matching mp3 header starting at" + seekFile.getFilePointer());
        startByte = seekFile.getFilePointer();
        return syncFound;
    }

    /**
     * Returns the byte position of the first MP3 Frame that the
     * <code>file</code> arguement refers to. This is the first byte of music
     * data and not the ID3 Tag Frame.
     *
     * @param file MP3 file to search
     * @return the byte position of the first MP3 Frame
     * @throws IOException           on any I/O error
     * @throws FileNotFoundException if the file exists but is a directory
     *                               rather than a regular file or cannot be opened for any other
     *                               reason
     */
    public final long getMp3StartByte()

    {
        return startByte;
    }

    /**
     * Returns the MP3 frame size for the frame this datatype refers to. Individual
     * Frames within an MP3 can vary in size. It assumes that <code>seekNextMP3Frame</code>
     * has already been called.
     *
     * @return MP3 Frame size in bytes.
     */
    private int getFrameSize()
    {
        if (this.frequency == 0)
        {
            return 0;
        }
        int size = 0;
        int paddingByte;
        if (padding)
        {
            paddingByte = 1;
        }
        else
        {
            paddingByte = 0;
        }
        if (this.layer == LAYER_I)
        {
            // Layer I
            size = (int) ((((12 * this.bitRate) / this.frequency) + paddingByte) * LAYER_I_SLOT_SIZE);
        }
        else
        {
            // Layer II & Layer III, Slot Size is 1 so dont need to multiply by slot size
            size = (int) (((144 * this.bitRate) / this.frequency) + paddingByte);
        }
        //if (protection) size += 2;
        return size;
    }

    /**
     * Reads the mp3 frame header from the current position in the file and
     * sets this datatype's private variables to what is found. It assumes the
     * <code>RandomAccessFile</code> is already pointing to a valid MP3 Frame.
     *
     * @param file File to read frame header
     * @throws IOException          on any I/O error
     * @throws TagNotFoundException if MP3 Frame sync bites were not
     *                              immediately found
     * @throws InvalidTagException  if any of the header values are invlaid
     */
    private void readFrameHeader(RandomAccessFile file)
        throws IOException, TagNotFoundException, InvalidTagException
    {
        byte[] buffer = new byte[HEADER_SIZE];
        file.read(buffer);

        MP3File.logger.info("Header" +
            AbstractTagDisplayFormatter.displayAsBinary(buffer[BYTE_1]) +
            AbstractTagDisplayFormatter.displayAsBinary(buffer[BYTE_2]) +
            AbstractTagDisplayFormatter.displayAsBinary(buffer[BYTE_3]) +
            AbstractTagDisplayFormatter.displayAsBinary(buffer[BYTE_4]));

        // Sync Check
        if ((buffer[BYTE_1] != (byte) SYNC_BYTE1) || ((buffer[BYTE_2] & (byte) SYNC_BYTE2) != (byte) SYNC_BYTE2))
        {
            throw new TagNotFoundException("MP3 Frame sync bits not found");
        }

        //MPEG Version
        mpegVersion = (byte) ((buffer[BYTE_2] & MASK_MP3_VERSION) >> 3);

        if ((mpegVersionAsString = (String) mpegVersionMap.get(new Integer(mpegVersion))) == null)
        {
            throw new InvalidTagException("Invalid mpeg version");
        }

        //MPEG Layer
        layer = (byte) ((buffer[BYTE_2] & MASK_MP3_LAYER) >> 1);
        if ((mpegLayerAsString = (String) mpegLayerMap.get(new Integer(layer))) == null)
        {
            throw new InvalidTagException("Invalid mpeg layer");
        }

        //Protection:Does it have a CRC Checksum
        this.protection = (buffer[BYTE_2] & MASK_MP3_PROTECTION) != 1;

        /* BitRate, get by checking header bitrate bits and MPEG Version and Layer, we work
         * out whether a particular MP3 uses VBR (Variable Bit Rate) by comparing the
         * bitRate with the bitRate of the previous frame (if there was one), if they
         * are different we have a variable bit rate */
        int bitRateValue = (buffer[BYTE_3] & MASK_MP3_BITRATE) | (buffer[BYTE_2] & MASK_MP3_ID) | (buffer[BYTE_2] & MASK_MP3_LAYER);
        Integer newBitRate = (Integer) bitrateMap.get(new Integer(bitRateValue));
        if (newBitRate != null)
        {
            if (bitRateList.size() > 0)
            {
                if (!bitRateList.contains(newBitRate))
                {
                    this.variableBitRate = true;
                }
            }
            bitRateList.add(newBitRate);
            this.bitRate = newBitRate.intValue();
        }
        else
        {
            throw new InvalidTagException("Invalid bit rate");
        }

        //Frequency
        int frequencyVal = (buffer[BYTE_3] & MASK_MP3_FREQUENCY) >>> 2;
        try
        {
            this.frequency = ((Float) (((HashMap) frequencyMap.get(new Integer(mpegVersion))).get(new Integer(frequencyVal)))).floatValue();
        }
        catch (NullPointerException npe)
        {
            throw new InvalidTagException("Invalid frequency");
        }

        //Padding
        this.padding = (buffer[BYTE_3] & MASK_MP3_PADDING) != 0;

        //Privacy
        this.privacy = (buffer[BYTE_3] & MASK_MP3_PRIVACY) != 0;

        //Channel Mode
        this.mode = (byte) ((buffer[BYTE_4] & MASK_MP3_MODE) >> 6);

        //Channel Mode Extension ?
        this.modeExtension = (byte) ((buffer[BYTE_4] & MASK_MP3_MODE_EXTENSION) >> 4);

        //Copy Protection
        this.copyProtected = (buffer[BYTE_4] & MASK_MP3_COPY) != 0;

        //Home, is it original or copy
        this.home = (buffer[BYTE_4] & MASK_MP3_HOME) != 0;

        //Emphasis, volume adjustment recommendations
        this.emphasis = (byte) ((buffer[BYTE_4] & MASK_MP3_EMPHASIS));
    }

    /**
     * Return the mode, stero options
     */
    public final String getModeAsString()
    {
        return (String) modeMap.get(new Integer(mode));
    }

    /**
     * Return the MPEG Layer
     */
    public final String getMpegLayerAsString()
    {
        return mpegLayerAsString;
    }

    /**
     * Return the MPEG Version
     */
    public final String getMpegVersionAsString()
    {
        return mpegVersionAsString;
    }

    /**
     * Return the Frequency
     */
    public final String getFrequencyAsString()
    {
        return String.valueOf(frequency);
    }

    /**
     * Estimate the length of the audio track in seconds, for VBR because we cant get an accurate
     * value just assume bit rate of 180.
     * TODO dont understand scaling that gives us 0.008
     */
    private long getTrackLength()
    {
        if (isVariableBitRate() == false)
        {
            return (long) Math.floor((fileSize - startByte) / bitRate * 0.008);
        }
        else
        {
            return (long) Math.floor((fileSize - startByte) / 180 * 0.008);
        }
    }

    /**
     * Return the length in user friendly format
     */
    public final String getTrackLengthAsString()
    {
        try
        {
            long lengthInSecs = getTrackLength();
            Date timeIn = timeInFormat.parse(String.valueOf(lengthInSecs));
            return timeOutFormat.format(timeIn);
        }
        catch (Exception e)
        {
            return "";
        }

    }

    /**
     * Return the emphasis settings
     */
    public final String getEmphasisAsString()
    {
        return (String) emphasisMap.get(new Integer(emphasis));
    }

    /**
     * Returns true if the first MP3 frame can be found for the MP3 file
     * argument. It is recursive and calls itelf until read a number of
     * MP3 frames (defined in TagOptions) to guarantee this is a valid MP3 File.
     *
     * @param file       MP3 file to seek
     * @param iterations recursive counter
     * @return true if the first MP3 frame can be found
     * @throws IOException on any I/O error
     */
    private boolean seekNextMP3Frame(RandomAccessFile file, int iterations)
        throws IOException
    {
        MP3File.logger.finer("Seeking next frame at:" + file.getFilePointer());
        boolean syncFound = false;
        byte[] buffer;
        byte first;
        byte second;
        long filePointer;
        if (iterations == 0)
        {
            syncFound = true;
        }
        else
        {
            try
            {
                this.readFrameHeader(file);
            }
            catch (TagException ex)
            {
                MP3File.logger.finer("Unable to find header" + ex.getMessage());
                return false;
            }
            int size = getFrameSize();
            if ((size <= 0) || (size > file.length()))
            {
                MP3File.logger.finer("Unable to find header:size is invalid");
                return false;
            }
            MP3File.logger.finer("Found Header:" + iterations);
            iterations--;
            if (iterations == 0)
            {
                return true;
            }

            //Ok find next header, step along to the end of this frame as given by frame size
            buffer = new byte[size - HEADER_SIZE];
            file.read(buffer);
            filePointer = file.getFilePointer();
            first = file.readByte();
            if (first == (byte) SYNC_BYTE1)
            {
                second = (byte) (file.readByte() & (byte) SYNC_BYTE2);
                if (second == (byte) SYNC_BYTE2)
                {
                    file.seek(filePointer);
                    // recursively find the next frames
                    MP3File.logger.finer("Found Match iteration:" + iterations);
                    syncFound = seekNextMP3Frame(file, iterations);
                }
                else
                {
                    syncFound = false;
                }
            }
            else
            {
                syncFound = false;
            }
        }
        return syncFound;
    }
}
