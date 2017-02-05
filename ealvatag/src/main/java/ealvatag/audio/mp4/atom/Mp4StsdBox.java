package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4AudioHeader;
import okio.BufferedSource;

import java.io.IOException;

/**
 * StsdBox ( sample (frame encoding) description box)
 * <p>
 * <p>4 bytes version/flags = byte hex version + 24-bit hex flags
 * (current = 0)
 * 4 bytes number of descriptions = long unsigned total
 * (default = 1)
 * Then if audio contains mp4a,alac or drms box
 */
public class Mp4StsdBox extends AbstractMp4Box {
//    public static final int VERSION_FLAG_POS = 0;
//    public static final int OTHER_FLAG_POS = 1;
//    public static final int NO_OF_DESCRIPTIONS_POS = 4;

    private static final int VERSION_FLAG_LENGTH = 1;
    private static final int OTHER_FLAG_LENGTH = 3;
    private static final int NO_OF_DESCRIPTIONS_POS_LENGTH = 4;

    public Mp4StsdBox(final Mp4BoxHeader stsdBoxHeader, final BufferedSource bufferedSource, final Mp4AudioHeader audioHeader)
            throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.STSD.matches(stsdBoxHeader.getId()));
        header = stsdBoxHeader;
        int dataSize = stsdBoxHeader.getDataLength();

        final int skipUnusedAmount = VERSION_FLAG_LENGTH + OTHER_FLAG_LENGTH + NO_OF_DESCRIPTIONS_POS_LENGTH;
        bufferedSource.skip(skipUnusedAmount);
        dataSize -= skipUnusedAmount;

        AbstractMp4Box parsedBox = null;
        boolean done = false;
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && !done) {
            Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
            switch (childHeader.getIdentifier()) {
                case MP4A:
                    parsedBox = new Mp4Mp4aBox(childHeader, bufferedSource, audioHeader);
                    break;
                case DRMS:
                    parsedBox = new Mp4DrmsBox(childHeader, bufferedSource, audioHeader);
                    break;
                case ALAC:
                    parsedBox = new Mp4AlacBox(childHeader, bufferedSource, audioHeader, false);
                    break;
                default:
                    bufferedSource.skip(childHeader.getDataLength());
            }
            dataSize -= childHeader.getLength();
            if (parsedBox != null) {
                done = true;
            }
        }
        if (dataSize > 0) {
            // TODO: 2/4/17 LOG
            bufferedSource.skip(dataSize);
        }
    }
}
