package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTLAN;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTKEY;
import org.jaudiotagger.tag.reference.Languages;
import org.jaudiotagger.tag.reference.MusicalKey;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

/**
 * Support For LANGUAGE and INITIAL_KEY
 */
public class Issue241Test extends AbstractTestCase
{
    /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v24()
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            tag = af.getTag();
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
            TagField tagField = af.getTag().getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName());
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            assertTrue(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tagField = af.getTag().getFirstField(ID3v24FieldKey.KEY.getFieldName());
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            assertTrue(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());

            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"fred"));
            af.commit();
            assertEquals("fred",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            tagField = af.getTag().getFirstField(ID3v24FieldKey.LANGUAGE.getFieldName());
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTLAN);
            assertFalse(((FrameBodyTLAN)((ID3v24Frame)tagField).getBody()).isValid());

            tag.set(tag.createTagField(TagFieldKey.KEY,"keys"));
            af.commit();
            assertEquals("keys",af.getTag().getFirst(TagFieldKey.KEY));
            tagField = af.getTag().getFirstField(ID3v24FieldKey.KEY.getFieldName());
            assertTrue(tagField instanceof ID3v24Frame);
            assertTrue(((ID3v24Frame)tagField).getBody() instanceof FrameBodyTKEY);
            assertFalse(((FrameBodyTKEY)((ID3v24Frame)tagField).getBody()).isValid());


        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
     */
    public void testWriteFieldsToMp3ID3v23()
    {
        File testFile = null;
        Exception exceptionCaught = null;
        try
        {
            testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3");

            //Read File okay
            AudioFile af = AudioFileIO.read(testFile);
            af.getTagOrCreateAndSetDefault();
            Tag tag = af.getTag();
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp3 ID3v23
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing INITIAL_KEY and LANGUAGE to Ogg Vorbis
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

    /**
     * Test writing INITIAL_KEY and LANGUAGE to Flac
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }



    /**
     * Test writing INITIAL_KEY and LANGUAGE to Wma
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Test writing INITIAL_KEY and LANGUAGE to Mp4
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
            tag.set(tag.createTagField(TagFieldKey.LANGUAGE,"eng"));
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            af.commit();
            af = AudioFileIO.read(testFile);
            assertEquals("eng",af.getTag().getFirst(TagFieldKey.LANGUAGE));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertEquals("English",Languages.getInstanceOf().getValueForId(af.getTag().getFirst(TagFieldKey.LANGUAGE)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }

     /**
     * Key Validation
     */
    public void testValidateMusicalKey()
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


            tag.set(tag.createTagField(TagFieldKey.KEY,"G"));
            assertEquals("G",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));
            
            tag.set(tag.createTagField(TagFieldKey.KEY,"C#"));
            assertEquals("C#",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"Cm"));
            assertEquals("Cm",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"C#m"));
            assertEquals("C#m",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"C"));
            assertEquals("C",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"o"));
            assertEquals("o",af.getTag().getFirst(TagFieldKey.KEY));
            assertTrue(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"R#"));
            assertEquals("R#",af.getTag().getFirst(TagFieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"Cp"));
            assertEquals("Cp",af.getTag().getFirst(TagFieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

            tag.set(tag.createTagField(TagFieldKey.KEY,"C##"));
            assertEquals("C##",af.getTag().getFirst(TagFieldKey.KEY));
            assertFalse(MusicalKey.isValid(tag.getFirst(TagFieldKey.KEY)));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }

        assertNull(exceptionCaught);
    }


}
