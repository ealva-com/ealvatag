package org.jaudiotagger.audio.asf.io;

/**
 * Structure to tell the differences occurred by altering a chunk.
 * 
 * @author Christian Laireiter
 */
final class ModificationResult
{

    /**
     * Stores the difference of bytes.<br>
     */
    private final long byteDifference;

    /**
     * Stores the difference of the amount of chunks.<br>
     * &quot;-1&quot; if the chunk disappeared upon modification.<br>
     * &quot;0&quot; if the chunk was just modified.<br>
     * &quot;1&quot; if a chunk has been created.<br>
     */
    private final int chunkCountDifference;

    /**
     * Creates an instance.<br>
     * 
     * @param chunkCountDiff amount of chunks appeared, disappeared
     * @param bytesDiffer amount of bytes added or removed.
     */
    public ModificationResult(int chunkCountDiff, long bytesDiffer)
    {
        this.chunkCountDifference = chunkCountDiff;
        this.byteDifference = bytesDiffer;
    }

    /**
     * Returns the difference of bytes.
     * 
     * @return the byte difference
     */
    public long getByteDifference()
    {
        return this.byteDifference;
    }

    /**
     * Returns the difference of the amount of chunks. 
     * 
     * @return the chunk count difference
     */
    public int getChunkCountDifference()
    {
        return this.chunkCountDifference;
    }

}
