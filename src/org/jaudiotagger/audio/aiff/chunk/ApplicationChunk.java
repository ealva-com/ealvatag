package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The Application Chunk can be used for any purposes whatsoever by developers and application authors. For
 * example, an application that edits sounds might want to use this chunk to store editor state parameters such as
 * magnification levels, last cursor position, etc.
 */
public class ApplicationChunk extends Chunk
{
    private String SIGNATURE_PDOS = "pdos";
    private String SIGNATURE_STOC = "stoc";

    private AiffAudioHeader aiffHeader;

    /**
     * Constructor.
     *
     * @param hdr The header for this chunk
     * @param raf The file from which the AIFF data are being read
     */
    public ApplicationChunk(ChunkHeader hdr, RandomAccessFile raf, AiffAudioHeader aHdr)
    {
        super(raf, hdr);
        aiffHeader = aHdr;
    }

    /**
     * Reads a chunk and puts an Application property into
     * the RepInfo object.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        String applicationSignature = Utils.readString(raf, 4);
        String applicationName = null;
        byte[] data = new byte[(int) (bytesLeft - 4)];
        raf.readFully(data);

        /* If the application signature is 'pdos' or 'stoc',
         * then the beginning of the data area is a Pascal
         * string naming the application.  Otherwise, we
         * ignore the data.  ('pdos' is for Apple II
         * applications, 'stoc' for the entire non-Apple world.)
         */
        if (SIGNATURE_STOC.equals(applicationSignature) || SIGNATURE_PDOS.equals(applicationSignature))
        {
            applicationName = AiffUtil.bytesToPascalString(data);
        }
        aiffHeader.addApplicationIdentifier(applicationSignature + ": " + applicationName);

        return true;
    }
}
