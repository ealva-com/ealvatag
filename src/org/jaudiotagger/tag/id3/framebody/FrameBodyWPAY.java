/**
 *  Amended @author : Paul Taylor
 *  Initial @author : Eric Farng
 *
 *  Version @version:$Id$
 *
 *  MusicTag Copyright (C)2003,2004
 *
 *  This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 *  General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 *  or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 *  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 *  you can get a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Description:
 *
 */
package org.jaudiotagger.tag.id3.framebody;

import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3Frames;
import org.jaudiotagger.tag.id3.ID3Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Frames;


public class FrameBodyWPAY extends AbstractFrameBodyUrlLink implements ID3v24FrameBody
{
    /**
     * Creates a new FrameBodyWPAY datatype.
     */
    public FrameBodyWPAY()
    {
    }

    /**
     * Creates a new FrameBodyWPAY datatype.
     *
     * @param urlLink DOCUMENT ME!
     */
    public FrameBodyWPAY(String urlLink)
    {
        super(urlLink);
    }

    public FrameBodyWPAY(FrameBodyWPAY body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyWPAY datatype.
     *
     * @param file DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyWPAY(java.io.RandomAccessFile file, int frameSize)
        throws java.io.IOException, InvalidTagException
    {
        super(file, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return ID3v24Frames.FRAME_ID_URL_PAYMENT;
    }
}