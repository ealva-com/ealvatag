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
package org.jaudiotagger.audio.aiff;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.aiff.chunk.ChunkType;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.TagWriter;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.iff.ChunkHeader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;
import static org.jaudiotagger.audio.iff.IffHeaderChunk.*;


/**
 * Write Aiff Tag.
 */
public class AiffTagWriter implements TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");


    /**
     * Delete given {@link Tag} from file.
     *
     * @param tag tag, must be instance of {@link AiffTag}
     * @param raf random access file
     * @param tempRaf temporary random access file
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(final Tag tag, final RandomAccessFile raf, final RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
        logger.config("Deleting tag from file");
        final AiffTag existingTag;
        try
        {
            //Find AiffTag (if any)
            AiffTagReader im = new AiffTagReader();
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
    }

    /**
     * Write {@link Tag} to file.
     *
     * @param af audio file
     * @param tag tag, must be instance of {@link AiffTag}
     * @param raf random access file
     * @param rafTemp temporary random access file
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    public void write(final AudioFile af, final Tag tag, final RandomAccessFile raf, final RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        logger.config("Writing tag to file");
        final AiffTag     existingTag;
        try
        {
            //Find AiffTag (if any)
            AiffTagReader im = new AiffTagReader();
            existingTag = im.read(raf);
        }
        catch (CannotReadException ex)
        {
            throw new CannotWriteException("Failed to read file");
        }

        try
        {
            final AiffTag     aiffTag     = (AiffTag) tag;
            final ByteBuffer  bb          = convert(aiffTag);
            final long        newTagSize  = bb.array().length;

            //Replacing ID3 tag
            if (existingTag.getID3Tag() != null && existingTag.getStartLocationInFile() != null)
            {

                //TODO is it safe to rely on the location as calculated when initially read
                //Find existing location of ID3 chunk if any and seek to that location
                raf.seek(existingTag.getStartLocationInFile());
                final ChunkHeader ch = new ChunkHeader(ByteOrder.BIG_ENDIAN);
                ch.readHeader(raf);

                if(!ChunkType.TAG.getCode().equals(ch.getID()))
                {
                    throw new CannotWriteException("Unable to find ID3 chunk at original location has file been modified externally");
                }

                logger.info("Current Space allocated:" + aiffTag.getSizeOfID3Tag() + ":NewTagRequires:" + newTagSize);

                //Usual case ID3 is last chunk
                if(existingTag.getEndLocationInFile() == raf.length())
                {
                    //We have enough existing space in chunk so just keep existing chunk size
                    if (existingTag.getSizeOfID3Tag() >= newTagSize)
                    {
                        writeDataToFile(raf, bb, aiffTag.getSizeOfID3Tag());

                        //To ensure old data from previous tag are erased
                        if (aiffTag.getSizeOfID3Tag() > newTagSize)
                        {
                            writePaddingToFile(raf, (int) (aiffTag.getSizeOfID3Tag() - newTagSize));
                        }
                    }
                    //New tag is larger so set chunk size to accommodate it
                    else
                    {
                        writeDataToFile(raf, bb, newTagSize);
                    }
                }
                //Unusual Case where ID3 is not last chunk
                //For now treat as if no ID3 chunk, existing chunk will be superceded by last chunk
                //TODO remove earlier ID3 chunk from file will require using temp file
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

            //Rewrite FORM Header size
            raf.seek(SIGNATURE_LENGTH);
            raf.write(Utils.getSizeBEInt32(((int) raf.length()) - SIGNATURE_LENGTH - SIZE_LENGTH));


        }
        finally
        {
            raf.close();
        }
    }

    /**
     * Writes data as a {@link ChunkType#TAG} chunk to the file.
     *
     * @param raf random access file
     * @param bb data to write
     * @param chunkSize chunk size
     * @throws IOException
     */
    private void writeDataToFile(final RandomAccessFile raf,  final ByteBuffer bb, final long chunkSize)
            throws IOException
    {
        final ChunkHeader ch = new ChunkHeader(ByteOrder.BIG_ENDIAN);
        ch.setID(ChunkType.TAG.getCode());
        ch.setSize(chunkSize); // is chunk size different from bb.array.length?
        raf.write(ch.writeHeader().array());
        raf.write(bb.array());
    }

    private void writePaddingToFile(final RandomAccessFile raf, final int paddingSize)
            throws IOException
    {
        raf.write(new byte[paddingSize]);
    }

    /**
     * Converts tag to {@link ByteBuffer}.
     *
     * @param tag tag
     * @return byte buffer containing the tag data
     * @throws UnsupportedEncodingException
     */
    public ByteBuffer convert(final AiffTag tag) throws UnsupportedEncodingException
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
}

