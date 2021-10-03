package ealvatag.audio;

import ealvatag.TestUtil;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
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
        try {
            sourceFile = TestUtil.copyAudioToTmp("01.mp3");
            af = AudioFileIO.read(sourceFile);
        } catch (Throwable e) {
            throw new RuntimeException("Can't setUp test.", e);
        }
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    @Test public void testWriteAs() throws Exception {
        af.getTagOrSetNewDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
        af.save();

        assertNotNull(af.getTag().orNull());

        final String parent = sourceFile.getParent();
        File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
        af.saveAs(destinationNoExtension.getPath());

        Assert.assertEquals(destinationNoExtension + EXPECTED_EXTENSION, af.getFile().getPath());
        Assert.assertEquals(LANGUAGE, af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.LANGUAGE));
    }
}
