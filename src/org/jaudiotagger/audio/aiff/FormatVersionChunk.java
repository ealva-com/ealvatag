package org.jaudiotagger.audio.aiff;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldDataInvalidException;

public class FormatVersionChunk extends Chunk {

    
    private Date chunkTimestamp;
    
    private AiffTag aiffTag;
    
    /**
     * Constructor.
     * 
     * @param hdr      The header for this chunk
     * @param raf      The file from which the AIFF data are being read
     * @param tag      The AiffTag into which information is stored
     */
    public FormatVersionChunk (
            ChunkHeader hdr, 
            RandomAccessFile raf,
            AiffTag tag)
    {
        super (raf);
        aiffTag = tag;
    }
    
    /** Reads a chunk and puts a FormatVersion property into
     *  the RepInfo object. 
     * 
     *  @return   <code>false</code> if the chunk is structurally
     *            invalid, otherwise <code>true</code>
     */
    public boolean readChunk () throws IOException
    {
        long timestamp = Utils.readUint32(raf);
        // The timestamp is in seconds since January 1, 1904.
        // We must convert to Java time.
        String timestampStr = AiffUtil.timestampToDate (timestamp);
        try {
            aiffTag.setField(AiffTagFieldKey.TIMESTAMP, timestampStr);
        }
        catch (FieldDataInvalidException e) {}
        return true;
    }

}
