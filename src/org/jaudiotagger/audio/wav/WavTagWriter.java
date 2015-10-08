/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.audio.wav;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.TagWriter;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.audio.iff.IffHeaderChunk;
import org.jaudiotagger.audio.wav.chunk.WavInfoIdentifier;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.AbstractID3Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.wav.WavInfoTag;
import org.jaudiotagger.tag.wav.WavTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.Logger;

import static org.jaudiotagger.audio.iff.IffHeaderChunk.SIGNATURE_LENGTH;
import static org.jaudiotagger.audio.iff.IffHeaderChunk.SIZE_LENGTH;

/**
 * Write Wav Tag.
 */
public class WavTagWriter implements TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.Wav");

    /**
     * Read existing metadata
     *
     * @param raf
     * @return tags within Tag wrapper
     * @throws IOException
     * @throws CannotWriteException
     */
    private WavTag getExistingMetadata(RandomAccessFile raf) throws IOException, CannotWriteException
    {
        try
        {
            //Find WavTag (if any)
            WavTagReader im = new WavTagReader();
            return im.read(raf);
        }
        catch (CannotReadException ex)
        {
            throw new CannotWriteException("Failed to read file");
        }
    }

    /**
     * Seek in file to start of LIST Metadata chunk
     *
     * @param raf
     * @param existingTag
     * @throws IOException
     * @throws CannotWriteException
     */
    private ChunkHeader seekToStartOfListInfoMetadata(RandomAccessFile raf, WavTag existingTag) throws IOException, CannotWriteException
    {
        raf.seek(existingTag.getInfoTag().getStartLocationInFile());
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        chunkHeader.readHeader(raf);
        raf.seek(raf.getFilePointer() - ChunkHeader.CHUNK_HEADER_SIZE);

        if (!WavChunkType.LIST.getCode().equals(chunkHeader.getID()))
        {
            throw new CannotWriteException("Unable to find List chunk at original location has file been modified externally");
        }
        return chunkHeader;
    }

    /**
     * Seek in file to start of LIST Metadata chunk
     *
     * @param raf
     * @param existingTag
     * @throws IOException
     * @throws CannotWriteException
     */
    private ChunkHeader seekToStartOfId3Metadata(RandomAccessFile raf, WavTag existingTag) throws IOException, CannotWriteException
    {
        raf.seek(existingTag.getStartLocationInFileOfId3Chunk());
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.LITTLE_ENDIAN);
        chunkHeader.readHeader(raf);
        raf.seek(raf.getFilePointer() - ChunkHeader.CHUNK_HEADER_SIZE);
        if (!WavChunkType.ID3.getCode().equals(chunkHeader.getID()))
        {
            throw new CannotWriteException("Unable to find ID3 chunk at original location has file been modified externally");
        }
        return chunkHeader;
    }

    /**
     * Delete any existing metadata tags from files
     *
     * @param tag     tag, must be instance of {@link org.jaudiotagger.tag.wav.WavTag}
     * @param raf     random access file
     * @param tempRaf temporary random access file
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
        logger.info("Deleting metadata from file");
        WavTag existingTag = getExistingMetadata(raf);
        try
        {
            //have both tags
            if(existingTag.isExistingId3Tag() && existingTag.isExistingInfoTag())
            {
                BothTagsFileStructure fs = checkExistingLocations(existingTag, raf);
                //We can delete both chunks in one go
                if(fs.isContiguous)
                {
                    //Quick method
                    if(fs.isAtEnd)
                    {
                        if(fs.isInfoTagFirst)
                        {
                            logger.info("Setting new length to:" + existingTag.getInfoTag().getStartLocationInFile());
                            raf.setLength(existingTag.getInfoTag().getStartLocationInFile());
                        }
                        else
                        {
                            logger.info("Setting new length to:" + existingTag.getStartLocationInFileOfId3Chunk());
                            raf.setLength(existingTag.getStartLocationInFileOfId3Chunk());
                        }
                    }
                    //Slower
                    else
                    {
                        if(fs.isInfoTagFirst)
                        {
                            final int lengthTagChunk = (int)(existingTag.getEndLocationInFileOfId3Chunk() - existingTag.getInfoTag().getStartLocationInFile());
                            deleteTagChunk(raf, (int)existingTag.getEndLocationInFileOfId3Chunk(), lengthTagChunk);
                        }
                        else
                        {
                            final int lengthTagChunk = (int)(existingTag.getInfoTag().getEndLocationInFile().intValue()
                                    - existingTag.getStartLocationInFileOfId3Chunk());
                            deleteTagChunk(raf, (int)existingTag.getInfoTag().getEndLocationInFile().intValue(), lengthTagChunk);
                        }
                    }
                }
                //Tricky to delete both because once one is deleted affects the location of the other
                else
                {
                    WavInfoTag existingInfoTag = existingTag.getInfoTag();
                    ChunkHeader infoChunkHeader = seekToStartOfListInfoMetadata(raf, existingTag);

                    AbstractID3v2Tag existingID3Tag = existingTag.getID3Tag();
                    ChunkHeader id3ChunkHeader = seekToStartOfId3Metadata(raf, existingTag);

                    //If one of these two at end of file delete first then remove the other as a chunk
                    if (existingInfoTag.getEndLocationInFile() == raf.length())
                    {
                        raf.setLength(existingInfoTag.getStartLocationInFile());
                        deleteId3TagChunk(raf, existingTag,id3ChunkHeader);
                    }
                    else if (existingID3Tag.getEndLocationInFile() == raf.length())
                    {
                        raf.setLength(existingTag.getStartLocationInFileOfId3Chunk());
                        deleteInfoTagChunk(raf, existingTag, infoChunkHeader);
                    }
                    else
                    {
                        deleteId3TagChunk(raf, existingTag,id3ChunkHeader);
                        //Reread then delete other tag
                        existingTag = getExistingMetadata(raf);
                        deleteInfoTagChunk(raf, existingTag, infoChunkHeader);
                    }
                }
            }
            //Delete Info if exists
            else if(existingTag.isExistingInfoTag())
            {
                WavInfoTag existingInfoTag = existingTag.getInfoTag();
                ChunkHeader chunkHeader = seekToStartOfListInfoMetadata(raf, existingTag);
                //and it is at end of the file
                if (existingInfoTag.getEndLocationInFile() == raf.length())
                {
                    logger.info("Setting new length to:" + existingInfoTag.getStartLocationInFile());
                    raf.setLength(existingInfoTag.getStartLocationInFile());
                }
                else
                {
                    deleteInfoTagChunk(raf, existingTag, chunkHeader);
                }
            }
            else if(existingTag.isExistingId3Tag())
            {
                AbstractID3v2Tag existingID3Tag = existingTag.getID3Tag();
                ChunkHeader chunkHeader = seekToStartOfId3Metadata(raf, existingTag);
                //and it is at end of the file
                if (existingID3Tag.getEndLocationInFile() == raf.length())
                {
                    logger.info("Setting new length to:" + existingTag.getStartLocationInFileOfId3Chunk());
                    raf.setLength(existingTag.getStartLocationInFileOfId3Chunk());
                }
                else
                {
                    deleteId3TagChunk(raf, existingTag, chunkHeader);
                }
            }
            else
            {
                //Nothing to delete
            }
        }
        finally
        {
            rewriteRiffHeaderSize(raf);
        }
    }

    /**
     * Delete existing Info Tag
     *
     * @param raf
     * @param existingTag
     * @param chunkHeader
     * @throws IOException
     */
    private void deleteInfoTagChunk(final RandomAccessFile raf, final WavTag existingTag, final ChunkHeader chunkHeader) throws IOException
    {
        final WavInfoTag existingInfoTag = existingTag.getInfoTag();
        final int lengthTagChunk = (int) chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE;
        deleteTagChunk(raf, existingInfoTag.getEndLocationInFile().intValue(), lengthTagChunk);
    }

    /**
     * Delete existing Id3 Tag
     *
     * @param raf
     * @param existingTag
     * @param chunkHeader
     * @throws IOException
     */
    private void deleteId3TagChunk(final RandomAccessFile raf, final WavTag existingTag, final ChunkHeader chunkHeader) throws IOException
    {
        final int lengthTagChunk = (int) chunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE;
        deleteTagChunk(raf, (int)existingTag.getEndLocationInFileOfId3Chunk(), lengthTagChunk);
    }

    /**
     * Delete Tag Chunk
     *
     * Can be used when chunk is not the last chunk
     *
     * Continually copy a 4mb chunk, write the chunk and repeat until the rest of the file after the tag
     * is rewritten
     *
     * @param raf
     * @param endOfExistingChunk
     * @param lengthTagChunk
     * @throws IOException
     */
    private void deleteTagChunk(final RandomAccessFile raf, int endOfExistingChunk,  final int lengthTagChunk) throws IOException
    {
        //Position for reading after the tag
        raf.seek(endOfExistingChunk);
        final FileChannel channel = raf.getChannel();

        final ByteBuffer buffer = ByteBuffer.allocate((int)TagOptionSingleton.getInstance().getWriteChunkSize());
        while (channel.read(buffer) >= 0 || buffer.position() != 0)
        {
            buffer.flip();
            final long readPosition = channel.position();
            channel.position(readPosition - lengthTagChunk - buffer.limit());
            channel.write(buffer);
            channel.position(readPosition);
            buffer.compact();
        }
        //Truncate the file after the last chunk
        final long newLength = raf.length() - lengthTagChunk;
        logger.config("Setting new length to:" + newLength);
        raf.setLength(newLength);
    }

    /**
     * Write {@link org.jaudiotagger.tag.Tag} to file.
     *
     * @param af      audio file
     * @param tag     tag, must be instance of {@link org.jaudiotagger.tag.wav.WavTag}
     * @param raf     random access file
     * @param rafTemp temporary random access file
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    @Override
    public void write(final AudioFile af, final Tag tag, final RandomAccessFile raf, final RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        logger.info("Writing tag to file");

        WavSaveOptions wso = TagOptionSingleton.getInstance().getWavSaveOptions();
        final WavTag existingTag = getExistingMetadata(raf);

        try
        {
            final WavTag wavTag = (WavTag) tag;
            if(wso==WavSaveOptions.SAVE_BOTH)
            {
                saveBoth(wavTag, raf, existingTag);
            }
            else if(wso==WavSaveOptions.SAVE_ACTIVE)
            {
                saveActive(wavTag, raf, existingTag);
            }
            else if(wso==WavSaveOptions.SAVE_EXISTING_AND_ACTIVE)
            {
                saveActiveExisting(wavTag, raf, existingTag);
            }
            //Invalid Option, should never happen
            else
            {
                throw new RuntimeException("No setting for:WavSaveOptions");
            }
            rewriteRiffHeaderSize(raf);
        }
        finally
        {
            raf.close();
        }
    }

    /**
     * Rewrite RAF header to reflect new file size
     *
     * @param raf
     * @throws IOException
     */
    private void rewriteRiffHeaderSize(RandomAccessFile raf) throws IOException
    {
        raf.seek(IffHeaderChunk.SIGNATURE_LENGTH);
        raf.write(Utils.getSizeLEInt32(((int) raf.length()) - SIGNATURE_LENGTH - SIZE_LENGTH));
    }

    /**
     * Write LISTINFOChunk of specified size to current file location
     *
     * @param raf       random access file
     * @param bb        data to write
     * @param chunkSize chunk size
     * @throws java.io.IOException
     */
    private void writeInfoDataToFile(final RandomAccessFile raf, final ByteBuffer bb, final long chunkSize) throws IOException
    {
        //Now Write LIST header
        final ByteBuffer listBuffer = ByteBuffer.allocate(ChunkHeader.CHUNK_HEADER_SIZE);
        listBuffer.order(ByteOrder.LITTLE_ENDIAN);
        listBuffer.put(WavChunkType.LIST.getCode().getBytes(StandardCharsets.US_ASCII));
        listBuffer.putInt((int) chunkSize);
        listBuffer.flip();
        raf.getChannel().write(listBuffer);
        //Now write actual data
        raf.getChannel().write(bb);
    }

    /**
     * Write Id3Chunk of specified size to current file location
     *
     * @param raf       random access file
     * @param bb        data to write
     * @param chunkSize chunk size
     * @throws java.io.IOException
     */
    private void writeID3DataToFile(final RandomAccessFile raf, final ByteBuffer bb, final long chunkSize) throws IOException
    {
        //Now Write LIST header
        final ByteBuffer listBuffer = ByteBuffer.allocate(ChunkHeader.CHUNK_HEADER_SIZE);
        listBuffer.order(ByteOrder.LITTLE_ENDIAN);
        listBuffer.put(WavChunkType.ID3.getCode().getBytes(StandardCharsets.US_ASCII));
        listBuffer.putInt((int) chunkSize);
        listBuffer.flip();
        raf.getChannel().write(listBuffer);
        //Now write actual data
        raf.getChannel().write(bb);
    }

    private void writePaddingToFile(final RandomAccessFile raf, final int paddingSize) throws IOException
    {
        raf.write(new byte[paddingSize]);
    }

    /**
     * Converts INfO tag to {@link java.nio.ByteBuffer}.
     *
     * @param tag tag
     * @return byte buffer containing the tag data
     * @throws java.io.UnsupportedEncodingException
     */
    public ByteBuffer convertInfoChunk(final WavTag tag) throws UnsupportedEncodingException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            WavInfoTag wif = tag.getInfoTag();

            //Write the Info chunks
            Iterator<TagField> i = wif.getFields();
            while (i.hasNext())
            {
                TagTextField next = (TagTextField) i.next();
                WavInfoIdentifier wii = WavInfoIdentifier.getByByFieldKey(FieldKey.valueOf(next.getId()));
                baos.write(wii.getCode().getBytes(StandardCharsets.US_ASCII));
                logger.config("Writing:" + wii.getCode() + ":" + next.getContent());

                //TODO Is UTF8 allowed format
                byte[] contentConvertedToBytes = next.getContent().getBytes(StandardCharsets.UTF_8);
                baos.write(Utils.getSizeLEInt32(contentConvertedToBytes.length));
                baos.write(contentConvertedToBytes);

                //Write extra byte if data length not equal
                if ((contentConvertedToBytes.length & 1) != 0)
                {
                    baos.write(0);
                }
            }

            //Write any existing unrecognized tuples
            Iterator<TagTextField> ti = wif.getUnrecognisedFields().iterator();
            while(ti.hasNext())
            {
                TagTextField next = (TagTextField)ti.next();
                baos.write(next.getId().getBytes(StandardCharsets.US_ASCII));
                logger.config("Writing:" +next.getId() + ":" + next.getContent());
                byte[] contentConvertedToBytes = next.getContent().getBytes(StandardCharsets.UTF_8);
                baos.write(Utils.getSizeLEInt32(contentConvertedToBytes.length));
                baos.write(contentConvertedToBytes);

                //Write extra byte if data length not equal
                if ((contentConvertedToBytes.length & 1) != 0)
                {
                    baos.write(0);
                }
            }

            final ByteBuffer infoBuffer = ByteBuffer.wrap(baos.toByteArray());
            infoBuffer.rewind();

            //Now Write INFO header
            final ByteBuffer infoHeaderBuffer = ByteBuffer.allocate(SIGNATURE_LENGTH);
            infoHeaderBuffer.put(WavChunkType.INFO.getCode().getBytes(StandardCharsets.US_ASCII));
            infoHeaderBuffer.flip();


            //Construct a single ByteBuffer from both
            ByteBuffer listInfoBuffer = ByteBuffer.allocateDirect(infoHeaderBuffer.limit() + infoBuffer.limit());
            listInfoBuffer.put(infoHeaderBuffer);
            listInfoBuffer.put(infoBuffer);
            listInfoBuffer.flip();
            return listInfoBuffer;
        }
        catch (IOException ioe)
        {
            //Should never happen as not writing to file at this point
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Converts ID3tag to {@link ByteBuffer}.
     *
     * @param tag tag containing ID3tag
     * @return byte buffer containing the tag data
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer convertID3Chunk(final WavTag tag) throws UnsupportedEncodingException
    {
        try
        {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tag.getID3Tag().write(baos);
            final ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
            buf.rewind();
            return buf;
        }
        catch (IOException ioe)
        {
            //Should never happen as not writing to file at this point
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Used when writing both tags to work out the best way to do it
     */
    class BothTagsFileStructure
    {
        boolean isInfoTagFirst = false;
        boolean isContiguous   = false;
        boolean isAtEnd        = false;
    }

    private BothTagsFileStructure checkExistingLocations(WavTag wavTag, RandomAccessFile raf) throws IOException
    {
        BothTagsFileStructure fs = new BothTagsFileStructure();
        if(wavTag.getInfoTag().getStartLocationInFile() < wavTag.getID3Tag().getStartLocationInFile())
        {
            fs.isInfoTagFirst = true;
            //Must allow for odd size chunks
            if(Math.abs(wavTag.getInfoTag().getEndLocationInFile() - wavTag.getID3Tag().getStartLocationInFile()) <=1)
            {
                fs.isContiguous = true;
                if(wavTag.getID3Tag().getEndLocationInFile() == raf.length())
                {
                    fs.isAtEnd = true;
                }
            }
        }
        else
        {
            //Must allow for odd size chunks
            if(Math.abs(wavTag.getID3Tag().getEndLocationInFile() - wavTag.getInfoTag().getStartLocationInFile()) <=1)
            {
                fs.isContiguous = true;
                if(wavTag.getInfoTag().getEndLocationInFile() == raf.length())
                {
                    fs.isAtEnd = true;
                }
            }
        }
        return fs;
    }

    /**
     * Write Info chunk to current location which is last chunk of file
     *
     * @param raf
     * @param existingInfoTag
     * @param newTagBuffer
     * @throws CannotWriteException
     * @throws IOException
     */
    private void writeInfoChunk(RandomAccessFile raf, final WavInfoTag existingInfoTag, ByteBuffer newTagBuffer)
            throws CannotWriteException, IOException
    {
        long newInfoTagSize = newTagBuffer.limit();
        //We have enough existing space in chunk so just keep existing chunk size
        if (existingInfoTag.getSizeOfTag() >= newInfoTagSize)
        {
            writeInfoDataToFile(raf, newTagBuffer, existingInfoTag.getSizeOfTag());
            //To ensure old data from previous tag are erased
            if (existingInfoTag.getSizeOfTag() > newInfoTagSize)
            {
                writePaddingToFile(raf, (int) (existingInfoTag.getSizeOfTag() - newInfoTagSize));
            }
        }
        //New tag is larger so set chunk size to accommodate it
        else
        {
            writeInfoDataToFile(raf, newTagBuffer, newInfoTagSize);
        }
    }

    /**
     * Write Id3 chunk to current location which is last chunk of file
     *
     * @param raf
     * @param existingTag
     * @param newTagBuffer
     * @throws CannotWriteException
     * @throws IOException
     */
    private void writeId3Chunk(RandomAccessFile raf, final AbstractID3Tag existingTag, ByteBuffer newTagBuffer)
            throws CannotWriteException, IOException
    {
        long newId3TagSize = newTagBuffer.limit();
        //We have enough existing space in chunk so just keep existing chunk size
        if (existingTag.getSize() >= newId3TagSize)
        {
            writeID3DataToFile(raf, newTagBuffer, existingTag.getSize());
            //To ensure old data from previous tag are erased
            if (existingTag.getSize() > newId3TagSize)
            {
                writePaddingToFile(raf, (int) (existingTag.getSize() - newId3TagSize));
            }
        }
        //New tag is larger so set chunk size to accommodate it
        else
        {
            writeID3DataToFile(raf, newTagBuffer, newId3TagSize);
        }
    }

    /**
     * Seek to location of existing Info chunk and replace with new one
     *
     * @param raf
     * @param existingTag
     * @param newTagBuffer
     * @throws CannotWriteException
     * @throws IOException
     */
    private void seekAndWriteInfoChunk(RandomAccessFile raf,final WavTag existingTag, ByteBuffer newTagBuffer)
            throws CannotWriteException, IOException
    {
        ChunkHeader infoChunkHeader = seekToStartOfListInfoMetadata(raf, existingTag);
        if (existingTag.getInfoTag().getEndLocationInFile() == raf.length())
        {
            writeInfoChunk(raf, existingTag.getInfoTag(), newTagBuffer);
        }
        else
        {
            deleteInfoTagChunk(raf,existingTag, infoChunkHeader);
            raf.seek(raf.length());
            writeInfoDataToFile(raf, newTagBuffer, newTagBuffer.limit());
        }
    }

    /**
     * Seek to location of existing ID3 chunk and replace with new one
     *
     * @param raf
     * @param existingTag
     * @param newTagBuffer
     * @throws CannotWriteException
     * @throws IOException
     */
    private void seekAndWriteId3Chunk(RandomAccessFile raf,final WavTag existingTag, ByteBuffer newTagBuffer)
            throws CannotWriteException, IOException
    {
        ChunkHeader id3ChunkHeader = seekToStartOfId3Metadata(raf, existingTag);
        if (existingTag.getID3Tag().getEndLocationInFile() == raf.length())
        {
            writeId3Chunk(raf, existingTag.getID3Tag(), newTagBuffer);
        }
        else
        {
            deleteInfoTagChunk(raf,existingTag, id3ChunkHeader);
            raf.seek(raf.length());
            writeID3DataToFile(raf, newTagBuffer, newTagBuffer.limit());
        }
    }

    /**
     * Save both Info and ID3 chunk
     *
     * @param wavTag
     * @param raf
     * @param existingTag
     * @throws CannotWriteException
     * @throws IOException
     */
    private void saveBoth(WavTag wavTag, RandomAccessFile raf,  final WavTag existingTag )
            throws CannotWriteException, IOException
    {
        //Convert both tags and get existing ones
        final ByteBuffer infoTagBuffer = convertInfoChunk(wavTag);
        final long newInfoTagSize = infoTagBuffer.limit();
        final WavInfoTag existingInfoTag = existingTag.getInfoTag();

        final ByteBuffer id3TagBuffer = convertID3Chunk(wavTag);
        final AbstractID3v2Tag existingId3Tag = existingTag.getID3Tag();

        //If both tags already exist in file
        if(existingTag.isExistingInfoTag() && existingTag.isExistingId3Tag())
        {
            BothTagsFileStructure fs = checkExistingLocations(wavTag, raf);
            //We can write both chunks without affecting anything else
            if(fs.isContiguous && fs.isAtEnd)
            {
                if (fs.isInfoTagFirst)
                {
                    seekToStartOfListInfoMetadata(raf, existingTag);
                    writeInfoChunk(raf, existingInfoTag, infoTagBuffer );
                    writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
                }
                else
                {
                    seekToStartOfId3Metadata(raf, existingTag);
                    writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
                    writeInfoChunk(raf, existingInfoTag, infoTagBuffer);
                }
            }
            //Both chunks are together but there is another chunk after them
            else
            {
                raf.seek(raf.length());
                writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
                writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
            }
        }
        //If only INFO chunk exists
        else if(existingTag.isExistingInfoTag() && !existingTag.isExistingId3Tag())
        {
            ChunkHeader infoChunkHeader = seekToStartOfListInfoMetadata(raf, existingTag);
            if (existingInfoTag.getEndLocationInFile() == raf.length())
            {
                writeInfoChunk(raf, existingInfoTag, infoTagBuffer );
                writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
            }
            else
            {
                deleteInfoTagChunk(raf, existingTag, infoChunkHeader);
                raf.seek(raf.length());
                writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
                writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
            }
        }
        //If only ID3 chunk exists
        else if(!existingTag.isExistingInfoTag() && existingTag.isExistingId3Tag())
        {
            ChunkHeader id3ChunkHeader = seekToStartOfId3Metadata(raf, existingTag);
            if (existingId3Tag.getEndLocationInFile() == raf.length())
            {
                writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
                writeInfoChunk(raf, existingInfoTag, infoTagBuffer );
            }
            else
            {
                deleteId3TagChunk(raf, existingTag, id3ChunkHeader);
                raf.seek(raf.length());
                writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
                writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
            }
        }
        //No existing tags so write both to end
        else
        {
            //Go to end of file
            raf.seek(raf.length());
            writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
            writeId3Chunk(raf, existingId3Tag, id3TagBuffer );
        }
    }

    /**
     * Save Active chunk only, if a non-active metadata chunk exists will be removed
     *
     * @param wavTag
     * @param raf
     * @param existingTag
     * @throws CannotWriteException
     * @throws IOException
     */
    private void saveActive(WavTag wavTag, RandomAccessFile raf,  final WavTag existingTag )
            throws CannotWriteException, IOException
    {
        if(wavTag.getActiveTag() instanceof WavInfoTag)
        {
            final ByteBuffer infoTagBuffer = convertInfoChunk(wavTag);
            final long newInfoTagSize = infoTagBuffer.limit();

            if(existingTag.isExistingId3Tag())
            {
                ChunkHeader id3ChunkHeader = seekToStartOfId3Metadata(raf, existingTag);
                if (existingTag.getEndLocationInFileOfId3Chunk() == raf.length())
                {
                    raf.setLength(existingTag.getStartLocationInFileOfId3Chunk());
                    raf.seek(raf.length());
                    writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
                }
                else
                {
                    deleteId3TagChunk(raf,existingTag, id3ChunkHeader);
                    raf.seek(raf.length());
                    writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
                }
            }
            else
            {
                if (existingTag.isExistingInfoTag())
                {
                    seekAndWriteInfoChunk(raf, existingTag, infoTagBuffer);
                }
                else
                {
                    raf.seek(raf.length());
                    writeInfoDataToFile(raf, infoTagBuffer, newInfoTagSize);
                }
            }
        }
        else
        {
            final ByteBuffer id3TagBuffer = convertID3Chunk(wavTag);
            final long newInfoTagSize = id3TagBuffer.limit();

            if(existingTag.isExistingInfoTag())
            {
                ChunkHeader infoChunkHeader = seekToStartOfListInfoMetadata(raf, existingTag);
                if (existingTag.getInfoTag().getEndLocationInFile() == raf.length())
                {
                    raf.setLength(existingTag.getInfoTag().getStartLocationInFile());
                    raf.seek(raf.length());
                    writeID3DataToFile(raf, id3TagBuffer, newInfoTagSize);
                }
                else
                {
                    deleteInfoTagChunk(raf,existingTag, infoChunkHeader);
                    raf.seek(raf.length());
                    writeID3DataToFile(raf, id3TagBuffer, newInfoTagSize);
                }
            }
            else
            {
                if (existingTag.isExistingId3Tag())
                {
                    seekAndWriteId3Chunk(raf, existingTag, id3TagBuffer);
                }
                else
                {
                    raf.seek(raf.length());
                    writeID3DataToFile(raf, id3TagBuffer, newInfoTagSize);
                }
            }
        }
    }

    /**
     * Save Active chunk and existing chunks even if not the active chunk
     *
     * @param wavTag
     * @param raf
     * @param existingTag
     * @throws CannotWriteException
     * @throws IOException
     */
    private void saveActiveExisting(WavTag wavTag, RandomAccessFile raf,  final WavTag existingTag )
            throws CannotWriteException, IOException
    {
        if(wavTag.getActiveTag() instanceof WavInfoTag)
        {
            if(existingTag.isExistingId3Tag())
            {
                saveBoth(wavTag, raf,  existingTag );
            }
            else
            {
                saveActive(wavTag, raf,  existingTag );
            }
        }
        else
        {
            if(existingTag.isExistingInfoTag())
            {
                saveBoth(wavTag, raf,  existingTag );
            }
            else
            {
                saveActive(wavTag, raf,  existingTag );
            }
        }
    }
}

