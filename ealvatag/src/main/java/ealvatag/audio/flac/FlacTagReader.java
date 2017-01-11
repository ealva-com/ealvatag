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
package ealvatag.audio.flac;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.audio.flac.metadatablock.MetadataBlockHeader;
import ealvatag.logging.Hex;
import ealvatag.tag.InvalidFrameException;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.vorbiscomment.VorbisCommentReader;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Read Flac Tag
 */
public class FlacTagReader {
    // Logger Object
    public static Logger LOG = LoggerFactory.getLogger(FlacTagReader.class);

    private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();


    public FlacTag read(FileChannel fc, final String path) throws CannotReadException, IOException {
        FlacStreamReader flacStream = new FlacStreamReader(fc, path + " ");
        flacStream.findStream();

        //Hold the metadata
        VorbisCommentTag tag = null;
        List<MetadataBlockDataPicture> images = new ArrayList<>();

        //Seems like we have a valid stream
        boolean isLastBlock = false;
        while (!isLastBlock) {
            LOG.trace("{} Looking for MetaBlockHeader at:{}", path, fc.position());

            //Read the header
            MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
            if (mbh == null) {
                break;
            }

            LOG.trace("{} Reading MetadataBlockHeader:{} ending at {}", mbh.toString(), fc.position());

            //Is it one containing some sort of metadata, therefore interested in it?

            //JAUDIOTAGGER-466:CBlocktype can be null
            if (mbh.getBlockType() != null) {
                switch (mbh.getBlockType()) {
                    //We got a vorbiscomment comment block, parse it
                    case VORBIS_COMMENT:
                        ByteBuffer commentHeaderRawPacket = ByteBuffer.allocate(mbh.getDataLength());
                        fc.read(commentHeaderRawPacket);
                        tag = vorbisCommentReader.read(commentHeaderRawPacket.array(), false);
                        break;

                    case PICTURE:
                        try {
                            MetadataBlockDataPicture mbdp = new MetadataBlockDataPicture(mbh, fc);
                            images.add(mbdp);
                        } catch (IOException | InvalidFrameException e) {
                            LOG.warn("{} Unable to read picture metablock, ignoring:{}", path, e.getMessage());
                        }
                        break;

                    //This is not a metadata block we are interested in so we skip to next block
                    default:
                        LOG.trace("{} Ignoring MetadataBlock:{}", path, mbh.getBlockType());
                        fc.position(fc.position() + mbh.getDataLength());
                        break;
                }
            }
            isLastBlock = mbh.isLastBlock();
        }
        LOG.trace("Audio should start at:{}", Hex.asHex(fc.position()));

        //Note there may not be either a tag or any images, no problem this is valid however to make it easier we
        //just initialize Flac with an empty VorbisTag
        if (tag == null) {
            tag = VorbisCommentTag.createNewTag();
        }
        return new FlacTag(tag, images);
    }
}

