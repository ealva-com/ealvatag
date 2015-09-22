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
import org.jaudiotagger.audio.aiff.chunk.ChunkType;
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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.Logger;
import static org.jaudiotagger.audio.iff.IffHeaderChunk.*;

/**
 * Write Wav Tag.
 */
public class WavTagWriter implements TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.Wav");


    /**
     * Delete given {@link org.jaudiotagger.tag.Tag} from file.
     *
     * @param tag tag, must be instance of {@link org.jaudiotagger.tag.wav.WavTag}
     * @param raf random access file
     * @param tempRaf temporary random access file
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
        /*logger.config("Deleting tag from file");
        final WavTag existingTag;
        try
        {
            //Find WavTag (if any)
            WavTagReader im = new WavTagReader();
            existingTag = im.read(raf);
        }
        catch (CannotReadException ex)
        {
            throw new CannotWriteException("Failed to read file");
        }

        try
        {
            if (existingTag.getID3Tag() != null && existingTag.getStartLocationInFile() != null)
            {
                raf.seek(existingTag.getStartLocationInFile());
                final ChunkHeader ch = new ChunkHeader(ByteOrder.BIG_ENDIAN);
                ch.readHeader(raf);

                if(!ChunkType.TAG.getCode().equals(ch.getID()))
                {
                    throw new CannotWriteException("Unable to find ID3 chunk at expected location");
                }

                if (existingTag.getEndLocationInFile() == raf.length())
                {
                    logger.config("Setting new length to:" + existingTag.getStartLocationInFile());
                    raf.setLength(existingTag.getStartLocationInFile());

                    //Rewrite FORM size
                    raf.seek(SIGNATURE_LENGTH);
                    raf.write(Utils.getSizeBEInt32(((int) raf.length()) - SIGNATURE_LENGTH - SIZE_LENGTH));
                }
            }
        }
        finally
        {
            raf.close();
        }
        //and delete
        */
    }

    /**
     * Write {@link org.jaudiotagger.tag.Tag} to file.
     *
     * @param af audio file
     * @param tag tag, must be instance of {@link org.jaudiotagger.tag.wav.WavTag}
     * @param raf random access file
     * @param rafTemp temporary random access file
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    @Override
    public void write(final AudioFile af, final Tag tag, final RandomAccessFile raf, final RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        logger.severe("Writing tag to file");
        final WavTag     existingTag;
        try
        {
            //Find WavTag (if any)
            WavTagReader im = new WavTagReader();
            existingTag =  im.read(raf);
        }
        catch (CannotReadException ex)
        {
            throw new CannotWriteException("Failed to read file");
        }

        try
        {
            final WavTag wavTag = (WavTag) tag;

            if (wavTag.isInfoTag())
            {
                final ByteBuffer bb = convert(wavTag);
                final long newTagSize = bb.limit();
                final WavInfoTag existingInfoTag = existingTag.getInfoTag();

                //Replacing Info tag
                if (existingInfoTag != null && existingInfoTag.getStartLocationInFile() != null)
                {
                    raf.seek(existingTag.getInfoTag().getStartLocationInFile());
                    final ChunkHeader ch = new ChunkHeader(ByteOrder.BIG_ENDIAN);
                    ch.readHeader(raf);
                    raf.seek(raf.getFilePointer() - ChunkHeader.CHUNK_HEADER_SIZE);
                    if (!WavChunkType.LIST.getCode().equals(ch.getID()))
                    {
                        throw new CannotWriteException("Unable to find List chunk at original location has file been modified externally");
                    }

                    logger.info("Current Space allocated:" + (existingTag.getInfoTag().getEndLocationInFile() - existingTag.getInfoTag().getStartLocationInFile()) + ":NewTagRequires:" + newTagSize);

                    //Usual case LIST is last chunk
                    if (existingInfoTag.getEndLocationInFile() == raf.length())
                    {
                        //We have enough existing space in chunk so just keep existing chunk size
                        if (existingInfoTag.getSizeOfTag() >= newTagSize)
                        {
                            writeDataToFile(raf, bb, 0);
                            //To ensure old data from previous tag are erased
                            /*if (wavTag.getSizeOfID3Tag() > newTagSize)
                            {
                                writePaddingToFile(raf, (int) (wavTag.getSizeOfID3Tag() - newTagSize));
                            }*/
                        }
                        //New tag is larger so set chunk size to accommodate it
                        else
                        {
                            writeDataToFile(raf, bb, newTagSize);
                        }
                    }
                    //Unusual Case where LIST is not last chunk
                    //For now treat as if no LIST chunk, existing chunk will be superceded by last chunk
                    //TODO remove earlier LIST chunk from file will require using temp file
                    else
                    {
                        //Go to end of file
                        raf.seek(raf.length());
                        writeDataToFile(raf, bb, newTagSize);
                    }
                }
                //New Tag
                else
                {
                    //Go to end of file
                    raf.seek(raf.length());
                    writeDataToFile(raf, bb, newTagSize);
                }

                //Rewrite RIFF Header size
                raf.seek(IffHeaderChunk.SIGNATURE_LENGTH);
                raf.write(Utils.getSizeLEInt32(((int) raf.length()) - SIGNATURE_LENGTH - SIZE_LENGTH));

            }
        }
        finally
        {
            raf.close();
        }
    }

    /**

     *
     * @param raf random access file
     * @param bb data to write
     * @param chunkSize chunk size
     * @throws java.io.IOException
     */
    private void writeDataToFile(final RandomAccessFile raf,  final ByteBuffer bb, final long chunkSize)
            throws IOException
    {
        raf.getChannel().write(bb);
    }

    private void writePaddingToFile(final RandomAccessFile raf, final int paddingSize)
            throws IOException
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
                System.out.println(wii.getCode() + ":" + next.getContent());

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
            final ByteBuffer infoBuffer = ByteBuffer.wrap(baos.toByteArray());
            infoBuffer.rewind();

            //Now Write LIST INFO header
            final ByteBuffer listBuffer = ByteBuffer.allocate(ChunkHeader.CHUNK_HEADER_SIZE + SIGNATURE_LENGTH);
            listBuffer.put(WavChunkType.LIST.getCode().getBytes(StandardCharsets.US_ASCII));
            listBuffer.put(Utils.getSizeLEInt32((int) infoBuffer.limit() + SIGNATURE_LENGTH));
            listBuffer.put(WavChunkType.INFO.getCode().getBytes(StandardCharsets.US_ASCII));
            listBuffer.flip();


            //Construct a single ByteBuffer from both
            ByteBuffer listInfoBuffer = ByteBuffer.allocateDirect(infoBuffer.limit() + listBuffer.limit());
            listInfoBuffer.put(listBuffer);
            listInfoBuffer.put(infoBuffer);
            listInfoBuffer.flip();
            return listInfoBuffer;
        }
        catch(IOException ioe)
        {
            //Should never happen as not writing to file at this point
            throw new RuntimeException(ioe);
        }
    }
}

