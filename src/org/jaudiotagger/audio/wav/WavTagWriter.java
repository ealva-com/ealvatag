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
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;
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
     * Read existing tag
     *
     * @param raf
     * @return tag, returns empty tag wrapper if none actually exist
     * @throws IOException
     * @throws CannotWriteException
     */
    private WavTag getExistingTag(RandomAccessFile raf) throws IOException, CannotWriteException
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
    private ChunkHeader seekToStartOfMetadata(RandomAccessFile raf, WavTag existingTag) throws IOException, CannotWriteException
    {
        raf.seek(existingTag.getInfoTag().getStartLocationInFile());
        final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        chunkHeader.readHeader(raf);
        raf.seek(raf.getFilePointer() - ChunkHeader.CHUNK_HEADER_SIZE);

        if (!WavChunkType.LIST.getCode().equals(chunkHeader.getID()))
        {
            throw new CannotWriteException("Unable to find List chunk at original location has file been modified externally");
        }
        return chunkHeader;
    }

    /**
     * Delete given {@link org.jaudiotagger.tag.Tag} from file.
     *
     * @param tag     tag, must be instance of {@link org.jaudiotagger.tag.wav.WavTag}
     * @param raf     random access file
     * @param tempRaf temporary random access file
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
        logger.info("Deleting tag from file");
        final WavTag existingTag = getExistingTag(raf);

        try
        {
            final WavInfoTag existingInfoTag = existingTag.getInfoTag();

            //We have Info Chink we can delete
            if (existingInfoTag != null && existingInfoTag.getStartLocationInFile() != null)
            {
                ChunkHeader chunkHeader = seekToStartOfMetadata(raf, existingTag);
                //and it is at end of the file
                if (existingInfoTag.getEndLocationInFile() == raf.length())
                {
                    logger.info("Setting new length to:" + existingInfoTag.getStartLocationInFile());
                    raf.setLength(existingInfoTag.getStartLocationInFile());
                    rewriteRiffHeaderSize(raf);
                }
                else
                {
                    deleteTagChunk(raf, existingTag, chunkHeader);
                    rewriteRiffHeaderSize(raf);
                }
            }
        }
        finally
        {
            raf.close();
        }
    }

    /**
     * Delete Tag Chunk
     *
     * Can be used when chunk is not the last chunk
     *
     * @param raf
     * @param existingTag
     * @param listChunkHeader
     * @throws IOException
     */
    private void deleteTagChunk(final RandomAccessFile raf, final WavTag existingTag, final ChunkHeader listChunkHeader) throws IOException
    {
        final WavInfoTag existingInfoTag = existingTag.getInfoTag();
        final int lengthTagChunk = (int) listChunkHeader.getSize() + ChunkHeader.CHUNK_HEADER_SIZE;

        // position for reading after the  tag
        raf.seek(existingInfoTag.getStartLocationInFile() + lengthTagChunk);
        final FileChannel channel = raf.getChannel();

        // the following should work, but DOES not :-(
        /*
        long read;
        for (long position = existingTag.getStartLocationInFile();
             (read = channel.transferFrom(channel, position, newLength - position)) < newLength-position;
             position += read);
        */

        // so we do it the manual way
        final ByteBuffer buffer = ByteBuffer.allocate(64 * 1024);
        while (channel.read(buffer) >= 0 || buffer.position() != 0)
        {
            buffer.flip();
            final long readPosition = channel.position();
            channel.position(readPosition - lengthTagChunk - buffer.limit());
            channel.write(buffer);
            channel.position(readPosition);
            buffer.compact();
        }
        // truncate the file after the last chunk
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
        final WavTag existingTag = getExistingTag(raf);

        try
        {
            final WavTag wavTag = (WavTag) tag;

            //If we have some tag data to write
            if (wavTag.isInfoTag())
            {
                final ByteBuffer bb = convert(wavTag);
                final long newTagSize = bb.limit();
                final WavInfoTag existingInfoTag = existingTag.getInfoTag();

                //Replacing Info tag
                if (existingInfoTag != null && existingInfoTag.getStartLocationInFile() != null)
                {
                    seekToStartOfMetadata(raf, existingTag);
                    logger.info("Current Space allocated:" + (existingTag.getInfoTag().getEndLocationInFile() - existingTag.getInfoTag().getStartLocationInFile()) + ":NewTagRequires:" + newTagSize);

                    //Usual case LIST is last chunk
                    if (existingInfoTag.getEndLocationInFile() == raf.length())
                    {
                        //We have enough existing space in chunk so just keep existing chunk size
                        if (existingInfoTag.getSizeOfTag() >= newTagSize)
                        {
                            writeDataToFile(raf, bb, existingInfoTag.getSizeOfTag());
                            //To ensure old data from previous tag are erased
                            if (existingInfoTag.getSizeOfTag() > newTagSize)
                            {
                                writePaddingToFile(raf, (int) (existingInfoTag.getSizeOfTag() - newTagSize));
                            }
                        }
                        //New tag is larger so set chunk size to accommodate it
                        else
                        {
                            writeDataToFile(raf, bb, newTagSize);
                        }
                    }
                    //Unusual Case where LIST is not last chunk
                    //For now just write LIST to the end of the file, e.g the last chunk may be an ID3 one
                    //and we would want to keep that at the end
                    //TODO shouldn't we write to same place
                    //TODO what about if have multiple LIST chunks, shouldn't we remove earlier one
                    else
                    {
                        //Go to end of file
                        raf.seek(raf.length());
                        writeDataToFile(raf, bb, newTagSize);
                    }
                }
                //New Tag add to end of the file
                else
                {
                    //Go to end of file
                    raf.seek(raf.length());
                    writeDataToFile(raf, bb, newTagSize);
                }
                rewriteRiffHeaderSize(raf);
            }
            //No Tag data to write
            else
            {

            }
        }
        finally
        {
            raf.close();
        }
    }

    /**
     * Rewrite RAF header to reflect new file file
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
     * @param raf       random access file
     * @param bb        data to write
     * @param chunkSize chunk size
     * @throws java.io.IOException
     */
    private void writeDataToFile(final RandomAccessFile raf, final ByteBuffer bb, final long chunkSize) throws IOException
    {
        //Now Write LIST header
        final ByteBuffer listBuffer = ByteBuffer.allocate(ChunkHeader.CHUNK_HEADER_SIZE + SIGNATURE_LENGTH);
        listBuffer.order(ByteOrder.LITTLE_ENDIAN);
        listBuffer.put(WavChunkType.LIST.getCode().getBytes(StandardCharsets.US_ASCII));
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
     * Converts tag to {@link java.nio.ByteBuffer}.
     *
     * @param tag tag
     * @return byte buffer containing the tag data
     * @throws java.io.UnsupportedEncodingException
     */
    public ByteBuffer convert(final WavTag tag) throws UnsupportedEncodingException
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
                baos.write(Utils.getDefaultBytes(wii.getCode(), StandardCharsets.US_ASCII));
                logger.config("Writing:" + wii.getCode() + ":" + next.getContent());

                //TODO Is UTF8 allowed format
                byte[] contentConvertedToBytes = Utils.getDefaultBytes(next.getContent(), StandardCharsets.UTF_8);
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
                baos.write(Utils.getDefaultBytes(next.getId(), StandardCharsets.US_ASCII));
                logger.config("Writing:" +next.getId() + ":" + next.getContent());
                byte[] contentConvertedToBytes = Utils.getDefaultBytes(next.getContent(), StandardCharsets.UTF_8);
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


}

