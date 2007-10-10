package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents a single byte as a number
 *
 * Usually single byte fields are used as a boolean field, but not always so we dont do this conversion
 */
public class Mp4TagByteField extends Mp4TagTextField
{
    //TODO:Holds the actual size of the data content as held in the databoxitem, this is required because
    //we cant accurately work out the size by looking at the content because we only actually ever use a single byte
    //in calcultaing the content value
    //e.g byte data length seems to be 1 for pgap and cpil but 2 for tmpo, so we stored the dataSize
    //when we loaded the value so if greater than 1 we pad the value
    private int realDataLength;

    public Mp4TagByteField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TagByteField(String id, ByteBuffer raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    protected Mp4FieldType getFieldType()
    {
        return Mp4FieldType.BYTE;
    }

    protected byte[] getDataBytes()throws UnsupportedEncodingException
    {
         //Only a byte, but of course byte is signed we need unsigned
         Short shortValue = new Short(content);
         byte rawData [] = new byte[realDataLength];
         rawData[rawData.length-1] = shortValue.byteValue();
         return rawData;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
         //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header  = new Mp4BoxHeader(data);
        Mp4DataBox   databox = new Mp4DataBox(header,data);
        dataSize        = header.getDataLength();
        //Needed for subsequent write
        realDataLength  = dataSize - Mp4DataBox.PRE_DATA_LENGTH;
        content  = databox.getContent();
    }
}
