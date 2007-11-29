package org.jaudiotagger.audio.flac;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.id3.valuepair.PictureTypes;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;

import java.io.File;

import junit.framework.TestCase;

/**
 * basic Flac tests
 */
public class FlacHeaderTest extends TestCase
{
     public void testReadFileWithVorbisComment()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192",f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag();
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(6,infoReader.countMetaBlocks(f.getFile()));

              //Ease of use methods for common fields
            assertEquals("Artist", tag.getFirstArtist());
            assertEquals("Album", tag.getFirstAlbum());
            assertEquals("test3", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("4", tag.getFirstTrack());
            assertEquals("Crossover", tag.getFirstGenre());

            //Lookup by generickey
            assertEquals("Artist", tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("Album", tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("test3", tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(TagFieldKey.YEAR));
            assertEquals("4", tag.getFirst(TagFieldKey.TRACK));
            assertEquals("Composer", tag.getFirst(TagFieldKey.COMPOSER));

            //Images
            assertEquals(2,tag.get(TagFieldKey.COVER_ART).size());
            assertEquals(2,tag.get(TagFieldKey.COVER_ART.name()).size());
            assertEquals(2,tag.getImages().size());
           
            MetadataBlockDataPicture image = tag.getImages().get(0);
            assertEquals((int)PictureTypes.DEFAULT_ID,(int)image.getPictureType());
            assertEquals("image/png",image.getMimeType());
            assertEquals("",image.getDescription());
            assertEquals(0,image.getWidth());
            assertEquals(0,image.getHeight());
            assertEquals(0,image.getColourDepth());
            assertEquals(0,image.getIndexedColourCount());
            assertEquals(18545,image.getImageData().length);


        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Only contains vorbis comment with minimum encoder info 
     */
     public void testReadFileWithOnlyVorbisCommentEncoder()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.flac");
            AudioFile f = AudioFileIO.read(testFile);

            assertEquals("192",f.getAudioHeader().getBitRate());
            assertEquals("FLAC 16 bits",f.getAudioHeader().getEncodingType());
            assertEquals("2",f.getAudioHeader().getChannels());
            assertEquals("44100",f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag() instanceof FlacTag);
            FlacTag tag = (FlacTag)f.getTag();
            FlacInfoReader infoReader = new FlacInfoReader();
            assertEquals(4,infoReader.countMetaBlocks(f.getFile()));
            //No Images
            assertEquals(0,tag.getImages().size());
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

}
