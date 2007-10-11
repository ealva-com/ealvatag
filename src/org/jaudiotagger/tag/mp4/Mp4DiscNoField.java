package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;

import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Represents the Disc No field
 *
 * <p>For some reason uses an array of three numbers, but only the last two are of use for display purposes
 */
public class Mp4DiscNoField extends Mp4TagTextNumberField
{
    private static final int NONE_VALUE_INDEX   = 0;
    private static final int DISC_NO_INDEX = 1;
    private static final int DISC_TOTAL_INDEX = 2;

    /**
         * Create new Track Field parsing the String for the trackno/total
         *
         * TODO what should we do if discValue is invalid
         *
         * @param discValue
         */
        public Mp4DiscNoField(String discValue)
        {
            super(Mp4FieldKey.DISCNUMBER.getFieldName(), discValue);

            numbers = new ArrayList<Short>();
            numbers.add(new Short("0"));

            String values[] = discValue.split("/");
            switch(values.length)
            {
                case 1:
                    numbers.add(Short.parseShort(values[0]));
                    numbers.add(new Short("0"));

                case 2:
                    numbers.add(Short.parseShort(values[0]));
                    numbers.add(Short.parseShort(values[1]));

                default:
                    numbers.add(new Short("0"));
                    numbers.add(new Short("0"));
            }
        }


    /**
     * Create new Disc no  Field with only discNo
     *
     * @param discNo
     */
    public Mp4DiscNoField(int discNo)
    {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), String.valueOf(discNo));
        numbers = new ArrayList<Short>();
        numbers.add(new Short("0"));
        numbers.add((short)discNo);
        numbers.add(new Short("0"));
    }

     /**
     * Create new Disc No Field with disc No and total tracks
     *
     * @param discNo
     * @param total
     */
    public Mp4DiscNoField(int discNo,int total)
    {
        super(Mp4FieldKey.DISCNUMBER.getFieldName(), String.valueOf(discNo));
        numbers = new ArrayList<Short>();
        numbers.add(new Short("0"));
        numbers.add((short)discNo);
        numbers.add((short)total);
    }
    public Mp4DiscNoField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
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
