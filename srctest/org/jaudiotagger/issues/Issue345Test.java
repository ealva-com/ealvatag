package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyCOMM;
import org.jaudiotagger.tag.id3.framebody.FrameBodyPOPM;
import org.jaudiotagger.tag.reference.Languages;

import java.io.File;

/**
 * Support For Custom fields
 */
public class Issue345Test extends AbstractTestCase
{
    /**
     * Test writing Custom fields
     */
    public void  testWriteFieldsToMp3ID3v24()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v24Tag());
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.RATING);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
            }
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            {
                TagField tagField = af.getTag().getFirstField(FieldKey.RATING);
                assertTrue(tagField instanceof ID3v24Frame);
                assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyPOPM);
                FrameBodyPOPM body =(FrameBodyPOPM)((ID3v24Frame)tagField).getBody();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields
     */
    public void testWriteFieldsToMp3ID3v23()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v22()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.setTag(new ID3v22Tag());
            Tag tag = af.getTag();

            tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Ogg Vorbis
     */
    public void testWriteFieldsToOggVorbis()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.ogg");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
              tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing Custom fields to Flac
     */
    public void testWriteFieldsToFlac()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.flac");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
              tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }



    /**
     * Test writing Custom fields to Wma
     */
    public void testWriteFieldsToWma()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test1.wma");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing Custom fields to Mp4
     */
    public void testWriteFieldsToMp4()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("test.m4a");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            Tag tag = af.getTag();
            tag.setField(tag.createField(FieldKey.RATING,"50"));
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("50",af.getTag().getFirst(FieldKey.RATING));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }




}