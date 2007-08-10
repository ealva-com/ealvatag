package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFile;

import java.io.File;

/**
 */
public class VorbisImageTest extends AbstractTestCase
{
    /**
     * Test can read file with base64 encoded image
     *
     * Works
     */
    public void testReadFileWithSmallImageTag()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testsmallimage.ogg");
            AudioFile f = AudioFileIO.read(testFile);
            String mimeType  = ((VorbisCommentTag)f.getTag()).getFirst(VorbisCommentFieldKey.COVERARTMIME);
            assertEquals("image/jpeg",mimeType);
            if(mimeType!=null&mimeType.length()>0)
            {
                String imageRawData = ((VorbisCommentTag)f.getTag()).getFirst(VorbisCommentFieldKey.COVERART);
                assertEquals(22972,imageRawData.length());
            }
        }
        catch(Exception e)
        {
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test can read file with base64 encoded image thats spans multiple ogg pages
     *
     * Fails:Doesnt give error but doesnt read image
     */
    public void testReadFileWithLargeImageTag()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testlargeimage.ogg");
            AudioFile f = AudioFileIO.read(testFile);
            String mimeType  = ((VorbisCommentTag)f.getTag()).getFirst(VorbisCommentFieldKey.COVERARTMIME);
            assertEquals("image/jpeg",mimeType);
            if(mimeType!=null&mimeType.length()>0)
            {
                String imageRawData = ((VorbisCommentTag)f.getTag()).getFirst(VorbisCommentFieldKey.COVERART);
                assertEquals(1013576,imageRawData.length());
            }
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


}
