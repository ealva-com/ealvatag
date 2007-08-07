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
package org.jaudiotagger.audio.flac.util;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.exceptions.*;

import java.io.*;

public class FlacInfoReader {
	public GenericAudioHeader read(RandomAccessFile raf) throws CannotReadException, IOException {
		//Read the infos--------------------------------------------------------
		if (raf.length() == 0) {
			//Empty File
			throw new CannotReadException("Error: File empty");
		}
		raf.seek(0);

		//FLAC Header string
		byte[] b = new byte[4];
		raf.read(b);
		String flac = new String(b);
		if (!flac.equals("fLaC")) {
			throw new CannotReadException("fLaC Header not found");
		}

		MetadataBlockDataStreamInfo mbdsi = null;
		boolean isLastBlock = false;
		while (!isLastBlock) {
			b = new byte[4];
			raf.read(b);
			MetadataBlockHeader mbh = new MetadataBlockHeader(b);

			if (mbh.getBlockType() == MetadataBlockHeader.STREAMINFO) {
				b = new byte[mbh.getDataLength()];
				raf.read(b);

				mbdsi = new MetadataBlockDataStreamInfo(b);
				if (!mbdsi.isValid()) {
					throw new CannotReadException("FLAC StreamInfo not valid");
				}
				break;
			}
			raf.seek(raf.getFilePointer() + mbh.getDataLength());

			isLastBlock = mbh.isLastBlock();
			mbh = null; //Free memory
		}
		assert mbdsi != null;

		GenericAudioHeader info = new GenericAudioHeader();
//		info.setLength(mbdsi.getTrackLength());
		info.setPreciseLength(mbdsi.getPreciseLength());
		info.setChannelNumber(mbdsi.getChannelNumber());
		info.setSamplingRate(mbdsi.getSamplingRate());
		info.setEncodingType(mbdsi.getEncodingType());
		info.setExtraEncodingInfos("");
		info.setBitrate(computeBitrate(mbdsi.getLength(), raf.length()));

		return info;
	}

	private int computeBitrate(int length, long size) {
		return (int) ((size / 1000) * 8 / length);
	}
}
