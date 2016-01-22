package org.jaudiotagger.audio.wav.chunk;

import org.jaudiotagger.audio.aiff.chunk.AiffChunkType;
import org.jaudiotagger.audio.iff.ChunkSummary;
import org.jaudiotagger.audio.wav.WavChunkType;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.wav.WavTag;

/**
 * AIFF Specific methods for ChunkSummarys
 */
public class WavChunkSummary
{
    /**
     * Checks that there are only id3 tags after the currently selected id3tag because this means its safe to truncate
     * the remainder of the file.
     *
     * @param tag
     * @return
     */
    public static boolean isOnlyMetadataTagsAfterStartingMetadataTag(WavTag tag)
    {
        boolean firstMetadataTag = false;
        for(ChunkSummary cs:tag.getChunkSummaryList())
        {
            if(firstMetadataTag)
            {
                if(
                        !cs.getChunkId().equals(WavChunkType.ID3.getCode()) &&
                        !cs.getChunkId().equals(WavChunkType.LIST.getCode()) &&
                        !cs.getChunkId().equals(WavChunkType.INFO.getCode())
                  )
                {
                    return false;
                }
            }
            else
            {
                if (cs.getFileStartLocation() == tag.getStartLocationInFileOfId3Chunk())
                {
                    //Found starting point
                    firstMetadataTag = true;
                }
            }
        }

        //Should always be true but this is to protect against something gone wrong
        if(firstMetadataTag==true)
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
    public static ChunkSummary getChunkBeforeStartingId3MetadataTag(WavTag tag)
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

    /**
     * Get chunk before starting metadata tag
     *
     * @param tag
     * @return
     */
    public static ChunkSummary getChunkBeforeStartingInfoMetadataTag(WavTag tag)
    {
        for(int i=0;i < tag.getChunkSummaryList().size(); i++)
        {
            ChunkSummary cs = tag.getChunkSummaryList().get(i);
            if (cs.getFileStartLocation() == tag.getInfoTag().getStartLocationInFile())
            {
                return tag.getChunkSummaryList().get(i - 1);
            }
        }
        return null;
    }
}
