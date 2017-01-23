package ealvatag.tag.wav;

import ealvatag.AbstractTestCase;
import ealvatag.FilePermissionsTest;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
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

import java.io.File;

/**
 * User: paul
 * Date: 07-Dec-2007
 */
public class WavMetadataTest extends AbstractTestCase {


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testReadFileWithListInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
            assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            assertEquals("conductor\0", tag.getFirst(FieldKey.CONDUCTOR));
            assertEquals("lyricist\0", tag.getFirst(FieldKey.LYRICIST));
            assertEquals("composer\0", tag.getFirst(FieldKey.COMPOSER));
            assertEquals("albumArtist\0", tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals("100\0", tag.getFirst(FieldKey.RATING));
            assertEquals("encoder\0", tag.getFirst(FieldKey.ENCODER));
            assertEquals("ISRC\0", tag.getFirst(FieldKey.ISRC));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveBoth.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926552L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(280L, tag.getInfoTag().getSizeOfTag());
            assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926552L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadataSaveBothInfoThenId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataInfoId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getTag().or(NullTag.INSTANCE));


            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ALBUM_ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926700L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926700L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926718L, testFile.length());


            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "smallervalue");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(tag.getInfoTag());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testModifyFileWithMoreMetadataSaveBothId3ThenInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataId3Info.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ALBUM_ARTIST));
            assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926282L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926718L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            assertEquals(926718L, testFile.length());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "smallervalue");
            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(tag.getInfoTag());
            assertEquals(10L, tag.getSizeOfID3TagOnly()); //Because have SAVE BOTH option but nothign added to ID3 save empty ID3tag
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(18L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926282L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926718L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            assertEquals(926648L, testFile.length());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Delete file with Info metadata
     */
    public void testDeleteFileInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertTrue(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Delete file with Id3 metadata
     */
    public void testDeleteFileId3Metadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126DeleteId3Metadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            assertEquals(25L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(33L, tag.getSizeOfID3TagIncludingChunkHeader());

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testReadFileWithID3AndListInfoMetadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            //Ease of use methods for common fields
            assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
            assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));

            assertTrue(tag.isInfoTag());
            WavInfoTag wit = tag.getInfoTag();
            assertEquals("id3artistName\0", wit.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName\0", wit.getFirst(FieldKey.ALBUM));
            assertEquals("test123\0", wit.getFirst(FieldKey.TITLE));
            assertEquals("comment\0", wit.getFirst(FieldKey.COMMENT));
            assertEquals("2002\0", wit.getFirst(FieldKey.YEAR));
            assertEquals("1\0", wit.getFirst(FieldKey.TRACK));
            assertEquals("rock\0", wit.getFirst(FieldKey.GENRE));

            assertTrue(tag.isID3Tag());
            AbstractID3v2Tag id3tag = tag.getID3Tag();
            assertTrue(id3tag instanceof ID3v23Tag);
            assertEquals("id3artistName", id3tag.getFirst(FieldKey.ARTIST));
            assertEquals("id3albumName", id3tag.getFirst(FieldKey.ALBUM));
            assertEquals("test123", id3tag.getFirst(FieldKey.TITLE));
            assertEquals("comment", id3tag.getFirst(FieldKey.COMMENT));
            assertEquals("2002", id3tag.getFirst(FieldKey.YEAR));
            assertEquals("1", id3tag.getFirst(FieldKey.TRACK));
            assertEquals("rock", id3tag.getFirst(FieldKey.GENRE));


            assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(146L, tag.getInfoTag().getSizeOfTag());
            assertEquals(235L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }


    /**
     * Delete file with Info and ID3 metadata
     */
    public void testDeleteFileInfoAndID3Metadata() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav", new File("test125DeleteMetadata.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertTrue(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            //Ease of use methods for common fields
            assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));

            f.deleteFileTag();


            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertFalse(tag.isExistingId3Tag());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testWavReadOptionsHasId3AndInfo() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                assertEquals("1", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002", tag.getFirst(FieldKey.YEAR));
                assertEquals("1", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                File testFile = AbstractTestCase.copyAudioToTmp("test125.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("id3artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("id3albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testWavReadOptionsHasInfoOnly() {

        Exception exceptionCaught = null;
        try {
            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("", tag.getFirst(FieldKey.ARTIST));
                assertEquals("", tag.getFirst(FieldKey.ALBUM));
                assertEquals("", tag.getFirst(FieldKey.TITLE));
                assertEquals("", tag.getFirst(FieldKey.COMMENT));
                assertEquals("", tag.getFirst(FieldKey.YEAR));
                assertEquals("", tag.getFirst(FieldKey.TRACK));
                assertEquals("", tag.getFirst(FieldKey.GENRE));
            }


            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }

            {
                TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_UNLESS_ONLY_ID3);
                TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
                File testFile = AbstractTestCase.copyAudioToTmp("test123.wav");
                AudioFile f = AudioFileIO.read(testFile);
                Tag tag = f.getTag().or(NullTag.INSTANCE);
                //Ease of use methods for common fields
                assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));
                assertEquals("albumName\0", tag.getFirst(FieldKey.ALBUM));
                assertEquals("test123\0", tag.getFirst(FieldKey.TITLE));
                assertEquals("comment\0", tag.getFirst(FieldKey.COMMENT));
                assertEquals("2002\0", tag.getFirst(FieldKey.YEAR));
                assertEquals("1\0", tag.getFirst(FieldKey.TRACK));
                assertEquals("rock\0", tag.getFirst(FieldKey.GENRE));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileWithMoreMetadataSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMoreMetadataSaveActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            tag.setField(FieldKey.ALBUM_ARTIST, "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ARTIST));
            assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                         tag.getFirst(FieldKey.ALBUM_ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926700L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(428L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveExistingActiveInfo() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_INFO_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());
            //Ease of use methods for common fields
            assertEquals("artistName\0", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveExistingActiveId3Info() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_EXISTING_AND_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveExistingActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(26L, tag.getSizeOfID3TagOnly());
            assertEquals(926560L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId3() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test123.wav", new File("test123ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertTrue(tag.isExistingInfoTag());

            assertEquals(926264L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926560L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(288L, tag.getInfoTag().getSizeOfTag());
            assertEquals(0L, tag.getSizeOfID3TagOnly());
            assertEquals(0L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(0L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            assertEquals(26L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any additional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * Read file with metadata added by MediaMonkey
     */
    public void testModifyFileMetadataSaveActiveId32() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav", new File("test126ModifyMetadataSaveActiveId3.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());


            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertFalse(tag.isExistingInfoTag());

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            assertEquals(25L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(33L, tag.getSizeOfID3TagIncludingChunkHeader());

            //Ease of use methods for common fields
            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            //Modify Value
            tag.setField(FieldKey.ARTIST, "fred");
            f.save();

            //Read modified metadata now in file
            f = AudioFileIO.read(testFile);
            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag.getInfoTag());

            assertEquals("fred", tag.getFirst(FieldKey.ARTIST));

            assertTrue(tag.isInfoTag());
            assertTrue(tag.isID3Tag());
            assertFalse(tag.isExistingInfoTag());
            assertTrue(tag.isExistingId3Tag());

            assertNull(tag.getInfoTag().getStartLocationInFile());
            assertNull(tag.getInfoTag().getEndLocationInFile());
            assertEquals(0L, tag.getInfoTag().getSizeOfTag());
            assertEquals(26L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(34L, tag.getSizeOfID3TagIncludingChunkHeader());

            //So tag field now shorter so needs to truncate any addtional data
            tag.setField(FieldKey.ARTIST, "fr");
            f.save();

            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag().or(NullTag.INSTANCE));
        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteNumberedOddSaveActive() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_ACTIVE);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav", new File("test125ID3OddNumberedActive.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(146L, tag.getInfoTag().getSizeOfTag());
            assertEquals(235L, tag.getSizeOfID3TagOnly());
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());

            tag.setField(FieldKey.ARTIST, "a nice long artist names");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            tag.setField(FieldKey.ARTIST, "a nice long artist s");
            assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testWriteNumberedOddSaveBoth() {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test125.wav", new File("test125ID3OddNumberedBoth.wav"));
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("529", f.getAudioHeader().getBitRate());
            assertEquals("1", f.getAudioHeader().getChannels());
            assertEquals("22050", f.getAudioHeader().getSampleRate());

            assertTrue(f.getTag().or(NullTag.INSTANCE) instanceof WavTag);
            WavTag tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            assertEquals("id3artistName", tag.getFirst(FieldKey.ARTIST));
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(235L, tag.getSizeOfID3TagOnly());
            assertEquals(243L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926662L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(146L, tag.getInfoTag().getSizeOfTag());


            tag.setField(FieldKey.ARTIST, "a nice long artist names");
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(236L, tag.getSizeOfID3TagOnly());
            assertEquals(244L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926674L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(158L, tag.getInfoTag().getSizeOfTag());

            tag.setField(FieldKey.ARTIST, "a nice long artist s");
            assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));
            f.save();
            f = AudioFileIO.read(testFile);
            tag = (WavTag)f.getTag().or(NullTag.INSTANCE);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals(926264L, tag.getStartLocationInFileOfId3Chunk());
            assertEquals(236L, tag.getSizeOfID3TagOnly());
            assertEquals(244L, tag.getSizeOfID3TagIncludingChunkHeader());
            assertEquals(926508L, tag.getInfoTag().getStartLocationInFile().longValue());
            assertEquals(926670L, tag.getInfoTag().getEndLocationInFile().longValue());
            assertEquals(154L, tag.getInfoTag().getSizeOfTag());

            assertEquals("a nice long artist s", tag.getFirst(FieldKey.ARTIST));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * This file has three bytes of padding data at end of file
     */
    public void testReadFileWithPaddingAtEndOfListInfoMetadata() {
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
            File testFile = AbstractTestCase.copyAudioToTmp("test146.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("Bo Junior", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("Coffee Pot, Part 2", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("Hipshaker", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ALBUM, "Hippy");
            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));
            assertEquals("Bo Junior", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST));
            assertEquals("Coffee Pot, Part 2", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TITLE));
            assertEquals("Hippy", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testNaimRip() {
        File orig = new File("testdata", "test149.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test149.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testCreationOfDefaultTag() {
        File orig = new File("testdata", "test126.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test126.wav");
            AudioFile f = AudioFileIO.read(testFile);
            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));

            f.setNewDefaultTag();

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testRip2() {
        File orig = new File("testdata", "test500.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test500.wav");
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
        assertNull(exceptionCaught);
    }

    public void testRip3() {
        File orig = new File("testdata", "test501.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test501.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    public void testRip4() {
        File orig = new File("testdata", "test502.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test502.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assertNull(exceptionCaught);
    }

    /**
     * When chunk header has negative size we know something has gone wrong and should throw exception accordingly
     */
    public void testWavWithCorruptDataAfterDataChunkHeaderSize() {
        File orig = new File("testdata", "test503.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test503.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        assert (exceptionCaught instanceof CannotReadException);
    }

    public void testCleanAndThenWriteWavWithCorruptDataChunkHeaderSize() {
        File orig = new File("testdata", "test504.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        File testFile = AbstractTestCase.copyAudioToTmp("test504.wav", new File("test504clean.wav"));
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
        assertNull(exceptionCaught2);
    }

    public void testWavRead() {
        File orig = new File("testdata", "test505.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.ID3_THEN_INFO);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test505.wav");
            AudioFile f = AudioFileIO.read(testFile);

            System.out.println(f.getAudioHeader());
            System.out.println(f.getTag().or(NullTag.INSTANCE));


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
    }

    public void testWavReadNew() {
        File orig = new File("testdata", "test506.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test506.wav");
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
        assertTrue(exceptionCaught instanceof CannotReadException);
    }

    public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test123.wav");
    }

    public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test123.wav");
    }

    public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test123.wav");
    }

    public void testReadJacobPavluk() {
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
            File testFile = AbstractTestCase.copyAudioToTmp("GreenLight.wav");
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
    public void testTrackNumbersSyncedWhenNullTerminated() throws Exception {
        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO_AND_SYNC);
        File testFile = AbstractTestCase.copyAudioToTmp("bug153.wav", new File("bug153.wav"));
        AudioFile f = AudioFileIO.read(testFile);
        assertEquals("7", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.TRACK));
    }

    public void testWavRead2() {
        File orig = new File("testdata", "test160.wav");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_UNLESS_ONLY_INFO);
        TagOptionSingleton.getInstance().setWavSaveOrder(WavSaveOrder.INFO_THEN_ID3);

        Exception exceptionCaught = null;
        try {
            File testFile = AbstractTestCase.copyAudioToTmp("test160.wav");
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
        assertTrue(exceptionCaught instanceof CannotReadException);
    }
}
