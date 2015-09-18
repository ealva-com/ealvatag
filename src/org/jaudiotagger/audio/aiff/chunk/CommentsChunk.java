package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
     *
     * @param hdr
     * @param chunkData
     * @param aHdr
     */
    public CommentsChunk(ChunkHeader hdr, ByteBuffer chunkData, AiffAudioHeader aHdr)
    {
        super(chunkData, hdr);
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
        int numComments = Utils.u(chunkData.getShort());

        //For each comment
        for (int i = 0; i < numComments; i++)
        {
            long timestamp  = Utils.u(chunkData.getInt());
            Date jTimestamp = AiffUtil.timestampToDate(timestamp);
            int marker      = Utils.u(chunkData.getShort());
            int count       = Utils.u(chunkData.getShort());
            String comments = Utils.getString(chunkData, 0, count, StandardCharsets.ISO_8859_1);            // Append a timestamp to the comment
            comments += " " + AiffUtil.formatDate(jTimestamp);
            aiffHeader.addComment(comments);
        }
        return true;
    }

}
