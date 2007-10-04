package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Reads a single byte as a number
 */
public class Mp4TagByteField extends Mp4TagTextField
{

    public Mp4TagByteField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TagByteField(String id, ByteBuffer raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    protected byte[] getDataBytes()
    {
        return Utils.getSizeBigEndian(Integer.parseInt(content));
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
         //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header  = new Mp4BoxHeader(data);
        Mp4DataBox   databox = new Mp4DataBox(header,data);
        dataSize = header.getDataLength();
        content  = databox.getContent();
    }
}
