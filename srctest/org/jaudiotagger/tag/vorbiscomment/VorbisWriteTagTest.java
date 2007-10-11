package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;

/**
 * Vorbis Write tsgs
 */
public class VorbisWriteTagTest extends AbstractTestCase
{
    /**
     * Testing of writing various fields
     */
    public void testWriteTagToFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWriteTagToFile.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            assertTrue(f.getTag() instanceof VorbisCommentTag);
            Tag tag = f.getTag();
            //These have methods coz common over all formats
            tag.setArtist("ARTIST");
            tag.setAlbum("ALBUM");
            tag.setTitle("title");
            tag.setComment("comments");
            tag.setYear("1971");
            tag.setTrack("2");
            tag.setGenre("Genre");
            //Common keys
            tag.set(tag.createTagField(TagFieldKey.DISC_NO,"4"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER,"composer"));
            tag.set(tag.createTagField(TagFieldKey.ARTIST_SORT,"Sortartist"));
            tag.set(tag.createTagField(TagFieldKey.LYRICS,"lyrics"));
            tag.set(tag.createTagField(TagFieldKey.BPM,"200"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST,"Albumartist"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST_SORT,"Sortalbumartist"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_SORT,"Sortalbum"));
            tag.set(tag.createTagField(TagFieldKey.GROUPING,"GROUping"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER_SORT,"Sortcomposer"));
            tag.set(tag.createTagField(TagFieldKey.TITLE_SORT,"sorttitle"));
            tag.set(tag.createTagField(TagFieldKey.IS_COMPILATION,"1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICIP_ID,"66027994-edcf-9d89-bec8-0d30077d888c"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID,"e785f700-c1aa-4943-bcee-87dd316a2c31"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID,"989a13f6-b58c-4559-b09e-76ae0adb94ed"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID,"989a13f6-b58c-4559-b09e-76ae0adb94ed"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID,"19c6f0f6-3d6d-4b02-88c7-ffb559d52be6"));

            //Vorbis Only keys
            tag.set(((VorbisCommentTag)tag).createTagField(VorbisCommentFieldKey.DESCRIPTION,"description")); 

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"),"r");
            byte[] imagedata = new byte[(int)imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.set(((VorbisCommentTag)tag).createTagField(VorbisCommentFieldKey.COVERART,base64image));
            tag.set(((VorbisCommentTag)tag).createTagField(VorbisCommentFieldKey.COVERARTMIME,"image/png"));


            f.commit();

            f = AudioFileIO.read(testFile);
            tag = f.getTag();
            assertTrue(tag instanceof VorbisCommentTag);
            assertEquals("ARTIST",tag.getFirstArtist());
            assertEquals("ALBUM",tag.getFirstAlbum());
            assertEquals("title",tag.getFirstTitle());
            assertEquals("comments",tag.getFirstComment());
            assertEquals("1971",tag.getFirstYear());
            assertEquals("2",tag.getFirstTrack());
            assertEquals("Genre",tag.getFirstGenre());
            assertEquals("ARTIST",tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("ALBUM",tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title",tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments",tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971",tag.getFirst(TagFieldKey.YEAR));
            assertEquals("2",tag.getFirst(TagFieldKey.TRACK));
            assertEquals("4",tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer",tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist",tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics",tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("200",tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist",tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum",tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping",tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer",tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle",tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1",tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31",tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));

            //Cast to format specific tag
            VorbisCommentTag vorbisTag = (VorbisCommentTag)tag;
            //Lookup by vorbis comment key
            assertEquals("ARTIST",vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST));
            assertEquals("ALBUM",vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM));
            assertEquals("title",vorbisTag.getFirst(VorbisCommentFieldKey.TITLE));
            assertEquals("comments",vorbisTag.getFirst(VorbisCommentFieldKey.COMMENT));
            assertEquals("1971",vorbisTag.getFirst(VorbisCommentFieldKey.DATE));
            assertEquals("2",vorbisTag.getFirst(VorbisCommentFieldKey.TRACKNUMBER));
            assertEquals("4",vorbisTag.getFirst(VorbisCommentFieldKey.DISCNUMBER));
            assertEquals("composer",vorbisTag.getFirst(VorbisCommentFieldKey.COMPOSER));
            assertEquals("Sortartist",vorbisTag.getFirst(VorbisCommentFieldKey.ARTISTSORT));
            assertEquals("lyrics",vorbisTag.getFirst(VorbisCommentFieldKey.LYRICS));
            assertEquals("200",vorbisTag.getFirst(VorbisCommentFieldKey.BPM));
            assertEquals("Albumartist",vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST));
            assertEquals("Sortalbumartist",vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTISTSORT));
            assertEquals("Sortalbum",vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMSORT));
            assertEquals("GROUping",vorbisTag.getFirst(VorbisCommentFieldKey.GROUPING));
            assertEquals("Sortcomposer",vorbisTag.getFirst(VorbisCommentFieldKey.COMPOSERSORT));
            assertEquals("sorttitle",vorbisTag.getFirst(VorbisCommentFieldKey.TITLESORT));
            assertEquals("1",vorbisTag.getFirst(VorbisCommentFieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c",vorbisTag.getFirst(VorbisCommentFieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31",vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed",vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6",vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMID));            

            //Vorbis keys that have no mapping to generic key
            assertEquals("description",vorbisTag.getFirst(VorbisCommentFieldKey.DESCRIPTION));

            //VorbisImage base64 image, and reconstruct
            assertEquals(base64image,vorbisTag.getFirst(VorbisCommentFieldKey.COVERART));                          
            assertEquals("image/png",vorbisTag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
            BufferedImage bi = ImageIO.read(ImageIO
                        .createImageInputStream(new ByteArrayInputStream(Base64Coder.
                            decode(vorbisTag.getFirst(VorbisCommentFieldKey.COVERART).toCharArray()))));
            assertNotNull(bi);


            
            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile,"r"),0);
            assertEquals(30,oph.getPageLength());
            assertEquals(0,oph.getPageSequence());
            assertEquals(559748870,oph.getSerialNumber());
            assertEquals(-2111591604,oph.getCheckSum());
            assertEquals(2,oph.getHeaderType());

            
        }
        catch(Exception e)
        {
             e.printStackTrace();
             exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }
}
