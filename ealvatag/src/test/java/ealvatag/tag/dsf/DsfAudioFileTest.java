package ealvatag.tag.dsf;


import ealvatag.AbstractTestCase;
import ealvatag.FilePermissionsTest;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.id3.ID3v24Tag;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;

public class DsfAudioFileTest {

    @Test public void testReadDsfMetadata() throws Exception {
        File testFile = AbstractTestCase.copyAudioToTmp("test122.dsf", new File("test122readmetadata.dsf"));
        AudioFile f = AudioFileIO.read(testFile);
        System.out.println(f.getTag().or(NullTag.INSTANCE));
        Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof ID3v24Tag);
        assertEquals("test3", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
        assertEquals("Artist", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
        assertEquals("Album Artist", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST));
        assertEquals("Album", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
        assertEquals("Crossover", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.GENRE));
        assertEquals("comments", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMMENT));
        assertEquals("Publisher", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.RECORD_LABEL));
        assertEquals("Composer", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.COMPOSER));
    }

    @Test public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test122.dsf");
    }

    @Test public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test122.dsf");
    }

    @Test public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test122.dsf");
    }
}
