package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
typedef struct {
	  unsigned long   timeStamp;
	  MarkerID        marker;
	  unsigned short  count;
	  char            text[];
	} Comment;
 */
public class CommentsChunk extends Chunk
{
    public static final int NUM_COMMENTS_LENGTH = 2;
    public static final int TIMESTAMP_LENGTH = 4;
    public static final int MARKERID_LENGTH = 2;
    public static final int COUNT_LENGTH = 2;

    private AiffAudioHeader aiffHeader;

    /**
     * Constructor.
     *
     * @param hdr The header for this chunk
     * @param raf The file from which the AIFF data are being read
     */
    public CommentsChunk(ChunkHeader hdr, RandomAccessFile raf, AiffAudioHeader aHdr)
    {
        super(raf, hdr);
        aiffHeader = aHdr;
    }

    /**
     * Reads a chunk and extracts information.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        int numComments = Utils.readUint16(raf);
        bytesLeft -= NUM_COMMENTS_LENGTH;

        //For each comment
        for (int i = 0; i < numComments; i++)
        {
            long timestamp  = Utils.readUint32(raf);
            Date jTimestamp = AiffUtil.timestampToDate(timestamp);
            int marker      = Utils.readInt16(raf);
            int count       = Utils.readUint16(raf);
            bytesLeft       -= TIMESTAMP_LENGTH + MARKERID_LENGTH + COUNT_LENGTH;
            byte[] buf = new byte[count];
            raf.read(buf);
            bytesLeft -= count;
            String cmt = new String(buf);

            // Append a timestamp to the comment
            cmt += " " + AiffUtil.formatDate(jTimestamp);
            aiffHeader.addComment(cmt);
        }
        return true;
    }

}
