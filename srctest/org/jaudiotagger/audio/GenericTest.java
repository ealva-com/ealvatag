package org.jaudiotagger.audio;

import junit.framework.TestCase;

import java.io.File;

/**
 * Generic tests
 */
public class GenericTest extends TestCase
{
    /**
     * Test File filter, postive and negative tests
     */
    public void testReadFileUnsupportedFormat()
    {
        File nonAudioFile = new File("testdata", "coverart.bmp");
        AudioFileFilter aff = new AudioFileFilter();
        aff.accept(nonAudioFile);
        assertFalse(aff.accept(nonAudioFile));

        File audioFile = new File("testdata", "test.m4a");
        aff.accept(audioFile);
        assertTrue(aff.accept(audioFile));

        audioFile = new File("testdata", "test.flac");
        aff.accept(audioFile);
        assertTrue(aff.accept(audioFile));

        audioFile = new File("testdata", "test.ogg");
        aff.accept(audioFile);
        assertTrue(aff.accept(audioFile));

        audioFile = new File("testdata", "testV1.mp3");
        aff.accept(audioFile);
        assertTrue(aff.accept(audioFile));
    }
}
