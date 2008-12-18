package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.ID3v23Frames;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp4.Mp4AtomTree;

import java.io.File;
import java.io.RandomAccessFile;

/** Test write to mp4
 */
public class Issue270Test extends AbstractTestCase
{

    /**
     * Test write to mp4 file where we cant find the audio
     *
     * Not fixed yet but cathes the error earlier than did before when was throwing null pointer
     */
    public void testWriteToMp4()
    {
        File orig = new File("testdata", "test49.m4a");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test49.m4a");

            //First lets just create tree  , doesnt crash system but last atom is not valid it doesnt shown mdat tree
            Mp4AtomTree atomTree = new Mp4AtomTree(new RandomAccessFile(testFile, "r"));
            atomTree.printAtomTree();

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            System.out.println(af.getTag().toString());

            //Try and write to file
            af.getTag().setAlbum("fred");
            af.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNotNull(exceptionCaught);
        assertTrue(exceptionCaught.getMessage().contains("Unable to make changes to Mp4 file, unable to determine start of audio"));
    }
}
