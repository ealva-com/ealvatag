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

public class MetadataBlockDataStreamInfo {
	
	private int samplingRate,bitsPerSample,channelNumber;
	private float length;
	private boolean isValid = true;
	
	public MetadataBlockDataStreamInfo(byte[] b) {
		if(b.length<19) {
			isValid = false;
			return;
		}
		
		samplingRate = readSamplingRate(b[10], b[11], b[12] );
		
		channelNumber = ((u(b[12])&0x0E)>>>1) + 1;
		samplingRate = samplingRate / channelNumber;
		
		bitsPerSample = ((u(b[12])&0x01)<<4) + ((u(b[13])&0xF0)>>>4) + 1;
		
		int sampleNumber = readSampleNumber(b[13], b[14], b[15], b[16], b[17]);
		
		length = (float)((double)sampleNumber / samplingRate);
		
		/*
		System.err.println("SampleRate"+sampleRate);
		System.err.println("Channel number:"+channelNumber);
		System.err.println("bits per sample: "+bitsPerSample);
		System.err.println("SampleNumber: "+sampleNumber);
		System.err.println("Length: "+length);
		*/
	}
	
	public int getLength() {
		return (int)length;
	}
	
	public float getPreciseLength() {
		return length;
	}
	
	public int getChannelNumber() {
		return channelNumber;
	}
	
	public int getSamplingRate() {
		return samplingRate;
	}
	
	public String getEncodingType() {
		return "FLAC "+bitsPerSample+" bits";
	}
	
	public boolean isValid() {
		return isValid;
	}
	

	private int readSamplingRate(byte b1, byte b2, byte b3) {
		int rate = (u(b3)&0xF0)>>>3;
		rate += u(b2)<<5;
		rate += u(b1)<<13;
		return rate;
	}
	
	private int readSampleNumber(byte b1, byte b2, byte b3, byte b4, byte b5) {
		int nb = u(b5);
		nb += u(b4)<<8;
		nb += u(b3)<<16;
		nb += u(b2)<<24;
		nb += (u(b1)&0x0F)<<32;
		return nb;
	}
	
	private int u(int i) {
		return i & 0xFF;
	}
}
