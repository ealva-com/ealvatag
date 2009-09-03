package org.jaudiotagger.tag.vorbiscomment;

import org.jaudiotagger.AbstractTestCase;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.ogg.OggFileReader;
import org.jaudiotagger.audio.ogg.util.OggPageHeader;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagFieldKey;
import org.jaudiotagger.tag.vorbiscomment.util.Base64Coder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Vorbis Write tsgs
 */
public class VorbisWriteTagTest extends AbstractTestCase
{
    /**
     * Can summarize file
     */
    public void testSummarizeFile()
    {
        Exception exceptionCaught = null;
        try
        {
//Can summarize file
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testSummarizeFile.ogg"));
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            oggFileReader.summarizeOggPageHeaders(testFile);
            raf.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Testing of writing various fields
     */
    public void testWriteTagToFile()
    {
        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWriteTagTest.ogg"));
            AudioFile f = AudioFileIO.read(testFile);

            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();
            assertEquals("jaudiotagger", tag.getVendor());

            //These have methods coz common over all formats
            tag.setArtist("AUTHOR");
            tag.setAlbum("ALBUM");
            tag.setTitle("title");
            tag.setComment("comments");
            tag.setYear("1971");
            tag.setTrack("2");
            tag.setGenre("Genre");
            //Common keys
            tag.set(tag.createTagField(TagFieldKey.DISC_NO, "4"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER, "composer\u00A9"));
            tag.set(tag.createTagField(TagFieldKey.ARTIST_SORT, "Sortartist\u01ff"));
            tag.set(tag.createTagField(TagFieldKey.LYRICS, "lyrics"));
            tag.set(tag.createTagField(TagFieldKey.BPM, "200"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST, "Albumartist"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_ARTIST_SORT, "Sortalbumartist"));
            tag.set(tag.createTagField(TagFieldKey.ALBUM_SORT, "Sortalbum"));
            tag.set(tag.createTagField(TagFieldKey.GROUPING, "GROUping"));
            tag.set(tag.createTagField(TagFieldKey.COMPOSER_SORT, "Sortcomposer"));
            tag.set(tag.createTagField(TagFieldKey.TITLE_SORT, "sorttitle"));
            tag.set(tag.createTagField(TagFieldKey.IS_COMPILATION, "1"));
            tag.set(tag.createTagField(TagFieldKey.MUSICIP_ID, "66027994-edcf-9d89-bec8-0d30077d888c"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_TRACK_ID, "e785f700-c1aa-4943-bcee-87dd316a2c31"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_ARTISTID, "989a13f6-b58c-4559-b09e-76ae0adb94ed"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID, "989a13f6-b58c-4559-b09e-76ae0adb94ed"));
            tag.set(tag.createTagField(TagFieldKey.MUSICBRAINZ_RELEASEID, "19c6f0f6-3d6d-4b02-88c7-ffb559d52be6"));
            tag.set(tag.createTagField(TagFieldKey.URL_LYRICS_SITE,"http://www.lyrics.fly.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_DISCOGS_ARTIST_SITE,"http://www.discogs1.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_DISCOGS_RELEASE_SITE,"http://www.discogs2.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_OFFICIAL_ARTIST_SITE,"http://www.discogs3.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_OFFICIAL_RELEASE_SITE,"http://www.discogs4.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_WIKIPEDIA_ARTIST_SITE,"http://www.discogs5.com"));
            tag.set(tag.createTagField(TagFieldKey.URL_WIKIPEDIA_RELEASE_SITE,"http://www.discogs6.com"));

            //Vorbis Only keys
            tag.set(((VorbisCommentTag) tag).createTagField(VorbisCommentFieldKey.DESCRIPTION, "description"));

            //tag.set(tag.createTagField(TagFieldKey.ENCODER,"encoder"));
            tag.setVendor("encoder");
            assertEquals("encoder", tag.getVendor());

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.png"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.set(((VorbisCommentTag) tag).createTagField(VorbisCommentFieldKey.COVERART, base64image));
            tag.set(((VorbisCommentTag) tag).createTagField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));

            //key not known to jaudiotagger
            tag.set(((VorbisCommentTag) tag).createTagField("VOLINIST", "Sarah Curtis"));

            f.commit();

            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();
            assertTrue(tag instanceof VorbisCommentTag);
            assertEquals("AUTHOR", tag.getFirstArtist());
            assertEquals("ALBUM", tag.getFirstAlbum());
            assertEquals("title", tag.getFirstTitle());
            assertEquals("comments", tag.getFirstComment());
            assertEquals("1971", tag.getFirstYear());
            assertEquals("2", tag.getFirstTrack());
            assertEquals("Genre", tag.getFirstGenre());
            assertEquals("AUTHOR", tag.getFirst(TagFieldKey.ARTIST));
            assertEquals("ALBUM", tag.getFirst(TagFieldKey.ALBUM));
            assertEquals("title", tag.getFirst(TagFieldKey.TITLE));
            assertEquals("comments", tag.getFirst(TagFieldKey.COMMENT));
            assertEquals("1971", tag.getFirst(TagFieldKey.YEAR));
            assertEquals("2", tag.getFirst(TagFieldKey.TRACK));
            assertEquals("4", tag.getFirst(TagFieldKey.DISC_NO));
            assertEquals("composer\u00A9", tag.getFirst(TagFieldKey.COMPOSER));
            assertEquals("Sortartist\u01ff", tag.getFirst(TagFieldKey.ARTIST_SORT));
            assertEquals("lyrics", tag.getFirst(TagFieldKey.LYRICS));
            assertEquals("200", tag.getFirst(TagFieldKey.BPM));
            assertEquals("Albumartist", tag.getFirst(TagFieldKey.ALBUM_ARTIST));
            assertEquals("Sortalbumartist", tag.getFirst(TagFieldKey.ALBUM_ARTIST_SORT));
            assertEquals("Sortalbum", tag.getFirst(TagFieldKey.ALBUM_SORT));
            assertEquals("GROUping", tag.getFirst(TagFieldKey.GROUPING));
            assertEquals("Sortcomposer", tag.getFirst(TagFieldKey.COMPOSER_SORT));
            assertEquals("sorttitle", tag.getFirst(TagFieldKey.TITLE_SORT));
            assertEquals("1", tag.getFirst(TagFieldKey.IS_COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", tag.getFirst(TagFieldKey.MUSICIP_ID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", tag.getFirst(TagFieldKey.MUSICBRAINZ_TRACK_ID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(TagFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", tag.getFirst(TagFieldKey.MUSICBRAINZ_RELEASEID));
            //Cast to format specific tag
            VorbisCommentTag vorbisTag = (VorbisCommentTag) tag;
            //Lookup by vorbis comment key
            assertEquals("AUTHOR", vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST));
            assertEquals("ALBUM", vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM));
            assertEquals("title", vorbisTag.getFirst(VorbisCommentFieldKey.TITLE));
            assertEquals("comments", vorbisTag.getFirst(VorbisCommentFieldKey.COMMENT));
            assertEquals("1971", vorbisTag.getFirst(VorbisCommentFieldKey.DATE));
            assertEquals("2", vorbisTag.getFirst(VorbisCommentFieldKey.TRACKNUMBER));
            assertEquals("4", vorbisTag.getFirst(VorbisCommentFieldKey.DISCNUMBER));
            assertEquals("composer\u00A9", vorbisTag.getFirst(VorbisCommentFieldKey.COMPOSER));
            assertEquals("Sortartist\u01ff", vorbisTag.getFirst(VorbisCommentFieldKey.ARTISTSORT));
            assertEquals("lyrics", vorbisTag.getFirst(VorbisCommentFieldKey.LYRICS));
            assertEquals("200", vorbisTag.getFirst(VorbisCommentFieldKey.BPM));
            assertEquals("Albumartist", vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST));
            assertEquals("Sortalbumartist", vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTISTSORT));
            assertEquals("Sortalbum", vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMSORT));
            assertEquals("GROUping", vorbisTag.getFirst(VorbisCommentFieldKey.GROUPING));
            assertEquals("Sortcomposer", vorbisTag.getFirst(VorbisCommentFieldKey.COMPOSERSORT));
            assertEquals("sorttitle", vorbisTag.getFirst(VorbisCommentFieldKey.TITLESORT));
            assertEquals("1", vorbisTag.getFirst(VorbisCommentFieldKey.COMPILATION));
            assertEquals("66027994-edcf-9d89-bec8-0d30077d888c", vorbisTag.getFirst(VorbisCommentFieldKey.MUSICIP_PUID));
            assertEquals("e785f700-c1aa-4943-bcee-87dd316a2c31", vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_TRACKID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ARTISTID));
            assertEquals("989a13f6-b58c-4559-b09e-76ae0adb94ed", vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMARTISTID));
            assertEquals("19c6f0f6-3d6d-4b02-88c7-ffb559d52be6", vorbisTag.getFirst(VorbisCommentFieldKey.MUSICBRAINZ_ALBUMID));
            assertEquals("http://www.lyrics.fly.com",tag.getFirst(TagFieldKey.URL_LYRICS_SITE));
            assertEquals("http://www.discogs1.com",tag.getFirst(TagFieldKey.URL_DISCOGS_ARTIST_SITE));
            assertEquals("http://www.discogs2.com",tag.getFirst(TagFieldKey.URL_DISCOGS_RELEASE_SITE));
            assertEquals("http://www.discogs3.com",tag.getFirst(TagFieldKey.URL_OFFICIAL_ARTIST_SITE));
            assertEquals("http://www.discogs4.com",tag.getFirst(TagFieldKey.URL_OFFICIAL_RELEASE_SITE));
            assertEquals("http://www.discogs5.com",tag.getFirst(TagFieldKey.URL_WIKIPEDIA_ARTIST_SITE));
            assertEquals("http://www.discogs6.com",tag.getFirst(TagFieldKey.URL_WIKIPEDIA_RELEASE_SITE));

            assertEquals("Sarah Curtis", vorbisTag.getFirst("VOLINIST"));

            assertEquals("encoder", vorbisTag.getVendor());

            //List methods
            List<TagField> list = tag.get(TagFieldKey.ARTIST);
            assertEquals(1, list.size());
            for (TagField field : list)
            {
                assertEquals("AUTHOR", field.toString());
            }

            //Vorbis keys that have no mapping to generic key
            assertEquals("description", vorbisTag.getFirst(VorbisCommentFieldKey.DESCRIPTION));

            //VorbisImage base64 image, and reconstruct
            assertEquals(base64image, vorbisTag.getFirst(VorbisCommentFieldKey.COVERART));
            assertEquals("image/png", vorbisTag.getFirst(VorbisCommentFieldKey.COVERARTMIME));
            BufferedImage bi = ImageIO.read(ImageIO
                    .createImageInputStream(new ByteArrayInputStream(Base64Coder.
                    decode(vorbisTag.getFirst(VorbisCommentFieldKey.COVERART).toCharArray()))));
            assertNotNull(bi);


            OggFileReader ofr = new OggFileReader();
            OggPageHeader oph = ofr.readOggPageHeader(new RandomAccessFile(testFile, "r"), 0);
            assertEquals(30, oph.getPageLength());
            assertEquals(0, oph.getPageSequence());
            assertEquals(559748870, oph.getSerialNumber());
            assertEquals(-2111591604, oph.getCheckSum());
            assertEquals(2, oph.getHeaderType());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments too large to fit on single page anymore
     */
    public void testWriteToFileMuchLarger()
    {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWriteTagTestRequiresTwoPages.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.bmp"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERART, base64image));
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments too large to fit on single page anymore, and also setup header gets split
     */
    public void testWriteToFileMuchLargerSetupHeaderSplit()
    {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWriteTagTestRequiresTwoPagesHeaderSplit.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new pretend image to force split of setup header
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 128000; i++)
            {
                sb.append("a");
            }
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERART, sb.toString()));
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 3));

            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit so
     * comment data is changed but size of comment header is same length
     */
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeader()
    {
        File orig = new File("testdata", "test2.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.ogg", new File("testWriteTagWithExtraPacketsHeaderSameSize.ogg"));

            OggFileReader oggFileReader = new OggFileReader();
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            int packetsInSecondPageCount = pageHeader.getPacketList().size();
            pageHeader = null;
            raf.close();

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //These have methods coz common over all formats
            tag.setArtist("AUTHOR");

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals("AUTHOR", tag.getFirst(TagFieldKey.ARTIST));

            //Check 2nd page has same number of packets, this is only the case for this specific test, so check
            //in test not code itself.
            raf = new RandomAccessFile(testFile, "r");
            pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            raf.close();
            assertEquals(packetsInSecondPageCount, pageHeader.getPacketList().size());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is larger, but the comment, header and extra packets can still all fit on page 2
     */
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderLarger()
    {
        File orig = new File("testdata", "test2.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.ogg", new File("testWriteTagWithExtraPacketsHeaderLargerSize.ogg"));

            OggFileReader oggFileReader = new OggFileReader();
            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggPageHeader pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            int packetsInSecondPageCount = pageHeader.getPacketList().size();
            pageHeader = null;
            raf.close();

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //These have methods coz common over all formats
            tag.setArtist("ARTISTIC");

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals("ARTISTIC", tag.getFirst(TagFieldKey.ARTIST));

            //Check 2nd page has same number of packets, this is only the case for this specific test, so check
            //in test not code itself.
            raf = new RandomAccessFile(testFile, "r");
            pageHeader = oggFileReader.readOggPageHeader(raf, 1);
            raf.close();
            assertEquals(packetsInSecondPageCount, pageHeader.getPacketList().size());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is much larger, so that comment, header and extra packets can no longer fit on page 2
     */
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderMuchLarger()
    {
        File orig = new File("testdata", "test2.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testWriteTagWithExtraPacketsHeaderMuchLargerSize.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new image, requires two fields in oggVorbis with data in  base64 encoded form
            RandomAccessFile imageFile = new RandomAccessFile(new File("testdata", "coverart.bmp"), "r");
            byte[] imagedata = new byte[(int) imageFile.length()];
            imageFile.read(imagedata);
            char[] testdata = Base64Coder.encode(imagedata);
            String base64image = new String(testdata);
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERART, base64image));
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(1, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println("Page 2" + oggFileReader.readOggPageHeader(raf, 1));
            //System.out.println("Page 3"+oggFileReader.readOggPageHeader(raf,2));
            //oggFileReader.readOggPageHeader(raf,4);
            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Issue 197, test writing to file when audio packet come straight after setup packet on same oggPage, edit enough
     * so that comment is much larger, so that comment, header and extra packets can no longer fit on page 2 AND
     * setup header is also split over two
     */
    public void testWriteToFileWithExtraPacketsOnSamePageAsSetupHeaderMuchLargerAndSplit()
    {
        File orig = new File("testdata", "test2.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test2.ogg", new File("testWriteTagWithExtraPacketsHeaderMuchLargerSizeAndSplit.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Add new pretend image to force split of setup header
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 128000; i++)
            {
                sb.append("a");
            }
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERART, sb.toString()));
            tag.set(tag.createTagField(VorbisCommentFieldKey.COVERARTMIME, "image/png"));

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 3));

            raf.close();

            //tag = (VorbisCommentTag)f.getTag();

            //Check changes
            //assertEquals(1,tag.get(VorbisCommentFieldKey.COVERART).size());
            //assertEquals(1,tag.get(VorbisCommentFieldKey.COVERARTMIME).size());


        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments was too large for one page but not anymore
     */
    public void testWriteToFileNoLongerRequiresTwoPages()
    {
        File orig = new File("testdata", "test3.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test3.ogg", new File("testWriteTagTestNoLongerRequiresTwoPages.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteTagField(VorbisCommentFieldKey.COVERART);
            tag.deleteTagField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Test writing to file, comments was too large for one page and setup header split but not anymore
     */
    public void testWriteToFileNoLongerRequiresTwoPagesNorSplit()
    {
        File orig = new File("testdata", "test5.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test5.ogg", new File("testWriteTagTestNoLongerRequiresTwoPagesNorSplit.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteTagField(VorbisCommentFieldKey.COVERART);
            tag.deleteTagField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Test writing to file, comments was too large for one page but not anymore
     */
    public void testWriteToFileWriteToFileWithExtraPacketsNoLongerRequiresTwoPages()
    {
        File orig = new File("testdata", "test4.ogg");
        if (!orig.isFile())
        {
            return;
        }

        Exception exceptionCaught = null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test4.ogg", new File("testWriteTagTestWithPacketsNoLongerRequiresTwoPages.ogg"));

            AudioFile f = AudioFileIO.read(testFile);
            assertTrue(f.getTag() instanceof VorbisCommentTag);
            VorbisCommentTag tag = (VorbisCommentTag) f.getTag();

            //Delete Large Image
            tag.deleteTagField(VorbisCommentFieldKey.ARTIST);
            tag.deleteTagField(VorbisCommentFieldKey.COVERART);
            tag.deleteTagField(VorbisCommentFieldKey.COVERARTMIME);

            //Save
            f.commit();

            //Reread
            f = AudioFileIO.read(testFile);
            tag = (VorbisCommentTag) f.getTag();

            //Check changes
            assertEquals(0, tag.get(VorbisCommentFieldKey.ARTIST).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERART).size());
            assertEquals(0, tag.get(VorbisCommentFieldKey.COVERARTMIME).size());

            RandomAccessFile raf = new RandomAccessFile(testFile, "r");
            OggFileReader oggFileReader = new OggFileReader();
            System.out.println(oggFileReader.readOggPageHeader(raf, 0));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 1));
            raf.seek(0);
            System.out.println(oggFileReader.readOggPageHeader(raf, 2));
            raf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testDeleteTag() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testDelete.ogg"));
        AudioFile f = AudioFileIO.read(testFile);
        f.setTag(VorbisCommentTag.createNewTag());
        f.commit();

        f = AudioFileIO.read(testFile);
        assertTrue(f.getTag().isEmpty());
        assertEquals("jaudiotagger", ((VorbisCommentTag) f.getTag()).getVendor());
    }

    public void testDeleteTag2() throws Exception
    {
        File testFile = AbstractTestCase.copyAudioToTmp("test.ogg", new File("testDelete2.ogg"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioFileIO.delete(f);

        f = AudioFileIO.read(testFile);
        assertTrue(f.getTag().isEmpty());
        assertEquals("jaudiotagger", ((VorbisCommentTag) f.getTag()).getVendor());
    }
}
