package org.jaudiotagger.audio.mp4.util;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.exceptions.CannotReadException;

import java.nio.ByteBuffer;

/**
 * This MP4MetaBox is the parent of metadata, it also contains a small amount of data
 * that needs to be processed before we can examine the children
 *
 */
public class Mp4MetaBox extends AbstractMp4Box
{
    /**
     *
     * @param header header info
     * @param dataBuffer data of box (doesnt include header data)
     */
    public Mp4MetaBox(Mp4BoxHeader header, ByteBuffer dataBuffer)
    {
        this.header  = header;
        this.dataBuffer = dataBuffer;
    }

    public void processData() throws CannotReadException
    {
        //4-skip the meta flags and check they are the meta flags
        byte[] b = new byte[4];
        dataBuffer.get(b);
        if (b[0] != 0)
        {
            throw new CannotReadException();
        }
    }
}
