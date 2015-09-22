package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffType;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 The Common Chunk describes fundamental parameters of the waveform data such as sample rate,
 bit resolution, and how many channels of digital audio are stored in the FORM AIFF.
 */
public class CommonChunk extends Chunk
{
    private static final int NO_CHANNELS_LENGTH      = 2;
    private static final int NO_SAMPLE_FRAMES_LENGTH = 4;
    private static final int SAMPLE_SIZE_LENGTH      = 2;
    private static final int SAMPLE_RATE_LENGTH      = 10;
    private static final int COMPRESSION_TYPE_LENGTH      = 4;
    private AiffAudioHeader aiffHeader;

    /**
     *
     * @param hdr
     * @param chunkData
     * @param aiffAudioHeader
     */
    public CommonChunk(ChunkHeader hdr, ByteBuffer chunkData, AiffAudioHeader aiffAudioHeader)
    {
        super(chunkData, hdr);
        aiffHeader = aiffAudioHeader;
    }


    @Override
    public boolean readChunk() throws IOException
    {
        int numChannels      = Utils.u(chunkData.getShort());
        long numSampleFrames = chunkData.getInt();
        int sampleSize       = Utils.u(chunkData.getShort());
        double sampleRate    = AiffUtil.read80BitDouble(chunkData);

        //Compression format, but not necessarily compressed
        String compressionType;
        String compressionName;
        if (aiffHeader.getFileType() == AiffType.AIFC)
        {
            // This is a rather special case, but testing did turn up
            // a file that misbehaved in this way.
            if (chunkData.remaining()==0)
            {
                return false;
            }
            compressionType = Utils.readFourBytesAsChars(chunkData);
            if (compressionType.equals(AiffCompressionType.SOWT.getCode()))
            {
                aiffHeader.setEndian(AiffAudioHeader.Endian.LITTLE_ENDIAN);
            }
            compressionName = Utils.readPascalString(chunkData);

            //TODO This extra read fixes reading next chunk for ANNO, need more test cases to know
            //f error lies in file or code
            chunkData.get();

            // Proper handling of compression type should depend
            // on whether raw output is set
            if (compressionType != null)
            {
                //Is it a known compression type
                AiffCompressionType act = AiffCompressionType.getByCode(compressionType);
                if (act != null)
                {
                    compressionName = act.getCompression();
                    aiffHeader.setLossless(act.isLossless());
                    // we assume that the bitrate is not variable, if there is no compression
                    if (act == AiffCompressionType.NONE) {
                        aiffHeader.setVariableBitRate(false);
                    }
                }
                else
                {
                    // We don't know compression type, so we have to assume lossy compression as we know we are using AIFC format
                    aiffHeader.setLossless(false);
                }

                if (compressionName.isEmpty())
                {
                    aiffHeader.setAudioEncoding(compressionType);
                }
                else
                {
                    aiffHeader.setAudioEncoding(compressionName);
                }
            }
        }
        //Must be lossless
        else
        {
            aiffHeader.setLossless(true);
            aiffHeader.setAudioEncoding(AiffCompressionType.NONE.getCompression());
            // regular AIFF has no variable bit rate AFAIK
            aiffHeader.setVariableBitRate(false);
        }

        aiffHeader.setBitsPerSample(sampleSize);
        aiffHeader.setSamplingRate((int) sampleRate);
        aiffHeader.setChannelNumber(numChannels);
        aiffHeader.setLength((int) (numSampleFrames / sampleRate));
        aiffHeader.setPreciseLength((float) (numSampleFrames / sampleRate));

        if(aiffHeader.isLossless())
        {
            aiffHeader.setBitrate((int) (sampleRate * sampleSize * numChannels));
        }
        else
        {
            // TODO:The size of the data after compression isn't available
            // from the Common chunk
            // With a bit more sophistication, we could combine the
            // information from here and the Sound Data chunk to get
            // the effective byte rate
            // For now just ignore the issue
            aiffHeader.setBitrate((int) (sampleRate * sampleSize * numChannels));
        }
        return true;

    }

}
