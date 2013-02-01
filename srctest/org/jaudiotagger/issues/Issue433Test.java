package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Test
 */
public class Issue433Test extends AbstractTestCase
{
    public void testWriteMp4() throws Exception
    {
        Exception ex=null;
        File orig = new File("testdata", "test112.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = AbstractTestCase.copyAudioToTmp("test112.m4a");

        Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
        atomTree.printAtomTree();

        AudioFile af = AudioFileIO.read(testFile);


        /* Still fails
        af.getTag().setField(FieldKey.ALBUM,"fred");
        af.commit();
        */
    }


}
