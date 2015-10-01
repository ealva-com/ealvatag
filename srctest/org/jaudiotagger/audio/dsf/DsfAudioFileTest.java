package org.jaudiotagger.audio.dsf;


import junit.framework.TestCase;
import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;

import java.io.File;

public class DsfAudioFileTest extends TestCase {

    public void testReadDsf() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test122.dsf");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf",new File("test122read.dsf"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            System.out.println(ah);
            assertEquals("5644800",ah.getBitRate());
            assertEquals(5644800,ah.getBitRateAsNumber());
            assertEquals("2",ah.getChannels());
            assertEquals("2822400",ah.getSampleRate());
            assertEquals(4,ah.getTrackLength());
            assertFalse(ah.isLossless());

        }
        catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        

    }

}
