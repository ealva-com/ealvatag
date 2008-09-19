package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.File;

/**
 * Test Deleteing comments with common interface
 */
public class Issue239Test extends AbstractTestCase
{
    /**
     * Test Deleting Comments
     */
    public void testDeletingCOMMFrames()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File= (MP3File)af;
            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.save();
            mp3File = new MP3File(testFile);

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertEquals(0,mp3File.getTag().get(TagFieldKey.COMMENT).size());

            //Now write these fields
            mp3File.getTag().set(mp3File.getTag().createTagField(TagFieldKey.COMMENT,"comment1"));
            mp3File.getTag().add(mp3File.getTag().createTagField(TagFieldKey.COMMENT,"comment2"));

            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertTrue(mp3File.getTag() instanceof ID3v23Tag);
            assertEquals(2,mp3File.getTag().get(TagFieldKey.COMMENT).size());

            //Delete Fields
            mp3File.getTag().deleteTagField(TagFieldKey.COMMENT);
            mp3File.save();
            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().get(TagFieldKey.COMMENT).size());
        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }
}
