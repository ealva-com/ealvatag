package org.jaudiotagger.audio.iff;

import org.jaudiotagger.audio.aiff.chunk.ChunkType;
import org.jaudiotagger.logging.Hex;
import org.jaudiotagger.tag.aiff.AiffTag;

import java.util.List;

/**
 * Created by Paul on 22/01/2016.
 */
public class ChunkSummary
{
    private String chunkId;
    private long    fileStartLocation;
    private long chunkSize;

    public ChunkSummary(String chunkId, long fileStartLocation, long chunkSize)
    {
        this.chunkId=chunkId;
        this.fileStartLocation=fileStartLocation;
        this.chunkSize=chunkSize;
    }

    @Override
    public String toString()
    {
        long endLocation = fileStartLocation + chunkSize + ChunkHeader.CHUNK_HEADER_SIZE;
        return chunkId+":StartLocation:"+fileStartLocation + "(" + Hex.asHex(fileStartLocation) + "):SizeIncHeader:"+ chunkSize + ChunkHeader.CHUNK_HEADER_SIZE
                + ":EndLocation:"+(endLocation)
                +  "(" + Hex.asHex(endLocation) + ")";
    }

    public long getEndLocation()
    {
        return fileStartLocation + chunkSize + ChunkHeader.CHUNK_HEADER_SIZE;
    }
    public String getChunkId()
    {
        return chunkId;
    }

    public void setChunkId(String chunkId)
    {
        this.chunkId = chunkId;
    }

    public long getFileStartLocation()
    {
        return fileStartLocation;
    }

    public void setFileStartLocation(long fileStartLocation)
    {
        this.fileStartLocation = fileStartLocation;
    }

    public long getChunkSize()
    {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize)
    {
        this.chunkSize = chunkSize;
    }

    /**
     * Checks that there are only id3 tags after the currently selected id3tag because this means its safe to truncate
     * the remainder of the file.
     *
     * @param tag
     * @return
     */
    public static boolean isOnlyMetadataTagsAfterStartingMetadataTag(AiffTag tag)
    {
        boolean firstId3Tag = false;
        for(ChunkSummary cs:tag.getChunkSummaryList())
        {
            if(firstId3Tag)
            {
                if(!cs.chunkId.equals(ChunkType.TAG.getCode()))
                {
                    return false;
                }
            }
            else
            {
                if (cs.getFileStartLocation() == tag.getStartLocationInFileOfId3Chunk())
                {
                    //Found starting point
                    firstId3Tag = true;
                }
            }
        }

        //Should always be true but this is to protect against something gone wrong
        if(firstId3Tag==true)
        {
            return true;
        }
        return false;

    }

    /**
     * Get chunk before starting metadata tag
     *
     * @param tag
     * @return
     */
    public static ChunkSummary getChunkBeforeStartingMetadataTag(AiffTag tag)
    {
        for(int i=0;i < tag.getChunkSummaryList().size(); i++)
        {
            ChunkSummary cs = tag.getChunkSummaryList().get(i);
            if (cs.getFileStartLocation() == tag.getStartLocationInFileOfId3Chunk())
            {
                return tag.getChunkSummaryList().get(i - 1);
            }
        }
        return null;
    }
}
