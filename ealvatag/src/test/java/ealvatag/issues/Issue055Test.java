package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.mp3.MP3File;
import ealvatag.tag.FieldKey;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v23Frame;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.id3.ID3v24Frame;
import ealvatag.tag.id3.ID3v24Tag;
import ealvatag.tag.id3.framebody.FrameBodyTCON;
import ealvatag.tag.reference.ID3V2Version;

import java.io.File;

/**
 * Test different ways of writing genres for mp3s using generic interface
 */
public class Issue055Test extends AbstractTestCase
{
    public void testId3v23GenreWritingDefault()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("(17)",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("(17)",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testId3v23GenreWritingDefaultcaseInsensitive()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("(17)",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("(17)",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testId3v23GenreWritingTextAlways()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("Rock",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v23Tag);
            tconbody = ( FrameBodyTCON) ((ID3v23Frame)((ID3v23Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("Rock",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testId3v24GenreWritingDefault()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("17",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("17",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testId3v24GenreWritingDefaultcaseInsensitive()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(false);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("17",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("17",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testId3v24GenreWritingTextAlways()
    {
        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("01.mp3", new File("issue55.mp3"));
            MP3File mp3File = new MP3File(testFile);
            TagOptionSingleton.getInstance().setWriteMp3GenresAsText(true);
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V24);

            //Create and Save Genre passing text value
            mp3File.getTagOrCreateAndSetDefault().addField(FieldKey.GENRE, "Rock");
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            FrameBodyTCON tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("Rock",tconbody.getFirstTextValue());
            mp3File.save();
            assertEquals("Rock", mp3File.getID3v2Tag().getFirst(FieldKey.GENRE));
            assertTrue(mp3File.getID3v2Tag() instanceof ID3v24Tag);
            tconbody = ( FrameBodyTCON) ((ID3v24Frame)((ID3v24Tag)mp3File.getID3v2Tag()).getFrame("TCON")).getBody();
            assertEquals("Rock",tconbody.getFirstTextValue());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
