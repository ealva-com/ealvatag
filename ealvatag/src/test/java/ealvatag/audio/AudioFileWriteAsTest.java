package ealvatag.audio;

import ealvatag.AbstractTestCase;
import ealvatag.tag.FieldKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

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
        af.getTagOrSetNewDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
        af.save();

        assertNotNull(af.getTag());

        final String parent = sourceFile.getParent();
        File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
        af.saveAs(destinationNoExtension.getPath());

        Assert.assertEquals(destinationNoExtension + EXPECTED_EXTENSION, af.getFile().getPath());
        Assert.assertEquals(LANGUAGE, af.getTag().getFirst(FieldKey.LANGUAGE));
    }
}
