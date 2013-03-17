package org.jaudiotagger.issues;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.id3.ID3v22Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import java.io.File;

/**
 * Test Writing to new urls with common interface
 */
public class Issue242Test extends AbstractTestCase
{
    /**
     * Test New Urls ID3v24
     */
    public void testWriteNewUrlsFilev24()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1030.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File= (MP3File)af;
            //Checking not overwriting audio when have to pad to fix data                    
            long mp3AudioLength=testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();
            mp3File.setID3v2Tag(new ID3v24Tag());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(mp3AudioLength,testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());
                        
            //Check mapped okay ands empty
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());

            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://test1"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_RELEASE_SITE,"http://test2"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://test3"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://test4"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://test5"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test6"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_LYRICS_SITE,"http://test7"));
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertTrue(mp3File.getTag() instanceof ID3v24Tag);
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_LYRICS_SITE);
            mp3File.save();
            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());
        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

     /**
     * Test New Urls ID3v23
     */
    public void testWriteNewUrlsFilev23()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1031.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File= (MP3File)af;
            //Checking not overwriting audio when have to pad to fix data
            long mp3AudioLength=testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();

            mp3File.setID3v2Tag(new ID3v23Tag());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(mp3AudioLength,testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());

            //Check mapped okay ands empty
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());


            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://test1"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_RELEASE_SITE,"http://test2"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://test3"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://test4"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://test5"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test6"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_LYRICS_SITE,"http://test7"));

            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_LYRICS_SITE);
            mp3File.save();
            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());

        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

     /**
     * Test New Urls ID3v24
     */
    public void testWriteNewUrlsFilev22()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("testV1.mp3", new File("test1032.mp3"));

            //Add a v24Tag
            AudioFile af = AudioFileIO.read(testFile);
            MP3File mp3File= (MP3File)af;

            //Checking not overwriting audio when have to pad to fix data
            long mp3AudioLength=testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte();

            mp3File.setID3v2Tag(new ID3v22Tag());
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(mp3AudioLength,testFile.length() - mp3File.getMP3AudioHeader().getMp3StartByte());

            //Check mapped okay ands empty
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());


            //Now write these fields
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_RELEASE_SITE,"http://test1"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_RELEASE_SITE,"http://test2"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_DISCOGS_ARTIST_SITE,"http://test3"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://test4"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://test5"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_OFFICIAL_ARTIST_SITE,"http://test6"));
            mp3File.getTag().setField(mp3File.getTag().createField(FieldKey.URL_LYRICS_SITE,"http://test7"));
            mp3File.save();

            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            //Check mapped okay ands empty
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(1,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());

            //Delete Fields
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_DISCOGS_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_RELEASE_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_WIKIPEDIA_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_OFFICIAL_ARTIST_SITE);
            mp3File.getTag().deleteField(FieldKey.URL_LYRICS_SITE);

            mp3File.save();
            af = AudioFileIO.read(testFile);
            mp3File= (MP3File)af;
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_OFFICIAL_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_DISCOGS_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_RELEASE_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_WIKIPEDIA_ARTIST_SITE).size());
            assertEquals(0,mp3File.getTag().getFields(FieldKey.URL_LYRICS_SITE).size());
        }
        catch (Exception e)
        {
            exceptionCaught = e;
            e.printStackTrace();
        }
        assertNull(exceptionCaught);
    }

}
