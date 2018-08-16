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

import com.ealva.ealvalog.java.JLogger;
import com.ealva.ealvalog.java.JLoggers;

import static com.ealva.ealvalog.LogLevel.TRACE;
import static com.ealva.ealvalog.LogLevel.WARN;

import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.flac.metadatablock.MetadataBlockDataPicture;
import ealvatag.audio.flac.metadatablock.MetadataBlockHeader;
import ealvatag.logging.Hex;
import ealvatag.logging.EalvaTagLog;
import ealvatag.tag.InvalidFrameException;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.vorbiscomment.VorbisCommentReader;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;

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
  public static JLogger LOG = JLoggers.get(FlacTagReader.class, EalvaTagLog.MARKER);

  private VorbisCommentReader vorbisCommentReader = new VorbisCommentReader();


  public FlacTag read(FileChannel fc, final String path, final boolean ignoreArtwork) throws CannotReadException, IOException {
    FlacStreamReader flacStream = new FlacStreamReader(fc, path + " ");
    flacStream.findStream();

    //Hold the metadata
    VorbisCommentTag tag = null;
    List<MetadataBlockDataPicture> images = new ArrayList<>();

    //Seems like we have a valid stream
    boolean containsArtwork = false;
    boolean isLastBlock = false;
    while (!isLastBlock) {
      LOG.log(TRACE, "%s Looking for MetaBlockHeader at:%d", path, fc.position());

      //Read the header
      MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(fc);
      if (mbh == null) {
        break;
      }

      LOG.log(TRACE, "Reading MetadataBlockHeader:%s ending at %d", mbh, fc.position());

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
            containsArtwork = true;
            if (ignoreArtwork) {
              LOG.log(TRACE, "%s Ignoring MetadataBlock:%s", path, mbh.getBlockType());
              fc.position(fc.position() + mbh.getDataLength());
            } else {
              try {
                MetadataBlockDataPicture mbdp = new MetadataBlockDataPicture(mbh, fc);
                images.add(mbdp);
              } catch (IOException | InvalidFrameException e) {
                LOG.log(WARN, "%s Unable to read picture metablock, ignoring:%s", path, e.getMessage());
              }
            }
            break;

          default:
            //This is not a metadata block we are interested in so we skip to next block
            LOG.log(TRACE, "%s Ignoring MetadataBlock:%s", path, mbh.getBlockType());
            fc.position(fc.position() + mbh.getDataLength());
            break;
        }
      }
      isLastBlock = mbh.isLastBlock();
    }
    LOG.log(TRACE, "Audio should start at:%s", Hex.asHex(fc.position()));

    //Note there may not be either a tag or any images, no problem this is valid however to make it easier we
    //just initialize Flac with an empty VorbisTag
    if (tag == null) {
      tag = VorbisCommentTag.createNewTag();
    }
    return new FlacTag(tag, images, containsArtwork && ignoreArtwork);
  }
}

