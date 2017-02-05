package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.EncoderType;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4AudioHeader;
import okio.BufferedSource;

import java.io.IOException;

/**
 * Mp4aBox ( sample (frame encoding) description box)
 * <p>
 * At first glance appears to hold no of channels but actually always returns 2 even for mono recordings
 * so just need to skip over data in order to get to child atom esds
 * <p>
 * <p>4 bytes version/flags = byte hex version + 24-bit hex flags
 * (current = 0)
 * <p>
 * 6 bytes reserved = 48-bit value set to zero
 * 2 bytes data reference index
 * = short unsigned index from 'dref' box
 * 2 bytes QUICKTIME audio encoding version = short hex version
 * - default = 0 ; audio data size before decompression = 1
 * 2 bytes QUICKTIME audio encoding revision level
 * = byte hex version
 * - default = 0 ; video can revise this value
 * 4 bytes QUICKTIME audio encoding vendor
 * = long ASCII text string
 * - default = 0
 * 2 bytes audio channels = short unsigned count
 * (mono = 1 ; stereo = 2)
 * 2 bytes audio sample size = short unsigned value
 * (8 or 16)
 * 2 bytes QUICKTIME audio compression id = short integer value
 * - default = 0
 * 2 bytes QUICKTIME audio packet size = short value set to zero
 * 4 bytes audio sample rate = long unsigned fixed point rate
 */
class Mp4Mp4aBox extends AbstractMp4Box {
//    public static final int RESERVED_POS = 0;
//    public static final int REFERENCE_INDEX_POS = 6;
//    public static final int AUDIO_ENCODING_POS = 8;
//    public static final int AUDIO_REVISION_POS = 10;
//    public static final int AUDIO_ENCODING_VENDOR_POS = 12;
//    public static final int CHANNELS_POS = 16;
//    public static final int AUDIO_SAMPLE_SIZE_POS = 18;
//    public static final int AUDIO_COMPRESSION_ID_POS = 20;
//    public static final int AUDIO_PACKET_SIZE_POS = 22;
//    public static final int AUDIO_SAMPLE_RATE_POS = 24;

    private static final int RESERVED_LENGTH = 6;
    private static final int REFERENCE_INDEX_LENGTH = 2;
    private static final int AUDIO_ENCODING_LENGTH = 2;
    private static final int AUDIO_REVISION_LENGTH = 2;
    private static final int AUDIO_ENCODING_VENDOR_LENGTH = 4;
    private static final int CHANNELS_LENGTH = 2;
    private static final int AUDIO_SAMPLE_SIZE_LENGTH = 2;
    private static final int AUDIO_COMPRESSION_ID_LENGTH = 2;
    private static final int AUDIO_PACKET_SIZE_LENGTH = 2;
    private static final int AUDIO_SAMPLE_RATE_LENGTH = 4;

    private static final int TOTAL_LENGTH = RESERVED_LENGTH +
            REFERENCE_INDEX_LENGTH +
            AUDIO_ENCODING_LENGTH +
            AUDIO_REVISION_LENGTH +
            AUDIO_ENCODING_VENDOR_LENGTH +
            CHANNELS_LENGTH +
            AUDIO_SAMPLE_SIZE_LENGTH +
            AUDIO_COMPRESSION_ID_LENGTH +
            AUDIO_PACKET_SIZE_LENGTH +
            AUDIO_SAMPLE_RATE_LENGTH;


    Mp4Mp4aBox(final Mp4BoxHeader mp4aBoxHeader,
               final BufferedSource bufferedSource,
               final Mp4AudioHeader audioHeader) throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.MP4A.matches(mp4aBoxHeader.getId()));
        header = mp4aBoxHeader;

        int dataSize = mp4aBoxHeader.getDataLength();
        bufferedSource.skip(TOTAL_LENGTH);
        dataSize -= TOTAL_LENGTH;

        Mp4EsdsBox esdsBox = null;
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && esdsBox == null) {
            Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
            switch (childHeader.getIdentifier()) {
                case ESDS:
                    esdsBox = new Mp4EsdsBox(childHeader,
                                             bufferedSource,
                                             audioHeader,
                                             EncoderType.AAC);
                    break;
                default:
                    bufferedSource.skip(childHeader.getDataLength());
            }
            dataSize -= childHeader.getLength();
        }
        bufferedSource.skip(dataSize);

    }
}
