package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.logging.ErrorMessage;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagOptionSingleton;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import java.io.File;

/**
 * Test
 */
public class Issue173Test extends AbstractTestCase
{
    public void testMp4GenresUsingGenericInterface()
    {
        try
        {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = AbstractTestCase.copyAudioToTmp("01.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag) mp4File.getTag();
            //Set valid value
            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            mp4File.commit();

            //Rereads as value
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag) mp4File.getTag();
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            //Set Genre using integer
            tag.setField(FieldKey.GENRE, "1");
            //Read back as integer
            //TODO shoud read back as Blues here I think
            assertEquals("1", tag.getFirst(FieldKey.GENRE));
            assertEquals("1", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.commit();
            //On fresh reread shows as mapped value
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag) mp4File.getTag();
            assertEquals("Blues", tag.getFirst(FieldKey.GENRE));
            assertEquals("Blues", tag.getFirst(Mp4FieldKey.GENRE));

            //Set value that can only be stored as a custom
            //because using generic interface the genre field is removed automtically
            tag.setField(FieldKey.GENRE, "FlapFlap");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            assertEquals("FlapFlap", tag.getFirst(FieldKey.GENRE));
            assertEquals("FlapFlap", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.commit();
            assertEquals("FlapFlap", tag.getFirst(FieldKey.GENRE));
            assertEquals("FlapFlap", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));

            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.commit();
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));

            //Always use custom
            TagOptionSingleton.getInstance().setWriteMp4GenresAsText(true);
            tag.setField(FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
            mp4File.commit();
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            assertEquals("", tag.getFirst(Mp4FieldKey.GENRE));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testMp4GenresUsingMp4Interface()
    {
        try
        {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = AbstractTestCase.copyAudioToTmp("01.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag) mp4File.getTag();
            //Set valid value
            tag.setField(Mp4FieldKey.GENRE, "Rock");
            //mapped correctly otherwise would not be value for Mp4Fieldkey
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            //Doesnt remove CUSTOM field as we are using mp4 interface
            assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
            mp4File.commit();
            assertEquals("Rock", tag.getFirst(FieldKey.GENRE));
            assertEquals("Rock", tag.getFirst(Mp4FieldKey.GENRE));
            //Doesnt remove CUSTOM field as we are using mp4 interface
            assertEquals("Genre", tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void testMp4InvalidGenresUsingMp4Interface()
    {
        try
        {
            System.out.println(TagOptionSingleton.getInstance().isWriteMp4GenresAsText());
            AudioFile mp4File = null;
            Mp4Tag tag = null;
            File testFile = AbstractTestCase.copyAudioToTmp("01.m4a");
            mp4File = AudioFileIO.read(testFile);
            tag = (Mp4Tag) mp4File.getTag();
            //Set valid value
            tag.setField(Mp4FieldKey.GENRE, "Rocky");
        }
        catch (Exception ex)
        {
            assertTrue(ex instanceof IllegalArgumentException);
            assertTrue(ex.getMessage().equals(ErrorMessage.NOT_STANDARD_MP$_GENRE.getMsg()));
        }
    }
}
