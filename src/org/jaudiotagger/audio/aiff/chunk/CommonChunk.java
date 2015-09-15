package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class CommonChunk extends Chunk
{

    private AiffAudioHeader aiffHeader;

    /**
     * Constructor.
     *
     * @param hdr  The header for this chunk
     * @param raf  The file from which the AIFF data are being read
     * @param aHdr The AiffTag into which information is stored
     */
    public CommonChunk(ChunkHeader hdr, RandomAccessFile raf, AiffAudioHeader aHdr)
    {
        super(raf, hdr);
        aiffHeader = aHdr;
    }


    @Override
    public boolean readChunk() throws IOException
    {
        int numChannels = Utils.readUint16(raf);
        long numSampleFrames = Utils.readUint32(raf);
        int sampleSize = Utils.readUint16(raf);
        bytesLeft -= 8;

        String compressionType = null;
        String compressionName = null;

        double sampleRate = AiffUtil.read80BitDouble(raf);
        bytesLeft -= 10;

        if (aiffHeader.getFileType() == AiffAudioHeader.FileType.AIFCTYPE)
        {
            if (bytesLeft == 0)
            {
                // This is a rather special case, but testing did turn up
                // a file that misbehaved in this way.
                return false;
            }
            compressionType = AiffUtil.read4Chars(raf);
            // According to David Ackerman, the compression type can
            // change the endianness of the document.
            if (compressionType.equals( AiffCompressionType.SOWT.getCode()))
            {
                aiffHeader.setEndian(AiffAudioHeader.Endian.LITTLE_ENDIAN);
            }
            bytesLeft -= 4;
            compressionName = AiffUtil.readPascalString(raf);
            bytesLeft -= compressionName.length() + 1;
        }

        // Proper handling of compression type should depend
        // on whether raw output is set
        if (compressionType != null)
        {
            AiffCompressionType act = AiffCompressionType.getByCode(compressionType);
            if (act != null)
            {
                compressionName = act.getDescription();
            }
            else
            {
                // We don't know, so we have to assume lossy
                aiffHeader.setLossless(false);
            }


            // TODO:The size of the data after compression isn't available
            // from the Common chunk, so we mark it as "unknown."
            // With a bit more sophistication, we could combine the
            // information from here and the Sound Data chunk to get
            // the effective byte rate

            if (compressionName.isEmpty())
            {
                aiffHeader.setAudioEncoding(compressionType);
            }
            aiffHeader.setAudioEncoding(compressionName);
        }

        aiffHeader.setBitsPerSample(sampleSize);
        aiffHeader.setSamplingRate((int) sampleRate);
        aiffHeader.setChannelNumber(numChannels);
        aiffHeader.setLength((int) (numSampleFrames / sampleRate));
        aiffHeader.setPreciseLength((float) (numSampleFrames / sampleRate));
        aiffHeader.setLossless(true);   // for all known compression types

        //TODO Hardcoded based on http://www.theaudioarchive.com/TAA_Resources_File_Size.htm#File_Calculator
        //should be sampling rate / sample rate * noofchannels but doesnt quite match up so using this
        //hardcoded value
        aiffHeader.setBitrate((int) ((705.60f) * numChannels));

        return true;

    }

}
