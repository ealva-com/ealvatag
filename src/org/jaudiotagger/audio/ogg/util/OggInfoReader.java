/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Raphaël Slinckx <raphael@slinckx.net>
 * Copyright (c) 2004-2005 Christian Laireiter <liree@web.de>
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
package org.jaudiotagger.audio.ogg.util;

import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.exceptions.*;

import java.io.*;

/**
 * Read encoding info, only implemented for vorbis streams
 *
 */
public class OggInfoReader {
	public GenericAudioHeader read( RandomAccessFile raf )  throws CannotReadException, IOException {
		GenericAudioHeader info = new GenericAudioHeader();
		
		long oldPos = 0;
		
		//Reads the file encoding infos -----------------------------------
		raf.seek( 0 );
		double PCMSamplesNumber = -1;
		raf.seek( raf.length()-2);
		while(raf.getFilePointer() >= 4) {
			if(raf.read()==0x53) {
				raf.seek( raf.getFilePointer() - 4);
				byte[] ogg = new byte[3];
				raf.readFully(ogg);
				if(ogg[0]==0x4F && ogg[1]==0x67 && ogg[2]==0x67) {
					raf.seek( raf.getFilePointer() - 3);
					
					oldPos = raf.getFilePointer();
					raf.seek(raf.getFilePointer() + 26);
					int pageSegments = raf.readByte()&0xFF; //Unsigned
					raf.seek( oldPos );
					
					byte[] b = new byte[27 + pageSegments];
					raf.readFully( b );

					OggPageHeader pageHeader = new OggPageHeader( b );
					raf.seek(0);
					PCMSamplesNumber = pageHeader.getAbsoluteGranulePosition();
					break;
				}
			}	
			raf.seek( raf.getFilePointer() - 2);
		}
		
		if(PCMSamplesNumber == -1){
		
			throw new CannotReadException("Error: Could not find the Ogg Setup block");
		}
		

		//Supposing 1st page = codec infos
		//			2nd page = comment+decode info
		//...Extracting 1st page
		byte[] b = new byte[4];
		
		oldPos = raf.getFilePointer();
		raf.seek(26);
		int pageSegments = raf.read()&0xFF; //Unsigned
		raf.seek( oldPos );

		b = new byte[27 + pageSegments];
		raf.read( b );

		OggPageHeader pageHeader = new OggPageHeader( b );

		byte[] vorbisData = new byte[pageHeader.getPageLength()];

		raf.read( vorbisData );

		VorbisCodecHeader vorbisCodecHeader = new VorbisCodecHeader( vorbisData );

		//Populates encodingInfo----------------------------------------------------
		info.setPreciseLength( (float) (PCMSamplesNumber / vorbisCodecHeader.getSamplingRate()));
		info.setChannelNumber( vorbisCodecHeader.getChannelNumber() );
        System.out.println("Channel Number is:"+vorbisCodecHeader.getChannelNumber());
        info.setSamplingRate( vorbisCodecHeader.getSamplingRate() );
		info.setEncodingType( vorbisCodecHeader.getEncodingType() );
		info.setExtraEncodingInfos( "" );
		
		if(vorbisCodecHeader.getNominalBitrate() != 0
		        && vorbisCodecHeader.getMaxBitrate() == vorbisCodecHeader.getNominalBitrate()
		        && vorbisCodecHeader.getMinBitrate() == vorbisCodecHeader.getNominalBitrate()) {
		    //CBR (in kbps)
		    info.setBitrate(vorbisCodecHeader.getNominalBitrate() / 1000);
		    info.setVariableBitRate(false);
		}
		else if(vorbisCodecHeader.getNominalBitrate() != 0
		        && vorbisCodecHeader.getMaxBitrate() == 0
		        && vorbisCodecHeader.getMinBitrate() == 0) {
		    //Average vbr (in kpbs)
		    info.setBitrate(vorbisCodecHeader.getNominalBitrate() / 1000);
		    info.setVariableBitRate(true);
		}
		else {
			info.setBitrate( computeBitrate( info.getTrackLength(), raf.length() ) );
			info.setVariableBitRate(true);
		}
		
		return info;
	}

	private int computeBitrate( int length, long size ) {
		return (int) ( ( size / 1000 ) * 8 / length );
	}
}

