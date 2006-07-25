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
 */

package org.jaudiotagger.tag.lyrics3;

import org.jaudiotagger.tag.AbstractTagFrame;
import org.jaudiotagger.tag.InvalidTagException;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.id3.framebody.*;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagOptionSingleton;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class Lyrics3v2Field
    extends AbstractTagFrame
{
    /**
     * Creates a new Lyrics3v2Field datatype.
     */
    public Lyrics3v2Field()
    {
    }

    public Lyrics3v2Field(Lyrics3v2Field copyObject)
    {
        super(copyObject);
    }

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param body DOCUMENT ME!
     */
    public Lyrics3v2Field(AbstractLyrics3v2FieldFrameBody body)
    {
        this.frameBody = body;
    }

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param frame DOCUMENT ME!
     * @throws TagException DOCUMENT ME!
     */
    public Lyrics3v2Field(AbstractID3v2Frame frame)
        throws TagException
    {
        AbstractFrameBodyTextInfo textFrame;
        String text;
        String frameIdentifier = frame.getIdentifier();
        if (frameIdentifier.startsWith("USLT"))
        {
            frameBody = new FieldFrameBodyLYR("");
            ((FieldFrameBodyLYR) frameBody).addLyric((FrameBodyUSLT) frame.getBody());
        }
        else if (frameIdentifier.startsWith("SYLT"))
        {
            frameBody = new FieldFrameBodyLYR("");
            ((FieldFrameBodyLYR) frameBody).addLyric((FrameBodySYLT) frame.getBody());
        }
        else if (frameIdentifier.startsWith("COMM"))
        {
            text = new String(((FrameBodyCOMM) frame.getBody()).getText());
            frameBody = new FieldFrameBodyINF(text);
        }
        else if (frameIdentifier.equals("TCOM"))
        {
            textFrame = (AbstractFrameBodyTextInfo) frame.getBody();
            frameBody = new FieldFrameBodyAUT("");
            if ((textFrame != null) && (((String) ((String) textFrame.getText())).length() > 0))
            {
                frameBody = new FieldFrameBodyAUT(((String) textFrame.getText()));
            }
        }
        else if (frameIdentifier.equals("TALB"))
        {
            textFrame = (AbstractFrameBodyTextInfo) frame.getBody();
            if ((textFrame != null) && (((String) textFrame.getText()).length() > 0))
            {
                frameBody = new FieldFrameBodyEAL(((String) textFrame.getText()));
            }
        }
        else if (frameIdentifier.equals("TPE1"))
        {
            textFrame = (AbstractFrameBodyTextInfo) frame.getBody();
            if ((textFrame != null) && (((String) textFrame.getText()).length() > 0))
            {
                frameBody = new FieldFrameBodyEAR(((String) textFrame.getText()));
            }
        }
        else if (frameIdentifier.equals("TIT2"))
        {
            textFrame = (AbstractFrameBodyTextInfo) frame.getBody();
            if ((textFrame != null) && (((String) textFrame.getText()).length() > 0))
            {
                frameBody = new FieldFrameBodyETT(((String) textFrame.getText()));
            }
        }
        else
        {
            throw new TagException("Cannot create Lyrics3v2 field from given ID3v2 frame");
        }
    }

    /**
     * Creates a new Lyrics3v2Field datatype.
     *
     * @param file DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!

     */
    public Lyrics3v2Field(ByteBuffer byteBuffer)
        throws InvalidTagException
    {
        this.read(byteBuffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        if (frameBody == null)
        {
            return "";
        }
        return frameBody.getIdentifier();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize()
    {
        return frameBody.getSize() + 5 + getIdentifier().length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param byteBuffer DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     */
    public void read(ByteBuffer byteBuffer)
        throws InvalidTagException
    {
        byte[] buffer = new byte[6];
        // lets scan for a non-zero byte;
        long filePointer;
        byte b;
        do
        {
            b = byteBuffer.get();
        }
        while (b == 0);
        byteBuffer.position(byteBuffer.position()-1);
        // read the 3 character ID
        byteBuffer.get(buffer, 0, 3);
        String identifier = new String(buffer, 0, 3);
        // is this a valid identifier?
        if (Lyrics3v2Fields.isLyrics3v2FieldIdentifier(identifier) == false)
        {
            throw new InvalidTagException(identifier + " is not a valid ID3v2.4 frame");
        }
        frameBody = readBody(identifier, byteBuffer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        if (frameBody == null)
        {
            return "";
        }
        return frameBody.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void write(RandomAccessFile file)

    {
        try
        {
            if ((((AbstractLyrics3v2FieldFrameBody) frameBody).getSize() > 0) ||
                TagOptionSingleton.getInstance().isLyrics3SaveEmptyField())
            {
                byte[] buffer = new byte[3];
                String str = getIdentifier();
                for (int i = 0; i < str.length(); i++)
                {
                    buffer[i] = (byte) str.charAt(i);
                }
                file.write(buffer, 0, str.length());
                //body.write(file);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Read a Lyrics3 Field from a file.
     *
     * @param identifier DOCUMENT ME!
     * @param file       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     */
    private AbstractLyrics3v2FieldFrameBody readBody(String identifier, ByteBuffer byteBuffer)
        throws InvalidTagException
    {
        AbstractLyrics3v2FieldFrameBody newBody = null;
        if (identifier.equals(Lyrics3v2Fields.FIELD_V2_AUTHOR))
        {
            newBody = new FieldFrameBodyAUT(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_ALBUM))
        {
            newBody = new FieldFrameBodyEAL(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_ARTIST))
        {
            newBody = new FieldFrameBodyEAR(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_TRACK))
        {
            newBody = new FieldFrameBodyETT(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_IMAGE))
        {
            newBody = new FieldFrameBodyIMG(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_INDICATIONS))
        {
            newBody = new FieldFrameBodyIND(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_ADDITIONAL_MULTI_LINE_TEXT))
        {
            newBody = new FieldFrameBodyINF(byteBuffer);
        }
        else if (identifier.equals(Lyrics3v2Fields.FIELD_V2_LYRICS_MULTI_LINE_TEXT))
        {
            newBody = new FieldFrameBodyLYR(byteBuffer);
        }
        else
        {
            newBody = new FieldFrameBodyUnsupported(byteBuffer);
        }
        return newBody;
    }
}
