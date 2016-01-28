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
package org.jaudiotagger.audio.dsf;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.generic.AudioFileWriter;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Write/delete tag info for Dsf file
 */
public class DsfFileWriter extends AudioFileWriter
{
    @Override
    protected void writeTag(AudioFile audioFile, Tag tag, RandomAccessFile file, RandomAccessFile rafTemp) throws CannotWriteException, IOException
    {
        DsdChunk dsd = DsdChunk.readChunk(Utils.readFileDataIntoBufferLE(file, DsdChunk.DSD_HEADER_LENGTH));
        if(dsd!=null)
        {
            if(dsd.getMetadataOffset()> 0)
            {
                file.seek(dsd.getMetadataOffset());
                ByteBuffer tagBuffer = Utils.readFileDataIntoBufferLE(file, (int) (file.length() - file.getFilePointer()));
                String type = Utils.readThreeBytesAsChars(tagBuffer);
                if (DsfChunkType.ID3.getCode().equals(type))
                {
                    //Remove Existing tag
                    file.setLength(dsd.getMetadataOffset());
                    final ByteBuffer bb = convert((AbstractID3v2Tag)tag);
                    FileChannel fc = file.getChannel();
                    fc.write(bb);
                }
                else
                {
                    throw new CannotWriteException("Could not find existing ID3v2 Tag");
                }
            }
            else
            {
                //Write new tag and new offset and size
                file.seek(file.length());
                dsd.setMetadataOffset(file.length());
                final ByteBuffer bb = convert((AbstractID3v2Tag)tag);
                FileChannel fc = file.getChannel();
                fc.write(bb);
                dsd.setFileLength(file.length());
                file.seek(0);
                file.getChannel().write(dsd.write());
                System.out.println(dsd);
            }
        }
    }

    public ByteBuffer convert(final AbstractID3v2Tag tag) throws UnsupportedEncodingException
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            long existingTagSize = tag.getSize();

            //If existingTag is uneven size lets make it even
            if( existingTagSize > 0)
            {
                if((existingTagSize & 1)!=0)
                {
                    existingTagSize++;
                }
            }

            //Write Tag to buffer
            tag.write(baos, (int) existingTagSize);

            //If the tag is now odd because we needed to increase size and the data made it odd sized
            //we redo adding a padding byte to make it even
            if((baos.toByteArray().length & 1)!=0)
            {
                int newSize = baos.toByteArray().length + 1;
                baos = new ByteArrayOutputStream();
                tag.write(baos, newSize);
            }
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

    @Override
    protected void deleteTag(Tag tag, RandomAccessFile file, RandomAccessFile tempRaf) throws CannotWriteException, IOException
    {
        DsdChunk dsd = DsdChunk.readChunk(Utils.readFileDataIntoBufferLE(file, DsdChunk.DSD_HEADER_LENGTH));
        if(dsd!=null)
        {
            if (dsd.getMetadataOffset() > 0)
            {
                //Check really is metadtaa tag and then delete Metadata
                file.seek(dsd.getMetadataOffset());
                ByteBuffer tagBuffer = Utils.readFileDataIntoBufferLE(file, (int) (file.length() - file.getFilePointer()));
                String type = Utils.readThreeBytesAsChars(tagBuffer);
                if (DsfChunkType.ID3.getCode().equals(type))
                {
                    file.setLength(dsd.getMetadataOffset());
                    //set correct value for fileLength and zero offset
                    dsd.setMetadataOffset(0);
                    dsd.setFileLength(file.length());
                    file.seek(0);
                    file.getChannel().write(dsd.write());
                }
            }
            else
            {
                //Do Nothing;
            }
        }
    }
}

