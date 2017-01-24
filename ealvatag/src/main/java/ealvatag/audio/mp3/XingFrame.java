package ealvatag.audio.mp3;

import ealvatag.audio.exceptions.InvalidAudioFrameException;
import ealvatag.utils.ArrayUtil;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Xing Frame
 * <p>
 * <p>In some MP3s which variable bit rate the first frame in the file contains a special frame called a Xing Frame,
 * instead of audio data. This is used to store additional information about the file. The most important aspect for
 * this library is details allowing us to determine the bitrate of a Variable Bit Rate VBR file without having
 * to process the whole file.
 * <p>
 * Xing VBR Tag data format is 120 bytes long
 * 4 bytes for Header Tag
 * 4 bytes for Header Flags
 * 4 bytes for FRAME SIZE
 * 4 bytes for AUDIO_SIZE
 * 100 bytes for entry (NUMTOCENTRIES)
 * 4 bytes for VBR SCALE. a VBR quality indicator: 0=best 100=worst
 * <p>
 * It my then contain a Lame Frame ( a Lame frame is in essence an extended Xing Frame
 */
@SuppressWarnings("Duplicates") public class XingFrame {
    private static final Logger LOG = LoggerFactory.getLogger(XingFrame.class);

    //The offset into first frame varies based on the MPEG frame properties
    private static final int MPEG_VERSION_1_MODE_MONO_OFFSET = 21;
    private static final int MPEG_VERSION_1_MODE_STEREO_OFFSET = 36;
    private static final int MPEG_VERSION_2_MODE_MONO_OFFSET = 13;
    private static final int MPEG_VERSION_2_MODE_STEREO_OFFSET = 21;

    private static final int XING_HEADER_BUFFER_SIZE = 120;
    private static final int XING_IDENTIFIER_BUFFER_SIZE = 4;
    private static final int XING_FLAG_BUFFER_SIZE = 4;
    private static final int XING_FRAMECOUNT_BUFFER_SIZE = 4;
    private static final int XING_AUDIOSIZE_BUFFER_SIZE = 4;
    private static final int TEMP_BUF_SIZE = Math.max(Math.max(Math.max(Math.max(XING_IDENTIFIER_BUFFER_SIZE,
                                                                                 XING_FLAG_BUFFER_SIZE),
                                                                        XING_FRAMECOUNT_BUFFER_SIZE),
                                                               XING_AUDIOSIZE_BUFFER_SIZE),
                                                      LameFrame.ENCODER_SIZE);

    static final int MAX_BUFFER_SIZE_NEEDED_TO_READ_XING =
            MPEG_VERSION_1_MODE_STEREO_OFFSET + XING_HEADER_BUFFER_SIZE + LameFrame.LAME_HEADER_BUFFER_SIZE;


    private static final int BYTE_1 = 0;
    private static final int BYTE_2 = 1;
    private static final int BYTE_3 = 2;
    private static final int BYTE_4 = 3;

    /**
     * Use when it is a VBR (Variable Bitrate) file
     */
    private static final byte[] XING_VBR_ID = {'X', 'i', 'n', 'g'};

    /**
     * Use when it is a CBR (Constant Bitrate) file
     */
    private static final byte[] XING_CBR_ID = {'I', 'n', 'f', 'o'};


    private ByteBuffer header;

    private boolean vbr = false;
    private boolean isFrameCountEnabled = false;
    private int frameCount = -1;
    private boolean isAudioSizeEnabled = false;
    private int audioSize = -1;
    private LameFrame lameFrame;

    /**
     * Read the Xing Properties from the buffer
     */
    private XingFrame(ByteBuffer header) {
        this.header = header;

        //Go to start of Buffer
        header.rewind();

        //Set Vbr
        setVbr();

        //Read Flags, only the fourth byte of interest to us
        byte flagBuffer[] = new byte[XING_FLAG_BUFFER_SIZE];
        header.get(flagBuffer);

        //Read FrameCount if flag set
        if ((flagBuffer[BYTE_4] & (byte)(1)) != 0) {
            setFrameCount();
        }

        //Read Size if flag set
        if ((flagBuffer[BYTE_4] & (byte)(1 << 1)) != 0) {
            setAudioSize();
        }

        //TODO TOC
        //TODO VBR Quality

        //Look for LAME Header as long as we have enough bytes to do it properly
        if (header.limit() >= XING_HEADER_BUFFER_SIZE + LameFrame.LAME_HEADER_BUFFER_SIZE) {
            header.position(XING_HEADER_BUFFER_SIZE);
            lameFrame = LameFrame.parseLameFrame(header);
        }
    }

    private XingFrame(final Buffer buffer) {
        final byte[] tempBuf = new byte[TEMP_BUF_SIZE];

        int skipLess = setVbr(buffer, tempBuf);

        Arrays.fill(tempBuf, ArrayUtil.ZERO);
        buffer.read(tempBuf, 0, XING_FLAG_BUFFER_SIZE);
        skipLess += XING_FLAG_BUFFER_SIZE;

        final boolean hasFrameCount = (tempBuf[BYTE_4] & (byte)(1)) != 0;
        final boolean readSize = (tempBuf[BYTE_4] & (byte)(1 << 1)) != 0;

        if (hasFrameCount) {
            skipLess += setFrameCount(buffer, tempBuf);
        }

        if (readSize) {
            skipLess += setAudioSize(buffer, tempBuf);
        }

        //TODO TOC
        //TODO VBR Quality

        //Look for LAME Header as long as we have enough bytes to do it properly
        if (buffer.size() >= XING_HEADER_BUFFER_SIZE + LameFrame.LAME_HEADER_BUFFER_SIZE) {
            try {
                buffer.skip(XING_HEADER_BUFFER_SIZE - skipLess);
                lameFrame = LameFrame.parseLameFrame(buffer, tempBuf);
            } catch (EOFException e) {
                LOG.info("Not enough room for Lame header", e);
            }
        }
    }

    LameFrame getLameFrame() {
        return lameFrame;
    }

    /**
     * Set whether or not VBR, (Xing can also be used for CBR though this is less useful)
     */
    private void setVbr() {
        //Is it VBR or CBR
        byte[] identifier = new byte[XING_IDENTIFIER_BUFFER_SIZE];
        header.get(identifier);
        if (Arrays.equals(identifier, XING_VBR_ID)) {
            LOG.trace("Is Vbr");
            vbr = true;
        }
    }

    private int setVbr(Buffer buffer, final byte[] tempBuf) {
        //Is it VBR or CBR
        buffer.read(tempBuf, 0, XING_IDENTIFIER_BUFFER_SIZE);
        if (ArrayUtil.equals(tempBuf, XING_VBR_ID, XING_VBR_ID.length)) {
            LOG.trace("Is Vbr");
            vbr = true;
        }
        return XING_IDENTIFIER_BUFFER_SIZE;
    }

    /**
     * Set count of frames
     */
    private void setFrameCount() {
        byte frameCountBuffer[] = new byte[XING_FRAMECOUNT_BUFFER_SIZE];
        header.get(frameCountBuffer);
        isFrameCountEnabled = true;
        frameCount = (frameCountBuffer[BYTE_1] << 24) & 0xFF000000 | (frameCountBuffer[BYTE_2] << 16) & 0x00FF0000 |
                (frameCountBuffer[BYTE_3] << 8) & 0x0000FF00 | frameCountBuffer[BYTE_4] & 0x000000FF;
    }

    private int setFrameCount(final Buffer header, final byte[] frameCountBuffer) {
        ArrayUtil.fill(frameCountBuffer, ArrayUtil.ZERO, XING_FRAMECOUNT_BUFFER_SIZE);
        header.read(frameCountBuffer, 0, XING_FRAMECOUNT_BUFFER_SIZE);
        isFrameCountEnabled = true;
        frameCount = (frameCountBuffer[BYTE_1] << 24) & 0xFF000000 | (frameCountBuffer[BYTE_2] << 16) & 0x00FF0000 |
                (frameCountBuffer[BYTE_3] << 8) & 0x0000FF00 | frameCountBuffer[BYTE_4] & 0x000000FF;
        return XING_FRAMECOUNT_BUFFER_SIZE;
    }

    /**
     * @return true if frameCount has been specified in header
     */
    final boolean isFrameCountEnabled() {
        return isFrameCountEnabled;
    }

    /**
     * @return count of frames
     */
    final int getFrameCount() {
        return frameCount;
    }

    /**
     * Set size of AudioData
     */
    private void setAudioSize() {
        byte frameSizeBuffer[] = new byte[XING_AUDIOSIZE_BUFFER_SIZE];
        header.get(frameSizeBuffer);
        isAudioSizeEnabled = true;
        audioSize = (frameSizeBuffer[BYTE_1] << 24) & 0xFF000000 | (frameSizeBuffer[BYTE_2] << 16) & 0x00FF0000 |
                (frameSizeBuffer[BYTE_3] << 8) & 0x0000FF00 | frameSizeBuffer[BYTE_4] & 0x000000FF;
    }

    private int setAudioSize(final Buffer header, final byte[] frameSizeBuffer) {
        ArrayUtil.fill(frameSizeBuffer, ArrayUtil.ZERO, XING_AUDIOSIZE_BUFFER_SIZE);
        header.read(frameSizeBuffer, 0, XING_AUDIOSIZE_BUFFER_SIZE);
        isAudioSizeEnabled = true;
        audioSize = (frameSizeBuffer[BYTE_1] << 24) & 0xFF000000 | (frameSizeBuffer[BYTE_2] << 16) & 0x00FF0000 |
                (frameSizeBuffer[BYTE_3] << 8) & 0x0000FF00 | frameSizeBuffer[BYTE_4] & 0x000000FF;
        return XING_AUDIOSIZE_BUFFER_SIZE;
    }

    /**
     * @return true if audioSize has been specified in header
     */
    final boolean isAudioSizeEnabled() {
        return isAudioSizeEnabled;
    }

    /**
     * @return size of audio data in bytes
     */
    final int getAudioSize() {
        return audioSize;
    }

    /**
     * Parse the XingFrame of an MP3File, cannot be called until we have validated that
     * this is a XingFrame
     *
     * @return fully constructed XingFrame
     *
     * @throws InvalidAudioFrameException if problem parsing
     */
    static XingFrame parseXingFrame(ByteBuffer header) throws InvalidAudioFrameException {
        return new XingFrame(header);
    }

    static XingFrame parseXingFrame(final Buffer buffer) throws InvalidAudioFrameException {
        return new XingFrame(buffer);
    }

    static ByteBuffer isXingFrame(ByteBuffer bb, MPEGFrameHeader mpegFrameHeader) {
        //We store this so can return here after scanning through buffer
        int startPosition = bb.position();

        //Get to Start of where Xing Frame Should be ( we dont know if it is one at this point)
        if (mpegFrameHeader.getVersion() == MPEGFrameHeader.VERSION_1) {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                bb.position(startPosition + MPEG_VERSION_1_MODE_MONO_OFFSET);
            } else {
                bb.position(startPosition + MPEG_VERSION_1_MODE_STEREO_OFFSET);
            }
        }
        //MPEGVersion 2 and 2.5
        else {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                bb.position(startPosition + MPEG_VERSION_2_MODE_MONO_OFFSET);
            } else {
                bb.position(startPosition + MPEG_VERSION_2_MODE_STEREO_OFFSET);
            }
        }

        //Create header from here
        ByteBuffer header = bb.slice();

        // Return Buffer to start Point
        bb.position(startPosition);

        //Check Identifier
        byte[] identifier = new byte[XING_IDENTIFIER_BUFFER_SIZE];
        header.get(identifier);
        if ((!Arrays.equals(identifier, XING_VBR_ID)) && (!Arrays.equals(identifier, XING_CBR_ID))) {
            return null;
        }
        LOG.trace("Found Xing Frame");
        return header;
    }

    static Buffer isXingFrame(Buffer buffer, MPEGFrameHeader mpegFrameHeader) throws EOFException {

        //Get to Start of where Xing Frame Should be ( we dont know if it is one at this point)
        if (mpegFrameHeader.getVersion() == MPEGFrameHeader.VERSION_1) {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                buffer.skip(MPEG_VERSION_1_MODE_MONO_OFFSET);
            } else {
                buffer.skip(MPEG_VERSION_1_MODE_STEREO_OFFSET);
            }
        }
        //MPEGVersion 2 and 2.5
        else {
            if (mpegFrameHeader.getChannelMode() == MPEGFrameHeader.MODE_MONO) {
                buffer.skip(MPEG_VERSION_2_MODE_MONO_OFFSET);
            } else {
                buffer.skip(MPEG_VERSION_2_MODE_STEREO_OFFSET);
            }
        }

        //Create header from here

        //Check Identifier
        byte[] identifier = new byte[XING_IDENTIFIER_BUFFER_SIZE];
        for (int i = 0; i < identifier.length; i++) {
            identifier[i] = buffer.getByte(i);
        }
        if ((!Arrays.equals(identifier, XING_VBR_ID)) && (!Arrays.equals(identifier, XING_CBR_ID))) {
            return null;
        }
        LOG.trace("Found Xing Frame");
        return buffer;
    }

    final boolean isVbr() {
        return vbr;
    }

    public String toString() {
        return "xingheader" + " vbr:" + vbr + " frameCountEnabled:" + isFrameCountEnabled + " frameCount:" +
                frameCount + " audioSizeEnabled:" + isAudioSizeEnabled + " audioFileSize:" + audioSize;
    }
}
