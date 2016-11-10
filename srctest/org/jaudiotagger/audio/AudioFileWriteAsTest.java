package org.jaudiotagger.audio;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

/**
 * Able to write language ensures writes it as iso code for mp3s
 */
public class AudioFileWriteAsTest extends AbstractTestCase {

    public static final String EXPECTED_EXTENSION = ".mp3";
    public static final String LANGUAGE = "English";
    private static final String DESTINATION_FILE_NAME = "writeastest";
    private AudioFile af;
    private File sourceFile;

    @Override
    public void setUp() {
        super.setUp();
        File orig = new File("testdata", "01.mp3");
        try {
            sourceFile = AbstractTestCase.copyAudioToTmp(orig.getName());
            af = AudioFileIO.read(sourceFile);
        } catch (Throwable e) {
            throw new RuntimeException("Can't setUp test.", e);
        }
    }

    public void testWriteAs() throws Exception
    {
        af.getTagOrCreateAndSetDefault().setField(FieldKey.LANGUAGE, LANGUAGE);
        af.commit();

        final String parent = sourceFile.getParent();
        File destinationNoExtension = new File(parent, DESTINATION_FILE_NAME);
        AudioFileIO.writeAs(af, destinationNoExtension.getPath());

        assertEquals(destinationNoExtension + EXPECTED_EXTENSION, af.getFile().getPath());
        assertEquals(LANGUAGE, af.getTag().getFirst(FieldKey.LANGUAGE));
    }

    public void testWriteAsWithNull() throws Exception
    {
        try {
            AudioFileIO.writeAs(af, null);
        } catch (CannotWriteException e) {
            // expected
            return;
        }
        fail("Didn't get expected exception " + CannotWriteException.class);
    }
}