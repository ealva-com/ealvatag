package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v23Tag;

import java.io.File;

/**
 * Test Deleteing comments with common interface
 */
public class Issue239Test extends AbstractTestCase
{
    /**
     * Test Deleting Plain Comments
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
            assertEquals(0,mp3File.getTag().getFields(FieldKey.COMMENT).size());

            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.COMMENT,"comment1"));
            mp3File.getTag().addField(mp3File.getTag().createField(FieldKey.COMMENT,"comment2"));

            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertTrue(mp3File.getTag() instanceof ID3v23Tag);
            assertEquals(2,mp3File.getTag().getFields(FieldKey.COMMENT).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.COMMENT);
            assertEquals(0,mp3File.getTag().getFields(FieldKey.COMMENT).size());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.COMMENT).size());

        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test Deleting  Comments with Description field
     */
    public void testDeletingFieldThatUsesCOMMFrames()
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
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());

            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.CUSTOM1,"comment1"));
            mp3File.getTag().addField(mp3File.getTag().createField(FieldKey.CUSTOM1,"comment2"));

            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertTrue(mp3File.getTag() instanceof ID3v23Tag);
            assertEquals(2,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.CUSTOM1);
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());

        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test Deleting  Comments with Description field only deletes the correct comments
     */
    public void testDeletingFieldThatUsesCOMMFramesDoesntDeleteOtherCOMMFrame()
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
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());

            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.CUSTOM1,"comment1"));
            mp3File.getTag().addField(mp3File.getTag().createField(FieldKey.CUSTOM2,"comment2"));

            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertTrue(mp3File.getTag() instanceof ID3v23Tag);
            assertEquals(1,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.CUSTOM1);
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.CUSTOM2).size());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.CUSTOM1).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.CUSTOM2).size());

        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }
}
