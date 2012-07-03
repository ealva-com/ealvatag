package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.FlacAudioHeader;

import java.io.File;

/**
 * Reading MD5 Integrity Checksum
 */
public class Issue428Test extends AbstractTestCase
{
    public void testGetMD5ForFlac()
    {
        Throwable e = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertTrue(af.getAudioHeader() instanceof FlacAudioHeader);
            assertEquals("4d285826d15a2d38b4d02b4dc2d3f4e1",((FlacAudioHeader)af.getAudioHeader()).getMd5());

        }
        catch(Exception ex)
        {
            e=ex;
        }
        assertNull(e);
    }
}
