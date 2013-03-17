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

        File orig = new File("testdata", "M1F1-int8C-AFsp.aif");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = AbstractTestCase.copyAudioToTmp("M1F1-int8C-AFsp.aif");
        AiffAudioHeader aiffAudioHeader = null;
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue (ah instanceof AiffAudioHeader);
        }
        catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
        
/*        testFile = AbstractTestCase.copyAudioToTmp("M1F1-int8-AFsp.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue (ah instanceof AiffAudioHeader);
            AiffAudioHeader aah = (AiffAudioHeader) ah;
            List<String> appIdentifiers = aah.getApplicationIdentifiers();
            String ident = appIdentifiers.get(0);
            assertTrue(ident.indexOf ("CAPELLA") > 0);
        }
        catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);

        testFile = AbstractTestCase.copyAudioToTmp("ExportedFromItunes.aif");
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            assertTrue (ah instanceof AiffAudioHeader);
            Tag tag = f.getTag();
            assertNotNull (tag);
            assertTrue (tag instanceof AiffTag);
            assertTrue (tag.getFieldCount() == 10);
            assertEquals ("Gary McGath", tag.getFirst(FieldKey.ARTIST));
            assertEquals ("None", tag.getFirst(FieldKey.ALBUM));
            assertTrue (tag.getFirst(FieldKey.TITLE).indexOf ("Short sample") == 0);
            assertEquals ("This is actually a comment.", tag.getFirst(FieldKey.COMMENT));
            assertEquals ("2012", tag.getFirst(FieldKey.YEAR));
            assertEquals ("1", tag.getFirst(FieldKey.TRACK));
            
        }
        catch (Exception e) {
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
  */
    }

}
