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
 * This abstract class is each frame header inside a ID3v2 tag
 *
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.*;
import java.nio.*;

public abstract class AbstractID3v2Frame
    extends AbstractTagFrame
{

    protected static final String TYPE_FRAME = "frame";

    protected static final String TYPE_FRAME_SIZE = "frameSize";

    protected static final String UNSUPPORTED_ID = "Unsupported";

    //Frame identifier
    protected String identifier = "";

    //Frame Size
    protected int frameSize;

    /**
     * Create an empty frame
     */
    protected AbstractID3v2Frame()
    {
        ;
    }

    /**
     * This holds the Status flags (not supported in v2.20
     */
    StatusFlags statusFlags = null;

    /**
     * This holds the Encoding flags (not supported in v2.2)
     */
    EncodingFlags encodingFlags = null;

    /**
     * Create a frame based on another frame
     */
    public AbstractID3v2Frame(AbstractID3v2Frame frame)
    {
        super(frame);
    }

    /**
     * Create a frame based on a body
     */
    public AbstractID3v2Frame(AbstractID3v2FrameBody body)
    {
        this.frameBody = body;
    }

    /**
     * Create a frame with empty body based on identifier
     *
     * @todo the identifier checks should be done in the relevent subclasses
     */
    public AbstractID3v2Frame(String identifier)
    {
        logger.info("Creating empty frame of type" + identifier);
        this.identifier = identifier;

        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date.
         */
        try
        {
            Class c = Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            frameBody = (AbstractID3v2FrameBody) c.newInstance();
        }
        catch (ClassNotFoundException cnfe)
        {
            logger.severe(cnfe.getMessage());
            frameBody = new FrameBodyUnsupported();
        }
            //Instantiate Interfrace/Abstract should not happen
        catch (InstantiationException ie)
        {
            throw new RuntimeException(ie.getMessage());
        }
            //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            throw new RuntimeException(iae.getMessage());
        }
        logger.info("Created empty frame of type" + identifier);
    }

    /**
     * Return the frame identifier
     *
     * @return DOCUMENT ME!
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Read the frame body from the specified file via the buffer
     *
     * @param identifier DOCUMENT ME!
     * @param byteBuffer       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     */
    protected AbstractID3v2FrameBody readBody(String identifier, ByteBuffer byteBuffer, int frameSize)
        throws InvalidFrameException, IOException
    {
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date,although slight performance hit.
         */
        logger.finest("Creating framebody:start");

        AbstractID3v2FrameBody frameBody = null;
        try
        {
            Class c = Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            Class[] constructorParameterTypes =
                {((Class) Class.forName("java.nio.ByteBuffer")), Integer.TYPE
                };
            Object[] constructorParameterValues =
                {byteBuffer, new Integer(frameSize)
                };
            Constructor construct = c.getConstructor(constructorParameterTypes);
            frameBody = (AbstractID3v2FrameBody) (construct.newInstance(constructorParameterValues));
        }
        //No class defind for this frame type,use FrameUnsupported
        catch (ClassNotFoundException cex)
        {
            logger.info("Identifier not recognised:" + identifier + " using FrameBodyUnsupported");
            frameBody = new FrameBodyUnsupported(byteBuffer, frameSize);
        }
        //An error has occurred during frame  instantiation find out real exception and then throw frame away if invalid
        catch (InvocationTargetException ite)
        {
            ite.getCause().printStackTrace();
            logger.severe("invocation target exception:"+ite.getCause().getMessage());
            throw new InvalidFrameException(ite.getCause().getMessage());
        }
        //No Method should not happen
        catch (NoSuchMethodException sme)
        {
            logger.severe("no such method:"+sme.getMessage());
            throw new RuntimeException(sme.getMessage());
        }
        //Instantiate Interfrace/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.severe("instantiation exception:"+ie.getMessage());
            throw new RuntimeException(ie.getMessage());
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.severe("illegal access exception :"+iae.getMessage());
            throw new RuntimeException(iae.getMessage());
        }
        logger.finest("Created framebody:end" + frameBody.getIdentifier());
        return frameBody;
    }

    /**
     * This creates a new body based of type identifier but populated by the data
     * in the body. This is a different type to the body being created which is why
     * TagUtility.copyObject() can't be used. This is used when converting between
     * different versions of a tag for frames that have a non-trivial mapping such
     * as TYER in v3 to TDRC in v4.
     *
     * @param identifier DOCUMENT ME!
     * @param file       DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException         DOCUMENT ME!
     * @throws InvalidTagException DOCUMENT ME!
     */
    protected AbstractID3v2FrameBody readBody(String identifier, AbstractID3v2FrameBody body)

    {
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date,although slight performance hit.
         */
        AbstractID3v2FrameBody frameBody = null;
        try
        {
            Class c = Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            Class[] constructorParameterTypes = {body.getClass()};
            Object[] constructorParameterValues = {body};
            Constructor construct = c.getConstructor(constructorParameterTypes);
            frameBody = (AbstractID3v2FrameBody) (construct.newInstance(constructorParameterValues));
        }
        catch (ClassNotFoundException cex)
        {
            logger.info("Identifier not recognised:" + identifier + " using FrameBodyUnsupported");
        }
        catch (Exception e)
        {
            logger.info("Problem Initialising class" + identifier + " using FrameBodyUnsupported");
        }
        logger.finer("frame Body created" + frameBody.getIdentifier());
        return frameBody;
    }

    public abstract void write(ByteArrayOutputStream tagBuffer)
        throws IOException;

    protected StatusFlags getStatusFlags()
    {
        return statusFlags;
    }

    protected EncodingFlags getEncodingFlags()
    {
        return encodingFlags;
    }

    class StatusFlags
    {
        protected static final String TYPE_FLAGS = "statusFlags";

        protected byte originalFlags;
        protected byte writeFlags;

        protected StatusFlags()
        {

        }

        /**
         * This returns the flags as they were originally read or created
         */
        public byte getOriginalFlags()
        {
            return originalFlags;
        }

        /**
         * This returns the flags amended to meet specification
         */
        public byte getWriteFlags()
        {
            return writeFlags;
        }

        public void createStructure()
        {
        }


    }

    class EncodingFlags
    {
        protected static final String TYPE_FLAGS = "encodingFlags";

        protected byte flags;

        protected EncodingFlags()
        {

        }

        public byte getFlags()
        {
            return flags;
        }

        public void createStructure()
        {
        }
    }

    /**
     * Return String Representation of body
     *
     * @return DOCUMENT ME!
     */
    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_FRAME, getIdentifier());
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_FRAME);
    }
}
