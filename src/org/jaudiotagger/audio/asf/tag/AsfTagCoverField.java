package org.jaudiotagger.audio.asf.tag;

import org.jaudiotagger.audio.asf.data.ContentDescriptor;
import org.jaudiotagger.audio.asf.data.AsfHeader;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.logging.ErrorMessage;

import javax.imageio.ImageIO;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 * Enscapulates the WM/Pictures provides some convenience methods for decoding the binary data it contains
 *
 * The value of a Wm/Pictures content descriptor is as follows:
 *
 * byte0    Picture Type
 * byte1-4  Length of the image data
 * mimetype encoded as UTF-16LE
 * null byte
 * null byte
 * description encoded as UTF-16LE (optional)
 * null byte
 * null byte
 * image data
 *
 */
public class AsfTagCoverField extends AsfTagField
{
    /** Logger Object  */
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.asf.tag");

    /**
     * Picture Type
     */
    private int pictureType;

    /** Mimetype of binary
     *
     */
    private String mimeType;

    /** Description
     *
     */
    private String description;

    /**
     * Image Data Size as read
     */
    private int imageDataSize;

    /**
     * We need this to retrive the buffered image, if required
     */
    private int endOfName=0;

     /**
     * Creates an instance from a content descriptor
     *
     * @param source The content descriptor, whose content is published.<br>

     */
    public AsfTagCoverField(ContentDescriptor source)
    {
        super(source);

        if(!source.getName().equals(AsfFieldKey.COVER_ART.getFieldName()))
        {
            throw new IllegalArgumentException("Descriptor description must be WM/Picture");
        }
        if (source.getType() != ContentDescriptor.TYPE_BINARY)
        {
            throw new IllegalArgumentException("Descriptor type must be binary");
        }

        try
        {
            processRawContent();
        }
        catch(UnsupportedEncodingException uee)
        {
            //Should never happen
            throw new RuntimeException(uee);
        }
    }

    /**
     * Create New Image Field
     *
     * @param imageData
     * @param description
     */
    public AsfTagCoverField(byte[] imageData,int pictureType,String description)
    {
        super(new ContentDescriptor(AsfFieldKey.COVER_ART.getFieldName(), ContentDescriptor.TYPE_BINARY));
        this.getDescriptor().setBinaryValue(createRawContent(imageData,pictureType,description));
    }

    private byte[] createRawContent(byte[] data,int pictureType,String description)
    {
        //Get Mimetype
        mimeType=ImageFormats.getMimeTypeForBinarySignature(data);
        description=description;
        if(mimeType==null)
        {
            throw new RuntimeException(ErrorMessage.GENERAL_UNIDENITIFED_IMAGE_FORMAT.getMsg());
        }

        ByteArrayOutputStream baos = new  ByteArrayOutputStream();

        //PictureType
        baos.write(pictureType);

        //ImageDataSize
        baos.write(org.jaudiotagger.audio.generic.Utils.getSizeLEInt32(data.length),0,4);

        //mimetype
        byte[] mimeTypeData;
        try
        {
            mimeTypeData=mimeType.getBytes(AsfHeader.ASF_CHARSET.name());
        }
        catch(UnsupportedEncodingException  uee)
        {
            //Should never happen
            throw new RuntimeException("Unable to find encoding:"+AsfHeader.ASF_CHARSET.name());
        }
        baos.write(mimeTypeData,0,mimeTypeData.length);

        //Seperator
        baos.write(0x00);
        baos.write(0x00);

        //description
        if(description!=null && description.length()>0)
        {
            byte[] descriptionData;
            try
            {
                descriptionData=description.getBytes(AsfHeader.ASF_CHARSET.name());
            }
            catch(UnsupportedEncodingException  uee)
            {
                //Should never happen
                throw new RuntimeException("Unable to find encoding:"+AsfHeader.ASF_CHARSET.name());
            }
            baos.write(descriptionData,0,descriptionData.length);
        }

        //Seperator (always write whther or not we have descriptor field)
        baos.write(0x00);
        baos.write(0x00);

        //Image data
        baos.write(data,0,data.length);

        return baos.toByteArray();
    }


    private void processRawContent() throws UnsupportedEncodingException
    {
        //PictureType
        pictureType=this.getRawContent()[0];

        //ImageDataSize
        imageDataSize=org.jaudiotagger.audio.generic.Utils.getIntLE(this.getRawContent(), 1, 2);

        //Set Count to after picture type,datasize and two byte nulls
        int count=5;
        mimeType=null;
        description =null; //Optional
        int endOfMimeType=0;
       
        while(count<this.getRawContent().length - 1)
        {
            if(getRawContent()[count]==0 && getRawContent()[count+1]==0 )
            {
                if(mimeType==null)
                {
                    mimeType = new String(getRawContent(),5,(count)-5,"UTF-16LE");
                    endOfMimeType=count+2;
                }
                else if(description ==null)
                {
                    description = new String(getRawContent(),endOfMimeType,count - endOfMimeType,"UTF-16LE");
                    endOfName=count+2;
                    break;
                }
            }
            count+=2;  //keep on two byte word boundary
        }
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public String getDescription()
    {
        return description;
    }

    public int getPictureType()
    {
        return pictureType;
    }

    public int getImageDataSize()
    {
        return imageDataSize;
    }

    /**
     *
     * @return the raw image data only
     */
    public byte[] getRawImageData()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(getRawContent(),endOfName,getRawContent().length - endOfName);
        return baos.toByteArray();
    }

    /**
     *
     * @return the image
     * @throws IOException
     */
    public BufferedImage getImage() throws IOException
    {
        BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(getRawImageData())));
        return bi;
    }
}
