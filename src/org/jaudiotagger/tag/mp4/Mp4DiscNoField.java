package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;

/**
 * Represents simple text field, but reads the data content as an arry of 16 bit unsigned numbers
 */
public class Mp4DiscNoField extends Mp4TagTextNumberField
{
    private static final int NONE_VALUE_INDEX   = 0;
    private static final int DISC_NO_INDEX = 1;
    private static final int DISC_TOTAL_INDEX = 2;
    private static final int NONE_END_VALUE_INDEX   = 3;

    public Mp4DiscNoField(String id, String n)
    {
        super(id, n);
    }

    public Mp4DiscNoField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
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
        numbers  = databox.getNumbers();

        //Disc number always hold four values, we can discard the first one and last one, the second one is the disc no
        //and the third is the total no of discs so only use if not zero
        StringBuffer sb = new StringBuffer();
        sb.append(numbers.get(DISC_NO_INDEX));
        if(numbers.get(DISC_TOTAL_INDEX)>0)
        {
            sb.append("/"+numbers.get(DISC_TOTAL_INDEX));
        }
        content  = sb.toString();
    }
}