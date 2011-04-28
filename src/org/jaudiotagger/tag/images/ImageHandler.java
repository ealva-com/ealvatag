package org.jaudiotagger.tag.images;

import org.jaudiotagger.tag.datatype.Artwork;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Image Handler
 */
public interface ImageHandler
{
    public BufferedImage getImage(ByteArrayInputStream bais) throws IOException;
    public BufferedImage getImage2(ByteArrayInputStream bais) throws IOException;
    public void reduceQuality(Artwork artwork, int maxSize) throws IOException;
    public void makeSmaller(Artwork artwork,int size) throws IOException;
    public boolean isMimeTypeWritable(String mimeType);
    public byte[] writeImage(BufferedImage bi,String mimeType) throws IOException;
    public byte[] writeImageAsPng(BufferedImage bi) throws IOException;
    public void showReadFormats();
    public void showWriteFormats();
}
