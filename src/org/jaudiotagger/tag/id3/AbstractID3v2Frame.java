/*
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
 */
package org.jaudiotagger.tag.id3;

import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyUnsupported;
import org.jaudiotagger.tag.id3.framebody.AbstractID3v2FrameBody;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.*;
import java.nio.*;
import java.util.logging.Level;

/**
 * This abstract class is each frame header inside a ID3v2 tag.
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
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

    //The purpose of this is to provide the filename that should be used when writing debug messages
    //when problems occur reading or writing to file, otherwise it is difficult to track down the error
    //when processing many files
    private String loggingFilename = "";

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
     * This holds the Encoding flags (not supported in v2.20)
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
     * Create a new frame with empty body based on identifier
     *
     * @TODO the identifier checks should be done in the relevent subclasses
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
            frameBody = new FrameBodyUnsupported(identifier);
        }
        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE,"InstantiationException:" + identifier,ie);
            throw new RuntimeException(ie);
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE,"IllegalAccessException:" + identifier,iae);
            throw new RuntimeException(iae);
        }
        logger.info("Created empty frame of type" + identifier);
    }

     /**
     * Retrieve the logging filename to be used in debugging
     *
     * @return logging filename to be used in debugging
     */
    protected String getLoggingFilename()
    {
        return loggingFilename;
    }

    /**
     * Set logging filename when construct tag for read from file
     *
     * @param loggingFilename
     */
    protected void setLoggingFilename(String loggingFilename)
    {
        this.loggingFilename = loggingFilename;
    }

    /**
     * Return the frame identifier
     *
     * @return the frame identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Read the frame body from the specified file via the buffer
     *
     * @param identifier the frame identifier
     * @param byteBuffer to read the frabe body from
     * @return a newly created FrameBody
     * @throws InvalidFrameException  unable to construct a framebody from the data
     *
     */
    protected AbstractID3v2FrameBody readBody(String identifier, ByteBuffer byteBuffer, int frameSize)
        throws InvalidFrameException
    {
        //Use reflection to map id to frame body, which makes things much easier
        //to keep things up to date,although slight performance hit.
        logger.finest("Creating framebody:start");

        AbstractID3v2FrameBody frameBody;
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
            logger.info(getLoggingFilename()+":"+"Identifier not recognised:" + identifier + " using FrameBodyUnsupported");
            try
            {
                frameBody = new FrameBodyUnsupported(byteBuffer, frameSize);
            }
            //Should only throw InvalidFrameException but unfortunately legacy hierachy forces
            //read method to declare it can throw InvalidtagException
            catch(InvalidFrameException ife)
            {
                throw ife;
            }
            catch(InvalidTagException te)
            {
                throw new InvalidFrameException(te.getMessage());
            }
        }
        //An error has occurred during frame instantiation, if underlying cause is an unchecked exception or error
        //propagate it up otherwise mark this frame as invalid
        catch (InvocationTargetException ite)
        {
            logger.severe(getLoggingFilename()+":"+"An error occurred within abstractID3v2FrameBody for identifier:"
                +identifier+":"+ite.getCause().getMessage());  
            if(ite.getCause() instanceof Error)
            {
                throw (Error)ite.getCause();
            }
            else if(ite.getCause() instanceof RuntimeException)
            {
                throw (RuntimeException)ite.getCause();
            }
            else
            {
                throw new InvalidFrameException(ite.getCause().getMessage());
            }
        }
        //No Such Method should not happen
        catch (NoSuchMethodException sme)
        {
            logger.log(Level.SEVERE,getLoggingFilename()+":"+"No such method:"+sme.getMessage(),sme);
            throw new RuntimeException(sme.getMessage());
        }
        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE,getLoggingFilename()+":"+"Instantiation exception:"+ie.getMessage(),ie);
            throw new RuntimeException(ie.getMessage());
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE,getLoggingFilename()+":"+"Illegal access exception :"+iae.getMessage(),iae);
            throw new RuntimeException(iae.getMessage());
        }
        logger.finest(getLoggingFilename()+":"+"Created framebody:end" + frameBody.getIdentifier());
        return frameBody;
    }

    /**
     * This creates a new body based of type identifier but populated by the data
     * in the body. This is a different type to the body being created which is why
     * TagUtility.copyObject() can't be used. This is used when converting between
     * different versions of a tag for frames that have a non-trivial mapping such
     * as TYER in v3 to TDRC in v4. This will only work where appropriate constructors
     * exist in the frame body to be created, for example a FrameBodyTYER requires a constructor
     * consisting of a FrameBodyTDRC.
     *
     * If this method is called and a suitable constructor does not exist then an InvalidFrameException
     * will be thrown
     *
     * @param identifier to determine type of the frame
     * @throws InvalidFrameException if unable to construct a framebody for the identifier and body provided.
     * @return newly created framebody for this type
      */
    protected AbstractID3v2FrameBody readBody(String identifier, AbstractID3v2FrameBody body)
    throws InvalidFrameException
    {
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date, although slight performance hit.
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
            logger.info("Identifier not recognised:" + identifier + " unable to create framebody");
            throw new InvalidFrameException("FrameBody"+identifier + " does not exist");
        }
        //If suitable constructor does not exist
        catch (NoSuchMethodException sme)
        {
            logger.log(Level.SEVERE,"No such method:"+sme.getMessage(),sme);
            throw new InvalidFrameException("FrameBody"+identifier + " does not have a constructor that takes:"+body.getClass().getName());
        }
        catch (InvocationTargetException ite)
        {
            logger.severe("An error occurred within abstractID3v2FrameBody");
            logger.log(Level.SEVERE,"Invocation target exception:"+ite.getCause().getMessage(),ite.getCause());
            if(ite.getCause() instanceof Error)
            {
                throw (Error)ite.getCause();
            }
            else if(ite.getCause() instanceof RuntimeException)
            {
                throw (RuntimeException)ite.getCause();
            }
            else
            {
                throw new InvalidFrameException(ite.getCause().getMessage());
            }
        }

        //Instantiate Interface/Abstract should not happen
        catch (InstantiationException ie)
        {
            logger.log(Level.SEVERE,"Instantiation exception:"+ie.getMessage(),ie);
            throw new RuntimeException(ie.getMessage());
        }
        //Private Constructor shouild not happen
        catch (IllegalAccessException iae)
        {
            logger.log(Level.SEVERE,"Illegal access exception :"+iae.getMessage(),iae);
            throw new RuntimeException(iae.getMessage());
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
            resetFlags();
        }

        protected EncodingFlags(byte flags)
        {
           setFlags(flags);
        }

        public byte getFlags()
        {
            return flags;
        }

        public void setFlags(byte flags)
        {
            this.flags=flags;
        }

        public void resetFlags()
        {
            setFlags((byte) 0);
        }

        public void createStructure()
        {
        }
    }

    /**
     * Return String Representation of frame
     */
    public void createStructure()
    {
        MP3File.getStructureFormatter().openHeadingElement(TYPE_FRAME, getIdentifier());
        MP3File.getStructureFormatter().closeHeadingElement(TYPE_FRAME);
    }
}
