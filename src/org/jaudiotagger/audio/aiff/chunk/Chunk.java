package org.jaudiotagger.audio.aiff.chunk;

import java.io.IOException;
import java.io.RandomAccessFile;



/**
 * Abstract superclass for IFF/AIFF chunks.
 * 
 * @author Gary McGath
 *
 */
public abstract class Chunk {

    public static final String CHUNK_FORMAT_VERSION = "FVER";
    public static final String CHUNK_APPLICATION = "APPL";
    public static final String CHUNK_COMMON = "COMM";
    public static final String CHUNK_COMMENTS = "COMT";
    public static final String CHUNK_NAME = "NAME";
    public static final String CHUNK_AUTHOR = "AUTH";
    public static final String CHUNK_COPYRIGHT = "(c) ";
    public static final String CHUNK_ANNOTATION = "ANNO";
    public static final String CHUNK_TAG = "ID3 ";



    protected long bytesLeft;
    protected RandomAccessFile raf;

    /**
     *  Constructor.
     * @param hdr      The header for this chunk
     */
    public Chunk (RandomAccessFile raf, ChunkHeader hdr)
    {
        this.raf = raf;
        bytesLeft = hdr.getSize ();
    }
    
    
    /** Reads a chunk and puts appropriate information into
     *  the RepInfo object. 
     *
     *  @return   <code>false</code> if the chunk is structurally
     *            invalid, otherwise <code>true</code>
     * 
     */
    public abstract boolean readChunk () throws IOException;
    
    /** Convert a byte buffer cleanly to an ASCII string.
     *  This is used for fixed-allocation strings in Broadcast
     *  WAVE chunks, and might have uses elsewhere.
     *  If a string is shorter than its fixed allocation, we're
     *  guaranteed only that there is a null terminating the string,
     *  and noise could follow it.  So we can't use the byte buffer
     *  constructor for a string.
     */
    protected String byteBufString (byte[] b)
    {
        StringBuffer sb = new StringBuffer (b.length);
        for (int i = 0; i < b.length; i++) {
            byte c = b[i];
            if (c == 0) {
                // Terminate when we see a null
                break;
            }
            sb.append((char) c);
        }
        return sb.toString ();
    }
}
