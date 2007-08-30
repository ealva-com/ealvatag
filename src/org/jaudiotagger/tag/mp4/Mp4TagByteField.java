package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;

import java.io.UnsupportedEncodingException;

/**
 * Reads a single byte as a number
 */
public class Mp4TagByteField extends Mp4TagTextField
{

    public Mp4TagByteField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TagByteField(String id, byte[] raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    protected byte[] getDataBytes()
    {
        return Utils.getSizeBigEndian(Integer.parseInt(content));
    }

    protected void build(byte[] raw) throws UnsupportedEncodingException
    {
        this.content = Utils.getNumberBigEndian(raw,
            DATA_HEADER_LENGTH ,
            raw.length - 1) + "";
    }
}
