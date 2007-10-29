package org.jaudiotagger.audio.mp4.atom;

import org.jaudiotagger.audio.generic.Utils;

import java.nio.ByteBuffer;

/**
 * StcoBox ( media (stream) header), holds offsets into the Audio data
 */
public class Mp4StcoBox extends AbstractMp4Box
{
    public static final int VERSION_FLAG_POS         = 0;
    public static final int OTHER_FLAG_POS           = 1;
    public static final int NO_OF_OFFSETS_POS        = 4;


    public static final int VERSION_FLAG_LENGTH         = 1;
    public static final int OTHER_FLAG_LENGTH           = 3;
    public static final int NO_OF_OFFSETS_LENGTH        = 4;
    public static final int OFFSET_LENGTH               = 4;
    private int noOfOffSets = 0;
    private int firstOffSet;
    /**
     * Construct box from data and show contents
     *
     * @param header header info
     * @param originalDataBuffer data of box (doesnt include header data)
     */
    public Mp4StcoBox(Mp4BoxHeader header, ByteBuffer originalDataBuffer)
    {
        this.header  = header;

        //Make a slice of databuffer then we can work with relative or absolute methods safetly
        this.dataBuffer =  originalDataBuffer.slice();

        //Skip the flags
        dataBuffer.position(dataBuffer.position() + VERSION_FLAG_LENGTH  +  OTHER_FLAG_LENGTH);

        //No of offsets
        this.noOfOffSets = Utils.getNumberBigEndian(dataBuffer,
            dataBuffer.position(),
            (dataBuffer.position()  + NO_OF_OFFSETS_LENGTH  - 1));
        dataBuffer.position(dataBuffer.position() + NO_OF_OFFSETS_LENGTH );

        //First Offset, useful for sanity checks
        firstOffSet = Utils.getNumberBigEndian(dataBuffer,
                         dataBuffer.position(),
                        (dataBuffer.position()  + OFFSET_LENGTH  - 1));
    }

    /**
     * Show All offsets
     */
    public void printAlloffsets()
    {
        dataBuffer.rewind();
        dataBuffer.position(VERSION_FLAG_LENGTH  +  OTHER_FLAG_LENGTH + NO_OF_OFFSETS_LENGTH);
        for(int i=0;i<noOfOffSets -1;i++)
        {
            int offset = Utils.getNumberBigEndian(dataBuffer,
                         dataBuffer.position(),
                        (dataBuffer.position()  + OFFSET_LENGTH    - 1));
            System.out.println("offset into audio data is:"+offset);
            dataBuffer.position(dataBuffer.position() + OFFSET_LENGTH);
        }
        int offset = Utils.getNumberBigEndian(dataBuffer,
                         dataBuffer.position(),
                        (dataBuffer.position()  + OFFSET_LENGTH    - 1));
    }

     /**
     * Construct box from data and adjust offets accordingly
     *
     * @param header header info
     * @param originalDataBuffer data of box (doesnt include header data)
     */
    public Mp4StcoBox(Mp4BoxHeader header, ByteBuffer originalDataBuffer,int adjustment)
    {
        this.header  = header;

        //Make a slice of databuffer then we can work with relative or absolute methods safetly
        this.dataBuffer =  originalDataBuffer.slice();

        //Skip the flags
        dataBuffer.position(dataBuffer.position() + VERSION_FLAG_LENGTH  +  OTHER_FLAG_LENGTH);

        //No of offsets
        this.noOfOffSets = Utils.getNumberBigEndian(dataBuffer,
            dataBuffer.position(),
            (dataBuffer.position()  + NO_OF_OFFSETS_LENGTH  - 1));
        dataBuffer.position(dataBuffer.position() + NO_OF_OFFSETS_LENGTH );
              
        for(int i=0;i<noOfOffSets;i++)
        {
            int offset = Utils.getNumberBigEndian(dataBuffer,
                         dataBuffer.position(),
                        (dataBuffer.position()  + NO_OF_OFFSETS_LENGTH  - 1));

            //Calculate new offset and update buffer
            offset = offset + adjustment;
            dataBuffer.put(Utils.getSizeBigEndian(offset));
        }
    }

    /**
     * The number of offsets
     *
     * @return
     */
    public int getNoOfOffSets()
    {
        return noOfOffSets;
    }

    /**
     * The value of the first offset
     *
     * @return
     */
    public int getFirstOffSet()
    {
        return firstOffSet;
    }
}
