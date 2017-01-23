package ealvatag.issues;

import ealvatag.AbstractTestCase;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.flac.FlacTag;
import ealvatag.tag.vorbiscomment.VorbisAlbumArtistReadOptions;
import ealvatag.tag.vorbiscomment.VorbisAlbumArtistSaveOptions;
import ealvatag.tag.vorbiscomment.VorbisCommentFieldKey;
import ealvatag.tag.vorbiscomment.VorbisCommentTag;

import java.io.File;

/**
 * Test
 */
public class Issue123Test extends AbstractTestCase
{

    public void testWriteJRiverAlbumArtistOgg() throws Exception
    {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testWriteJRiverAlbumArtistFlac() throws Exception
    {
        File orig = new File("testdata", "test.flac");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "Album Artist");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"jim");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "jim");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "jim");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_BOTH);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testReadJRiverAlbumArtistOgg() throws Exception
    {
        File orig = new File("testdata", "test.ogg");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.ogg");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag)af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((VorbisCommentTag) af.getTag().or(NullTag.INSTANCE)).getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }

    public void testReadJRiverAlbumArtistFlac() throws Exception
    {
        File orig = new File("testdata", "test.flac");
        if (!orig.isFile())
        {
            System.err.println("Unable to test file - not available");
            return;
        }

        Exception ex=null;
        try
        {
            File testFile = AbstractTestCase.copyAudioToTmp("test.flac");
            AudioFile af = AudioFileIO.read(testFile);
            assertNotNull(af.getTag().orNull());
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ALBUM_ARTIST);
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            assertEquals("", af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST));

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "tom");
            af.save();

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_ALBUMARTIST_THEN_JRIVER);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"fred");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistReadOptions(VorbisAlbumArtistReadOptions.READ_JRIVER_THEN_ALBUMARTIST);
            assertEquals(af.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM_ARTIST),"tom");
            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST, "fred");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag)af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tom");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "fred");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tom");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_ALBUMARTIST_AND_DELETE_JRIVER_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"freddy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "freddy");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "");

            TagOptionSingleton.getInstance().setVorbisAlbumArtistSaveOptions(VorbisAlbumArtistSaveOptions.WRITE_JRIVER_ALBUMARTIST_AND_DELETE_ALBUMARTIST);
            af.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM_ARTIST,"tommy");
            af.save();
            System.out.println(af.getTag().or(NullTag.INSTANCE));
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST), "");
            assertEquals(((FlacTag) af.getTag().or(NullTag.INSTANCE)).getVorbisCommentTag().getFirst(VorbisCommentFieldKey.ALBUMARTIST_JRIVER), "tommy");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ex=e;
        }
        assertNull(ex);
    }
}
