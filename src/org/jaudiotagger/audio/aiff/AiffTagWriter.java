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
import org.jaudiotagger.audio.aiff.chunk.ChunkHeader;
import org.jaudiotagger.audio.aiff.chunk.ChunkType;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.TagWriter;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.aiff.AiffTag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static org.jaudiotagger.audio.aiff.AiffFileHeader.*;


/**
 * Write Aiff Tag
 */
public class AiffTagWriter implements TagWriter
{
    // Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.aiff");


    /**
     * Delete Tag from file
     *
     * @param tag
     * @param raf
     * @param tempRaf
     * @throws java.io.IOException
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     */
    public void delete(Tag tag, RandomAccessFile raf, RandomAccessFile tempRaf) throws IOException, CannotWriteException
    {
        logger.info("Deleting tag from file");
        Throwable t = new Throwable();
        t.printStackTrace();
        try
        {
            //Find ID3 tag chunk
            AiffTag         aiffTag         = (AiffTag) tag;
            if (aiffTag.getID3Tag() != null && aiffTag.getStartLocationInFile() != null)
            {
                //TODO is it safe to rely on the location as calculated when initially read
                //Find existing location of ID3 chunk if any and seek to that location
                raf.seek(aiffTag.getStartLocationInFile());
                ChunkHeader ch = new ChunkHeader();
                ch.readHeader(raf);

                if(!ch.getID().equals(ChunkType.TAG.getCode()))
                {
                    throw new CannotWriteException("Unable to find ID3 chunk at original location has file been modified externally");
                }

                if(aiffTag.getEndLocationInFile() == raf.length())
                {
                    logger.info("Setting new length to:" + aiffTag.getStartLocationInFile());
                    raf.setLength(aiffTag.getStartLocationInFile());

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
     * Write tag to file
     *
     * @param af,
     * @param tag
     * @param raf
     * @param rafTemp
     * @throws org.jaudiotagger.audio.exceptions.CannotWriteException
     * @throws java.io.IOException
     */
    public void write(AudioFile af, Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        logger.info("Writing tag to file");
        try
        {
            AiffTag         aiffTag         = (AiffTag) tag;
            ByteBuffer      bb          = convert(aiffTag);
            long            newTagSize  = bb.array().length;

            //Replacing ID3 tag
            if (aiffTag.getID3Tag() != null && aiffTag.getStartLocationInFile() != null)
            {

                //TODO is it safe to rely on the location as calculated when initially read
                //Find existing location of ID3 chunk if any and seek to that location
                raf.seek(aiffTag.getStartLocationInFile());
                ChunkHeader ch = new ChunkHeader();
                ch.readHeader(raf);

                if(!ch.getID().equals(ChunkType.TAG.getCode()))
                {
                    throw new CannotWriteException("Unable to find ID3 chunk at original location has file been modified externally");
                }

                logger.info("Current Space allocated:" + aiffTag.getSizeOfID3Tag() + ":NewTagRequires:" + newTagSize);

                //Usual case ID3 is last chunk
                if(aiffTag.getEndLocationInFile() == raf.length())
                {
                    //We have enough existing space in chunk so just keep existing chunk size
                    if (aiffTag.getSizeOfID3Tag() >= newTagSize)
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

    private void writeDataToFile(RandomAccessFile raf,  ByteBuffer bb, long chunkSize)
            throws IOException
    {
        ChunkHeader ch = new ChunkHeader();
        ch.setID(ChunkType.TAG.getCode());
        ch.setSize(chunkSize);
        raf.write(ch.writeHeader().array());
        raf.write(bb.array());
    }

    private void writePaddingToFile(RandomAccessFile raf,  int paddingSize)
            throws IOException
    {
        ByteBuffer padding = ByteBuffer.allocate(paddingSize);
        raf.write(padding.array());
    }

    public ByteBuffer convert(AiffTag tag) throws UnsupportedEncodingException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tag.getID3Tag().write(baos);
            ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
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

