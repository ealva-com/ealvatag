package org.jaudiotagger.audio.asf.data;

import org.jaudiotagger.audio.asf.util.ChunkPositionComparator;
import org.jaudiotagger.audio.asf.util.Utils;

import java.math.BigInteger;
import java.util.*;

/**
 * Stores multiple ASF objects (chunks) in form of {@link Chunk} objects, and is itself an ASF object (chunk).<br>  
 * 
 * @author Christian Laireiter
 */
public class ChunkContainer extends Chunk
{

    /**
     * Stores the {@link GUID} instances, which are allowed multiple times
     * within an ASF header.
     */
    private final static HashSet<GUID> MULTI_CHUNKS;

    static
    {
        MULTI_CHUNKS = new HashSet<GUID>();
        MULTI_CHUNKS.add(GUID.GUID_STREAM);
    }

    /**
     * Stores the {@link Chunk} objects to their {@link GUID}.
     */
    private final Map<GUID, List<Chunk>> chunkTable;

    /**
     * Creates an instance.
     * @param chunkGUID the GUID which identifies the chunk.
     * @param pos the position of the chunk within the stream.
     * @param length the length of the chunk.
     */
    public ChunkContainer(final GUID chunkGUID, final long pos, final BigInteger length)
    {
        super(chunkGUID, pos, length);
        this.chunkTable = new Hashtable<GUID, List<Chunk>>();
    }

    /**
     * Adds a chunk to the container.<br>
     * @param toAdd The chunk which is to be added.
     */
    public void addChunk(final Chunk toAdd)
    {
        List<Chunk> list = assertChunkList(toAdd.getGuid());
        if (!list.isEmpty() && !MULTI_CHUNKS.contains(toAdd.getGuid()))
        {
            throw new IllegalArgumentException("The GUID of the given chunk indicates, that there is no more instance allowed."); //$NON-NLS-1$
        }
        list.add(toAdd);
        assert chunkstartsUnique() : "Chunk has equal start position like an already inserted one."; //$NON-NLS-1$
    }

    /**
     * This method asserts that a {@link List} exists for the given {@link GUID}, 
     * in {@link #chunkTable}.<br>
     * @param lookFor The GUID to get list for.
     * @return an already existing, or newly created list.
     */
    protected List<Chunk> assertChunkList(final GUID lookFor)
    {
        List<Chunk> result = this.chunkTable.get(lookFor);
        if (result == null)
        {
            result = new ArrayList<Chunk>();
            this.chunkTable.put(lookFor, result);
        }
        return result;
    }

    /**
     * Tests whether all stored chunks have a unique starting position among
     * their brothers.
     * @return <code>true</code> if all chunks are located at an unique position. However, no intersection is tested.
     */
    private boolean chunkstartsUnique()
    {
        boolean result = true;
        HashSet<Long> chunkStarts = new HashSet<Long>();
        Collection<Chunk> chunks = getChunks();
        for (Chunk curr : chunks)
        {
            result &= chunkStarts.add(curr.getPosition());
        }
        return result;
    }

    /**
     * Returns a collection of all contained chunks.<br>
     * 
     * @return all contained chunks
     */
    public Collection<Chunk> getChunks()
    {
        final List<Chunk> result = new ArrayList<Chunk>();
        for (List<Chunk> curr : this.chunkTable.values())
        {
            result.addAll(curr);
        }
        return result;
    }

    /**
     * Looks for the first stored chunk which has the given GUID.
     * 
     * @param lookFor GUID to look up.
     * @param instanceOf The class which must additionally be matched. 
     * @return <code>null</code> if no chunk was found, or the stored instance doesn't match.
     */
    protected Chunk getFirst(GUID lookFor, Class<? extends Chunk> instanceOf)
    {
        List<Chunk> list = this.chunkTable.get(lookFor);
        if (list != null && !list.isEmpty())
        {
            Chunk result = list.get(0);
            if (result.getClass().isAssignableFrom(instanceOf))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String prettyPrint(String prefix)
    {
        return prettyPrint(prefix, "");
    }

    /**
     * Nearly the same as {@link #prettyPrint(String)} however, additional information can be injected
     * below the {@link Chunk#prettyPrint(String)} output and the listing of the contained chunks.<br>
     * 
     * @param prefix The prefix to prepend.
     * @param containerInfo Information to inject.
     * @return Information of current Chunk Object.
     */
    public String prettyPrint(final String prefix, final String containerInfo)
    {
        final StringBuffer result = new StringBuffer(super.prettyPrint(prefix));
        result.append(containerInfo);
        result.append(prefix + "  |" + Utils.LINE_SEPARATOR);
        final ArrayList<Chunk> list = new ArrayList<Chunk>(getChunks());
        Collections.sort(list, new ChunkPositionComparator());

        for (Chunk curr : list)
        {
            result.append(curr.prettyPrint(prefix + "  |"));
            result.append(prefix + "  |" + Utils.LINE_SEPARATOR);
        }
        return result.toString();
    }
}
