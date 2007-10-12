package org.jaudiotagger.tag.mp4.field;

import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.tag.mp4.atom.Mp4DataBox;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Represents a single byte as a number
 *
 * <p>Usually single byte fields are used as a boolean field, but not always so we dont do this conversion
 */
public class Mp4TagByteField extends Mp4TagTextField
{
    //TODO:Holds the actual size of the data content as held in the databoxitem, this is required because
    //we cant accurately work out the size by looking at the content because we only actually ever use a single byte
    //in calculating the content value
    //e.g byte data length seems to be 1 for pgap and cpil but 2 for tmpo, so we stored the dataSize
    //when we loaded the value so if greater than 1 we pad the value
    private int realDataLength;

    /**
     * Create new field
     *
     * Assume length of 1 which is correct for most but not all byte fields
     * 
     * @param id
     * @param value
     */
    public Mp4TagByteField(Mp4FieldKey id, String value)
    {
        this(id,value,1);
    }

    /**
     * Create new field with known length
     *
     * @param id
     * @param value
     */
    public Mp4TagByteField(Mp4FieldKey id, String value,int realDataLength)
    {
        super(id.getFieldName(), value);
        this.realDataLength=realDataLength;
    }

    /**
     * Construct from rawdata from audio file
     *
     * @param id
     * @param raw
     * @throws UnsupportedEncodingException
     */
    public Mp4TagByteField(String id, ByteBuffer raw) throws UnsupportedEncodingException
    {
        super(id, raw);
    }

    public Mp4FieldType getFieldType()
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
