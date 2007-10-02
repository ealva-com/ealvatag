package org.jaudiotagger.tag.mp4;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import java.io.File;

import junit.framework.TestCase;

/**
 */
public class M4aReadTagTest  extends TestCase
{
    /**
     * Test to read all metadata from an Apple iTunes encoded m4a file
     */
    public void testReadFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.m4a");
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag();
            System.out.println(tag);

            //Ease of use methods for common fields
            assertEquals("Artist",tag.getFirstArtist());
            assertEquals("Album",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("0/1/10/0",tag.getFirstTrack());

            //Lookup by generickey
            assertEquals("Artist",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("Album",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("0/1/10/0",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("0/1/10",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("199",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));


            //Cast to format specific tag
            Mp4Tag mp4tag = (Mp4Tag)tag;

            //Lookup by mp4 key
            assertEquals("Artist",mp4tag.getFirst(Mp4FieldKey.ARTIST));
            assertEquals("Album",mp4tag.getFirst(Mp4FieldKey.ALBUM));
            assertEquals("title",mp4tag.getFirst(Mp4FieldKey.TITLE));
            assertEquals("comments",mp4tag.getFirst(Mp4FieldKey.COMMENT));
            assertEquals("1971",mp4tag.getFirst(Mp4FieldKey.DAY));
            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("0/1/10/0",mp4tag.getFirst(Mp4FieldKey.TRACK));
            assertEquals("0/1/10/0",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getContent());
            assertEquals(new Integer(0),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(0));
            assertEquals(new Integer(1),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(1));
            assertEquals(new Integer(10),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(2));
            assertEquals(new Integer(0),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.TRACK).get(0)).getNumbers().get(3));

            //Not sure why there are 4 values, only understand 2nd and third
            assertEquals("0/1/10",mp4tag.getFirst(Mp4FieldKey.DISCNUMBER));
            assertEquals("0/1/10",((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getContent());
            assertEquals(new Integer(0),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(0));
            assertEquals(new Integer(1),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(1));
            assertEquals(new Integer(10),((Mp4TagTextNumberField)mp4tag.get(Mp4FieldKey.DISCNUMBER).get(0)).getNumbers().get(2));

            assertEquals("composer",mp4tag.getFirst(Mp4FieldKey.COMPOSER));
            assertEquals("Sortartist",mp4tag.getFirst(Mp4FieldKey.ARTIST_SORT));
            assertEquals("lyrics",mp4tag.getFirst(Mp4FieldKey.LYRICS));
            assertEquals("199",mp4tag.getFirst(Mp4FieldKey.BPM));
            assertEquals("Albumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",mp4tag.getFirst(Mp4FieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",mp4tag.getFirst(Mp4FieldKey.ALBUM_SORT));
            assertEquals("GROUping",mp4tag.getFirst(Mp4FieldKey.GROUPING));
            assertEquals("Sortcomposer",mp4tag.getFirst(Mp4FieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",mp4tag.getFirst(Mp4FieldKey.TITLE_SORT));
            assertEquals("1",mp4tag.getFirst(Mp4FieldKey.COMPILATION));

            //Lookup by mp4key (no generic key mapping for these yet)
            assertEquals("iTunes v7.4.3.1, QuickTime 7.2",mp4tag.getFirst(Mp4FieldKey.ENCODER));
            assertEquals("sortshow",mp4tag.getFirst(Mp4FieldKey.SHOW_SORT));
            assertEquals("show",mp4tag.getFirst(Mp4FieldKey.SHOW));
            assertEquals("Genre",mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM));

            //TODO Artwork field, mapping two generic field to single generic field
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
