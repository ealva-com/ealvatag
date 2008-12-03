package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.Mp4NotMetaFieldKey;

import java.nio.ByteBuffer;

/**
 * This MP4MetaBox is the parent of metadata, it also contains a small amount of data
 * that needs to be processed before we can examine the children
 */
public class Mp4MetaBox extends AbstractMp4Box
{
    public static final int FLAGS_LENGTH = 4;

    /**
     * @param header     header info
     * @param dataBuffer data of box (doesnt include header data)
     */
    public Mp4MetaBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header = header;
        this.dataBuffer = dataBuffer;
    }

    public void processData() throws CannotReadException
    {
        //4-skip the meta flags and check they are the meta flags
        byte[] b = new byte[FLAGS_LENGTH];
        dataBuffer.get(b);
        if (b[0] != 0)
        {
            throw new CannotReadException();
        }
    }

    /**
     * Create an iTunes style Meta box
     *
     * <p>Useful when writing to mp4 that previously didn't contain an mp4 meta atom</p>
     *
     * @param childrenSize
     * @return
     */
    public static Mp4MetaBox createiTunesStyleMetaBox(int childrenSize)
    {
        Mp4BoxHeader metaHeader = new Mp4BoxHeader(Mp4NotMetaFieldKey.META.getFieldName());
        metaHeader.setLength(Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH + childrenSize);
        ByteBuffer metaData = ByteBuffer.allocate(Mp4MetaBox.FLAGS_LENGTH);     
        Mp4MetaBox metaBox = new Mp4MetaBox(metaHeader,metaData);
        return metaBox;
    }
}
