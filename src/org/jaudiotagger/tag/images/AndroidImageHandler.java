package org.jaudiotagger.tag.images;

import org.jaudiotagger.tag.datatype.Artwork;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 Image Handling to to use when running on Android

 TODO need to provide Android compatible implementations
 */
public class AndroidImageHandler implements ImageHandler
{

    public BufferedImage getImage(ByteArrayInputStream bais) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public BufferedImage getImage2(ByteArrayInputStream bais) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Resize the image until the total size require to store the image is less than maxsize
     * @param artwork
     * @param maxSize
     * @throws IOException
     */
    public void reduceQuality(Artwork artwork, int maxSize) throws IOException
    {
        throw new UnsupportedOperationException();
    }
     /**
     * Resize image using Java 2D
      * @param artwork
      * @param size
      * @throws java.io.IOException
      */
    public void makeSmaller(Artwork artwork,int size) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    public boolean isMimeTypeWritable(String mimeType)
    {
        throw new UnsupportedOperationException();
    }

    /**
     *  Write buffered image as required format
     *
     * @param bi
     * @param mimeType
     * @return
     * @throws IOException
     */
    public byte[] writeImage(BufferedImage bi,String mimeType) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param bi
     * @return
     * @throws IOException
     */
    public byte[] writeImageAsPng(BufferedImage bi) throws IOException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Show read formats
     *
     * On Windows supports png/jpeg/bmp/gif
     */
    public void showReadFormats()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Show write formats
     *
     * On Windows supports png/jpeg/bmp
     */
    public void showWriteFormats()
    {
        throw new UnsupportedOperationException();
    }
}
