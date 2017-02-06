package ealvatag.tag.aiff;


import ealvatag.TestUtil;
import ealvatag.FilePermissionsTest;
import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.audio.AudioHeader;
import ealvatag.audio.aiff.AiffAudioHeader;
import ealvatag.audio.aiff.chunk.AiffChunkType;
import ealvatag.audio.iff.ChunkHeader;
import ealvatag.audio.iff.IffHeaderChunk;
import ealvatag.audio.wav.WavOptions;
import ealvatag.audio.wav.WavSaveOptions;
import ealvatag.tag.FieldKey;
import ealvatag.tag.NullTag;
import ealvatag.tag.Tag;
import ealvatag.tag.TagOptionSingleton;
import ealvatag.tag.id3.ID3v22Tag;
import ealvatag.tag.id3.ID3v23Tag;
import ealvatag.tag.reference.ID3V2Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AiffAudioTagTest {

    @After public void tearDown() {
        TestUtil.deleteTestDataTemp();
    }


    @Test public void testReadAiff1() throws Exception {
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V23);
        File testFile = TestUtil.copyAudioToTmp("test119.aif", new File("test119ReadAiffWithoutTag.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertTrue(tag instanceof AiffTag);
        Assert.assertTrue(((AiffTag)tag).getID3Tag() instanceof ID3v23Tag);
        Assert.assertFalse(((AiffTag)tag).isExistingId3Tag());
        assertEquals(0L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(0L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(0L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
    }

    @Test public void testReadAiffChangeDefault() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test119.aif", new File("test119ReadAiffWithoutTag.aif"));
        TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertTrue(tag instanceof AiffTag);
        Assert.assertTrue(((AiffTag)tag).getID3Tag() instanceof ID3v22Tag);
        Assert.assertFalse(((AiffTag)tag).isExistingId3Tag());
        assertEquals(0L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(0L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(0L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
    }

    @Test public void testReadAiff2() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test120.aif", new File("test120ReadAiffWithTag.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof AiffTag);
        Assert.assertTrue(((AiffTag)tag).isExistingId3Tag());
        Assert.assertTrue(tag.getFieldCount() == 10);
        assertEquals("Gary McGath", tag.getFirst(FieldKey.ARTIST));
        assertEquals("None", tag.getFirst(FieldKey.ALBUM));
        Assert.assertTrue(tag.getFirst(FieldKey.TITLE).indexOf("Short sample") == 0);
        assertEquals("This is actually a comment.", tag.getFirst(FieldKey.COMMENT));
        assertEquals("2012", tag.getFirst(FieldKey.YEAR));
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
        assertEquals(2254L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(2246L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(148230L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
    }

    @Test public void testReadAiff3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test121.aif", new File("test121ReadAiffWithoutItunesTag.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        assertEquals(2, ah.getChannelCount());
        assertEquals(44100, ah.getSampleRate());
        assertEquals(5, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals(5000000000L, ah.getDuration(TimeUnit.NANOSECONDS, true));

        System.out.println(tag);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof AiffTag);
        Assert.assertTrue(((AiffTag)tag).isExistingId3Tag());
        Assert.assertTrue(tag.getFieldCount() == 6);
        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
        assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
        assertEquals("2002", tag.getFirst(FieldKey.YEAR));
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
        assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(882054L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
    }

    @Test public void testReadAiff4() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test124.aif", new File("test124ReadAiffWithoutItunesTag.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        assertEquals(2, ah.getChannelCount());
        assertEquals(44100, ah.getSampleRate());
        assertEquals(5, ah.getDuration(TimeUnit.SECONDS, true));
        assertEquals(5000000000L, ah.getDuration(TimeUnit.NANOSECONDS, true));
        assertEquals(5.0D, ah.getDurationAsDouble(), 0.001);

        System.out.println(tag);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof AiffTag);
        Assert.assertTrue(((AiffTag)tag).isExistingId3Tag());
        Assert.assertTrue(tag.getFieldCount() == 6);
        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        assertEquals("A Rush Of Blood To The Head", tag.getFirst(FieldKey.ALBUM));
        assertEquals("Politik", tag.getFirst(FieldKey.TITLE));
        assertEquals("2002", tag.getFirst(FieldKey.YEAR));
        assertEquals("1", tag.getFirst(FieldKey.TRACK));
        assertEquals("11", tag.getFirst(FieldKey.TRACK_TOTAL));
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(12L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
    }

    @Test public void testWriteAiff3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test121.aif", new File("test121WriteAiffWithTagAddPadding.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        Assert.assertNotNull(tag);
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(882054L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());

        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        tag.setField(FieldKey.ARTIST, "Warmplay");
        assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(882054L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
        System.out.println(f.getTag());

        assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        tag.setField(FieldKey.ARTIST, "Warmplayer");
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(f.getTag());
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(882054L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
        assertEquals("Warmplayer", tag.getFirst(FieldKey.ARTIST));
    }

    @Test public void testWriteAiffWithoutTag() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test119.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        File testFile = TestUtil.copyAudioToTmp("test119.aif", new File("test119WriteAiffWithoutTag.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            Assert.assertNotNull(tag);
            System.out.println(tag);
            tag.setField(FieldKey.ARTIST, "Warmplay");
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
            f.save();

            f = AudioFileIO.read(testFile);
            tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        } catch (Exception ex) {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testDeleteAiff3() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test121.aif", new File("test121DeleteTag.aif"));
        final int oldSize = readAIFFFormSize(testFile);
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertNotNull(tag);
        Assert.assertNotNull(((AiffTag)tag).getID3Tag());
        Assert.assertFalse(tag.isEmpty());
        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        f.deleteFileTag();

        final int newSize = readAIFFFormSize(testFile);
        AudioFile f2 = AudioFileIO.read(testFile);
        Tag tag2 = f2.getTag().or(NullTag.INSTANCE);
        System.out.println(tag2);
        Assert.assertNotNull(tag2);
        Assert.assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
        Assert.assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);
    }

    @Test public void testDeleteAiff4() throws Exception {
        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File testFile = TestUtil.copyAudioToTmp("test124.aif", new File("test124DeleteTag.aif"));
        final List<String> oldChunkIds = readChunkIds(testFile);
        assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
        assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
        assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
        Assert.assertTrue(oldChunkIds.size() == 3);

        final int oldSize = readAIFFFormSize(testFile);
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(tag);
        Assert.assertNotNull(tag);
        Assert.assertNotNull(((AiffTag)tag).getID3Tag());
        Assert.assertFalse(tag.isEmpty());
        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        f.deleteFileTag();

        final int newSize = readAIFFFormSize(testFile);
        AudioFile f2 = AudioFileIO.read(testFile);
        Tag tag2 = f2.getTag().or(NullTag.INSTANCE);
        System.out.println(tag2);
        Assert.assertNotNull(tag2);
        Assert.assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
        Assert.assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);

        final List<String> newChunkIds = readChunkIds(testFile);
        assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
        assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
        Assert.assertTrue(newChunkIds.size() == 2);
    }

    @Test public void testWriteNotLastChunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test124.aif", new File("test124WriteAiffWithChunkNotAtEnd.aif"));
        final List<String> oldChunkIds = readChunkIds(testFile);
        assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
        assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
        assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
        Assert.assertTrue(oldChunkIds.size() == 3);

        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        Tag tag = f.getTag().or(NullTag.INSTANCE);
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(12L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());
        Assert.assertNotNull(tag);
        assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
        tag.setField(FieldKey.ARTIST, "Warmplay");
        assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(f.getTag());

        assertEquals("Warmplay", tag.getFirst(FieldKey.ARTIST));
        tag.setField(FieldKey.ARTIST, "Warmplayer");
        f.save();

        f = AudioFileIO.read(testFile);
        tag = f.getTag().or(NullTag.INSTANCE);
        System.out.println(f.getTag());
        assertEquals("Warmplayer", tag.getFirst(FieldKey.ARTIST));
        assertEquals(10274L, ((AiffTag)tag).getSizeOfID3TagIncludingChunkHeader());
        assertEquals(10266L, ((AiffTag)tag).getSizeOfID3TagOnly());
        assertEquals(882054L, ((AiffTag)tag).getStartLocationInFileOfId3Chunk());

        final List<String> newChunkIds = readChunkIds(testFile);
        assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
        assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
        // ID3 TAG should be at end
        assertEquals(AiffChunkType.TAG.getCode(), newChunkIds.get(2));
        Assert.assertTrue(newChunkIds.size() == 3);
    }

    private static int readAIFFFormSize(final File file) throws IOException {
        try (final RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(IffHeaderChunk.SIGNATURE_LENGTH);
            return raf.readInt();
        }
    }

    private static List<String> readChunkIds(final File file) throws IOException {
        final List<String> chunkIds = new ArrayList<>();
        try (final RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(IffHeaderChunk.HEADER_LENGTH);
            final ChunkHeader chunkHeader = new ChunkHeader(ByteOrder.BIG_ENDIAN);

            while (raf.getFilePointer() != raf.length()) {
                if (chunkHeader.readHeader(raf)) {
                    chunkIds.add(chunkHeader.getID());
                }
                raf.skipBytes((int)chunkHeader.getSize());
            }
        }
        return chunkIds;
    }

    @Test public void testWriteMetadataAifcWhenSSNDBeforeCOMMChunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test136.aif", new File("test135SSNDBeforeCOMMChunk.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);

        f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "album");
        f.save();

        f = AudioFileIO.read(testFile);
        ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        assertEquals("album", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    @Test public void testWriteMetadataAifcWithUnknonwExtraChunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test136.aif", new File("test136WriteMetadataWithUnknownExtraChunk.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        System.out.println(f.getTag());

        f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "album");
        f.save();

        f = AudioFileIO.read(testFile);
        ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        System.out.println(f.getTag());
        assertEquals("album", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    /**
     * TODO this file had bad zzzz chunk so when we write back unable to read data back because cant find
     * start of ID3 chunk
     */
    @Test public void testWriteMetadataAifcWithJunk() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test137.aif", new File("test137WriteMetadataWithJunkAtEnd.aif"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);

        f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "album");
        f.save();

        f = AudioFileIO.read(testFile);
        ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
//            assertEquals("album", f.getTag().getFirst(FieldKey.ALBUM));
    }

    @Test public void testWriteMetadataAiffWithNameAndAuthorChunks() throws Exception {
        File testFile = TestUtil.copyAudioToTmp("test138.aiff", new File("test138WriteMetadataWithNameAuthorChunks.aiff"));
        AudioFile f = AudioFileIO.read(testFile);
        AudioHeader ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);

        f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "album");
        f.save();

        f = AudioFileIO.read(testFile);
        ah = f.getAudioHeader();
        Assert.assertTrue(ah instanceof AiffAudioHeader);
        System.out.println(ah);
        assertEquals("album", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
    }

    /**
     * Starts of with Id3chunk which is odd but doesnt have padding byte but at end of file
     * so can still read, then we write to it padding bit added and when read/write again we
     * correctly work out ID3chunk is still at end of file.
     */
    @Test public void testOddLengthID3ChunkFile() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test144.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        File testFile = TestUtil.copyAudioToTmp("test144.aif", new File("test144Odd.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ACOUSTID_ID);
            f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ACOUSTID_FINGERPRINT);
            f.save();
            f = AudioFileIO.read(testFile);
            f.getTag().or(NullTag.INSTANCE).setField(FieldKey.ARTIST, "freddy");
            f.save();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ARTIST), "freddy");

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testDeleteOddLengthID3ChunkFile() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test144.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        TagOptionSingleton.getInstance().setWavOptions(WavOptions.READ_ID3_ONLY_AND_SYNC);
        TagOptionSingleton.getInstance().setWavSaveOptions(WavSaveOptions.SAVE_BOTH_AND_SYNC);
        File testFile = TestUtil.copyAudioToTmp("test144.aif", new File("test144OddDelete.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ACOUSTID_ID);
            f.getTag().or(NullTag.INSTANCE).deleteField(FieldKey.ACOUSTID_FINGERPRINT);
            f.save();
            f.deleteFileTag();
            f = AudioFileIO.read(testFile);
            System.out.println(f.getTag());
            assertEquals(0, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getStartLocationInFileOfId3Chunk());
            assertEquals(0, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getSizeOfID3TagIncludingChunkHeader());
            assertEquals(0, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getSizeOfID3TagOnly());


        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);


    }

    @Test public void testDeleteAiff4Odd() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test124.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }

        // test124.aif is special in that the ID3 chunk is right at the beginning, not the end.
        File testFile = TestUtil.copyAudioToTmp("test124.aif", new File("test124DeleteOddTag.aif"));

        try {
            final List<String> oldChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.TAG.getCode(), oldChunkIds.get(0));
            assertEquals(AiffChunkType.COMMON.getCode(), oldChunkIds.get(1));
            assertEquals(AiffChunkType.SOUND.getCode(), oldChunkIds.get(2));
            Assert.assertTrue(oldChunkIds.size() == 3);

            final int oldSize = readAIFFFormSize(testFile);
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            Assert.assertNotNull(tag);
            Assert.assertNotNull(((AiffTag)tag).getID3Tag());
            Assert.assertFalse(tag.isEmpty());
            assertEquals("Coldplay", tag.getFirst(FieldKey.ARTIST));
            f.deleteFileTag();

            final int newSize = readAIFFFormSize(testFile);
            AudioFile f2 = AudioFileIO.read(testFile);
            Tag tag2 = f2.getTag().or(NullTag.INSTANCE);
            System.out.println(tag2);
            Assert.assertNotNull(tag2);
            Assert.assertTrue(tag2.getFirst(FieldKey.ARTIST).isEmpty());
            Assert.assertFalse("FORM chunk size should have changed, but hasn't.", oldSize == newSize);

            final List<String> newChunkIds = readChunkIds(testFile);
            assertEquals(AiffChunkType.COMMON.getCode(), newChunkIds.get(0));
            assertEquals(AiffChunkType.SOUND.getCode(), newChunkIds.get(1));
            Assert.assertTrue(newChunkIds.size() == 2);

        } catch (Exception ex) {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteMetadataAifcWithUnknownExtraChunkID3DatSizeOdd() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test136.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }


        File
                testFile =
                TestUtil.copyAudioToTmp("test136.aif", new File("test136WriteMetadataWithUnknownExtraChunkID3DatSizeOdd.aif"));
        try {
            TagOptionSingleton.getInstance().setID3V2Version(ID3V2Version.ID3_V22);
            AudioFile f = AudioFileIO.read(testFile);
            AudioHeader ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());

            f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "albums");
            f.save();
            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());
            assertEquals("albums", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals(122, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getStartLocationInFileOfId3Chunk());
            assertEquals(154, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getEndLocationInFileOfId3Chunk());


            f.getTagOrSetNewDefault().setField(FieldKey.ALBUM, "albuks");
            f.save();
            f = AudioFileIO.read(testFile);
            ah = f.getAudioHeader();
            Assert.assertTrue(ah instanceof AiffAudioHeader);
            System.out.println(ah);
            System.out.println(f.getTag());
            assertEquals("albuks", f.getTag().or(NullTag.INSTANCE).getFirst(FieldKey.ALBUM));
            assertEquals(122, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getStartLocationInFileOfId3Chunk());
            assertEquals(154, ((AiffTag)f.getTag().or(NullTag.INSTANCE)).getEndLocationInFileOfId3Chunk());

        } catch (Exception e) {
            e.printStackTrace();
            exceptionCaught = e;
        }
        Assert.assertNull(exceptionCaught);
    }

    @Test public void testWriteWriteProtectedFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckDisabled("test121.aif");
    }

    @Test public void testWriteWriteProtectedFileWithCheckEnabled() throws Exception {

        FilePermissionsTest.runWriteWriteProtectedFileWithCheckEnabled("test121.aif");
    }

    @Test public void testWriteReadOnlyFileWithCheckDisabled() throws Exception {

        FilePermissionsTest.runWriteReadOnlyFileWithCheckDisabled("test121.aif");
    }

    @Test public void testDeleteArtworkField() {
        Exception exceptionCaught = null;

        File orig = new File("testdata", "test157.aif");
        if (!orig.isFile()) {
            System.err.println("Unable to test file - not available");
            return;
        }


        File testFile = TestUtil.copyAudioToTmp("test157.aif", new File("testDeleteArtworkField.aif"));
        try {
            AudioFile f = AudioFileIO.read(testFile);
            Tag tag = f.getTag().or(NullTag.INSTANCE);
            System.out.println(tag);
            Assert.assertNotNull(tag);
            Assert.assertTrue(tag instanceof AiffTag);
            Assert.assertTrue(((AiffTag)tag).isExistingId3Tag());
            assertEquals(2, tag.getArtworkList().size());

            tag.deleteArtwork();
            f.save();

            AudioFile updatedFile = AudioFileIO.read(testFile);
            Tag updatedTag = updatedFile.getTag().or(NullTag.INSTANCE);

            assertEquals(0, updatedTag.getArtworkList().size());

        } catch (Exception ex) {
            ex.printStackTrace();
            exceptionCaught = ex;
        }
        Assert.assertNull(exceptionCaught);
    }

}
