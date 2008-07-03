package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.tag.mp4.field.Mp4GenreField;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.Tag;

import java.io.FileOutputStream;
import java.io.File;

/**
 * Test Mp4 genres can be invalid
 */
public class Issue224Test extends AbstractTestCase
{
    /** Reading a file contains genre field but set to invalid value 149, because Mp4genreField always
     *  store the value the genre is mapped to we return null. This is correct behaviour.
     */
    public void testReadInvalidGenre()
    {
        String genre=null;

        File orig = new File("testdata", "test30.m4a");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test30.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            genre=tag.getFirstGenre();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught=e;
        }
        assertNull(exceptionCaught);
        assertNull(genre);

    }
}