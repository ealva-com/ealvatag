package ealvatag.audio.mp4.atom;

import com.google.common.base.Preconditions;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.mp4.Mp4AtomIdentifier;
import ealvatag.audio.mp4.Mp4IlstBox;
import ealvatag.logging.ErrorMessage;
import ealvatag.tag.mp4.Mp4Tag;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This MP4 MetaBox is the parent of metadata, it usually contains four bytes of data
 * that needs to be processed before we can examine the children. But I also have a file that contains
 * meta (and no udta) that does not have this children data.
 */
public class Mp4MetaBox extends AbstractMp4Box {
    public static final int FLAGS_LENGTH = 4;

    /**
     * @param header     header info
     * @param dataBuffer data of box (doesn't include header data)
     */
    public Mp4MetaBox(Mp4BoxHeader header, ByteBuffer dataBuffer) {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }

    public Mp4MetaBox(final Mp4BoxHeader metaBoxHeader,
                      final BufferedSource bufferedSource,
                      final Mp4Tag mp4Tag,
                      final boolean ignoreArtwork)
            throws IOException, CannotReadException {
        Preconditions.checkArgument(Mp4AtomIdentifier.META.matches(metaBoxHeader.getId()));

        header = metaBoxHeader;
        int dataSize = metaBoxHeader.getDataLength();

        bufferedSource.skip(FLAGS_LENGTH);
        dataSize -= FLAGS_LENGTH;

        Mp4IlstBox ilstBox = null;
        while (dataSize >= Mp4BoxHeader.HEADER_LENGTH && ilstBox == null) {
            Mp4BoxHeader childHeader = new Mp4BoxHeader(bufferedSource);
            switch (childHeader.getIdentifier()) {
                case ILST:
                    ilstBox = new Mp4IlstBox(childHeader, bufferedSource, mp4Tag, ignoreArtwork);
                    break;
                default:
                    bufferedSource.skip(childHeader.getDataLength());
            }
            dataSize -= childHeader.getLength();
        }
        bufferedSource.skip(dataSize);
    }

    public void processData() throws CannotReadException {
        //4-skip the meta flags and check they are the meta flags
        byte[] b = new byte[FLAGS_LENGTH];
        dataBuffer.get(b);
        if (b[0] != 0) {
            throw new CannotReadException(ErrorMessage.MP4_FILE_META_ATOM_CHILD_DATA_NOT_NULL);
        }
    }

    /**
     * Create an iTunes style Meta box
     * <p>
     * <p>Useful when writing to mp4 that previously didn't contain an mp4 meta atom
     *
     * @param childrenSize used to set the length
     *
     * @return an Mp4MetaBox properly sized for {@code childrenSize}
     */
    public static Mp4MetaBox createiTunesStyleMetaBox(int childrenSize) {
        Mp4BoxHeader metaHeader = new Mp4BoxHeader(Mp4AtomIdentifier.META.getFieldName());
        metaHeader.setLength(Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH + childrenSize);
        ByteBuffer metaData = ByteBuffer.allocate(Mp4MetaBox.FLAGS_LENGTH);
        return new Mp4MetaBox(metaHeader, metaData);
    }
}
