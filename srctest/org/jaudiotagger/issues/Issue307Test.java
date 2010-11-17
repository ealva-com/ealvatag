package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.ID3v23Frame;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyIPLS;

import java.io.File;

/**
 * Test Reading dodgy IPLS frame shouldnt cause file not to be loaded
 */
public class Issue307Test extends AbstractTestCase
{
    public static int countExceptions =0;

    public void testMultiThreadedMP3HeaderAccess() throws Exception
    {
        File orig = new File("testdata", "test71.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception e=null;
        MP3File mp3File = null;
        try
        {
            final File testFile = AbstractTestCase.copyAudioToTmp("test71.mp3");
            if (!testFile.isFile())
            {
                System.err.println("Unable to test file - not available");
                return;
            }
            mp3File = new MP3File(testFile);
        }
        catch(Exception ex)
        {
            e=ex;
            ex.printStackTrace();
        }
        assertNull(e);
        FrameBodyIPLS frameBody = (FrameBodyIPLS)(((ID3v23Frame)((ID3v23Tag)mp3File.getTag()).getFirstField("IPLS")).getBody());
        assertEquals(3,frameBody.getNumberOfPairs());
        assertEquals("producer",frameBody.getKeyAtIndex(0));
        assertEquals("Tom Wilson",frameBody.getValueAtIndex(0));
        assertEquals("producer",frameBody.getKeyAtIndex(1));
        assertEquals("John H. Hammond",frameBody.getValueAtIndex(1));


    }
}