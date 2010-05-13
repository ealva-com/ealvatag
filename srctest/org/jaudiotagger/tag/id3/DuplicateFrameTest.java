package org.jaudiotagger.tag.id3;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;

import java.io.File;

/**
 * Test if read a tag with a corrupt frame that in certain circumstances continue to read the other frames
 * regardless.
 */
public class DuplicateFrameTest extends AbstractTestCase
{
    public void testReadingFileWithCorruptFirstFrame() throws Exception
    {
        File orig = new File("testdata", "test78.mp3");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }
        File testFile = AbstractTestCase.copyAudioToTmp("test78.mp3");

        MP3File f = (MP3File)AudioFileIO.read(testFile);
        Tag tag = f.getTag();
        assertTrue(f.getTag() instanceof ID3v23Tag);
        ID3v23Tag id3v23tag = (ID3v23Tag)tag;
        //Frame contains two TYER frames
        assertEquals(21,id3v23tag.getDuplicateBytes());
        assertEquals("*TYER*","*"+id3v23tag.getDuplicateFrameId()+"*");
        f.commit();
        f = (MP3File)AudioFileIO.read(testFile);
        tag = f.getTag();
        id3v23tag = (ID3v23Tag)tag;
        //After save the duplicate frame has been discarded
        assertEquals(0,id3v23tag.getDuplicateBytes());
        assertEquals("",id3v23tag.getDuplicateFrameId());

    }
}