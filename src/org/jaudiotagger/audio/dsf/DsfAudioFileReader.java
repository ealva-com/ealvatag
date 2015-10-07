/*
 * Created on 03.05.2015
 * Author: Veselin Markov.
 */
package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;

/**
 * Reads the ID3 Tags as specified by <a href=
 * "http://dsd-guide.com/sites/default/files/white-papers/DSFFileFormatSpec_E.pdf"
 * /> DSFFileFormatSpec_E.pdf </a>.
 *
 * @author Veselin Markov (veselin_m84 a_t yahoo.com)
 */
public class DsfAudioFileReader extends AudioFileReader
{
    private static final String DSD_SIGNATURE = "DSD ";
    private static final String FMT_SIGNATURE = "fmt ";

    public static final int SIGNATURE_LENGTH = 4;
    public static final int CHUNKSIZE_LENGTH = 8;
    public static final int FILESIZE_LENGTH = 8;
    public static final int METADATA_OFFSET_LENGTH = 8;
    public static final int FMT_CHUNK_MIN_DATA_SIZE_ = 40;


    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        ByteBuffer headerBuffer = Utils.readFileDataIntoBufferLE(raf, SIGNATURE_LENGTH);
        String type = Utils.readFourBytesAsChars(headerBuffer);
        if (DSD_SIGNATURE.equals(type))
        {
            raf.skipBytes(CHUNKSIZE_LENGTH + FILESIZE_LENGTH + METADATA_OFFSET_LENGTH);
            headerBuffer = Utils.readFileDataIntoBufferLE(raf, SIGNATURE_LENGTH + CHUNKSIZE_LENGTH);
            String fmt = Utils.readFourBytesAsChars(headerBuffer);
            if (FMT_SIGNATURE.equals(fmt))
            {
                long size = headerBuffer.getLong();
                long sizeExcludingChunkHeader = size - (SIGNATURE_LENGTH + CHUNKSIZE_LENGTH);
                ByteBuffer audioData = Utils.readFileDataIntoBufferLE(raf, (int)sizeExcludingChunkHeader);
                return readAudioInfo(audioData);
            }
            else
            {
                throw new CannotReadException("Not a valid dsf file. Content does not start with 'fmt '.");
            }
        }
        else
        {
            throw new CannotReadException("Not a valid dsf file. Content does not start with 'DSD '.");
        }
    }

    /**
     * @param audioInfoChunk contains the bytes from "format version" up to "reserved"
     *                       fields
     * @return an empty {@link GenericAudioHeader} if audioInfoChunk has less
     * than 40 bytes, the read data otherwise. Never <code>null</code>.
     */
    @SuppressWarnings("unused")
    private GenericAudioHeader readAudioInfo(ByteBuffer audioInfoChunk)
    {
        GenericAudioHeader audioHeader = new GenericAudioHeader();
        if (audioInfoChunk.limit() < FMT_CHUNK_MIN_DATA_SIZE_)
        {
            logger.log(Level.WARNING, "Not enough bytes supplied for Generic audio header. Returning an empty one.");
            return audioHeader;
        }

        audioInfoChunk.order(ByteOrder.LITTLE_ENDIAN);
        int version = audioInfoChunk.getInt();
        int formatId =audioInfoChunk.getInt();
        int channelType =audioInfoChunk.getInt();
        int channelNumber = audioInfoChunk.getInt();
        int samplingFreqency = audioInfoChunk.getInt();
        int bitsPerSample =audioInfoChunk.getInt();
        long sampleCount = audioInfoChunk.getLong();
        int blocksPerSample = audioInfoChunk.getInt();

        audioHeader.setBitRate(bitsPerSample * samplingFreqency * channelNumber);
        audioHeader.setBitsPerSample(bitsPerSample);
        audioHeader.setChannelNumber(channelNumber);
        audioHeader.setSamplingRate(samplingFreqency);
        audioHeader.setNoOfSamples(sampleCount);
        audioHeader.setPreciseLength((float) sampleCount / samplingFreqency);
        audioHeader.setVariableBitRate(false);
        logger.log(Level.FINE, "Created audio header: " + audioHeader);

        return audioHeader;
    }

    @Override
    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException
    {
        String type = Utils.readFourBytesAsChars(Utils.readFileDataIntoBufferLE(raf, SIGNATURE_LENGTH));
        if (DSD_SIGNATURE.equals(type))
        {
            raf.skipBytes(CHUNKSIZE_LENGTH + FILESIZE_LENGTH);
            long offset = Long.reverseBytes(raf.readLong());
            return readTag(raf, offset);
        }
        else
        {
            throw new CannotReadException("Not a valid dsf file. Content does not start with 'DSD '.");
        }
    }

    /**
     * Reads the ID3v2 tag starting at the {@code tagOffset} position in the
     * supplied file.
     *
     * @param file      the file from which to read
     * @param tagOffset the offset where it ID3v2 tag begins
     * @return the read tag or an empty tag if something went wrong. Never
     * <code>null</code>.
     * @throws IOException if cannot read file.
     */
    private Tag readTag(RandomAccessFile file, long tagOffset) throws CannotReadException,IOException
    {
        //Move to start of ID3Tag and read rest of file into ByteBuffer
        file.seek(tagOffset);
        ByteBuffer tagBuffer = Utils.readFileDataIntoBufferLE(file, (int) (file.length() - file.getFilePointer()));

        //Work out ID3 major version
        int majorVersion = tagBuffer.get(AbstractID3v2Tag.FIELD_TAG_MAJOR_VERSION_POS);
        try
        {
            logger.log(Level.FINE, "Start creating ID3v2 Tag for version: " + majorVersion);
            switch (majorVersion)
            {
                case ID3v22Tag.MAJOR_VERSION:
                    return new ID3v22Tag(tagBuffer, "");
                case ID3v23Tag.MAJOR_VERSION:
                    return new ID3v23Tag(tagBuffer, "");
                case ID3v24Tag.MAJOR_VERSION:
                    return new ID3v24Tag(tagBuffer, "");
                default:
                    logger.log(Level.WARNING, "Unknown major ID3v2 version " + majorVersion + ". Returning an empty ID3v2 Tag.");
                    return new ID3v24Tag();
            }
        }
        catch (TagException e)
        {
            throw new CannotReadException("Could not create ID3v2 Tag");
        }
    }

}
