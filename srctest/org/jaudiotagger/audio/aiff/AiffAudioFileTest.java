package org.jaudiotagger.audio.aiff;


import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class AiffAudioFileTest extends TestCase {

    public void testReadAiff() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test119.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue (ah instanceof AiffAudioHeader);
            AiffAudioHeader aah = (AiffAudioHeader)ah;
            assertEquals("128000",ah.getBitRate());
            assertEquals(128000,ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("8000",ah.getSampleRate());
            assertEquals(2,ah.getTrackLength());
            assertEquals("not compressed", ((AiffAudioHeader) ah).getAudioEncoding());
            //assertEquals(2.936625,((AiffAudioHeader) ah).getPreciseLength());
            assertEquals(null, aah.getName());
            assertEquals(null, aah.getCopyright());
            assertEquals(null, aah.getAuthor());
            assertNotNull(aah.getComments());
            System.out.println(ah);
        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        

    }

}
