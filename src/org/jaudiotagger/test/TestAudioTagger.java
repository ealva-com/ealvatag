/**
 * Initial @author : Paul Taylor
 * <p/>
 * Version @version:$Id$
 * <p/>
 * Jaudiotagger Copyright (C)2004,2005
 * <p/>
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Description:
 */
package org.jaudiotagger.test;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTPE1;

import java.io.File;

public class TestAudioTagger
{
    public static void main(final String[] args) throws Exception
    {
        ID3v24Frame newFrame = new ID3v24Frame("TPE1");
        AbstractTagFrameBody tmpFrameBody = newFrame.getBody();
        FrameBodyTPE1 body = (FrameBodyTPE1)tmpFrameBody ;

        MP3File mp3File = new MP3File(new File(args[0]), MP3File.LOAD_IDV1TAG | MP3File.LOAD_IDV2TAG );
        System.out.println(mp3File.displayStructureAsXML());
    }


}
