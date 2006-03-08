/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * Date :${DATE}
 * <p/>
 * Jaikoz Copyright Copyright (C) 2003 -2005 JThink Ltd
 */
package org.jaudiotagger.audio.mp3;

import org.jaudiotagger.FileConstants;
import org.jaudiotagger.audio.InvalidAudioFrameException;
import org.jaudiotagger.logging.AbstractTagDisplayFormatter;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.TagNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;


/**
 * Represents a MPEGFrameHeader, an MP3 is made up of a number of frames each frame starts witha four
 * byte frame header.
 */
public class MPEGFrameHeader
{
     /**
     * Constants for MP3 Frame header, each frame has a basic header of
     * 4 bytes
     */
    private static final int BYTE_1 = 0;
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;
    public static final int HEADER_SIZE = 4;

     /**
     * Sync Value to identify the start of an MPEGFrame
     */
    public static final int SYNC_SIZE = 2;

    public static final int SYNC_BYTE1 = 0xFF;
    public static final int SYNC_BYTE2 = 0xE0;

    private static int position     = 0;
    private static final byte[] header = new byte[HEADER_SIZE];


    /**
     * Constants for MPEG Version
     */
    private static final Map mpegVersionMap = new HashMap();
    public final static int VERSION_2_5 = 0;
    public final static int VERSION_2 = 2;
    public final static int VERSION_1 = 3;

    static
    {
        mpegVersionMap.put(new Integer(VERSION_2_5), "Version 2.5");
        mpegVersionMap.put(new Integer(VERSION_2)  , "Version 2");
        mpegVersionMap.put(new Integer(VERSION_1)  , "Version 1");
    }

    /**
     * Constants for MPEG Layer
     */
    private static final Map mpegLayerMap = new HashMap();
    public final static int LAYER_I   = 3;
    public final static int LAYER_II  = 2;
    public final static int LAYER_III = 1;

    static
    {
        mpegLayerMap.put(new Integer(LAYER_I), "Layer I");
        mpegLayerMap.put(new Integer(LAYER_II), "Layer II");
        mpegLayerMap.put(new Integer(LAYER_III), "Layer III");
    }

    /**
     * Slot Size is dependent on Layer
     */
    public final static int LAYER_I_SLOT_SIZE = 4;
    public final static int LAYER_II_SLOT_SIZE = 1;
    public final static int LAYER_III_SLOT_SIZE = 1;

    /**
     * Bit Rates, the setBitrate varies for different Version and Layer
     */
    private static final Map bitrateMap = new HashMap();

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
    private static final Map modeMap = new HashMap();
    public final static int MODE_STEREO = 0;
    public final static int MODE_JOINT_STEREO = 1;
    public final static int MODE_DUAL_CHANNEL = 2;
    public final static int MODE_MONO = 3;

    static
    {
        modeMap.put(new Integer(MODE_STEREO), "Stereo");
        modeMap.put(new Integer(MODE_JOINT_STEREO), "Joint Stereo");
        modeMap.put(new Integer(MODE_DUAL_CHANNEL), "Dual");
        modeMap.put(new Integer(MODE_MONO), "Mono");
    }

    /**
     * Constants for Emphasis
     */
    private static final Map emphasisMap = new HashMap();
    public final static int EMPHASIS_NONE = 0;
    public final static int EMPHASIS_5015MS = 1;
    public final static int EMPHASIS_RESERVED = 2;
    public final static int EMPHASIS_CCITT = 3;

    static
    {
        emphasisMap.put(new Integer(EMPHASIS_NONE), "None");
        emphasisMap.put(new Integer(EMPHASIS_5015MS), "5015MS");
        emphasisMap.put(new Integer(EMPHASIS_RESERVED), "Reserved");
        emphasisMap.put(new Integer(EMPHASIS_CCITT), "CCITT");
    }


    private static final Map modeExtensionMap = new HashMap();
    private final static int MODE_EXTENSION_NONE = 0;
    private final static int MODE_EXTENSION_ONE = 1;
    private final static int MODE_EXTENSION_TWO = 2;
    private final static int MODE_EXTENSION_THREE = 3;

    private static final Map modeExtensionLayerIIIMap = new HashMap();
    private final static int MODE_EXTENSION_OFF_OFF = 0;
    private final static int MODE_EXTENSION_ON_OFF = 1;
    private final static int MODE_EXTENSION_OFF_ON = 2;
    private final static int MODE_EXTENSION_ON_ON = 3;

    static
    {
        modeExtensionMap.put(new Integer(MODE_EXTENSION_NONE), "4-31");
        modeExtensionMap.put(new Integer(MODE_EXTENSION_ONE), "8-31");
        modeExtensionMap.put(new Integer(MODE_EXTENSION_TWO), "12-31");
        modeExtensionMap.put(new Integer(MODE_EXTENSION_THREE), "16-31");

        modeExtensionLayerIIIMap.put(new Integer(MODE_EXTENSION_OFF_OFF), "off-off");
        modeExtensionLayerIIIMap.put(new Integer(MODE_EXTENSION_ON_OFF), "on-off");
        modeExtensionLayerIIIMap.put(new Integer(MODE_EXTENSION_OFF_ON), "off-on");
        modeExtensionLayerIIIMap.put(new Integer(MODE_EXTENSION_ON_ON), "on-on");
    }

    /**
     * Sampling Rate in Hz
     */
    private static final Map samplingRateMap = new HashMap();
    private static final Map samplingV1Map = new HashMap();
    private static final Map samplingV2Map = new HashMap();
    private static final Map samplingV25Map = new HashMap();

    static
    {
        samplingV1Map.put(new Integer(0), new Integer(44100));
        samplingV1Map.put(new Integer(1), new Integer(48000));
        samplingV1Map.put(new Integer(2), new Integer(32000));

        samplingV2Map.put(new Integer(0), new Integer(22050));
        samplingV2Map.put(new Integer(1), new Integer(24000));
        samplingV2Map.put(new Integer(2), new Integer(16000));

        samplingV25Map.put(new Integer(0), new Integer(11025));
        samplingV25Map.put(new Integer(1), new Integer(12000));
        samplingV25Map.put(new Integer(2), new Integer(8000));

        samplingRateMap.put(new Integer(VERSION_1), samplingV1Map);
        samplingRateMap.put(new Integer(VERSION_2), samplingV2Map);
        samplingRateMap.put(new Integer(VERSION_2_5), samplingV25Map);
    }

    /* Samples Per Frame */
    private static final Map samplesPerFrameMap = new HashMap();
    private static final Map samplesPerFrameV1Map = new HashMap();
    private static final Map samplesPerFrameV2Map = new HashMap();
    private static final Map samplesPerFrameV25Map = new HashMap();
    static
    {
        samplesPerFrameV1Map.put(new Integer(LAYER_I)   , new Integer(384));
        samplesPerFrameV1Map.put(new Integer(LAYER_II)  , new Integer(1152));
        samplesPerFrameV1Map.put(new Integer(LAYER_III) , new Integer(1152));

        samplesPerFrameV1Map.put(new Integer(LAYER_I)   , new Integer(384));
        samplesPerFrameV2Map.put(new Integer(LAYER_II)  , new Integer(1152));
        samplesPerFrameV2Map.put(new Integer(LAYER_III) , new Integer(1152));

        samplesPerFrameV25Map.put(new Integer(LAYER_I)  , new Integer(384));
        samplesPerFrameV25Map.put(new Integer(LAYER_II) , new Integer(1152));
        samplesPerFrameV25Map.put(new Integer(LAYER_III), new Integer(1152));

        samplesPerFrameMap.put(new Integer(VERSION_1)  , samplesPerFrameV1Map);
        samplesPerFrameMap.put(new Integer(VERSION_2)  , samplesPerFrameV2Map);
        samplesPerFrameMap.put(new Integer(VERSION_2_5), samplesPerFrameV25Map);

    }




    private static final int SCALE_BY_THOUSAND = 1000;
    private static final int LAYER_I_FRAME_SIZE_COEFFICIENT   = 12;
    private static final int LAYER_II_FRAME_SIZE_COEFFICIENT  = 144;
    private static final int LAYER_III_FRAME_SIZE_COEFFICIENT = 144;

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
     * The setBitrate of this MP3
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
     * MP3 Frame Header bit mask
     */
    private static final int MASK_MP3_EMPHASIS = FileConstants.BIT1 | FileConstants.BIT0;


    private byte[] mpegBytes;

    /**
     * The version of this MPEG frame (see the constants)
     */
    private int version;

    private String versionAsString;

    /**
     * Contains the mpeg layer of this frame (see constants)
     */
    private int layer;

    private String layerAsString;
    /**
     * Bitrate of this frame
     */
    private Integer bitRate;

    /**
     * Channel Mode of this Frame (see constants)
     */
    private int channelMode;

    /**
     * Channel Mode of this Frame As English String
     */
    private String channelModeAsString;

    /**
     * Emphasis of this frame
     */
    private int emphasis;

    /**
     *
     * Emphasis mode string
     */
    private String emphasisAsString;

    /**
     * Mode Extension
     */
    private String modeExtension;

    /**
     * Flag indicating if this frame has padding byte
     */
    private boolean isPadding;

    /**
     * Flag indicating if this frame contains copyrighted material
     */
    private boolean isCopyrighted;

    /**
     * Flag indicating if this frame contains original material
     */
    private boolean isOriginal;

    /**
     * Flag indicating if this frame is protected
     */
    private boolean isProtected;


    /**
     * Flag indicating if this frame is private
     */
    private boolean isPrivate;

    private Integer samplingRate;


    /**
     * Gets the layerVersion attribute of the MPEGFrame object
     *
     * @return The layerVersion value
     */
    public int getLayer()
    {
        return layer;
    }

    public String getLayerAsString()
    {
        return layerAsString;
    }

    /**
     * Gets the copyrighted attribute of the MPEGFrame object
     */
    private void setCopyrighted()
    {
        isCopyrighted = (mpegBytes[BYTE_4] & MASK_MP3_COPY) != 0;
    }


    /**
     * Set the version of this frame as an int value (see constants)
     */
    private void setVersion() throws InvalidAudioFrameException
    {
        //MPEG Version
        version = (byte) ((mpegBytes[BYTE_2] & MASK_MP3_VERSION) >> 3);
        versionAsString = (String)mpegVersionMap.get(new Integer(version));
        if ( versionAsString == null)
        {
            throw new InvalidAudioFrameException("Invalid mpeg version");
        }
    }

    /**
     * Sets the original attribute of the MPEGFrame object
     */
    private void setOriginal()
    {
        isOriginal = (mpegBytes[BYTE_4] & MASK_MP3_HOME) != 0;
    }

    /**
     * Sets the protected attribute of the MPEGFrame object
     */
    private void setProtected()
    {
        isProtected = (mpegBytes[BYTE_2] & MASK_MP3_PROTECTION) == 0x00;
    }

    /**
     * Sets the private attribute of the MPEGFrame object
     */
    private void setPrivate()
    {
        isPrivate = (mpegBytes[BYTE_3] & MASK_MP3_PRIVACY) != 0;
    }

    /**
     * Get the setBitrate of this frame
     */
    private void setBitrate() throws InvalidAudioFrameException
    {
        /* BitRate, get by checking header setBitrate bits and MPEG Version and Layer */
        int bitRateIndex = mpegBytes[BYTE_3] & MASK_MP3_BITRATE |
            mpegBytes[BYTE_2] & MASK_MP3_ID | mpegBytes[BYTE_2] & MASK_MP3_LAYER;

        bitRate = (Integer) bitrateMap.get(new Integer(bitRateIndex));
        if (bitRate == null)
        {
            throw new InvalidAudioFrameException("Invalid bitrate");
        }
    }



    /**
     * Set the Mpeg channel mode of this frame as a constant (see constants)
     */
    private void setChannelMode() throws InvalidAudioFrameException
    {
        channelMode = (mpegBytes[BYTE_4] & MASK_MP3_MODE) >>> 6;
        channelModeAsString = (String)modeMap.get(new Integer(channelMode));
        if ( channelModeAsString == null)
        {
            throw new InvalidAudioFrameException("Invalid channel mode");
        }
    }

    /**
     * Get the setEmphasis mode of this frame in a string representation
     */
    private void setEmphasis() throws InvalidAudioFrameException
    {
        emphasis = mpegBytes[BYTE_4] & MASK_MP3_EMPHASIS;
        emphasisAsString = (String) emphasisMap.get(new Integer(emphasis));
        if (getEmphasisAsString() == null)
        {
            throw new InvalidAudioFrameException("Invalid emphasis");
        }
    }


    /**
     * Set whether this frame uses padding bytes
     */
    private void setPadding()
    {
        isPadding = (mpegBytes[BYTE_3] & MASK_MP3_PADDING) != 0;
    }


    /**
     * Get the layer version of this frame as a constant int value (see constants)
     */
    private void setLayer() throws InvalidAudioFrameException
    {
        layer = (mpegBytes[BYTE_2] & MASK_MP3_LAYER) >>> 1;
        layerAsString = (String)mpegLayerMap.get(new Integer(layer));
        if ( layerAsString== null)
        {
            throw new InvalidAudioFrameException("Invalid Layer");
        }
    }


    /**
     * Sets the string representation of the mode extension of this frame
     */
    private void setModeExtension() throws InvalidAudioFrameException
    {
        int index = (mpegBytes[BYTE_4] & MASK_MP3_MODE_EXTENSION) >> 4;
        if (layer == LAYER_III)
        {
            modeExtension = (String) modeExtensionLayerIIIMap.get(new Integer(index));
            if (getModeExtension() == null)
            {
                throw new InvalidAudioFrameException("Invalid Mode Extension");
            }
        }
        else
        {
            modeExtension = (String) modeExtensionMap.get(new Integer(index));
            if (getModeExtension() == null)
            {
                throw new InvalidAudioFrameException("Invalid Mode Extension");
            }
        }
    }

    /**
     * set the sampling rate in Hz of this frame
     */
    private void setSamplingRate() throws InvalidAudioFrameException
    {
        //Frequency
        int index = (mpegBytes[BYTE_3] & MASK_MP3_FREQUENCY) >>> 2;
        HashMap samplingRateMapForVersion = (HashMap) samplingRateMap.get(new Integer(version));
        if (samplingRateMapForVersion == null)
        {
            throw new InvalidAudioFrameException("Invalid version");
        }
        samplingRate = (Integer) (samplingRateMapForVersion.get(new Integer(index)));
        if (samplingRate == null)
        {
            throw new InvalidAudioFrameException("Invalid sampling rate");
        }
    }

    /**
     * Gets the number of channels
     *
     * @return The setChannelMode value
     */
    public int getNumberOfChannels()
    {
        switch (channelMode)
        {
            case MODE_DUAL_CHANNEL:
                return 2;
            case MODE_JOINT_STEREO:
                return 2;
            case MODE_MONO:
                return 1;
            case MODE_STEREO:
                return 2;
            default:
                return 0;
        }
    }

    public int getChannelMode()
    {
        return channelMode;
    }

    public String getChannelModeAsString()
    {
        return channelModeAsString;
    }
    /**
     * Gets the mPEGVersion attribute of the MPEGFrame object
     *
     * @return The mPEGVersion value
     */
    public int getVersion()
    {
        return version;
    }

    public String getVersionAsString()
    {
        return versionAsString;
    }

    /**
     * Gets the paddingLength attribute of the MPEGFrame object
     *
     * @return The paddingLength value
     */
    public int getPaddingLength()
    {
        if (isPadding())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public Integer getBitRate()
    {
        return bitRate;
    }

    public Integer getSamplingRate()
    {
        return samplingRate;
    }

    /*
     * Gets this frame length in bytes
     *
     * Calculation is Bitrate (scaled to bps) divided by sampling frequency (in Hz), The larger the bitrate the larger
     * the frame but the more samples per second the smaller the value, also have to take into account frame padding
     * Have to multiple by a coefficient constant depending upon the layer it is encoded in,

     */
    public int getFrameLength()
    {
        switch (layer)
        {
            case LAYER_I:
                return (LAYER_I_FRAME_SIZE_COEFFICIENT * (getBitRate().intValue() * SCALE_BY_THOUSAND) / getSamplingRate().intValue() + getPaddingLength()) * LAYER_I_SLOT_SIZE;

            case LAYER_II:
                return LAYER_II_FRAME_SIZE_COEFFICIENT * (getBitRate().intValue() * SCALE_BY_THOUSAND) / getSamplingRate().intValue() + getPaddingLength() * LAYER_II_SLOT_SIZE;

            case LAYER_III:
                return LAYER_III_FRAME_SIZE_COEFFICIENT * (getBitRate().intValue() * SCALE_BY_THOUSAND) / getSamplingRate().intValue() + getPaddingLength()* LAYER_III_SLOT_SIZE;

            default:
                //Shouldnt happen
                return LAYER_III_FRAME_SIZE_COEFFICIENT * (getBitRate().intValue() * SCALE_BY_THOUSAND) / getSamplingRate().intValue() + getPaddingLength()* LAYER_III_SLOT_SIZE;

        }
    }

    /**
    * Get the number of samples in a frame, all frames in a file have a set number of samples as defined by their MPEG Versiona
    * and Layer
    */
    public int getNoOfSamples()
    {
        Integer noOfSamples = (Integer) ((HashMap)  samplesPerFrameMap.get(new Integer(version))).get(new Integer(layer));
        return noOfSamples.intValue();
    }


    public boolean isPadding()
    {
        return isPadding;
    }

    public boolean isCopyrighted()
    {
        return isCopyrighted;
    }

    public boolean isOriginal()
    {
        return isOriginal;
    }

    public boolean isProtected()
    {
        return isProtected;
    }

    public boolean isPrivate()
    {
        return isPrivate;
    }

    public boolean isVariableBitRate()
    {
        return false;
    }

    public int getEmphasis()
    {
        return emphasis;
    }

    public String getEmphasisAsString()
    {
        return emphasisAsString;
    }

    public String getModeExtension()
    {
        return modeExtension;
    }


    /**
     * Hide Constructor
     */
    private MPEGFrameHeader() throws InvalidAudioFrameException
    {

    }

    /**
     * Try and create a new MPEG frame with the given byte array and decodes its contents
     * If decoding header causes a problem it is not a valid header
     *
     * @param b the array of bytes representing this mpeg frame
     * @throws InvalidAudioFrameException if does not match expected format
     */
    private MPEGFrameHeader(byte[] b) throws InvalidAudioFrameException
    {
        mpegBytes = b;
        setVersion();
        setLayer();
        setProtected();
        setBitrate();
        setSamplingRate();
        setPadding();
        setPrivate();
        setChannelMode();
        setModeExtension();
        setCopyrighted();
        setOriginal();
        setEmphasis();
    }

    /**
     * Parse the MPEGFrameHeader of an MP3File, file pointer returns at end of the frame header
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static MPEGFrameHeader parseMPEGHeader(ByteBuffer bb)
        throws InvalidAudioFrameException
    {
        int position =  bb.position();
        bb.get(header,0,HEADER_SIZE);
        MPEGFrameHeader frameHeader = new MPEGFrameHeader(header);
        bb.position(position);
        return frameHeader;
    }

    /**
     * Gets the MPEGFrame attribute of the MPEGFrame object
     *
     * @return The mPEGFrame value
     */
    public static boolean isMPEGFrame(ByteBuffer  bb)
    {
        position = bb.position();
        return    (
                   ((bb.get(position) & SYNC_BYTE1) == SYNC_BYTE1)
                    &&
                   ((bb.get(position + 1) & SYNC_BYTE2) == SYNC_BYTE2)
                  );

    }

    /**
     *
     * @return a string represntation
     */
    public String toString()
    {
        return
            AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_1]) +
            AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_2]) +
            AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_3]) +
            AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_4]);
    }
}

