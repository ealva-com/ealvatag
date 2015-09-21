package org.jaudiotagger.audio.wav.chunk;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.Tag;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Stores basic only metadata but only seems to exist as part of a LIST chunk, doesn't have its own size field
 * instead contains a number of name,size, value tuples. So for this reason we dont subclass the Chunk class
 */
public class WavInfoChunk
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.wav.WavInfoChunk");

    private Tag tag;
    public WavInfoChunk(Tag tag)
    {
        this.tag=tag;
    }

    /**
     * Read Info chunk
     * @param chunkData
     */
    public  boolean readChunks(ByteBuffer chunkData)
    {
        while(chunkData.hasRemaining())
        {
            String id       = Utils.readFourBytesAsChars(chunkData);
            int    size     = chunkData.getInt();
            String value    = Utils.getString(chunkData, 0, size, StandardCharsets.UTF_8);
            System.out.println("Result:" + id + ":" + size + ":" + value+":");

            WavInfoIdentifier wii = WavInfoIdentifier.get(id);
            if(wii!=null && wii.getFieldKey()!=null)
            {
                try
                {

                    tag.setField(wii.getFieldKey(), value);
                }
                catch(FieldDataInvalidException fdie)
                {
                    fdie.printStackTrace();
                }
            }

            //Each tuple aligned on even byte boundary
            if ((size & 1) != 0 && chunkData.hasRemaining())
            {
                chunkData.get();
            }
        }
        return true;
    }
}
