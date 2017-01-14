package ealvatag.audio;

import ealvatag.AbstractTestCase;
import ealvatag.audio.exceptions.CannotWriteException;
import ealvatag.tag.FieldKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class AudioFileWriteAsTest {

    private static final String EXPECTED_EXTENSION = ".mp3";
    private static final String LANGUAGE = "English";
    private static final String DESTINATION_FILE_NAME = "writeastest";
    private AudioFile af;
    private File sourceFile;

    @Before public void setUp() throws Exception {
        File orig = new File("testdata", "01.mp3");
        try {
            sourceFile = AbstractTestCase.copyAudioToTmp(orig.getName());
            af = AudioFileIO.read(sourceFile);
        } catch (Throwable e) {
            throw new RuntimeException("Can't setUp test.", e);
        }
    }

    @Test public void testWriteAs() throws Exception {
        af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
        af.commit();

        final String parent = sourceFile.getParent();
        File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
        AudioFileIO.writeAs(af, destinationNoExtension.getPath());

        Assert.assertEquals(destinationNoExtension + EXPECTED_EXTENSION, af.getFile().getPath());
        Assert.assertEquals(LANGUAGE, af.getTag().getFirst(FieldKey.LANGUAGE));
    }

    @Test(expected = CannotWriteException.class)
    public void testWriteAsWithNull() throws Exception {
        //noinspection ConstantConditions
        AudioFileIO.writeAs(af, null);
    }
}
