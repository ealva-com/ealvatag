package org.jaudiotagger.audio.aiff.chunk;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.aiff.AiffTag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class ID3Chunk extends Chunk
{
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff.chunk");
    private AiffTag aiffTag;

    /**
     * Constructor.
     *
     * @param hdr The header for this chunk
     * @param raf The file from which the AIFF data are being read
     * @param tag The AiffTag into which information is stored
     */
    public ID3Chunk(ChunkHeader hdr, RandomAccessFile raf, AiffTag tag)
    {
        super(raf, hdr);
        aiffTag = tag;
    }

    @Override
    public boolean readChunk() throws IOException
    {
        ByteBuffer headerData = ByteBuffer.allocateDirect(AbstractID3v2Tag.FIELD_TAGID_LENGTH + AbstractID3v2Tag.FIELD_TAG_MAJOR_VERSION_LENGTH);
        raf.getChannel().read(headerData);
        headerData.position(0);

        if (!isId3v2Tag(headerData))
        {
            logger.severe("Invalid ID3 header for ID3 chunk");
            return false;
        }

        int version = headerData.get();
        AbstractID3v2Tag id3Tag;
        switch (version)
        {
            case ID3v22Tag.MAJOR_VERSION:
                id3Tag = new ID3v22Tag();
                AudioFile.logger.finest("Reading ID3V2.2 tag");
                break;
            case ID3v23Tag.MAJOR_VERSION:
                id3Tag = new ID3v23Tag();
                AudioFile.logger.finest("Reading ID3V2.3 tag");
                break;
            case ID3v24Tag.MAJOR_VERSION:
                id3Tag = new ID3v24Tag();
                AudioFile.logger.finest("Reading ID3V2.4 tag");
                break;
            default:
                return false;     // bad or unknown version
        }
        aiffTag.setID3Tag(id3Tag);
        raf.seek(raf.getFilePointer() - (AbstractID3v2Tag.FIELD_TAGID_LENGTH + AbstractID3v2Tag.FIELD_TAG_MAJOR_VERSION_LENGTH));

        //Includes the ID3Tag itself and the Aiff ID3 Chunk header, useful for tagwriter
        aiffTag.setStartLocationInFile(raf.getFilePointer() - ChunkHeader.CHUNK_HEADER_SIZE);
        aiffTag.setEndLocationInFile(aiffTag.getStartLocationInFile() + ChunkHeader.CHUNK_HEADER_SIZE + bytesLeft);

        byte[] buf = new byte[(int) bytesLeft];
        raf.read(buf);
        ByteBuffer bb = ByteBuffer.allocateDirect((int) bytesLeft);
        bb.put(buf);
        try
        {
            id3Tag.read(bb);
        }
        catch (TagException e)
        {
            AudioFile.logger.info("Exception reading ID3 tag: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Reads 3 bytes to determine if the tag really looks like ID3 data.
     */
    private boolean isId3v2Tag(ByteBuffer headerData) throws IOException
    {
        for (int i = 0; i < AbstractID3v2Tag.FIELD_TAGID_LENGTH; i++)
        {
            if (headerData.get() != AbstractID3v2Tag.TAG_ID[i])
            {
                return false;
            }
        }
        return true;
    }

}
