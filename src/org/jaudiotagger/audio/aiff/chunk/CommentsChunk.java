package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.aiff.AiffAudioHeader;
import org.jaudiotagger.audio.aiff.AiffUtil;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.Chunk;
import org.jaudiotagger.audio.iff.ChunkHeader;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * <p>
 *     A comment consists of a time stamp, marker id, and a text count followed by text.
 * </p>
 * <pre>
 * typedef struct {
 *    unsigned long   timeStamp;
 *    MarkerID        marker;
 *    unsigned short  count;
 *    char            text[];
 * } Comment;
 * </pre>
 * <p>
 *     {@code timeStamp} indicates when the comment was created. Units are the number of seconds
 *     since January 1, 1904. (This time convention is the one used by the Macintosh. For procedures
 *     that manipulate the time stamp, see The Operating System Utilities chapter in Inside Macintosh,
 *     vol II). For a routine that will convert this to an Apple II GS/OS format time, please see
 *     Apple II File Type Note for filetype 0xD8, aux type 0x0000.
 * </p>
 * <p>
 *     A comment can be linked to a marker. This allows applications to store long descriptions of
 *     markers as a comment. If the comment is referring to a marker, then marker is the ID of that
 *     marker. Otherwise, marker is zero, indicating that this comment is not linked to a marker.
 * </p>
 * <p>
 *     {@code count} is the length of the text that makes up the comment. This is a 16 bit quantity,
 *     allowing much longer comments than would be available with a pstring.
 * </p>
 * <p>
 *     {@code text} contains the comment itself. This text must be padded with a byte at the end to
 *     insure that it is an even number of bytes in length. This pad byte, if present, is not
 *     included in count.
 * </p>
 *
 * @see AnnotationChunk
 */
public class CommentsChunk extends Chunk
{
    private static final int NUM_COMMENTS_LENGTH = 2;
    private static final int TIMESTAMP_LENGTH = 4;
    private static final int MARKERID_LENGTH = 2;
    private static final int COUNT_LENGTH = 2;

    private AiffAudioHeader aiffHeader;

    /**
     * @param chunkHeader The header for this chunk
     * @param raf The file from which the AIFF data are being read
     * @param aiffAudioHeader audio header
     */
    public CommentsChunk(final ChunkHeader chunkHeader, final RandomAccessFile raf, final AiffAudioHeader aiffAudioHeader)
    {
        super(raf, chunkHeader);
        this.aiffHeader = aiffAudioHeader;
    }

    /**
     * Reads a chunk and extracts information.
     *
     * @return <code>false</code> if the chunk is structurally
     * invalid, otherwise <code>true</code>
     */
    public boolean readChunk() throws IOException
    {
        final int numComments = Utils.readUint16(raf);
        bytesLeft -= NUM_COMMENTS_LENGTH;

        //For each comment
        for (int i = 0; i < numComments; i++)
        {
            final long timestamp  = Utils.readUint32(raf);
            final Date jTimestamp = AiffUtil.timestampToDate(timestamp);
            final int marker      = Utils.readInt16(raf);
            final int count       = Utils.readUint16(raf);
            bytesLeft -= TIMESTAMP_LENGTH + MARKERID_LENGTH + COUNT_LENGTH;
            final byte[] textBuffer = new byte[count];
            raf.read(textBuffer);
            bytesLeft -= count;
            if (count % 2 != 0) {
                // if count is odd, text is padded with an extra byte that we need to consume
                raf.skipBytes(1);
                bytesLeft--;
            }
            // comment plus timestamp
            final String text = new String(textBuffer, StandardCharsets.ISO_8859_1) + " " + AiffUtil.formatDate(jTimestamp);
            aiffHeader.addComment(text);
        }
        return true;
    }

}
