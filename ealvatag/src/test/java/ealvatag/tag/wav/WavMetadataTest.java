package ealvatag.tag.wav;

import ealvatag.FilePermissionsTest;
import ealvatag.TestUtil;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.Utils;
import ealvatag.audio.exceptions.CannotReadException;
import ealvatag.audio.wav.WavCleaner;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.audio.wav.WavSaveOrder;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.AbstractID3v2Tag;
import ealvatag.tag.id3.ID3v23Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataTest {

    @Before
    public void setup() {
        TagOptionSingleton.getInstance().setToDefault();
    }

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testReadFileWithListInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            Assert.assertEquals("conductor\0", tag.getFirst(FieldKey.CONDUCTOR));
            Assert.assertEquals("lyricist\0", tag.getFirst(FieldKey.LYRICIST));
            Assert.assertEquals("composer\0", tag.getFirst(FieldKey.COMPOSER));
            Assert.assertEquals("albumArtist\0", tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals("100\0", tag.getFirst(FieldKey.RATING));
            Assert.assertEquals("encoder\0", tag.getFirst(FieldKey.ENCODER));
            Assert.assertEquals("ISRC\0", tag.getFirst(FieldKey.ISRC));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveBoth.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926552L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(280L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            Assert.assertEquals(926552L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileWithMoreMetadataSaveBothInfoThenId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataInfoId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getTag().or(NullTag.INSTANCE));


            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ALBUM_ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926700L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            Assert.assertEquals(926700L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926718L, testFile.length());


            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "smallervalue");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(tag.getInfoTag());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testModifyFileWithMoreMetadataSaveBothId3ThenInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataId3Info.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ALBUM_ARTIST));
            Assert.assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926282L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926718L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(926718L, testFile.length());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "smallervalue");
            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926282L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926718L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(926648L, testFile.length());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Delete file with Info metadata
     */
    @Test public void testDeleteFileInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertTrue(tag.isExistingInfoTag());
            Assert.assertFalse(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertFalse(tag.isExistingId3Tag());

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Delete file with Id3 metadata
     */
    @Test public void testDeleteFileId3Metadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126DeleteId3Metadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(25L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(33L, tag.getSizeOfID3TagIncludingChunkHeader());

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertFalse(tag.isExistingId3Tag());

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testReadFileWithID3AndListInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test125.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            //Ease of use methods for common fields
            Assert.assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));

            Assert.assertTrue(tag.isInfoTag());
            WavInfoTag wit = tag.getInfoTag();
            Assert.assertEquals("id3artistName\0", wit.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("id3albumName\0", wit.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test123\0", wit.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comment\0", wit.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("2002\0", wit.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1\0", wit.getFirst(FieldKey.TRACK));
            Assert.assertEquals("rock\0", wit.getFirst(FieldKey.GENRE));

            Assert.assertTrue(tag.isID3Tag());
            AbstractID3v2Tag id3tag = tag.getID3Tag();
            Assert.assertTrue(id3tag instanceof ID3v23Tag);
            Assert.assertEquals("id3artistName", id3tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("id3albumName", id3tag.getFirst(FieldKey.ALBUM));
            Assert.assertEquals("test123", id3tag.getFirst(FieldKey.TITLE));
            Assert.assertEquals("comment", id3tag.getFirst(FieldKey.COMMENT));
            Assert.assertEquals("2002", id3tag.getFirst(FieldKey.YEAR));
            Assert.assertEquals("1", id3tag.getFirst(FieldKey.TRACK));
            Assert.assertEquals("rock", id3tag.getFirst(FieldKey.GENRE));


            Assert.assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(146L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(235L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }


    /**
     * Delete file with Info and ID3 metadata
     */
    @Test public void testDeleteFileInfoAndID3Metadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test125.wav", new File("test125DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertTrue(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            Assert.assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertFalse(tag.isExistingId3Tag());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testWavReadOptionsHasId3AndInfo() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                File testFile = TestUtil.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = TestUtil.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                File testFile = TestUtil.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                File testFile = TestUtil.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testWavReadOptionsHasInfoOnly() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

                File testFile = TestUtil.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = TestUtil.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = TestUtil.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = TestUtil.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                Assert.assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                Assert.assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                Assert.assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                Assert.assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                Assert.assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                Assert.assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileWithMoreMetadataSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                                tag.getFirst(FieldKey.ALBUM_ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926700L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveExistingActiveInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            Assert.assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveExistingActiveId3Info() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926560L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveActiveId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertTrue(tag.isExistingInfoTag());

            Assert.assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(0L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any additional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    @Test public void testModifyFileMetadataSaveActiveId32() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test126.wav", new File("test126ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));


            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertFalse(tag.isExistingInfoTag());

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(25L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(33L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());

            Assert.assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            Assert.assertTrue(tag.isInfoTag());
            Assert.assertTrue(tag.isID3Tag());
            Assert.assertFalse(tag.isExistingInfoTag());
            Assert.assertTrue(tag.isExistingId3Tag());

            Assert.assertNull(tag.getInfoTag().getStartLocationInFile());
            Assert.assertNull(tag.getInfoTag().getEndLocationInFile());
            Assert.assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(26L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteNumberedOddSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test125.wav", new File("test125ID3OddNumberedActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(146L, tag.getInfoTag().getSizeOfTag());
            Assert.assertEquals(235L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());

            tag.setField(FieldKey.ARTIST, "a nice long artist names");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            tag.setField(FieldKey.ARTIST, "a nice long artist s");
            Assert.assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteNumberedOddSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test125.wav", new File("test125ID3OddNumberedBoth.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("529", Utils.formatBitRate(f.getAudioHeader(), f.getAudioHeader().getBitRate()));
            Assert.assertEquals("1", String.valueOf(f.getAudioHeader().getChannelCount()));
            Assert.assertEquals("22050", String.valueOf(f.getAudioHeader().getSampleRate()));

            Assert.assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            Assert.assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(235L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(146L, tag.getInfoTag().getSizeOfTag());


            tag.setField(FieldKey.ARTIST, "a nice long artist names");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(236L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(244L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926674L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(158L, tag.getInfoTag().getSizeOfTag());

            tag.setField(FieldKey.ARTIST, "a nice long artist s");
            Assert.assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            Assert.assertEquals(236L, tag.getSizeOfID3TagOnly());
            Assert.assertEquals(244L, tag.getSizeOfID3TagIncludingChunkHeader());
            Assert.assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            Assert.assertEquals(926670L, tag.getInfoTag().getEndLocationInFile().longValue());
            Assert.assertEquals(154L, tag.getInfoTag().getSizeOfTag());

            Assert.assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * This file has three bytes of padding data at end of file
     */
    @Test public void testReadFileWithPaddingAtEndOfListInfoMetadata() {
        File orig = new File("testdata", "test146.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test146.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("Bo Junior", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Coffee Pot, Part 2", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Hipshaker", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "Hippy");
            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            Assert.assertEquals("Bo Junior", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            Assert.assertEquals("Coffee Pot, Part 2", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            Assert.assertEquals("Hippy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testNaimRip() {
        File orig = new File("testdata", "test149.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test149.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testCreationOfDefaultTag() {
        File orig = new File("testdata", "test126.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test126.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.setNewDefaultTag();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testRip2() {
        File orig = new File("testdata", "test500.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test500.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.deleteFileTag();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testRip3() {
        File orig = new File("testdata", "test501.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test501.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testRip4() {
        File orig = new File("testdata", "test502.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test502.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    /**
     * When chunk header has negative size we know something has gone wrong and should throw exception accordingly
     */
    @Test public void testWavWithCorruptDataAfterDataChunkHeaderSize() {
        File orig = new File("testdata", "test503.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test503.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assert (exceptionCaught instanceof CannotReadException);
    }

    @Test public void testCleanAndThenWriteWavWithCorruptDataChunkHeaderSize() {
        File orig = new File("testdata", "test504.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        File testFile = TestUtil.copyAudioToTmp("test504.wav", new File("test504clean.wav"));
        try {

            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }

        Exception exceptionCaught2 = null;
        try {
            WavCleaner wc = new WavCleaner(testFile);
            wc.clean();
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            tag.setField(FieldKey.ALBUM, "fred");

            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught2 = e;
        }
        Assert.assertNull(exceptionCaught2);
    }

    @Test public void testWavRead() {
        File orig = new File("testdata", "test505.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test505.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
    }

    @Test public void testWavReadNew() {
        File orig = new File("testdata", "test506.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test506.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "artist");
            f.save();
            f = AudioFileIO.read(testFile);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
    }

    @Test public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test123.wav");
    }

    @Test public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test123.wav");
    }

    @Test public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test123.wav");
    }

    @Test public void testReadJacobPavluk() {
        File orig = new File("testdata", "GreenLight.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("GreenLight.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "artist");
            f.save();
            System.out.println("**********************SavedAudioFIle");
            f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
    }

    /**
     * https://bitbucket.org/ijabz/jaudiotagger/issues/153/when-using-wavoptions-sync-null-terminated
     * bug153.wav has two tags: an info tag with title, album and track number, and an ID3 tag with
     * artist. This test ensures the track number is copied over.
     */
    @Test public void testTrackNumbersSyncedWhenNullTerminated() throws Exception {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO_AND_SYNC);
        File testFile = TestUtil.copyAudioToTmp("bug153.wav", new File("bug153.wav"));
        AudioFile f = AudioFileIO.read(testFile);
        Assert.assertEquals("7", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK));
    }

    @Test public void testWavRead2() {
        File orig = new File("testdata", "test160.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = TestUtil.copyAudioToTmp("test160.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "artist");
            f.save();
            f = AudioFileIO.read(testFile);

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertTrue(exceptionCaught instanceof CannotReadException);
    }
}
