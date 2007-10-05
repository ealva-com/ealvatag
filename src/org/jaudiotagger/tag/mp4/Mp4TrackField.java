package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

import java.util.List;
import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;

/**
 * Represents simple text field, but reads the data content as an arry of 16 bit unsigned numbers
 */
public class Mp4TrackField extends Mp4TagTextNumberField
{
    private static final int NONE_VALUE_INDEX   = 0;
    private static final int TRACK_NO_INDEX     = 1;
    private static final int TRACK_TOTAL_INDEX  = 2;

    public Mp4TrackField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TrackField(String id, ByteBuffer data) throws UnsupportedEncodingException
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

        //Track number always hold three values, we can discard the first one, the second one is the track no
        //and the third is the total no of tracks so only use if not zero
        StringBuffer sb = new StringBuffer();
        sb.append(numbers.get(TRACK_NO_INDEX));
        if(numbers.get(TRACK_TOTAL_INDEX )>0)
        {
            sb.append("/"+numbers.get(TRACK_TOTAL_INDEX ));
        }
        content  = sb.toString();
    }
}
