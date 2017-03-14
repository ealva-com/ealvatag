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
package ealvatag.audio.ogg;

import ealvatag.audio.AudioFileReader;
import ealvatag.audio.GenericAudioHeader;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.ogg.util.OggInfoReader;
import ealvatag.audio.ogg.util.OggPageHeader;
import ealvatag.tag.TagFieldContainer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

/**
 * Read Ogg File Tag and Encoding information
 * <p>
 * Only implemented for ogg files containing a vorbis stream with vorbis comments
 */
public class OggFileReader extends AudioFileReader {
  private OggInfoReader ir;
  private OggVorbisTagReader vtr;

  public OggFileReader() {
    ir = new OggInfoReader();
    vtr = new OggVorbisTagReader();
  }

  protected GenericAudioHeader getEncodingInfo(RandomAccessFile raf) throws CannotReadException, IOException {
    return ir.read(raf);
  }

  protected TagFieldContainer getTag(RandomAccessFile raf, final boolean ignoreArtwork) throws CannotReadException, IOException {
    return vtr.read(raf);
  }

  /**
   * Return count Ogg Page header, count starts from zero
   * <p>
   * count=0; should return PageHeader that contains Vorbis Identification Header
   * count=1; should return Pageheader that contains VorbisComment and possibly SetupHeader
   * count>=2; should return PageHeader containing remaining VorbisComment,SetupHeader and/or Audio
   *
   * @param raf
   * @param count
   *
   * @return
   *
   * @throws CannotReadException
   * @throws IOException
   */
  public OggPageHeader readOggPageHeader(RandomAccessFile raf, int count) throws CannotReadException, IOException {
    OggPageHeader pageHeader = OggPageHeader.read(raf);
    while (count > 0) {
      raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
      pageHeader = OggPageHeader.read(raf);
      count--;
    }
    return pageHeader;
  }

//  @SuppressWarnings("unused")
//  public void summarizeOggPageHeaders(File oggFile, PrintStream out) throws CannotReadException, IOException {
//    RandomAccessFile raf = new RandomAccessFile(oggFile, "r");
//
//    while (raf.getFilePointer() < raf.length()) {
//      out.println("pageHeader starts at absolute file position:" + raf.getFilePointer());
//      OggPageHeader pageHeader = OggPageHeader.read(raf);
//      out.println("pageHeader finishes at absolute file position:" + raf.getFilePointer());
//      out.println(pageHeader + "\n");
//      raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
//    }
//    out.println("Raf File Pointer at:" + raf.getFilePointer() + "File Size is:" + raf.length());
//    raf.close();
//  }
//
//  @SuppressWarnings("unused")
//  public void shortSummarizeOggPageHeaders(File oggFile, PrintStream out) throws CannotReadException, IOException {
//    RandomAccessFile raf = new RandomAccessFile(oggFile, "r");
//
//    int i = 0;
//    while (raf.getFilePointer() < raf.length()) {
//      out.println("pageHeader starts at absolute file position:" + raf.getFilePointer());
//      OggPageHeader pageHeader = OggPageHeader.read(raf);
//      out.println("pageHeader finishes at absolute file position:" + raf.getFilePointer());
//      out.println(pageHeader + "\n");
//      raf.seek(raf.getFilePointer() + pageHeader.getPageLength());
//      i++;
//      if (i >= 5) {
//        break;
//      }
//    }
//    out.println("Raf File Pointer at:" + raf.getFilePointer() + "File Size is:" + raf.length());
//    raf.close();
//  }
}

