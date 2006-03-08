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

import org.jaudiotagger.tag.datatype.*;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import java.io.IOException;
import java.nio.ByteBuffer;


public class FrameBodyCOMR extends AbstractID3v2FrameBody implements ID3v24FrameBody,ID3v23FrameBody
{
    /**
     * Creates a new FrameBodyCOMR datatype.
     */
    public FrameBodyCOMR()
    {
        //        this.setObject("Text Encoding", new Byte((byte) 0));
        //        this.setObject("Price String", "");
        //        this.setObject("Valid Until", "");
        //        this.setObject("Contact URL", "");
        //        this.setObject("Recieved As", new Byte((byte) 0));
        //        this.setObject("Name Of Seller", "");
        //        this.setObject(ObjectTypes.OBJ_DESCRIPTION, "");
        //        this.setObject("Picture MIME Type", "");
        //        this.setObject("Seller Logo", new byte[0]);
    }

    public FrameBodyCOMR(FrameBodyCOMR body)
    {
        super(body);
    }

    /**
     * Creates a new FrameBodyCOMR datatype.
     *
     * @param textEncoding DOCUMENT ME!
     * @param priceString  DOCUMENT ME!
     * @param validUntil   DOCUMENT ME!
     * @param contactUrl   DOCUMENT ME!
     * @param recievedAs   DOCUMENT ME!
     * @param nameOfSeller DOCUMENT ME!
     * @param description  DOCUMENT ME!
     * @param mimeType     DOCUMENT ME!
     * @param sellerLogo   DOCUMENT ME!
     */
    public FrameBodyCOMR(byte textEncoding, String priceString, String validUntil, String contactUrl, byte recievedAs, String nameOfSeller, String description, String mimeType, byte[] sellerLogo)
    {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, new Byte(textEncoding));
        this.setObjectValue(DataTypes.OBJ_PRICE_STRING, priceString);
        this.setObjectValue(DataTypes.OBJ_VALID_UNTIL, validUntil);
        this.setObjectValue(DataTypes.OBJ_CONTACT_URL, contactUrl);
        this.setObjectValue(DataTypes.OBJ_RECIEVED_AS, new Byte(recievedAs));
        this.setObjectValue(DataTypes.OBJ_SELLER_NAME, nameOfSeller);
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description);
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType);
        this.setObjectValue(DataTypes.OBJ_SELLER_LOGO, sellerLogo);
    }

    /**
     * Creates a new FrameBodyCOMR datatype.
     *
     * @param file DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    public FrameBodyCOMR(ByteBuffer byteBuffer, int frameSize)
        throws IOException, InvalidTagException
    {
        super(byteBuffer, frameSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        String str = ID3v24Frames.FRAME_ID_COMMERCIAL_FRAME;
        java.util.Iterator iterator = objectList.listIterator();

        while (iterator.hasNext())
        {
            str += (((char) 0) + getOwner());
        }

        return str;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOwner()
    {
        return (String) getObjectValue(DataTypes.OBJ_OWNER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param description DOCUMENT ME!
     */
    public void getOwner(String description)
    {
        setObjectValue(DataTypes.OBJ_OWNER, description);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setupObjectList()
    {
        objectList.add(new NumberHashMap(DataTypes.OBJ_TEXT_ENCODING, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_PRICE_STRING, this));
        objectList.add(new StringDate(DataTypes.OBJ_VALID_UNTIL, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_CONTACT_URL, this));
        objectList.add(new NumberHashMap(DataTypes.OBJ_RECIEVED_AS, this, 1));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_SELLER_NAME, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_DESCRIPTION, this));
        objectList.add(new StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this));
        objectList.add(new ByteArraySizeTerminated(DataTypes.OBJ_SELLER_LOGO, this));
    }
}
