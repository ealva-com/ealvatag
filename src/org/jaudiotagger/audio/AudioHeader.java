package org.jaudiotagger.audio;

/**
 * Representation of AudioHeader
 *
 * <p>Contains info about the Audio Header
 */
public interface AudioHeader
{
    /**
     * @return the audio file type
     */
    public abstract String getEncodingType();

    /**
     * @return the ByteRate of the Audio, this is the total average amount of bytes of data sampled per second
     */
    public Integer getByteRate();



    /**
     * @return the BitRate of the Audio, this is the amount of kilobits of data sampled per second
     */
    public String getBitRate();

    /**
     * @return bitRate as a number, this is the amount of kilobits of data sampled per second
     */
    public long getBitRateAsNumber();


    /**
     * @return the Sampling rate, the number of samples taken per second
     */
    public String getSampleRate();

    /**
     * @return he Sampling rate, the number of samples taken per second
     */
    public int getSampleRateAsNumber();

    /**
     * @return the format
     */
    public String getFormat();

    /**
     * @return the number of channels (i.e 1 = Mono, 2 = Stereo)
     */
    public String getChannels();

    /**
     * @return if the sampling bitRate is variable or constant
     */
    public boolean isVariableBitRate();

    /**
     * @return track length in seconds
     */
    public int getTrackLength();
    
    /**
     * @return the number of bits in each sample
     */
    public int getBitsPerSample();

    /**
     *
     * @return if the audio codec is lossless or lossy
     */
    public boolean isLossless();
}
