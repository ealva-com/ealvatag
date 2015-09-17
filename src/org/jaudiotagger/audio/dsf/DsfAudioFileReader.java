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
import org.jaudiotagger.tag.id3.*;

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
    public static final int FMT_CHUNK_SIGNATURE_LENGTH = 4;
    public static final int FMT_CHUNK_SIZE_LENGTH = 8;
    public static final int FMT_CHUNK_MIN_DATA_SIZE_ = 40;


    @Override
    protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException
    {
        String type = Utils.readFourBytesAsChars(raf);
        if (DSD_SIGNATURE.equals(type))
        {
            raf.skipBytes(CHUNKSIZE_LENGTH + FILESIZE_LENGTH + METADATA_OFFSET_LENGTH);
            String fmt = Utils.readFourBytesAsChars(raf);
            if (FMT_SIGNATURE.equals(fmt))
            {
                long size = Long.reverseBytes(raf.readLong());
                long sizeExcludingChunkHeader = size - (FMT_CHUNK_SIGNATURE_LENGTH + FMT_CHUNK_SIZE_LENGTH);
                ByteBuffer audioData = ByteBuffer.allocate((int)sizeExcludingChunkHeader);
                raf.getChannel().read(audioData);
                audioData.position(0);
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

        audioHeader.setBitrate(bitsPerSample * samplingFreqency * channelNumber);
        audioHeader.setBitsPerSample(bitsPerSample);
        audioHeader.setChannelNumber(channelNumber);
        audioHeader.setSamplingRate(samplingFreqency);
        audioHeader.setLength((int) (sampleCount / samplingFreqency));
        audioHeader.setChannelNumber(channelNumber);
        audioHeader.setPreciseLength((float) sampleCount / samplingFreqency);
        audioHeader.setVariableBitRate(false);

        logger.log(Level.FINE, "Created audio header: " + audioHeader);

        return audioHeader;
    }

    @Override
    protected Tag getTag(RandomAccessFile raf) throws CannotReadException, IOException
    {
        String type = Utils.readFourBytesAsChars(raf);
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
    private Tag readTag(RandomAccessFile file, long tagOffset) throws IOException
    {
        file.seek(tagOffset);
        file.skipBytes(AbstractID3v2Tag.FIELD_TAGID_LENGTH);
        int majorVersion = file.readByte();
        file.skipBytes(AbstractID3v2Tag.FIELD_TAG_MINOR_VERSION_LENGTH + AbstractID3v2Tag.FIELD_TAG_FLAG_LENGTH);
        ByteBuffer tagBuffer = getTagBuffer(file, tagOffset);

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
                    break;
            }
        }
        catch (TagException e)
        {
            logger.log(Level.WARNING, "Could not create ID3v2 Tag. Returning an empty one.", e);
        }
        return new ID3v24Tag();
    }

    /**
     * Extract the Id3Tag and return as a ByteBuffer
     *
     * @param file
     * @param tagOffset
     * @return
     * @throws IOException
     */
    private ByteBuffer getTagBuffer(RandomAccessFile file, long tagOffset) throws IOException
    {
        byte[] sizeBytes = new byte[AbstractID3v2Tag.FIELD_TAG_SIZE_LENGTH];
        file.read(sizeBytes);
        int sizeOfId3Tag = ID3SyncSafeInteger.bufferToValue(sizeBytes);
        sizeOfId3Tag += AbstractID3v2Tag.TAG_HEADER_LENGTH;

        file.seek(tagOffset);
        byte[] tagBytes = new byte[sizeOfId3Tag];
        file.read(tagBytes);
        ByteBuffer tagBuffer = ByteBuffer.wrap(tagBytes);
        return tagBuffer;
    }
}
